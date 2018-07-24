package com.sunxy.sunglide.core.load.generator;

import com.sunxy.sunglide.core.GlideContext;
import com.sunxy.sunglide.core.load.model.ModelLoader;
import com.sunxy.sunglide.core.load.model.data.DataFetcher;

import java.util.List;

/**
 * --专门负责从图片源地址加载数据的生成器
 * <p>
 * Created by sunxy on 2018/7/24 0024.
 */
public class SourceGenerator implements DataGenerator, DataFetcher.DataFetcherCallback{

    private final DataGenerator.DataGeneratorCallback cb;
    private GlideContext context;
    private int loadDataListIndex;
    private List<ModelLoader.LoadData<?>> loadDatas;
    private ModelLoader.LoadData<?> loadData;

    public SourceGenerator(GlideContext context, Object model, DataGenerator.DataGeneratorCallback cb) {
        this.context = context;
        this.cb = cb;
        loadDatas = context.getRegistry().getLoadDatas(model);
    }


    @Override
    public boolean startNext() {
        boolean started = false;
        while (!started && hasNextModelLoader()) {
            loadData = loadDatas.get(loadDataListIndex++);
            // hasLoadPath : 是否有个完整的加载路径 从将Model转换为Data之后 有没有一个对应的将Data
            // 转换为图片的解码器
            if (loadData != null && context.getRegistry().hasLoadPath(loadData.fetcher.getDataClass
                    ())) {
                started = true;
                // 将Model转换为Data
                loadData.fetcher.loadData(this);
            }
        }
        return started;
    }

    /**
     * 是否有下一个modelloader支持加载
     * @return
     */
    private boolean hasNextModelLoader() {
        return loadDataListIndex < loadDatas.size();
    }

    @Override
    public void cancel() {
        if (loadData != null) {
            loadData.fetcher.cancel();
        }
    }

    /**
     * 成功由Model获得一个Data
     * @param data
     */
    @Override
    public void onFetcherReady(Object data) {
        cb.onDataReady(loadData.key, data, DataGeneratorCallback.DataSource.REMOTE);
    }

    @Override
    public void onLoadFailed(Exception e) {
        cb.onDataFetcherFailed(loadData.key, e);
    }

}
