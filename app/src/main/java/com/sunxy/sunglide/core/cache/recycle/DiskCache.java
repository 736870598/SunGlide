package com.sunxy.sunglide.core.cache.recycle;

import com.sunxy.sunglide.core.cache.Key;

import java.io.File;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/20 0020.
 */
public interface DiskCache {

    interface Writer{
        boolean write(File file);
    }

    File get(Key key);

    void put(Key key, Writer writer);

    void delete(Key key);

    void clear();


}
