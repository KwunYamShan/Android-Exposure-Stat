package com.wh.stat.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.wh.stat.HBHStatistical;
import com.wh.stat.layout.LayoutManager;

/**
 * @author KwunYamShan.
 * @time 2019/6/6.
 * @explain 该类用于监听项目中所有Activity的生命周期<p />
 * 需要在[Application]中初始化，以便于能够及时监听所有的[Activity]
 */
public class ActivityLifeCycle implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (activity != null) {
            LayoutManager.wrap(activity);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        HBHStatistical.getInstance().bind(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

}
