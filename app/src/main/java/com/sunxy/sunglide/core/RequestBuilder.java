package com.sunxy.sunglide.core;

import com.sunxy.sunglide.core.request.Request;

import javax.swing.text.html.ImageView;

/**
 * -
 * <p>
 * Created by Sunxy on 2018/7/22.
 */
public class RequestBuilder {

    private RequestManager requestManager;
    private Object model;

    public RequestBuilder(RequestManager requestManager){
        this.requestManager = requestManager;
    }

    public RequestBuilder load(String str){
        model = str;
        return this;
    }

    /**
     * 将图片加载到imageView上
     */
    public void into(ImageView view){
        //将View交给Target
        Target target = new Target(view);
        //图片加载宇设置
        Request request = new Request(model, target);
        //request交给requestManager管理
        requestManager.track(request);

    }
}
