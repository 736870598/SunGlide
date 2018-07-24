package com.sunxy.sunglide.core.request;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/24 0024.
 */
public class RequestOptions {

    private int errorId;
    private int placeholderId;
    private int overrideHeight = -1;
    private int overrideWidth = -1;

    public RequestOptions placeholder(int resourceId){
        this.placeholderId = resourceId;
        return this;
    }

    public RequestOptions error(int resourceId){
        this.errorId = errorId;
        return this;
    }

    public RequestOptions override(int w, int h){
        this.overrideWidth = w;
        this.overrideHeight = h;
        return this;
    }

    public final int getErrorId() {
        return errorId;
    }

    public final int getPlaceholderId() {
        return placeholderId;
    }


    public final int getOverrideWidth() {
        return overrideWidth;
    }

    public final int getOverrideHeight() {
        return overrideHeight;
    }

}
