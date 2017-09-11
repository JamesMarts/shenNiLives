package com.shenni.lives.jjdxmplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.shenni.lives.R;
import com.shenni.lives.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class JjdxmMainActivity extends BaseActivity implements View.OnClickListener {
    private long exitTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jjdxmmain);
        ButterKnife.inject(this);
        InitView();
    }

    public void InitView() {

    }

    @OnClick({R.id.btn_h, R.id.btn_v, R.id.btn_live, R.id.btn_origin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_h:
                /**半屏播放器*/
                startActivity(HPlayerActivity.class);
                break;
            case R.id.btn_v:
                /**竖屏播放器*/
                startActivity(PlayerActivity.class);
                break;
            case R.id.btn_live:
                /**竖屏直播播放器*/
                startActivity(PlayerLiveActivity.class);
                break;
            case R.id.btn_origin:
                /**ijkplayer原生的播放器*/
                startActivity(OriginPlayerActivity.class);
                break;
        }
    }

    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(JjdxmMainActivity.this, cls);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            exitTime = System.currentTimeMillis();
            toast("再按一次退出程序");
        } else {
            finishAll();
        }

    }
}
