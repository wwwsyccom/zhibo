package com.syc.zhibo.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.syc.zhibo.R;
import com.syc.zhibo.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> implements View.OnClickListener{
    private List<User> users;
    private Bitmap imageBitMap;
    private Handler handler= new Handler();
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView   photo;
        TextView price;
        TextView access;
        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.price);
//            access = (TextView) view.findViewById(R.id.access);
            photo = (ImageView) view.findViewById(R.id.photo);
        }
    }
    public HomeAdapter(List<User> users) {
        this.users = users;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fg_home_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return holder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final User user = users.get(position);
        holder.name.setText(user.getName());
        holder.price.setText(user.getPrice()+"豆/分钟");
//        holder.access.setText(user.getAccess()+"%接通率");

        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(users.get(position));

        // 构建Runnable对象，在runnable中更新界面
        final Runnable runnableUi=new Runnable(){
            @Override
            public void run() {
                //更新界面
                holder.photo.setImageBitmap(imageBitMap);
            }
        };
        new Thread(){
            public void run(){
                imageBitMap = returnBitMap(user.getPhoto());
                handler.post(runnableUi);
            }
        }.start();
//      holder.photo.setImageBitmap(returnBitMap(user.getPhoto()));
    }
    @Override
    public int getItemCount() {
        return users.size();
    }
    public Bitmap returnBitMap(String url){
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , User data);
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(User)v.getTag());
        }
    }
}
