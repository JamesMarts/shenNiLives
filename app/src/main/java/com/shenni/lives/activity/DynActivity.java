package com.shenni.lives.activity;

import android.app.Notification;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.shenni.lives.R;
import com.shenni.lives.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.shenfan.updateapp.UpdateService;


/**
 * 我的动态
 */
public class DynActivity extends BaseActivity {


    @InjectView(R.id.my_dyn)
    ImageView myDyn;
    private static final String URL = "http://27.221.81.15/dd.myapp.com/16891/63C4DA61823B87026BBC8C22BBBE212F.apk?mkey=575e443c53406290&f=8b5d&c=0&fsname=com.daimajia.gold_3.2.0_80.apk&p=.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dyn);
        ButterKnife.inject(this);
        InitView();
        myDyn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                updateFlag(view);
//                upApk();
            }
        });
    }

    public void InitView() {
        setTitleBar("我的动态");
    }


    public void update(View view) {
        UpdateService.Builder.create(URL).build(this);
    }

    public void updateFlag(View view) {
        UpdateService.Builder.create(URL)
                .setStoreDir("update/flag")
                .setUpdateProgress(1)
                .setDownloadSuccessNotificationFlag(Notification.DEFAULT_ALL)
                .setDownloadErrorNotificationFlag(Notification.DEFAULT_ALL)
                .build(this);
    }

    public void updateStore(View view) {
        UpdateService.Builder.create(URL)
                .setStoreDir("update/store")
                .build(this);
    }


}
