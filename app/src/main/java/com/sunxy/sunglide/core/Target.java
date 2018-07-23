package com.sunxy.sunglide.core;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.sunxy.sunglide.core.request.Request;

import java.lang.ref.WeakReference;

/**
 * -
 * <p>
 * Created by Sunxy on 2018/7/22.
 */
public class Target {

    private static int maxDisplayLength = -1;
    private SizeReadyCallback cb;
    private LayoutListener layoutListener;
    private ImageView imageView;
    private Request request;

    public void cancel(){
        ViewTreeObserver observer = imageView.getViewTreeObserver();
        if (observer.isAlive()){
            observer.removeOnPreDrawListener(layoutListener);
        }
        layoutListener = null;
        cb = null;
    }

    public void onLoadFailed(Drawable error){
        imageView.setImageDrawable(error);
    }

    public void onLoadStarted(Drawable placeholderDrawable){
        imageView.setImageDrawable(placeholderDrawable);
    }

    public void onResourceReady(Bitmap bitmap){
        imageView.setImageBitmap(bitmap);
    }

    public void setRequest(Request request){
        this.request = request;
    }

    private static final class LayoutListener implements ViewTreeObserver.OnPreDrawListener{

        private final WeakReference<Target> targetRef;

        LayoutListener(Target sizeDeterminer) {
            targetRef = new WeakReference<>(sizeDeterminer);
        }

        @Override
        public boolean onPreDraw() {
            Target target = targetRef.get();
            if (target != null){
                target.checkCurrentDimens();
            }
            return true;
        }
    }



    public interface SizeReadyCallback{
        void onSizeReady(int width, int height);
    }

    public Target(ImageView imageView){
        this.imageView = imageView;
    }


    public Request getRequest() {
        return request;
    }

    public void getSize(){

    }

    public void checkCurrentDimens(){

    }
}
