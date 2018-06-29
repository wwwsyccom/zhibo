package com.syc.zhibo.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.syc.zhibo.R;
import com.syc.zhibo.model.Circle;
import com.syc.zhibo.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CircleAdapter extends RecyclerView.Adapter<CircleAdapter.ViewHolder>{
    private List<Circle> circles;
    private Handler handler= new Handler();


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
        }
    }
    public CircleAdapter(List<Circle> circles) {
        this.circles= circles;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fg_circle_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Circle circle = circles.get(position);
        holder.name.setText(circle.getName());
        holder.time.setText(circle.getTime());
        holder.msg.setText(circle.getMsg());
        holder.photo.setImageURI(Uri.parse(circle.getPhoto()));
        String[] imgs = circle.getImgs();
        for(int i=0;i<imgs.length;i++){
            holder.imgs[i].setImageURI(Uri.parse(imgs[i]));
        }
        for(int i=imgs.length;i<3;i++){
            holder.imgs[i].setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() {
        return circles.size();
    }
}
