package com.sunxy.sunglide.core.request;

import com.sunxy.sunglide.core.Target;

/**
 * -
 * <p>
 * Created by Sunxy on 2018/7/22.
 */
public class Request {
    private final Object model;
    private final Target target;

    public Request(Object model, Target target) {
        this.model = model;
        this.target = target;
    }
}
