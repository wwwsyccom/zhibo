package com.syc.zhibo;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.syc.zhibo.adapter.CircleAdapter;
import com.syc.zhibo.model.Circle;
import com.syc.zhibo.util.SwipeRefreshView;

import java.util.ArrayList;
import java.util.List;

public class CircleFragment extends Fragment implements RadioGroup.OnCheckedChangeListener{
    private RadioButton navAll;
    private SwipeRefreshView mSwipeRefreshView;
    private CircleAdapter adapter;
    private List<Circle> circles = new ArrayList<Circle>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        View view = inflater.inflate(R.layout.fg_circle,container,false);
//        RadioGroup navGroup = (RadioGroup) view.findViewById(R.id.nav_group);
        navAll= (RadioButton) view.findViewById(R.id.nav_all);
        navAll.setChecked(true);

        mSwipeRefreshView = (SwipeRefreshView) view.findViewById(R.id.list_wrap);
        mSwipeRefreshView.setProBar((ProgressBar)view.findViewById(R.id.probar));
        RecyclerView listView = (RecyclerView) view.findViewById(R.id.list);
        listView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new CircleAdapter(circles);
        listView.setAdapter(adapter);

        initData();
        initEvent();
        return view;
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.nav_all:
                break;
            case R.id.nav_my:
                break;
            case R.id.nav_concern:
                break;
            case R.id.nav_video:
                break;
        }
    }
    private void initEvent() {

        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        mSwipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        // 设置下拉加载更多
        mSwipeRefreshView.setOnLoadMoreListener(new SwipeRefreshView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMoreData();
            }
        });
    }
    private void loadMoreData() {
        if(DataResource.getPage()==2){
            return;
        }
        mSwipeRefreshView.setLoading(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                circles.clear();
                circles.addAll(DataResource.getMoreData());
                adapter.notifyDataSetChanged();
                if(DataResource.getPage()==2){
                    Toast.makeText(getActivity(), "已加载所有数据", Toast.LENGTH_SHORT).show();
                }
                // 加载完数据设置为不加载状态，将加载进度收起来
                mSwipeRefreshView.setLoading(false);
            }
        }, 2000);
    }
    private void initData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                circles.clear();
                circles.addAll(DataResource.getData());
                adapter.notifyDataSetChanged();

                Toast.makeText(getActivity(), "刷新了20条数据", Toast.LENGTH_SHORT).show();

                // 加载完数据设置为不刷新状态，将下拉进度收起来
                if (mSwipeRefreshView.isRefreshing()) {
                    mSwipeRefreshView.setRefreshing(false);
                }
            }
        }, 2000);
    }
    public static class DataResource {
        private static List<Circle> datas = new ArrayList<>();
        private static int page = 0;
        private static String photo = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1530011656045&di=376a7119361dbb261c9b6242021a9b31&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fbf096b63f6246b60553a62a0e1f81a4c510fa22a.jpg";
        private static String time="10:00";
        private static String msg="监考老师大家赶快来收感觉克拉时光阿斯科利";

        private static String[] imgs = new String[3];

        public static List<Circle> getData() {
            imgs[0] = photo;
            imgs[1] = photo;
            imgs[2] = photo;
            page = 0;
            datas.clear();
            for (int i = 0; i < 5; i++) {
                datas.add(new Circle(111,"user"+i,time,msg,imgs, photo));
            }

            return datas;
        }

        public static List<Circle> getMoreData() {
            page = page + 1;
            int i;
            for (i = 20 * page; i < 20 * (page + 1); i++) {
                datas.add(new Circle(111,"user"+i,time,msg,imgs, photo));
            }
            return datas;
        }

        public static int getPage() {
            return page;
        }
    }
}
