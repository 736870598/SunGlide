package com.sunxy.sunglide.core.cache.recycle;

import android.graphics.Bitmap;
import android.util.LruCache;

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/20 0020.
 */
public class LruBitmapPool extends LruCache<Integer, Bitmap> implements BitmapPool{

    private boolean isRemoved;

    private NavigableMap<Integer, Integer> map = new TreeMap<>();

    private final static int MAX_OVER_SIZE_MULTIPLE = 2;

    public LruBitmapPool(int maxSize) {
        super(maxSize);
    }

    /**
     * 将bitmap放入复用池
     */
    @Override
    public void put(Bitmap bitmap) {
        //必须设置了isMutable才可复用
        if (bitmap.isMutable()){
            bitmap.recycle();
            return;
        }
        int size = bitmap.getAllocationByteCount();
        if (size >= maxSize()){
            bitmap.recycle();
            return;
        }
        put(size, bitmap);
        map.put(size, 0);

    }

    /**
     * 获取一个可复用的bitmap
     * @param width
     * @param height
     * @param config
     * @return
     */
    @Override
    public Bitmap get(int width, int height, Bitmap.Config config) {
        //新Bitmap需要的内存大小  (只关心 argb8888和RGB65)
        int size = width * height * (config == Bitmap.Config.ARGB_8888?4:2);
        //获得等于 size或者大于size的key
        Integer key = map.ceilingKey(size);
        //从key集合从找到一个>=size并且 <= size*MAX_OVER_SIZE_MULTIPLE
        if (key != null && key <= size * MAX_OVER_SIZE_MULTIPLE){
            isRemoved = true;
            Bitmap remove = remove(key);
            isRemoved = false;
            return remove;
        }
        return null;
    }

    @Override
    protected int sizeOf(Integer key, Bitmap value) {
        return value.getAllocationByteCount();
    }

    @Override
    protected void entryRemoved(boolean evicted, Integer key, Bitmap oldValue, Bitmap newValue) {
        map.remove(key);
        if (!isRemoved){
            oldValue.recycle();
        }
    }
}
