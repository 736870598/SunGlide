package com.sunxy.sunglide.core.cache;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/19 0019.
 */
public interface ArrayPool {

    byte[] get(int len);

    void put(byte[] data);

    void clearMemory();

    void trimMemory(int level);

    int getMaxSize();
}
