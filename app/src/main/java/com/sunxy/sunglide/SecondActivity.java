package com.sunxy.sunglide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/25 0025.
 */
public class SecondActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);

//        initLv();
        initRv();

    }

    private void initLv(){
        ListView listView = findViewById(R.id.list_view);
        listView.setVisibility(View.VISIBLE);
        listView.setAdapter(new MyAdapter());
    }

    private void initRv(){
        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyRecycleAdapter());
    }
}
