package com.shenni.lives.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.dou361.ijkplayer.widget.PlayerView;
import com.fanwei.jubaosdk.common.core.OnPayResultListener;
import com.fanwei.jubaosdk.common.util.ToastUtil;
import com.fanwei.jubaosdk.shell.FWPay;
import com.fanwei.jubaosdk.shell.PayOrder;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.shenni.lives.R;
import com.shenni.lives.activity.GiftBackBean;
import com.shenni.lives.activity.WebActivity;
import com.shenni.lives.adapter.AudienceAdapter;
import com.shenni.lives.adapter.MessageAdapter;
import com.shenni.lives.adapter.MessageAdapter2;
import com.shenni.lives.api.Api;
import com.shenni.lives.base.BaseFragment;
import com.shenni.lives.bean.AnchorBean;
import com.shenni.lives.bean.AnchorListBean;
import com.shenni.lives.bean.BackMsg;
import com.shenni.lives.bean.CallBackBean;
import com.shenni.lives.bean.ChatBean;
import com.shenni.lives.bean.CoreBean;
import com.shenni.lives.bean.FollowBean;
import com.shenni.lives.bean.GiftBean;
import com.shenni.lives.bean.GoldBean;
import com.shenni.lives.bean.PayBean;
import com.shenni.lives.bean.UsersBean;
import com.shenni.lives.bean.WXPayBean;
import com.shenni.lives.utils.Constants;
import com.shenni.lives.utils.DialogUtil;
import com.shenni.lives.utils.DisplayUtil;
import com.shenni.lives.utils.GlideImageLoader;
import com.shenni.lives.utils.GoldPay;
import com.shenni.lives.utils.GsonUtil;
import com.shenni.lives.utils.MyAppUtil;
import com.shenni.lives.utils.PPWUtil;
import com.shenni.lives.utils.PayResult;
import com.shenni.lives.utils.SPCiKuUtils;
import com.shenni.lives.utils.SPUserUtils;
import com.shenni.lives.utils.SPUtils;
import com.shenni.lives.utils.StringUtil;
import com.shenni.lives.widget.HorizontalListView;
import com.shenni.lives.widget.MagicTextView;
import com.shenni.lives.widget.PeriscopeLayout;
import com.squareup.picasso.Picasso;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.dync.giftlibrary.util.GiftPanelControl;
import org.dync.giftlibrary.widget.GiftControl;
import org.dync.giftlibrary.widget.GiftFrameLayout;
import org.dync.giftlibrary.widget.GiftModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static com.shenni.lives.R.id.tv_chat_audience;
import static com.shenni.lives.R.id.tv_chat_guanzhu;
import static com.shenni.lives.utils.Constants.CHAT_CHARGE_TIPS;
import static com.shenni.lives.utils.Constants.SELECT_PAY_TYPE;
import static com.shenni.lives.utils.SPUserUtils.sharedInstance;
import static me.shenfan.updateapp.UpdateService.TAG;


/**
 * 该Fragment是用来处理 聊天 礼物 界面逻辑
 */
@SuppressWarnings("ResourceType")
public class ChatFragment extends BaseFragment implements View.OnClickListener {

    private List<FollowBean.ListBean> list;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    break;
            }
        }
    };

    /**
     * 标示判断
     */
    private boolean isOpen;
    private long liveTime;

    /**
     * 界面相关
     */
    private LinearLayout llpicimage;
    private RelativeLayout rlsentimenttime;
    private HorizontalListView hlvaudience;
    private TextView tvtime;
    private LinearLayout llgiftcontent;
    private ListView lvmessage;
    private ListView mListView;
    private ImageView tvSendone;
    private TextView tvSendtwo;
    private TextView tvSendthree;
    private TextView tvSendfor;
    private EditText etInput;
    private TextView tvChat;
    private TextView sendInput;
    private LinearLayout llInputParent;

    private String msibom;
    private LinearLayout giftLayout;
    private LinearLayout ll_portrait;
    private LinearLayout ll_landscape;
    private TextView btnGift;
    private ViewPager mViewpager;
    private LinearLayout mDotsLayout;
    private RecyclerView mRecyclerView;


    private int mkeyshow = 1;

    @InjectView(R.id.iv_chat_head)
    CircleImageView ivChatHead;
    @InjectView(R.id.chat_fragment)
    FrameLayout mFrameLayout;
    @InjectView(R.id.tv_chat_title)
    TextView tvnickname;
    @InjectView(R.id.toolbox_tv_num)
    TextView toolbox_tv_num;
    @InjectView(tv_chat_audience)
    TextView tvaudience;
    @InjectView(R.id.tv_chat_guanzhu)
    TextView tvchatguanzhu;
    @InjectView(R.id.tv_chat_coins)
    TextView tvcoins;
    @InjectView(R.id.tv_chat_daojishi)
    TextView tv_chat_daojishi;
    @InjectView(R.id.ll_chongzhi)
    LinearLayout llchongzhi;
    @InjectView(R.id.bottom)
    LinearLayout banner;
    @InjectView(R.id.ll_chat_daoqi)
    LinearLayout ll_chat_daoqi;
    @InjectView(R.id.linearLayout)
    LinearLayout linearLayout;
    @InjectView(R.id.btn_chat_dan)
    CheckBox btn_chat_dan;
    private TextView tvID;


    // 心型气泡
    private PeriscopeLayout periscopeLayout;

    private boolean temp = true;
    /**
     * 动画相关
     */
    private NumAnim giftNumAnim;
    private TranslateAnimation inAnim;
    private TranslateAnimation outAnim;
    private AnimatorSet animatorSetHide = new AnimatorSet();
    private AnimatorSet animatorSetShow = new AnimatorSet();
    /**
     * 数据相关
     */
    private List<View> giftViewCollection = new ArrayList<View>();
    private List<ChatBean> messageData = new LinkedList<>();
    private List<ChatBean> messageData2 = new LinkedList<>();
    private MessageAdapter messageAdapter;
    int mRobot;
    int second;
    private String mAid;
    private int videotype = 1;
    private PlayerView player;
    /**
     * 主播信息
     */
    private AnchorBean.ListBean listBean;

    /**
     * 金币
     */
    private int mCoins = 0;
    //人数
    private int mpeople = 0;
    private View mPopWindow;

    /**
     * 关注
     */
    int is_qx = 0;

    /**
     * 词库
     */
    private AnchorListBean.ListBean.CikuBean cikuBean;
    /**
     * 加载更多词库时的默认 2
     */
    private int page = 1;
    private int mIntCiKu = 0;


    /**
     * 获取来自网络礼物图片
     */
    private List<GiftBean.ListBean> getGiftBean;
    private String mGifturl = "";
    private String mGifid = "";
    private String mGiftName = "";
    private String mGiftPrice = "";
    private GiftControl giftControl;
    private GiftFrameLayout giftFrameLayout1;
    private GiftFrameLayout giftFrameLayout2;
    private PopupWindow popComment;

    private String mZanTing = "";


    private IWXAPI api;

    /**
     * 收费提示
     */
    private int chargeTips = 0;
    private int chargeDaojiShi = -1;


    public ChatFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public ChatFragment(PlayerView player) {
        super();
        this.player = player;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mAid = bundle.getString("liveAid");
            videotype = bundle.getInt("videotype", 1);
        }
        mIntCiKu = (int) SPUtils.get(mAid + "mIntCiKu_Index", -100);
        mIntCiKu = mIntCiKu > 0 ? mIntCiKu : 0;

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        llpicimage = (LinearLayout) view.findViewById(R.id.llpicimage);
        rlsentimenttime = (RelativeLayout) view.findViewById(R.id.rlsentimenttime);
        hlvaudience = (HorizontalListView) view.findViewById(R.id.hlvaudience);
        tvtime = (TextView) view.findViewById(R.id.tvtime);
        tvID = (TextView) view.findViewById(R.id.tvdate);
        llgiftcontent = (LinearLayout) view.findViewById(R.id.llgiftcontent);
        lvmessage = (ListView) view.findViewById(R.id.lvmessage);
        tvChat = (TextView) view.findViewById(R.id.tvChat);
        tvSendone = (ImageView) view.findViewById(R.id.tvSendone);
        tvSendtwo = (TextView) view.findViewById(R.id.tvSendtwo);
        tvSendthree = (TextView) view.findViewById(R.id.tvSendthree);
        tvSendfor = (TextView) view.findViewById(R.id.tvSendfor);
//        llInputParent = (LinearLayout) view.findViewById(R.id.llinputparent);
        periscopeLayout = (PeriscopeLayout) view.findViewById(R.id.chat_periscope);
        tvChat.setOnClickListener(this);
        tvSendone.setOnClickListener(this);
        tvSendtwo.setOnClickListener(this);
        tvSendthree.setOnClickListener(this);
        tvSendfor.setOnClickListener(this);
        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.gift_in);
        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.gift_out);
        giftNumAnim = new NumAnim();
        clearTiming();
        getAnchorData();
        getCiku(mAid);
        ButterKnife.inject(this, view);
        toolbox_tv_num.setText(SPUserUtils.sharedInstance().getWallet());
        initGiftLayout(view);
        mGiftlist();
        getCore();

        btn_chat_dan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    llpicimage.setVisibility(View.INVISIBLE);
                    rlsentimenttime.setVisibility(View.INVISIBLE);
                    linearLayout.setVisibility(View.INVISIBLE);
                    lvmessage.setVisibility(View.INVISIBLE);
                } else {
                    llpicimage.setVisibility(View.VISIBLE);
                    rlsentimenttime.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                    lvmessage.setVisibility(View.VISIBLE);
                }
            }
        });
        api = WXAPIFactory.createWXAPI(getActivity(), Constants.APP_ID);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (llInputParent.getVisibility() == View.VISIBLE) {
//                    tvChat.setVisibility(View.VISIBLE);
//                    llInputParent.setVisibility(View.GONE);
//                    hideKeyboard();
//                    return;
//                }

                if (banner.getVisibility() == View.VISIBLE) {
                    banner.setVisibility(View.GONE);
                    return;
                }

            }
        });
//        softKeyboardListnenr();
//        for (int x = 0; x < 20; x++) {
//            messageData.add("Johnny: 默认聊天内容" + x);
//        }
        messageAdapter = new MessageAdapter(getActivity(), messageData);
        lvmessage.setAdapter(messageAdapter);
        lvmessage.setSelection(messageData.size());
        hlvaudience.setAdapter(new AudienceAdapter(getActivity()));
        startTimer();
    }


    private void initGiftLayout(View view) {
        giftFrameLayout1 = (GiftFrameLayout) view.findViewById(R.id.gift_layout1);
        giftFrameLayout2 = (GiftFrameLayout) view.findViewById(R.id.gift_layout2);

        ll_portrait = (LinearLayout) view.findViewById(R.id.ll_portrait);
        ll_landscape = (LinearLayout) view.findViewById(R.id.ll_landscape);
        giftLayout = (LinearLayout) view.findViewById(R.id.giftLayout);
        btnGift = (TextView) view.findViewById(R.id.toolbox_iv_face);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_gift);
        mViewpager = (ViewPager) view.findViewById(R.id.toolbox_pagers_face);
        mDotsLayout = (LinearLayout) view.findViewById(R.id.face_dots_container);

        giftLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里的作用是消费掉点击事件
            }
        });

        giftControl = new GiftControl(getActivity());
        giftControl.setGiftLayout(giftFrameLayout1, giftFrameLayout2);
        //礼物发送
        btnGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mGiftName)) {
                    toast("你还没选择礼物呢");
                } else {
                    if (TextUtils.isEmpty(mGifid))
                        toast("请稍后重试");
                    else {
//                        toast("礼物ID："+mGifid);
                        if (Double.valueOf(SPUserUtils.sharedInstance().getWallet()) >= Double.valueOf(mGiftPrice))
                            mSendgift(mGifid, mGiftPrice);
                        else toast(R.string.live_chat_tips);

//                    adapter.add(mGiftName);
                    }

                }
            }
        });


    }

    private List<GiftModel> toGiftModel(List<GiftBean.ListBean> datas) {
        List<GiftModel> giftModels = new ArrayList<>();
        GiftModel giftModel;
        for (int i = 0; i < datas.size(); i++) {
            GiftBean.ListBean giftListBean = datas.get(i);
            giftModel = new GiftModel(giftListBean.getId(), giftListBean.getTitle(), giftListBean.getImg(), giftListBean.getPrice());
            giftModels.add(giftModel);
        }
        return giftModels;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvChat:/*聊天*/
//                showChat();
                ppwindowComment();
                break;
//            case R.id.sendInput:/*发送*/
//                sendText();
//                break;
            case R.id.tvSendone:/*礼物1*/
//                showGift("Johnny1");
//
//                Intent intent = new Intent(getActivity(), ShareActivity.class);
//                intent.putExtra("add", 1);
//                startActivity(intent);

                toast(R.string.live_chat_share);
                break;
            case R.id.tvSendtwo:/*礼物2*/
//                showGift("Johnny2");
//                Praise();

                getActivity().finish();
                break;
            case R.id.tvSendthree:/*礼物3*/
                showGift("Johnny3");
                break;
            case R.id.tvSendfor:/*礼物4*/
//                showGift("showGiftJohnny4");
                banner.setVisibility(View.VISIBLE);
                toolbox_tv_num.setText(SPUserUtils.sharedInstance().getWallet());
                if (null == getGiftBean)
                    mGiftlist();
                btnGift.setBackgroundResource(R.drawable.shap_grey3);
                break;
        }
    }


    @OnClick({R.id.ll_chongzhi, R.id.btn_chat_sibo, R.id.btn_chat_zousi})
    void mChongzhiClick(View view) {
        switch (view.getId()) {
            case R.id.ll_chongzhi:
                banner.setVisibility(View.GONE);
                ppwCHongZhiShow();
                break;
            case R.id.btn_chat_sibo:
//                getCore();
//                Intent intent = new Intent(getActivity(), OpenActivity.class);
//                intent.putExtra("liveAid", mAid);
//                intent.putExtra("sibo", 100);
//                startActivityForResult(intent, 200);
                ppwPayShow2(msibom);
                break;
            case R.id.btn_chat_zousi:
                toast("走私");
                break;
            default:
                break;
        }
    }


    /**
     * 添加礼物view,(考虑垃圾回收)
     */
    private View addGiftView() {
        View view = null;
        if (giftViewCollection.size() <= 0) {
            /*如果垃圾回收中没有view,则生成一个*/
            view = LayoutInflater.from(getActivity()).inflate(R.layout.item_gift, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = 10;
            view.setLayoutParams(lp);
            llgiftcontent.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View view) {
                }

                @Override
                public void onViewDetachedFromWindow(View view) {
                    giftViewCollection.add(view);
                }
            });
        } else {
            view = giftViewCollection.get(0);
            giftViewCollection.remove(view);
        }
        return view;
    }

    /**
     * 删除礼物view
     */
    private void removeGiftView(final int index) {
        final View removeView = llgiftcontent.getChildAt(index);
        outAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llgiftcontent.removeViewAt(index);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        if (getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (removeView != null)
                        removeView.startAnimation(outAnim);
                }
            });
    }


    /**
     * 显示礼物的方法
     */
    private void showGift(String tag) {
        View giftView = llgiftcontent.findViewWithTag(tag);
        if (giftView == null) {/*该用户不在礼物显示列表*/

            if (llgiftcontent.getChildCount() > 2) {/*如果正在显示的礼物的个数超过两个，那么就移除最后一次更新时间比较长的*/
                View giftView1 = llgiftcontent.getChildAt(0);
                CircleImageView picTv1 = (CircleImageView) giftView1.findViewById(R.id.crvheadimage);
                long lastTime1 = (Long) picTv1.getTag();
                View giftView2 = llgiftcontent.getChildAt(1);
                CircleImageView picTv2 = (CircleImageView) giftView2.findViewById(R.id.crvheadimage);
                long lastTime2 = (Long) picTv2.getTag();
                if (lastTime1 > lastTime2) {/*如果第二个View显示的时间比较长*/
                    removeGiftView(1);
                } else {/*如果第一个View显示的时间长*/
                    removeGiftView(0);
                }
            }

            giftView = addGiftView();/*获取礼物的View的布局*/
            giftView.setTag(tag);/*设置view标识*/

            CircleImageView crvheadimage = (CircleImageView) giftView.findViewById(R.id.crvheadimage);
            final MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.giftNum);/*找到数量控件*/
            giftNum.setText("x1");/*设置礼物数量*/
            crvheadimage.setTag(System.currentTimeMillis());/*设置时间标记*/
            giftNum.setTag(1);/*给数量控件设置标记*/

            llgiftcontent.addView(giftView);/*将礼物的View添加到礼物的ViewGroup中*/
            llgiftcontent.invalidate();/*刷新该view*/
            giftView.startAnimation(inAnim);/*开始执行显示礼物的动画*/
            inAnim.setAnimationListener(new Animation.AnimationListener() {/*显示动画的监听*/
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    giftNumAnim.start(giftNum);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        } else {/*该用户在礼物显示列表*/
            CircleImageView crvheadimage = (CircleImageView) giftView.findViewById(R.id.crvheadimage);/*找到头像控件*/
            MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.giftNum);/*找到数量控件*/
            int showNum = (Integer) giftNum.getTag() + 1;
            giftNum.setText("x" + showNum);
            giftNum.setTag(showNum);
            crvheadimage.setTag(System.currentTimeMillis());
            giftNumAnim.start(giftNum);
        }
    }

    /**
     * 显示聊天布局
     */
    private void showChat() {
//        tvChat.setVisibility(View.GONE);
        llInputParent.setVisibility(View.VISIBLE);
        llInputParent.requestFocus();
//        showKeyboard();
    }

    /**
     * 发送消息
     */
    private void sendText() {
        if (!etInput.getText().toString().trim().isEmpty()) {

//            hideKeyboard();
            ChatBean chatBean = new ChatBean();
            chatBean.setLevel(-2);
            chatBean.setUsername(sharedInstance().getNickname() + " :");
            chatBean.setText(etInput.getText().toString().trim());

            messageData.add(chatBean);
            etInput.setText("");
            messageAdapter.NotifyAdapter(messageData);
            lvmessage.setSelection(messageData.size());
            popComment.dismiss();

        } else
            toast("内容不能为空");
//            hideKeyboard();
    }

    /**
     * 开始计时功能
     */
    private void startTimer() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(0);
        calendar.add(Calendar.HOUR_OF_DAY, -8);
        Date time = calendar.getTime();
        liveTime = time.getTime();
        second = time.getSeconds();
        handler.post(timerRunnable);
    }


    /**
     * 动态的修改listview的高度
     *
     * @param heightPX
     */
    private void dynamicChangeListviewH(int heightPX) {
        ViewGroup.LayoutParams layoutParams = lvmessage.getLayoutParams();
        layoutParams.height = DisplayUtil.dip2px(getActivity(), heightPX);
        lvmessage.setLayoutParams(layoutParams);
    }

    /**
     * 动态修改礼物父布局的高度
     *
     * @param showhide
     */
    private void dynamicChangeGiftParentH(boolean showhide) {
        if (showhide) {/*如果软键盘显示中*/
            if (llgiftcontent.getChildCount() != 0) {
                /*判断是否有礼物显示，如果有就修改父布局高度，如果没有就不作任何操作*/
                ViewGroup.LayoutParams layoutParams = llgiftcontent.getLayoutParams();
                layoutParams.height = llgiftcontent.getChildAt(0).getHeight();
                llgiftcontent.setLayoutParams(layoutParams);
            }
        } else {/*如果软键盘隐藏中*/
            /*就将装载礼物的容器的高度设置为包裹内容*/
            ViewGroup.LayoutParams layoutParams = llgiftcontent.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            llgiftcontent.setLayoutParams(layoutParams);
        }
    }

    /**
     * 头部布局执行显示的动画
     */
    private void animateToShow() {
        ObjectAnimator leftAnim = ObjectAnimator.ofFloat(rlsentimenttime, "translationX", -rlsentimenttime.getWidth(), 0);
        ObjectAnimator topAnim = ObjectAnimator.ofFloat(llpicimage, "translationY", -llpicimage.getHeight(), 0);
        animatorSetShow.playTogether(leftAnim, topAnim);
        animatorSetShow.setDuration(300);
        animatorSetShow.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isOpen = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isOpen = true;
            }
        });
        if (!isOpen) {
            animatorSetShow.start();
        }
    }

    /**
     * 头部布局执行退出的动画
     */
    private void animateToHide() {
        ObjectAnimator leftAnim = ObjectAnimator.ofFloat(rlsentimenttime, "translationX", 0, -rlsentimenttime.getWidth());
        ObjectAnimator topAnim = ObjectAnimator.ofFloat(llpicimage, "translationY", 0, -llpicimage.getHeight());
        animatorSetHide.playTogether(leftAnim, topAnim);
        animatorSetHide.setDuration(300);
        animatorSetHide.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isOpen = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isOpen = true;
            }
        });
        if (!isOpen) {
            animatorSetHide.start();
        }
    }


    /**
     * 定时清除礼物
     */
    private void clearTiming() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int count = llgiftcontent.getChildCount();
                for (int i = 0; i < count; i++) {
                    View view = llgiftcontent.getChildAt(i);
                    CircleImageView crvheadimage = (CircleImageView) view.findViewById(R.id.crvheadimage);
                    long nowtime = System.currentTimeMillis();
                    long upTime = (Long) crvheadimage.getTag();
                    if ((nowtime - upTime) >= 3000) {
                        removeGiftView(i);
                        return;
                    }
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, 3000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    /**
     * 数字放大动画
     */
    public class NumAnim {
        private Animator lastAnimator = null;

        public void start(View view) {
            if (lastAnimator != null) {
                lastAnimator.removeAllListeners();
                lastAnimator.end();
                lastAnimator.cancel();
            }
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "scaleX", 1.6f, 1.0f);
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "scaleY", 1.6f, 1.0f);
            AnimatorSet animSet = new AnimatorSet();
            lastAnimator = animSet;
            animSet.setDuration(200);
            animSet.setInterpolator(new OvershootInterpolator());
            animSet.playTogether(anim1, anim2);
            animSet.start();
        }
    }


    int praiseCount;
    final int praiseSendDelay = 2 * 1000;
    private Thread sendPraiseThread;

    /**
     * 点赞
     */
    void Praise() {
        periscopeLayout.addHeart();
        synchronized (this) {
            ++praiseCount;
        }
        if (sendPraiseThread == null) {
            sendPraiseThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
//                        int count = 0;
//                        synchronized (getActivity()) {
//                            count = praiseCount;
//                            praiseCount = 0;
//                        }
                        try {
                            Thread.sleep(praiseSendDelay + new Random().nextInt(500));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                }
            });
            sendPraiseThread.setDaemon(true);
            sendPraiseThread.start();
        }

    }

    private int mSuccess = 0;
    /**
     * 循环执行线程
     */
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(timerRunnable, 1000);
//            long sysTime = System.currentTimeMillis();
            liveTime += 1000;
//            CharSequence sysTimeStr = DateFormat.format("HH:mm:ss", liveTime);
//            CharSequence sysDateStr = DateFormat.format("yyyy/MM/dd", sysTime);
//            tvtime.setText(sysTimeStr);
//            tvID.setText(sysDateStr);
            second = Integer.valueOf(DateFormat.format("ss", liveTime).toString());
//            Log.e("second", "" + second);
//            Log.e("liveTime", "" + liveTime);
//            Log.e("？？？sad", "" + second / 2);

            if (second % 2 == 0) {
//                messageData.add("Johnny: " + etInput.getText().toString().trim());
//                etInput.setText("");
//                messageAdapter.NotifyAdapter(messageData);
//                lvmessage.setSelection(messageData.size());
                if (null != listBean) {
//                    Praise();
                    try {
//                        Log.e("随机run: ", "" + StringUtil.intRandom(listBean.getAudience_start(), listBean.getAudience_end()));
//                        Log.e("suiji人数", String.valueOf(listBean.getAudience() + StringUtil.intRandom(listBean.getAudience_start(), listBean.getAudience_end())));
//                        Log.e("suiji进", String.valueOf(mCoins += StringUtil.intRandom(listBean.getCoin_start(), listBean.getCoin_end())));
//                        if (listBean.getAudience_start() == listBean.getAudience_end())
//                            tvaudience.setText(String.valueOf(mpeople += StringUtil.intRandom(listBean.getAudience_start(), listBean.getAudience_end())) + "人");
//                        else
                        tvaudience.setText(String.valueOf(listBean.getAudience() + StringUtil.intRandom(listBean.getAudience_start(), listBean.getAudience_end())) + "人");
                        tvcoins.setText(String.valueOf(mCoins += StringUtil.intRandom(listBean.getCoin_start(), listBean.getCoin_end())));

                    } catch (Exception e) {
                        Log.e("run: ", e.toString());
                    }
                }
            }
            if (null != tv_chat_daojishi)
                tv_chat_daojishi.setText(player.getProgress(player.getCurrentPosition()));
            if (videotype == 1) {
                if (null != cikuBean) {
                    //这里的判断 是为了解决卡顿时多次次词库的重复问题
                    if (!mZanTing.equalsIgnoreCase(player.getProgress(player.getCurrentPosition())))
//                Log.e("cikuBean-" + cikuBean.getTime().get(mIntCiKu), player.getProgress(player.getCurrentPosition()));
                        if ((mIntCiKu = mList(cikuBean.getTime(), mZanTing = player.getProgress(player.getCurrentPosition()))) != -100) {
//                if (cikuBean.getTime().get(mIntCiKu).equalsIgnoreCase(player.getProgress(player.getCurrentPosition()))) {
                            List<AnchorListBean.ListBean.CikuBean.ConBean> conBean = cikuBean.getCon();
                            //提问内容
                            String msg = StringUtil.isNullOrEmpty(conBean.get(mIntCiKu).getTxt()) ? "" : conBean.get(mIntCiKu).getTxt();
                            AnchorListBean.ListBean.CikuBean.ConBean.UserBean userBeen = conBean.get(mIntCiKu).getUser();
                            String nick = userBeen.getNickname();
                            if (msg.length() > 0) {
                                ChatBean chatBean = new ChatBean();
                                chatBean.setUsername(nick + " :");
                                chatBean.setText(msg);
                                chatBean.setLevel(userBeen.getLevel());
                                messageData.add(chatBean);
                                messageAdapter.NotifyAdapter(messageData);
                                lvmessage.setSelection(messageData.size());
                            }
                            AnchorListBean.ListBean.CikuBean.ConBean.GiftBean giftBean = conBean.get(mIntCiKu).getGift();
                            if (null != giftBean)
                                if (!StringUtil.isNullOrEmpty(giftBean.getTitle())) {
                                    ChatBean chatBean2 = new ChatBean();
                                    chatBean2.setUsername(conBean.get(mIntCiKu).getUser().getNickname() + " :");
                                    Log.e("chatBean2", "run: " + giftBean.getNum());
                                    chatBean2.setText("送给主播" + giftBean.getNum() + "个" + giftBean.getTitle());
                                    chatBean2.setLevel(userBeen.getLevel());
                                    messageData.add(chatBean2);
                                    messageAdapter.NotifyAdapter(messageData);
                                    lvmessage.setSelection(messageData.size());

                                    giftControl.loadGift(new GiftModel(giftBean.getTitle(), giftBean.getTitle(), giftBean.getNum(), giftBean.getImg(), conBean.get(mIntCiKu).getUser().getUid(), conBean.get(mIntCiKu).getUser().getNickname(), conBean.get(mIntCiKu).getUser().getHeadpic(), System.currentTimeMillis()));
//                        giftControl.loadGift(new GiftModel(mGiftName, "礼物名字", 1, mGifturl, "1234", SPUserUtils.sharedInstance().getNickname(), "", System.currentTimeMillis()));
                                }

                            if (conBean.size() - 1 == mIntCiKu)
                                mIntCiKu = conBean.size() - 1;
                            else
                                mIntCiKu++;
                        }
                }
            }
            if (second > 2)
                if (null != listBean) {
                    for (int i = 1; i < StringUtil.intRandom(1, 5); i++)
                        Praise();
                }


            if (SPUserUtils.sharedInstance().getLevel().equalsIgnoreCase("0")) {
                if (chargeTips == 0) {
                    //直播到期
                    if ((int) SPUtils.get("daoqi" + mAid, -11) == 2) {
                        mDaoqi();
                        return;
                    }
                    //非直播到期
//                if (player.status == PlayStateParams.STATE_PAUSED) {
                    if (player.isFree() == true) {
                        if (player.getDuration() == 0)
                            SPUtils.put("daoqi" + mAid, 2);
                        mDaoqi();
                    }
                }
            } else
                try {
                    if (null != ll_chat_daoqi)
                        if (ll_chat_daoqi.getVisibility() == View.VISIBLE) {
                            if ((mSuccess = Integer.valueOf(SPUserUtils.sharedInstance().getLevel())) > 0) {
                                Log.e("mSuccessmSuccess", "run: " + mSuccess);
                                ll_chat_daoqi.setVisibility(View.GONE);
                                player.mPayPlay();
                                mSuccess++;
                            }
                        }
                } catch (Exception e) {
                    ll_chat_daoqi.setVisibility(View.GONE);
                }


//            if (config.getLevel().equalsIgnoreCase("0")) {
//                try {
//                    if (chargeDaojiShi != -1) {
//                        tv_chat_daojishi.setVisibility(View.VISIBLE);
//                        tv_chat_daojishi.setText(chargeDaojiShi + "后退出房间");
//                        chargeDaojiShi--;
//                    }
//                    if (chargeDaojiShi == 0) {
//                        getActivity().finish();
//                    }
//                } catch (Exception e) {
//                }
//            }

        }


    };

    private int mList(List<String> list, String playerIndex) {
        for (int i = 0; i < list.size(); i++) {
            try {
                if (list.get(i).equalsIgnoreCase(playerIndex)) {
                    return i;
                }
            } catch (Exception e) {
            }

        }
        return -100;
    }

    private void mDaoqi() {
        try {
            if (videotype == 2) return;
            SPUtils.put(mAid + "livedaoqi", 1);
            ll_chat_daoqi.setVisibility(View.VISIBLE);
//            Intent intent = new Intent(getActivity(), PlayerEndActivity.class);
//            intent.putExtra("liveAid", mAid);
//            intent.putExtra("chat", "chat");
//            startActivityForResult(intent, CHAT_CHARGE_TIPS);
            chatViews();
            chargeTips = 1;
        } catch (Exception e) {
        }
    }

    /**
     * 上层添加聊天界面 不影响其他页面的播放添加可见判断
     */
    private void chatViews() {
        mFrameLayout.setVisibility(View.VISIBLE);
//            noActionBar();
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        PlayerEndFragment mFragment = new PlayerEndFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("liveAid", mAid);
//        mFragment.setArguments(bundle);

        transaction.replace(R.id.chat_fragment, mFragment).commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHAT_CHARGE_TIPS)
            if (resultCode == RESULT_OK) {
                chargeTips = data.getIntExtra("chargeTips", 0);
                if (player.isFree() == true) {
//                if (player.status == PlayStateParams.STATE_PAUSED) {
                    if (data.getIntExtra("isTrue", 0) == 1) {
                        player.mPayPlay();
                        ll_chat_daoqi.setVisibility(View.GONE);
                    }
                    return;
                }

            } else {
                chargeDaojiShi = 15;
                getActivity().finish();
            }
        else {
            player.mPayPlay();
        }
    }

//    /**
//     * 充值会员ppw
//     */
//    private void ppwLevelShow() {
//        mPopWindow = PPWUtil.showPop(getActivity(), R.id.ll_chongzhi, R.layout.activity_open);
//        if (null != mPopWindow) {
//            Banner banner = (Banner) mPopWindow.findViewById(R.id.banner_op_heand);
//            TextView dfh = (TextView) mPopWindow.findViewById(R.id.tv_open_dfh);
//            TextView gw = (TextView) mPopWindow.findViewById(R.id.tv_open_gw);
//            ImageView level1 = (ImageView) mPopWindow.findViewById(R.id.iv_open_level1);
//            ImageView level2 = (ImageView) mPopWindow.findViewById(R.id.iv_open_level2);
//        }
//    }


    /**
     * 私播页面PPW
     */
    private void ppwPayShow() {

        mPopWindow = PPWUtil.showPop(getActivity(), R.id.ll_chongzhi, R.layout.activity_photo_graph);
        if (null != mPopWindow) {
            Button mWx = (Button) mPopWindow.findViewById(R.id.btn_selectphoto);
            Button mAli = (Button) mPopWindow.findViewById(R.id.btn_creame);
            Button mclose = (Button) mPopWindow.findViewById(R.id.btn_cancle);
            mWx.setTextColor(getResources().getColor(R.color.blue));
            try {
                XmlPullParser xrp = getActivity().getResources().getXml(R.color.pop_text_selector2);
                ColorStateList csl = ColorStateList.createFromXml(getResources(), xrp);
                mAli.setTextColor(csl);
            } catch (Exception e) {
            }

            mWx.setText("约她私播");
            mAli.setText("立即约(" + ((String) SPUtils.get("User_zs", "0")) + "元)");
            mclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PPWUtil.dismissPop();
                }
            });
            mAli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PPWUtil.dismissPop();
//                    ppwPayTypeShow();
                    selectPays(getActivity(), SPUserUtils.sharedInstance().getUid(), mAid, 2);

                }
            });


        }

    }

    private void ppwPayShow2(String sibo) {

        mPopWindow = PPWUtil.showPop(getActivity(), R.id.ll_chongzhi, R.layout.activity_open);
        if (null != mPopWindow) {
            Banner mWx = (Banner) mPopWindow.findViewById(R.id.banner_op_heand);
            RelativeLayout mRl1 = (RelativeLayout) mPopWindow.findViewById(R.id.rl_op_hj);
            RelativeLayout mRl2 = (RelativeLayout) mPopWindow.findViewById(R.id.rl_op_th);
            RelativeLayout mRl3 = (RelativeLayout) mPopWindow.findViewById(R.id.rl_op_sibo);
            ImageView mclose = (ImageView) mPopWindow.findViewById(R.id.iv_op_close);
            ImageView mAli = (ImageView) mPopWindow.findViewById(R.id.iv_open_level3);
            TextView mSiboM = (TextView) mPopWindow.findViewById(R.id.tv_open_gw3);
            mRl1.setVisibility(View.GONE);
            mRl2.setVisibility(View.GONE);
            mRl3.setVisibility(View.VISIBLE);
            mSiboM.setText(sibo + "元/次");
            setBannsers(mWx);
            getBanner(mWx);
            mclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PPWUtil.dismissPop();
                }
            });

            mAli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PPWUtil.dismissPop();
//                    ppwPayTypeShow();
                    selectPays(getActivity(), SPUserUtils.sharedInstance().getUid(), mAid, 2);

                }
            });


        }

    }


    private void setBannsers(Banner banner) {
        try {
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
                        Intent intent = new Intent(getActivity(), WebActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception w) {
        }
    }


    /**
     * 获取广告轮播图
     */
    public void getBanner(final Banner banner) {
        OkGo.get(Api.GET_BANNER_URL)
                .tag(getActivity())
                .params("sid", 4)
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


    String paytype = "";

    /**
     * 私播-充值方式选择页面PPW
     */
    private void ppwPayTypeShow(final String type) {
        if (null != popComment) popComment.dismiss();
        mPopWindow = PPWUtil.showPop(getActivity(), R.id.ll_chongzhi, R.layout.activity_photo_graph);
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
                    payLevel(type, paytype);
                    PPWUtil.dismissPop();
                }
            });
            mAli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    paytype = "alipay";
                    paytype = Constants.PAY_TYPE[1];
                    payLevel(type, paytype);
                    PPWUtil.dismissPop();
                }
            });


        }

    }


    private void payLevel(String type, String paytype) {
//        int level, String amount, String paytype
//        int si, String aid, int types, String amount, String paytype
        switch (type) {
            case "私播":
                if (Constants.SET_PAY_TYPE_JBY)
                    paySis(getActivity(), 1, mAid, 2, msibom, paytype);
                else
                    SNpaySis(getActivity(), 1, mAid, 2, msibom, paytype);
                break;
            case "500猫币":
                if (Constants.SET_PAY_TYPE_JBY)
                    payGolds(getActivity(), 500, "49", paytype);
                else
                    SNpayGolds(getActivity(), 500, "49", paytype);
                break;
            case "1000猫币":
                if (Constants.SET_PAY_TYPE_JBY)
                    payGolds(getActivity(), 1000, "100", paytype);
                else
                    SNpayGolds(getActivity(), 1000, "100", paytype);
                break;
            case "2000猫币":
                if (Constants.SET_PAY_TYPE_JBY)
                    payGolds(getActivity(), 2000, "200", paytype);
                else
                    SNpayGolds(getActivity(), 2000, "200", paytype);
                break;
            case "5000猫币":
                if (Constants.SET_PAY_TYPE_JBY)
                    payGolds(getActivity(), 5000, "500", paytype);
                else
                    SNpayGolds(getActivity(), 5000, "500", paytype);
                break;
            default:
                break;
        }

    }


    /**
     * 获取设置金额
     */
    public void getCore() {
        OkGo.get(Api.GET_USE_CORE)
                .tag(getActivity())
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
                                    msibom = conBean.getZs();
//                                    ppwPayShow2(msibom);
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

    private int cz = 0;
    TextView mYue;

    /**
     * 充值页面PPW
     */
    @SuppressWarnings("ResourceType")
    private void ppwCHongZhiShow() {
//        mPopWindow = PPWUtil.showPop(getActivity(), R.id.ll_chongzhi, R.layout.activity_chongzhi);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.activity_chongzhi, null);
//        popComment = new PopupWindow(v, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popComment = new PopupWindow(v,
                getActivity().getResources().getDisplayMetrics().widthPixels,
                getActivity().getResources().getDisplayMetrics().heightPixels, true);

        // 设置弹出窗口可点击
        popComment.setOutsideTouchable(true);
        popComment.setFocusable(true);
        // 更新
        popComment.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(00000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        popComment.setBackgroundDrawable(dw);
        popComment.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        popTips.setAnimationStyle(R.style.mypopwindow_anim_style);
        popComment.showAtLocation(llchongzhi, Gravity.BOTTOM, 0, 0);


        if (null != popComment) {
            mYue = (TextView) v.findViewById(R.id.tv_chat_yue);
            ImageView mClose = (ImageView) v.findViewById(R.id.iv_chongzhi_close);
            ImageView mPay = (ImageView) v.findViewById(R.id.rv_pay_ok);


            final LinearLayout LL1 = (LinearLayout) v.findViewById(R.id.ll_chat_cz1);
            final TextView tvm1 = (TextView) v.findViewById(R.id.tv_chat_m1);
            final TextView tvl1 = (TextView) v.findViewById(R.id.tv_chat_l1);
            final TextView tvy1 = (TextView) v.findViewById(R.id.tv_chat_y1);


            final LinearLayout LL2 = (LinearLayout) v.findViewById(R.id.ll_chat_cz2);
            final TextView tvm2 = (TextView) v.findViewById(R.id.tv_chat_m2);
            final TextView tvl2 = (TextView) v.findViewById(R.id.tv_chat_l2);
            final TextView tvy2 = (TextView) v.findViewById(R.id.tv_chat_y2);


            final LinearLayout LL3 = (LinearLayout) v.findViewById(R.id.ll_chat_cz3);
            final TextView tvm3 = (TextView) v.findViewById(R.id.tv_chat_m3);
            final TextView tvl3 = (TextView) v.findViewById(R.id.tv_chat_l3);
            final TextView tvy3 = (TextView) v.findViewById(R.id.tv_chat_y3);


            final LinearLayout LL4 = (LinearLayout) v.findViewById(R.id.ll_chat_cz4);
            final TextView tvm4 = (TextView) v.findViewById(R.id.tv_chat_m4);
            final TextView tvl4 = (TextView) v.findViewById(R.id.tv_chat_l4);
            final TextView tvy4 = (TextView) v.findViewById(R.id.tv_chat_y4);


            LL1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cz = 1;
                    LL1.setBackgroundResource(R.color.font_orange);
                    tvm1.setTextColor(getResources().getColor(R.color.white));
                    tvl1.setBackgroundResource(R.drawable.view_line_white);
                    tvy1.setTextColor(getResources().getColor(R.color.white));

                    LL2.setBackgroundResource(R.drawable.shap_ll_orange);
                    tvm2.setTextColor(getResources().getColor(R.color.font_orange));
                    tvl2.setBackgroundResource(R.drawable.view_line_or);
                    tvy2.setTextColor(getResources().getColor(R.color.font_orange));

                    LL3.setBackgroundResource(R.drawable.shap_ll_orange);
                    tvm3.setTextColor(getResources().getColor(R.color.font_orange));
                    tvl3.setBackgroundResource(R.drawable.view_line_or);
                    tvy3.setTextColor(getResources().getColor(R.color.font_orange));

                    LL4.setBackgroundResource(R.drawable.shap_ll_orange);
                    tvm4.setTextColor(getResources().getColor(R.color.font_orange));
                    tvl4.setBackgroundResource(R.drawable.view_line_or);
                    tvy4.setTextColor(getResources().getColor(R.color.font_orange));


                }
            });
            LL2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cz = 2;
                    LL2.setBackgroundResource(R.color.font_orange);
                    tvm2.setTextColor(getResources().getColor(R.color.white));
                    tvl2.setBackgroundResource(R.drawable.view_line_white);
                    tvy2.setTextColor(getResources().getColor(R.color.white));

                    LL1.setBackgroundResource(R.drawable.shap_ll_orange);
                    tvm1.setTextColor(getResources().getColor(R.color.font_orange));
                    tvl1.setBackgroundResource(R.drawable.view_line_or);
                    tvy1.setTextColor(getResources().getColor(R.color.font_orange));

                    LL3.setBackgroundResource(R.drawable.shap_ll_orange);
                    tvm3.setTextColor(getResources().getColor(R.color.font_orange));
                    tvl3.setBackgroundResource(R.drawable.view_line_or);
                    tvy3.setTextColor(getResources().getColor(R.color.font_orange));

                    LL4.setBackgroundResource(R.drawable.shap_ll_orange);
                    tvm4.setTextColor(getResources().getColor(R.color.font_orange));
                    tvl4.setBackgroundResource(R.drawable.view_line_or);
                    tvy4.setTextColor(getResources().getColor(R.color.font_orange));


                }
            });
            LL3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cz = 3;
                    LL3.setBackgroundResource(R.color.font_orange);
                    tvm3.setTextColor(getResources().getColor(R.color.white));
                    tvl3.setBackgroundResource(R.drawable.view_line_white);
                    tvy3.setTextColor(getResources().getColor(R.color.white));

                    LL2.setBackgroundResource(R.drawable.shap_ll_orange);
                    tvm2.setTextColor(getResources().getColor(R.color.font_orange));
                    tvl2.setBackgroundResource(R.drawable.view_line_or);
                    tvy2.setTextColor(getResources().getColor(R.color.font_orange));

                    LL1.setBackgroundResource(R.drawable.shap_ll_orange);
                    tvm1.setTextColor(getResources().getColor(R.color.font_orange));
                    tvl1.setBackgroundResource(R.drawable.view_line_or);
                    tvy1.setTextColor(getResources().getColor(R.color.font_orange));

                    LL4.setBackgroundResource(R.drawable.shap_ll_orange);
                    tvm4.setTextColor(getResources().getColor(R.color.font_orange));
                    tvl4.setBackgroundResource(R.drawable.view_line_or);
                    tvy4.setTextColor(getResources().getColor(R.color.font_orange));


                }
            });
            LL4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cz = 4;
                    LL4.setBackgroundResource(R.color.font_orange);
                    tvm4.setTextColor(getResources().getColor(R.color.white));
                    tvl4.setBackgroundResource(R.drawable.view_line_white);
                    tvy4.setTextColor(getResources().getColor(R.color.white));

                    LL2.setBackgroundResource(R.drawable.shap_ll_orange);
                    tvm2.setTextColor(getResources().getColor(R.color.font_orange));
                    tvl2.setBackgroundResource(R.drawable.view_line_or);
                    tvy2.setTextColor(getResources().getColor(R.color.font_orange));

                    LL3.setBackgroundResource(R.drawable.shap_ll_orange);
                    tvm3.setTextColor(getResources().getColor(R.color.font_orange));
                    tvl3.setBackgroundResource(R.drawable.view_line_or);
                    tvy3.setTextColor(getResources().getColor(R.color.font_orange));

                    LL1.setBackgroundResource(R.drawable.shap_ll_orange);
                    tvm1.setTextColor(getResources().getColor(R.color.font_orange));
                    tvl1.setBackgroundResource(R.drawable.view_line_or);
                    tvy1.setTextColor(getResources().getColor(R.color.font_orange));


                }
            });


//            final RadioGroup rbBottom = (RadioGroup) v.findViewById(R.id.rg_bottom);
            final RadioGroup rbSelect = (RadioGroup) v.findViewById(R.id.rg_pay_select);
            mYue.setText(SPUserUtils.sharedInstance().getWallet() + getString(R.string.live_gold));
            mClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v
                ) {
                    popComment.dismiss();
                }
            });
            mPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v
                ) {
//                    rbBottom.setVisibility(View.GONE);
//                    rbSelect.setVisibility(View.VISIBLE);
                    if (cz == 0)
                        toast("请先选择");
                    else {
//                        ppwPayMbShow(cz);
                        switch (cz) {
                            case 1:
                                ppwPayTypeShow("500猫币");
                                break;
                            case 2:
                                ppwPayTypeShow("1000猫币");
                                break;
                            case 3:
                                ppwPayTypeShow("2000猫币");
                                break;
                            case 4:
                                ppwPayTypeShow("5000猫币");
                                break;
                        }
                    }
                }
            });
//
//            rbBottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                    switch (checkedId) {
//                        case R.id.mrb_pay_300:
//                            break;
//                        case R.id.mrb_pay_1k:
//                            break;
//                    }
//                }
//            });
        }

    }


    /**
     * 猫币-充值方式选择页面PPW
     */

    private void ppwPayMbShow(int type) {

        mPopWindow = PPWUtil.showPop(getActivity(), R.id.ll_chongzhi, R.layout.activity_photo_graph);
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

                    PPWUtil.dismissPop();
                }
            });
            mAli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PPWUtil.dismissPop();
                }
            });


        }

    }

    CoreBean coreBean;

    /**
     * 获取主播详情
     */
    public void getAnchorData() {
        if (!StringUtil.isNullOrEmpty(mAid))
            OkGo.get(Api.GET_ANCHOR_MESSAGE_URL)
                    .tag(getActivity())
                    .params("uid", uid)
                    .params("aid", mAid)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Log.e("主播详情：", s);

                            AnchorBean bean = GsonUtil.parseJsonWithGson(s, AnchorBean.class);
                            if (null != bean) {
                                if (bean.getStatus().equals("1")) {
                                    listBean = bean.getList();

                                    SPUtils.put(mAid + "endaudience", String.valueOf(listBean.getAudience()));
                                    SPUtils.put(mAid + "endduration", player.getProgresEnd(player.getDuration()));

                                    String ciku_id = listBean.getCiku_id();
                                    if (videotype == 1) {
                                        if (!StringUtil.isNullOrEmpty(ciku_id))
                                            //第一次加载
                                            if (StringUtil.isNullOrEmpty((String) SPCiKuUtils.get(ciku_id + "msg", ""))) {
                                                page = 2;
                                                cikuBean = GsonUtil.parseJsonWithGson(new Gson().toJson(listBean.getCiku()), AnchorListBean.ListBean.CikuBean.class);
                                                cikuBean.setmPager(page);
                                                SPCiKuUtils.put(ciku_id + "msg", new Gson().toJson(cikuBean));
                                                getMoreCiku(ciku_id);

                                            } else {
                                                //判断本地是否储存完毕 以词库ID为准
                                                String ciku = (String) SPCiKuUtils.get(ciku_id + "msg", "");
                                                Log.e("词库", ciku);
                                                cikuBean = GsonUtil.parseJsonWithGson(ciku, AnchorListBean.ListBean.CikuBean.class);
                                                if (cikuBean.getmSuccess() != 1) {
                                                    page = cikuBean.getmPager();
                                                    getMoreCiku(ciku_id);
                                                }
                                            }
                                    }

                                    initView(listBean);
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
     * 网络 获取更多的词库
     */
    public void getMoreCiku(final String ciku_id) {
        OkGo.get(Api.GET_USE_GETMORECIKU)
                .tag(getActivity())
                .params("ciku_id", ciku_id)
                .params("num", 10)
                .params("page", page)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        request.removeHeader("channel");
                        super.onBefore(request);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("更多的词库：", s);
//                        MoreCiKuBean morecikuBean = GsonUtil.parseJsonWithGson(s, MoreCiKuBean.class);
                        AnchorListBean.ListBean morecikuBean = GsonUtil.parseJsonWithGson(s, AnchorListBean.ListBean.class);
                        if (null != morecikuBean && 1 == morecikuBean.getStatus()) {
                            page++;
                            if (null != morecikuBean.getCiku().getTime()) {
                                cikuBean.getTime().addAll(morecikuBean.getCiku().getTime());
                                cikuBean.getCon().addAll(morecikuBean.getCiku().getCon());
                                cikuBean.setmPager(page);
                                SPCiKuUtils.put(ciku_id + "msg", new Gson().toJson(cikuBean));
//                                SPCiKuUtils.put(ciku_id + "msg", ((String) SPCiKuUtils.get(ciku_id + "msg", "")) + new Gson().toJson(cikuBean));
//                                new Gson().toJson(mList.get(position).getCiku())
                            }
                            getMoreCiku(ciku_id);
                        } else {
                            //加载词库完成
                            cikuBean.setmSuccess(1);
                            SPCiKuUtils.put(ciku_id + "msg", new Gson().toJson(cikuBean));
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.e("更多的词库onError", e.toString());
                        super.onError(call, response, e);
                    }
                });

    }


    private void initView(AnchorBean.ListBean listBean) {
        try {
            if (null != ivChatHead)
                if (!StringUtil.isNullOrEmpty(listBean.getSmallpic()))
                    Picasso.with(getActivity())
                            .load(listBean.getSmallpic())
                            .placeholder(R.color.white)
                            .resize(100, 100).centerCrop()
                            .error(R.drawable.icon_kongbai)
                            .into(ivChatHead);
            mCoins = listBean.getCoin();
            mpeople = listBean.getAudience();
            tvaudience.setText(String.valueOf(listBean.getAudience()));
            tvnickname.setText(String.valueOf(listBean.getNickname()));
            tvID.setText(listBean.getAid());
            tvcoins.setText(String.valueOf(listBean.getCoin()));
            is_qx = Integer.valueOf(listBean.getIs_focus());
            if (listBean.getIs_focus().equals("0")) {
                tvchatguanzhu.setText("关注");
                tvchatguanzhu.setBackgroundResource(R.drawable.shap_de_12);
            } else {
                tvchatguanzhu.setText("取消");
                tvchatguanzhu.setBackgroundResource(R.drawable.shap_12);
            }
        } catch (Exception e) {
        }
    }

    @OnClick(R.id.iv_chat_head)
    public void mHeadClick() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.activity_anchor_detail, null);
//        popComment = new PopupWindow(v, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popComment = new PopupWindow(v,
                getActivity().getResources().getDisplayMetrics().widthPixels,
                getActivity().getResources().getDisplayMetrics().heightPixels, true);

        // 设置弹出窗口可点击
        popComment.setOutsideTouchable(true);
        popComment.setFocusable(true);
        // 更新
        popComment.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(00000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        popComment.setBackgroundDrawable(dw);
        popComment.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        popTips.setAnimationStyle(R.style.mypopwindow_anim_style);
        popComment.showAtLocation(llchongzhi, Gravity.BOTTOM, 0, 0);


//        mPopWindow = PPWUtil.showPop(getActivity(), R.id.iv_chat_head, R.layout.activity_anchor_detail);
        if (null != popComment) {
            CircleImageView ppwhead = (CircleImageView) v.findViewById(R.id.iv_chat_d_head);
            ImageView ppwClose = (ImageView) v.findViewById(R.id.iv_chat_d_close);
            ImageView ppwQQ = (ImageView) v.findViewById(R.id.iv_chat_d_qq);
            ImageView ppwWeixin = (ImageView) v.findViewById(R.id.iv_chat_d_weixin);
            RelativeLayout ppwJuBao = (RelativeLayout) v.findViewById(R.id.rv_caht_d_jb);
            TextView ppwname = (TextView) v.findViewById(R.id.tv_caht_d_name);
            TextView ppwID = (TextView) v.findViewById(R.id.tv_caht_d_aid);
            TextView ppwchatnums = (TextView) v.findViewById(R.id.chat_d_nums);
            TextView ppwfen = (TextView) v.findViewById(R.id.chat_d_fens);
            ImageView ppw_chat_si = (ImageView) v.findViewById(R.id.ppw_chat_si);

            try {

                Picasso.with(getActivity())
                        .load(listBean.getSmallpic())
                        .resize(150, 150).centerCrop()
                        .error(R.drawable.icon_kongbai)
                        .into(ppwhead);
                ppwname.setText(listBean.getNickname());
                ppwID.setText(mAid);
//                ppwchatnums.setText(String.valueOf(listBean.getAudience() + StringUtil.intRandom(listBean.getAudience_start(), listBean.getAudience_end())));
//                ppwfen.setText(String.valueOf(mCoins += StringUtil.intRandom(listBean.getCoin_start(), listBean.getCoin_end())));
                ppwchatnums.setText(tvaudience.getText().toString().replace("人", ""));
//                if (listBean.getFun_start() == listBean.getFun_end())
//                    ppwfen.setText(String.valueOf(listBean.getFun() + StringUtil.intRandom(listBean.getFun_start(), listBean.getFun_end() + 2)));
//                else
                ppwfen.setText(String.valueOf(listBean.getFun() + StringUtil.intRandom(listBean.getFun_start(), listBean.getFun_end())));
            } catch (Exception e) {

            }
            ppwClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != popComment) popComment.dismiss();
                }
            });

            ppwQQ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (SPUserUtils.sharedInstance().getLevel().equals("2")) {
                        StringUtil.copyText(listBean.getQq());
                        toast(R.string.anchor_qq);
                    } else toast(R.string.chat_th_tips);
                }
            });
            ppwWeixin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SPUserUtils.sharedInstance().getLevel().equals("2")) {
                        StringUtil.copyText(listBean.getWx());
                        toast(R.string.anchor_weixin);
                    } else toast(R.string.chat_th_tips);
                }
            });
            ppw_chat_si.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != popComment) popComment.dismiss();
                    ppwindowsixin();
                }
            });


            ppwJuBao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPbanchor();
                }
            });
        }


    }

    /**
     * 输入框PPW
     */
    public void ppwindowsixin() {
        try {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.pop_sixin, null);
            popComment = new PopupWindow(v, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            TextView caht_sixin_nick = (TextView) v.findViewById(R.id.caht_sixin_nick);
            final EditText etInput2 = (EditText) v.findViewById(R.id.caht_sixin_etInput);
            TextView sendInput2 = (TextView) v.findViewById(R.id.caht_sixin_sendInput);
            ImageView ppwClose = (ImageView) v.findViewById(R.id.caht_sixin_close);
            mListView = (ListView) v.findViewById(R.id.caht_sixin_lvmessage);


            final MessageAdapter2 messageAdapter = new MessageAdapter2(getActivity(), messageData2);
            mListView.setAdapter(messageAdapter);
            mListView.setSelection(messageData2.size());


            caht_sixin_nick.setText(listBean.getNickname());
            ppwClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != popComment) popComment.dismiss();
                }
            });

            sendInput2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                releaseComment(et_comment_content.getText().toString().trim());
//                sendText();
                    if (!StringUtil.isNullOrEmpty(etInput2.getText().toString().trim())) {
                        ChatBean chatBean = new ChatBean();
                        chatBean.setLevel(0);
                        chatBean.setUsername(sharedInstance().getNickname() + " :");
                        chatBean.setText(etInput2.getText().toString().trim());

                        messageData2.add(chatBean);
                        etInput2.setText("");
                        messageAdapter.NotifyAdapter(messageData2);
                        mListView.setSelection(messageData2.size());
//                    popComment.dismiss();
                    } else {
                        toast("内容不能为空");
                    }
                }
            });


            // 设置弹出窗口可点击
            popComment.setOutsideTouchable(true);
            popComment.setFocusable(true);
            // 更新
            popComment.update();
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(00000000);
            // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
            popComment.setBackgroundDrawable(dw);
            popComment.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popComment.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (isSoftShowing())
                        popupInputMethodWindow();
                }
            });
//        popTips.setAnimationStyle(R.style.mypopwindow_anim_style);
            popComment.showAtLocation(tvChat, Gravity.BOTTOM, 0, 0);
            popupInputMethodWindow();
        } catch (Exception w) {
        }
    }

    /**
     * 输入框PPW
     */
    public void ppwindowComment() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.pop_comment, null);
        popComment = new PopupWindow(v, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        etInput = (EditText) v.findViewById(R.id.etInput);
        sendInput = (TextView) v.findViewById(R.id.sendInput);
        final CheckBox mChatdanmu = (CheckBox) v.findViewById(R.id.mrb_chat_danmu);
        if (Double.valueOf(SPUserUtils.sharedInstance().getWallet()) <= 0)
            mChatdanmu.setChecked(false);
        else mChatdanmu.setChecked(true);
        mChatdanmu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    if (Double.valueOf(SPUserUtils.sharedInstance().getWallet()) <= 0) {
                        mChatdanmu.setChecked(false);
                        toast("请先充值金币");
//                        popComment.dismiss();
//                        ppwPayShow();
                    }
                }
            }
        });

        sendInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                releaseComment(et_comment_content.getText().toString().trim());
                sendText();
            }
        });

        // 设置弹出窗口可点击
        popComment.setOutsideTouchable(true);
        popComment.setFocusable(true);
        // 更新
        popComment.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(00000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        popComment.setBackgroundDrawable(dw);
        popComment.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popComment.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (isSoftShowing())
                    popupInputMethodWindow();
            }
        });
//        popTips.setAnimationStyle(R.style.mypopwindow_anim_style);
        popComment.showAtLocation(tvChat, Gravity.BOTTOM, 0, 0);
        popupInputMethodWindow();
    }


    /**
     * 关注
     */

    @OnClick(tv_chat_guanzhu)
    public void getGuanZhu() {
        OkGo.get(Api.GET_USE_GUANZHU)
                .tag(getActivity())
                .params("uid", uid)
                .params("aid", mAid)
                .params("num", num)
                .params("is_qx", is_qx)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("关注", s);

                        BackMsg backMsg = GsonUtil.parseJsonWithGson(s, BackMsg.class);
                        if (null != backMsg && 1 == backMsg.getStatus()) {
                            if (1 == backMsg.getStatus()) {
                                toast(backMsg.getInfo());
                                is_qx = Math.abs(is_qx -= 1);
                                if (is_qx == 0) {
                                    tvchatguanzhu.setText("关注");
                                    tvchatguanzhu.setBackgroundResource(R.drawable.shap_de_12);
                                } else {
                                    tvchatguanzhu.setText("取消");
                                    tvchatguanzhu.setBackgroundResource(R.drawable.shap_12);
                                }
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
     * 屏蔽
     */

    public void mPbanchor() {
        OkGo.get(Api.GET_USE_PBANCHOR)
                .tag(getActivity())
                .params("uid", uid)
                .params("aid", mAid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("屏蔽", s);

                        BackMsg backMsg = GsonUtil.parseJsonWithGson(s, BackMsg.class);
                        if (null != backMsg && 1 == backMsg.getStatus()) {
//                        if (1 == backMsg.getStatus()) {
                            toast("举报成功");
//                        }

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
     * 送礼物
     *
     * @param gid     礼物ID
     * @param goldnum 礼物价格
     */

    public void mSendgift(String gid, final String goldnum) {
        OkGo.get(Api.GET_USE_SENDGIFT)
                .tag(getActivity())
                .params("uid", uid)
                .params("aid", mAid)
                .params("gid", gid)
                .params("goldnum", goldnum)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("送礼物", s);
                        GiftBackBean giftBackBean = GsonUtil.parseJsonWithGson(s, GiftBackBean.class);
                        if (null != giftBackBean) {
                            if (null != giftBackBean && 1 == giftBackBean.getStatus()) {
                                try {
                                    SPUserUtils.sharedInstance().setWallet(String.valueOf(Double.valueOf(SPUserUtils.sharedInstance().getWallet()) - Double.valueOf(goldnum)));
                                    SPUserUtils.sharedInstance().savePreferences();
                                    toolbox_tv_num.setText(SPUserUtils.sharedInstance().getWallet());
                                    getData();
                                    giftControl.loadGift(new GiftModel(mGiftName, mGiftName, 1, mGifturl, "sendUserId", sharedInstance().getNickname(), sharedInstance().getHeadpic(), System.currentTimeMillis()));

                                    ChatBean chatBean = new ChatBean();
                                    chatBean.setUsername(sharedInstance().getNickname() + " :");
                                    chatBean.setText("送给主播" + "1个" + mGiftName);
                                    chatBean.setLevel(Integer.valueOf(sharedInstance().getLevel()));
                                    messageData.add(chatBean);
                                    messageAdapter.NotifyAdapter(messageData);
                                    lvmessage.setSelection(messageData.size());
                                } catch (Exception w) {
                                }
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
     * 获取用户信息
     */
    private void getData() {
        OkGo.get(Api.GET_USER_INFORMATION)
                .tag(this)
                .params("uid", uid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("用户信息：", s);

                        try {
                            UsersBean bean = GsonUtil.parseJsonWithGson(s, UsersBean.class);
                            if (null != bean && 1 == bean.getStatus()) {
                                UsersBean.UserBean userbran = bean.getUser();
                                SPUserUtils config = sharedInstance();

                                config.setUid(userbran.getUid());
                                config.setNickname(userbran.getNickname());
                                config.setHeadpic(userbran.getHeadpic());
                                config.setSex(userbran.getSex());
                                config.setLevel(userbran.getLevel());
                                config.setWallet(userbran.getWallet());
                                config.setFocus(userbran.getFocus());

                                config.savePreferences();

                                toolbox_tv_num.setText(userbran.getWallet());
                            }
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.e("s", e.toString());
                        super.onError(call, response, e);
                    }
                });

    }

    private boolean isInitCache = false;

    /**
     * 礼物列表
     */
    public void mGiftlist() {
        OkGo.get(Api.GET_USE_GIFTLIST)
                .tag(getActivity())
                .params("uid", uid)
//                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            Log.e("礼物列表", s);
                            GiftBean giftBean = GsonUtil.parseJsonWithGson(s, GiftBean.class);
                            if (null != giftBean) {
                                if (null != giftBean && 1 == giftBean.getStatus()) {
                                    getGiftBean = giftBean.getList();

                                    List<GiftModel> giftModels = toGiftModel(getGiftBean);//转化为发送礼物的集合

                                    GiftPanelControl giftPanelControl = new GiftPanelControl(getActivity(), mViewpager, mRecyclerView, mDotsLayout);
                                    giftPanelControl.init(giftModels);//这里如果为null则加载本地礼物图片
                                    giftPanelControl.setGiftListener(new GiftPanelControl.GiftListener() {
                                        @Override
                                        public void getGiftInfo(String giftid, String giftPic, String giftName, String giftPrice) {
                                            mGifid = giftid;
                                            mGifturl = giftPic;
                                            mGiftName = giftName;
                                            mGiftPrice = giftPrice;
                                            btnGift.setBackgroundResource(R.drawable.shap_pink3);
                                        }

                                    });
                                }

                            }
                        } catch (Exception e) {
                        }
                    }

//                    @Override
//                    public void onCacheSuccess(String s, Call call) {
//                        //一般来说,只需呀第一次初始化界面的时候需要使用缓存刷新界面,以后不需要,所以用一个变量标识
//                        if (!isInitCache) {
//                            Log.e("礼物缓存", s);
//                            //一般来说,缓存回调成功和网络回调成功做的事情是一样的,所以这里直接回调onSuccess
//                            onSuccess(s, call, null);
//                            isInitCache = true;
//                        }
//                    }
//
//
//                    @Override
//                    public void onAfter(@Nullable String s, @Nullable Exception e) {
//                        super.onAfter(s, e);
//                        //可能需要移除之前添加的布局
//                        newsAdapter.removeAllFooterView();
//                        //最后调用结束刷新的方法
////                        setRefreshing(false);
//                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.e("onError", e.toString());
                        super.onError(call, response, e);
                    }
                });

    }


    /**
     * 获取机器人聊天
     *
     * @param mAid
     */
    private void getCiku(String mAid) {
        if (!StringUtil.isNullOrEmpty(mAid)) {
            String ciku = (String) SPUtils.get(mAid + "msg", "");
            cikuBean = GsonUtil.parseJsonWithGson(ciku, AnchorListBean.ListBean.CikuBean.class);
        }
    }


    //弹出软键盘
    private void popupInputMethodWindow() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 0);
    } //弹出软键盘

    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getActivity().getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int VIewscreenHeight = rect.bottom;
        if (screenHeight - VIewscreenHeight - getSoftButtonsBarHeight() != 0) {
            Log.e(TAG, "isSoftShowing--可见");
        } else {
            Log.e(TAG, "isSoftShowing---不可见");
        }
//        screenHeight - rect.bottom - getSoftButtonsBarHeight()!= 0
        return screenHeight - VIewscreenHeight - getSoftButtonsBarHeight() != 0;
    }

    /**
     * 底部虚拟按键栏的高度
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

//
//    /**
//     * 显示软键盘并因此头布局
//     */
//    private void showKeyboard() {
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                InputMethodManager imm = (InputMethodManager)
//                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(etInput, InputMethodManager.SHOW_FORCED);
//            }
//        }, 100);
//    }
//

    /**
     * 隐藏软键盘并显示头布局
     */
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0);
    }


//
//    /**
//     * 软键盘显示与隐藏的监听
//     */
//    private void softKeyboardListnenr() {
//        SoftKeyBoardListener.setListener(getActivity(), new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
//            @Override
//            public void keyBoardShow(int height) {/*软键盘显示：执行隐藏title动画，并修改listview高度和装载礼物容器的高度*/
//                animateToHide();
//                dynamicChangeListviewH(100);
//                dynamicChangeGiftParentH(true);
//            }
//
//            @Override
//            public void keyBoardHide(int height) {/*软键盘隐藏：隐藏聊天输入框并显示聊天按钮，执行显示title动画，并修改listview高度和装载礼物容器的高度*/
//                tvChat.setVisibility(View.VISIBLE);
//                llInputParent.setVisibility(View.GONE);
//                animateToShow();
//                dynamicChangeListviewH(150);
//                dynamicChangeGiftParentH(false);
//            }
//        });
//    }


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
                                        toast("你已经和该直播私播过了，还要在继续么？");
                                        ppwPayTypeShow("私播");
                                    } else {
                                        ppwPayTypeShow("私播");

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


    private GoldBean mGoldbean;


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
//                        if (null != mGoldbean) {
//                            mFwPAy(amount, mGoldbean.getRemark(), paytype.equalsIgnoreCase("wxpay") ? 1 : 2);
//                        }


                        Log.e("付款-走私=付费观看一次，私播=打赏：", s);
                        if (paytype.equalsIgnoreCase("alipay")) {
                            PayBean bean = GsonUtil.parseJsonWithGson(s, PayBean.class);
                            if (null != bean && !StringUtil.isNullOrEmpty(bean.getUrl())) {
                                final String info = bean.getUrl();
                                Runnable payRunnable = new Runnable() {

                                    @Override
                                    public void run() {
                                        // 构造payTask 对象
                                        PayTask payTask = new PayTask(getActivity());
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
                                DialogUtil.errorDialog(getActivity(), getString(R.string.wx_pay_callback_msg));
                        } else if (paytype.equalsIgnoreCase(SELECT_PAY_TYPE[0])) {
                            PayBean bean = GsonUtil.parseJsonWithGson(s, PayBean.class);
                            if (null != bean && !StringUtil.isNullOrEmpty(bean.getUrl()))
                                MyAppUtil.jumpToWeb(getActivity(), bean.getUrl());
                            else toast(R.string.pay_null_tips);
                            Log.e("", "onSuccess: wx");
                        } else {
                            PayBean bean = GsonUtil.parseJsonWithGson(s, PayBean.class);
                            if (null != bean && !StringUtil.isNullOrEmpty(bean.getUrl()))
                                MyAppUtil.jumpToWeb(getActivity(), bean.getUrl());
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
//                        Log.e("付款-充值金币：", s);
//                        mGoldbean = GsonUtil.parseJsonWithGson(s, GoldBean.class);
//                        if (null != mGoldbean) {
//                            mFwPAy(amount, mGoldbean.getRemark(), paytype.equalsIgnoreCase("wxpay") ? 1 : 2);
//                        }


                        Log.e("付款-充值金币：", s);
                        if (paytype.equalsIgnoreCase("alipay")) {
                            PayBean bean = GsonUtil.parseJsonWithGson(s, PayBean.class);
                            if (null != bean && !StringUtil.isNullOrEmpty(bean.getUrl())) {
                                final String info = bean.getUrl();
                                Runnable payRunnable = new Runnable() {

                                    @Override
                                    public void run() {
                                        // 构造payTask 对象
                                        PayTask payTask = new PayTask(getActivity());
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
                                DialogUtil.errorDialog(getActivity(), getString(R.string.wx_pay_callback_msg));
                        } else if (paytype.equalsIgnoreCase(SELECT_PAY_TYPE[0])) {
                            PayBean bean = GsonUtil.parseJsonWithGson(s, PayBean.class);
                            if (null != bean && !StringUtil.isNullOrEmpty(bean.getUrl()))
                                MyAppUtil.jumpToWeb(getActivity(), bean.getUrl());
                            else toast(R.string.pay_null_tips);
                            Log.e("", "onSuccess: wx");
                        } else {
                            PayBean bean = GsonUtil.parseJsonWithGson(s, PayBean.class);
                            if (null != bean && !StringUtil.isNullOrEmpty(bean.getUrl()))
                                MyAppUtil.jumpToWeb(getActivity(), bean.getUrl());
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
                    player.mPayPlay();
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


    private PayOrder orders;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ToastUtil.showToast(getActivity(), "网络错误", Toast.LENGTH_SHORT);
                    break;
                case 1:
                    try {
                        String channelType = (String) msg.obj;
//                        if ("[]".equals(channelType)) {
                        if (!channelType.startsWith("[")) {
                            ToastUtil.showToast(getActivity(), channelType, Toast.LENGTH_SHORT);
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
                        FWPay.pay(getActivity(), orders, index, new OnPayResultListener() {
                            @Override
                            public void onSuccess(Integer code, String message, String payId) {
                                Log.e("聚宝云支付-onSuccess", "[code=" + code + "]"
                                        + "[message=" + message + "]" + "[payId=" + payId + "]");
                                GoldPay.paySuccessAfter(getActivity(), SPUserUtils.sharedInstance().getUid());
                            }

                            @Override
                            public void onFailed(Integer code, String message, String payId) {
                                Log.e("聚宝云支付-onFailed", "[code=" + code + "]"
                                        + "[message=" + message + "]" + "[payId=" + payId + "]");
                            }
                        });
//                        if (levels != 0)
//                            if (StringUtil.isNullOrEmpty(chat))
//                                setLevel(String.valueOf(levels));
//                            else setLevel(String.valueOf(levels), true);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtil.showToast(getActivity(), "支付通道配置错误", Toast.LENGTH_SHORT);
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
                .setGoodsName(SPUserUtils.sharedInstance().getNickname())
                .setRemark(goodName)
                //商户订单号(必需)
                .setPayId(mGoldbean.getOrdernum())
//                .setPayId(CommonUtils.setRand())
                //玩家Id(必需)
                .setPlayerId(SPUserUtils.sharedInstance().getUid());


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
    public void onStop() {
        super.onStop();
        try {
            if (!StringUtil.isNullOrEmpty(mAid)) {
                listBean.setAudience(Integer.valueOf(tvaudience.getText().toString().replace("人", "")));
                listBean.setCoin(Integer.valueOf(tvcoins.getText().toString()));
                listBean.setFun(Integer.valueOf(String.valueOf(listBean.getFun() + StringUtil.intRandom(listBean.getFun_start(), listBean.getFun_end()))));
                OkGo.get(Api.GET_USE_EDITANCHOR)
                        .tag(getActivity())
                        .params("aid", mAid)
                        .params("audience", listBean.getAudience())
                        .params("coin", listBean.getCoin())
                        .params("fun", listBean.getFun())
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                Log.e("修改主播信息：", s);
//                            AnchorBean bean = GsonUtil.parseJsonWithGson(s, AnchorBean.class);
                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                Log.e("onError", e.toString());
                                super.onError(call, response, e);
                            }
                        });

            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SPUtils.put(mAid + "mIntCiKu_Index", mIntCiKu);
        //Activity销毁时，取消网络请求
        OkGo.getInstance().cancelTag(this);
    }


}
