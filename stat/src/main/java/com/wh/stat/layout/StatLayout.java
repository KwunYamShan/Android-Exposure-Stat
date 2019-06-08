package com.wh.stat.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.wh.stat.HBHStatistical;

public class StatLayout extends FrameLayout {
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
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    HBHStatistical.getInstance().delayed();
                    break;
            }
        }
        return super.onTouchEvent(event);
    }
}
