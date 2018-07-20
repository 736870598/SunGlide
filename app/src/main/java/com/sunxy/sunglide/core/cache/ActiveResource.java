package com.sunxy.sunglide.core.cache;

import com.sunxy.sunglide.core.cache.recycle.Resource;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/19 0019.
 */
public class ActiveResource {

    private ReferenceQueue<Resource> resourceReferenceQueue;
    private volatile boolean isShutdown;
    private Resource.ResourceListener resourceListener;

    /**
     * 用弱引用记录正在使用的资源
     */
    private Map<Key, ResourceWeakReference> acriveResources = new HashMap<>();
    private Thread cleanReferenceQueueThread;

    public ActiveResource(Resource.ResourceListener resourceListener){
        this.resourceListener = resourceListener;
    }

    public void activate(Key key, Resource resource){
        resource.setResourceListener(key, resourceListener);
        acriveResources.put(key, new ResourceWeakReference(key, resource, getReferenceQueue()));
    }

    public void deactivate(Key key){
        //
        ResourceWeakReference remove = acriveResources.remove(key);
        if(remove != null){
            remove.clear();
        }
    }

    public Resource get(Key key){
        ResourceWeakReference activeRef = acriveResources.get(key);
        if (activeRef == null){
            return null;
        }
        return activeRef.get();
    }

    private ReferenceQueue<Resource> getReferenceQueue(){
        if (resourceReferenceQueue == null){
            resourceReferenceQueue = new ReferenceQueue<>();
            cleanReferenceQueueThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!isShutdown){
                        try {
                            ResourceWeakReference ref =
                                    (ResourceWeakReference) resourceReferenceQueue.remove();
                            acriveResources.remove(ref.key);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                }
            }, "glide-active-resources");
            cleanReferenceQueueThread.start();
        }
        return resourceReferenceQueue;
    }

    public void shutdown(){
        isShutdown = true;
        if (cleanReferenceQueueThread == null){
            return;
        }
        cleanReferenceQueueThread.interrupt();
        try {
            cleanReferenceQueueThread.join(TimeUnit.SECONDS.toMillis(5));
            if (cleanReferenceQueueThread.isAlive()){
                throw new RuntimeException("Failed to join in time");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    static final class ResourceWeakReference extends WeakReference<Resource> {

        Key key;

        public ResourceWeakReference(Key key, Resource resource, ReferenceQueue<? super
                Resource> q) {
            super(resource, q);
            this.key = key;
        }
    }

}
