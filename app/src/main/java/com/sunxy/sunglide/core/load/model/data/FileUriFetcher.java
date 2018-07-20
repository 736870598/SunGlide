package com.sunxy.sunglide.core.load.model.data;

import android.content.ContentResolver;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/20 0020.
 */
public class FileUriFetcher implements DataFetcher<InputStream> {

    private final Uri uri;
    private final ContentResolver cr;

    public FileUriFetcher(Uri uri, ContentResolver cr) {
        this.uri = uri;
        this.cr = cr;
    }

    @Override
    public void loadData(DataFetcherCallback<InputStream> callback) {
        InputStream is = null;
        try {
            is = cr.openInputStream(uri);
            callback.onFetcherReady(is);
        } catch (FileNotFoundException e) {
            callback.onLoadFailed(e);
        }finally {
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void cancel() {

    }
}
