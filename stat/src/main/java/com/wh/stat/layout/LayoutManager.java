package com.wh.stat.layout;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import java.util.List;

/**
 * @author KwunYamShan.
 * @time 2019/6/14.
 * @explain
 */
public class LayoutManager {

    private static List<String> mExposureActivityList;
    public static void wrap(Activity activity) {
        if (isWrap(activity)) {
            return;
        }
        Window window = activity.getWindow();
        View decorView = window.getDecorView();
        if (decorView != null && decorView instanceof ViewGroup) {
            StatLayout statLayout = new StatLayout(activity);
            ((ViewGroup) decorView).addView(statLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    /**
     * 是否要添加外部布局
     * true:不添加
     * false：添加
     */
    private static boolean isWrap(Activity activity) {
        if (mExposureActivityList != null && !mExposureActivityList.isEmpty()) {
            String canonicalName = activity.getClass().getCanonicalName();
            if (canonicalName != null && !mExposureActivityList
                .contains(canonicalName)) {
                return true;
            }
        }
        return false;
    }

    public static void setExposureActivityList(List<String> exposureActivityList) {
       mExposureActivityList = exposureActivityList;
    }
}
