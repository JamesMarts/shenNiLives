package com.shenni.lives.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shenni.lives.R;
import com.shenni.lives.activity.AnchorMoneyActivity;
import com.shenni.lives.activity.DynActivity;
import com.shenni.lives.activity.MyAccActivity;
import com.shenni.lives.activity.MyDataActivity;
import com.shenni.lives.activity.ShareActivity;
import com.shenni.lives.api.Api;
import com.shenni.lives.base.BaseFragment;
import com.shenni.lives.bean.CoreBean;
import com.shenni.lives.bean.UsersBean;
import com.shenni.lives.utils.Constants;
import com.shenni.lives.utils.GsonUtil;
import com.shenni.lives.utils.SPUserUtils;
import com.shenni.lives.utils.SPUtils;
import com.shenni.lives.utils.StringUtil;
import com.shenni.lives.widget.PersonalScrollView;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;


/**
 * 我的页面
 */
public class MineFragment extends BaseFragment {


    @InjectView(R.id.iv_bagpic)
    ImageView ivBagpic;
    @InjectView(R.id.iv_mine_head)
    CircleImageView ivMineHead;
    @InjectView(R.id.tv_mine_name)
    TextView tvMineName;
    @InjectView(R.id.tv_my_golds)
    TextView tvMineGolds;
    @InjectView(R.id.tv_mine_gold)
    TextView tvMineGold;
    @InjectView(R.id.tv_mine1)
    ImageView tvMine1;
    @InjectView(R.id.rl_mine_acc)
    RelativeLayout rlMineAcc;
    @InjectView(R.id.tv_mine2)
    ImageView tvMine2;
    @InjectView(R.id.rl_mine_dyn)
    RelativeLayout rlMineDyn;
    @InjectView(R.id.tv_mine3)
    ImageView tvMine3;
    @InjectView(R.id.rl_mine_earnings)
    RelativeLayout rlMineEarnings;
    @InjectView(R.id.tv_mine4)
    ImageView tvMine4;
    @InjectView(R.id.rl_mine_service)
    RelativeLayout rlMineService;
    @InjectView(R.id.tv_mine5)
    ImageView tvMine5;
    @InjectView(R.id.rl_mine_share)
    RelativeLayout rlMineShare;
    @InjectView(R.id.scrollView)
    PersonalScrollView scrollView;
    @InjectView(R.id.tv_focus_num)
    TextView tvFocusNum;
    @InjectView(R.id.iv_mine_level)
    ImageView ivMineLevel;
    private String qq = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.inject(this, view);
        initView();
        return view;
    }

    public void initView() {
        scrollView.setImageView(ivBagpic);
        SPUserUtils config = SPUserUtils.sharedInstance();
//        Picasso.with(getActivity())
//                .load(config.getHeadpic())
//                .resize(100, 100).centerCrop()
//                .into(ivMineHead);

        head(config.getHeadpic());

        tvMineName.setText(config.getNickname());
        tvFocusNum.setText(config.getFocus());
        tvMineGolds.setText(config.getWallet());
        ivMineLevel.setBackgroundResource(levetype(Integer.valueOf(config.getLevel())));
//        if (config.getLevel().equalsIgnoreCase("0"))
//            ivMineLevel.setVisibility(View.VISIBLE);
//        else ivMineLevel.setVisibility(View.GONE);

        getData();
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
                            UsersBean.UserBean userbran = bean.getUser();
                            SPUserUtils config = SPUserUtils.sharedInstance();

                            config.setUid(userbran.getUid());
                            config.setNickname(userbran.getNickname());
                            config.setHeadpic(userbran.getHeadpic());
                            config.setSex(userbran.getSex());
                            config.setLevel(userbran.getLevel());
                            config.setWallet(userbran.getWallet());
                            config.setFocus(userbran.getFocus());

                            config.savePreferences();

//                            Picasso.with(getActivity())
//                                    .load(userbran.getHeadpic())
//                                    .resize(100, 100).centerCrop()
//                                    .error(R.drawable.icon_kongbai)
//                                    .into(ivMineHead);
                            head(userbran.getHeadpic());
                            tvMineName.setText(userbran.getNickname());
                            tvFocusNum.setText(userbran.getFocus());
                            tvMineGolds.setText(userbran.getWallet());
                            ivMineLevel.setBackgroundResource(levetype(Integer.valueOf(userbran.getLevel())));
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


    private void head(String headString) {
        if (headString.toLowerCase().endsWith(".gif")) {
            Glide.with(getActivity())
                    .load(headString)
                    .asGif()
                    .override(80, 80)
                    .placeholder(R.color.white)
                    .error(R.color.white)
                    .dontAnimate()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(ivMineHead);
        } else {
//            Glide.with(getActivity())
//                    .load(headString)
//                    .placeholder(R.color.white)
//                    .transform(new GlideCircleTransform(getActivity()))
//                    .into(ivMineHead);
            Picasso.with(getActivity())
                    .load(headString)
                    .resize(100, 100).centerCrop()
                    .error(R.drawable.icon_kongbai)
                    .into(ivMineHead);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.iv_mine_head, R.id.rl_mine_acc, R.id.rl_mine_dyn, R.id.rl_mine_earnings, R.id.rl_mine_service, R.id.rl_mine_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_mine_head:
                startActivityForResult(new Intent(getActivity(), MyDataActivity.class), Constants.SET_DATA_CUT);
                break;
            case R.id.rl_mine_acc:
                startActivityForResult(new Intent(getActivity(), MyAccActivity.class), Constants.SET_DATA_ACC);
                break;
            case R.id.rl_mine_dyn:
                startActivity(new Intent(getActivity(), DynActivity.class));
                break;
            case R.id.rl_mine_earnings:
                startActivity(new Intent(getActivity(), AnchorMoneyActivity.class));
                break;
            case R.id.rl_mine_service:
                getCore();
//                final String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=%s&version=1";
//                try {
//                    final String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=%s&version=1&src_type=web&web_src=snsbao.com";
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(qqUrl, (String) SPUtils.get("User_qq", "")))));
//                } catch (Exception e) {
//                    toast("请先安装QQ");
//                }
                break;
            case R.id.rl_mine_share:
                startActivity(new Intent(getActivity(), ShareActivity.class));
                break;
        }
    }


    /**
     * @param requestCode 跳转依据
     * @param resultCode  回传的标记，我在B中回传的是RESULT_OK
     * @param data        回传的Intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.SET_DATA_CUT) {
            if (resultCode == RESULT_OK) {
                getData();
            }
        }
        if (requestCode == Constants.SET_DATA_ACC) {
            if (resultCode == RESULT_OK) {
                getData();
            }
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
                        try {
                            qq = coreBean.getCon().getQq();
                            jumpToQQ(qq);
                        } catch (Exception e) {
                            jumpToQQ("");
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.e("s", e.toString());
                        qq = (String) SPUtils.get("User_qq", "");
                        jumpToQQ(qq);
                        super.onError(call, response, e);
                    }
                });

    }


    private void jumpToQQ(String qq) {
        try {
            if (!StringUtil.isNullOrEmpty(qq)) {
                final String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=%s&version=1&src_type=web&web_src=snsbao.com ";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(qqUrl, qq))));
            } else {
                final String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=%s&version=1";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(qqUrl, (String) SPUtils.get("User_qq", "")))));
            }
        } catch (Exception e) {
            toast("请先安装QQ");
        }
    }


}
