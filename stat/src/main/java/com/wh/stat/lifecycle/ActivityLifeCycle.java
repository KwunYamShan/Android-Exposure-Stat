package com.wh.stat.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.wh.stat.HBHStatistical;
import com.wh.stat.layout.LayoutManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * @author wh.
 * @time 2019/6/6.
 * @explain 该类用于监听项目中所有Activity的生命周期<p />
 * 需要在[Application]中初始化，以便于能够及时监听所有的[Activity]
 */
public class ActivityLifeCycle implements Application.ActivityLifecycleCallbacks {

    private int created;
    private static int resumed;
    private static int paused;
    private static int started;
    private static int stopped;
    private int destroyed;
    private ArrayList<WeakReference<Activity>> refs = new ArrayList<>();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        ++created;
        if (activity != null) {
            LayoutManager.wrap(activity);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;

        //判断app是否为从后台切换到前台
        if (refs.isEmpty()) {
            //
        }

        if (activity != null) {
            refs.add(new WeakReference(activity));
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ++resumed;
            HBHStatistical.getInstance().bind(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ++paused;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
        if (activity != null) {
            for (WeakReference ref : refs) {
                if (ref.get() == activity) {
                    refs.remove(ref);
                    break;
                }
            }
        }
        //前台切换到后台
        if (refs.isEmpty()) {
            if (!activity.isFinishing()) {
                //
            }
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        ++destroyed;
        //退出app
        if (destroyed == created) {
            created = started = resumed = paused = stopped = destroyed = 0;
        }
    }

    public static boolean isApplicationInForeground() {
        // 当所有 Activity 的状态中处于 resumed 的大于 paused 状态的，即可认为有Activity处于前台状态中
        return started > stopped;
    }

}
