package com.sunxy.sunglide.core.load.model.data;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/20 0020.
 */
public class HttpUrlFetcher implements DataFetcher<InputStream> {

    private final Uri uri;
    private boolean isCanceled;

    public HttpUrlFetcher(Uri uri) {
        this.uri = uri;
    }

    @Override
    public void loadData(DataFetcherCallback<InputStream> callback) {
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            URL url = new URL(uri.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            is=conn.getInputStream();
            int responseCode = conn.getResponseCode();
            if (isCanceled){
                return;
            }
            if (responseCode == HttpURLConnection.HTTP_OK){
                callback.onFetcherReady(is);
            }else{
                callback.onLoadFailed(new RuntimeException(conn.getResponseMessage()));
            }
        } catch (Exception e) {
            callback.onLoadFailed(e);
        }finally {
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null){
                conn.disconnect();
            }
        }
    }

    @Override
    public void cancel() {
        isCanceled = true;
    }
}
