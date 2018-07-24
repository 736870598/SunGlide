package com.sunxy.sunglide.core.load;

import android.graphics.Bitmap;

import com.sunxy.sunglide.core.GlideContext;
import com.sunxy.sunglide.core.cache.Key;
import com.sunxy.sunglide.core.cache.recycle.DiskCache;
import com.sunxy.sunglide.core.cache.recycle.Resource;
import com.sunxy.sunglide.core.load.generator.DataCacheGenerator;
import com.sunxy.sunglide.core.load.generator.DataGenerator;
import com.sunxy.sunglide.core.load.generator.SourceGenerator;
import com.sunxy.sunglide.core.load.model.data.DataFetcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Retention;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/24 0024.
 */
public class DecodeJob implements Runnable, DataGenerator.DataGeneratorCallback {

    private final GlideContext context;
    private final DiskCache diskCache;
    private final int width;
    private final int height;
    private final Callback callback;
    private final Object model;
    private boolean isCancelled;
    private DataGenerator currentGenerator;
    private boolean isCallbackNotified;
    private Stage stage;
    private Key sourceKey;


    /**
     * 当前阶段
     */
    private enum Stage {
        INITIALIZE, //初始化阶段
        DATA_CACHE, //查找文件缓存阶段
        SOURCE,
        FINISHED,
    }

    interface Callback {
        void onResourceReady(Resource resource);
        void onLoadFailed(Throwable e);
    }

    public DecodeJob(GlideContext context, DiskCache diskCache, Object model,
                     int width, int height, Callback callback){
        this.context = context;
        this.diskCache = diskCache;
        this.width = width;
        this.height = height;
        this.callback = callback;
        this.model = model;
    }


    public void cancel(){
        isCancelled = true;
        if (currentGenerator != null){
            currentGenerator.cancel();
        }
    }

    @Override
    public void run() {
        try {
            if (isCancelled){
                callback.onLoadFailed(new IOException("cancelled"));
                return;
            }
            // 阶段 这里获得的是 查找文件缓存阶段
            stage = getNextStage(Stage.INITIALIZE);
            //下一个数据生成器
            currentGenerator = getNextGenerator();
            //使用这个生成器
            runGenerators();
        }catch (Exception e){
            callback.onLoadFailed(e);
        }
    }

    @Override
    public void onDataReady(Key sourceKey, Object data, DataSource dataSource) {
        this.sourceKey = sourceKey;
        runLoadPath(data, dataSource);
    }

    @Override
    public void onDataFetcherFailed(Key sourceKey, Exception e) {
        runGenerators();
    }

    private Stage getNextStage(Stage current){
        switch (current) {
            case INITIALIZE:
                return Stage.DATA_CACHE;
            case DATA_CACHE:
                return Stage.SOURCE;
            case SOURCE:
            case FINISHED:
                return Stage.FINISHED;
            default:
                throw new IllegalArgumentException("Unrecognized stage: " + current);
        }
    }

    private DataGenerator getNextGenerator(){
        switch (stage){
            case DATA_CACHE:
                return new DataCacheGenerator(context, diskCache, model, this);
            case SOURCE:
                return new SourceGenerator(context, model, this);
            case FINISHED:
                return null;
            default:
                throw new IllegalStateException("Unrecognized stage: " + stage);
        }
    }

    private void runGenerators(){
        boolean isStarted = false;
        while (!isCancelled && currentGenerator != null && !isStarted){
            isStarted = currentGenerator.startNext();
            //生成器工作了，就break
            if (isStarted){
                break;
            }
            stage = getNextStage(stage);
            if (stage == Stage.FINISHED){
                break;
            }
            currentGenerator = getNextGenerator();
        }

        if ((stage == Stage.FINISHED || isCancelled) && !isStarted){
            notifyFailed();
        }
    }

    private void notifyFailed(){
        if (!isCallbackNotified){
            isCallbackNotified = true;
            callback.onLoadFailed(new RuntimeException("failed to load resource"));
        }
    }


    private <Data> void runLoadPath(Data data, DataSource dataSource) {
        LoadPath<Data> loadPath = (LoadPath<Data>) context.getRegistry().getLoadPath(data.getClass());
        Bitmap bitmap = loadPath.runLoad(data, width, height);
        if (bitmap != null){
            notifyComplete(bitmap, dataSource);
        }else{
            runGenerators();
        }
    }

    private void notifyComplete(final Bitmap bitmap, DataSource dataSource) {
        if (dataSource == DataSource.REMOTE){
            diskCache.put(sourceKey, new DiskCache.Writer() {
                @Override
                public boolean write(File file) {
                    FileOutputStream os = null;
                    try {
                        os = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, os);
                        return true;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }finally {
                        if (null != os){
                            try {
                                os.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    return false;
                }
            });
        }
        Resource resource = new Resource(bitmap);
        callback.onResourceReady(resource);
    }


}
