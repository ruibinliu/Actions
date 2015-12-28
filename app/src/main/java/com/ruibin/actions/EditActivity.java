package com.ruibin.actions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {
    private static final String EXTRA_ACTION = "action";

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

    private Toolbar mToolbar;

    private EditText mTitleText;

    private EditText mDescriptionText;

    private Action mAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(mMenuItemClickListener);

        mTitleText = (EditText) findViewById(R.id.title);
        mDescriptionText = (EditText) findViewById(R.id.description);

        Intent intent = getIntent();
        mAction = (Action) intent.getSerializableExtra(EXTRA_ACTION);
        if (mAction == null) {
            mAction = new Action();
        }

        mTitleText.setText(mAction.getTitle());
        mDescriptionText.setText(mAction.getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    private Toolbar.OnMenuItemClickListener mMenuItemClickListener= new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_save:
                    mAction.setTitle(mTitleText.getText().toString());
                    mAction.setDescription(mDescriptionText.getText().toString());
                    ActionController.getInstance().save(mAction);
                    makeToast(R.string.save_success);
                    finish();
                    break;
                case R.id.action_discard:
                    finish();
                    break;
                default:
                    break;
            }

            return true;
        }
    };

    private void makeToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
    }

    private void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
