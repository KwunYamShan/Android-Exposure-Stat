package com.wh.stat.utils;

import android.content.Context;
import android.graphics.Rect;

/**
 * @author wh.
 * @time 2019/6/14.
 * @explain 配置类
 */
public class StatConfig {
    public static final int REPORT_DELAYED = 1;
    public Context context;
    public int tagId;
    public int top;
    public int bottom;
    public int left;
    public int right;
    public Rect mScreenRect;
    public long delayTime = 5000;
    public boolean debugModel ;
    public int coverRange;
}
