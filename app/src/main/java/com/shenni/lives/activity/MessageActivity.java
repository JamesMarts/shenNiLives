package com.shenni.lives.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shenni.lives.R;
import com.shenni.lives.adapter.InformationAdapter;
import com.shenni.lives.api.Api;
import com.shenni.lives.base.BaseActivity;
import com.shenni.lives.bean.MessageBean;
import com.shenni.lives.utils.GsonUtil;
import com.shenni.lives.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;
import okhttp3.Response;

public class MessageActivity extends BaseActivity {

    @InjectView(R.id.lv_message_listt)
    ListView lvMessageListt;
    @InjectView(R.id.trl_refresh_list)
    TwinklingRefreshLayout mRefreshlist;
    private InformationAdapter mAdapter;

    private List<MessageBean.ListBean> mList;

    private Intent intentrefersh = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        ButterKnife.inject(this);
        initView();
        getData();
    }

    private void initView() {
        setTitleBar("消息");

        mList = new ArrayList<>();
        mAdapter = new InformationAdapter(MessageActivity.this, mList);
        lvMessageListt.setAdapter(mAdapter);

        setRefreshLayout();
        lvMessageListt.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setResult(RESULT_OK, intentrefersh);
                SPUtils.put("未读消息", ((int) SPUtils.get("未读消息", 999)) - 1);
                Intent intent = new Intent(MessageActivity.this, MessagedetailActivity.class);
                intent.putExtra("id", String.valueOf(mList.get(position).getId()));
                startActivity(intent);
            }
        });


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
                        getData();
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
                        if (null != mList)
                            getData();
                        if (null != mRefreshlist)
                            mRefreshlist.finishLoadmore();
                    }
                }, 500);
            }
        });

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
                        if (null != messageBean && 1 == messageBean.getStatus()) {
                            List<MessageBean.ListBean> addlist = messageBean.getList();
                            //if (page == 1)
                            mList.clear();
                            mList.addAll(addlist);
                            mAdapter.notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.e("s", e.toString());
                        super.onError(call, response, e);
                    }
                });
    }

}
