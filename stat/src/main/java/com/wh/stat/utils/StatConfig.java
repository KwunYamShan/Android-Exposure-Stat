package com.wh.stat.utils;

import android.content.Context;
import android.graphics.Rect;

public class StatConfig {
    public static final int REPORT_DELAYED = 1;
    /**
     * 用来作为上报标示的tag名
     */
    public Context context;
    public int top;
    public int bottom;
    public int left;
    public int right;
    public Rect mScreenRect;
    public long delayTime = 5000;
    public boolean debugModel = true;
}
