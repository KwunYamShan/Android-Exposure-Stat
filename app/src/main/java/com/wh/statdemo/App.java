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
    public static int mStatTagId = R.id.mark;

    @Override
    public void onCreate() {
        super.onCreate();
        //Application中初始化，Application需要实现IContext
        new StatBuilder(this)
                //对每个需要曝光统计的View以setTag的方式进行标识，标识需自行定义
                .setTagId(mStatTagId)
                //设置时长:view显示在页面中x毫秒后算一次有效曝光
                .setDuration(5000)
                //设置曝光的屏幕范围
                //.setSubRange(0,0,screenWidth,screenHeight)
                //设置是否为线上版本，目前的区别就是是否需要打日志
                .setDebugModle(true)
                //返回已曝光的view集合
                .setViewResultListener(new HBHStatistical.ViewResultListener() {
                    @Override
                    public void onViewResult(ArrayList<View> displayViews) {
                        //不需要非空判断
                        Iterator iterator = displayViews.iterator();
                        while (iterator.hasNext()) {
                            View view = (View) iterator.next();
                            String mark = (String) view.getTag(mStatTagId);
                            Log.e(TAG, "曝光统计：id:" + view.getId() + "     , 数据:" + mark);
                        }
                    }
                })
                .create();
    }
}
