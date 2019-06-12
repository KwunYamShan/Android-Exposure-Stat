package com.wh.stat;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;

import com.wh.stat.lifecycle.ActivityLifeCycle;
import com.wh.stat.lifecycle.IContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

import static android.view.View.VISIBLE;

public class HBHStatistical {

    public static final String TAG = HBHStatistical.class.getSimpleName();
    private ActivityLifeCycle mActivityLifeCycle;
    private static View mRootView;
    private Rect mScreenRect;
    private static final int REPORT_DELAYED = 1;

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
        Log.e(TAG, "mRect:" + mRect.left + "," + mRect.top + "," + mRect.right + "," + mRect.bottom);
        Log.e(TAG, "mScreenRect:" + mScreenRect.left + "," + mScreenRect.top + "," + mScreenRect.right + "," + mScreenRect.bottom);
        boolean contains = mScreenRect.contains(mRect);
        Log.e(TAG, "contains:" + contains);
        String mark = (String) view.getTag(getMarkId());
        Log.e(TAG, "遍历的id:" + view.getId()+"     , 数据:" + mark);
        return contains;
    }

    /**
     * 该方法为递归实现，如果传入的布局为[ViewGroup]，则执行递归添加；否则直接添加出现在屏幕中的view
     */
    public ArrayList<View> findHitView(View parent) {
        ArrayList<View> hitViews = new ArrayList<>();
        if (isVisible(parent) && hitPoint(parent)) {
            // 仅在parent可见，并且命中了点击位置时才对该parent进行判断/递归查找，减少查找的次数，提高效率
            if (parent instanceof AdapterView) {
                hitViews.add(parent);
                // 由于在AdapterView中可能会有上报局部View的情况，故此处需要对AdapterView中的子View进行递归查询
                // 如果子View可点击，则只会触发子View的点击，而不会触发AdapterView的点击
                findHitViewsInGroup((ViewGroup) parent, hitViews);
            } else if (parent instanceof ViewGroup) {
                hitViews.add(parent);
                // 如果是ViewGroup，则去对其子View进行查询
                findHitViewsInGroup((ViewGroup) parent, hitViews);
            } else  {
                // 如果parent本身不是ViewGroup，则直接将View返回
                hitViews.add(parent);
            }
        }
        return hitViews;
    }

    /**
     * 对ViewGroup中的所有子View进行查询，如果子View中没有符合条件的View
     * 则会对父View进行检查，如果父View可见，并且在屏幕范围内，则List中会包含父View
     */
    public void findHitViewsInGroup(ViewGroup parent, ArrayList<View> hitViews) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            ArrayList<View> hitChildren = findHitView(child);
            if (!hitChildren.isEmpty()) {
                hitViews.addAll(hitChildren);
            } else if (isVisible(child) && hitPoint(child)) {
                hitViews.add(child);
            }
        }
    }

    public void bind(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindow().getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        mRootView = activity.getWindow().getDecorView().getRootView();
        mScreenRect = new Rect(0, 0, metrics.widthPixels, metrics.heightPixels);

//        mRootView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                Log.e(TAG, "--addOnScrollChangedListener");
//            }
//        });
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.e(TAG, "--addOnGlobalLayoutListener");
            }
        });
        mRootView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                //用来计算状态栏
                Log.e(TAG, "--setOnSystemUiVisibilityChangeListener");
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
                HBHStatistical.getInstance().cancel();
            }
        });
        mRootView.getViewTreeObserver().addOnTouchModeChangeListener(new ViewTreeObserver.OnTouchModeChangeListener() {
            @Override
            public void onTouchModeChanged(boolean isInTouchMode) {
                Log.e(TAG, "--addOnTouchModeChangeListener:" + isInTouchMode);
            }
        });

        mRootView.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                Log.e(TAG, "--addOnGlobalFocusChangeListener:");
            }
        });

        mRootView.getViewTreeObserver().addOnWindowFocusChangeListener(new ViewTreeObserver.OnWindowFocusChangeListener() {
            @Override
            public void onWindowFocusChanged(boolean hasFocus) {
                Log.e(TAG, "--onWindowFocusChanged:");
            }
        });
        //        //此方法无效  会被子view拦截掉
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
                Log.e(TAG, "已上报：id:" + id+"     , 数据:" + mark);
            }else{
                Log.e(TAG, "非上报：id:" + id);
            }
        }
    }

    /**
     * 计算
     */
    public void delayed() {
        cancel();
        handler.sendEmptyMessageDelayed(REPORT_DELAYED, 5000);
    }

    /**
     * 取消上报
     */
    public void cancel() {
        handler.removeCallbacksAndMessages(null);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == REPORT_DELAYED) {
                report();
            }
        }
    };
    private Field mListenerInfoField;
    private Field mOnTouchListenerField;
    /**
     * 通过反射获取mListenerInfo
     * @param view
     * @return
     */
    private Field getListenerInfoField(View view){
        Field declaredField = null;
        try {
            // 通过反射拿到mListenerInfo，并且设置为可访问（用于后续替换点击事件）
            Class viewClazz = Class.forName("android.view.View");
            declaredField = viewClazz.getDeclaredField("mListenerInfo");
            if (!declaredField.isAccessible()) {
                declaredField.setAccessible(true);
            }
            mListenerInfoField = declaredField;
            return declaredField;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return declaredField;
    }
    private Field getOnTouchListenerField(View view){
        Field clickInfo = null;
        try {
            Object viewInfo = getListenerInfoField(view).get(view);
            if (viewInfo != null){

                clickInfo = viewInfo.getClass().getDeclaredField("mOnTouchListener");
                if (!clickInfo.isAccessible()) {
                    clickInfo.setAccessible(true);
                }
                mOnTouchListenerField = clickInfo;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return clickInfo;
    }
    public void wrapTouch(View view , MotionEvent motionEvent){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            wrapOnTouch(view,motionEvent);
        }else {
            if (getListenerInfoField(view) != null && getOnTouchListenerField(view) != null){
                wrapOnTouch(view,motionEvent);
            }
        }
    }

    private void wrapOnTouch(View view,MotionEvent motionEvent){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            Object viewInfo = null;
            Field touchInfo = null;
            try {
                viewInfo = getListenerInfoField(view).get(view);
                if (viewInfo != null) {
                    touchInfo = viewInfo.getClass().getDeclaredField("mOnTouchListener");
                    touchInfo.setAccessible(true);
                }
//                if (!clickInfo.isAccessible()) {
//                    clickInfo.setAccessible(true);
//                }
                wrapOnTouch(viewInfo,touchInfo,view,motionEvent);
            }catch(IllegalAccessException e){
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

        }else {
            try {
                wrapOnTouch(mListenerInfoField.get(view),mOnTouchListenerField,view,motionEvent);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void wrapOnTouch(Object viewInfo,Field touchInfo,View view,MotionEvent motionEvent){
        Object touch;
        try {
            touch = touchInfo != null ? touchInfo.get(viewInfo) : null;
            if (!(touch instanceof View.OnTouchListener)){
                touch = null;
            }

            View.OnTouchListener source = (View.OnTouchListener) touch;
            if (source != null) {
                // 如果source已经是ClickWrapper则不需继续处理
                if (!(source instanceof TouchWrapper)) {
                    // 如果source不是TouchWrapper，则首先尝试复用原先已有的ClickWrapper（可能在RecyclerView中对View重新设置了OnClickListener，
                    // 但是其ClickWrapper对象还在）
                    Object wrapper = view.getTag(R.id.android_touch_listener);
                    if (wrapper instanceof TouchWrapper) {
                        // 如果原先已存在ClickWrapper
                        // 则对比原先ClickWrapper中的OnClickListener是否与source为同一个实例
                        if (((TouchWrapper) wrapper).source != source) {
                            ((TouchWrapper) wrapper).source = source;
                        }
                    } else {
                        // 如果原先不存在ClickWrapper，则创建ClickWrapper
                        wrapper = new TouchWrapper(source, motionEvent);
                        view.setTag(R.id.android_touch_listener, wrapper);
                    }

                    touchInfo.setAccessible(true);
                    touchInfo.set(viewInfo, wrapper);

                }
            }else{
                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
                            HBHStatistical.getInstance().delayed();
                        }
                        return false;
                    }
                });
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


    /**
     * [View.OnClickListener]的包装类，内部包装了View的原[View.OnClickListener]，并且增加了点击统计
     *
     */
    private class TouchWrapper implements View.OnTouchListener {

        private View.OnTouchListener source;
        private MotionEvent ev;

        public TouchWrapper(View.OnTouchListener source, MotionEvent ev) {
            this.source = source;
            this.ev = ev;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (source != null){
                source.onTouch( v, event);
            }
            if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() == MotionEvent.ACTION_CANCEL) {
                HBHStatistical.getInstance().delayed();
            }
            return false;
        }
    }

}
