package com.syc.zhibo.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.syc.zhibo.ActivityDetail;
import com.syc.zhibo.ActivityPhoto;
import com.syc.zhibo.R;
import com.syc.zhibo.model.Circle;
import com.syc.zhibo.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CircleAdapter extends RecyclerView.Adapter<CircleAdapter.ViewHolder> implements View.OnClickListener{
    private List<Circle> circles;
    private Handler handler= new Handler();
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private static Activity act;


    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        SimpleDraweeView photo;
        TextView time;
        TextView msg;
        SimpleDraweeView[] imgs = new SimpleDraweeView[3];

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            photo = (SimpleDraweeView) view.findViewById(R.id.photo);
            time = (TextView) view.findViewById(R.id.time);
            msg = (TextView) view.findViewById(R.id.msg);
            imgs[0] = (SimpleDraweeView) view.findViewById(R.id.img0);
            imgs[1] = (SimpleDraweeView) view.findViewById(R.id.img1);
            imgs[2] = (SimpleDraweeView) view.findViewById(R.id.img2);
            imgs[0].setAspectRatio(1);
            imgs[1].setAspectRatio(1);
            imgs[2].setAspectRatio(1);
            photo.setAspectRatio(1);

            photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    act.startActivity(new Intent(act, ActivityDetail.class));
                }
            });
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    act.startActivity(new Intent(act, ActivityDetail.class));
                }
            });
        }
    }
    public CircleAdapter(List<Circle> circles, Activity activity) {
        this.circles= circles;
        this.act = activity;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fg_circle_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return holder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Circle circle = circles.get(position);
        holder.name.setText(circle.getName());
        holder.time.setText(circle.getTime());
        holder.msg.setText(circle.getMsg());
        holder.photo.setImageURI(Uri.parse(circle.getPhoto()));
        final String[] imgs = circle.getImgs();
        for(int i=0;i<imgs.length;i++){
            holder.imgs[i].setImageURI(Uri.parse(imgs[i]));

            final int finalI = i;
            holder.imgs[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(act, ActivityPhoto.class);
                    Bundle bd = new Bundle();
                    bd.putInt("index", finalI);
                    bd.putStringArray("photos", imgs);
                    intent.putExtras(bd);
                    act.startActivity(intent);
                }
            });
        }
        for(int i=imgs.length;i<3;i++){
            holder.imgs[i].setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() {
        return circles.size();
    }
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , Circle data);
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(Circle) v.getTag());
        }
    }
}
