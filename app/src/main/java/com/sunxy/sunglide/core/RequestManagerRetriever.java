package com.sunxy.sunglide.core;


import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.sunxy.sunglide.core.manager.ApplicationLifecycle;
import com.sunxy.sunglide.core.manager.SupportRequestManagerFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * -
 * <p>
 * Created by Sunxy on 2018/7/23.
 */
public class RequestManagerRetriever implements android.os.Handler.Callback{

    public static final String FRAG_TAG = "glide_fragment";
    private final static int REMOvE_SUPPORT_FRAGMENT = -1;
    private RequestManager applicationRequestManager;
    private final GlideContext glideContext;
    private Handler handler;
    private Map<FragmentManager, SupportRequestManagerFragment> supports = new HashMap<>();

    public RequestManagerRetriever(GlideContext glideContext) {
        this.glideContext = glideContext;
        handler = new Handler(Looper.myLooper(), this);
    }

    /**
     * 给Application使用的 不管理生命周期
     */
    private RequestManager getApplicationManager(){
        if (null == applicationRequestManager){
            applicationRequestManager = new RequestManager(glideContext, new ApplicationLifecycle());
        }
        return applicationRequestManager;
    }

    protected RequestManager get(Context context){
        if (!(context instanceof Application)) {
            if (context instanceof FragmentActivity) {
                return get((FragmentActivity) context);
            }
        }
        return getApplicationManager();
    }

    private RequestManager get(FragmentActivity activity) {
        FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
        return supportFragmentGet(supportFragmentManager);
    }

    private RequestManager supportFragmentGet(FragmentManager fm){
        SupportRequestManagerFragment fragment = getSupportRequestManagerFragment(fm);
        RequestManager requestManager = fragment.getRequestManager();
        if (null == requestManager){
            requestManager = new RequestManager(glideContext, fragment.getGlideLifecycle());
            fragment.setRequestManager(requestManager);
        }
        return requestManager;
    }

    private SupportRequestManagerFragment getSupportRequestManagerFragment(FragmentManager fm) {
        SupportRequestManagerFragment fragment = (SupportRequestManagerFragment) fm.findFragmentByTag(FRAG_TAG);
        if (fragment == null){
            fragment = supports.get(fm);
            if (fragment == null){
                fragment = new SupportRequestManagerFragment();
                //防止多次创建......  beginTransaction走的handler，可能有延时...
                supports.put(fm, fragment);
                fm.beginTransaction().add(fragment, FRAG_TAG).commitAllowingStateLoss();
                handler.obtainMessage(REMOvE_SUPPORT_FRAGMENT, fm).sendToTarget();
            }
        }
        return fragment;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case REMOvE_SUPPORT_FRAGMENT:
                FragmentManager fm = (FragmentManager) msg.obj;
                supports.remove(fm);
                break;
        }
        return false;
    }
}
