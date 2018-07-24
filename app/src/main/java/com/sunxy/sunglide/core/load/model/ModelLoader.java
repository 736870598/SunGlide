package com.sunxy.sunglide.core.load.model;

import com.sunxy.sunglide.core.cache.Key;
import com.sunxy.sunglide.core.load.model.data.DataFetcher;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/20 0020.
 */
public interface ModelLoader<Model, Data> {

    interface ModelLoaderFactory<Model, Data>{
        ModelLoader<Model, Data> build(ModelLoaderRegistry registry);
    }

    class LoadData<Data>{
        //缓存的key
        public final Key key;
        //加载数据
        public final DataFetcher<Data> fetcher;

        public LoadData(Key key, DataFetcher<Data> fetcher) {
            this.key = key;
            this.fetcher = fetcher;
        }
    }

    /**
     * 此modelLoader是否能处理model类型
     */
    boolean handles(Model model);

    /**
     * 创建加载数据
     */
    LoadData<Data> buildData(Model model);
}
