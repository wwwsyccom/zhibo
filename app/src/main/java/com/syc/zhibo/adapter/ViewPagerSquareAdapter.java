package com.syc.zhibo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.syc.zhibo.ActivityPhoto;
import com.syc.zhibo.util.SquareImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerSquareAdapter extends PagerAdapter {
    private Context mContext;
    private String[] mImages; // 图片链接数组
    private List<ImageView> mImageViews; // ImageView集合
    private Activity activity;

    public ViewPagerSquareAdapter(Context context, String[] images, Activity act){
        mContext = context;
        mImages = images;
        mImageViews = new ArrayList<>();
        activity = act;
        initImageViews(mImages);
    }

    /**
     * 初始化ImageViews集合
     * @param imageUrls
     */
    private void initImageViews(final String[] imageUrls) {
        Log.v("ooooooooo", imageUrls.length+"");
        // 根据图片资源数组填充ImageViews集合
        for(int i = 0 ; i < imageUrls.length ; i++){
            final SquareImageView mImageView = new SquareImageView(mContext);
            final int finalI = i;
            new Thread(){
                public void run(){
                    final Bitmap imageBitMap = returnBitMap(imageUrls[finalI]);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mImageView.setImageBitmap(imageBitMap);
                        }
                    });
                }
            }.start();
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageViews.add(mImageView);
            final int finalI1 = i;
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("ooooooooooo", "clicked...."+ finalI);
                    Intent intent = new Intent(activity, ActivityPhoto.class);
                    Bundle bd = new Bundle();
                    bd.putInt("index", finalI1);
                    bd.putStringArray("photos", imageUrls);
                    intent.putExtras(bd);
                    activity.startActivity(intent);
                }
            });
        }

        // ImageViews集合中的图片个数在[2,3]时会存在问题，递归再次填充一遍
        if(mImageViews.size() > 1 && mImageViews.size() < 4){
            initImageViews(imageUrls);
        }
    }

    @Override
    public int getCount() {
        return mImageViews.size() <=1 ? mImageViews.size() : Short.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView mImageView = mImageViews.get(position % mImageViews.size());
        container.addView(mImageView);
        return mImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
    public Bitmap returnBitMap(String url){
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
