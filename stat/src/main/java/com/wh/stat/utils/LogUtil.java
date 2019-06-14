package com.wh.stat.utils;

import android.util.Log;

import com.wh.stat.HBHStatistical;

public class LogUtil {
    public static final String TAG = HBHStatistical.class.getSimpleName();
    public static boolean debugModel = HBHStatistical.getInstance().getConfig().debugModel;

    public static void e(String msg) {
        if (debugModel) {
            Log.e(TAG,msg);
        }
    }
}
