package com.github.miao1007.konakanwallpaper.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.miao1007.konakanwallpaper.activity.R;
import com.github.miao1007.konakanwallpaper.model.Image;
import com.github.miao1007.konakanwallpaper.widget.DialogWithChoice;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by leon on 14-8-30.
 */
public class MyAdaper extends BaseAdapter {
    private int firstPosition = 0;
    private Context context;
    private List<Image> images;
    private android.os.Handler handler;


    public MyAdaper(Context context, android.os.Handler handler , List<Image> images) {
        this.context = context;
        this.images = images;
        this.handler = handler;
    }

    class ViewHolder {
        ImageView imageView;
        ProgressBar progressBar;
        //TextView textView;
        //ViewStub stub;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int i) {
        return images.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    ViewHolder holder = null;
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //ViewHolder holder = null;
        //int downLoadProgress = images.get(i).getDownLoadProgress();
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.adaper_my, null);
            holder.imageView = (ImageView) view.findViewById(R.id.imageView);
            holder.progressBar = (ProgressBar) view.findViewById(R.id.adapter_my_progressBar);
            //holder.textView = (TextView)view.findViewById(R.id.textView);
            //holder.stub = (ViewStub)view.findViewById(R.id.adapter_download);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Picasso.with(context)
                .load(images.get(i).getPreview_url())
                        //A request will be retried three times before the error placeholder is shown.
                .error(R.drawable.android_load_error)
                .placeholder(R.drawable.android_load_loading)
                .into(holder.imageView);
        //Log.w("Progress",String.valueOf(downLoadProgress));
//        if (downLoadProgress != holder.progressBar.getProgress()){
//            holder.progressBar.setProgress(downLoadProgress);
//        }
        final int currentPosition = i;
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //holder.imageView.setImageResource(R.drawable.android_load_loading);
                Log.w("Image", "Onclickimg");
                Image image = images.get(currentPosition);
                DialogWithChoice.createDialog(context,image);

            }
        });
        return view;
    }


}
