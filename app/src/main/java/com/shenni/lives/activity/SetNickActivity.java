package com.shenni.lives.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.shenni.lives.R;
import com.shenni.lives.base.BaseActivity;
import com.shenni.lives.utils.StringUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SetNickActivity extends BaseActivity {

    @InjectView(R.id.et_setnick)
    EditText etSetnick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_nick);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        setRightTxtTitleBar("设置昵称", "保存");
    }


    @Override
    public void rightAction() {
        super.rightAction();
        if (StringUtil.isNullOrEmpty(etSetnick.getText().toString())) {
            toast("昵称不能为空");
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("setNick",etSetnick.getText().toString().trim());
        setResult(RESULT_OK, intent);
        finish();

    }
}
