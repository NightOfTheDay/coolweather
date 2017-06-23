package com.coolweather.android;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;

/**
 * 中心布局
 */
public class CentreActivity extends FragmentActivity {

    //定义FragmentTabHost对象
    private FragmentTabHost mTabHost;

    //定义一个布局
    private LayoutInflater layoutInflater;

    //定义数组来存放Fragment界面
    private Class fragmentArray[] = {WeatherFragment.class,PersonalCenterFragment.class};

    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.weather_f,R.drawable.me_f};

    private static String weatherId;

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centre);
        ActivityCollector.addActivity(this);
        //无缓存是去服务器查信息
        String weatherId = getIntent().getStringExtra("weather_id");
        this.setWeatherId(weatherId);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取底部栏碎片
        PersonalCenterFragment pcf = (PersonalCenterFragment) getSupportFragmentManager().findFragmentByTag("11");
        if(pcf!=null && !"".equals(pcf)){
            pcf.assignment();
        }

    }

    /**
     * 初始化组件
     */
    private void initView(){
        //实例化布局对象
        layoutInflater = LayoutInflater.from(this);

        //实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        //得到fragment的个数
        int count = fragmentArray.length;
        for( int i = 0; i < count; i++){

            //为每一个Tab按钮设置图标、文字和内容 .newTabSpec(""+i)为每个子标签添加标识
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec("1"+i).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            //设置Tab按钮的背景
            //mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);

        }


        mTabHost.getTabWidget().getChildTabViewAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //恢复系统状态为透明
                if (Build.VERSION.SDK_INT >= 21){
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                }
                //由于已经覆写了点击方法，所以需要实现tab切换
                mTabHost.setCurrentTab(0);
                mTabHost.getTabWidget().requestFocus(View.FOCUS_FORWARD);
            }
        });
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index){
        View view = layoutInflater.inflate(R.layout.bottom_menu_son, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.son_text);
        imageView.setImageResource(mImageViewArray[index]);
        return view;
    }


}
