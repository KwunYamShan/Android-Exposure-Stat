package com.wh.stat.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.wh.stat.HBHStatistical;

import java.util.ArrayList;
import java.util.Iterator;

import static com.wh.stat.HBHStatistical.TAG;

public class StatLayout extends FrameLayout  {
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
                ArrayList<View> hitViews = HBHStatistical.getInstance().findHitView(rootView);
                Iterator iterator = hitViews.iterator();
                while (iterator.hasNext()) {
                    View view = (View) iterator.next();
                    //view.setOnTouchListener(this);
                    HBHStatistical.getInstance().wrapTouch(view);
                }
            }
        }
        return super.onTouchEvent(event);
    }
}
