package com.wh.statdemo;

import android.app.Application;
import android.util.Log;
import android.view.View;

import com.wh.stat.HBHStatistical;
import com.wh.stat.utils.StatBuilder;
import com.wh.stat.lifecycle.IContext;

import java.util.ArrayList;
import java.util.Iterator;

public class App extends Application implements IContext {
    private static final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        //HBHStatistical.getInstance().initialize(this);
        new StatBuilder(this)
                .setDuration(5000)
                .setDebugModle(true)
                .setViewResultListener(new HBHStatistical.ViewResultListener() {
                    @Override
                    public void onViewResult(ArrayList<View> displayViews) {
                        //不需要非空判断
                        Iterator iterator = displayViews.iterator();
                        while (iterator.hasNext()) {
                            View view = (View) iterator.next();
                            String mark = (String) view.getTag(HBHStatistical.getInstance().getTagId());
                            Log.e(TAG, "已上报：id:" + view.getId() + "     , 数据:" + mark);
                        }
                    }
                })
                .create();
    }
}
