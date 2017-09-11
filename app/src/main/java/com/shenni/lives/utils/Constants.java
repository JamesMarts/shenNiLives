package com.shenni.lives.utils;

import android.os.Environment;

import com.shenni.lives.BuildConfig;

import java.io.File;

/*
 *  Application 配置常量
 */
public class Constants {
    /**
     * 保存路径
     **/
    public static final String SavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "shenni";
    /**
     * 性别的跳转依据，可以写其他的值，但一定要>=0
     */
    public static final int SET_SEX_CUT = 11;
    /**
     * 昵称设置的跳转依据，可以写其他的值，但一定要>=0
     */
    public static final int SET_NICK_CUT = 12;
    /**
     * 修改个人信息的的跳转依据，可以写其他的值，但一定要>=0
     */
    public static final int SET_DATA_CUT = 14;

    /**
     * 拍照的跳转依据，可以写其他的值，但一定要>=0
     */
    public static final int REQUEST_TAKE_CAEAME = 111;
    /**
     * 相册的跳转依据，可以写其他的值，但一定要>=0
     */
    public static final int IMAGE_PICKER_SELECT = 222;
    /**
     * 裁剪的跳转依据，可以写其他的值，但一定要>=0
     */
    public static final int PHOTO_REQUEST_CUT = 333;
    /**
     * 刷新的跳转依据，可以写其他的值，但一定要>=0
     */
    public static final int REFRESH_FOCUS = 15;

    /**
     * 收费提示
     */
    public static final int CHAT_CHARGE_TIPS = 16;
    /**
     * 试看到期提示
     */
    public static final int CHAT_FREE_TIPS = 17;

    /**
     * 我的账户的的跳转依据，可以写其他的值，但一定要>=0
     */
    public static final int SET_DATA_ACC = 18;
    /**
     * 我的账户的的跳转依据，可以写其他的值，但一定要>=0
     */
    public static final int SET_DATA_MSS = 19;
    /**
     * 屏蔽，可以写其他的值，但一定要>=0
     */
    public static final int SET_DATA_PBANCHOR = 20;

    /**
     * 是否聚宝云支付
     */
    public static final boolean SET_PAY_TYPE_JBY = false;
    /**
     * 微信支付是否可用
     */
    public static final boolean PAY_TYPE_WX = true;
    /**
     * 支付时是否打开外部浏览器
     */
    public static final boolean PAY_WEB = false;

    /**
     * 微信APP_ID
     */
    public static final String APP_ID = "wx4c1136a1e85f0ceb";
//    public static final String APP_ID = "wx5f044788968f0aed";

    /**
     * 目前使用的是哪个 （根据：SET_PAY_TYPE_JBY判断） 直接使用微信支付宝支付 不经过第四方
     * 一次两个 可以混搭
     * jupay_wx jupay 用于聚宝云微信、支付宝支付
     * wxpay alipay 用于官方的微信、支付宝支付
     * other_wx other 用于拼接支付宝链接 打开外部浏览器跳转到支付宝
     */
    public static final String PAY_TYPE[] = {"mpay_wx", "mpay"};

    /**
     * 链接形式的支付是否是支付宝或者微信支付
     */
    public static final String SELECT_PAY_TYPE[] = {"mpay_wx", "mpay"};


    /**
     * 渠道ID
     */
    public static final String CHANNEL_APP_ID= BuildConfig.APP_CHANNEL_ID;
//    public static final String CHANNEL_APP_ID="10002";
//    public static final String CHANNEL_APP_ID= MyApplication.FwPay_APP_ID;
}
