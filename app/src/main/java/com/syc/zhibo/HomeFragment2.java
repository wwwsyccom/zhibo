package com.syc.zhibo;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.syc.zhibo.util.SwipeRefreshView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment2 extends Fragment implements RadioGroup.OnCheckedChangeListener{
    private RadioGroup navGroup;
    private RadioButton navRecommend;
    private RadioButton navActive;
    private RadioButton navConcern;

    private List<String> mList;
    private StringAdapter mAdapter;
    private SwipeRefreshView mSwipeRefreshView;


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
        mSwipeRefreshView.setItemCount(20);
        ListView listView = (ListView) view.findViewById(R.id.list);
        mList = new ArrayList<>();
        mAdapter = new StringAdapter();
        listView.setAdapter(mAdapter);
        // 设置下拉进度的背景颜色，默认就是白色的
        mSwipeRefreshView.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mSwipeRefreshView.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_blue_bright, R.color.colorPrimaryDark,
                android.R.color.holo_orange_dark, android.R.color.holo_red_dark, android.R.color.holo_purple);
        // 手动调用,通知系统去测量
        mSwipeRefreshView.measure(0, 0);
        mSwipeRefreshView.setRefreshing(true);

        initEvent();
        initData();

        return view;
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
//        mSwipeRefreshView.setLoadingRefresh(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mList.clear();
                mList.addAll(DataResource.getData());
                mAdapter.notifyDataSetChanged();

                Toast.makeText(getActivity(), "刷新了20条数据", Toast.LENGTH_SHORT).show();
//                mSwipeRefreshView.setLoadingRefresh(false);

                // 加载完数据设置为不刷新状态，将下拉进度收起来
                if (mSwipeRefreshView.isRefreshing()) {
                    mSwipeRefreshView.setRefreshing(false);
                }
            }
        }, 2000);
    }
    private void loadMoreData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mList.clear();
                mList.addAll(DataResource.getMoreData());
                Toast.makeText(getActivity(), "加载了" + 20 + "条数据", Toast.LENGTH_SHORT).show();

                // 加载完数据设置为不加载状态，将加载进度收起来
                mSwipeRefreshView.setLoading(false);
            }
        }, 2000);
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
    /**
     * 适配器
     */
    private class StringAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getActivity(), android.R.layout.simple_list_item_1, null);
            }

            TextView tv = (TextView) convertView;
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(0, 20, 0, 20);
            tv.setText(mList.get(position));
            return convertView;
        }
    }
    public static class DataResource {
        private static List<String> datas = new ArrayList<>();
        private static int page = 0;

        public static List<String> getData() {
            page = 0;
            datas.clear();
            for (int i = 0; i < 5; i++) {
                datas.add("我是天才" + i + "号");
            }

            return datas;
        }

        public static List<String> getMoreData() {
            page = page + 1;
            int i = 0;
            for (i = 20 * page; i < 20 * (page + 1); i++) {
                datas.add("我是天才" + i + "号");
            }

            Log.v("000000", i+"");
            return datas;
        }
    }

}
