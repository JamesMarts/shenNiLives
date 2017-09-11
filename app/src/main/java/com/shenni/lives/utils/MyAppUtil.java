package com.shenni.lives.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.shenni.lives.R;
import com.shenni.lives.activity.WebActivity2;

/**
 * Created by Administrator on 2017/7/1.
 * function：
 */

public class MyAppUtil {

    public static void jumpToWeb(Context context, String weburl) {
        try {
            if (StringUtil.isNullOrEmpty(weburl)) {
                ToastUtil.toast(R.string.pay_null_tips);
                return;
            }
            Log.e("jumpToWeb", "jumpToWeb: " + weburl);
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
//                Uri content_url = Uri.parse("http://play.jb51.net");
//        Uri content_url = Uri.parse(weburl.replace("HTTPS","https"));

            StringBuilder sb = new StringBuilder(weburl);
            if (weburl.toLowerCase().startsWith("https")) {
                weburl = sb.replace(0, weburl.indexOf(":"), "https").toString();
            }
            if (Constants.PAY_WEB) {
                Uri content_url = Uri.parse(weburl);
                intent.setData(content_url);
                context.startActivity(intent);
            } else {
                Intent intent2 = new Intent(context, WebActivity2.class);
                intent2.putExtra("url", weburl);
                context.startActivity(intent2);
            }
        } catch (Exception e) {
        }
    }
}