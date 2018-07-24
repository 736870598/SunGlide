package com.sunxy.sunglide.core.load;

import com.sunxy.sunglide.core.GlideContext;
import com.sunxy.sunglide.core.cache.ActiveResource;
import com.sunxy.sunglide.core.cache.Key;
import com.sunxy.sunglide.core.cache.MemoryCache;
import com.sunxy.sunglide.core.cache.recycle.BitmapPool;
import com.sunxy.sunglide.core.cache.recycle.DiskCache;
import com.sunxy.sunglide.core.cache.recycle.Resource;
import com.sunxy.sunglide.core.request.ResourceCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/24 0024.
 */
public class Engine implements MemoryCache.ResourceRemovedListener, Resource.ResourceListener,
        EngineJob.EngineJobListener {

    public static class LoadStatus{
        private final EngineJob engineJob;
        private final ResourceCallback cb;

        public LoadStatus(EngineJob engineJob, ResourceCallback cb) {
            this.engineJob = engineJob;
            this.cb = cb;
        }

        public void cancel(){
            engineJob.removeCallBack(cb);
        }
    }

    private final DiskCache diskCache;
    private final BitmapPool bitmapPool;
    private final MemoryCache memoryCache;
    private final ThreadPoolExecutor threadPool;
    ActiveResource activeResources;
    Map<Key, EngineJob> jobs = new HashMap<>();

    public Engine(MemoryCache memoryCache, DiskCache diskCache, BitmapPool
            bitmapPool, ThreadPoolExecutor threadPool) {
        this.memoryCache = memoryCache;
        this.diskCache = diskCache;
        this.bitmapPool = bitmapPool;
        this.threadPool = threadPool;
        activeResources = new ActiveResource(this);
    }

    public void shutdown(){
        long shutdownSecond = 5;
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(shutdownSecond, TimeUnit.SECONDS)){
                threadPool.shutdownNow();
                if (!threadPool.awaitTermination(shutdownSecond, TimeUnit.SECONDS)){
                    throw new RuntimeException("failed to shutdown");
                }
            }
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        diskCache.clear();
        activeResources.shutdown();
    }

    public LoadStatus load(GlideContext glideContext, Object model, int width, int height,
                           ResourceCallback cb){
        EngineKey engineKey = new EngineKey(model, width, height);
        //1. 先从活动资源中查找对应的图片
        Resource resource = activeResources.get(engineKey);
        if (resource != null){
            resource.acquire();
            cb.onResourceReady(resource);
            return null;
        }
        //2. 从内存中找图片
        resource = memoryCache.remove2(engineKey);
        if (null != resource){
            activeResources.activate(engineKey, resource);
            resource.acquire();
            resource.setResourceListener(engineKey, this);
            cb.onResourceReady(resource);
            return null;
        }

        //3、文件缓存 或者 图片的源地址加载  IO操作
        EngineJob engineJob = jobs.get(engineKey);
        if (engineJob != null){
            engineJob.addCallback(cb);
            return new LoadStatus(engineJob, cb);
        }
        //创建一个新的加载任务
        engineJob = new EngineJob(engineKey, threadPool, this);
        engineJob.addCallback(cb);
        //加载任务
        DecodeJob decodeJob = new DecodeJob(glideContext, diskCache, model, width, height, engineJob);
        //启动加载任务
        engineJob.start(decodeJob);
        jobs.put(engineKey, engineJob);
        return new LoadStatus(engineJob, cb);
    }



    @Override
    public void onResourceRemoved(Resource resource) {
        bitmapPool.put(resource.getBitmap());
    }

    @Override
    public void onResourceReleased(Key key, Resource resource) {
        activeResources.deactivate(key);
        memoryCache.put(key, resource);
    }

    @Override
    public void onEngineJobComplete(EngineJob engineJob, Key key, Resource resource) {
        if (resource != null) {
            //设置引用计数为0(没有在使用了)的回调
            resource.setResourceListener(key, this);
            //加入活动缓存
            activeResources.activate(key, resource);
        }
        jobs.remove(key);
    }

    @Override
    public void onEngineJobCancelled(EngineJob engineJob, Key key) {
        jobs.remove(key);
    }
}
