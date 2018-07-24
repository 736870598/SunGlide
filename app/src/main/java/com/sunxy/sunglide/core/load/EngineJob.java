package com.sunxy.sunglide.core.load;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.sunxy.sunglide.core.cache.Key;
import com.sunxy.sunglide.core.cache.recycle.Resource;
import com.sunxy.sunglide.core.request.ResourceCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/24 0024.
 */
public class EngineJob implements DecodeJob.Callback{

    private static final Handler MAIN_THREAD_HANDLER =
            new Handler(Looper.getMainLooper(), new MainThreadCallback());

    private static final int MSG_COMPLETE = 1;
    private static final int MSG_EXCEPTION = 2;
    private static final int MSG_CANCELLED = 3;
    private Resource resource;

    interface EngineJobListener {
        void onEngineJobComplete(EngineJob engineJob, Key key, Resource resource);
        void onEngineJobCancelled(EngineJob engineJob, Key key);
    }

    private EngineKey key;
    private final List<ResourceCallback> cbs = new ArrayList<>();
    private final ThreadPoolExecutor threadPool;
    private final EngineJobListener listener;
    private boolean isCancelled;
    private DecodeJob decodeJob;


    public EngineJob(EngineKey key, ThreadPoolExecutor threadPool, EngineJobListener listener) {
        this.key = key;
        this.threadPool = threadPool;
        this.listener = listener;
    }

    public void addCallback(ResourceCallback cb){
        cbs.add(cb);
    }

    public void removeCallBack(ResourceCallback cb){
        cbs.remove(cb);
        //这一个请求取消了，可能还有其他地方的请求
        //只有回调为空 才表示请求需要取消
        if (cbs.isEmpty()){
            cancel();
        }
    }

    private void cancel(){
        isCancelled = true;
        decodeJob.cancel();
        listener.onEngineJobCancelled(this, key);
    }

    public void start(DecodeJob decodeJob){
        this.decodeJob = decodeJob;
        threadPool.execute(decodeJob);
    }

    @Override
    public void onResourceReady(Resource resource) {
        this.resource = resource;
        MAIN_THREAD_HANDLER.obtainMessage(MSG_COMPLETE, this).sendToTarget();
    }

    @Override
    public void onLoadFailed(Throwable e) {
        MAIN_THREAD_HANDLER.obtainMessage(MSG_EXCEPTION, this).sendToTarget();
    }

    private static class MainThreadCallback implements Handler.Callback{

        @Override
        public boolean handleMessage(Message msg) {
            EngineJob job = (EngineJob) msg.obj;
            switch (msg.what){
                case MSG_COMPLETE:
                    //加载成功
                    job.handleResultOnMainThread();
                    break;
                case MSG_EXCEPTION:
                    //加载失败
                    job.handleExceptionOnMainThread();
                    break;
                case MSG_CANCELLED:
                    //取消
                    job.handleCancelledOnMainThread();
                    break;
                default:
                    throw new IllegalStateException("msg.what = " + msg.what);
            }
            return true;
        }
    }

    private void handleCancelledOnMainThread(){
        listener.onEngineJobCancelled(this, key);
        release();
    }

    private void handleResultOnMainThread(){
        if (isCancelled){
            resource.recycle();
            release();
            return;
        }
        //将引用计数+1
        resource.acquire();
        //1、回调给Engine 操作缓存
        listener.onEngineJobComplete(this, key, resource);
        //2、cbs里面是ResourceCallback集合，这里其实就是一堆的Request
        for (ResourceCallback cb : cbs) {
            //将引用计数+1
            resource.acquire();
            cb.onResourceReady(resource);
        }
        //将引用计数-1
        resource.release();
        release();
    }

    private void handleExceptionOnMainThread(){
        if (isCancelled){
            release();
            return;
        }
        listener.onEngineJobComplete(this, key, null);
        for (ResourceCallback cb : cbs) {
            cb.onResourceReady(null);
        }
    }

    private void release() {
        cbs.clear();
        key = null;
        resource = null;
        isCancelled = false;
        decodeJob = null;
    }

}

