package com.sunxy.sunglide;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sunxy.sunglide.core.Glide;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/25 0025.
 */
public class MyRecycleAdapter  extends RecyclerView.Adapter<MyRecycleAdapter.ViewHolder>{

    private final String[] urls = {"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=4155045779," +
            "3430902485&fm=27&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2669567003,3609261574&fm=27&gp=0.jpg22222222asads",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524570152282&di" +
                    "=541514e8c837032cbc184915e4d66139&imgtype=0&src=http%3A%2F%2Fimg1.gtimg" +
                    ".com%2Fent%2Fpics%2Fhv1%2F74%2F146%2F2023%2F131582879.jpg"};

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(position);

    }

    @Override
    public int getItemCount() {
        return 10;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_item);
        }

        public void bindView(int pos){
            Glide.with(itemView.getContext()).load(urls[0]).into(imageView);
//        Glide.with(itemView.getContext()).load(urls[pos%urls.length]).into(imageView);
        }
    }
}
