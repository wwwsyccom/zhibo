package com.syc.zhibo.adapter;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.syc.zhibo.R;
import com.syc.zhibo.model.Gift;

import java.util.List;

public class GiftSendAdapter extends RecyclerView.Adapter<GiftSendAdapter.ViewHolder>{
    private List<Gift> gifts;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView img;
        TextView price;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            img = (ImageView) view.findViewById(R.id.img);
            price= (TextView) view.findViewById(R.id.price);
        }
    }
    public GiftSendAdapter(List<Gift> gifts) {
        this.gifts= gifts;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_send_gift, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Gift gift = gifts.get(position);
        holder.name.setText("鲜花");
        holder.img.setImageResource(R.mipmap.gift_520);
        holder.price.setText("10");
    }
    @Override
    public int getItemCount() {
        return gifts.size();
    }
}
