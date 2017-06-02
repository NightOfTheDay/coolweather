package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * 人员信息表
 * Created by Administrator on 2017/6/2.
 */

public class User  extends DataSupport {

    private int id;
    private String account;
    private String pass;
    private String phone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
