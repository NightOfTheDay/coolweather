package com.coolweather.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coolweather.android.gson.Forecast;
import com.coolweather.android.gson.Weather;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 天气碎片
 */
public class WeatherFragment extends Fragment {
    private ScrollView weatherLayout;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView apiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    private ImageView bingPicImg;

    public SwipeRefreshLayout swipeRefresh;

    private String mWeatherId;

    public DrawerLayout drawerLayout;

    private Button navButton;

    private View view;//缓存Fragment view

    private String weatherId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //获取活动传来的城市id
        CentreActivity centreActivity = (CentreActivity)getActivity();
        weatherId = centreActivity.getWeatherId();

        //FragmentTabHost切换Fragment时避免重复加载UI
        if(view==null){
            view = inflater.inflate(R.layout.activity_weather,null);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        //设置系统状态为透明
        statusBar();
        //下拉刷新
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        //进度条颜色
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        //初始化组件
        weatherLayout  = (ScrollView) view.findViewById(R.id.weather_layout);
        titleCity = (TextView) view.findViewById(R.id.title_city);
        titleUpdateTime = (TextView) view.findViewById(R.id.title_update_time);
        degreeText = (TextView) view.findViewById(R.id.degree_text);
        weatherInfoText = (TextView) view.findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) view.findViewById(R.id.forecast_layout);
        apiText = (TextView) view.findViewById(R.id.api_text);
        pm25Text = (TextView) view.findViewById(R.id.pm25_text);
        comfortText = (TextView) view.findViewById(R.id.comfort_text);
        carWashText = (TextView) view.findViewById(R.id.car_wash_text);
        sportText = (TextView) view.findViewById(R.id.sport_text);
        bingPicImg = (ImageView) view.findViewById(R.id.bing_pic_img);
        /*滑动菜单*/
        drawerLayout = (DrawerLayout)view.findViewById(R.id.drawer_layout);
        navButton = (Button)view.findViewById(R.id.nav_button);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
         /*点击显示滑动菜单*/
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        //获取缓存信息
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String weatherString = prefs.getString("weather",null);
        if (weatherString != null){
            //有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            //展示数据
            showWeatherInfo(weather);
        } else {
            //无缓存是去服务器查信息
            //String weatherId = getActivity().getIntent().getStringExtra("weather_id");
            //String weatherId = "昆明市";
                    mWeatherId = weatherId;
            weatherLayout.setVisibility(View.INVISIBLE);
            //根据天气id请求城市天气信息
            requestWeather(weatherId);
        }
        //添加每日图片
        String bingPic = prefs.getString("bing_pic",null);
        if (bingPic != null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            //加载每日图片
            loadBingPic();
        }

        //下拉刷新触发
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //根据天气id请求城市天气信息
                requestWeather(mWeatherId);
            }
        });
    }


    /**
     * 根据天气id请求城市天气信息
     * @param wetherId
     */
    public void requestWeather(final String wetherId){
        String weatherUrl = "http://guolin.tech/api/weather?cityid="+wetherId+
                "&key=0203c8c652fc486bbfdc3e4325dbfdbc";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //判断是否正确返回新
                        if (weather != null && "ok".equals(weather.status)){
                            //读取缓存信息
                            SharedPreferences.Editor editor = PreferenceManager.
                                    getDefaultSharedPreferences(getActivity()).edit();
                            editor.putString("weather",responseText);
                            editor.apply();//提交信息
                            //展示数据
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(getActivity(),"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                        //关闭下拉刷新
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        //关闭下拉刷新
                        //关闭下拉刷新
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        //加载每日图片
        loadBingPic();
    }

    /**
     * 处理并展示Weather实体类中的数据
     */
    private void showWeatherInfo(Weather weather){
        //启动定时服务
        Intent intent = new Intent(getActivity(),AutoUpdateService.class);
        getActivity().startService(intent);

        //当天度数加载数据
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature+"度";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText("更新时间："+updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);

        /*预报块加载数据*/
        forecastLayout.removeAllViews();//移出所有子视图
        for(Forecast forecast:weather.forecastList){
            /*预报子块加载数据*/
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }

        /*空气质量块加载数据*/
        if (weather.aqi != null){
            apiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }else{
            apiText.setText("无");
            pm25Text.setText("无");
        }

        /*生活建议块加载数据*/
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运动建议：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);

        /*内容块显示*/
        weatherLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 加载必应每日一图
     */
    public void loadBingPic(){
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.
                        getDefaultSharedPreferences(getActivity()).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();//提交信息
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(getActivity()).load(bingPic).into(bingPicImg);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

        });
    }


    //设置系统状态为透明
    public void statusBar(){
        //设置系统状态为透明
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

}
