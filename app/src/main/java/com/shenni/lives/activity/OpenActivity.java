package com.shenni.lives.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.shenni.lives.bean.CallBackBean;
import com.shenni.lives.bean.CoreBean;
import com.shenni.lives.bean.FollowBean;
import com.shenni.lives.bean.GoldBean;
import com.shenni.lives.bean.PayBean;
import com.shenni.lives.bean.UsersBean;
import com.shenni.lives.bean.WXPayBean;
import com.shenni.lives.jjdxmplayer.PlayerLiveActivity;
import com.shenni.lives.utils.Constants;
import com.shenni.lives.utils.DialogUtil;
import com.shenni.lives.utils.GlideImageLoader;
import com.shenni.lives.utils.GsonUtil;
import com.shenni.lives.utils.MyAppUtil;
import com.shenni.lives.utils.PPWUtil;
import com.shenni.lives.utils.PayResult;
import com.shenni.lives.utils.SPUserUtils;
import com.shenni.lives.utils.SPUtils;
import com.shenni.lives.utils.StringUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.shenni.lives.utils.Constants.SELECT_PAY_TYPE;


public class OpenActivity extends BaseActivity implements OnPayResultListener {

    @InjectView(R.id.banner_op_heand)
    Banner banner;
    public String mAid;
    String url = "";
    //是否 在直播页面进来 字符串chat
    String chat = "";
    Intent mIntent = new Intent();
    @InjectView(R.id.iv_acc1)
    ImageView ivAcc1;
    @InjectView(R.id.tv_marquess)
    TextView tvMarquess;
    @InjectView(R.id.tv_acc_price)
    TextView tvAccPrice;
    @InjectView(R.id.iv_open_level1)
    ImageView ivOpenLevel1;
    @InjectView(R.id.iv_acc2)
    ImageView ivAcc2;
    @InjectView(R.id.tv_king)
    TextView tvKing;
    @InjectView(R.id.tv_king_price)
    TextView tvKingPrice;
    @InjectView(R.id.textView3)
    TextView textView3;
    @InjectView(R.id.iv_open_level2)
    ImageView ivOpenLevel2;
    @InjectView(R.id.re_op_level12)
    LinearLayout reOpLevel12;
    @InjectView(R.id.re_op_levelr3)
    RelativeLayout reoplevelr3;
    @InjectView(R.id.tv_open_dfh)
    TextView tvOpenDfh;
    @InjectView(R.id.tv_open_gw)
    TextView tvOpenGw;
    @InjectView(R.id.tv_open_daojishi)
    TextView tvOpenDaojishi;
    @InjectView(R.id.iv_op_sibo)
    ImageView ivOpSibo;
    @InjectView(R.id.tv_op_money)
    TextView tvOpMoney;
    @InjectView(R.id.re_op_level3)
    LinearLayout reOpLevel3;
    @InjectView(R.id.iv_op_close)
    ImageView ivOpClose;
    @InjectView(R.id.op_close2)
    View opClose2;
    @InjectView(R.id.rl_op_hj)
    RelativeLayout rlOpHj;
    @InjectView(R.id.rl_op_th)
    RelativeLayout rlOpTh;
    @InjectView(R.id.rl_op_sibo)
    RelativeLayout rlOpSibo;

    //轮播数据来源  1:热门 2:关注 3:同城
    private String sid = "4";
    private String S_money = "";
    private int level;
    private View mPopWindow;
    String paytype = "";
    CoreBean.ConBean conBean;
    private List<FollowBean.ListBean> list;


    Handler handler = new Handler();
    private int chargeDaojiShi = 120;

    private int mIntPay = 0;
    private int sibo = 0;
    String bigpic = "";


    private GoldBean mGoldbean;
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OpenActivity.this.setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_open);
        ButterKnife.inject(this);
        getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        getCore();
        if (null != getIntent()) {
            try {
                mAid = getIntent().getStringExtra("liveAid");
                sibo = getIntent().getIntExtra("sibo", 0);
                if (sibo == 100) {
                    rlOpHj.setVisibility(View.GONE);
                    rlOpTh.setVisibility(View.GONE);
                    rlOpSibo.setVisibility(View.VISIBLE);
                } else {
                    url = getIntent().getStringExtra("source");
                    bigpic = getIntent().getStringExtra("bigpic");
                    S_money = getIntent().getStringExtra("S_money");
                    level = getIntent().getIntExtra("level", 0);
                    chat = getIntent().getStringExtra("chat");
                    if (StringUtil.isNullOrEmpty(chat))
                        if (level == 3) {
                            selectPays(OpenActivity.this, config.getUid(), mAid, 1);
                        } else paySuccessAfters(OpenActivity.this, config.getUid());
                }
            } catch (Exception e) {
                Log.e("PlayerLiveActivity have Exception:", e.toString());
            }
        }
        InitView();
        getBanner();
        startHandler();
    }

    /**
     * 获取设置金额
     */
    public void getCore() {
        OkGo.get(Api.GET_USE_CORE)
                .tag(OpenActivity.this)
                //.params("channel", "APP")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("获取设置金额：", s);
                        CoreBean coreBean = GsonUtil.parseJsonWithGson(s, CoreBean.class);
                        if (null != coreBean) {
                            if (coreBean.getStatus() == 1) {
                                conBean = coreBean.getCon();
                                if (null != conBean) {
                                    tvOpenDfh.setText(String.valueOf(conBean.getDfh()) + getString(R.string.live_pay_month));
                                    tvOpenGw.setText(String.valueOf(conBean.getGw()) + getString(R.string.live_pay_month));
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

    @Override
    protected void onResume() {
        super.onResume();
        if (mIntPay > 0)
            startHandler();
        mIntPay++;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (config.getLevel().equalsIgnoreCase("0")) {
                try {
                    tvOpenDaojishi.setVisibility(View.VISIBLE);
                    tvOpenDaojishi.setText(chargeDaojiShi + "秒后退出房间");
                    chargeDaojiShi--;
                    if (chargeDaojiShi == 0) {
                        finish();
                    }
                } catch (Exception e) {
                }
            }
            handler.postDelayed(runnable, 1000);
        }
    };


    public void InitView() {
        if (level == 3) {
            reOpLevel12.setVisibility(View.GONE);
            reoplevelr3.setVisibility(View.VISIBLE);
            tvOpMoney.setText(S_money + "元/每场");
        } else {

            reOpLevel12.setVisibility(View.VISIBLE);
            reoplevelr3.setVisibility(View.GONE);
        }

        noActionBar();
        List<Integer> images = new ArrayList<>();
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
//        banner.setBannerAnimation(Transformer.CubeOut);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles("当banner样式有显示title时");
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

        //点击事件
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
//                toast("你点击了："+position);
//                startActivity(new Intent(getActivity(), TipsActivity.class));

                String url = list.get(position).getLink();
                if (StringUtil.isNullOrEmpty(url)) {
//                    toast("暂无活动");
                } else {
                    Intent intent = new Intent(OpenActivity.this, WebActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
            }
        });

    }

    /**
     * 获取广告轮播图
     */
    public void getBanner() {
        OkGo.get(Api.GET_BANNER_URL)
                .tag(OpenActivity.this)
                .params("sid", sid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("广告轮播图", s);
                        FollowBean bean = GsonUtil.parseJsonWithGson(s, FollowBean.class);
                        if (null != bean && 1 == bean.getStatus()) {
                            //设置图片来源
                            list = bean.getList();
                            List<String> images = new ArrayList<String>();
                            for (int i = 0; i < list.size(); i++) {
                                images.add(list.get(i).getImg());
                            }
                            try {
                                banner.setImages(images);

                                if (images.size() == 0)
                                    banner.setVisibility(View.GONE);
                                else
                                    banner.start();
                            } catch (Exception e) {
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.e("onError", e.toString());
                        super.onError(call, response, e);
                    }
                });

    }

    @OnClick({R.id.op_close1, R.id.op_close2, R.id.iv_open_level1, R.id.iv_open_level2, R.id.iv_open_level3, R.id.re_op_level3, R.id.iv_op_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.op_close1:
            case R.id.op_close2:
                finish();
                break;
            case R.id.iv_open_level1:
                ppwPayShow("帅锅");
                break;
            case R.id.iv_open_level2:
                ppwPayShow("土豪");
                break;
            case R.id.iv_open_level3:
                chatsibo(OpenActivity.this, SPUserUtils.sharedInstance().getUid(), mAid, 2);
                break;
            case R.id.re_op_level3:
                ppwPayShow("走私");
                break;
            case R.id.iv_op_close:
                finish();
                break;
        }
    }

    /**
     * 充值选择页面PPW
     */
    private void ppwPayShow(final String type) {
        handler.removeCallbacks(runnable);
        mPopWindow = PPWUtil.showPop(this, R.id.banner_op_heand, R.layout.activity_photo_graph);
        if (null != mPopWindow) {
            Button mWx = (Button) mPopWindow.findViewById(R.id.btn_selectphoto);
            Button mAli = (Button) mPopWindow.findViewById(R.id.btn_creame);
            Button mclose = (Button) mPopWindow.findViewById(R.id.btn_cancle);

            mWx.setText("微信支付");
            mAli.setText("支付宝支付");
            mclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startHandler();
                    PPWUtil.dismissPop();
                }
            });
            mWx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    paytype = "wxpay";
                    paytype = Constants.PAY_TYPE[0];
//                    startHandler();
                    payLevel(type, paytype);
                    //充值成功后操作
//                    if (StringUtil.isNullOrEmpty(chat))
//                        setLevel("1");
//                    else setLevel("1", true);
                    PPWUtil.dismissPop();
                }
            });
            mAli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    paytype = "alipay";
//                    startHandler();
                    paytype = Constants.PAY_TYPE[1];
                    payLevel(type, paytype);

                    PPWUtil.dismissPop();
                }
            });


        }

    }


    private void startHandler() {
        if (!StringUtil.isNullOrEmpty(chat))
            handler.post(runnable);
    }

    private int levels = 0;

    private void payLevel(String type, String paytype) {
//        int level, String amount, String paytype
//        int si, String aid, int types, String amount, String paytype
        switch (type) {
            case "帅锅":
                if (Constants.SET_PAY_TYPE_JBY)
                    payLevels(OpenActivity.this, levels = 1, String.valueOf(conBean.getDfh()), paytype);
                else
                    SNpayLevels(OpenActivity.this, levels = 1, String.valueOf(conBean.getDfh()), paytype);
                break;
            case "土豪":
//                payLevels(OpenActivity.this, levels = 2, "1", paytype);
                if (Constants.SET_PAY_TYPE_JBY)
                    payLevels(OpenActivity.this, levels = 2, String.valueOf(conBean.getGw()), paytype);
                else
                    SNpayLevels(OpenActivity.this, levels = 2, String.valueOf(conBean.getGw()), paytype);
                break;
            case "走私":
                if (Constants.SET_PAY_TYPE_JBY)
                    paySis(OpenActivity.this, 1, mAid, 1, S_money, paytype);
                else
                    SNpaySis(OpenActivity.this, 1, mAid, 1, S_money, paytype);
                break;
            case "私播":
                if (Constants.SET_PAY_TYPE_JBY)
                    paySis(OpenActivity.this, 1, mAid, 2, (String) SPUtils.get("User_zs", "0"), paytype);
                else
                    SNpaySis(OpenActivity.this, 1, mAid, 2, (String) SPUtils.get("User_zs", "0"), paytype);
                break;
            default:
                break;
        }

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

    };


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
    public GoldBean SNpaySis(Context contexts, int si, String aid, int types, final String amount, final String paytype) {
        OkGo.get(Api.GET_USE_PAY)
                .tag(contexts)
//                //.params("channel", "APP")
                //.params("channel", Constants.CHANNEL_APP_ID)
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
//                        Log.e("付款-走私=付费观看一次，私播=打赏：", s);
//                        mGoldbean = GsonUtil.parseJsonWithGson(s, GoldBean.class);
//                        mFwPAy(amount, mGoldbean.getRemark(), paytype.equalsIgnoreCase("wxpay") ? 1 : 2);


                        Log.e("付款-走私=付费观看一次，私播=打赏：", s);
                        if (paytype.equalsIgnoreCase("alipay")) {
                            PayBean bean = GsonUtil.parseJsonWithGson(s, PayBean.class);
                            if (null != bean && !StringUtil.isNullOrEmpty(bean.getUrl())) {
                                final String info = bean.getUrl();
                                Runnable payRunnable = new Runnable() {

                                    @Override
                                    public void run() {
                                        // 构造payTask 对象
                                        PayTask payTask = new PayTask(OpenActivity.this);
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
                                DialogUtil.errorDialog(OpenActivity.this, getString(R.string.wx_pay_callback_msg));
                        } else if (paytype.equalsIgnoreCase(SELECT_PAY_TYPE[0])) {
                            PayBean bean = GsonUtil.parseJsonWithGson(s, PayBean.class);
                            if (null != bean && !StringUtil.isNullOrEmpty(bean.getUrl()))
                                MyAppUtil.jumpToWeb(OpenActivity.this, bean.getUrl());
                            else toast(R.string.pay_null_tips);
                            Log.e("", "onSuccess: wx");
                        } else {
                            PayBean bean = GsonUtil.parseJsonWithGson(s, PayBean.class);
                            if (null != bean && !StringUtil.isNullOrEmpty(bean.getUrl()))
                                MyAppUtil.jumpToWeb(OpenActivity.this, bean.getUrl());
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
//                        Log.e("付款-开通等级：", s);
//                        mGoldbean = GsonUtil.parseJsonWithGson(s, GoldBean.class);
//                        mFwPAy(amount, mGoldbean.getRemark(), paytype.equalsIgnoreCase("wxpay") ? 1 : 2);


                        Log.e("付款-开通等级：", s);
                        if (paytype.equalsIgnoreCase("alipay")) {
                            PayBean bean = GsonUtil.parseJsonWithGson(s, PayBean.class);
                            if (null != bean && !StringUtil.isNullOrEmpty(bean.getUrl())) {
                                final String info = bean.getUrl();
                                Runnable payRunnable = new Runnable() {

                                    @Override
                                    public void run() {
                                        // 构造payTask 对象
                                        PayTask payTask = new PayTask(OpenActivity.this);
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
                                DialogUtil.errorDialog(OpenActivity.this, getString(R.string.wx_pay_callback_msg));
                        } else if (paytype.equalsIgnoreCase(SELECT_PAY_TYPE[0])) {
                            PayBean bean = GsonUtil.parseJsonWithGson(s, PayBean.class);
                            if (null != bean && !StringUtil.isNullOrEmpty(bean.getUrl()))
                                MyAppUtil.jumpToWeb(OpenActivity.this, bean.getUrl());
                            else toast(R.string.pay_null_tips);
                            Log.e("", "onSuccess: wx");
                        } else {
                            PayBean bean = GsonUtil.parseJsonWithGson(s, PayBean.class);
                            if (null != bean && !StringUtil.isNullOrEmpty(bean.getUrl()))
                                MyAppUtil.jumpToWeb(OpenActivity.this, bean.getUrl());
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
    public GoldBean paySis(Context contexts, int si, String aid, int types, final String amount, final String paytype) {
        OkGo.get(Api.GET_USE_PAY)
                .tag(contexts)
//                //.params("channel", "APP")
                //.params("channel", Constants.CHANNEL_APP_ID)
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
//                        mFwPAy(amount, mGoldbean.getRemark(), paytype.equalsIgnoreCase("wxpay") ? 1 : 2);
                        if (null != mGoldbean)
                            mFwPAy(amount, mGoldbean.getRemark(), paytype.equalsIgnoreCase(Constants.PAY_TYPE[0]) ? 1 : 2);
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
//                        mFwPAy(amount, mGoldbean.getRemark(), paytype.equalsIgnoreCase("wxpay") ? 1 : 2);
                        if (null != mGoldbean)
                            mFwPAy(amount, mGoldbean.getRemark(), paytype.equalsIgnoreCase(Constants.PAY_TYPE[0]) ? 1 : 2);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                });

        return mGoldbean;

    }


    private PayOrder orders;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ToastUtil.showToast(OpenActivity.this, "网络错误", Toast.LENGTH_SHORT);
                    break;
                case 1:
                    try {
                        String channelType = (String) msg.obj;
//                        if ("[]".equals(channelType)) {
                        if (!channelType.startsWith("[")) {
                            ToastUtil.showToast(OpenActivity.this, channelType, Toast.LENGTH_SHORT);
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
                        FWPay.pay(OpenActivity.this, orders, index, OpenActivity.this);
//                        if (levels != 0)
//                            if (StringUtil.isNullOrEmpty(chat))
//                                setLevel(String.valueOf(levels));
//                            else setLevel(String.valueOf(levels), true);
                        if (StringUtil.isNullOrEmpty(chat))
                            finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtil.showToast(OpenActivity.this, "支付通道配置错误", Toast.LENGTH_SHORT);
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
        //充值成功后操作
//        if (StringUtil.isNullOrEmpty(chat))
//            setLevel(String.valueOf(level));
//        else setLevel(String.valueOf(level), true);
//        GoldPay.paySuccessAfter(OpenActivity.this, config.getUid());
    }

    @Override
    public void onFailed(Integer code, String message, String payId) {
        Log.e("聚宝云支付-onFailed", "[code=" + code + "]"
                + "[message=" + message + "]" + "[payId=" + payId + "]");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }


    /**
     * 走私/偷窥查询记录
     *
     * @param context
     * @param uid     用户ID
     * @param aid     主播ID
     * @param types   1：立即偷窥，2：约她私播
     */
    public void selectPays(Context context, String uid, String aid, int types) {
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
                                CallBackBean mCallBackBean = GsonUtil.parseJsonWithGson(s, CallBackBean.class);
                                if (null != mCallBackBean) {
                                    if (mCallBackBean.getStatus() == 1) {
                                        Intent mIntent = new Intent(OpenActivity.this, PlayerLiveActivity.class);
                                        mIntent.putExtra("liveAid", mAid);
                                        mIntent.putExtra("source", url);
                                        mIntent.putExtra("bigpic", bigpic);
                                        startActivity(mIntent);
                                        finish();
                                    }
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
    }

    static UsersBean.UserBean userbran;

    /**
     * 付款成功后修改本用户地数据
     */
    public UsersBean.UserBean paySuccessAfters(Context context, String uid) {
        if (context != null && !uid.isEmpty())
            try {
                OkGo.get(Api.GET_USER_INFORMATION)
                        .tag(context)
                        .params("uid", uid)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                Log.e("获取用户信息：", s);

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
                                    if (StringUtil.isNullOrEmpty(chat)) {
                                        if (Integer.valueOf(userbran.getLevel()) >= level) {
                                            Intent mIntent = new Intent(OpenActivity.this, PlayerLiveActivity.class);
                                            mIntent.putExtra("liveAid", mAid);
                                            mIntent.putExtra("source", url);
                                            mIntent.putExtra("bigpic", bigpic);
                                            startActivity(mIntent);
                                            finish();
                                        }
                                    } else if (Integer.valueOf(userbran.getLevel()) > 0) {
                                        Intent intent = new Intent();
                                        intent.putExtra("chargeTips", 1);
                                        intent.putExtra("isTrue", 1);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
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

    @Override
    protected void onRestart() {
        super.onRestart();
        paySuccessAfters(OpenActivity.this, config.getUid());
    }

    /**
     * @param level 成功后 传递充值等级
     */
    private void setLevel(String level) {
        Intent mIntent = new Intent(OpenActivity.this, PlayerLiveActivity.class);
        mIntent.putExtra("liveAid", mAid);
        mIntent.putExtra("source", url);
        mIntent.putExtra("bigpic", bigpic);
        config.setLevel(level);
        config.savePreferences();
        startActivity(mIntent);
        finish();
    }

    /**
     * @param level 成功后 返回充值等级
     */
    private void setLevel(String level, boolean isboolean) {
        //充值成功后  不再提示充值
        mIntent.putExtra("chargeTips", 1);
        mIntent.putExtra("isTrue", isboolean ? 1 : 0);
        config.setLevel(level);
        config.savePreferences();
        setResult(RESULT_OK, mIntent);
        finish();
    }


    /**
     * 走私/偷窥查询记录
     *
     * @param context
     * @param uid     用户ID
     * @param aid     主播ID
     * @param types   1：立即偷窥，2：约她私播
     */
    public void chatsibo(Context context, String uid, String aid, int types) {
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
                                CallBackBean mCallBackBean = GsonUtil.parseJsonWithGson(s, CallBackBean.class);
                                if (null != mCallBackBean) {
                                    if (mCallBackBean.getStatus() == 1) {
                                        toast("你已经和该直播私播过了，还要在继续么？");
                                        ppwPayShow("私播");
                                    } else {
                                        ppwPayShow("私播");

                                    }
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
    }


}











