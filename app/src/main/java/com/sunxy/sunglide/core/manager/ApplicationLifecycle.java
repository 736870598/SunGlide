package com.sunxy.sunglide.core.manager;

/**
 * -
 * <p>
 * Created by Sunxy on 2018/7/22.
 */
public class ApplicationLifecycle implements Lifecycle {

    @Override
    public void addListener(LifecycleListener lifecycleListener) {
        lifecycleListener.onStart();
    }

    @Override
    public void removeListener(LifecycleListener lifecycleListener) {

    }
}
