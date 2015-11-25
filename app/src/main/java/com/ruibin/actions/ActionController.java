package com.ruibin.actions;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class ActionController {
    public static final String ACTION_DATABASE_CHANGED = ActionController.class.getPackage() + ".ACTION_DATABASE_CHANGED";

    public static ActionController instance = new ActionController();

    public static ActionController getInstance() {
        return instance;
    }

    public static void init(Context context) {
        instance.context = context;
        instance.broadcastManager = LocalBroadcastManager.getInstance(context);

        DatabaseController.init(context);
    }

    private Context context;

    private LocalBroadcastManager broadcastManager;

    public void save(Action action) {
        if (action.getId() > 0) {
            DatabaseController.getInstance().update(action);
        } else {
            DatabaseController.getInstance().add(action);
        }

        Intent intent = new Intent(ACTION_DATABASE_CHANGED);
        broadcastManager.sendBroadcast(intent);
    }

    public Action find(int id) {
        return DatabaseController.getInstance().find(id);
    }
}
