package com.shenni.lives.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.fanwei.jubaosdk.common.core.OnPayResultListener;
import com.fanwei.jubaosdk.common.util.ToastUtil;
import com.fanwei.jubaosdk.shell.FWPay;
import com.fanwei.jubaosdk.shell.PayOrder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shenni.lives.R;
import com.shenni.lives.api.Api;
import com.shenni.lives.base.BaseActivity;
import com.shenni.lives.bean.CoreBean;
import com.shenni.lives.bean.GoldBean;
import com.shenni.lives.bean.PayBean;
import com.shenni.lives.bean.UsersBean;
import com.shenni.lives.bean.WXPayBean;
import com.shenni.lives.utils.Constants;
import com.shenni.lives.utils.DialogUtil;
import com.shenni.lives.utils.GsonUtil;
import com.shenni.lives.utils.MyAppUtil;
import com.shenni.lives.utils.PPWUtil;
import com.shenni.lives.utils.PayResult;
import com.shenni.lives.utils.SPUserUtils;
import com.shenni.lives.utils.SPUtils;
import com.shenni.lives.utils.StringUtil;
import com.squareup.picasso.Picasso;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

import static com.shenni.lives.R.id.tv_mine_kefu;
import static com.shenni.lives.activity.OpenActivity.userbran;
import static com.shenni.lives.utils.Constants.SELECT_PAY_TYPE;


/**
 * 我的账户
 */
public class MyAccActivity extends BaseActivity implements OnPayResultListener {

    @InjectView(R.id.iv_mine_head)
    CircleImageView ivMineHead;
    @InjectView(R.id.tv_mine_name)
    TextView tvMineName;
    @InjectView(R.id.tv_mine_gold)
    TextView tvMineGold;
    @InjectView(R.id.tv_mine_golds)
    TextView tvMineGolds;
    @InjectView(R.id.iv_acc1)
    ImageView ivAcc1;
    @InjectView(R.id.tv_marquess)
    TextView tvMarquess;
    @InjectView(R.id.tv_acc_price)
    TextView tvAccPrice;
    @InjectView(R.id.iv_acc2)
    ImageView ivAcc2;
    @InjectView(R.id.tv_king)
    TextView tvKing;
    @InjectView(R.id.tv_king_price)
    TextView tvKingPrice;
    @InjectView(tv_mine_kefu)
    TextView tvMineKefu;
    @InjectView(R.id.iv_level)
    ImageView ivLevel;
    @InjectView(R.id.textView2)
    TextView textView2;


    @InjectView(R.id.ev_mine_dfh)
    TextView evMineDfh;
    @InjectView(R.id.tv_mine_gw)
    TextView tvMineGw;

    private View mPopWindow;
    private GoldBean mGoldbean;

    private String mMineDfh = "0";
    private String mMineGw = "0";

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_acc);
        ButterKnife.inject(this);
        getCore();
        InitView();
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
    }


    public void InitView() {
        setTitleBar("我的账户");
        Picasso.with(this)
                .load(config.getHeadpic())
                .resize(100, 100).centerCrop()
                .error(R.drawable.icon_kongbai)
                .into(ivMineHead);
        tvMineName.setText(config.getNickname());
        tvMineGolds.setText(config.getUid());
        ivLevel.setBackgroundResource(levetype(Integer.valueOf(config.getLevel())));


        OkGo.get(Api.GET_USER_INFORMATION)
                .tag(this)
                .params("uid", uid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("用户信息：", s);
                        UsersBean bean = GsonUtil.parseJsonWithGson(s, UsersBean.class);
                        if (null != bean && 1 == bean.getStatus()) {
                            UsersBean.UserBean userbran = bean.getUser();
                            Picasso.with(MyAccActivity.this)
                                    .load(userbran.getHeadpic())
                                    .into(ivMineHead);

                            tvMineName.setText(userbran.getNickname());
                            tvMineGolds.setText(userbran.getUid());
                            ivLevel.setBackgroundResource(levetype(Integer.valueOf(userbran.getLevel())));
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.e("s", e.toString());
                        super.onError(call, response, e);
                    }
                });
    }


    @OnClick(R.id.tv_mine_kefu)
    public void onViewClicked() {
        try {
            final String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=%s&version=1";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(qqUrl, (String) SPUtils.get("User_qq", "")))));
        } catch (Exception e) {
            toast("请先安装QQ");
        }
    }

    private int levetype(int level) {
        switch (level) {
            case 0:
                return R.drawable.icon_dredge4;
            case 1:
                return R.drawable.icon_shuaiguo;
            case 2:
                return R.drawable.icon_tuhao;
            default:
                return R.drawable.icon_dredge4;
        }
    }

    private boolean refersh = false;

    @Override
    public void leftAction() {
        if (refersh) {
            Intent intent = new Intent();
            intent.putExtra("refersh", "");
            setResult(RESULT_OK, intent);
        }
        super.leftAction();
    }

    @Override
    public void onBackPressed() {
        if (refersh) {
            Intent intent = new Intent();
            intent.putExtra("refersh", "");
            setResult(RESULT_OK, intent);
            finish();
        } else finish();

    }

    /**
     * 获取设置金额
     */
    public void getCore() {
        OkGo.get(Api.GET_USE_CORE)
                .tag(MyAccActivity.this)
                //.params("channel", "APP")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("获取设置金额：", s);
                        CoreBean coreBean = GsonUtil.parseJsonWithGson(s, CoreBean.class);
                        if (null != coreBean) {
                            if (coreBean.getStatus() == 1) {
                                CoreBean.ConBean conBean = coreBean.getCon();
                                if (null != conBean) {
                                    evMineDfh.setText((mMineDfh = conBean.getDfh()) + "元/每月");
                                    tvMineGw.setText((mMineGw = conBean.getGw()) + "元/每月");
                                    if (!StringUtil.isNullOrEmpty(conBean.getQq()))
                                        SPUtils.put("User_qq", conBean.getQq());//400
                                }
                            }

                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.e("s", e.toString());
                        super.onError(call, response, e);
                    }
                });

    }


    String paytype = "";

    @OnClick({R.id.iv_macc_gold1, R.id.iv_macc_gold2, R.id.iv_macc_gold3, R.id.iv_macc_gold4, R.id.iv_macc_level1, R.id.iv_macc_level2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_macc_level1:
                ppwPayShow("帅锅");
//                mFwPAy(String.valueOf(conBean.getDfh()), getString(R.string.live_pay_level1), 1);
                break;
            case R.id.iv_macc_level2:
                ppwPayShow("土豪");
//                mFwPAy(String.valueOf(conBean.getGw()), getString(R.string.live_pay_level2), 1);
                break;
            case R.id.iv_macc_gold1:
                ppwPayShow("500猫币");
//                mFwPAy("49", getString(R.string.gold_Pay1_m), 0);
                break;
            case R.id.iv_macc_gold2:
                ppwPayShow("1000猫币");
//                mFwPAy("100", getString(R.string.gold_Pay2_m), 0);
                break;
            case R.id.iv_macc_gold3:
                ppwPayShow("2000猫币");
//                mFwPAy("200", getString(R.string.gold_Pay3_m), 0);
                break;
            case R.id.iv_macc_gold4:
                ppwPayShow("5000猫币");
//                mFwPAy("500", getString(R.string.gold_Pay4_m), 0);
                break;
        }
    }


    /**
     * 充值选择页面PPW
     */
    private void ppwPayShow(final String type) {

        mPopWindow = PPWUtil.showPop(this, R.id.iv_macc_gold1, R.layout.activity_photo_graph);
        if (null != mPopWindow) {
            Button mWx = (Button) mPopWindow.findViewById(R.id.btn_selectphoto);
            Button mAli = (Button) mPopWindow.findViewById(R.id.btn_creame);
            Button mclose = (Button) mPopWindow.findViewById(R.id.btn_cancle);

            mWx.setText("微信支付");
            mAli.setText("支付宝支付");
            mclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PPWUtil.dismissPop();
                }
            });
            mWx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    paytype = "wxpay";
                    paytype = Constants.PAY_TYPE[0];
                    payGold(type, paytype);
                    PPWUtil.dismissPop();
                }
            });
            mAli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    paytype = "alipay";
                    paytype = Constants.PAY_TYPE[1];
                    payGold(type, paytype);
                    PPWUtil.dismissPop();
                }
            });


        }

    }

    private void payGold(String type, String paytype) {
        switch (type) {
            case "500猫币":
                if (Constants.SET_PAY_TYPE_JBY)
                    payGolds(MyAccActivity.this, 500, "49", paytype);
                else
                    SNpayGolds(MyAccActivity.this, 500, "49", paytype);
                break;
            case "1000猫币":
                if (Constants.SET_PAY_TYPE_JBY)
                    payGolds(MyAccActivity.this, 1000, "100", paytype);
                else
                    SNpayGolds(MyAccActivity.this, 1000, "100", paytype);
                break;
            case "2000猫币":
                if (Constants.SET_PAY_TYPE_JBY)
                    payGolds(MyAccActivity.this, 2000, "200", paytype);
                else
                    SNpayGolds(MyAccActivity.this, 2000, "200", paytype);
                break;
            case "5000猫币":
                if (Constants.SET_PAY_TYPE_JBY)
                    payGolds(MyAccActivity.this, 5000, "500", paytype);
                else
                    SNpayGolds(MyAccActivity.this, 5000, "500", paytype);
                break;
            case "帅锅":
                if (Constants.SET_PAY_TYPE_JBY)
                    payLevels(MyAccActivity.this, 1, mMineDfh, paytype);
                else
                    SNpayLevels(MyAccActivity.this, 1, mMineDfh, paytype);
                break;
            case "土豪":
                if (Constants.SET_PAY_TYPE_JBY)
                    payLevels(MyAccActivity.this, 2, mMineGw, paytype);
                else
                    SNpayLevels(MyAccActivity.this, 2, mMineGw, paytype);
                break;
        }

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
    public GoldBean SNpayLevels(Context contexts, int level, final String amount, final String paytype) {
        OkGo.get(Api.GET_USE_PAY)
                .tag(contexts)
//                //.params("channel", "APP")
                //.params("channel", Constants.CHANNEL_APP_ID)
                .params("uid", SPUserUtils.sharedInstance().getUid())
                .params("level", level)
                .params("amount", amount)
                .params("paytype", paytype)
                .params("is_a", 1)

                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("付款-开通等级：", s);
                        if (paytype.equalsIgnoreCase("alipay")) {
                            PayBean bean = GsonUtil.parseJsonWithGson(s, PayBean.class);
                            if (null != bean && !StringUtil.isNullOrEmpty(bean.getUrl())) {
                                final String info = bean.getUrl();
                                Runnable payRunnable = new Runnable() {

                                    @Override
                                    public void run() {
                                        // 构造payTask 对象
                                        PayTask payTask = new PayTask(MyAccActivity.this);
                                        // 调用授权接口，获取授权结果
                                        Map<String, String> result = payTask.payV2(info, true);

                                        Message msg = new Message();
                                        msg.what = 1;
                                        msg.obj = result;
                                        mHandlers.sendMessage(msg);
                                    }
                                };

                                // 必须异步调用
                                Thread payThread = new Thread(payRunnable);
                                payThread.start();
                            }
                        } else if (paytype.equalsIgnoreCase("wxpay")) {
                            Log.e("微信wechat", s);
                            if (Constants.PAY_TYPE_WX) {
                                WXPayBean bean = GsonUtil.parseJsonWithGson(s, WXPayBean.class);
                                if (null != bean && null != bean.getUrl()) {
                                    PayReq req = new PayReq();
                                    req.appId = bean.getUrl().getAppid();
                                    req.partnerId = bean.getUrl().getPartnerId();
                                    req.prepayId = bean.getUrl().getPrepayId();
                                    req.packageValue = bean.getUrl().getPackageX();
                                    req.nonceStr = bean.getUrl().getNonceStr();
                                    req.timeStamp = bean.getUrl().getTimeStamp();
                                    req.sign = bean.getUrl().getSign();
                                    api.sendReq(req);
                                }
                            } else
                                DialogUtil.errorDialog(MyAccActivity.this, getString(R.string.wx_pay_callback_msg));
                        } else if (paytype.equalsIgnoreCase(SELECT_PAY_TYPE[0])) {
                            PayBean bean = GsonUtil.parseJsonWithGson(s, PayBean.class);
                            if (null != bean && !StringUtil.isNullOrEmpty(bean.getUrl()))
                                MyAppUtil.jumpToWeb(MyAccActivity.this, bean.getUrl());
                            else toast(R.string.pay_null_tips);
                            Log.e("", "onSuccess: wx");
                        } else {
                            PayBean bean = GsonUtil.parseJsonWithGson(s, PayBean.class);
                            if (null != bean && !StringUtil.isNullOrEmpty(bean.getUrl()))
                                MyAppUtil.jumpToWeb(MyAccActivity.this, bean.getUrl());
                            else toast(R.string.pay_null_tips);
                            Log.e("", "onSuccess: 其他支付");
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
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
    public GoldBean SNpayGolds(Context contexts, int goldnum, final String amount, final String paytype) {
        OkGo.get(Api.GET_USE_PAY)
                .tag(contexts)
                //.params("channel", Constants.CHANNEL_APP_ID)
//                //.params("channel", "APP")
                .params("uid", SPUserUtils.sharedInstance().getUid())
                .params("goldnum", goldnum)
                .params("amount", amount)
                .params("paytype", paytype)
                .params("is_a", 1)

                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("付款-充值金币：", s);
//                        mGoldbean = GsonUtil.parseJsonWithGson(s, GoldBean.class);
//                        if (null != mGoldbean) {
//                            mFwPAy(amount, mGoldbean.getRemark(), paytype.equalsIgnoreCase("wxpay") ? 1 : 2);
//                        }


                        if (paytype.equalsIgnoreCase("alipay")) {
                            PayBean bean = GsonUtil.parseJsonWithGson(s, PayBean.class);
                            if (null != bean && !StringUtil.isNullOrEmpty(bean.getUrl())) {
                                final String info = bean.getUrl();
                                Runnable payRunnable = new Runnable() {

                                    @Override
                                    public void run() {
                                        // 构造payTask 对象
                                        PayTask payTask = new PayTask(MyAccActivity.this);
                                        // 调用授权接口，获取授权结果
                                        Map<String, String> result = payTask.payV2(info, true);

                                        Message msg = new Message();
                                        msg.what = 1;
                                        msg.obj = result;
                                        mHandlers.sendMessage(msg);
                                    }
                                };

                                // 必须异步调用
                                Thread payThread = new Thread(payRunnable);
                                payThread.start();
                            }
                        } else if (paytype.equalsIgnoreCase("wxpay")) {
                            Log.e("微信wechat", s);
                            if (Constants.PAY_TYPE_WX) {
                                WXPayBean bean = GsonUtil.parseJsonWithGson(s, WXPayBean.class);
                                if (null != bean && null != bean.getUrl()) {
                                    PayReq req = new PayReq();
                                    req.appId = bean.getUrl().getAppid();
                                    req.partnerId = bean.getUrl().getPartnerId();
                                    req.prepayId = bean.getUrl().getPrepayId();
                                    req.packageValue = bean.getUrl().getPackageX();
                                    req.nonceStr = bean.getUrl().getNonceStr();
                                    req.timeStamp = bean.getUrl().getTimeStamp();
                                    req.sign = bean.getUrl().getSign();
                                    api.sendReq(req);
                                }
                            } else
                                DialogUtil.errorDialog(MyAccActivity.this, getString(R.string.wx_pay_callback_msg));
                        } else if (paytype.equalsIgnoreCase(SELECT_PAY_TYPE[0])) {
                            PayBean bean = GsonUtil.parseJsonWithGson(s, PayBean.class);
                            if (null != bean && !StringUtil.isNullOrEmpty(bean.getUrl()))
                                MyAppUtil.jumpToWeb(MyAccActivity.this, bean.getUrl());
                            else toast(R.string.pay_null_tips);
                            Log.e("", "onSuccess: wx");
                        } else {
                            PayBean bean = GsonUtil.parseJsonWithGson(s, PayBean.class);
                            if (null != bean && !StringUtil.isNullOrEmpty(bean.getUrl()))
                                MyAppUtil.jumpToWeb(MyAccActivity.this, bean.getUrl());
                            else toast(R.string.pay_null_tips);
                            Log.e("", "onSuccess: 其他支付");
                        }
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
     * 支付-开通等级
     *
     * @param level   开通等级（必须）
     * @param amount  付款金额（必须）
     * @param paytype 支付方式（支付宝:alipay 微信:wxpay）
     *                is_a    APP是不是安卓
     * @return
     */
    public GoldBean payLevels(Context contexts, int level, final String amount, final String paytype) {
        OkGo.get(Api.GET_USE_PAY)
                .tag(contexts)
//                //.params("channel", "APP")
                //.params("channel", Constants.CHANNEL_APP_ID)
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
                        if (null != mGoldbean) {
//                            mFwPAy(amount, mGoldbean.getRemark(), paytype.equalsIgnoreCase("wxpay") ? 1 : 2);
                            mFwPAy(amount, mGoldbean.getRemark(), paytype.equalsIgnoreCase(Constants.PAY_TYPE[0]) ? 1 : 2);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
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
    public GoldBean payGolds(Context contexts, int goldnum, final String amount, final String paytype) {
        OkGo.get(Api.GET_USE_PAY)
                .tag(contexts)
//                //.params("channel", "APP")
                //.params("channel", Constants.CHANNEL_APP_ID)
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
                        if (null != mGoldbean) {
//                            mFwPAy(amount, mGoldbean.getRemark(), paytype.equalsIgnoreCase("wxpay") ? 1 : 2);
                            mFwPAy(amount, mGoldbean.getRemark(), paytype.equalsIgnoreCase(Constants.PAY_TYPE[0]) ? 1 : 2);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.e("s", e.toString());
                        super.onError(call, response, e);
                    }
                });

        return mGoldbean;

    }


    private Handler mHandlers = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        toast("支付成功");
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        toast("支付失败");
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };


    private PayOrder orders;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ToastUtil.showToast(MyAccActivity.this, "网络错误", Toast.LENGTH_SHORT);
                    break;
                case 1:
                    try {
                        String channelType = (String) msg.obj;
//                        if ("[]".equals(channelType)) {
                        if (!channelType.startsWith("[")) {
                            ToastUtil.showToast(MyAccActivity.this, "未开通支付通道", Toast.LENGTH_SHORT);
                            return;
                        }
                        JSONArray array = new JSONArray(channelType);
                        // 在这里只取json数组的第一位，用户可自定义使用相对应的值
//                        int index = array.getInt(1);
                        int index = msg.arg1;

//                        Activity activity : 传入当前调起支付的Activity即可；
//               PayOrder payOrder : 支付信息的实体类；
//               int channelType : 若为非API版本，则直接传入0即可；  值根据自身情况选择传入，[1微信 , 2阿里支付, 3点卡支付, 4银联支付,5QQ钱包支付,0随机选择一种]
//               OnPayResultListener listener : 支付结果回调监听器。需要自己实现OnPayResultListener 接口。
                        // 主线程中调用 FWPay.pay 方法
                        FWPay.pay(MyAccActivity.this, orders, index, MyAccActivity.this);
                        refersh = true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtil.showToast(MyAccActivity.this, "支付通道配置错误", Toast.LENGTH_SHORT);
                    }
                    break;
            }
        }
    };

    /**
     * 聚宝云支付
     *
     * @param amount   金额
     * @param goodName 商品名称
     * @param payType  非API版本填0,[1微信 , 2阿里支付, 3点卡支付, 4银联支付,5QQ钱包支付,0随机选择一种]
     */
    private void mFwPAy(String amount, String goodName, final int payType) {

        orders = new PayOrder()
                //金额(必需)
                .setAmount(amount)
                //商品名称(必需)
                .setGoodsName(config.getNickname())
                .setRemark(goodName)
                //商户订单号(必需)
                .setPayId(mGoldbean.getOrdernum())
//                .setPayId(CommonUtils.setRand())
                //玩家Id(必需)
                .setPlayerId(config.getUid());


        // 子线程中调用 FWPay.getChannelType() 方法
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String channelType = FWPay.getChannelType(orders);
                    Message message = Message.obtain();
                    message.obj = channelType;
                    message.what = 1;
                    message.arg1 = payType;
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(0);
                }
            }
        }).start();
    }

    @Override
    public void onSuccess(Integer code, String message, String payId) {
        Log.e("聚宝云支付-onSuccess", "[code=" + code + "]"
                + "[message=" + message + "]" + "[payId=" + payId + "]");
//        ToastUtil.showToast(this, "[code=" + code + "]"
//                + "[message=" + message + "]" + "[payId=" + payId + "]", Toast.LENGTH_SHORT);
    }

    @Override
    public void onFailed(Integer code, String message, String payId) {
        Log.e("聚宝云支付-onFailed", "[code=" + code + "]"
                + "[message=" + message + "]" + "[payId=" + payId + "]");
//        ToastUtil.showToast(this, "[code=" + code + "]"
//                + "[message=" + message + "]" + "[payId=" + payId + "]", Toast.LENGTH_SHORT);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        OkGo.get(Api.GET_USER_INFORMATION)
                .tag(MyAccActivity.this)
                .params("uid", config.getUid())
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

                            ivLevel.setBackgroundResource(levetype(Integer.valueOf((userbran.getLevel()))));
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.e("s", e.toString());
                        super.onError(call, response, e);
                    }
                });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

}
