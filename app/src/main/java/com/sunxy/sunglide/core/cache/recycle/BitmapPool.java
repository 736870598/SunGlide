package com.sunxy.sunglide.core.cache.recycle;

import android.graphics.Bitmap;

/**
 * --bitmap 复用池
 * <p>
 * Created by sunxy on 2018/7/19 0019.
 */
public interface BitmapPool {

    void put(Bitmap bitmap);

    Bitmap get(int width, int height, Bitmap.Config config);


}
