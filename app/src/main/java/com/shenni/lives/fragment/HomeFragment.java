package com.shenni.lives.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shenni.lives.R;
import com.shenni.lives.activity.LoginActivity;
import com.shenni.lives.activity.MessageActivity;
import com.shenni.lives.activity.SearchActivity;
import com.shenni.lives.adapter.ViewPagerAdapter;
import com.shenni.lives.api.Api;
import com.shenni.lives.base.BaseFragment;
import com.shenni.lives.bean.MessageBean;
import com.shenni.lives.bean.PagerInfo;
import com.shenni.lives.utils.Constants;
import com.shenni.lives.utils.GsonUtil;
import com.shenni.lives.utils.SPUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static com.shenni.lives.utils.Constants.REFRESH_FOCUS;
import static com.shenni.lives.utils.Constants.SET_DATA_MSS;


/**
 * 直播列表页面
 */
public class HomeFragment extends BaseFragment {


    @InjectView(R.id.tab_title)
    TabLayout tabLayout;
    @InjectView(R.id.vPager)
    ViewPager viewPager;

    @InjectView(R.id.iv_home_message)
    ImageView iv_home_message;

    private ViewPagerAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.inject(this, view);
        InitView();
        if ((int) SPUtils.get("未读消息", 999) > 0) {
            iv_home_message.setBackgroundResource(R.drawable.icon_message2);

            getData();
        } else {
            iv_home_message.setBackgroundResource(R.drawable.icon_message);
        }
        return view;
    }


    public void InitView() {
        List<PagerInfo> pagerList = new ArrayList<PagerInfo>();

        PagerInfo A = new PagerInfo(new FocusFragment(), R.string.tab1);
        PagerInfo B = new PagerInfo(new HotFragment(), R.string.tab2);
//        PagerInfo B = new PagerInfo(new Hot2Fragment(), R.string.tab2);
        PagerInfo C = new PagerInfo(new CityFragment(), R.string.tab3);

        pagerList.add(A);
        pagerList.add(B);
        pagerList.add(C);

        mAdapter = new ViewPagerAdapter(getChildFragmentManager(), pagerList);
        for (int i = 0; i < pagerList.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(mAdapter.getPageTitle(i)));
        }
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);


//        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.getTabAt(1).select();


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        //Activity销毁时，取消网络请求
        OkGo.getInstance().cancelTag(this);
    }

    @OnClick(R.id.iv_search)
    public void onViewClicked() {
        startActivityForResult(new Intent(getActivity(), SearchActivity.class), REFRESH_FOCUS);
    }

    @OnClick(R.id.iv_home_message)
    public void onViewClicked2() {
        startActivityForResult(new Intent(getActivity(), MessageActivity.class), SET_DATA_MSS);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REFRESH_FOCUS) {
            if (resultCode == RESULT_OK) {
                mAdapter.refreshUi(0);
            }
        }
        if (requestCode == SET_DATA_MSS) {
            if (resultCode == RESULT_OK) {
                if ((int) SPUtils.get("未读消息", 999) > 0) {
                    iv_home_message.setBackgroundResource(R.drawable.icon_message2);
                } else {
                    iv_home_message.setBackgroundResource(R.drawable.icon_message);
                }
            }
        }
    }


    /**
     * 消息列表
     */
    private void getData() {
        OkGo.get(Api.GET_USER_MSGLIST)
                .tag(this)
                .params("uid", uid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("消息列表：", s);
                        MessageBean messageBean = GsonUtil.parseJsonWithGson(s, MessageBean.class);
                        if (null != messageBean && 1 == messageBean.getStatus())
                            try {
                                if (messageBean.getList().size() > 0) {
                                    SPUtils.put("未读消息", messageBean.getList().size());
                                    if (null != iv_home_message)
                                        iv_home_message.setBackgroundResource(R.drawable.icon_message2);
                                } else {
                                    if (null != iv_home_message)
                                        iv_home_message.setBackgroundResource(R.drawable.icon_message);

                                }
                            } catch (Exception e) {
                            }
                        else {

                            if (null != messageBean && messageBean.getInfo().equalsIgnoreCase("用户ID有误")) {
//                                SPUserUtils.sharedInstance().resetPreferences();
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                getActivity().finish();
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.e("s", e.toString());
                        iv_home_message.setBackgroundResource(R.drawable.icon_message);

                        super.onError(call, response, e);
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            tabLayout.post(new Runnable() {
                @Override
                public void run() {
                    setIndicator(tabLayout, 20, 20);
                }
            });
        } catch (Exception e) {
        }
    }

    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        if (null != tabs) {
            Class<?> tabLayout = tabs.getClass();
            Field tabStrip = null;
            try {
                tabStrip = tabLayout.getDeclaredField("mTabStrip");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            tabStrip.setAccessible(true);
            LinearLayout llTab = null;
            try {
                llTab = (LinearLayout) tabStrip.get(tabs);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
            int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

            for (int i = 0; i < llTab.getChildCount(); i++) {
                View child = llTab.getChildAt(i);
                child.setPadding(0, 0, 0, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                params.leftMargin = left;
                params.rightMargin = right;
                child.setLayoutParams(params);
                child.invalidate();
            }
        }
    }
}
