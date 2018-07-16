package com.syc.zhibo;

import android.content.Intent;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.syc.zhibo.adapter.ViewPagerAdapter;
import com.syc.zhibo.adapter.ViewPagerSquareAdapter;

public class ActivityPhoto extends AppCompatActivity {
    private int startIndex = -1;   //照片初始索引, viewpage中转化后的

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_photo);

        Intent intent = getIntent();
        ViewPager mViewPager = (ViewPager)findViewById(R.id.pager);
        Bundle bd = intent.getExtras();
        final String[] photos = bd.getStringArray("photos");
        final int photoIndex = bd.getInt("index");
        final TextView indexTxt = (TextView) findViewById(R.id.indexTxt);
        indexTxt.setText(photoIndex+1+"/"+photos.length);
        ViewPagerAdapter mAdapter = new ViewPagerAdapter(this, photos, this);
        mViewPager.setAdapter(mAdapter);
        // 将ViewPager定位到中间页（Short.MAX_VALUE/2附近的图片资源数组第1个元素对应的页面）
        // 目的：1.图片个数 >1 才轮播    2.定位到中间页，向左向右都可滑
        if(photos.length > 1) {
            mViewPager.setCurrentItem(((Short.MAX_VALUE / 2) / photos.length) * photos.length+photoIndex, false);
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                // arg0是当前选中的页面的Position
                int currentIndex;
                Log.d("ooooooooo,select", "onPageSelected------>"+arg0);
                int gap = arg0 - startIndex;
                Log.d("ooooooooo,select,gap=", gap+"");
                Log.d("ooooo,select,photoIndex", photoIndex+"");
                if(gap>0){
                    Log.d("ooooo,select,gap>0", "");
                    currentIndex = (photoIndex + gap + 1)%photos.length;
                    Log.d("ooooo,select,currentInd", currentIndex+"");
                    if(currentIndex==0) {
                        currentIndex = photos.length;
                    }
                }else{
                    if(photoIndex+1+gap==0){
                        currentIndex = photos.length;
                    }else if(photoIndex+1+gap>0){
                        currentIndex = photoIndex+1+gap;
                    }else{
                        int n = 0;
                        do {
                            currentIndex = photoIndex+1+gap+photos.length*++n;
                        }while (currentIndex<=0);
                    }
                }
                indexTxt.setText(currentIndex+"/"+photos.length);
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                //只在进入页面时赋值
                if(startIndex==-1){
                    Log.d("ooooooo, startindex=", startIndex+"");
                    startIndex = arg0;
                }
                // arg0 :当前页面，及你点击滑动的页面；arg1:当前页面偏移的百分比；arg2:当前页面偏移的像素位置
                Log.d("oooooooo", "onPageScrolled------>arg0："+arg0+"\nonPageScrolled------>arg1:"+arg1+"\nonPageScrolled------>arg2:"+arg2);
            }
            @Override
            public void onPageScrollStateChanged(int arg0) {
                //arg0 ==1的时表示正在滑动，arg0==2的时表示滑动完毕了，arg0==0的时表示什么都没做。
                if(arg0 == 0){
//                    Log.e("oooooooo", "onPageScrollStateChanged------>0");
                }else if(arg0 == 1){
//                    Log.e("oooooooo", "onPageScrollStateChanged------>1");
                }else if(arg0 == 2){
//                    Log.e("ooooooooooo", "onPageScrollStateChanged------>2");
                }

            }
        });
    }
}
