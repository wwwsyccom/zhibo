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
import com.syc.zhibo.model.GiftHistory;

import org.w3c.dom.Text;

import java.util.List;

public class GiftHistoryAdapter extends RecyclerView.Adapter<GiftHistoryAdapter.ViewHolder>{
    private List<GiftHistory> histories;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView name;
        TextView consume;
        TextView date;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            img = (ImageView) view.findViewById(R.id.photo);
            consume = (TextView) view.findViewById(R.id.consume);
            date = (TextView)view.findViewById(R.id.date);
        }
    }
    public GiftHistoryAdapter(List<GiftHistory> histories) {
        this.histories= histories;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gift_history,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final GiftHistory history = histories.get(position);
        holder.name.setText("kkkkkkk");
        holder.img.setImageResource(R.mipmap.timg);
        holder.consume.setText("10");
        holder.date.setText("2010-10-10 10:10:10");
    }
    @Override
    public int getItemCount() {
        return histories.size();
    }
}
