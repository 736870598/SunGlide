package com.sunxy.sunglide.core.load.generator;

import android.util.Log;

import com.sunxy.sunglide.core.GlideContext;
import com.sunxy.sunglide.core.cache.Key;
import com.sunxy.sunglide.core.cache.recycle.DiskCache;
import com.sunxy.sunglide.core.load.model.ModelLoader;
import com.sunxy.sunglide.core.load.model.data.DataFetcher;

import java.io.File;
import java.util.List;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/24 0024.
 */
public class DataCacheGenerator implements DataGenerator, DataFetcher.DataFetcherCallback{

    private final DataGeneratorCallback cb;
    private final GlideContext context;
    private final DiskCache diskCache;
    private List<ModelLoader<File, ?>> modelLoaders;
    private List<Key> keys;
    private int sourceIdIndex = -1;
    private Key sourceKey;
    private int modelLoaderIndex;
    private File cacheFile;
    private ModelLoader.LoadData<?> loadData;

    public DataCacheGenerator(GlideContext context, DiskCache diskCache, Object model,
                              DataGeneratorCallback cb) {
        this.context = context;
        this.diskCache = diskCache;
        this.cb = cb;
        //获得对应类型的所有key (当前只有ObjectKey用于磁盘缓存，磁盘缓存不需要宽、高等)
        //即如果文件缓存 获得缓存的key
        keys = context.getRegistry().getKeys(model);
    }

    @Override
    public boolean startNext() {
        //负责将 注册的model 转换为需要的 data
        // 我们注册了 将http地址/文件地址 转化为InputStream
        while (modelLoaders == null){
            sourceIdIndex++;

            if (sourceIdIndex >= keys.size()){
                return false;
            }
            // 通过model获得的一个Key 缓存【key:value】
            Key sourceId = keys.get(sourceIdIndex);
            //获得磁盘缓存的文件
            cacheFile = diskCache.get(sourceId);
            if (cacheFile != null){
                sourceKey = sourceId;
                //获得所有的文件加载器
                modelLoaders = context.getRegistry().getModelLoaders(cacheFile);
            }
        }
        boolean started = false;
        //找出好几个File为Model的 Loader 直到确定一个完全满足
        // 即 能够由此Loader解析完成
        while (!started && hasNextModelLoader()) {
            ModelLoader<File, ?> modelLoader = modelLoaders.get(modelLoaderIndex++);
            loadData = modelLoader.buildData(cacheFile);
            //是否可以把此loader加载的Data 解码出Bitmap
            if (loadData != null && context.getRegistry().hasLoadPath(loadData.fetcher.getDataClass
                    ())) {
                started = true;
                loadData.fetcher.loadData(this);
            }
        }
        return started;
    }

    private boolean hasNextModelLoader() {
        return modelLoaderIndex < modelLoaders.size();
    }

    @Override
    public void cancel() {
        if (loadData != null) {
            loadData.fetcher.cancel();
        }
    }

    @Override
    public void onFetcherReady(Object data) {
        cb.onDataReady(sourceKey, data, DataGeneratorCallback.DataSource.CACHE);
    }

    @Override
    public void onLoadFailed(Exception e) {
        cb.onDataFetcherFailed(sourceKey, e);
    }
}
