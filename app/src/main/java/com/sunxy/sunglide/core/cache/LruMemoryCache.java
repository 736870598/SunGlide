package com.sunxy.sunglide.core.cache;

import android.os.Build;
import android.util.LruCache;

import com.sunxy.sunglide.core.cache.recycle.Resource;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/19 0019.
 */
public class LruMemoryCache extends LruCache<Key, Resource> implements MemoryCache{

    private boolean isRemoved;
    private ResourceRemovedListener listener;

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public LruMemoryCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected void entryRemoved(boolean evicted, Key key, Resource oldValue, Resource newValue) {
        super.entryRemoved(evicted, key, oldValue, newValue);
        if (listener != null && oldValue != null && !isRemoved){
            listener.onResourceRemoved(oldValue);
        }
    }

    @Override
    public Resource remove2(Key key) {
        //主动remove的不回调
        isRemoved = true;
        Resource remove = remove(key);
        isRemoved = false;
        return remove;
    }

    @Override
    protected int sizeOf(Key key, Resource value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            return value.getBitmap().getAllocationByteCount();
        }else{
            return value.getBitmap().getByteCount();
        }

    }

    @Override
    public void setResourceRemovedListener(ResourceRemovedListener listener) {
        this.listener = listener;
    }

    @Override
    public void clearMemory() {
        evictAll();
    }

    @Override
    public void trimMemory(int level) {
        if (level >= android.content.ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) {
            clearMemory();
        } else if (level >= android.content.ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            trimToSize(maxSize() / 2);
        }
    }
}
