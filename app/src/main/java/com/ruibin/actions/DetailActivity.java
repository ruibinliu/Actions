package com.ruibin.actions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailActivity extends Activity {
    private static final String EXTRA_ACTION = "action";

    private Action mAction;

    private TextView mTitleText;
    private TextView mDescriptionText;

    public static void start(Context context, Action action) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_ACTION, action);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitleText = (TextView) findViewById(R.id.title);
        mDescriptionText = (TextView) findViewById(R.id.description);

        Intent intent = getIntent();
        mAction = (Action) intent.getSerializableExtra(EXTRA_ACTION);

        mTitleText.setText(mAction.getTitle());
        mDescriptionText.setText(mAction.getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                EditActivity.start(this, mAction);
                break;
        }

        return true;
    }
}
