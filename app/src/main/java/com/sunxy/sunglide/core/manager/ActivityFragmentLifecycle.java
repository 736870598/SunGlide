package com.sunxy.sunglide.core.manager;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * -
 * <p>
 * Created by Sunxy on 2018/7/22.
 */
public class ActivityFragmentLifecycle implements Lifecycle{

    //
    private final Set<LifecycleListener> lifecycleListeners =
            Collections.newSetFromMap(new WeakHashMap<LifecycleListener, Boolean>());
    //已启动
    private boolean isStarted;
    //已销毁
    private boolean isDestoryed;


    @Override
    public void addListener(LifecycleListener lifecycleListener) {
        lifecycleListeners.add(lifecycleListener);
        if (isDestoryed){
            lifecycleListener.onDestroy();
        }else if(isStarted){
            lifecycleListener.onStart();
        }else{
            lifecycleListener.onStop();
        }
    }

    @Override
    public void removeListener(LifecycleListener lifecycleListener) {
        lifecycleListeners.remove(lifecycleListener);
    }

    void onStart(){
        isStarted = true;
        for (LifecycleListener lifecycleListener : lifecycleListeners) {
            lifecycleListener.onStart();
        }
    }

    void onStop(){
        isStarted = false;
        for (LifecycleListener lifecycleListener : lifecycleListeners) {
            lifecycleListener.onStop();
        }
    }

    void onDestroy(){
        isDestoryed = true;
        for (LifecycleListener lifecycleListener : lifecycleListeners) {
            lifecycleListener.onDestroy();
        }
    }
}
