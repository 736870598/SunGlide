package com.sunxy.sunglide.core.cache;

import com.sunxy.sunglide.core.cache.recycle.Resource;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/19 0019.
 */
public interface MemoryCache {

    interface ResourceRemovedListener{
        void onResourceRemoved(Resource resource);
    }

    Resource remove2(Key key);

    Resource put(Key key, Resource resource);

    void setResourceRemovedListener(ResourceRemovedListener listener);

    void clearMemory();

    void trimMemory(int level);
}
