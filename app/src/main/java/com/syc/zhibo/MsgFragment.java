package com.syc.zhibo;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.syc.zhibo.adapter.MsgAdapter;
import com.syc.zhibo.model.Msg;

import java.util.ArrayList;
import java.util.List;

public class MsgFragment extends Fragment{
    private RadioButton navMsg;
    private MsgAdapter adapter;
    private List<Msg> msgs = new ArrayList<Msg>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        View view = inflater.inflate(R.layout.fg_msg,container,false);
        navMsg= (RadioButton) view.findViewById(R.id.nav_msg);
        navMsg.setChecked(true);

        RecyclerView listView = (RecyclerView) view.findViewById(R.id.list);
        listView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new MsgAdapter(msgs);
        listView.setAdapter(adapter);

        initData();
        return view;
    }
    private void initData() {
        msgs.clear();
        msgs.addAll(DataResource.getData());
        adapter.notifyDataSetChanged();
    }
    public static class DataResource {
        private static List<Msg> datas = new ArrayList<>();
        private static String photo = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1530011656045&di=376a7119361dbb261c9b6242021a9b31&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fbf096b63f6246b60553a62a0e1f81a4c510fa22a.jpg";
        private static String time = "10:00";
        private static String msg = "监考老师大家赶快来收感觉克拉时光阿斯科利";


        public static List<Msg> getData() {
            datas.clear();
            for (int i = 0; i < 25; i++) {
                datas.add(new Msg(111, photo, "user"+i, msg, "10:10"));
            }

            return datas;
        }
    }
}
