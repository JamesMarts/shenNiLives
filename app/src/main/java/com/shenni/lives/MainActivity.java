package com.shenni.lives;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.shenni.lives.activity.AnchorJoinActivity;
import com.shenni.lives.base.BaseActivity;
import com.shenni.lives.bean.CoreBean;
import com.shenni.lives.fragment.HomeFragment;
import com.shenni.lives.fragment.MineFragment;
import com.shenni.lives.utils.GoldPay;
import com.shenni.lives.utils.SPUserUtils;
import com.shenni.lives.widget.KeyRadioGroupV1;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @InjectView(R.id.rg_bottom)
    KeyRadioGroupV1 rgBottom;
    @InjectView(R.id.tv_statutitle)
    TextView tvStatutitle;
    private long exitTime;


    CoreBean coreBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        InitView();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        GoldPay.paySuccessAfter(MainActivity.this, config.getUid());
    }





    public void InitView() {
        noActionBar();
        SPUserUtils configs= SPUserUtils.sharedInstance();
        configs.setBoolean(true);
        configs.savePreferences();
        rgBottom.setOnCheckedChangeListener(new KeyRadioGroupV1.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(KeyRadioGroupV1 group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        tvStatutitle.setVisibility(View.VISIBLE);
                        switchFragment(new HomeFragment(), null);
                        break;
                    case R.id.rb_minne:
                        tvStatutitle.setVisibility(View.GONE);
                        switchFragment(new MineFragment(), null);
                        break;
                }
            }

        });

        switchFragment(new HomeFragment(), null);
    }

    @OnClick(R.id.btn_entry)
    void mEnttryClick() {
        startActivity(new Intent(MainActivity.this, AnchorJoinActivity.class));

    }

    public void switchFragment(Fragment fragment, Bundle args) {
        try {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.fl_body, fragment);
            ft.commit();
        } catch (Exception exceptione) {
        }
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
