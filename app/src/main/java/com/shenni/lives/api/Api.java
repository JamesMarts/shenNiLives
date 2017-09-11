package com.shenni.lives.api;

/**
 * Created by Administrator on 2017/5/2.
 * function：
 */

public class Api {
        public static final String URL = "http://waejx.cn/zhibo/admin.php/api/";
    /**
     * 登录
     */
    public static final String LOGIN_URL = URL + "login";

    /**
     * 获取用户信息
     */
    public static final String GET_USER_INFORMATION = URL + "getUser";
    /**
     * 获取轮播图
     */
    public static final String GET_BANNER_URL = URL + "banner";

    /**
     * 获取主播列表信息
     */
    public static final String GET_ANCHOR_LIST_URL = URL + "anchorlist";
    /**
     * 获取主播详情
     */
    public static final String GET_ANCHOR_MESSAGE_URL = URL + "details";

    /**
     * 修改用户信息
     */
    public static final String EDIT_USER_INFORMATION = URL + "editUser";
    /**
     * 消息列表
     */
    public static final String GET_USER_MSGLIST = URL + "msglist";
    /**
     * 消息详情
     */
    public static final String GET_USER_GETMSG = URL + "getmsg";
    /**
     * 搜索
     */
    public static final String GET_USE_SEARCH = URL + "search";
    /**
     * 关注
     */
    public static final String GET_USE_GUANZHU = URL + "guanzhu";
    /**
     * 屏蔽主播
     */
    public static final String GET_USE_PBANCHOR = URL + "pbAnchor";
    /**
     * 礼物列表
     */
    public static final String GET_USE_GIFTLIST = URL + "giftlist";
    /**
     * 送礼物
     */
    public static final String GET_USE_SENDGIFT = URL + "sendgift";
    /**
     * 申请主播
     */
    public static final String GET_USE_APPLY = URL + "apply";
    /**
     * 获取设置金额
     */
    public static final String GET_USE_CORE = URL + "core";
    /**
     * 付款渠道
     */
    public static final String GET_USE_PAY = URL + "pay";
    /**
     * 关于我们
     */
    public static final String GET_USE_ABOUT = URL + "about";
    /**
     * 走私/偷窥查询记录
     */
    public static final String GET_USE_PRI_LOG = URL + "pri_log";
    /**
     * 修改主播信息
     */
    public static final String GET_USE_EDITANCHOR = URL + "editAnchor";
    /**
     * 获取更多的词库
     */
    public static final String GET_USE_GETMORECIKU = URL + "getMoreCiku";


}
