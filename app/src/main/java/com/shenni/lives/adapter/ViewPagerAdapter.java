package com.shenni.lives.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shenni.lives.MyApplication;
import com.shenni.lives.bean.PagerInfo;
import com.shenni.lives.fragment.FocusFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/27.
 * function：
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<PagerInfo> mList;
    private FragmentManager mFragmentManager;
    private ArrayList<Fragment> mFragmentList;

    public ViewPagerAdapter(FragmentManager fm, List<PagerInfo> list) {
        super(fm);
        this.mFragmentManager = fm;
        this.mList = list;
    }

    @Override
    public Fragment getItem(int position) {
        if (position < mList.size()) {
            return mList.get(position).getFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    //分类标题
    @Override
    public CharSequence getPageTitle(int position) {
//        return mList.get(position).getTitle();
        return MyApplication.getInstance().getString(mList.get(position).getTitleResId());
    }


    public void refreshUi(int index) {
        Fragment fragment=mList.get(index).getFragment();
        ((FocusFragment)fragment).refreshUi();
    }
}







