package com.sunxy.sunglide.core;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.sunxy.sunglide.core.cache.ArrayPool;
import com.sunxy.sunglide.core.cache.MemoryCache;
import com.sunxy.sunglide.core.cache.recycle.BitmapPool;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/20 0020.
 */
public class Glide {

    private final MemoryCache memoryCache;
    private final BitmapPool bitmapPool;
    private final ArrayPool arrayPool;

    private final RequestManagerRetriever requestManagerRetriever;

    private static Glide glide;

    public Glide(Context context, GlideBuilder builder) {
        requestManagerRetriever = new RequestManagerRetriever();
        memoryCache = builder.getMemoryCache();
        bitmapPool = builder.getBitmapPool();
        arrayPool = builder.getArrayPool();
    }

    private static Glide get(Context context){
        if (null == glide){
            synchronized (Glide.class){
                if (null == glide){
                    init(context, new GlideBuilder());
                }
            }
        }
        return glide;
    }

    /**
     * 使用者可以定制自己的 GlideBuilder
     */
    private static void init(Context context, GlideBuilder glideBuilder) {
        Context applicationContext = context.getApplicationContext();
        Glide glide = glideBuilder.build(applicationContext);
        Glide.glide = glide;
    }


    public static RequestManager with(FragmentActivity activity) {
        return Glide.get(activity).requestManagerRetriever.get(activity);
    }
}
