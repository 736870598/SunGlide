package com.sunxy.sunglide;

import com.sunxy.sunglide.core.cache.ActiveResource;
import com.sunxy.sunglide.core.cache.Key;
import com.sunxy.sunglide.core.cache.LruMemoryCache;
import com.sunxy.sunglide.core.cache.MemoryCache;
import com.sunxy.sunglide.core.cache.recycle.BitmapPool;
import com.sunxy.sunglide.core.cache.recycle.LruBitmapPool;
import com.sunxy.sunglide.core.cache.recycle.Resource;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/20 0020.
 */
public class CacheTest implements Resource.ResourceListener, MemoryCache.ResourceRemovedListener {


    private LruMemoryCache lruMemoryCache;
    private ActiveResource activeResource;
    BitmapPool bitmapPool;

    public Resource test(Key key){
        bitmapPool = new LruBitmapPool(10);
        //内存缓存
        lruMemoryCache = new LruMemoryCache(10);
        lruMemoryCache.setResourceRemovedListener(this);

        //活动资源缓存
        activeResource = new ActiveResource(this);


        /**
         * 从活动缓存中拿取
         */
        Resource resource = activeResource.get(key);
        if (resource != null){
            resource.acquire();
            return resource;
        }

        /**
         * 从内容缓存中拿取
         */
        resource = lruMemoryCache.get(key);
        if (resource != null){
            lruMemoryCache.remove2(key);
            activeResource.activate(key, resource);
            resource.acquire();
            return resource;
        }

        /**
         * 本地拿取
         */

        return null;
    }


    /**
     * 这个资源没有正在使用了
     * 将其从活动资源移除
     * 重新加入到内存缓存中
     */
    @Override
    public void onResourceReleased(Key key, Resource resource) {
        activeResource.deactivate(key);
        lruMemoryCache.put(key, resource);
    }

    /**
     * 从内存缓存被动移除 回调
     * 放入 复用池
     *
     * @param resource
     */
    @Override
    public void onResourceRemoved(Resource resource) {
        bitmapPool.put(resource.getBitmap());
    }
}
