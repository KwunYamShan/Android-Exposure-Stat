package com.wh.stat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import com.wh.stat.layout.StatLayout;
import com.wh.stat.lifecycle.ActivityLifeCycle;
import com.wh.stat.lifecycle.IContext;
import com.wh.stat.utils.StatConfig;

import java.util.ArrayList;
import java.util.Iterator;

import static android.content.Context.WINDOW_SERVICE;

public class HBHStatistical {

    public static final String TAG = HBHStatistical.class.getSimpleName();
    private static View mRootView;

    private StatConfig mConfig;

    private static class HelperHolder {
        public static final HBHStatistical mStatistical = new HBHStatistical();
    }

    public static HBHStatistical getInstance() {
        return HelperHolder.mStatistical;
    }

    public int getMarkId() {
        return R.id.mark;
    }

    public void initialize(StatConfig config) {
        mConfig = config;
        ActivityLifeCycle mActivityLifeCycle = new ActivityLifeCycle();
        if (mConfig.context instanceof IContext) {
            ((IContext) mConfig.context).registerActivityLifecycleCallbacks(mActivityLifeCycle);
        } else {
            throw new ClassCastException("Application没有实现IContext接口");
        }

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) mConfig.context.getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getRealMetrics(metrics);
        if (mConfig.mScreenRect == null) {
            mConfig.mScreenRect = new Rect(0 + mConfig.left, 0 + mConfig.top, metrics.widthPixels - mConfig.right, metrics.heightPixels - mConfig.bottom);
        }
    }

    public StatConfig getConfig() {
        return mConfig;
    }

    /**
     * 绑定当前显示的视图
     *
     * @param activity
     */
    public void bind(Activity activity) {
        mRootView = activity.getWindow().getDecorView().getRootView();
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
        //        mRootView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                Log.e(TAG, "--addOnScrollChangedListener");
//            }
//        });
//        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                Log.e(TAG, "--addOnGlobalLayoutListener");
//            }
//        });
//        mRootView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
//            @Override
//            public void onSystemUiVisibilityChange(int visibility) {
//                //用来计算状态栏
//                Log.e(TAG, "--setOnSystemUiVisibilityChangeListener");
//            }
//        });
//        mRootView.getViewTreeObserver().addOnTouchModeChangeListener(new ViewTreeObserver.OnTouchModeChangeListener() {
//            @Override
//            public void onTouchModeChanged(boolean isInTouchMode) {
//                Log.e(TAG, "--addOnTouchModeChangeListener:" + isInTouchMode);
//            }
//        });
//        mRootView.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
//            @Override
//            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
//                Log.e(TAG, "--addOnGlobalFocusChangeListener:");
//            }
//        });
//
//        mRootView.getViewTreeObserver().addOnWindowFocusChangeListener(new ViewTreeObserver.OnWindowFocusChangeListener() {
//            @Override
//            public void onWindowFocusChanged(boolean hasFocus) {
//                Log.e(TAG, "--onWindowFocusChanged:");
//            }
//        });
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
     * TODO 上报
     */
    public void report(ArrayList<View> displayViews) {
        if (displayViews != null && displayViews.size() > 0) {
            Iterator iterator = displayViews.iterator();
            while (iterator.hasNext()) {
                View view = (View) iterator.next();
                String mark = (String) view.getTag(HBHStatistical.getInstance().getMarkId());

                int id = view.getId();
                if (mark != null) {
                    Log.e(TAG, "已上报：id:" + id + "     , 数据:" + mark);
                } else {
                    Log.e(TAG, "非上报：id:" + id);
                }
            }
        }
    }

    /**
     * 计算
     */
    public void delayed() {
        cancel();
        handler.sendEmptyMessageDelayed(StatConfig.REPORT_DELAYED, mConfig.delayTime);
    }

    /**
     * 取消上报
     */
    public void cancel() {
        handler.removeCallbacksAndMessages(null);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == StatConfig.REPORT_DELAYED) {
                HBHStatistical.getInstance().report(findDisplayViews());
            }
        }
    };

    public ArrayList<View> findDisplayViews() {
        if (mRootView instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) mRootView;
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);
                if (child instanceof StatLayout) {
                    StatLayout statLayout = (StatLayout) child;
                    return statLayout.findDisplayView(mRootView);
                }
            }
        }
        return null;
    }

    /**
     * 判断是否要被上报
     *
     * @param view
     * @return
     */
    public boolean isNeedReport(View view) {
        return isTagged(view);
    }

    /**
     * 是否被标记过
     *
     * @param view
     * @return
     */
    public boolean isTagged(View view) {
        return view.getTag(getMarkId()) == null ? false : true;
    }


}
