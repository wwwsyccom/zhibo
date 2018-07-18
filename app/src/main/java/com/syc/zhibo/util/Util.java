package com.syc.zhibo.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.TypedValue;
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
    /**
     * 旋转图片，使图片保持正确的方向。
     *
     * @param bitmap  原始图片
     * @param degrees 原始图片的角度
     * @return Bitmap 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || null == bitmap) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bmp;
    }


    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 拍摄图片的完整路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return degree;
        }
        return degree;
    }
    public static int dip2px(Context context, float dipValue)
    {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
    }
}
