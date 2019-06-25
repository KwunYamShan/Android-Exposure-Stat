package com.wh.stat.utils;

import android.content.Context;

import com.wh.stat.HBHStatistical;

/**
 * @author KwunYamShan.
 * @time 2019/6/14.
 * @explain 构建类
 */
public class StatBuilder {

    private StatConfig mConfig;

    public StatBuilder(Context context) {
        mConfig = new StatConfig();
        mConfig.setContext(context);
    }

    /**
     * 给需要曝光的view设置的tagId, 必须是id类型否则会报错
     * 在main-res-values目录下创建ids.xml文件:
     * <resources>
     * <item name="tag_id" type="id"/>
     * </resources>
     * <p>
     * 调用：setTagId(R.id.tag_id);
     */
    public StatBuilder setTagId(int id) {
        mConfig.setTagId(id);
        return this;
    }

    /**
     * 设置是否为线上版本
     *
     * @param debugModel true为debug模式， false为线上模式
     */
    public StatBuilder setDebugModle(boolean debugModel) {
        mConfig.setDebugModel(debugModel);
        return this;
    }

    /**
     * 设置延时曝光的时长
     *
     * @param millisecond view显示millisecond毫秒后并且用户没有主动操作时才算一次有效曝光
     */
    public StatBuilder setDuration(long millisecond) {
        mConfig.setDelayTime(millisecond);
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
     * 设置需曝光的view可被遮挡范围
     *
     * @param range 范围：0-100  例：20代表view被覆盖或显示不全20%以内依然可以算作是有效曝光。默认值：0 表示view不可被覆盖
     */
    public StatBuilder setCoverRange(int range) {
        mConfig.setCoverRange(range);
        return this;
    }

    /**
     * 设置是否需要自动曝光
     * （自动曝光：打开了页面但用户未执行其他任何操作情况下会自动执行一次延时曝光任务）
     *
     * @param isAuto isAuto为true时自动延时曝光。 isAuto为false表示当有用户操作时才会执行延时曝光
     */
    public StatBuilder setAutoStat(boolean isAuto) {
        mConfig.setAuto(isAuto);
        return this;
    }

    /**
     * 曝光去重（是否可以重复曝光）
     *
     * @param isRepeat isRepeat为true时表示每当有用户操作后都会执行延时曝光的命令。isRepeat为false表示曝光过一次后在view所在的Activity生命周期结束之前不会被再次曝光
     */
    public StatBuilder setRepeat(boolean isRepeat) {
        mConfig.setRepeat(isRepeat);
        return this;
    }

    /**
     * 关键方法，在配置完成后进行create就创建好配置了
     */
    public void create() {
        HBHStatistical.getInstance().initialize(mConfig);
    }

}
