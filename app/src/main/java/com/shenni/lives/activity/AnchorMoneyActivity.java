package com.shenni.lives.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shenni.lives.MyApplication;
import com.shenni.lives.R;
import com.shenni.lives.base.BaseActivity;
import com.shenni.lives.utils.Constants;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 主播收益
 */
public class AnchorMoneyActivity extends BaseActivity {

    @InjectView(R.id.tv_an_m_msg)
    TextView tvAnMMsg;

    int mx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earnings);
        ButterKnife.inject(this);
        InitView();
        tvAnMMsg.setText(
                "版本号:" + MyApplication.getVersionCode() + "--版本名:" + MyApplication.getVersionName()
                        + "\n" + MyApplication.getAppName()
                        + "\n" + "渠道：" + Constants.CHANNEL_APP_ID);
    }


    public void InitView() {
        setTitleBar("主播收益");

    }

    @OnClick(R.id.an_m_cl)
    public void onViewClicked() {
        if (mx == 5)
            tvAnMMsg.setVisibility(View.VISIBLE);
        else
            tvAnMMsg.setVisibility(View.GONE);
        mx++;
    }
}
