package com.shenni.lives.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shenni.lives.R;
import com.shenni.lives.adapter.SearchAdapter;
import com.shenni.lives.api.Api;
import com.shenni.lives.base.BaseActivity;
import com.shenni.lives.bean.BackMsg;
import com.shenni.lives.bean.SearchBean;
import com.shenni.lives.utils.GsonUtil;
import com.shenni.lives.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class SearchActivity extends BaseActivity {

    @InjectView(R.id.lv_search_listt)
    ListView lvSearchList;
    @InjectView(R.id.iv_left)
    ImageView ivLeft;
    @InjectView(R.id.rl_left)
    RelativeLayout rlLeft;
    @InjectView(R.id.ed_title)
    EditText edTitle;
    @InjectView(R.id.iv_right)
    ImageView ivRight;
    @InjectView(R.id.rl_search_right)
    RelativeLayout rlSearchRight;
    @InjectView(R.id.toolbar_main)
    LinearLayout toolbarMain;
    @InjectView(R.id.trl_refresh_list)
    TwinklingRefreshLayout mRefreshlist;

    private SearchAdapter mAdapter;
    private List<SearchBean.ListBean> mData;
    private Intent intentrefersh = new Intent();
    int is_qx = 0;


    private OnSearClickListener mOnItemClickListener = null;

    public static interface OnSearClickListener {
        void onViewClick(int search);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        noActionBar();
        mData = new ArrayList<>();
        mAdapter = new SearchAdapter(SearchActivity.this, mData);
        mAdapter.setOnItemClickListener(listener);
        lvSearchList.setAdapter(mAdapter);

        edTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//do something;
                    getData(getSearch());
                    return true;
                }
                return false;
            }
        });


        setRefreshLayout();
        lvSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent mIntent = new Intent(SearchActivity.this, PlayerLiveActivity.class);
//                mIntent.putExtra("liveAid", mData.get(position).getAid());
//                mIntent.putExtra("address", mData.get(position).getAddress());
//                startActivity(mIntent);

//                int liveend = (int) SPUtils.get(mData.get(position).getAid() + "liveend", -11);
//                if (liveend != 1)
//                    if (3 == mData.get(position).getLevel()) {
//                        toast("走私");
//                    } else if (Integer.valueOf(config.getLevel()) >= mData.get(position).getLevel()) {
//                        SPUtils.put(mData.get(position).getAid() + "msg", new Gson().toJson(mData.get(position).getCiku()));
//                        Intent mIntent = new Intent(SearchActivity.this, PlayerLiveActivity.class);
//                        mIntent.putExtra("liveAid", mData.get(position).getAid());
//                        mIntent.putExtra("source", mData.get(position).getSource());
//                        mIntent.putExtra("freetime", mData.get(position).getFreetime());
//                        startActivity(mIntent);
//                    } else {
//                        Intent mIntent = new Intent(SearchActivity.this, OpenActivity.class);
//                        mIntent.putExtra("liveAid", mData.get(position).getAid());
//                        mIntent.putExtra("source", mData.get(position).getSource());
//                        startActivity(mIntent);
//                    }
            }
        });
    }


    protected View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            switch (v.getId()) {
                case R.id.tv_nums:
                    if (tag != null) {
                        int position = (int) tag;
                        getGuanZhu(mData.get(position).getAid(), position);
                    }
                    break;

            }
        }
    };

    @OnClick(R.id.rl_left)
    public void onViewClicked() {
        finish();
    }

    @OnClick(R.id.rl_search_right)
    public void rightAction() {
        getData(getSearch());
        super.rightAction();
    }

    private String getSearch() {
        String strTitle = edTitle.getText().toString().trim();
        if (!StringUtil.isNullOrEmpty(strTitle))
            return strTitle;
        return "";
    }


    private void setRefreshLayout() {
        SinaRefreshView headerView = new SinaRefreshView(this);
        headerView.setArrowResource(R.drawable.ic_arrow);
        headerView.setTextColor(getResources().getColor(R.color.colorAccent));
        LoadingView loadingView = new LoadingView(this);
        mRefreshlist.setHeaderView(headerView);
        mRefreshlist.setBottomView(loadingView);
        mRefreshlist.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        getData(getSearch());
                        if (null != mRefreshlist)
                            mRefreshlist.finishRefreshing();
                    }
                }, 500);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        if (null != mData)
                            getData(getSearch());
                        if (null != mRefreshlist)
                            mRefreshlist.finishLoadmore();
                    }
                }, 500);
            }
        });

    }


    /**
     * 搜索
     */
    public void getData(String strTitle) {
        OkGo.get(Api.GET_USE_SEARCH)
                .tag(SearchActivity.this)
                .params("uid", uid)
                .params("title", strTitle)
                .params("num", num)
                .params("page", page)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("搜索", s);
                        SearchBean searchBean = GsonUtil.parseJsonWithGson(s, SearchBean.class);
                        if (null != searchBean && 1 == searchBean.getStatus()) {
                            List<SearchBean.ListBean> addlist = searchBean.getList();
                            if (searchBean.getStatus() == 1) {
                                if (page == 1)
                                    mData.clear();
                                mData.addAll(addlist);
                                mAdapter.notifyDataSetChanged();
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
     * 关注
     */
    public void getGuanZhu(int aid, final int position) {
        is_qx = mData.get(position).getIs_focus();
        OkGo.get(Api.GET_USE_GUANZHU)
                .tag(SearchActivity.this)
                .params("uid", uid)
                .params("aid", aid)
                .params("num", num)
                .params("is_qx", is_qx)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("关注", s);

                        BackMsg backMsg = GsonUtil.parseJsonWithGson(s, BackMsg.class);
                        if (null != backMsg && 1 == backMsg.getStatus()) {
                            if (1 == backMsg.getStatus()) {
                                setResult(RESULT_OK, intentrefersh);
                                toast(backMsg.getInfo());
                                if (is_qx == 0)
                                    mData.get(position).setIs_focus(1);
                                else
                                    mData.get(position).setIs_focus(0);
                                mAdapter.notifyDataSetChanged();
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


}
