
package com.ruibin.actions;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class ActionsTest {

    private static final String PACKAGE_NAME = "com.ruibin.actions";

    private static final int LAUNCH_TIMEOUT = 5000;

    private static final String TITLE_TO_BE_TYPED = "Automatic Test";
    private static final String DESCRIPTION_TO_BE_TYPED = "Verify add action function.";

    private UiDevice mDevice;

    @Before
    public void initialize() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        lunchMainActivityFromHomeScreen();
    }

    @Test
    public void testActions_addAction() {
        // Clear all action in database.
        ActionController.getInstance().delete();

        // Lunch the edit activity to create a new Action.
        mDevice.findObject(By.res(PACKAGE_NAME, "action_new")).click();

        // Wait for the activity to appear
        mDevice.wait(Until.hasObject(By.pkg(PACKAGE_NAME).clazz(EditActivity.class)),
                LAUNCH_TIMEOUT);

        // Type in the title and description then press the 'save' button.
        mDevice.findObject(By.res(PACKAGE_NAME, "title")).setText(TITLE_TO_BE_TYPED);
        mDevice.findObject(By.res(PACKAGE_NAME, "description")).setText(DESCRIPTION_TO_BE_TYPED);
        mDevice.findObject(By.res(PACKAGE_NAME, "action_save")).click();

        // Wait for the activity to appear
        mDevice.wait(Until.hasObject(By.pkg(PACKAGE_NAME).clazz(MainActivity.class)),
                LAUNCH_TIMEOUT);

        UiObject uiObject = mDevice.findObject(new UiSelector()
                .className("android.support.v7.widget.RecyclerView")
                .instance(0)
                .childSelector(new UiSelector().className("android.widget.TextView")));

        try {
            assertTrue(TITLE_TO_BE_TYPED.equals(uiObject.getText()));
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testActions_achieveAction() {
        UiObject uiObject = mDevice.findObject(new UiSelector()
                .className("android.support.v7.widget.RecyclerView")
                .instance(0)
                .childSelector(new UiSelector().className("android.widget.CheckBox")));

        try {
            // Mark the action as 'achieved' status
            uiObject.click();

            assertTrue(uiObject.isChecked());
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        // Finish the activity, cause it to be destroy.
        mDevice.pressBack();

        lunchMainActivityFromHomeScreen();

        uiObject = mDevice.findObject(new UiSelector()
                .className("android.support.v7.widget.RecyclerView")
                .instance(0)
                .childSelector(new UiSelector().className("android.widget.CheckBox")));

        try {
            assertTrue(uiObject.isChecked());
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void lunchMainActivityFromHomeScreen() {
        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        // Launch the blueprint app
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(PACKAGE_NAME);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(PACKAGE_NAME).depth(0)), LAUNCH_TIMEOUT);
    }

    /**
     * Uses package manager to find the package name of the device launcher. Usually this package
     * is "com.android.launcher" but can be different at times. This is a generic solution which
     * works on all platforms.`
     */
    private String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }
}
