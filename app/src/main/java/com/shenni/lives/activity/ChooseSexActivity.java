package com.shenni.lives.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.shenni.lives.R;
import com.shenni.lives.base.BaseActivity;
import com.shenni.lives.widget.MyRadioButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ChooseSexActivity extends BaseActivity {

    @InjectView(R.id.v_view)
    View vView;
    @InjectView(R.id.iv_choose_ok)
    ImageView ivChooseOk;
    @InjectView(R.id.iv_choose_clean)
    ImageView ivChooseClean;
    @InjectView(R.id.rb_girl)
    MyRadioButton rbGirl;
    @InjectView(R.id.rb_boy)
    MyRadioButton rbBoy;
    @InjectView(R.id.rg_bottom)
    RadioGroup rgBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ChooseSexActivity.this.setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_choose_sex);
        ButterKnife.inject(this);
        getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        InitView();
    }

    private void InitView() {
        noActionBar();

        if (config.getSex().equalsIgnoreCase("1")) {
            ((RadioButton) rgBottom.findViewById(rgBottom.getChildAt(1).getId())).setChecked(true);
        } else {
            ((RadioButton) rgBottom.findViewById(rgBottom.getChildAt(0).getId())).setChecked(true);
        }
    }

    @OnClick({R.id.v_view, R.id.iv_choose_ok, R.id.iv_choose_clean, R.id.rb_girl, R.id.rb_boy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.v_view:
                break;
            case R.id.iv_choose_ok:
                Intent intent = new Intent();
                String boy = rbBoy.isChecked() ? "1" : "0";
                intent.putExtra("setSex", boy);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.iv_choose_clean:
                finish();
                break;
            case R.id.rb_girl:
                break;
            case R.id.rb_boy:
                break;
        }
    }
}
