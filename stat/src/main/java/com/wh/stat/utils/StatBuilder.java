package com.wh.stat.utils;

import android.content.Context;
import android.graphics.Rect;

import com.wh.stat.HBHStatistical;

/**
 * @author wh.
 * @time 2019/6/14.
 * @explain 构建类
 */
public class StatBuilder {

    private StatConfig mConfig;

    public StatBuilder(Context context) {
        mConfig = new StatConfig();
        mConfig.context = context;
    }

    /**
     * 设置是否打印log
     *
     * @param debugModel
     * @return true 打印
     */
    public StatBuilder setDebugModle(boolean debugModel) {
        mConfig.debugModel = debugModel;
        return this;
    }

    /**
     * 设置曝光的屏幕范围
     */
    public StatBuilder setSubRange(int left, int top, int right, int bottom) {
        mConfig.top = top;
        mConfig.left = left;
        mConfig.right = right;
        mConfig.bottom = bottom;
        return this;
    }

    /**
     * 设置曝光的屏幕范围
     */
    public StatBuilder setStatRange(Rect screenRange) {
        mConfig.mScreenRect = screenRange;
        return this;
    }

    /**
     * 设置曝光的屏幕范围，
     *
     * @param top 减去上方不需要曝光的高度，比如减去状态栏或者ActionBar的高度
     * @return
     */
    public StatBuilder setSubTop(int top) {
        mConfig.top = top;
        return this;
    }

    /**
     * 设置曝光的屏幕范围，
     *
     * @param bottom 减去下方不需要曝光的高度，比如减去系统按键的高度
     * @return
     */
    public StatBuilder setSubBottom(int bottom) {
        mConfig.bottom = bottom;
        return this;
    }

    /**
     * 设置曝光的屏幕范围，
     *
     * @param left 减去左面不需要曝光的高度
     * @return
     */
    public StatBuilder setSubLeft(int left) {
        mConfig.left = left;
        return this;
    }

    /**
     * 设置曝光的屏幕范围，
     *
     * @param right 减去右面不需要曝光的高度
     * @return
     */
    public StatBuilder setSubrRight(int right) {
        mConfig.right = right;
        return this;
    }

    /**
     * 设置时长
     *
     * @param millisecond view显示millisecond毫秒后算一次有效曝光
     */
    public StatBuilder setDuration(long millisecond) {
        mConfig.delayTime = millisecond;
        return this;
    }

    /**
     * 返回绑定数据View的集合
     *
     * @param viewResultListener
     * @return
     */
    public StatBuilder setViewResultListener(HBHStatistical.ViewResultListener viewResultListener) {
        HBHStatistical.getInstance().setViewResultListener(viewResultListener);
        return this;
    }

    /**
     * 关键方法，在配置完成后进行create就创建好配置了
     */
    public void create() {
        HBHStatistical.getInstance().initialize(mConfig);
    }

    /**
     * 设置view的可被遮挡范围
     */
//        public Builder setCoverRange(){
//
//            return this;
//        }

    /**
     * 上报之后是否需要再次上报
     */
}
