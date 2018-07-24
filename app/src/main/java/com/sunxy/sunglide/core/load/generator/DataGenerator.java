package com.sunxy.sunglide.core.load.generator;

import com.sunxy.sunglide.core.cache.Key;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/24 0024.
 */
public interface DataGenerator {

    interface DataGeneratorCallback{

        enum DataSource{
            REMOTE,
            CACHE
        }

        void onDataReady(Key sourceKey, Object data, DataSource dataSource);

        void onDataFetcherFailed(Key sourceKey, Exception e);
    }

    boolean startNext();

    void cancel();
}
