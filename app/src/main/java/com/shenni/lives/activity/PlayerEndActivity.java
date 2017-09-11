package com.shenni.lives.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fanwei.jubaosdk.common.core.OnPayResultListener;
import com.fanwei.jubaosdk.common.util.ToastUtil;
import com.fanwei.jubaosdk.shell.FWPay;
import com.fanwei.jubaosdk.shell.PayOrder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shenni.lives.R;
import com.shenni.lives.api.Api;
import com.shenni.lives.base.BaseActivity;
import com.shenni.lives.bean.AnchorBean;
import com.shenni.lives.bean.BackMsg;
import com.shenni.lives.bean.GoldBean;
import com.shenni.lives.utils.GsonUtil;
import com.shenni.lives.utils.PPWUtil;
import com.shenni.lives.utils.SPUserUtils;
import com.shenni.lives.utils.SPUtils;
import com.shenni.lives.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class PlayerEndActivity extends BaseActivity implements OnPayResultListener {

    @InjectView(R.id.tv_chat_end_time)
    TextView tvChatEndTime;
    @InjectView(R.id.tv_chat_end_renshu)
    TextView tvChatEndRenshu;
    @InjectView(R.id.tv_end_title)
    TextView tv_end_title;
    @InjectView(R.id.btn_chat_end_focus)
    Button btnChatEndFocus;
    @InjectView(R.id.ll_end_center)
    LinearLayout ll_end_center;


    @InjectView(R.id.tv_end_daojishi)
    Button tv_end_daojishi;

    //主播ID
    private String liveAid;
    //视频总时长
    private String duration;
    //观众数量
    private String audience;

    //level:观看权限[0:屌丝视频,1:帅哥视频,2:土豪视频,3:走私视频]
    private int level = -200;
    //直播是否下线 online 1 上线  2 下线
    private int online;

    private AnchorBean.ListBean listBean;

    private int chargeDaojiShi = 90;
    Handler handler = new Handler();
    String chat = "";
    private GoldBean mGoldbean;


    private Intent intentrefersh = new Intent();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_end);
        ButterKnife.inject(this);
        getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);

        if (null != getIntent()) {

            online = getIntent().getIntExtra("online", 1);
            liveAid = getIntent().getStringExtra("liveAid");
            chat = getIntent().getStringExtra("chat");
            level = getIntent().getIntExtra("liveLevel", -100);
            duration = (String) SPUtils.get(liveAid + "endduration", "");
            audience = (String) SPUtils.get(liveAid + "endaudience", "");

        }
        noActionBar();
        startHandler();

        if (StringUtil.isNullOrEmpty(chat)) {
            handler.removeCallbacks(runnable);
            if (online == 2) {
                tv_end_title.setText(getString(R.string.live_end_tips));
                btnChatEndFocus.setVisibility(View.INVISIBLE);
                tv_end_daojishi.setVisibility(View.GONE);
                return;
            }
            //播放结束
            if (level == 0 || level == 1 || level == 2) {
                mPbanchor();
                tv_end_title.setText(getString(R.string.live_end_tips));
//                tvChatEndTime.setText(duration);
//                if (StringUtil.isNullOrEmpty(audience)) getAnchorData();
//                else
//                    tvChatEndRenshu.setText(audience);
                btnChatEndFocus.setVisibility(View.INVISIBLE);
                tv_end_daojishi.setVisibility(View.GONE);

            } else if (level == 3) {
                mPbanchor();
                //私播已结束
                ll_end_center.setVisibility(View.INVISIBLE);
                tv_end_title.setText(getString(R.string.live_end_tips2));
                btnChatEndFocus.setVisibility(View.INVISIBLE);
                tv_end_daojishi.setVisibility(View.GONE);
            } else {
                //第二次点击

                tv_end_daojishi.setVisibility(View.GONE);
                ll_end_center.setVisibility(View.INVISIBLE);
                tv_end_title.setText(getString(R.string.live_end_tips1));
                btnChatEndFocus.setText(getString(R.string.live_op_op));
            }
        } else {
            //提示充值
            ll_end_center.setVisibility(View.INVISIBLE);
            tv_end_title.setText(getString(R.string.live_end_tips1));
            btnChatEndFocus.setText(getString(R.string.live_op_op));
        }

    }


    @OnClick({R.id.btn_chat_end_focus, R.id.btn_chat_end_close, R.id.tv_end_daojishi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_chat_end_focus:
                if (level == 3) {
//                    ppwPayShow();
                    startActivity(new Intent(PlayerEndActivity.this, MyAccActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(PlayerEndActivity.this, MyAccActivity.class));
                    finish();
                }
                break;
            case R.id.btn_chat_end_close:
                finish();
                break;
            case R.id.tv_end_daojishi:
                finish();
                break;
        }
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (config.getLevel().equalsIgnoreCase("0")) {
                try {
                    tv_end_daojishi.setText("退出倒计时" + chargeDaojiShi + "秒");
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


    private void startHandler() {
        handler.post(runnable);
    }


    /**
     * 关注
     */

    public void getGuanZhu() {
        OkGo.get(Api.GET_USE_GUANZHU)
                .tag(this)
                .params("uid", uid)
                .params("aid", liveAid)
                .params("num", num)
                .params("is_qx", 0)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("关注", s);

                        BackMsg backMsg = GsonUtil.parseJsonWithGson(s, BackMsg.class);
                        if (null != backMsg && 1 == backMsg.getStatus()) {
                            if (1 == backMsg.getStatus()) {
                                toast(backMsg.getInfo());
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

    /**
     * 获取主播详情
     */
    public void getAnchorData() {
        if (!StringUtil.isNullOrEmpty(liveAid))
            OkGo.get(Api.GET_ANCHOR_MESSAGE_URL)
                    .tag(this)
                    .params("uid", uid)
                    .params("aid", liveAid)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Log.e("主播详情：", s);
                            AnchorBean bean = GsonUtil.parseJsonWithGson(s, AnchorBean.class);
                            if (null != bean) {
                                if (bean.getStatus().equals("0")) {
                                    listBean = bean.getList();

                                    duration = (String) SPUtils.get(liveAid + "endduration", "");
                                    tvChatEndTime.setText(duration);
                                    tvChatEndRenshu.setText(String.valueOf(listBean.getAudience()));
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

    private View mPopWindow;
    String paytype = "";

    /**
     * 充值选择页面PPW
     */
    private void ppwPayShow() {

        mPopWindow = PPWUtil.showPop(this, R.id.ll_end_top, R.layout.activity_photo_graph);
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
                    paytype = "wxpay";
                    payGold(paytype);
                    PPWUtil.dismissPop();
                }
            });
            mAli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    paytype = "alipay";
                    payGold(paytype);
                    PPWUtil.dismissPop();
                }
            });


        }

    }


    private void payGold(final String paytype) {
        OkGo.get(Api.GET_USE_PAY)
                .tag(PlayerEndActivity.this)
                //.params("channel", "APP")
                .params("uid", SPUserUtils.sharedInstance().getUid())
                .params("si", 1)
                .params("aid", liveAid)
                .params("types", 3)
                .params("amount", ((String) SPUtils.get("User_ttk", "")))
                .params("paytype", paytype)
                .params("is_a", 1)


                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("付款-偷偷看：", s);
                        mGoldbean = GsonUtil.parseJsonWithGson(s, GoldBean.class);
                        if (null != mGoldbean) {
                            mFwPAy(((String) SPUtils.get("User_ttk", "")), mGoldbean.getRemark(), paytype.equalsIgnoreCase("wxpay") ? 1 : 2);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.e("s", e.toString());
                        super.onError(call, response, e);
                    }
                });

    }


    private PayOrder orders;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ToastUtil.showToast(PlayerEndActivity.this, "网络错误", Toast.LENGTH_SHORT);
                    break;
                case 1:
                    try {
                        String channelType = (String) msg.obj;
                        if ("[]".equals(channelType)) {
                            ToastUtil.showToast(PlayerEndActivity.this, "未开通支付通道", Toast.LENGTH_SHORT);
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
                        FWPay.pay(PlayerEndActivity.this, orders, index, PlayerEndActivity.this);
//                        if (levels != 0)
//                            if (StringUtil.isNullOrEmpty(chat))
//                                setLevel(String.valueOf(levels));
//                            else setLevel(String.valueOf(levels), true);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtil.showToast(PlayerEndActivity.this, "支付通道配置错误", Toast.LENGTH_SHORT);
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
//        GoldPay.paySuccessAfter(PlayerEndActivity.this, config.getUid());
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
     * 屏蔽
     */

    public void mPbanchor() {
        OkGo.get(Api.GET_USE_PBANCHOR)
                .tag(PlayerEndActivity.this)
                .params("aid", liveAid)
                .params("uid", SPUserUtils.sharedInstance().getUid())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("屏蔽", s);
                        setResult(RESULT_OK, new Intent());
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.e("onError", e.toString());
                        super.onError(call, response, e);
                    }
                });

    }

}
