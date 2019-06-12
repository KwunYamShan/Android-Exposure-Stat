package com.wh.statdemo;

import android.app.Application;
import android.content.Context;

import com.wh.stat.utils.StatBuilder;
import com.wh.stat.lifecycle.IContext;

public class App extends Application implements IContext {

    @Override
    public void onCreate() {
        super.onCreate();
        //HBHStatistical.getInstance().initialize(this);
        new StatBuilder(this)
                .setDuration(5000)
                .setDebugModle(true)
                .setTabMark("mark").create();
    }
}
