package com.wh.stat.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.IntRange;

import com.wh.stat.R;
import java.util.List;

/**
 * @author KwunYamShan.
 * @time 2019/6/14.
 * @explain 配置类
 */
public class StatConfig {
    //constant
    public static final int HANDLER_REPORT_DELAYED = 1;
    public static final int HANDLER_SCROLL_DELAYED = 2;
    public int unMarkId = R.id.unmark;
    public long scrollTime = 100;

    private Context context;
    //屏幕宽高的矩形
    private Rect mScreenRect;
    //用来标记需要曝光的view
    private int tagId;
    //有效曝光视图显示的范围，0-100
    private int validRange = 100;
    //曝光延时的时间
    private long delayTime = 5000;
    //模式
    private boolean debugModel;
    //是否需要自动曝光
    private boolean isAuto;
    //是否需要重复上报
    private boolean isRepeat;
    //要曝光的activity
    private List<String> exposureActivities;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Rect getScreenRect() {
        return mScreenRect;
    }

    public void setScreenRect(Rect screenRect) {
        this.mScreenRect = screenRect;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getValidRange() {
        return validRange;
    }

    public void setValidRange(@IntRange(from = 0, to = 100)int validRange) {
        this.validRange = validRange;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    public boolean isDebugModel() {
        return debugModel;
    }

    public void setDebugModel(boolean debugModel) {
        this.debugModel = debugModel;
    }

    public boolean isAuto() {
        return isAuto;
    }

    public void setAuto(boolean auto) {
        isAuto = auto;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public void setRepeat(boolean repeat) {
        isRepeat = repeat;
    }

    public List<String> getExposureActivities() {
        return exposureActivities;
    }

    public void setExposureActivities(List<String> exposureActivities) {
        this.exposureActivities = exposureActivities;
    }
}
