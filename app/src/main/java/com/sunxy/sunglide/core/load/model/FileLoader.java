package com.sunxy.sunglide.core.load.model;

import android.net.Uri;

import java.io.File;
import java.io.InputStream;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/20 0020.
 */
public class FileLoader<Data> implements ModelLoader<File, Data> {

    private final ModelLoader<Uri, Data> loader;

    public FileLoader(ModelLoader<Uri, Data> loader) {
        this.loader = loader;
    }

    @Override
    public boolean handles(File file) {
        return true;
    }

    @Override
    public LoadData<Data> buildData(File file) {
        return loader.buildData(Uri.fromFile(file));
    }

    public static class Factory implements ModelLoaderFactory<File, InputStream>{

        @Override
        public ModelLoader<File, InputStream> build(ModelLoaderRegistry registry) {
            return new FileLoader(registry.build(Uri.class, InputStream.class));
        }
    }
}
