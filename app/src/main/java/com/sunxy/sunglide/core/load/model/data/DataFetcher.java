package com.sunxy.sunglide.core.load.model.data;

/**
 * -- 负责加载数据
 * <p>
 * Created by sunxy on 2018/7/20 0020.
 */
public interface DataFetcher<Data> {

    interface  DataFetcherCallback<Data> {

        /**
         * 数据加载完成
         */
        void onFetcherReady(Data data);

        /**
         * 加载失败
         */
        void onLoadFailed(Exception e);
    }

    void loadData(DataFetcherCallback<Data> callback);

    void cancel();

    Class<?> getDataClass();
}
