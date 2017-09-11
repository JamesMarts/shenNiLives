package com.shenni.lives.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shenni.lives.R;
import com.shenni.lives.api.Api;
import com.shenni.lives.base.BaseActivity;
import com.shenni.lives.bean.LoginBean;
import com.shenni.lives.utils.GsonUtil;
import com.shenni.lives.utils.SPUserUtils;
import com.shenni.lives.utils.StringUtil;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

import static com.shenni.lives.utils.Constants.SET_NICK_CUT;
import static com.shenni.lives.utils.Constants.SET_SEX_CUT;

/**
 * 我的资料
 */
public class MyDataActivity extends BaseActivity {


    @InjectView(R.id.iv_data_head)
    CircleImageView ivDataHead;
    @InjectView(R.id.tv_data_name)
    TextView tvDataName;
    @InjectView(R.id.tv_data_gold)
    TextView tvDataGold;
    @InjectView(R.id.tv_data_golds)
    TextView tvDataGolds;
    @InjectView(R.id.iv_mynick)
    ImageView ivMynick;
    @InjectView(R.id.rl_mick)
    RelativeLayout rlMick;
    @InjectView(R.id.iv_sex)
    ImageView ivSex;
    @InjectView(R.id.rl_sex)
    RelativeLayout rlSex;

    @InjectView(R.id.my_iv_level)
    ImageView ivLevel;

    private SPUserUtils config;
    private String PHOTO_FILE_NAME = null;

    private boolean refersh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data);
        ButterKnife.inject(this);
        config = SPUserUtils.sharedInstance();
        InitView();
    }

    public void InitView() {
        setTitleBar("我的资料");
        SPUserUtils config = SPUserUtils.sharedInstance();
        Picasso.with(MyDataActivity.this)
                .load(config.getHeadpic())
//                .placeholder(R.drawable.icon_load)
                .resize(100, 100).centerCrop()
                .error(R.drawable.icon_kongbai)
                .into(ivDataHead);
        tvDataName.setText(config.getNickname());
        tvDataGolds.setText(config.getUid());
        ivLevel.setBackgroundResource(levetype(Integer.valueOf(config.getLevel())));

    }

    @OnClick({R.id.rl_mick, R.id.rl_sex})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_mick:
                startActivityForResult(new Intent(MyDataActivity.this, SetNickActivity.class), SET_NICK_CUT);
                break;
            case R.id.rl_sex:
                startActivityForResult(new Intent(MyDataActivity.this, ChooseSexActivity.class), SET_SEX_CUT);
                break;
        }
    }


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
     * 修改用户信息
     */
    public void editUserInformation(String nick, String sex) {
        OkGo.get(Api.EDIT_USER_INFORMATION)
                .tag(this)
                .params("uid", uid)
                .params("nickname", StringUtil.isNullOrEmpty(nick) ? config.getNickname() : nick)
                .params("sex", StringUtil.isNullOrEmpty(sex) ? config.getSex() : sex)
//                .params("headpic",etMyapliay.getText().toString().trim())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("修改用户信息", s);
                        LoginBean bean = GsonUtil.parseJsonWithGson(s, LoginBean.class);
                        if (null != bean && 1 == bean.getStatus()) {
                            if (bean.getStatus() == 1) {
                                LoginBean.UserBean ubean = bean.getUser();
                                config = SPUserUtils.sharedInstance();
                                config.setNickname(ubean.getNickname());
                                config.savePreferences();

                                //设置结果显示框的显示数值
                                tvDataName.setText(ubean.getNickname());
                                refersh = true;
                            } else if (bean.getStatus() == 0) {
                                refersh = false;
                                toast(bean.getInfo());
                            }

                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.e("s", e.toString());
                        refersh = false;
                        super.onError(call, response, e);
                    }
                });
    }


    @OnClick(R.id.iv_data_head)
    protected void headClick() {
        Intent intent = new Intent(this, PhotoGraphActivity.class);
        startActivityForResult(intent, 1);

    }


    /**
     * @param requestCode 跳转依据
     * @param resultCode  回传的标记，我在B中回传的是RESULT_OK
     * @param data        回传的Intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SET_NICK_CUT) {
            if (resultCode == RESULT_OK)
                editUserInformation(data.getStringExtra("setNick"), null);
        }
        if (requestCode == SET_SEX_CUT) {
            if (resultCode == RESULT_OK)
                editUserInformation(null, data.getStringExtra("setSex"));
        }

        if (requestCode == 1 && resultCode == RESULT_OK) {
            refersh = true;
            if (!StringUtil.isNullOrEmpty(PHOTO_FILE_NAME = data.getStringExtra("PHOTO_FILE_NAME")))
                uploadHead();
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

    //上传头像,获取链接
    public void uploadHead() {
        OkGo.post(Api.EDIT_USER_INFORMATION)
                .tag(this)
                .params("uid", SPUserUtils.sharedInstance().getUid())
                .params("headpic", new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("photo", s);
                        LoginBean bean = GsonUtil.parseJsonWithGson(s, LoginBean.class);
                        if (null != bean && 1 == bean.getStatus()) {
                            if (bean.getStatus() == 1) {
                                LoginBean.UserBean ubean = bean.getUser();

                                config.setUid(ubean.getUid());
                                config.setNickname(ubean.getNickname());
                                config.setHeadpic(ubean.getHeadpic());
                                config.setSex(ubean.getSex());
                                config.setLevel(ubean.getLevel());
                                config.setWallet(ubean.getWallet());
                                config.setStatus(ubean.getStatus());
                                config.setFocus(ubean.getFocus());

                                config.savePreferences();
                                InitView();
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.e("photo", e.toString());
                        super.onError(call, response, e);
                    }
                });
    }

}
