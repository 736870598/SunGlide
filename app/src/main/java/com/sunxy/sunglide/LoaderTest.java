package com.sunxy.sunglide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.sunxy.sunglide.core.load.model.FileLoader;
import com.sunxy.sunglide.core.load.model.FileUriLoader;
import com.sunxy.sunglide.core.load.model.HttpUriLoader;
import com.sunxy.sunglide.core.load.model.ModelLoader;
import com.sunxy.sunglide.core.load.model.ModelLoaderRegistry;
import com.sunxy.sunglide.core.load.model.StringModelLoader;
import com.sunxy.sunglide.core.load.model.data.DataFetcher;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/20 0020.
 */
public class LoaderTest {

    public static void testFindLoader(final Context context){

        new Thread(){
            @Override
            public void run() {
                super.run();

                ModelLoaderRegistry loaderRegistry = new ModelLoaderRegistry();
                loaderRegistry.add(String.class, InputStream.class, new StringModelLoader.Factory());
                loaderRegistry.add(Uri.class, InputStream.class, new FileUriLoader.Factory(context.getContentResolver()));
                loaderRegistry.add(Uri.class, InputStream.class, new HttpUriLoader.Factory());
                loaderRegistry.add(File.class, InputStream.class, new FileLoader.Factory());

                List<ModelLoader<String, ?>> modelLoaders = loaderRegistry.getModelLoaders(String.class);
                final ModelLoader<String, ?> modelLoader = modelLoaders.get(0);

                ModelLoader.LoadData<InputStream> loadData =
                        (ModelLoader.LoadData<InputStream>) modelLoader.buildData("https://ss1.bdstatic" +
                                ".com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2669567003," +
                                "3609261574&fm=27&gp=0.jpg22222222asads");

                loadData.fetcher.loadData(new DataFetcher.DataFetcherCallback<InputStream>() {
                    @Override
                    public void onFetcherReady(InputStream o) {
                        try {
                            Log.v("sunxy----", "onFetcherReady:" + o.available());
                            Bitmap bitmap = BitmapFactory.decodeStream(o);
                            Log.v("sunxy----", "bitmap:" + bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onLoadFailed(Exception e) {
                        Log.v("sunxy", "onLoadFailed:" + e.getMessage());
                        e.printStackTrace();
                    }
                });
            }
        }.start();


    }
}
