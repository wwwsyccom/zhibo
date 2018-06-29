package com.syc.zhibo;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.syc.zhibo.adapter.HomeAdapter;
import com.syc.zhibo.model.User;
import com.syc.zhibo.util.SwipeRefreshView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements RadioGroup.OnCheckedChangeListener{
    private RadioGroup navGroup;
    private RadioButton navRecommend;
    private RadioButton navActive;
    private RadioButton navConcern;

    private SwipeRefreshView mSwipeRefreshView;

    private List<User> users = new ArrayList<User>();
    private HomeAdapter adapter;

    private Boolean dataAllFetched = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_home,container,false);
        navGroup = (RadioGroup) view.findViewById(R.id.nav_group);
        navRecommend = (RadioButton) view.findViewById(R.id.nav_recommend);
        navActive = (RadioButton) view.findViewById(R.id.nav_active);
        navConcern= (RadioButton) view.findViewById(R.id.nav_concern);

        navGroup.setOnCheckedChangeListener(this);
        navRecommend.setChecked(true);

        mSwipeRefreshView = (SwipeRefreshView) view.findViewById(R.id.list_wrap);
        mSwipeRefreshView.setProBar((ProgressBar)view.findViewById(R.id.probar));
        RecyclerView listView = (RecyclerView) view.findViewById(R.id.list);
        listView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

//      listView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        adapter = new HomeAdapter(users);
        listView.setAdapter(adapter);

        initData();
        initEvent();

        return view;
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.nav_recommend:
                break;
            case R.id.nav_active:
                break;
            case R.id.nav_concern:
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
    private void initData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                users.clear();
                users.addAll(DataResource.getData());
                adapter.notifyDataSetChanged();

                Toast.makeText(getActivity(), "刷新了20条数据", Toast.LENGTH_SHORT).show();

                // 加载完数据设置为不刷新状态，将下拉进度收起来
                if (mSwipeRefreshView.isRefreshing()) {
                    mSwipeRefreshView.setRefreshing(false);
                }
            }
        }, 2000);
    }
    private void loadMoreData() {
        if(DataResource.getPage()==2){
            return;
        }
        mSwipeRefreshView.setLoading(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                users.clear();
                users.addAll(DataResource.getMoreData());
                adapter.notifyDataSetChanged();
                if(DataResource.getPage()==2){
                    Toast.makeText(getActivity(), "已加载所有数据", Toast.LENGTH_SHORT).show();
                }
                // 加载完数据设置为不加载状态，将加载进度收起来
                mSwipeRefreshView.setLoading(false);
                //
            }
        }, 2000);
    }

    public static class DataResource {
        private static List<User> datas = new ArrayList<>();
        private static int page = 0;
        private static String photo = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1530011656045&di=376a7119361dbb261c9b6242021a9b31&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fbf096b63f6246b60553a62a0e1f81a4c510fa22a.jpg";
        private static int price = 20;
        private static int access = 90;

        public static List<User> getData() {
            page = 0;
            datas.clear();
            for (int i = 0; i < 25; i++) {
                datas.add(new User("user"+i, photo, price, access));
            }

            return datas;
        }

        public static List<User> getMoreData() {
            page = page + 1;
            int i;
            for (i = 20 * page; i < 20 * (page + 1); i++) {
                datas.add(new User("user"+i, photo, price, access));
            }
            return datas;
        }

        public static int getPage() {
            return page;
        }
    }




}
