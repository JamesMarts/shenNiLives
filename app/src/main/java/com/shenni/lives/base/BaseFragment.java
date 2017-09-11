package com.shenni.lives.base;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shenni.lives.MyApplication;
import com.shenni.lives.utils.SPUserUtils;
import com.shenni.lives.utils.StringUtil;
import com.shenni.lives.utils.ToastUtil;

/**
 * Fragment 基类. 设置Fragment模式为懒加载模式
 */
public abstract class BaseFragment extends Fragment {

    protected View mView;
    //   界面是否显示
    protected boolean isVisiable;
    //布局是否加载完成
    protected boolean isPrepared;

    private Context mContext;


    /**
     * 页数
     */
    public int page = 1;
    /**
     * 显示的数据条数
     */
    public int num = 6;
    public String uid = "";

//    private Unbinder unbinder;

    public BaseFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        createPresenter();
        if (StringUtil.isNullOrEmpty(uid)) {
            SPUserUtils config = SPUserUtils.sharedInstance();
            uid = config.getUid();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mView = inflater.inflate(setLayoutResID(), container, false);
        mContext = getActivity();
//        Logger.init().hideThreadInfo().setMethodCount(3).setLogLevel(LogLevel.FULL);
//        unbinder=ButterKnife.bind(this, mView);
//        ButterKnife.inject(this,mView);
        isPrepared = true;
        isVisiable = true;
        return mView;
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {//在ViewPager这种预加载的控件中使用 Fragment的懒加载模式
//            //isVisiable = true;
//            onVisiable();
//        } else {
//            //isVisiable = false;
//            onInvisiable();
//        }
//    }


//
//    /**
//     * 加载布局文件
//     *
//     * @return
//     */
//    public abstract int setLayoutResID();


//    /**
//     * 初始化布局
//     */
//    public abstract void InitView();


//    /**
//     * 创建传递器
//     */
//    public abstract void createPresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void toast(String msg) {
        try {
            ToastUtil.shortToast(MyApplication.getContext(), msg);
        } catch (Exception e) {
        }
    }

    public void toast(int resId) {
        try {
            ToastUtil.shortToast(MyApplication.getContext(), resId);
        } catch (Exception e) {
        }
    }


}
