package com.syc.zhibo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.syc.zhibo.adapter.GiftAdapter;
import com.syc.zhibo.model.Gift;
import com.syc.zhibo.util.RecyclerMarginDecoration;

import java.util.ArrayList;
import java.util.List;

public class ActivityGift extends AppCompatActivity  implements RadioGroup.OnCheckedChangeListener{
    private RadioGroup navGroup;
    private RadioButton give;
    private RadioButton receive;
    private List<Gift> gifts = new ArrayList<Gift>();
    private GiftAdapter adapter;
    private TextView tip1;
    private TextView tip2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);

        navGroup = (RadioGroup) findViewById(R.id.nav_group);
        receive = (RadioButton) findViewById(R.id.receive);
        give = (RadioButton)findViewById(R.id.give);

        navGroup.setOnCheckedChangeListener(this);
        receive.setChecked(true);

        //收到礼物
        RecyclerView listView = (RecyclerView) findViewById(R.id.list_gift);
        listView.addItemDecoration(new RecyclerMarginDecoration(20, 4));
        listView.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new GiftAdapter(gifts);
        listView.setAdapter(adapter);
        initData();
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.receive:
                tip1.setText("共收到100件礼物");
                tip2.setText("点击礼物可查看礼物来源");
                break;
            case R.id.give:
                tip1.setText("共送出100件礼物");
                tip2.setText("点击礼物可查看礼物去向");
                break;
        }
    }
    private void initData() {
        gifts.addAll(DataResource.getData());
        adapter.notifyDataSetChanged();

    }
    public static class DataResource {
        private static List<Gift> datas = new ArrayList<>();

        public static List<Gift> getData() {
            datas.clear();
            for (int i = 0; i < 10; i++) {
                datas.add(new Gift("icon", "礼物"+i, i));
            }
            return datas;
        }
        public static List<Gift> getDataSend() {
            datas.clear();
            for (int i = 0; i < 10; i++) {
                Gift gift = new Gift();
                datas.add(gift);
            }
            return datas;
        }
    }
}
