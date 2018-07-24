package com.sunxy.sunglide.core;

import android.widget.ImageView;

import com.sunxy.sunglide.core.request.Request;
import com.sunxy.sunglide.core.request.RequestOptions;

import java.io.File;


/**
 * -
 * <p>
 * Created by Sunxy on 2018/7/22.
 */
public class RequestBuilder {

    private final GlideContext glideContext;
    private RequestOptions requestOptions;
    private RequestManager requestManager;
    private Object model;

    public RequestBuilder(GlideContext glideContext, RequestManager requestManager) {
        this.glideContext = glideContext;
        this.requestManager = requestManager;
        this.requestOptions = glideContext.defaultRequestOptions;
    }

    public RequestBuilder apply(RequestOptions requestOptions) {
        this.requestOptions = requestOptions;
        return this;
    }

    public RequestBuilder load(String string) {
        model = string;
        return this;
    }

    public RequestBuilder load(File file) {
        model = file;
        return this;
    }

    /**
     * 将图片加载到imageView上
     */
    public void into(ImageView view){
        //将View交给Target
        Target target = new Target(view);
        //图片加载设置
        Request request = new Request(glideContext,requestOptions, model, target);
        //request交给requestManager管理
        requestManager.track(request);

    }
}
