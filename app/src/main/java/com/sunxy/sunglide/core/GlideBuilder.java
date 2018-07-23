package com.sunxy.sunglide.core;

import android.app.ActivityManager;
import android.content.Context;
import android.util.DisplayMetrics;

import com.sun.xml.internal.bind.v2.model.core.ID;
import com.sunxy.sunglide.core.cache.ArrayPool;
import com.sunxy.sunglide.core.cache.LruMemoryCache;
import com.sunxy.sunglide.core.cache.MemoryCache;
import com.sunxy.sunglide.core.cache.recycle.BitmapPool;
import com.sunxy.sunglide.core.cache.recycle.DiskCache;
import com.sunxy.sunglide.core.cache.recycle.DiskLruCacheWrapper;
import com.sunxy.sunglide.core.cache.recycle.LruBitmapPool;

/**
 * -
 * <p>
 * Created by Sunxy on 2018/7/21.
 */
public class GlideBuilder {

    private MemoryCache memoryCache;
    private DiskCache diskCache;
    private BitmapPool bitmapPool;
    private ArrayPool arrayPool;

    private static int getMaxSize(ActivityManager activityManager){
        //将最大可用内从的0.4倍作为缓存使用
        int memoryClassBytes = activityManager.getMemoryClass() * 1024 * 1024;
        return Math.round(memoryClassBytes * 0.4f);
    }


    public Glide build(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int maxSize = getMaxSize(activityManager);

        //获取缓存可用的大小
        int availableSize = maxSize-arrayPool.getMaxSize();

        //
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;

        // 获得一个屏幕大小的argb所占的内存大小
        int screenSize = widthPixels * heightPixels * 4;

        float BitmapPoolSize = screenSize * 4f;
        float memoryCacheSize = screenSize * 2.0f;

        if (BitmapPoolSize + memoryCacheSize <= availableSize){
            //没有超出范围，可以使用
        }else{
            //超出了，则按6份进行分配
            float part = availableSize / 6.0f;
            BitmapPoolSize = Math.round(part * 4);
            memoryCacheSize = Math.round(part * 2);
        }

        if (bitmapPool == null){
            bitmapPool = new LruBitmapPool((int) BitmapPoolSize);
        }

        if (memoryCache == null){
            memoryCache = new LruMemoryCache((int) BitmapPoolSize);
        }

        if (null == diskCache) {
            diskCache = new DiskLruCacheWrapper(context);
        }
        return new Glide(context, this);

    }

    public MemoryCache getMemoryCache() {
        return memoryCache;
    }

    public void setMemoryCache(MemoryCache memoryCache) {
        this.memoryCache = memoryCache;
    }

    public DiskCache getDiskCache() {
        return diskCache;
    }

    public void setDiskCache(DiskCache diskCache) {
        this.diskCache = diskCache;
    }

    public BitmapPool getBitmapPool() {
        return bitmapPool;
    }

    public void setBitmapPool(BitmapPool bitmapPool) {
        this.bitmapPool = bitmapPool;
    }

    public ArrayPool getArrayPool() {
        return arrayPool;
    }

    public void setArrayPool(ArrayPool arrayPool) {
        this.arrayPool = arrayPool;
    }
}
