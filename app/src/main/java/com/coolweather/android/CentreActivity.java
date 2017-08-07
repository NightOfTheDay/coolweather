package com.coolweather.android;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * 中心布局
 */
public class CentreActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener{

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

    private List<Fragment> list = new ArrayList<Fragment>();

    private ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centre);
        ActivityCollector.addActivity(this);
        //无缓存是去服务器查信息
        String weatherId = getIntent().getStringExtra("weather_id");
        this.setWeatherId(weatherId);
        //初始化组件
        initView();
        initPage();//初始化页面
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
        //菜单滑动
        vp = (ViewPager) findViewById(R.id.pager);
        /*实现OnPageChangeListener接口,目的是监听Tab选项卡的变化，然后通知ViewPager适配器切换界面*/
        /*简单来说,是为了让ViewPager滑动的时候能够带着底部菜单联动*/

        vp.addOnPageChangeListener(this);//设置页面切换时的监听器

           vp.getAdapter();

        //实例化布局对象
        layoutInflater = LayoutInflater.from(this);

        //实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.pager);

        /*实现setOnTabChangedListener接口,目的是为监听界面切换），然后实现TabHost里面图片文字的选中状态切换*/
        /*简单来说,是为了当点击下面菜单时,上面的ViewPager能滑动到对应的Fragment*/
        mTabHost.setOnTabChangedListener(this);

        //得到fragment的个数
        int count = fragmentArray.length;
        for( int i = 0; i < count; i++){

            //为每一个Tab按钮设置图标、文字和内容 .newTabSpec(""+i)为每个子标签添加标识
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec("1"+i).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            mTabHost.setTag(i);
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


    /*初始化Fragment*/
    private void initPage() {
        WeatherFragment fragment1 = new WeatherFragment();
        PersonalCenterFragment fragment2 = new PersonalCenterFragment();

        list.add(fragment1);
        list.add(fragment2);

        //绑定Fragment适配器
        vp.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(), list));
        mTabHost.getTabWidget().setDividerDrawable(null);
    }


    @Override
    public void onPageScrollStateChanged(int arg0) {

    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    //页面跳转完毕的时候调用的。
    @Override
    public void onPageSelected(int arg0) {//arg0是表示你当前选中的页面位置Postion
        TabWidget widget = mTabHost.getTabWidget();
        int oldFocusability = widget.getDescendantFocusability();
        widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);//设置View覆盖子类控件而直接获得焦点
        mTabHost.setCurrentTab(arg0);//根据位置Postion设置当前的Tab
        widget.setDescendantFocusability(oldFocusability);//设置取消分割线

    }


    @Override
    public void onTabChanged(String tabId) {//Tab改变的时候调用
        int position = mTabHost.getCurrentTab();
        vp.setCurrentItem(position);//把选中的Tab的位置赋给适配器，让它控制页面切换
    }



}
