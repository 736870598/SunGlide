package com.sunxy.sunglide.core.load.model;

import android.graphics.ColorSpace;
import android.net.Uri;

import java.io.File;
import java.io.InputStream;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/20 0020.
 */
public class StringModelLoader implements ModelLoader<String, InputStream> {

    private final ModelLoader<Uri, InputStream> loader;

    public StringModelLoader(ModelLoader<Uri, InputStream> loader) {
        this.loader = loader;
    }

    @Override
    public boolean handles(String s) {
        return true;
    }

    @Override
    public LoadData<InputStream> buildData(String model) {
        Uri uri;
        if (model.startsWith("/")){
            uri = Uri.fromFile(new File(model));
        }else{
            uri = Uri.parse(model);
        }
        return loader.buildData(uri);
    }

    public static class Factory implements ModelLoaderFactory<String, InputStream>{

        @Override
        public ModelLoader<String, InputStream> build(ModelLoaderRegistry registry) {
            /**
             * 将String 交给 Uri 的组件处理
             */
            return new StringModelLoader(registry.build(Uri.class, InputStream.class));
        }
    }
}
