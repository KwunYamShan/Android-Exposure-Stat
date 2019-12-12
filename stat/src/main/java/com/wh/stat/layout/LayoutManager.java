package com.wh.stat.layout;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.wh.stat.HBHStatistical;

/**
 * @author KwunYamShan.
 * @time 2019/6/14.
 * @explain
 */
public class LayoutManager {
    public static void wrap(Activity activity) {
        if (HBHStatistical.getInstance().isNeedExpoActivity(activity)) {
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            if (decorView != null && decorView instanceof ViewGroup) {
                StatLayout statLayout = new StatLayout(activity);
                ((ViewGroup) decorView).addView(statLayout,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
            }
        }
    }
}
