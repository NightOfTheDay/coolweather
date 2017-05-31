package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * 省份数据库
 * Created by Administrator on 2017/5/31.
 */

public class Province extends DataSupport {
    private int id;//主键id
    private String provinceName;//省份名字
    private int provinceCode;//省代号

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
