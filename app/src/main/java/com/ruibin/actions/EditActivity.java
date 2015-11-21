package com.ruibin.actions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends Activity {
    private static final String EXTRA_ACTION = "action";

    private EditText mTitleText;
    private EditText mDescriptionText;

    private Action mAction;

    private boolean mIsEdit;

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Action action) {
        Intent intent = new Intent(context, EditActivity.class);
        if (action != null) {
            intent.putExtra(EXTRA_ACTION, action);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mTitleText = (EditText) findViewById(R.id.title);
        mDescriptionText = (EditText) findViewById(R.id.description);

        Intent intent = getIntent();
        mAction = (Action) intent.getSerializableExtra(EXTRA_ACTION);
        if (!(mIsEdit = (mAction != null))) {
            mAction = new Action();
        }

        mTitleText.setText(mAction.getTitle());
        mDescriptionText.setText(mAction.getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveAction();
                finish();
                break;
            case R.id.action_discard:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveAction() {
        if (mIsEdit) {
            // TODO Update
            makeToast("Update");
        } else {
            // TODO Insert
            makeToast("Insert");
        }
    }

    private void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
