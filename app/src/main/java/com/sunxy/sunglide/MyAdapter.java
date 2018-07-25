package com.sunxy.sunglide;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.sunxy.sunglide.core.Glide;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/25 0025.
 */
public class MyAdapter extends BaseAdapter {

    static String[] urls = {"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=4155045779," +
            "3430902485&fm=27&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2669567003,3609261574&fm=27&gp=0.jpg22222222asads",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524570152282&di" +
                    "=541514e8c837032cbc184915e4d66139&imgtype=0&src=http%3A%2F%2Fimg1.gtimg" +
                    ".com%2Fent%2Fpics%2Fhv1%2F74%2F146%2F2023%2F131582879.jpg"};

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
            imageView = convertView.findViewById(R.id.image_view_item);
            convertView.setTag(imageView);
        }else{
            imageView = (ImageView) convertView.getTag();
        }
        Glide.with(parent.getContext()).load(urls[0]).into(imageView);
//        Glide.with(parent.getContext()).load(urls[position%urls.length]).into(imageView);
        return convertView;
    }

    public static class ViewHolder{

    }
}
