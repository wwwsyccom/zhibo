package com.syc.zhibo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.syc.zhibo.update.DownLoadService;
import com.syc.zhibo.update.util.DeviceUtils;
import com.syc.zhibo.update.util.ExternalPermissionUtils;
import com.syc.zhibo.util.CommonProgressDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView findIcon;
    private ImageView circleIcon;
    private ImageView myIcon;
    private ImageView   msgIcon;
    private TextView    find;
    private TextView circle;
    private TextView msg;
    private TextView my;
    private TextView msgNum;
    private Activity mContext;

    private FragmentManager fManager;
    private FgHome fgHome;
    private FgCircle fgCircle;
    private FgMsg fgMsg;
    private FgMy fgMy;

    private MyReceiver receiver=null;
    private CommonProgressDialog pBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startActivity(new Intent(this, ActivityEdit.class));

        super.onCreate(savedInstanceState);
        //设置状态栏背景和字体图标颜色
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //设置状态栏白色背景
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
            //设置状态栏字体图标颜色为深色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_main);
        mContext = this;


        findIcon = (ImageView) findViewById(R.id.tab_menu_find_icon);
        circleIcon = (ImageView) findViewById(R.id.tab_menu_circle_icon);
        msgIcon = (ImageView) findViewById(R.id.tab_menu_msg_icon);
        myIcon = (ImageView) findViewById(R.id.tab_menu_my_icon);
        find = (TextView) findViewById(R.id.tab_menu_find);
        circle = (TextView) findViewById(R.id.tab_menu_circle);
        msg = (TextView) findViewById(R.id.tab_menu_msg);
        my = (TextView) findViewById(R.id.tab_menu_my);
        msgNum = (TextView) findViewById(R.id.tab_menu_msg_num);

        findIcon.setOnClickListener(this);
        circleIcon.setOnClickListener(this);
        msgIcon.setOnClickListener(this);
        myIcon.setOnClickListener(this);
        find.setOnClickListener(this);
        circle.setOnClickListener(this);
        msg.setOnClickListener(this);
        my.setOnClickListener(this);
        msgNum.setOnClickListener(this);

        fManager = getFragmentManager();
        //点亮首页图标
        msg.performClick();

//        checkUpdate(false);

    }
    public void showPbar(){
        pBar = new CommonProgressDialog(MainActivity.this);
        pBar.setCanceledOnTouchOutside(false);
        pBar.setTitle("正在下载");
        pBar.setCustomTitle(LayoutInflater.from(
                MainActivity.this).inflate(
                R.layout.title_dialog, null));
        pBar.setMessage("正在下载");
        pBar.setIndeterminate(true);
        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pBar.setCancelable(false);
        pBar.show();
        //注册广播接收器
        receiver=new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.syc.zhibo.update.DownLoadService");
        MainActivity.this.registerReceiver(receiver,filter);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //检验是否获取权限，如果获取权限，外部存储会处于开放状态，会弹出一个toast提示获得授权
                    String sdCard = Environment.getExternalStorageState();
                    if (sdCard.equals(Environment.MEDIA_MOUNTED)){
//                        Toast.makeText(this,"获得授权",Toast.LENGTH_LONG).show();
                        this.startService(new Intent(this, DownLoadService.class));
                        showPbar();
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            int percent =bundle.getInt("percent");
            Log.d("ooooooo,percent=", percent+"");
            pBar.setProgress(percent);
            if(percent==100){
                mContext.finish();
            }
        }
    }
    public void onClick(View view){
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        if(view.isSelected()){
            return;
        }else{
            resetTabIcon();
            switch (view.getId()){
                case R.id.tab_menu_find:
                case R.id.tab_menu_find_icon:
                    find.setSelected(true);
                    findIcon.setSelected(true);
                    if(fgHome == null){
                        fgHome = new FgHome();
                        fTransaction.add(R.id.fg_wrapper,fgHome);
                    }else{
                        fTransaction.show(fgHome);
                    }
                    break;
                case R.id.tab_menu_circle:
                case R.id.tab_menu_circle_icon:
                    circle.setSelected(true);
                    circleIcon.setSelected(true);
                    if(fgCircle== null){
                        fgCircle = new FgCircle();
                        fTransaction.add(R.id.fg_wrapper,fgCircle);
                    }else{
                        fTransaction.show(fgCircle);
                    }
                    break;
                case R.id.tab_menu_msg:
                case R.id.tab_menu_msg_icon:
                case R.id.tab_menu_msg_num:
                    msg.setSelected(true);
                    msgIcon.setSelected(true);
                    if(fgMsg== null){
                        fgMsg= new FgMsg();
                        fTransaction.add(R.id.fg_wrapper,fgMsg);
                    }else{
                        fTransaction.show(fgMsg);
                    }
                    break;
                case R.id.tab_menu_my:
                case R.id.tab_menu_my_icon:
                    my.setSelected(true);
                    myIcon.setSelected(true);
                    if(fgMy== null){
                        fgMy= new FgMy();
                        fTransaction.add(R.id.fg_wrapper,fgMy);
                    }else{
                        fTransaction.show(fgMy);
                    }
                    break;
            }
            fTransaction.commit();
        }
    }
    //重置所有导航图标至灰
    public void resetTabIcon(){
        findIcon.setSelected(false);
        circleIcon.setSelected(false);
        msgIcon.setSelected(false);
        myIcon.setSelected(false);

        find.setSelected(false);
        circle.setSelected(false);
        msg.setSelected(false);
        my.setSelected(false);

    }
    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(fgHome != null)fragmentTransaction.hide(fgHome);
        if(fgCircle != null)fragmentTransaction.hide(fgCircle);
        if(fgMsg!= null)fragmentTransaction.hide(fgMsg);
        if(fgMy!= null)fragmentTransaction.hide(fgMy);
    }

    public void checkUpdate(final boolean isToast) {
        /**
         * 在这里请求后台接口，获取更新的内容和最新的版本号
         */
        Log.d("ooooooooo", "checkupdate........");
        // 版本的更新信息
        String version_info = "更新内容\n" + "    1. 车位分享异常处理\n" + "    2. 发布车位折扣格式统一\n" + "    ";
        int mVersion_code = DeviceUtils.getVersionCode(this);// 当前的版本号
        int nVersion_code = 2;
        if (mVersion_code < nVersion_code) {
            // 显示提示对话
            showNoticeDialog(version_info);
        } else {
            if (isToast) {
                Toast.makeText(this, "已经是最新版本", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /**
     * 显示更新对话框
     *
     * @param version_info
     */
    private void showNoticeDialog(String version_info) {
        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        mDialog.setTitle("版本更新");
        mDialog.setMessage(version_info);
        mDialog.setCancelable(false);
        mDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if (ExternalPermissionUtils.isGrantExternalRW(mContext, 1)) {
                    mContext.startService(new Intent(mContext, DownLoadService.class));
                    showPbar();
                }
            }
        }).create().show();
    }
}
