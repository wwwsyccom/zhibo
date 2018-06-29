package com.syc.zhibo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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

    private FragmentManager fManager;
    private HomeFragment fgHome;
    private CircleFragment fgCircle;
    private MsgFragment fgMsg;
    private MyFragment fgMy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        circle.performClick();

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
                        fgHome = new HomeFragment();
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
                        fgCircle = new CircleFragment();
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
                        fgMsg= new MsgFragment();
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
                        fgMy= new MyFragment();
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

}
