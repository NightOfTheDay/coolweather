package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 天气数据总数据
 * Created by Administrator on 2017/6/1.
 */

public class Weather {
    public String status;

    public Basic basic;

    public AQI api;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
