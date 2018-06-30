package com.syc.zhibo.adapter;

import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.syc.zhibo.R;
import com.syc.zhibo.model.Circle;
import com.syc.zhibo.model.Msg;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder>{
    private List<Msg> msgs;
    private Handler handler= new Handler();


    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        SimpleDraweeView photo;
        TextView time;
        TextView msg;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            photo = (SimpleDraweeView) view.findViewById(R.id.photo);
            time = (TextView) view.findViewById(R.id.time);
            msg = (TextView) view.findViewById(R.id.msg);
            photo.setAspectRatio(1);
        }
    }
    public MsgAdapter(List<Msg> msgs) {
        this.msgs= msgs;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fg_msg_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Msg msg = msgs.get(position);
        holder.name.setText(msg.getName());
        holder.time.setText(msg.getTime());
        String msgStr = msg.getMsg();
        if(msgStr.length()>20){
            msgStr = msgStr.substring(0,20)+"...";
        }
        holder.msg.setText(msgStr);
        holder.photo.setImageURI(Uri.parse(msg.getPhoto()));
    }
    @Override
    public int getItemCount() {
        return msgs.size();
    }
}
