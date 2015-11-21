package com.ruibin.actions;

import android.app.Activity;
import android.os.Bundle;
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
        for (int i = 0; i < 10; i++) {
            mActions.add(new Action("Action Item " + i, "Description " + i, 0, 0, 0));
        }
        mCallback = new ActionCallback();

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(mActions, mCallback);
        mRecyclerView.setAdapter(mAdapter);
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
