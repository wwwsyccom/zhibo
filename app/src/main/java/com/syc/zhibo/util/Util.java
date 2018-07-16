package com.syc.zhibo.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Util {
    public  static Bitmap returnBitMap(String url){
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
    public static void setImageBitMap(final SquareImageView img, final String url, final Activity act){
        new Thread(){
            public void run(){
                final Bitmap imageBitMap = returnBitMap(url);
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        img.setImageBitmap(imageBitMap);
                    }
                });
            }
        }.start();
    }
    public static void setImageBitMap(final ImageView img, final String url, final Activity act){
        new Thread(){
            public void run(){
                final Bitmap imageBitMap = returnBitMap(url);
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        img.setImageBitmap(imageBitMap);
                    }
                });
            }
        }.start();
    }
    public static void setWindowAlpha(Activity activity, float bgAlpha)
    {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }
    public static void toast(Context context, String text){
        Toast makeText = Toast.makeText(context, null,  Toast.LENGTH_SHORT);
        makeText.setText(text);
        makeText.show();
    }
}
