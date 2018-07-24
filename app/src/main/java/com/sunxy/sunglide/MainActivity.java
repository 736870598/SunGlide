package com.sunxy.sunglide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.sunxy.sunglide.core.Glide;
import com.sunxy.sunglide.core.request.RequestOptions;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView iv = findViewById(R.id.image_view);

        Glide.with(this)
                .load("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2669567003,3609261574&fm=27&gp=0.jpg22222222asads")
//                .apply(new RequestOptions()
//                        .error(R.drawable.ic_launcher_background)
//                        .placeholder(R.mipmap.ic_launcher)
//                        .override(1500, 1500))
                .into(iv);


    }
}
