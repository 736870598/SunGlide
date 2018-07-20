package com.sunxy.sunglide.core.load.model;

import android.content.ContentResolver;
import android.net.Uri;

import com.sunxy.sunglide.core.load.ObjectKey;
import com.sunxy.sunglide.core.load.model.data.FileUriFetcher;

import java.io.InputStream;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/20 0020.
 */
public class FileUriLoader implements ModelLoader<Uri, InputStream> {

    private final ContentResolver contentResolver;

    public FileUriLoader(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    @Override
    public boolean handles(Uri uri) {
        return ContentResolver.SCHEME_FILE.equalsIgnoreCase(uri.getScheme());
    }

    @Override
    public LoadData<InputStream> buildData(Uri uri) {
        return new LoadData<>(new ObjectKey(uri), new FileUriFetcher(uri, contentResolver));
    }

    public static class Factory implements ModelLoaderFactory<Uri, InputStream> {

        private final ContentResolver contentResolver;

        public Factory(ContentResolver contentResolver) {
            this.contentResolver = contentResolver;
        }

        @Override
        public ModelLoader<Uri, InputStream> build(ModelLoaderRegistry registry) {
            return new FileUriLoader(contentResolver);
        }
    }
}
