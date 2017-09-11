package com.shenni.lives.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 同城页面
 */
public class CityFragment extends FocusFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sid = "3";
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
