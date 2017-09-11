package com.shenni.lives.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shenni.lives.R;
import com.shenni.lives.adapter.ShareViewAdapter;
import com.shenni.lives.base.BaseFragment;
import com.shenni.lives.bean.ShareBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 分享
 */
public class ShareFragment extends BaseFragment {

    @InjectView(R.id.share_recycleview)
    RecyclerView shareRecycleview;
    private ShareViewAdapter mAdapter;

    private List<ShareBean> mData;
    private int add = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        ButterKnife.inject(this, view);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            add = bundle.getInt("add", 0);
        }


        initView();
        return view;
    }

    private void initView() {

        mData = new ArrayList<ShareBean>();

        ShareBean qq = new ShareBean("QQ好友", R.drawable.icon_share_qq);
        ShareBean snia = new ShareBean("新浪微博", R.drawable.icon_share_snia);
        ShareBean weichatzone = new ShareBean("朋友圈", R.drawable.icon_share_weochat_zone);
        ShareBean qqzone = new ShareBean("QQ空间", R.drawable.icon_share_qq_zone);
        ShareBean weichat = new ShareBean("微信好友", R.drawable.icon_share_weichat);

        mData.add(qq);
        mData.add(snia);
        mData.add(weichatzone);
        mData.add(qqzone);
        mData.add(weichat);

        if (add == 1) {
            ShareBean jubao = new ShareBean("投诉举报", R.drawable.icon_share_tousu);
            mData.add(jubao);
        }

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);//这里用线性宫格显示 类似于grid view
//设置布局管理器
        shareRecycleview.setLayoutManager(layoutManager);
//设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mAdapter = new ShareViewAdapter(getContext(), mData);
        shareRecycleview.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ShareViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                toast(mData.get(position).getText());
//                toast(mData.get(position).getText());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
