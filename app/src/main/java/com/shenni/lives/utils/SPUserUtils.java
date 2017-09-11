package com.shenni.lives.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.shenni.lives.MyApplication;
import com.shenni.lives.BuildConfig;


/**
 * SharedPreferences
 */
public class SPUserUtils {

    private static SPUserUtils config;
    //用户编号
    private String uid;
    //用户昵称,
    private String nickname;
    //头像,
    private String headpic;
    //性别
    private String sex;
    //等级
    private String level;
    //金币
    private String wallet;
    private String status;
    //关注
    private String focus;
    //第一次是否成功进入主页
    private boolean isBoolean;

    public static SPUserUtils sharedInstance() {
        if (config == null) {
            config = new SPUserUtils();
        }

//        SharedPreferences prefs = new SecurePreferences(MyApplication.getAppContext(), "", prefsFile);
        SharedPreferences prefs = MyApplication.getContext().getSharedPreferences("SPUserUtils", Context.MODE_PRIVATE);
        if (BuildConfig.DEBUG) {
        }

        config.isBoolean = prefs.getBoolean("isBoolean", false);
        config.uid = prefs.getString("uid", "");
        config.nickname = prefs.getString("nickname", "");
        config.headpic = prefs.getString("headpic", "");
        config.sex = prefs.getString("sex", "");
        config.level = prefs.getString("level", "");
        config.wallet = prefs.getString("wallet", "");
        config.status = prefs.getString("status", "");
        config.focus = prefs.getString("focus", "");
//        config.focus = prefs.getInt("focus", 0);
        return config;
    }


    public void savePreferences() {
        SharedPreferences prefs = MyApplication.getInstance().getSharedPreferences("SPUserUtils", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isBoolean", config.isBoolean);
        editor.putString("uid", config.uid);
        editor.putString("nickname", config.nickname);
        editor.putString("headpic", config.headpic);
        editor.putString("sex", config.sex);
        editor.putString("level", config.level);
        editor.putString("wallet", config.wallet);
        editor.putString("status", config.status);
        editor.putString("focus", config.focus);
//        editor.putInt("focus", config.focus);
        editor.commit();
    }

    public void resetPreferences() {
        SharedPreferences prefs = MyApplication.getInstance().getSharedPreferences("SPUserUtils", Context.MODE_PRIVATE);
//        SharedPreferences prefs = new SecurePreferences(App.getAppContext(), "", prefsFile);

        SharedPreferences.Editor editor = prefs.edit();
        prefs.getBoolean("isBoolean", false);
        prefs.getString("uid", "");
        prefs.getString("nickname", "");
        prefs.getString("headpic", "");
        prefs.getString("sex", "");
        prefs.getString("level", "");
        prefs.getString("wallet", "");
        prefs.getString("status", "");
        prefs.getString("focus", "");
        editor.commit();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFocus() {
        return focus;
    }

    public void setFocus(String focus) {
        this.focus = focus;
    }

    public boolean isBoolean() {
        return isBoolean;
    }

    public void setBoolean(boolean aBoolean) {
        isBoolean = aBoolean;
    }
}