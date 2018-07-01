package com.syc.zhibo.adapter;

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
import com.syc.zhibo.model.Gift;
import com.syc.zhibo.model.Msg;

import java.util.List;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.ViewHolder>{
    private List<Gift> gifts;
    private Handler handler= new Handler();


    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView img;
        TextView num;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            img = (ImageView) view.findViewById(R.id.img);
            num = (TextView) view.findViewById(R.id.num);
        }
    }
    public GiftAdapter(List<Gift> gifts) {
        this.gifts= gifts;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_detail_gift,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Gift gift = gifts.get(position);
        holder.name.setText(gift.getName());
        holder.img.setImageResource(R.mipmap.gift_520);
        holder.num.setText("10");
    }
    @Override
    public int getItemCount() {
        return gifts.size();
    }
}
