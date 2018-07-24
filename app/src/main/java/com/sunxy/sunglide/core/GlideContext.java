package com.sunxy.sunglide.core;

import android.content.Context;

import com.sunxy.sunglide.core.load.Engine;
import com.sunxy.sunglide.core.request.RequestOptions;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/24 0024.
 */
public class GlideContext {

    Context context;
    RequestOptions defaultRequestOptions;
    Registry registry;
    Engine engine;

    public GlideContext(Context context, RequestOptions defaultRequestOptions, Engine engine,
                        Registry registry) {
        this.context = context;
        this.defaultRequestOptions = defaultRequestOptions;
        this.engine = engine;
        this.registry = registry;
    }

    public Context getContext() {
        return context;
    }


    public RequestOptions getDefaultRequestOptions() {
        return defaultRequestOptions;
    }

    public Engine getEngine() {
        return engine;
    }

    public Registry getRegistry() {
        return registry;
    }

    public Context getApplicationContext() {
        return context;
    }
}
