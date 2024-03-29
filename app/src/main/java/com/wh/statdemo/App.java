package com.wh.statdemo;

import android.app.Application;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wh.stat.HBHStatistical;
import com.wh.stat.utils.StatBuilder;
import com.wh.stat.lifecycle.IContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author KwunYamShan.
 * @time 2019/6/25.
 * @explain
 */
public class App extends Application implements IContext {
    private static final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
      List<String> list = new ArrayList<>();
      list.add("com.wh.statdemo.TestActivity");
      list.add("com.wh.statdemo.MainActivity");
        //Application中初始化，Application需要实现IContext
        new StatBuilder(this)
                //对每个需要曝光统计的View以setTag的方式进行标识，标识需自行定义
                .setTagId(ReportUtil.getInstance().getMarkTag())
                //设置时长:view显示在页面中x毫秒后算一次有效曝光
                .setDuration(5000)
                //设置有效曝光视图显示的范围，范围：0-100  例：80代表该视图显示自身总面积的80%以上都可以算作是有效曝光的视图
                .setValidRange(80)
                //设置是否为线上版本，目前的区别就是是否需要打日志
                .setDebugModle(true)
                //是否需要自动执行曝光任务
                .setAutoStat(true)
                //是否可以被重复曝光
                .setRepeat(true)
                //设置只统计集合中activity的view，不设置默认统计全部页面的view
                .setExposureActivities(list)
                //返回已曝光的view集合
                .setViewResultListener(new HBHStatistical.ViewResultListener() {
                    @Override
                    public void onViewResult(ArrayList<View> displayViews) {
                        //displayViews不需要做非空判断
                        Iterator iterator = displayViews.iterator();
                        while (iterator.hasNext()) {
                            View view = (View) iterator.next();
                            String block = (String) view.getTag(ReportUtil.getInstance().getBlockNameTag());
                            String item = (String) view.getTag(ReportUtil.getInstance().getItemNameTag());
                            String price = (String) view.getTag(ReportUtil.getInstance().getPriceTag());
                            Log.e(TAG, "曝光统计：id:" + view.getId() + "     , 数据: " + block + "   " + item + "    " + price);
                            Toast.makeText(App.this, "曝光事件被触发，在控制台搜索“曝光统计”查看log", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .create();
    }
}
