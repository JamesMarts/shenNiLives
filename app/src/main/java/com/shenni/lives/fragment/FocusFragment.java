package com.shenni.lives.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shenni.lives.R;
import com.shenni.lives.activity.OpenActivity;
import com.shenni.lives.activity.PlayerEndActivity;
import com.shenni.lives.adapter.FocusAdapter;
import com.shenni.lives.api.Api;
import com.shenni.lives.base.BaseFragment;
import com.shenni.lives.bean.AnchorListBean;
import com.shenni.lives.bean.FoucsBean;
import com.shenni.lives.jjdxmplayer.PlayerLiveActivity;
import com.shenni.lives.utils.GsonUtil;
import com.shenni.lives.utils.SPUserUtils;
import com.shenni.lives.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static com.shenni.lives.utils.Constants.SET_DATA_PBANCHOR;


/**
 * 关注页面
 */
public class FocusFragment extends BaseFragment {
    @InjectView(R.id.recycleview)
    RecyclerView recycleview;

    private List<AnchorListBean.ListBean> mList;
    @InjectView(R.id.trl_hotrefresh_list)
    TwinklingRefreshLayout mRefreshlist;

    private FocusAdapter mAdapter;
    private FoucsBean mFoucsBean;
    private AnchorListBean bean;
    protected String sid = "2";

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        if (isVisibleToUser) {
//            page = 1;
//            getDataList();
//            if (null != mRefreshlist)
//                mRefreshlist.finishRefreshing();
//        }
//        super.setUserVisibleHint(isVisibleToUser);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_focus, container, false);
        ButterKnife.inject(this, view);
        InitData();
        getDataList();
        return view;
    }


    private void InitData() {

        LinearLayoutManager layoutManager;
        if (sid.equalsIgnoreCase("2") || sid.equalsIgnoreCase("3")) {
            layoutManager = new GridLayoutManager(getActivity(), 2);//这里用线性宫格显示 类似于grid view
        } else {
            layoutManager = new LinearLayoutManager(getActivity());
        }
//设置布局管理器
        recycleview.setLayoutManager(layoutManager);
//设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mList = new ArrayList<>();
        mAdapter = new FocusAdapter(getContext(), mList);
        recycleview.setAdapter(mAdapter);
        setRefreshLayout();
        mAdapter.setOnItemClickListener(new FocusAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

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
                        mIntent.putExtra("bigpic", mList.get(position).getBigpic());
                        mIntent.putExtra("source", mList.get(position).getSource());
                        //int
                        mIntent.putExtra("liveLevel", mList.get(position).getLevel());
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
                        mIntent.putExtra("bigpic", mList.get(position).getBigpic());
                        mIntent.putExtra("source", mList.get(position).getSource());
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
        if (requestCode == SET_DATA_PBANCHOR) {
            if (resultCode == RESULT_OK) {
//                page = 1;
//                getDataList();
//                if (null != mRefreshlist)
//                    mRefreshlist.finishRefreshing();
            }
        }
    }

    private void setRefreshLayout() {
        SinaRefreshView headerView = new SinaRefreshView(getActivity());
        headerView.setArrowResource(R.drawable.ic_arrow);
        headerView.setTextColor(getResources().getColor(R.color.colorAccent));
        LoadingView loadingView = new LoadingView(getActivity());
        mRefreshlist.setHeaderView(headerView);
        mRefreshlist.setBottomView(loadingView);
        mRefreshlist.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                page = 1;
                getDataList();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                page++;
                if (null != bean)
                    getDataList();
            }
        });

    }


    public FocusFragment instance(int type) {
        if (type == 0)
            refreshUi();
        return new FocusFragment();
    }

    public void refreshUi() {
        try {
            if (sid.equalsIgnoreCase("2"))
                page = 1;
            getDataList();
            mRefreshlist.finishRefreshing();
        } catch (Exception e) {
        }
    }


    /**
     * 获取主播列表信息
     */
    public void getDataList() {
        OkGo.get(Api.GET_ANCHOR_LIST_URL)
                .tag(getActivity())
                .params("uid", uid)
                .params("types", sid)
                .params("num", num)
                .params("page", page)
                .params("order", page == 1 ? "" : bean.getOrder())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            finishRefresh();
                            Log.e(sid.equalsIgnoreCase("2") ? "关注：" : "同城：", s);
                            bean = GsonUtil.parseJsonWithGson(s, AnchorListBean.class);
                            if (null != bean.getList()) {
                                List<AnchorListBean.ListBean> addlist = bean.getList();
                                if (null != bean && 1 == bean.getStatus()) {
                                    if (page == 1)
                                        mList.clear();
                                    mList.addAll(addlist);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        } catch (Exception w) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        //Activity销毁时，取消网络请求
        OkGo.getInstance().cancelTag(this);
    }

}
