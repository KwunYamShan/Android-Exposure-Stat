package com.wh.stat.layout;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.wh.stat.HBHStatistical;
import com.wh.stat.R;
import com.wh.stat.utils.LogUtil;
import com.wh.stat.utils.StatConfig;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author KwunYamShan.
 * @time 2019/6/14.
 * @explain
 */
public class StatLayout extends FrameLayout implements View.OnTouchListener {
    private Rect mRect = new Rect();

    private Field mListenerInfoField;
    private Field mOnTouchListenerField;

    public StatLayout(Context context) {
        super(context);
    }

    public StatLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StatLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event != null) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                HBHStatistical.getInstance().cancel();
                View rootView = getRootView();
                ArrayList<View> displayViews = findDisplayView(rootView);
                Iterator iterator = displayViews.iterator();
                while (iterator.hasNext()) {
                    View view = (View) iterator.next();
                    //view.setOnTouchListener(this);
                    wrapTouch(view);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            HBHStatistical.getInstance().delayed();
        }
        return false;
    }

    public boolean isVisible(View view) {
        return view.getVisibility() == VISIBLE;
    }

    /**
     * 判断一个View是否包含在屏幕范围内
     */
    public boolean displayView(View view) {
        StatConfig mConfig = HBHStatistical.getInstance().getConfig();
        view.getGlobalVisibleRect(mRect);
        boolean contains = mConfig.getScreenRect().contains(mRect);
        String mark = (String) view.getTag(HBHStatistical.getInstance().getTagId());
        LogUtil.e("displayView 是否包含在屏幕中:" + contains + ",  id:" + view.getId() + ",  " + mRect.left + "+" + mRect.top + "+" + mRect.right + "+" + mRect.bottom + ", 数据：" + mark);
        return contains;
    }

    /**
     * 是否显示在屏幕中
     *
     * @param view
     * @return
     */
    public boolean isViewGlobalVisible(final View view) {
        return view.getGlobalVisibleRect(mRect);
    }

    /**
     * 该方法为递归实现，如果传入的布局为[ViewGroup]，则执行递归添加；否则直接添加出现在屏幕中的view
     */
    public ArrayList<View> findDisplayView(View parent) {
        ArrayList<View> displayViews = new ArrayList<>();
        // 仅在parent可见，并其触发了触摸事件时才对该parent进行判断/递归查找，减少查找的次数，提高效率
        if (isVisible(parent) && isViewGlobalVisible(parent)) {
            displayViews.add(parent);
            if (parent instanceof ViewGroup) {
                findDisplayViewsInGroup((ViewGroup) parent, displayViews);
            }
        }
        return displayViews;
    }

    /**
     * 是否为显示完整的view
     *
     * @param view
     * @return
     */
    public boolean isCompleteView(View view) {
        view.getGlobalVisibleRect(mRect);
        LogUtil.e("isCompleteView viewId:" + view.getId() + "，实际宽:" + view.getMeasuredWidth() + "，显示宽：" + (mRect.right - mRect.left) + ",实际高：" + view.getMeasuredHeight() + "，显示高：" + (mRect.bottom - mRect.top));
        if (view.getMeasuredWidth() <= (mRect.right - mRect.left) && view.getMeasuredHeight() <= (mRect.bottom - mRect.top)) {
            return true;
        }
        return false;
    }

    /**
     * 是否在可被覆盖的范围内
     *
     * @param view
     * @return
     */
    public boolean isViewCoverRange(View view) {
        int coverRange = HBHStatistical.getInstance().getConfig().getCoverRange();
        if (coverRange == 0) return true;

        view.getGlobalVisibleRect(mRect);
        //view的面积*可被覆盖掉的范围如果比view当前显示的面积大则为有效曝光
        coverRange = coverRange <= 0 ? 1 : coverRange;
        coverRange = coverRange >= 100 ? 100 : coverRange;
        float percent = (float) (100 - coverRange) / 100;
        LogUtil.e("isViewCoverRange 测量的view宽高:" + "view.getMeasuredWidth()" + view.getMeasuredWidth() + ", view.getMeasuredHeight()" + view.getMeasuredHeight());
        LogUtil.e("isViewCoverRange 计算的view大小" + ((float) (view.getMeasuredWidth() * view.getMeasuredHeight()) * percent));
        LogUtil.e("isViewCoverRange 显示的view大小" + (mRect.right - mRect.left) * (mRect.bottom - mRect.top));
        if (((float) (view.getMeasuredWidth() * view.getMeasuredHeight()) * percent) <= ((mRect.right - mRect.left) * (mRect.bottom - mRect.top))) {
            return true;
        }
        return false;
    }

    /**
     * 对ViewGroup中的所有子View进行查询，如果子View中没有符合条件的View
     * 则会对父View进行检查，如果父View可见，并且在屏幕范围内，则List中会包含父View
     */
    public void findDisplayViewsInGroup(ViewGroup parent, ArrayList<View> displayViews) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            ArrayList<View> displayChildren = findDisplayView(child);
            if (!displayChildren.isEmpty()) {
                displayViews.addAll(displayChildren);
            } else if (isVisible(child) && isViewGlobalVisible(child)) {
                displayViews.add(child);
            }
        }
    }

    /**
     * 通过反射获取mListenerInfo
     *
     * @return
     */
    private Field getListenerInfoField() {
        Field declaredField = null;
        try {
            // 通过反射拿到mListenerInfo，并且设置为可访问（用于后续替换触摸事件）
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

    /**
     * 通过反射获取mOnTouchListener对象
     *
     * @param view
     * @return
     */
    private Field getOnTouchListenerField(View view) {
        Field touchInfo = null;
        try {
            Object viewInfo = getListenerInfoField().get(view);
            if (viewInfo != null) {

                touchInfo = viewInfo.getClass().getDeclaredField("mOnTouchListener");
                if (!touchInfo.isAccessible()) {
                    touchInfo.setAccessible(true);
                }
                mOnTouchListenerField = touchInfo;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return touchInfo;
    }

    public void wrapTouch(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            wrapOnTouch(view);
        } else {
            if (getListenerInfoField() != null && getOnTouchListenerField(view) != null) {
                wrapOnTouch(view);
            }
        }
    }

    private void wrapOnTouch(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            Object viewInfo ;
            Field touchInfo = null;
            try {
                viewInfo = getListenerInfoField().get(view);
                if (viewInfo != null) {
                    touchInfo = viewInfo.getClass().getDeclaredField("mOnTouchListener");
                    touchInfo.setAccessible(true);
                }
//                if (!touchInfo.isAccessible()) {
//                    touchInfo.setAccessible(true);
//                }
                wrapOnTouch(viewInfo, touchInfo, view);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

        } else {
            try {
                wrapOnTouch(mListenerInfoField.get(view), mOnTouchListenerField, view);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void wrapOnTouch(Object viewInfo, Field touchInfo, View view) {
        Object touch;
        try {
            touch = touchInfo != null ? touchInfo.get(viewInfo) : null;
            if (!(touch instanceof View.OnTouchListener)) {
                touch = null;
            }

            View.OnTouchListener source = (View.OnTouchListener) touch;
            if (source != null) {
                // 如果source已经是TouchWrapper则不需继续处理
                if (!(source instanceof TouchWrapper)) {
                    // 如果source不是TouchWrapper，则首先尝试复用原先已有的TouchWrapper（可能在RecyclerView中对View重新设置了OnTouchListener，
                    // 但是其TouchWrapper对象还在）
                    Object wrapper = view.getTag(R.id.android_touch_listener);
                    if (wrapper instanceof TouchWrapper) {
                        // 如果原先已存在TouchWrapper
                        // 则对比原先TouchWrapper中的OnTouchListener是否与source为同一个实例
                        if (((TouchWrapper) wrapper).source != source) {
                            ((TouchWrapper) wrapper).source = source;
                        }
                    } else {
                        // 如果原先不存在TouchWrapper，则创建TouchWrapper
                        wrapper = new TouchWrapper(source);
                        view.setTag(R.id.android_touch_listener, wrapper);
                    }
                    touchInfo.setAccessible(true);
                    touchInfo.set(viewInfo, wrapper);
                }
            } else {
                view.setOnTouchListener(this);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * [View.OnTouchListener]的包装类，内部包装了View的原[View.OnTouchListener]，增加曝光的事件统计
     */
    public class TouchWrapper implements View.OnTouchListener {

        public View.OnTouchListener source;

        public TouchWrapper(View.OnTouchListener source) {
            this.source = source;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (source != null) {
                source.onTouch(v, event);
            }
            if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                HBHStatistical.getInstance().delayed();
            }
            return false;
        }
    }
}
