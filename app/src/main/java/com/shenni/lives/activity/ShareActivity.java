package com.shenni.lives.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.shenni.lives.R;
import com.shenni.lives.base.BaseActivity;
import com.shenni.lives.fragment.ShareFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 分享
 */
public class ShareActivity extends BaseActivity {


    private int add = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.inject(this);
        getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);

        add = getIntent().getIntExtra("add", 0);

        InitView();


    }

    public void InitView() {
        noActionBar();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        ShareFragment mFragment = new ShareFragment();
        if (add == 1) {
            Bundle bundle = new Bundle();
            bundle.putInt("add", add);
            mFragment.setArguments(bundle);
        }

        transaction.replace(R.id.share_fragment, mFragment).commit();

    }

    @OnClick(R.id.share_view)
    public void onViewClicked() {
        finish();
    }
}
