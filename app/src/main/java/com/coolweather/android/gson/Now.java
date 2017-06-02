package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 当天度数
 * Created by Administrator on 2017/6/1.
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More{
        @SerializedName("txt")
        public String info;
    }
}
