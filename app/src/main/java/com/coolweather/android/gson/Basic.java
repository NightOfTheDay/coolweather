package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 城市天气数据类
 * Created by Administrator on 2017/6/1.
 */

public class Basic {
    @SerializedName("city")
    public String cityName;//城市名称
    @SerializedName("id")
    public String weatherId;//天气id

    public Update update;

    public class Update{
        @SerializedName("loc")
        public String updateTime;//更新时间
    }
}
