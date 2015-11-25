package com.ruibin.actions;

import android.app.Application;

public class ActionsApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActionController.init(this);
    }
}
