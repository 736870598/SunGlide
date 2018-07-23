package com.sunxy.sunglide.core.manager;

/**
 * -
 * <p>
 * Created by Sunxy on 2018/7/22.
 */
public interface Lifecycle {

    void addListener(LifecycleListener lifecycleListener);

    void removeListener(LifecycleListener lifecycleListener);
}
