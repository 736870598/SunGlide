package com.sunxy.sunglide.core;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.sunxy.sunglide.core.manager.ApplicationLifecycle;
import com.sunxy.sunglide.core.manager.RequestManagerFragment;
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
    private final static int REMOVE_SUPPORT_FRAGMENT = -1;
    private RequestManager applicationRequestManager;
    private final GlideContext glideContext;
    private Handler handler;
    private Map<Object, Object> supports = new HashMap<>();

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
            }else if (context instanceof Activity) {
                return get((Activity) context);
            }
        }
        return getApplicationManager();
    }

    private RequestManager get(FragmentActivity activity) {
        FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
        return supportFragmentGet(supportFragmentManager);
    }


    private RequestManager get(Activity activity) {
        android.app.FragmentManager fragmentManager = activity.getFragmentManager();
        return fragmentGet(fragmentManager);
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

    private RequestManager fragmentGet( android.app.FragmentManager fm){
        RequestManagerFragment fragment = getRequestManagerFragment(fm);
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
            fragment = (SupportRequestManagerFragment) supports.get(fm);
            if (fragment == null){
                fragment = new SupportRequestManagerFragment();
                //防止多次创建......  beginTransaction走的handler，可能有延时...
                supports.put(fm, fragment);
                fm.beginTransaction().add(fragment, FRAG_TAG).commitAllowingStateLoss();
                handler.obtainMessage(REMOVE_SUPPORT_FRAGMENT, fm).sendToTarget();
            }
        }
        return fragment;
    }

    private RequestManagerFragment getRequestManagerFragment(android.app.FragmentManager fm) {
        RequestManagerFragment fragment = (RequestManagerFragment) fm.findFragmentByTag(FRAG_TAG);
        if (fragment == null){
            fragment = (RequestManagerFragment) supports.get(fm);
            if (fragment == null){
                fragment = new RequestManagerFragment();
                //防止多次创建......  beginTransaction走的handler，可能有延时...
                supports.put(fm, fragment);
                fm.beginTransaction().add(fragment, FRAG_TAG).commitAllowingStateLoss();
                handler.obtainMessage(REMOVE_SUPPORT_FRAGMENT, fm).sendToTarget();
            }
        }
        return fragment;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case REMOVE_SUPPORT_FRAGMENT:
                supports.remove(msg.obj);
                break;
        }
        return false;
    }
}
