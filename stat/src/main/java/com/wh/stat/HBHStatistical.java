package com.wh.stat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import com.wh.stat.layout.StatLayout;
import com.wh.stat.lifecycle.ActivityLifeCycle;
import com.wh.stat.lifecycle.IContext;
import com.wh.stat.utils.LogUtil;
import com.wh.stat.utils.StatConfig;

import java.util.ArrayList;
import java.util.Iterator;

import static android.content.Context.WINDOW_SERVICE;

/**
 * @author KwunYamShan.
 * @time 2019/6/14.
 * @explain
 */
public class HBHStatistical {

    private static View mRootView;

    private StatConfig mConfig;

    public boolean isScroll;

    private static class HelperHolder {
        public static final HBHStatistical mStatistical = new HBHStatistical();
    }

    public static HBHStatistical getInstance() {
        return HelperHolder.mStatistical;
    }

    public void initialize(StatConfig config) {
        if (config == null) {
            throw new NullPointerException("请使用StatBuilder构建类进行初始化");
        }
        mConfig = config;
        if (mConfig.getTagId() == 0) {
            throw new NullPointerException("StatBuilder在构建时需要初始化ID：setTagId(R.id.xxx)");
        }
        ActivityLifeCycle mActivityLifeCycle = new ActivityLifeCycle();
        if (mConfig.getContext() instanceof IContext) {
            ((IContext) mConfig.getContext()).registerActivityLifecycleCallbacks(mActivityLifeCycle);
        } else {
            throw new ClassCastException("Application没有实现IContext接口");
        }

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) mConfig.getContext().getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getRealMetrics(metrics);
        if (mConfig.getScreenRect() == null) {
            mConfig.setScreenRect(new Rect(0, 0, metrics.widthPixels, metrics.heightPixels));
        }
    }

    public int getTagId() {
        return mConfig.getTagId();
    }

    public int getUnMarkId() {
        return mConfig.unMarkId;
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
        if (mConfig.isAuto()) {
            /**
             * 当前窗体得到或失去焦点的时候的时候调用，这是这个活动是否是用户可见的最好的标志
             * 当按下home键、弹窗弹出、被其他页面遮盖住时或者退出app时hasFocus为false，
             * 当home键返回到主页面时、页面被启动、弹出框消失、覆盖页消失时hasFocus为true
             */
            mRootView.getViewTreeObserver().addOnWindowFocusChangeListener(new ViewTreeObserver.OnWindowFocusChangeListener() {
                @Override
                public void onWindowFocusChanged(boolean hasFocus) {
                    LogUtil.e("onWindowFocusChanged:" + hasFocus);
                    if (hasFocus) {
                        reportDelayed();
                    }
//                else {
//                    cancel();
//                    //页面层叠的情况remove会出问题
//                    //mRootView.getViewTreeObserver().removeOnWindowFocusChangeListener(this);
//                }
                }
            });
        }

        /**
         * 当一个视图发生滚动时调用OnScrollChangedListener的onScrollChanged()函数
         */
        mRootView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                LogUtil.e("addOnScrollChangedListener");
                if (isScroll) {
                    scrollDelayed();
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
//                LogUtil.e("addOnWindowAttachListener:onWindowAttached");
//                //reportDelayed();
//            }
//
//            @Override
//            public void onWindowDetached() {
//                LogUtil.e("addOnWindowAttachListener:onWindowDetached");
//                //cancel();
//            }
//        });

        /**
         * 当在一个视图树中的焦点状态或者可见性发生改变时调用OnGlobalFocusChangeListener的onGlobalFocusChanged()函数
         */
//        mRootView.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
//            @Override
//            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
//                LogUtil.e("addOnGlobalFocusChangeListener");
//            }
//        });

//        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                LogUtil.e("addOnGlobalLayoutListener");
//            }
//        });

        /**
         * 监听状态栏页面的隐藏与显示、动态显示与隐藏状态栏
         */
//        mRootView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
//            @Override
//            public void onSystemUiVisibilityChange(int visibility) {
//                //状态栏
//                LogUtil.e("setOnSystemUiVisibilityChangeListener:" + visibility);
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
//               LogUtil.e("addOnTouchModeChangeListener:" + isInTouchMode);
//            }
//        });

        /**
         * 此方法无效  会被子view拦截掉
         */
//        mRootView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP){
//                    reportDelayed();
//                }else if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN){
//                    cancel();
//                }
//                return false;
//            }
//        });
    }

    /**
     * 洗数据，返回结果
     */
    public void report() {
        StatLayout statLayout = findCurrentStatLayout();
        if (statLayout != null) {
            ArrayList<View> displayView = statLayout.findDisplayView(mRootView);
            Iterator iterator = displayView.iterator();
            ArrayList<View> list = new ArrayList();
            while (iterator.hasNext()) {
                View view = (View) iterator.next();
                int id = view.getId();
                String mark = (String) view.getTag(getTagId());
                if (isNeedReport(view) && statLayout.isViewValidRange(view)) {
                    if (isRepeat()) {
                        list.add(view);
                        LogUtil.e("需上报：id:" + id + "     , 数据:" + mark);
                    } else {
                        String unMarkTag = (String) view.getTag(getUnMarkId());
                        if (unMarkTag == null) {
                            list.add(view);
                            view.setTag(getUnMarkId(), "msg");
                            LogUtil.e("需上报：id:" + id + "     , 数据:" + mark);
                        } else {
                            LogUtil.e("非上报：id:" + id);
                        }
                    }
                } else {
                    LogUtil.e("非上报：id:" + id);
                }
            }
            if (list.size() > 0) {
                if (mViewResultListener != null) {
                    mViewResultListener.onViewResult(list);
                }
            }
        }
    }

    private boolean isRepeat() {
        return mConfig.isRepeat();
    }

    public interface ViewResultListener {
        void onViewResult(ArrayList<View> view);
    }

    private ViewResultListener mViewResultListener;

    public void setViewResultListener(ViewResultListener viewResultListener) {
        mViewResultListener = viewResultListener;
    }

    /**
     * 延时上报
     */
    public void reportDelayed() {
        cancel();
        isScroll = false;
        handler.sendEmptyMessageDelayed(StatConfig.HANDLER_REPORT_DELAYED, mConfig.getDelayTime());
    }

    /**
     * 判断滚动停止
     */
    public void scrollDelayed() {
        cancel();
        isScroll = true;
        handler.sendEmptyMessageDelayed(StatConfig.HANDLER_SCROLL_DELAYED, mConfig.scrollTime);
    }

    /**
     * 取消上报
     */
    public void cancel() {
        isScroll = false;
        handler.removeCallbacksAndMessages(null);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == StatConfig.HANDLER_REPORT_DELAYED) {
                report();
            }
            //表示滚动停止
            else if (msg.what == StatConfig.HANDLER_SCROLL_DELAYED) {
                reportDelayed();
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
     * 判断当前view是否需要曝光
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
