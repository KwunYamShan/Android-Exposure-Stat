package com.wh.stat.lifecycle;

import android.app.Application;

/**
 * @author KwunYamShan.
 * @time 2019/6/6.
 * @explain describe:辅助获取[Context]的接口
 * 需要在APP的[Application]类中实现该接口
 */
public interface IContext {
    void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callbacks);
}
