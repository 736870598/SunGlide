package com.sunxy.sunglide.core.request;

import com.sunxy.sunglide.core.cache.recycle.Resource;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/24 0024.
 */
public interface ResourceCallback {

    void onResourceReady(Resource resource);

}
