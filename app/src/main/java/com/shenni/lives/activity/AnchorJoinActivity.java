package com.shenni.lives.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shenni.lives.R;
import com.shenni.lives.api.Api;
import com.shenni.lives.base.BaseActivity;
import com.shenni.lives.utils.ImageUtils;
import com.shenni.lives.utils.SPUtils;
import com.shenni.lives.utils.StringUtil;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 申请直播页面
 */
public class AnchorJoinActivity extends BaseActivity {


    @InjectView(R.id.iv_update)
    Button ivUpdate;
    @InjectView(R.id.anjoin_text)
    TextView anjoin_text;
    @InjectView(R.id.iv_ac_up_head)
    ImageView ivAcUpHead;
    @InjectView(R.id.ll_aj_next)
    RelativeLayout llAjNext;
    @InjectView(R.id.ed_anj_qq)
    EditText edAnjQq;
    @InjectView(R.id.ed_anj_phone)
    EditText edAnjPhone;
    @InjectView(R.id.ed_anj_jingyan)
    EditText edAnjJingyan;
    @InjectView(R.id.ed_anj_shiduan)
    EditText edAnjShiduan;
    @InjectView(R.id.btn_aj_join)
    Button btnAjJoin;
    @InjectView(R.id.ben_aj_close)
    Button benAjClose;
    @InjectView(R.id.ll_aj_input)
    LinearLayout ll_aj_input;
    private String mCurrentPhotoPath = "";
    private String PHOTO_FILE_NAME = null;

    private String contact, phone, history, worktime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        ButterKnife.inject(this);
        if (((String) SPUtils.get(config.getUid() + "申请", "")).equalsIgnoreCase("申请主播")) {
            llAjNext.setVisibility(View.VISIBLE);
            ll_aj_input.setVisibility(View.GONE);
        } else {
            ll_aj_input.setVisibility(View.VISIBLE);
            llAjNext.setVisibility(View.GONE);
        }
        InitView();
    }


    public void InitView() {
        setLeftImgTitleBar("主播申请", R.drawable.icon_close);
    }


    @OnClick(R.id.iv_update)
    public void onViewClicked() {
        Intent intent = new Intent(this, PhotoGraphActivity.class);
//        intent.putExtra("iscrop","no");
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            PHOTO_FILE_NAME = data.getStringExtra("PHOTO_FILE_NAME");
            mCurrentPhotoPath = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME).getPath();
            Bitmap mBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
            if (mBitmap == null) {
                mBitmap = ImageUtils.getScaledBitmap(mCurrentPhotoPath);
//                Picasso.with(AnchorJoinActivity.this)
//                        .load(mCurrentPhotoPath)
//                        .resize(100, 100).centerCrop()
//                        .into(ivAcUpHead);
            } else
                ivAcUpHead.setImageBitmap(mBitmap);
            anjoin_text.setVisibility(View.INVISIBLE);
//            getUserInformation();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.btn_aj_join)
    public void onJoinClicked() {
        contact = edAnjQq.getText().toString().trim();
        phone = edAnjPhone.getText().toString().trim();
        history = edAnjJingyan.getText().toString().trim();
        worktime = edAnjShiduan.getText().toString().trim();


        if (StringUtil.isNullOrEmpty(PHOTO_FILE_NAME)) {
            toast("请录入照片");
            return;
        }
        if (StringUtil.isNullOrEmpty(contact)) {
            toast("QQ|微信号不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(phone)) {
            toast("手机号不能为空");
            return;
        }
        if (!StringUtil.isPhone(phone)) {
            toast("手机号格式错误");
            return;
        }
        if (StringUtil.isNullOrEmpty(history)) {
            toast("有无直播经验不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(worktime)) {
            toast("直播时段不能为空");
            return;
        }

        joinAnchor();

    }

    @OnClick(R.id.ben_aj_close)
    public void onCloseClicked() {
        finish();
    }


    /**
     * 申请主播
     */
    private void joinAnchor() {
        OkGo.post(Api.GET_USE_APPLY)
                .tag(this)
                .params("uid", uid)
                .params("pic", new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME))
                .params("contact", contact)
                .params("phone", phone)
                .params("history", history)
                .params("worktime", worktime)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("申请主播：", s);

                        llAjNext.setVisibility(View.VISIBLE);

                        SPUtils.put(config.getUid() + "申请", "申请主播");
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.e("s", e.toString());
                        super.onError(call, response, e);
                    }

                });
    }
}


