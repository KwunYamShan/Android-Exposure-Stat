package com.wh.stat.utils;

import android.content.Context;
import android.graphics.Rect;

/**
 * @author wh.
 * @time 2019/6/14.
 * @explain 配置类
 */
public class StatConfig {
    //constant
    public static final int REPORT_DELAYED = 1;

    public Context context;
    //用来标记需要曝光的view
    public int tagId;
    //屏幕宽高的矩形
    public Rect mScreenRect;
    //曝光延时的时间
    public long delayTime = 5000;
    //模式
    public boolean debugModel;
    //是否需要自动曝光
    public boolean isAuto;
    //view可被覆盖的范围，1-100
    public int coverRange;

}
