package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * 人员信息表
 * Created by Administrator on 2017/6/2.
 */

public class User  extends DataSupport {

    private int id;
    private String account;//账户
    private String pass;//密码
    private String phone;//手机号
    private String remember;//账户密码 0不记住，1记住
    private String quit;//退出状态 0退出 1没有退出

    public String getQuit() {
        return quit;
    }

    public void setQuit(String quit) {
        this.quit = quit;
    }

    public String getRemember() {
        return remember;
    }

    public void setRemember(String remember) {
        this.remember = remember;
    }

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
