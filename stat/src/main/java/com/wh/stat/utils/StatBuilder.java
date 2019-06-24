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
     * 给需要曝光的view设置的tagId, 必须是id类型否则会报错
     * 在main-res-values目录下创建ids.xml文件:
     * <resources>
     * <item name="tag_id" type="id"/>
     * </resources>
     */
    public StatBuilder setTagId(int id) {
        mConfig.tagId = id;
        return this;
    }

    /**
     * 设置是否为线上版本
     *
     * @param debugModel
     */
    public StatBuilder setDebugModle(boolean debugModel) {
        mConfig.debugModel = debugModel;
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
     * 设置view的可被遮挡范围
     *
     * @param range 1~100
     */
    public StatBuilder setCoverRange(int range) {
        mConfig.coverRange = range;
        return this;
    }

    /**
     * 设置是否需要自动曝光
     * 自动曝光：打开了页面但用户未执行其他任何操作情况下执行延时曝光任务
     */
    public StatBuilder setAutoStat(boolean isAuto) {
        mConfig.isAuto = isAuto;
        return this;
    }

    /**
     * 关键方法，在配置完成后进行create就创建好配置了
     */
    public void create() {
        HBHStatistical.getInstance().initialize(mConfig);
    }

    /**
     * 上报去重
     */
}
