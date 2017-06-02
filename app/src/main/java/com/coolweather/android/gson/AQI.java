package com.coolweather.android.gson;

/**
 * 空气质量
 * Created by Administrator on 2017/6/1.
 */

public class AQI {

    public AQICity city;

    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
