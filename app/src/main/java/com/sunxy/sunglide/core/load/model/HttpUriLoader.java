package com.sunxy.sunglide.core.load.model;

import android.net.Uri;

import com.sunxy.sunglide.core.load.ObjectKey;
import com.sunxy.sunglide.core.load.model.data.HttpUrlFetcher;

import java.io.InputStream;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/20 0020.
 */
public class HttpUriLoader implements ModelLoader<Uri, InputStream> {
    @Override
    public boolean handles(Uri uri) {
        String scheme = uri.getScheme();
        return scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https");
    }

    @Override
    public LoadData<InputStream> buildData(Uri uri) {
        return new LoadData<>(new ObjectKey(uri), new HttpUrlFetcher(uri));
    }

    public static class Factory implements ModelLoaderFactory<Uri, InputStream>{

        @Override
        public ModelLoader<Uri, InputStream> build(ModelLoaderRegistry registry) {
            return new HttpUriLoader();
        }
    }
}
