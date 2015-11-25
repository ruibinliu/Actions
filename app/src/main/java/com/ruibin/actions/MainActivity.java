package com.ruibin.actions;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Action> mActions;
    private MyAdapter.Callback mCallback;
    private LocalBroadcastManager broadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mActions = new ArrayList<Action>();
        mCallback = new ActionCallback();

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(mActions, mCallback);
        mRecyclerView.setAdapter(mAdapter);

        reload();

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
        mActions.clear();
        mActions.addAll(DatabaseController.getInstance().select());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                EditActivity.start(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class ActionCallback implements MyAdapter.Callback {
        @Override
        public void onActionClicked(Action action) {
            DetailActivity.start(MainActivity.this, action);
        }
    }

    private void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
