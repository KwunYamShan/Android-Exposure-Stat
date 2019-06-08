package com.wh.stat;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;

import com.wh.stat.lifecycle.ActivityLifeCycle;
import com.wh.stat.lifecycle.IContext;

import java.util.ArrayList;
import java.util.Iterator;

import static android.view.View.VISIBLE;

public class HBHStatistical {

    private static final String TAG = HBHStatistical.class.getSimpleName();
    private ActivityLifeCycle mActivityLifeCycle;
    private static View mRootView;
    private Rect mScreenRect;

    private static class HelperHolder {
        public static final HBHStatistical mStatistical = new HBHStatistical();
    }

    public static HBHStatistical getInstance() {
        return HelperHolder.mStatistical;
    }

    private Rect mRect = new Rect();


    public int getMarkId() {
        return R.id.mark;
    }


    public void initialize(IContext app) {
        mActivityLifeCycle = new ActivityLifeCycle();
        app.registerActivityLifecycleCallbacks(mActivityLifeCycle);
    }

    public boolean isVisible(View view) {
        return view.getVisibility() == VISIBLE;
    }

    /**
     * 判断一个View是否包含了对应的坐标
     */
    public boolean hitPoint(View view) {
        view.getGlobalVisibleRect(mRect);
        Log.e(TAG, "mRect:"+mRect.left + "," + mRect.top + "," + mRect.right + "," + mRect.bottom);
        Log.e(TAG, "mScreenRect:"+mScreenRect.left + "," + mScreenRect.top + "," + mScreenRect.right + "," + mScreenRect.bottom);
        boolean contains = mScreenRect.contains(mRect);
        Log.e(TAG, "contains:" + contains);
        return contains;
    }

    /**
     * 根据当前坐标值查找对应位置的View
     * <p>
     * 该方法为递归实现，如果传入的布局为[ViewGroup]，则执行递归；否则，则对[View]进行判断。
     * 在递归过程中，如果发现[ViewGroup]中没有合适的[View]，则会对[ViewGroup]本身进行判断，
     * 如果[ViewGroup]本身可点击，则会将[ViewGroup]当做点击的[View]
     */
    public ArrayList<View> findHitView(View parent) {
        ArrayList<View> hitViews = new ArrayList<>();
        if (isVisible(parent) && hitPoint(parent)) {
            // 仅在parent可见，并且命中了点击位置时才对该parent进行判断/递归查找，减少查找的次数，提高效率
            if (parent instanceof AdapterView) {
                hitViews.add(parent);
                // 由于在AdapterView中可能会有局部的View可点击的情况，故此处需要对AdapterView中的子View进行递归查询
                // 如果子View可点击，则只会触发子View的点击，而不会触发AdapterView的点击
                findHitViewsInGroup((ViewGroup) parent, hitViews);
            } else if (parent instanceof ViewGroup) {
                hitViews.add(parent);
                // 如果是ViewGroup，则去对其子View进行查询
                findHitViewsInGroup((ViewGroup) parent, hitViews);
            } else if (!(parent instanceof ViewGroup) /*&& parent.isClickable()*/) {
                // 如果parent本身不是ViewGroup，且可点击，则当做可触发点击事件的View返回
                hitViews.add(parent);
            }
        }
        return hitViews;
    }

    /**
     * 对ViewGroup中的所有子View进行查询，如果子View中没有符合条件的View
     * 则会对父View进行检查，如果父View可点击，则List中会包含父View
     */
    public void findHitViewsInGroup(ViewGroup parent, ArrayList<View> hitViews) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            ArrayList<View> hitChildren = findHitView(child);
            if (!hitChildren.isEmpty()) {
                hitViews.addAll(hitChildren);
            } else if (isVisible(child) /*&& child.isClickable()*/ && hitPoint(child)) {
                hitViews.add(child);
            }
        }
    }

    public void bind(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindow().getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        mRootView = activity.getWindow().getDecorView().getRootView();
        mScreenRect = new Rect(0, 0, metrics.widthPixels, metrics.heightPixels);

        mRootView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                Log.e(TAG, "--onScrollChanged");
            }
        });
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.e(TAG, "--onGlobalLayout");
            }
        });
        mRootView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                //用来计算状态栏
                Log.e(TAG, "--onSystemUiVisibilityChange");
            }
        });
        mRootView.getViewTreeObserver().addOnWindowAttachListener(new ViewTreeObserver.OnWindowAttachListener() {
            @Override
            public void onWindowAttached() {
                Log.e(TAG, "--onWindowAttached");
                HBHStatistical.getInstance().delayed();
            }

            @Override
            public void onWindowDetached() {
                Log.e(TAG, "--onWindowDetached");
            }
        });
        mRootView.getViewTreeObserver().addOnTouchModeChangeListener(new ViewTreeObserver.OnTouchModeChangeListener() {
            @Override
            public void onTouchModeChanged(boolean isInTouchMode) {
                Log.e(TAG, "--onTouchModeChanged:"+isInTouchMode);
            }
        });

        mRootView.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                Log.e(TAG, "--onGlobalFocusChanged:");
            }
        });
        //此方法无效  会被子view拦截掉
//        mRootView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP){
//                    HBHStatistical.getInstance().delayed();
//                }else if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN){
//                    HBHStatistical.getInstance().cancel();
//                }
//                return false;
//            }
//        });
    }

    /**
     * 上报
     */
    public void report() {
        ArrayList<View> hitViews = findHitView(mRootView);
        Iterator iterator = hitViews.iterator();
        while (iterator.hasNext()) {
            View view = (View) iterator.next();
            String mark = (String) view.getTag(getMarkId());
            int id = view.getId();
            if (mark != null) {
                Log.e(TAG, "上报， mark:" + mark + ",id:" + id);
            }
            Log.e(TAG, "id:" + id);
        }
    }

    /**
     * 计算
     */
    public void delayed() {
        cancel();
        handler.sendEmptyMessageDelayed(1, 5000);
    }

    /**
     * 取消上报
     */
    public void cancel(){
        handler.removeCallbacksAndMessages(null);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                report();
            }
        }
    };
}
