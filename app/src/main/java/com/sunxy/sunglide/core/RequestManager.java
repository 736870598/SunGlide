package com.sunxy.sunglide.core;

import com.sunxy.sunglide.core.manager.Lifecycle;
import com.sunxy.sunglide.core.manager.LifecycleListener;
import com.sunxy.sunglide.core.manager.RequestTrack;
import com.sunxy.sunglide.core.request.Request;

/**
 * -
 * <p>
 * Created by Sunxy on 2018/7/22.
 */
public class RequestManager implements LifecycleListener{

    private final Lifecycle lifecycle;
    RequestTrack requestTrack;


    public RequestManager(Lifecycle lifecycle){
        this.lifecycle = lifecycle;
        lifecycle.addListener(this);
        requestTrack = new RequestTrack();
    }

    @Override
    public void onStart() {
        resumeRequests();
    }

    @Override
    public void onStop() {
        pauseRequests();
    }

    @Override
    public void onDestroy() {
        lifecycle.removeListener(this);
        requestTrack.clearRequests();
    }

    private void pauseRequests(){
        requestTrack.pauseRequests();
    }

    private void resumeRequests(){
        requestTrack.resumeRequests();
    }

    public void track(Request request){
        requestTrack.runRequest(request);
    }

    public RequestBuilder load(String str){
        return new RequestBuilder(this).load(str);
    }
}
