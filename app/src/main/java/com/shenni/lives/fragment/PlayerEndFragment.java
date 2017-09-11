package com.shenni.lives.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shenni.lives.R;
import com.shenni.lives.activity.MyAccActivity;
import com.shenni.lives.base.BaseFragment;
import com.shenni.lives.utils.SPUserUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class PlayerEndFragment extends BaseFragment {


    @InjectView(R.id.tv_chat_end_time)
    TextView tvChatEndTime;
    @InjectView(R.id.tv_chat_end_renshu)
    TextView tvChatEndRenshu;
    @InjectView(R.id.tv_end_title)
    TextView tv_end_title;
    @InjectView(R.id.btn_chat_end_focus)
    Button btnChatEndFocus;
    @InjectView(R.id.ll_end_center)
    LinearLayout ll_end_center;


    @InjectView(R.id.tv_end_daojishi)
    Button tv_end_daojishi;


    private int chargeDaojiShi = 90;
    protected SPUserUtils config;
    Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_player_end, container, false);
        ButterKnife.inject(this, view);
        config = SPUserUtils.sharedInstance();


        ll_end_center.setVisibility(View.INVISIBLE);
        tv_end_title.setText(getString(R.string.live_end_tips1));
        btnChatEndFocus.setText(getString(R.string.live_op_op));
        startHandler();
        return view;
    }


    @OnClick({R.id.btn_chat_end_focus, R.id.btn_chat_end_close, R.id.tv_end_daojishi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_chat_end_focus:
                startActivity(new Intent(getActivity(), MyAccActivity.class));
                getActivity().finish();
                break;
            case R.id.btn_chat_end_close:
                getActivity().finish();
                break;
            case R.id.tv_end_daojishi:
                getActivity().finish();
                break;
        }
    }


    private void startHandler() {
        handler.post(runnable);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (config.getLevel().equalsIgnoreCase("0")) {
                try {
                    tv_end_daojishi.setText("退出倒计时"+chargeDaojiShi + "秒");
                    chargeDaojiShi--;
                    if (chargeDaojiShi == 0) {
                        getActivity().finish();
                    }
                } catch (Exception e) {
                }
            }
            handler.postDelayed(runnable, 1000);
        }
    };





    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
