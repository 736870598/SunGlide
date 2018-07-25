package com.sunxy.sunglide.core;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;

import com.sunxy.sunglide.core.cache.ArrayPool;
import com.sunxy.sunglide.core.cache.MemoryCache;
import com.sunxy.sunglide.core.cache.recycle.BitmapPool;
import com.sunxy.sunglide.core.load.Engine;
import com.sunxy.sunglide.core.load.codec.StreamBitmapDecoder;
import com.sunxy.sunglide.core.load.model.FileLoader;
import com.sunxy.sunglide.core.load.model.FileUriLoader;
import com.sunxy.sunglide.core.load.model.HttpUriLoader;
import com.sunxy.sunglide.core.load.model.StringModelLoader;
import com.sunxy.sunglide.core.request.RequestOptions;

import java.io.File;
import java.io.InputStream;

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

    private final Engine engine;
    private final GlideContext glideContext;

    public Glide(Context context, GlideBuilder builder) {
        memoryCache = builder.getMemoryCache();
        bitmapPool = builder.getBitmapPool();
        arrayPool = builder.getArrayPool();

        //注册机
        Registry registry = new Registry();
        ContentResolver contentResolver = context.getContentResolver();
        registry.add(String.class, InputStream.class, new StringModelLoader.Factory())
                .add(Uri.class, InputStream.class, new HttpUriLoader.Factory())
                .add(Uri.class, InputStream.class, new FileUriLoader.Factory(contentResolver))
                .add(File.class, InputStream.class, new FileLoader.Factory())
                .register(InputStream.class, new StreamBitmapDecoder(bitmapPool, arrayPool));

        engine = builder.getEngine();
        glideContext = new GlideContext(context, new RequestOptions(),
                engine, registry);

        requestManagerRetriever = new RequestManagerRetriever(glideContext);
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

    private static void init(Context context, GlideBuilder glideBuilder) {
        Context applicationContext = context.getApplicationContext();
        Glide.glide = glideBuilder.build(applicationContext);
    }


    public static RequestManager with(FragmentActivity activity) {
        return Glide.get(activity).requestManagerRetriever.get(activity);
    }

    public static RequestManager with(Activity activity) {
        return Glide.get(activity).requestManagerRetriever.get(activity);
    }

    public static RequestManager with(Context context) {
        return Glide.get(context).requestManagerRetriever.get(context);
    }
}
