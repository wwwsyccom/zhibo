package com.syc.zhibo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityEditName extends AppCompatActivity {
    private Activity mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mcontext = this;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);

        final EditText  nameEt = (EditText) findViewById(R.id.name);
        final Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        nameEt.setText(name);

        TextView confirm = (TextView) findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameStr = nameEt.getText().toString();
                if(nameStr.trim().equals("")){
                    Toast.makeText(mcontext, "请输入昵称", Toast.LENGTH_SHORT).show();
                }else if(nameStr.trim().length()<3){
                    Toast.makeText(mcontext, "昵称至少3个字符", Toast.LENGTH_SHORT).show();
                }else{
                    intent.putExtra("name", nameStr);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}
