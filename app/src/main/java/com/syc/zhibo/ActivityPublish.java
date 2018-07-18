package com.syc.zhibo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.syc.zhibo.util.Util;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityPublish extends AppCompatActivity {
    private final int REQUEST_FILE_PM= 2;  //调用相机，读取文件权限
    private final int REQUEST_ALBUM_PM = 3;  //读取相册权限
    private final int CODE_TAKE_PHOTO = 1;//相机RequestCode
    private final int TYPE_TAKE_PHOTO = 1;//Uri获取类型判断
    private final int REQUEST_ALBUM= 3;
    private Activity act;
    private String photoPath;
    private Uri photoUri;
    private LinearLayout photoWrap;
    private ImageView photoAddIcon;
    private TextView tip;
    private int countUp=0;  //已上传照片张数
    private int countRemain=3;  //还剩下的张数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        act = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        photoAddIcon = (ImageView) findViewById(R.id.add);
        photoAddIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPhoto();
            }
        });
    }
    private void showAddPhoto(){
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();  //屏幕宽度
        width = (int) (width*0.8);
        LinearLayout ll = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.edit_photo_add,null);
        final PopupWindow popupWindow= new PopupWindow(ll,
                width, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(ll);
        View rootview = LayoutInflater.from(this).inflate(R.layout.activity_edit, null);
        popupWindow.setAnimationStyle(R.style.contextMenuAnim);
        popupWindow.showAtLocation(rootview, Gravity.BOTTOM,0,100);
        Util.setWindowAlpha(this,0.5f);
        popupWindow.setOnDismissListener(new poponDismissListener());

        TextView camera = (TextView) ll.findViewById(R.id.camera);
        TextView album = (TextView)ll.findViewById(R.id.album);
        TextView cancel = (TextView) ll.findViewById(R.id.cancel);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (Build.VERSION.SDK_INT >= 24) {
                    //判断是否有存取文件权限
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(act, new String[]{android .Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_FILE_PM);
                    }else{
                        takePhoto24();
                    }
                } else {
                    photoUri = getMediaFileUri(TYPE_TAKE_PHOTO);
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(takeIntent, CODE_TAKE_PHOTO);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                //检查权限
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_ALBUM_PM);
                }else {
                    choosePhoto();
                }
            }
        });
    }
    class poponDismissListener implements PopupWindow.OnDismissListener{
        @Override
        public void onDismiss() {
            Util.setWindowAlpha(act,1f);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ///权限允许后，开始定位
        if(requestCode== REQUEST_FILE_PM){ //调用相机，创建文件
            takePhoto24();
        }else if(requestCode==REQUEST_ALBUM_PM){    //调用相册
            choosePhoto();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    public void takePhoto24(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "cameraZb");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("oooooooooo", "11111111111");
            }
        }
        //创建Media File
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        photoPath = mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg";
        File mediaFile = new File(photoPath);
        Log.d("oooooooooo,uri", "="+ FileProvider.getUriForFile(this, getPackageName()+".fileProvider", mediaFile));
        photoUri = FileProvider.getUriForFile(this, getPackageName()+".fileProvider", mediaFile);
        Log.d("ooooo,photoUri", "="+photoUri);
        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(takeIntent, CODE_TAKE_PHOTO);
    }
    public void choosePhoto(){
        Intent intent = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_ALBUM);
    }
    public Uri getMediaFileUri(int type){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "cameraZb");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        //创建Media File
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        photoPath = mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg";
        File mediaFile;
        if (type == TYPE_TAKE_PHOTO) {
            mediaFile = new File(photoPath);
        } else {
            return null;
        }
        return Uri.fromFile(mediaFile);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        photoWrap = (LinearLayout) findViewById(R.id.photo_wrap);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CODE_TAKE_PHOTO && resultCode==RESULT_OK){    //照相
            int degree = Util.readPictureDegree(photoPath);
            Bitmap bitmap = null;

            if (Build.VERSION.SDK_INT >= 24){
                Log.d("ooooooo,photoUri", "="+photoUri+"");
                try {
                    photoUri = FileProvider.getUriForFile(this, getPackageName()+".fileProvider", new File(photoPath));
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else {
                bitmap = BitmapFactory.decodeFile(photoPath);
            }
            bitmap = Util.rotateBitmap(bitmap, degree);
            addPhoto(bitmap);
        }else if(requestCode==REQUEST_ALBUM&& resultCode==RESULT_OK){
            try {
                Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String path = cursor.getString(columnIndex); //获取照片路径
                cursor.close();
                int degree = Util.readPictureDegree(path);
                Bitmap bitmap = null;
                try {
                    FileInputStream fis = new FileInputStream(path);
                    bitmap= BitmapFactory.decodeStream(fis);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bitmap = Util.rotateBitmap(bitmap, degree);
                addPhoto(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void addPhoto(Bitmap bitmap){
        ImageView photo = new ImageView(this);
        photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        photo.setImageBitmap(bitmap);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
        params.setMargins(0,0,10,0);
        photo.setLayoutParams(params);  //设置图片宽高
        photoWrap.addView(photo, photoWrap.getChildCount()-1);
        tip = (TextView) findViewById(R.id.tip);
        tip.setText("共上传"+(++countUp)+"张照片,还可上传"+(--countRemain)+"张");
        if(countRemain==0){
            photoAddIcon.setVisibility(View.INVISIBLE);
        }
    }
}
