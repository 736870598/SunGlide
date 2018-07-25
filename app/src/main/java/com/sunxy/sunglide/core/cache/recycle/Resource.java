package com.sunxy.sunglide.core.cache.recycle;

import android.graphics.Bitmap;
import android.util.Log;

import com.sunxy.sunglide.core.cache.Key;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/19 0019.
 */
public class Resource {

    private Bitmap bitmap;
    private int acquired;
    private Key key;
    private ResourceListener listener;

    public interface ResourceListener{
        void onResourceReleased(Key key, Resource resource);
    }

    public Resource(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void recycle(){
        if (acquired > 0){
            return;
        }
        if (!bitmap.isRecycled()){
            bitmap.recycle();
        }
    }

    public void setResourceListener(Key key, ResourceListener listener){
        this.key = key;
        this.listener = listener;
    }

    public void acquire(){
        if (bitmap.isRecycled()){
            throw new IllegalStateException("Cannot acquire a recycled resource");
        }
        ++acquired;
        Log.v("sunxy---", toString());
    }

    public void release(){
        if (--acquired == 0){
            listener.onResourceReleased(key,this);
        }
        Log.v("sunxy---", toString());
    }

    @Override
    public String toString() {
        return "Resource{" +
                "acquired=" + acquired +
                ", key=" + key +
                '}';
    }
}
