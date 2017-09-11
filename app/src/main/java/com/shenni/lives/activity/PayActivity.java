package com.shenni.lives.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.shenni.lives.R;
import com.shenni.lives.base.BaseActivity;
import com.shenni.lives.widget.MyRadioButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 充值页面
 */
public class PayActivity extends BaseActivity {


    @InjectView(R.id.iv_chongzhi_close)
    ImageView ivChongzhiClose;
    @InjectView(R.id.rv_pay_ok)
    ImageView rvPayOk;
    @InjectView(R.id.rg_bottom)
    RadioGroup rgBottom;
    @InjectView(R.id.rg_pay_weixin)
    MyRadioButton rgPayWeixin;
    @InjectView(R.id.rb_pay_zhifubao)
    MyRadioButton rbPayZhifubao;
    @InjectView(R.id.rg_pay_select)
    RadioGroup rgPaySelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chongzhi);
        ButterKnife.inject(this);
        InitView();
    }

    public void InitView() {
        noActionBar();
    }

    @OnClick({R.id.rv_pay_ok, R.id.iv_chongzhi_close, R.id.rg_pay_weixin, R.id.rb_pay_zhifubao})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rv_pay_ok:
                rgBottom.setVisibility(View.GONE);
                rgPaySelect.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_chongzhi_close:
                break;
            case R.id.rg_pay_weixin:
                break;
            case R.id.rb_pay_zhifubao:
                break;
        }
    }
}
