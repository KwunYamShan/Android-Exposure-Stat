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
        //Application中初始化，Application需要实现IContext
        new StatBuilder(this)
                //对每个需要曝光统计的View以setTag的方式进行标识，标识需自行定义
                .setTagId(ReportUtil.getInstance().getMarkTag())
                //设置时长:view显示在页面中x毫秒后算一次有效曝光
                .setDuration(5000)
                //设置可被覆盖的范围，范围：1-100  20代表view被覆盖或显示不全20%以内依然可以算作是有效曝光的view
                .setCoverRange(20)
                //设置是否为线上版本，目前的区别就是是否需要打日志
                .setDebugModle(true)
                //返回已曝光的view集合
                .setViewResultListener(new HBHStatistical.ViewResultListener() {
                    @Override
                    public void onViewResult(ArrayList<View> displayViews) {
                        //displayViews不需要做非空判断
                        Iterator iterator = displayViews.iterator();
                        while (iterator.hasNext()) {
                            View view = (View) iterator.next();
                            String block = (String)view.getTag(ReportUtil.getInstance().getBlockNameTag());
                            String item =  (String)view.getTag(ReportUtil.getInstance().getItemNameTag());
                            String price =  (String)view.getTag(ReportUtil.getInstance().getPriceTag());
                            Log.e(TAG, "曝光统计：id:" + view.getId() + "     , 数据: " +block+"   "+item+"    "+price);
                        }
                    }
                })
                .create();
    }
}
