package com.shenni.lives.utils;

import android.content.Context;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shenni.lives.api.Api;
import com.shenni.lives.bean.CallBackBean;
import com.shenni.lives.bean.CoreBean;
import com.shenni.lives.bean.GoldBean;
import com.shenni.lives.bean.UsersBean;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by Administrator on 2017/5/15.
 * function：
 */

public class GoldPay {

    static CoreBean coreBean;
    static CoreBean.ConBean conBean = new CoreBean.ConBean();
    static UsersBean.UserBean userbran;
    static GoldBean mGoldbean;

    /**
     * 获取设置金额
     */
    public static CoreBean.ConBean getCore(Context contexts) {
//        if ((int) SPUtils.get("User_dfh", 0) == 0 || (int) SPUtils.get("User_gw", 0) == 0 || (int) SPUtils.get("User_zs", 0) == 0)
        OkGo.get(Api.GET_USE_CORE)
                .tag(contexts)
//                .params("channel", "APP")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("获取设置金额：", s);
                        coreBean = GsonUtil.parseJsonWithGson(s, CoreBean.class);
                        if (null != coreBean) {
                            if (coreBean.getStatus() == 1) {
                                conBean = coreBean.getCon();
                                try {
                                SPUtils.put("User_dfh", conBean.getDfh());
                                SPUtils.put("User_gw", conBean.getGw());
                                SPUtils.put("User_zs", conBean.getZs());//400
                                SPUtils.put("User_qq", conBean.getQq());//400
                                SPUtils.put("User_ttk", conBean.getTtk());//400
                                }catch (Exception e){}
//                                OkGo.get(URL)
//                                        .tag(this)
//                                        .execute(new FileCallback() {
//                                            @Override
//                                            public void onBefore(BaseRequest request) {
//                                                toast("正在下载中");
//                                            }
//
//                                            @Override
//                                            public void onSuccess(File file, Call call, Response response) {
//                                                toast("下载完成");
//                                            }
//                                        });
                            }

                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.e("s", e.toString());
                        super.onError(call, response, e);
                    }
                });
//        else {
//            conBean.setDfh((int) SPUtils.get("User_dfh", 0));
//            conBean.setGw((int) SPUtils.get("User_gw", 0));
//            conBean.setZs((int) SPUtils.get("User_zs", 0));
//        }
        return conBean;

    }


    /**
     * 付款成功后修改本用户地数据
     */
    public static UsersBean.UserBean paySuccessAfter(Context context, String uid) {
        if (context != null && !uid.isEmpty())
            try {
                OkGo.get(Api.GET_USER_INFORMATION)
                        .tag(context)
                        .params("uid", uid)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                Log.e("支付成功后获取用户信息：", s);

                                UsersBean bean = GsonUtil.parseJsonWithGson(s, UsersBean.class);
                                if (null != bean && 1 == bean.getStatus()) {
                                    userbran = bean.getUser();
                                    SPUserUtils config = SPUserUtils.sharedInstance();

                                    config.setUid(userbran.getUid());
                                    config.setNickname(userbran.getNickname());
                                    config.setHeadpic(userbran.getHeadpic());
                                    config.setSex(userbran.getSex());
                                    config.setLevel(userbran.getLevel());
                                    config.setWallet(userbran.getWallet());
                                    config.setFocus(userbran.getFocus());

                                    config.savePreferences();
                                }

                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                Log.e("s", e.toString());
                                super.onError(call, response, e);
                            }
                        });
            } catch (Exception e) {
            }
        else
            return null;
        return userbran;
    }

    static CallBackBean mCallBackBean;

    /**
     * 走私/偷窥查询记录
     *
     * @param context
     * @param uid     用户ID
     * @param aid     主播ID
     * @param types   1：立即偷窥，2：约她私播
     */
    public static CallBackBean selectPay(Context context, String uid, String aid, int types) {
        if (context != null && !uid.isEmpty())
            try {
                OkGo.get(Api.GET_USE_PRI_LOG)
                        .tag(context)
                        .params("uid", uid)
                        .params("aid", aid)
                        .params("types", types)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                Log.e("走私/偷窥查询记录：", s);
                                mCallBackBean = GsonUtil.parseJsonWithGson(s, CallBackBean.class);
                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                Log.e("s", e.toString());
                                super.onError(call, response, e);
                            }
                        });
            } catch (Exception e) {
            }
        else
            return null;
        return mCallBackBean;
    }


    /**
     * 支付-开通等级
     *
     * @param level   开通等级（必须）
     * @param amount  付款金额（必须）
     * @param paytype 支付方式（支付宝:alipay 微信:wxpay）
     *                is_a    APP是不是安卓
     * @return
     */
    public static GoldBean payLevel(Context contexts, int level, String amount, String paytype) {
        OkGo.get(Api.GET_USE_PAY)
                .tag(contexts)
//                .params("channel", "APP")
                .params("uid", SPUserUtils.sharedInstance().getUid())
                .params("level", level)
                .params("amount", amount)
                .params("paytype", paytype)
                .params("is_a", 1)

                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("付款-开通等级：", s);
                        mGoldbean = GsonUtil.parseJsonWithGson(s, GoldBean.class);

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.e("s", response.toString());
                        super.onError(call, response, e);
                    }
                });

        return mGoldbean;

    }

    /**
     * 支付-充值金币
     *
     * @param goldnum 充值金币数量
     * @param amount  付款金额（必须）
     * @param paytype 支付方式（支付宝:alipay 微信:wxpay）
     *                is_a    APP是不是安卓
     * @return
     */
    public static GoldBean payGold(Context contexts, int goldnum, String amount, String paytype) {
        OkGo.get(Api.GET_USE_PAY)
                .tag(contexts)
//                .params("channel", "APP")
                .params("uid", SPUserUtils.sharedInstance().getUid())
                .params("goldnum", goldnum)
                .params("amount", amount)
                .params("paytype", paytype)
                .params("is_a", 1)

                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("付款-充值金币：", s);
                        mGoldbean = GsonUtil.parseJsonWithGson(s, GoldBean.class);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.e("s", e.toString());
                        super.onError(call, response, e);
                    }
                });

        return mGoldbean;

    }

    /**
     * 支付-走私=付费观看一次，私播=打赏
     *
     * @param si      走私视频
     * @param aid     主播ID
     * @param types   走私=付费观看一次，私播=打赏
     * @param amount  付款金额（必须）
     * @param paytype 支付方式（支付宝:alipay 微信:wxpay）
     *                is_a    APP是不是安卓
     * @return
     */
    public static GoldBean paySi(Context contexts, int si, String aid, int types, String amount, String paytype) {
        OkGo.get(Api.GET_USE_PAY)
                .tag(contexts)
//                .params("channel", "APP")
                .params("uid", SPUserUtils.sharedInstance().getUid())
                .params("si", si)
                .params("aid", aid)
                .params("types", types)
                .params("amount", amount)
                .params("paytype", paytype)
                .params("is_a", 1)

                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("付款-走私=付费观看一次，私播=打赏：", s);
                        mGoldbean = GsonUtil.parseJsonWithGson(s, GoldBean.class);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.e("s", e.toString());
                        super.onError(call, response, e);
                    }
                });

        return mGoldbean;

    }


    /**
     * 支付-开通等级 充值金币  走私=付费观看一次，私播=打赏
     *
     * @param si      走私视频
     * @param aid     主播ID
     * @param types   走私=付费观看一次，私播=打赏
     * @param level   开通等级
     * @param goldnum 充值金币数量
     * @param amount  付款金额（必须）
     * @param paytype 支付方式（支付宝:alipay 微信:wxpay）
     * @param is_a    APP是不是安卓
     * @return
     */
    public static CoreBean pay(Context contexts, int si, String aid, int types, int level, int goldnum, String amount, String paytype, int is_a) {
        if ((int) SPUtils.get("User_dfh", 0) == 0)
            OkGo.get(Api.GET_USE_PAY)
                    .tag(contexts)
//                    .params("channel", "APP")
                    .params("uid", SPUserUtils.sharedInstance().getUid())
                    .params("is_a", is_a)
                    .params("level", level)
                    .params("goldnum", goldnum)
                    .params("si", si)
                    .params("aid", aid)
                    .params("types", types)
                    .params("amount", amount)
                    .params("paytype", paytype)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Log.e("付款-开通等级：", s);
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            Log.e("s", e.toString());
                            super.onError(call, response, e);
                        }
                    });

        return coreBean;

    }


}
