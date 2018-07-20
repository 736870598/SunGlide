package com.sunxy.sunglide.core.cache;

import java.security.MessageDigest;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/19 0019.
 */
public interface Key {

    void updateDiskCacheKey(MessageDigest messageDigest);

    byte[] getKeyBates();

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

}
