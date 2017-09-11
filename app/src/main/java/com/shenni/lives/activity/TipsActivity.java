package com.shenni.lives.activity;

import android.os.Bundle;

import com.shenni.lives.R;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.ButterKnife;

public class TipsActivity extends AutoLayoutActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TipsActivity.this.setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_open);
        ButterKnife.inject(this);
        InitView();
    }

    public void InitView() {

    }
}
