package com.shenni.lives.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shenni.lives.R;
import com.shenni.lives.activity.LoginActivity;
import com.shenni.lives.activity.OpenActivity;
import com.shenni.lives.activity.PlayerEndActivity;
import com.shenni.lives.activity.WebActivity;
import com.shenni.lives.adapter.HotAdapter;
import com.shenni.lives.api.Api;
import com.shenni.lives.base.BaseFragment;
import com.shenni.lives.bean.AnchorListBean;
import com.shenni.lives.bean.FollowBean;
import com.shenni.lives.bean.FoucsBean;
import com.shenni.lives.jjdxmplayer.PlayerLiveActivity;
import com.shenni.lives.utils.Constants;
import com.shenni.lives.utils.GlideImageLoader;
import com.shenni.lives.utils.GoldPay;
import com.shenni.lives.utils.GsonUtil;
import com.shenni.lives.utils.SPUserUtils;
import com.shenni.lives.utils.SPUtils;
import com.shenni.lives.utils.StringUtil;
import com.shenni.lives.widget.ListViewForScrollView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static com.shenni.lives.utils.Constants.SET_DATA_PBANCHOR;


/**
 * 热门页面
 */
public class HotFragment extends BaseFragment {

    @InjectView(R.id.lv_list)
    ListViewForScrollView mLvlist;
    @InjectView(R.id.trl_refresh_list)
    TwinklingRefreshLayout mRefreshlist;
    @InjectView(R.id.head_banner)
    Banner banner;
    private String sid = "1";

    private HotAdapter mAdapter;
    private AnchorListBean bean;
    private FoucsBean mFoucsBean;

    private List<AnchorListBean.ListBean> mList;
    private List<FollowBean.ListBean> list;


//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        if (isVisibleToUser) {
//            page = 1;
////            getDataList();
//            if (null != mRefreshlist)
//                mRefreshlist.finishRefreshing();
//        }
//        super.setUserVisibleHint(isVisibleToUser);
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        ButterKnife.inject(this, view);
        InitView();
        InitData();
//        getBanner();
//        getDataList();
        GoldPay.paySuccessAfter(getActivity(), SPUserUtils.sharedInstance().getUid());
        return view;
    }

    private void InitData() {
        mList = new ArrayList<>();
        mAdapter = new HotAdapter(getActivity(), mList);
//        View headview = getActivity().getLayoutInflater().inflate(R.layout.headview_banner, null);
//
//        banner = (Banner) headview.findViewById(R.id.head_banner);
        mLvlist.setAdapter(mAdapter);
//        mLvlist.addHeaderView(banner);

        setRefreshLayout();
        mLvlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SPUserUtils config = SPUserUtils.sharedInstance();

                if (mList.get(position).getLevel() == 1 && Integer.valueOf(config.getLevel()) < 1) {
                    toast(String.format("只有%s和%s才可以观看", getString(R.string.live_pay_level1), getString(R.string.live_pay_level2)));
                }
                if (mList.get(position).getLevel() == 2 && Integer.valueOf(config.getLevel()) < 2) {
                    toast(String.format("只有%s才可以观看", getString(R.string.live_pay_level2)));
                }
                if (mList.get(position).getLevel() == 3) {
                    toast("偷窥后可以观看");
                }
                //2直播 1视频
                if (2 == mList.get(position).getVideotype()) {
                    Intent mIntent = new Intent(getActivity(), PlayerLiveActivity.class);
                    mIntent.putExtra("liveAid", mList.get(position).getAid());
                    mIntent.putExtra("source", mList.get(position).getSource());
                    //int
                    mIntent.putExtra("liveLevel", mList.get(position).getLevel());
                    mIntent.putExtra("bigpic", mList.get(position).getBigpic());
                    mIntent.putExtra("freetime", mList.get(position).getFreetime());

                    //2直播 1视频
                    mIntent.putExtra("videotype", mList.get(position).getVideotype());

                    if (1 == mList.get(position).getOnline()) {
                        startActivityForResult(mIntent, SET_DATA_PBANCHOR);
                        return;
                    } else {
                        Intent intent = new Intent(getActivity(), PlayerEndActivity.class);
                        //int
                        intent.putExtra("online", mList.get(position).getOnline());
                        startActivityForResult(intent, SET_DATA_PBANCHOR);
                        return;
                    }
                }

                //播放完毕为1
                int liveend = (int) SPUtils.get(mList.get(position).getAid() + "liveend", -11);
                if (liveend != 1) {
                    if (config.getLevel().equals("0")) {
                        int livedaoqi = (int) SPUtils.get(mList.get(position).getAid() + "livedaoqi", -1001);
                        if (1 == livedaoqi) {
                            Intent intent = new Intent(getActivity(), PlayerEndActivity.class);
                            intent.putExtra("liveAid", mList.get(position).getAid());
                            startActivity(intent);
                            return;
                        }
                    }
                    if (Integer.valueOf(config.getLevel()) >= mList.get(position).getLevel()) {
                        SPUtils.put(mList.get(position).getAid() + "msg", new Gson().toJson(mList.get(position).getCiku()));
                        Intent mIntent = new Intent(getActivity(), PlayerLiveActivity.class);
                        mIntent.putExtra("liveAid", mList.get(position).getAid());
                        mIntent.putExtra("source", mList.get(position).getSource());
                        //int
                        mIntent.putExtra("liveLevel", mList.get(position).getLevel());
                        mIntent.putExtra("bigpic", mList.get(position).getBigpic());
                        mIntent.putExtra("freetime", mList.get(position).getFreetime());
                        //2直播 1视频
                        mIntent.putExtra("videotype", mList.get(position).getVideotype());

                        if (1 == mList.get(position).getVideotype())
                            startActivityForResult(mIntent, SET_DATA_PBANCHOR);
                        else if (1 == mList.get(position).getOnline())
                            startActivityForResult(mIntent, SET_DATA_PBANCHOR);
                        else {
                            Intent intent = new Intent(getActivity(), PlayerEndActivity.class);
                            //int
                            intent.putExtra("online", mList.get(position).getOnline());
                            startActivityForResult(intent, SET_DATA_PBANCHOR);
                        }
                    } else {
                        Intent mIntent = new Intent(getActivity(), OpenActivity.class);
                        SPUtils.put(mList.get(position).getAid() + "msg", new Gson().toJson(mList.get(position).getCiku()));
                        mIntent.putExtra("liveAid", mList.get(position).getAid());
                        mIntent.putExtra("source", mList.get(position).getSource());
                        mIntent.putExtra("bigpic", mList.get(position).getBigpic());
                        mIntent.putExtra("level", mList.get(position).getLevel());
                        mIntent.putExtra("S_money", mList.get(position).getS_money());
                        startActivity(mIntent);
                    }
                } else {
                    Intent intent = new Intent(getActivity(), PlayerEndActivity.class);
                    intent.putExtra("liveAid", mList.get(position).getAid());
                    //int
                    intent.putExtra("liveLevel", mList.get(position).getLevel());
                    startActivityForResult(intent, SET_DATA_PBANCHOR);

                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.SET_DATA_PBANCHOR) {
            if (resultCode == RESULT_OK) {
//                page = 1;
//                getDataList();
//                if (null != mRefreshlist)
//                    mRefreshlist.finishRefreshing();
            }
        }
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
        banner.setBannerAnimation(Transformer.Default);
//        banner.setBannerAnimation(Transformer.CubeOut);
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
                String url = list.get(position).getLink();
                if (StringUtil.isNullOrEmpty(url)) {
//                    toast("暂无活动");
                } else {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }

//                Intent intent = new Intent();
//                //Intent intent = new Intent(Intent.ACTION_VIEW,uri);
//                intent.setAction("android.intent.action.VIEW");
//                Uri content_url = Uri.parse("HTTPS://QR.ALIPAY.COM/FKX07910WZPN4ZZ800EK64");
//                intent.setData(content_url);
//                startActivity(intent);

//                Uri uri = Uri.parse("HTTP://www.baudu.com");
////                Uri uri = Uri.parse("HTTPS://QR.ALIPAY.COM/FKX07910WZPN4ZZ800EK64");
//                Intent it = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(it);

//                Intent intent = new Intent();
//                intent.setAction("android.intent.action.VIEW");
////                Uri content_url = Uri.parse("http://play.jb51.net");
//                Uri content_url = Uri.parse("HTTPS://QR.ALIPAY.COM/FKX07910WZPN4ZZ800EK64");
//                intent.setData(content_url);
//                startActivity(intent);

            }
        });
    }

    private void setRefreshLayout() {
        SinaRefreshView headerView = new SinaRefreshView(getActivity());
        headerView.setArrowResource(R.drawable.ic_arrow);
        headerView.setTextColor(getResources().getColor(R.color.colorAccent));
        LoadingView loadingView = new LoadingView(getActivity());
        mRefreshlist.setHeaderView(headerView);
        mRefreshlist.setBottomView(loadingView);
        mRefreshlist.startRefresh();
        mRefreshlist.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                page = 1;
                getDataList();
                getBanner();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                page++;
                if (null != bean)
                    getDataList();
            }
        });

    }


    /**
     * 获取广告轮播图
     */
    public void getBanner() {
        if (null != list)
            list.clear();
        OkGo.get(Api.GET_BANNER_URL)
                .tag(getActivity())
                .params("sid", sid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("广告轮播图", s);
                        FollowBean bean = GsonUtil.parseJsonWithGson(s, FollowBean.class);
                        if (null != bean && 1 == bean.getStatus()) {
                            //设置图片来源
                            list = bean.getList();
                            List<String> images = new ArrayList<String>();
                            for (int i = 0; i < list.size(); i++) {
                                images.add(list.get(i).getImg());
                            }
                            try {
                                banner.setImages(images);

                                if (images.size() == 0)
                                    banner.setVisibility(View.GONE);
                                else
                                    banner.start();
                            } catch (Exception e) {
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.e("onError", e.toString());
                        super.onError(call, response, e);
                    }
                });

    }


    /**
     * 获取主播列表信息
     */
    public void getDataList() {
        if (sid.equals("1"))
            OkGo.get(Api.GET_ANCHOR_LIST_URL)
                    .tag(getActivity())
                    .params("uid", SPUserUtils.sharedInstance().getUid())
                    .params("types", sid)
                    .params("num", num)
                    .params("page", page)
                    .params("order", page == 1 ? "" : bean.getOrder())
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Log.e("热门：", s);
                            finishRefresh();
                            bean = GsonUtil.parseJsonWithGson(s, AnchorListBean.class);
                            if (null != bean)
                                if (1 == bean.getStatus()) {
                                    List<AnchorListBean.ListBean> addlist = bean.getList();
                                    if (page == 1)
                                        mList.clear();
                                    mList.addAll(addlist);
                                    mAdapter.notifyDataSetChanged();
                                } else {

                                    if (null != bean && bean.getInfo().equalsIgnoreCase("用户ID有误")) {
//                                SPUserUtils.sharedInstance().resetPreferences();
                                        startActivity(new Intent(getActivity(), LoginActivity.class));
                                        getActivity().finish();
                                    }
                                }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            Log.e("s", e.toString());
                            finishRefresh();
                            super.onError(call, response, e);
                        }

                    });
    }

    private void finishRefresh() {
        if (null != mRefreshlist)
            if (page == 1)
                mRefreshlist.finishRefreshing();
            else
                mRefreshlist.finishLoadmore();
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
