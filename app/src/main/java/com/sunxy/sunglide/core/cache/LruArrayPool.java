package com.sunxy.sunglide.core.cache;

import android.util.LruCache;

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/19 0019.
 */
public class LruArrayPool implements ArrayPool{


    public static final int ARRAY_POOL_SIZE_BYTES = 4 * 1024 * 1024;
    private int maxSize;
    private LruCache<Integer, byte[]> cache;
    private final NavigableMap<Integer, Integer> sortedSizes = new TreeMap<>();
    //单个资源的与maxsize 最大比例
    private static final int SINGLE_ARRAY_MAX_SIZE_DIVISOR = 2;
    //溢出大小
    private final static int MAX_OVER_SIZE_MULTIPLE = 8;

    public LruArrayPool(){
        this(ARRAY_POOL_SIZE_BYTES);
    }

    public LruArrayPool(int maxSize){
        this.maxSize = maxSize;
        this.cache = new LruCache<Integer, byte[]>(maxSize) {
            @Override
            protected int sizeOf(Integer key, byte[] value) {
                return value.length;
            }

            @Override
            protected void entryRemoved(boolean evicted, Integer key, byte[] oldValue, byte[] newValue) {
                sortedSizes.remove(oldValue.length);
            }
        };
    }

    @Override
    public byte[] get(int len) {
        Integer key = sortedSizes.ceilingKey(len);
        if (key != null){
            if (key <= (len * MAX_OVER_SIZE_MULTIPLE)){
                byte[] bytes = cache.remove(key);
                sortedSizes.remove(key);
                return bytes == null ? new byte[len] : bytes;
            }
        }
        return new byte[len];
    }

    @Override
    public void put(byte[] data) {
        int length = data.length;
        if (!isSmallEnoughForResus(length)){
            //太大了，不需要
            return;
        }
        sortedSizes.put(length, 1);
        cache.put(length,data);
    }

    private boolean isSmallEnoughForResus(int byteSize){
        return byteSize <= maxSize / SINGLE_ARRAY_MAX_SIZE_DIVISOR;
    }

    @Override
    public void clearMemory() {
        cache.evictAll();
    }

    @Override
    public void trimMemory(int level) {
        if (level >= android.content.ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) {
            clearMemory();
        } else if (level >= android.content.ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN
                || level == android.content.ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL) {
            cache.trimToSize(maxSize / 2);
        }
    }

    @Override
    public int getMaxSize() {
        return maxSize;
    }
}
