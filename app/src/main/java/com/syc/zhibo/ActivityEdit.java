package com.syc.zhibo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.syc.zhibo.util.PhotoBitmapUtils;
import com.syc.zhibo.util.SquareImageView;
import com.syc.zhibo.util.Util;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ActivityEdit extends AppCompatActivity {
    private Activity act;
    public AMapLocationClient mLocationClient = null;
    private final int SDK_PERMISSION_REQUEST = 127;
    private final int REQUEST_CAREMA = 1;
    private final int REQUEST_FILE = 2;
    private final int REQUEST_ALBUM= 3;
    private TextView birthday;
    private  TextView name;
    private int indexClicked;   //点击的小图位置
    private  LinearLayout photosWrap;   //小图的外层
    private String[] photos;
    private SquareImageView photoTop;   //封面图
    private final int TYPE_TAKE_PHOTO = 1;//Uri获取类型判断
    private final int CODE_TAKE_PHOTO = 1;//相机RequestCode
    private Uri photoUri;
    private String photoPath;

    //异步获取定位结果
    private AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            TextView cityTv = (TextView) findViewById(R.id.city);
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //解析定位结果
                    Log.d("oooooooo,province=",amapLocation.getProvince());
                    Log.d("oooooooo,city=",amapLocation.getCity());
                    String province = amapLocation.getProvince().replace("省","").replace("市","");
                    String city = amapLocation.getCity().replace("市", "");
                    if(province.equals(city)){
                        cityTv.setText(province);
                    }else{
                        cityTv.setText(province+"."+city);
                    }
                    mLocationClient.stopLocation();
                    Util.toast(act, "成功获取所在城市");
                }else{
                    Util.toast(act, "获取所在城市失败");
                    cityTv.setText("火星");
                }
            }else{
                Util.toast(act, "获取所在城市失败");
                cityTv.setText("火星");
            }
        }
    };
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ///权限允许后，开始定位
        if(requestCode==SDK_PERMISSION_REQUEST){
            getLocation();
        }else if(requestCode==REQUEST_CAREMA){
        }else if(requestCode==REQUEST_FILE){ //调用相机，创建文件
            takePhoto24();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    public void getLocation(){
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        photoTop = (SquareImageView) findViewById(R.id.photo_top);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        act = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //设置照片
        photos = new String[3];
        photos[0] = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1530271740159&di=bd30f72e195f79765f8389bf7fbf46f6&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F0df431adcbef7609c64a714b24dda3cc7dd99ea5.jpg";
        photos[1] = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1530271740159&di=b6ebe74fb5ce3b1f40217138d6ce8407&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Ffd039245d688d43fc7682126771ed21b0ff43b24.jpg";
        photos[2] = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1530271740159&di=38e2c5cf777c20fe14292a6c2a1353d4&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fd788d43f8794a4c273cb6b0804f41bd5ad6e392c.jpg";
        photosWrap = (LinearLayout) findViewById(R.id.photo_small_wrapper2);
        for(int i=0;i<photos.length;i++){
            SquareImageView image = new SquareImageView(this);
            image.setBackgroundResource(R.drawable.border_2dp);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(212, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,10,0);
            image.setLayoutParams(params);  //设置图片宽高
            Util.setImageBitMap(image, photos[i], this);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            photosWrap.addView(image);
            final int finalI = i;
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("ooooooooooo", "clicked...."+ finalI);
                    indexClicked = finalI;
                    showEditPhoto();
                }
            });
        }
        SquareImageView image = new SquareImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(212, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,10,0);
        image.setLayoutParams(params);  //设置图片宽高
        image.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.img_edit_add_photo));
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        photosWrap.addView(image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPhoto();
                Log.d("ooooooooooo", "clicked....");
            }
        });
        //修改昵称
        RelativeLayout nameWrap = (RelativeLayout) findViewById(R.id.name_wrap);
        name = (TextView) findViewById(R.id.name);
        nameWrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(act, ActivityEditName.class);
                intent.putExtra("name", name.getText());
                startActivityForResult(intent,0);//此处的requestCode应与下面结果处理函中调用的requestCode一致
            }
        });
        //获取城市
        RelativeLayout cityWrap = (RelativeLayout) findViewById(R.id.city_wrap);
        cityWrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
                    ArrayList<String> permissions = new ArrayList<String>();
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
                    }
                    if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                    }
                    if (permissions.size() > 0) {
                        requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
                    }else{
                        getLocation();
                    }
                }else{
                    getLocation();
                }
            }
        });

        //生日选择控件,18-40岁
        birthday = (TextView) findViewById(R.id.birthday);
        RelativeLayout birthdayWrap = (RelativeLayout) findViewById(R.id.birthday_wrap);
        final DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day) {
                birthday.setText(year+"-"+(++month)+"-"+day);
            }
        };
        final DatePickerDialog dialog=new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,listener,1990, 5,1);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
        Date date = new Date();
        date.setYear(new Date().getYear()-18);
        dialog.getDatePicker().setMaxDate(date.getTime());
        date.setYear(new Date().getYear()-40);
        dialog.getDatePicker().setMinDate(date.getTime());
        //点击生日
        birthdayWrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        //修改性别
        RelativeLayout sexWrap = (RelativeLayout)findViewById(R.id.sex_wrap);
        sexWrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(act)
                    .setTitle("")
                    .setMessage("请联系客服修改性别")
                    .setPositiveButton("确定", null)
                    .show();
            }
        });
    }
    //从editname 活动返回数据时调用
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ooooooooo,requestcode=", requestCode+"");
        Log.d("ooooooo,resultcode=", resultCode+"");
        if(requestCode==0 && resultCode==RESULT_OK){    //修改昵称
            Toast.makeText(act, "修改昵称成功", Toast.LENGTH_SHORT).show();
            Bundle bundle = data.getExtras();
            String text =null;
            if(bundle!=null){
                text=bundle.getString("name");
                name.setText(text);
            }
        }else if(requestCode==CODE_TAKE_PHOTO && resultCode==RESULT_OK){    //照相
            photoTop = (SquareImageView) findViewById(R.id.photo_top);
            String photoPath2 = PhotoBitmapUtils.amendRotatePhoto(photoPath, act);

            if (Build.VERSION.SDK_INT >= 24){
                Log.d("ooooooo,photoUri", "="+photoUri+"");
                Bitmap bitmap = null;
                try {
                    photoUri = FileProvider.getUriForFile(this, getPackageName()+".fileProvider", new File(photoPath2));
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                photoTop.setImageBitmap(bitmap);
            }else {
                Bitmap bitmap = BitmapFactory.decodeFile(photoPath2);
                photoTop.setImageBitmap(bitmap);
            }
        }else if(requestCode==REQUEST_ALBUM&& resultCode==RESULT_OK){

        }
    }
    private void showEditPhoto(){
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();  //屏幕宽度
        width = (int) (width*0.8);

        LinearLayout ll = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.edit_photo_set,null);
//        RelativeLayout rl = (RelativeLayout)LayoutInflater.from(this).inflate(R.layout.gift,null);
//        rl.findViewById(R.id.****);
        final PopupWindow popupWindow= new PopupWindow(ll,
                width, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(ll);
        View rootview = LayoutInflater.from(this).inflate(R.layout.activity_edit, null);
        popupWindow.setAnimationStyle(R.style.contextMenuAnim);
        popupWindow.showAtLocation(rootview, Gravity.BOTTOM,0,100);
        Util.setWindowAlpha(this,0.5f);
        popupWindow.setOnDismissListener(new poponDismissListener());

        //取消
        TextView cancel = (TextView) ll.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //删除
        TextView delete = (TextView)ll.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                new AlertDialog.Builder(act)
                        .setTitle("")
                        .setMessage("确定删除吗？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                photosWrap.removeViewAt(indexClicked);
                            }
                        })
                        .setNegativeButton("否", null)
                        .show();
            }
        });
        //查看大图
        TextView bigPhoto = (TextView)ll.findViewById(R.id.big_photo);
        bigPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ooooooooooo", "clicked...."+ indexClicked);
                Intent intent = new Intent(act, ActivityPhoto.class);
                Bundle bd = new Bundle();
                bd.putInt("index", indexClicked);
                bd.putStringArray("photos", photos);
                intent.putExtras(bd);
                startActivity(intent);
            }
        });
        //设为封面图
        TextView cover = (TextView)ll.findViewById(R.id.cover);
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Util.setImageBitMap(photoTop, photos[indexClicked], act);
            }
        });
    }
    private void showAddPhoto(){
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();  //屏幕宽度
        width = (int) (width*0.8);
        LinearLayout ll = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.edit_photo_add,null);
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
                        ActivityCompat.requestPermissions(act, new String[]{android .Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_FILE);
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
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_ALBUM);
            }
        });
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
        Log.d("oooooooooo,uri", "="+FileProvider.getUriForFile(this, getPackageName()+".fileProvider", mediaFile));
        photoUri = FileProvider.getUriForFile(this, getPackageName()+".fileProvider", mediaFile);
        Log.d("ooooo,photoUri", "="+photoUri);
        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(takeIntent, CODE_TAKE_PHOTO);
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
    class poponDismissListener implements PopupWindow.OnDismissListener{
        @Override
        public void onDismiss() {
            Util.setWindowAlpha(act,1f);
        }
    }
}
