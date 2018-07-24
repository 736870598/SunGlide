package com.sunxy.sunglide.core.manager;

import android.app.Fragment;

import com.sunxy.sunglide.core.RequestManager;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/24 0024.
 */
public class RequestManagerFragment extends Fragment {

    ActivityFragmentLifecycle lifecycle;
    RequestManager requestManager;

    public RequestManagerFragment(){
        lifecycle = new ActivityFragmentLifecycle();
    }

    public void setRequestManager(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public ActivityFragmentLifecycle getGlideLifecycle() {
        return lifecycle;
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycle.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        lifecycle.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycle.onDestroy();
    }
}
