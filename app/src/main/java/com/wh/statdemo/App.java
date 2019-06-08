package com.wh.statdemo;

import android.app.Application;

import com.wh.stat.HBHStatistical;
import com.wh.stat.lifecycle.IContext;

public class App extends Application implements IContext {
    @Override
    public void onCreate() {
        super.onCreate();
        HBHStatistical.getInstance().initialize(this);
    }
}
