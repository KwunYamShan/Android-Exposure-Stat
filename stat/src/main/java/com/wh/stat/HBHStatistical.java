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

/**
 * @author wh.
 * @time 2019/6/14.
 * @explain
 */
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

    /**
     * 给需要曝光的view设置的tagId，值不可为null
     * 例：mTvTitle.setTag(HBHStatistical.getInstance().getTagId(),"标题");
     *
     * @return
     */
    public int getTagId() {
        return R.id.mark;
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
        /**
         * 当前窗体得到或失去焦点的时候的时候调用，这是这个活动是否是用户可见的最好的标志
         * 当按下home键、弹窗弹出、被其他页面遮盖住时或者退出app时hasFocus为false，
         * 当home键返回到主页面时、页面被启动、弹出框消失、覆盖页消失时hasFocus为true
         */
        mRootView.getViewTreeObserver().addOnWindowFocusChangeListener(new ViewTreeObserver.OnWindowFocusChangeListener() {
            @Override
            public void onWindowFocusChanged(boolean hasFocus) {
                Log.e(TAG, "onWindowFocusChanged:"+hasFocus);
                if (hasFocus) {
                    HBHStatistical.getInstance().delayed();
                } else {
                    HBHStatistical.getInstance().cancel();
                    mRootView.getViewTreeObserver().removeOnWindowFocusChangeListener(this);
                }
            }
        });
        /**
         * 当View树绑定到window上的时候回调OnWindowAttachListener的onWindowAttached() 函数，
         * 当它从window上解绑时调用OnWindowAttachListener的onWindowDetached()
         */
//        mRootView.getViewTreeObserver().addOnWindowAttachListener(new ViewTreeObserver.OnWindowAttachListener() {
//            @Override
//            public void onWindowAttached() {
//                Log.e(TAG, "onWindowAttached");
//                HBHStatistical.getInstance().delayed();
//            }
//
//            @Override
//            public void onWindowDetached() {
//                Log.e(TAG, "onWindowDetached");
//                HBHStatistical.getInstance().cancel();
//            }
//        });
        /**
         * 当一个视图发生滚动时调用OnScrollChangedListener的onScrollChanged()函数
         */
//        mRootView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                Log.e(TAG, "addOnScrollChangedListener");
//            }
//        });
//        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                Log.e(TAG, "addOnGlobalLayoutListener");
//            }
//        });
        /**
         * 监听状态栏页面的隐藏与显示、动态显示与隐藏状态栏
         */
//        mRootView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
//            @Override
//            public void onSystemUiVisibilityChange(int visibility) {
//                //状态栏
//                Log.e(TAG, "setOnSystemUiVisibilityChangeListener:" + visibility);
//                if (visibility == View.SYSTEM_UI_FLAG_FULLSCREEN || visibility == View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN) {
//                    //全屏
//                } else {
//                    //非全屏
//                }
//            }
//        });
//        mRootView.getViewTreeObserver().addOnTouchModeChangeListener(new ViewTreeObserver.OnTouchModeChangeListener() {
//            @Override
//            public void onTouchModeChanged(boolean isInTouchMode) {
//                Log.e(TAG, "addOnTouchModeChangeListener:" + isInTouchMode);
//            }
//        });
        /**
         * 当在一个视图树中的焦点状态或者可见性发生改变时调用OnGlobalFocusChangeListener的onGlobalFocusChanged()函数
         */
//        mRootView.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
//            @Override
//            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
//                Log.e(TAG, "addOnGlobalFocusChangeListener");
//            }
//        });
//
        /**
         * 此方法无效  会被子view拦截掉
         */
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
    public void report(ArrayList<View> displayViews) {
        Iterator iterator = displayViews.iterator();
        ArrayList<View> list = new ArrayList();

        while (iterator.hasNext()) {
            View view = (View) iterator.next();
            int id = view.getId();
            if (isNeedReport(view)) {
                list.add(view);
                String mark = (String) view.getTag(HBHStatistical.getInstance().getTagId());
                Log.e(TAG, "需上报：id:" + id + "     , 数据:" + mark);
            } else {
                Log.e(TAG, "非上报：id:" + id);
            }
        }
        if (list.size() > 0) {
            if (mViewResultListener != null) {
                mViewResultListener.onViewResult(list);
            }
        }
    }

    public interface ViewResultListener {
        void onViewResult(ArrayList<View> view);
    }

    private ViewResultListener mViewResultListener;

    public void setViewResultListener(ViewResultListener viewResultListener) {
        mViewResultListener = viewResultListener;
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
                StatLayout statLayout = findCurrentStatLayout();
                if (statLayout != null) {
                    ArrayList<View> displayView = statLayout.findDisplayView(mRootView);
                    HBHStatistical.getInstance().report(displayView);
                }
            }
        }
    };

    public StatLayout findCurrentStatLayout() {
        if (mRootView instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) mRootView;
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);
                if (child instanceof StatLayout) {
                    StatLayout statLayout = (StatLayout) child;
                    return statLayout;
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
        return view.getTag(getTagId()) == null ? false : true;
    }

}
