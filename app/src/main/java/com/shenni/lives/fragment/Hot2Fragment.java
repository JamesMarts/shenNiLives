package com.shenni.lives.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shenni.lives.R;
import com.shenni.lives.adapter.Hot2Adapter;
import com.shenni.lives.base.BaseFragment;
import com.shenni.lives.bean.FoucsBean;
import com.shenni.lives.jjdxmplayer.PlayerLiveActivity;
import com.shenni.lives.utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 关注页面 暂时不用  转到
 */
public class Hot2Fragment extends BaseFragment {
    @InjectView(R.id.recycleview)
    RecyclerView recycleview;
    private Banner banner;
    private List<FoucsBean> mData;

    private Hot2Adapter mAdapter;
    int mbackground[] = {R.drawable.live_girl01,
            R.drawable.live_avatar_girl01,
            R.drawable.live_avatar_girl02,};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow, container, false);

//        View headview = getActivity().getLayoutInflater().inflate(R.layout.headview_banner, null);
//        banner = (Banner) headview.findViewById(R.id.banners);
        banner = (Banner) view.findViewById(R.id.banner);

        ButterKnife.inject(this, view);
        InitData();
        InitView();
        return view;
    }

    private void InitData() {
        mData = new ArrayList<FoucsBean>();
        for (int i = 500; i < 506; i++) {
            int type = (int) ((Math.random() * 300) + 1);
            int type2 = (int) ((Math.random() * 3) + 1);

            FoucsBean bean = new FoucsBean();
            bean.setNum("" + type);
            bean.setCover(mbackground[(type2 + 1) % 3]);
            mData.add(bean);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        GridLayoutManager layoutManager=new GridLayoutManager(getActivity(), 2);//这里用线性宫格显示 类似于grid view
//设置布局管理器
        recycleview.setLayoutManager(layoutManager);
//设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mAdapter = new Hot2Adapter(getContext(), mData);
        recycleview.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new Hot2Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(getActivity(), PlayerLiveActivity.class));
//                toast("test");
            }
        });
    }

    public void InitView() {
        List<Integer> images = new ArrayList<>();
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.CubeOut);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles("当banner样式有显示title时");
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

        //点击事件
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
//                toast("你点击了："+position);
//                startActivity(new Intent(getActivity(), TipsActivity.class));
            }
        });
    }


    //如果你需要考虑更好的体验，可以这么操作
    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
