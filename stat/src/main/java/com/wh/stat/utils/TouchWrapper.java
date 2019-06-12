package com.wh.stat.utils;

import android.view.MotionEvent;
import android.view.View;

import com.wh.stat.HBHStatistical;
/**
 * [View.OnTouchListener]的包装类，内部包装了View的原[View.OnTouchListener]，并且增加了手指离开屏幕的事件统计
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