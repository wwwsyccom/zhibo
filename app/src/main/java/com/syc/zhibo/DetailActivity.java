package com.syc.zhibo;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.syc.zhibo.adapter.ViewPagerAdapter;
import com.syc.zhibo.util.SquareImageView;
import com.syc.zhibo.util.SquareViewPager;
import com.syc.zhibo.util.Util;

public class DetailActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
    private String[] photos; // 图片资源数组

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
    }
}
