package com.wh.stat.layout;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class LayoutManager {
    public static void wrap(Activity activity){
        Window window = activity.getWindow();
        View decorView = window.getDecorView();
        if (decorView != null && decorView instanceof ViewGroup){
            StatLayout trackLayout = new StatLayout(activity);
            ((ViewGroup) decorView).addView(trackLayout,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }
}
