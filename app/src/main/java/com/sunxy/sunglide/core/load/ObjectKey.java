package com.sunxy.sunglide.core.load;

import com.sunxy.sunglide.core.cache.Key;

import java.security.MessageDigest;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/20 0020.
 */
public class ObjectKey implements Key {

    private final Object object;

    public ObjectKey(Object object){
        this.object = object;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(getKeyBates());
    }

    @Override
    public byte[] getKeyBates() {
        return object.toString().getBytes();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjectKey objectKey = (ObjectKey) o;
        return object != null ? objectKey.equals(objectKey.object) : objectKey.object == null;
    }

    @Override
    public int hashCode() {
        return object != null ? object.hashCode() : 0;
    }
}
