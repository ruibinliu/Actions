package com.ruibin.actions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    private static final String EXTRA_ACTION = "action";

    private Action mAction;

    private Toolbar mToolbar;
    private TextView mTitleText;
    private TextView mDescriptionText;

    private LocalBroadcastManager broadcastManager;

    public static void start(Context context, Action action) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_ACTION, action);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(mMenuItemClickListener);

        mTitleText = (TextView) findViewById(R.id.title);
        mDescriptionText = (TextView) findViewById(R.id.description);

        Intent intent = getIntent();
        mAction = (Action) intent.getSerializableExtra(EXTRA_ACTION);

        mTitleText.setText(mAction.getTitle());
        mDescriptionText.setText(mAction.getDescription());

        broadcastManager = LocalBroadcastManager.getInstance(this);

        IntentFilter filter = new IntentFilter(ActionController.ACTION_DATABASE_CHANGED);
        broadcastManager.registerReceiver(databaseChangedReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        broadcastManager.unregisterReceiver(databaseChangedReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver databaseChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            reload();
        }
    };

    private void reload() {
        mAction = ActionController.getInstance().find(mAction.getId());
        mTitleText.setText(mAction.getTitle());
        mDescriptionText.setText(mAction.getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    private Toolbar.OnMenuItemClickListener mMenuItemClickListener= new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_edit:
                    EditActivity.start(DetailActivity.this, mAction);
                    break;
                default:
                    break;
            }

            return true;
        }
    };
}
