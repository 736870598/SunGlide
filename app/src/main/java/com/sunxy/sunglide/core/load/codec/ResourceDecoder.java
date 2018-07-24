package com.sunxy.sunglide.core.load.codec;

import android.graphics.Bitmap;

import java.io.IOException;

/**
 * -- bitmap解码器
 * <p>
 * Created by sunxy on 2018/7/24 0024.
 */
public interface ResourceDecoder<T> {

    boolean handles(T source) throws IOException;
    Bitmap decode(T source, int width, int height) throws IOException;
}
