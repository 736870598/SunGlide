package com.sunxy.sunglide.core.load.codec;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sunxy.sunglide.core.cache.ArrayPool;
import com.sunxy.sunglide.core.cache.recycle.BitmapPool;

import java.io.IOException;
import java.io.InputStream;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/24 0024.
 */
public class StreamBitmapDecoder implements ResourceDecoder<InputStream> {

    private final BitmapPool bitmapPool;
    private final ArrayPool arrayPool;

    public StreamBitmapDecoder(BitmapPool bitmapPool, ArrayPool arrayPool) {
        this.bitmapPool = bitmapPool;
        this.arrayPool = arrayPool;
    }

    @Override
    public boolean handles(InputStream source) throws IOException {
        return true;
    }

    /**
     * 解码
     */
    @Override
    public Bitmap decode(InputStream source, int width, int height) throws IOException {
        MarkInputStream is;
        if (source instanceof MarkInputStream){
            is = (MarkInputStream) source;
        }else{
            is = new MarkInputStream(source, arrayPool);
        }
        is.mark(0);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        options.inJustDecodeBounds = false;
        //获取图片原图宽高
        int sourceW = options.outWidth;
        int sourceH = options.outHeight;
        //获取要显示的目标宽高
        int targetW = width < 0 ? sourceW : width;
        int targetH = height < 0 ? sourceH : height;
        //获取宽高方向上的缩放因子
        float factorW = targetW / sourceW;
        float factorH = targetH / sourceH;
        //获得最大的缩放因子
        float factor = Math.max(factorW, factorH);
        //获得目标宽、高
        int outWidth = Math.round(factor * sourceW);
        int outHeight = Math.round(factor * sourceH);
        //相对原图的缩放比 宁愿小一点 不超过需要的宽、高
        int widthScaleFactor = sourceW / outWidth + (sourceW % outWidth == 0 ? 0 : 1);
        int heightScaleFactor = sourceH / outHeight + (sourceH % outHeight == 0 ? 0 : 1);

        int sampleSize = Math.max(widthScaleFactor, heightScaleFactor);
        sampleSize = Math.max(1, sampleSize);
        // 设置缩放因子
        options.inSampleSize = sampleSize;
        //设置解码格式
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        //设置复用图片
        options.inBitmap = bitmapPool.get(outWidth, outHeight, Bitmap.Config.RGB_565);
        //设置可复用
        options.inMutable = true;
        is.reset();
        Bitmap result = BitmapFactory.decodeStream(is, null, options);
        is.release();
        return result;
    }
}
