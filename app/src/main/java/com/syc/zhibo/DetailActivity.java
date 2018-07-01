package com.syc.zhibo;

import android.content.Context;
import android.graphics.Rect;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.syc.zhibo.adapter.GiftAdapter;
import com.syc.zhibo.adapter.GiftSendAdapter;
import com.syc.zhibo.adapter.ViewPagerAdapter;
import com.syc.zhibo.model.Gift;
import com.syc.zhibo.util.RecyclerMarginDecoration;
import com.syc.zhibo.util.SquareImageView;
import com.syc.zhibo.util.SquareViewPager;
import com.syc.zhibo.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity{
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
    private String[] photos; // 图片资源数组

    private List<Gift> gifts = new ArrayList<Gift>();
    private List<Gift> giftSend = new ArrayList<Gift>();
    private GiftAdapter adapter;
    private GiftSendAdapter adapterSend;
    private PopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mViewPager = (SquareViewPager) findViewById(R.id.photo_pager);
        photos = new String[3];
        photos[0] = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1530271740159&di=bd30f72e195f79765f8389bf7fbf46f6&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F0df431adcbef7609c64a714b24dda3cc7dd99ea5.jpg";
        photos[1] = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1530271740159&di=b6ebe74fb5ce3b1f40217138d6ce8407&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Ffd039245d688d43fc7682126771ed21b0ff43b24.jpg";
        photos[2] = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1530271740159&di=38e2c5cf777c20fe14292a6c2a1353d4&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fd788d43f8794a4c273cb6b0804f41bd5ad6e392c.jpg";
        mAdapter = new ViewPagerAdapter(this, photos, this);
        mViewPager.setAdapter(mAdapter);
        // 将ViewPager定位到中间页（Short.MAX_VALUE/2附近的图片资源数组第1个元素对应的页面）
        // 目的：1.图片个数 >1 才轮播    2.定位到中间页，向左向右都可滑
        if(photos.length > 1) {
            mViewPager.setCurrentItem(((Short.MAX_VALUE / 2) / photos.length) * photos.length, false);
        }

        LinearLayout wrap = (LinearLayout) findViewById(R.id.photo_small_wrapper2);
        for(int i=0;i<photos.length;i++){
            SquareImageView image = new SquareImageView(this);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(212, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,20,0);
            image.setLayoutParams(params);  //设置图片宽高
            Util.setImageBitMap(image, photos[i], this);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            wrap.addView(image);
        }

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.send_gift);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });



        //收到礼物
        RecyclerView listView = (RecyclerView) findViewById(R.id.list_gift);
        listView.addItemDecoration(new RecyclerMarginDecoration(20, 4));
        listView.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new GiftAdapter(gifts);
        listView.setAdapter(adapter);
        initData();

    }
    public void setWindowAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
    private void showPopupWindow() {
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();  //屏幕宽度
        width = (int) (width*0.9);

        View view = LayoutInflater.from(this).inflate(R.layout.gift,null);
        popupWindow= new PopupWindow(view,
                width, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(view);
        View rootview = LayoutInflater.from(this).inflate(R.layout.activity_detail, null);
        popupWindow.setAnimationStyle(R.style.contextMenuAnim);
        popupWindow.showAtLocation(rootview, Gravity.BOTTOM,0,200);

        setWindowAlpha(0.5f);
        popupWindow.setOnDismissListener(new poponDismissListener());

        RecyclerView listView = (RecyclerView) view.findViewById(R.id.gift_wrap);
        listView.addItemDecoration(new RecyclerMarginDecoration(20, 5));
        listView.setLayoutManager(new GridLayoutManager(this, 5));
        giftSend.clear();
        giftSend.addAll(DataResource.getDataSend());
        adapterSend = new GiftSendAdapter(giftSend);
        adapterSend.notifyDataSetChanged();
        listView.setAdapter(adapterSend);

    }
    class poponDismissListener implements PopupWindow.OnDismissListener{
        @Override
        public void onDismiss() {
            setWindowAlpha(1f);
        }

    }
//    class GiftItemDecoration extends RecyclerView.ItemDecoration {
//        private int space;
//        private int col;
//        public GiftItemDecoration(int space, int col) {
//            this.space = space;
//            this.col = col;
//        }
//        @Override
//        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//            if ((parent.getChildLayoutPosition(view)+1) % col == 0){
//                outRect.right= 0;
//            }else{
//                outRect.right= space;
//            }
//        }
//    }
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popupWindow!= null) {
            popupWindow.dismiss();
        }
    }
}
