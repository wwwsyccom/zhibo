package com.syc.zhibo;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.syc.zhibo.adapter.GiftHistoryAdapter;
import com.syc.zhibo.model.GiftHistory;

import java.util.ArrayList;
import java.util.List;

public class ActivityGiftHistory extends AppCompatActivity implements  RadioGroup.OnCheckedChangeListener{
    private RadioGroup navGroup;
    private RadioButton give;
    private RadioButton receive;
    private TextView tip;
    private GiftHistoryAdapter adapter;
    private List<GiftHistory> histories = new ArrayList<GiftHistory>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_history);

        navGroup = (RadioGroup) findViewById(R.id.nav_group);
        receive = (RadioButton) findViewById(R.id.receive);
        give = (RadioButton)findViewById(R.id.give);
        tip = (TextView)findViewById(R.id.tip);

        navGroup.setOnCheckedChangeListener(this);
        receive.setChecked(true);

        //历史列表
        RecyclerView listView = (RecyclerView) findViewById(R.id.list);
        listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new GiftHistoryAdapter(histories);
        listView.setAdapter(adapter);
        initData();
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.receive:
                tip.setText("共收到100件礼物");
                break;
            case R.id.give:
                tip.setText("共送出100件礼物");
                break;
        }
    }
    private void initData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                histories.clear();
                histories.addAll(DataResource.getData());
                adapter.notifyDataSetChanged();
            }
        }, 2000);
    }
    public static class DataResource {
        private static List<GiftHistory> datas = new ArrayList<>();
        private static int page = 0;
        private static String photo = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1530011656045&di=376a7119361dbb261c9b6242021a9b31&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fbf096b63f6246b60553a62a0e1f81a4c510fa22a.jpg";
        private static String time="10:00";
        private static String msg="监考老师大家赶快来收感觉克拉时光阿斯科利";

        private static String[] imgs = new String[3];

        public static List<GiftHistory> getData() {
            for (int i = 0; i < 5; i++) {
                datas.add(new GiftHistory());
            }
            return datas;
        }
    }
}
