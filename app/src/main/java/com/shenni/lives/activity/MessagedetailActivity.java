package com.shenni.lives.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shenni.lives.R;
import com.shenni.lives.api.Api;
import com.shenni.lives.base.BaseActivity;
import com.shenni.lives.bean.MessDetailBean;
import com.shenni.lives.widget.FullyLinearLayoutManager;
import com.squareup.picasso.Picasso;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;
import okhttp3.Response;


public class MessagedetailActivity extends BaseActivity {

    @InjectView(R.id.tv_msgd_title)
    TextView tvMsgdTitle;
    @InjectView(R.id.tv_msgd_content)
    TextView tvMsgdContent;
    @InjectView(R.id.tv_msgd_content_pic)
    RecyclerView tvMsgdContentPic;
    @InjectView(R.id.tv_msgd_time)
    TextView tvMsgdTime;
    private String mMessageID = "";
    private MessDetailBean messDetailBean;

    private List<String> mList;
    private MessDetaAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagedetail);
        ButterKnife.inject(this);
        mMessageID = getIntent().getStringExtra("id");
        initView();
        getData();
    }

    private void initView() {
        setTitleBar("消息详情");

        FullyLinearLayoutManager layoutManager;
//        = new GridLayoutManager(MessagedetailActivity.this, 2);
        layoutManager = new FullyLinearLayoutManager(MessagedetailActivity.this);
//设置布局管理器
        tvMsgdContentPic.setLayoutManager(layoutManager);
//设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mList = new ArrayList<>();
        mAdapter = new MessDetaAdapter(MessagedetailActivity.this, mList);
        tvMsgdContentPic.setAdapter(mAdapter);
    }


    /**
     * 消息详情
     */
    private void getData() {
        OkGo.get(Api.GET_USER_GETMSG)
                .tag(this)
                .params("uid", uid)
                .params("id", mMessageID)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("消息详情：", s);
//                        MessageBean messageBean = GsonUtil.parseJsonWithGson(s, MessageBean.class);

                        messDetailBean = new Gson().fromJson(s, MessDetailBean.class);
                        if (null != messDetailBean ) {
//                        if (null != messDetailBean && 1 == messDetailBean.getStatus()) {

                            tvMsgdTitle.setText(messDetailBean.getMsg().getTitle());
                            List<String> mContent = messDetailBean.getMsg().getContent().getTxtlist();
                            for (String cont : mContent) {
                                tvMsgdContent.append(cont + "\n");
                            }
                            mList.addAll(messDetailBean.getMsg().getContent().getPics());
                            mAdapter.notifyDataSetChanged();

                            tvMsgdTime.setText(messDetailBean.getMsg().getAddtime());
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.e("s", e.toString());
                        super.onError(call, response, e);
                    }
                });
    }


    protected class MessDetaAdapter extends RecyclerView.Adapter<MessDetaAdapter.MyViewHolder> {

        private List<String> mList;
        private LayoutInflater mLayoutInflater;
        private Context mcontext;


        public MessDetaAdapter(Context context, List<String> mList) {
            mLayoutInflater = LayoutInflater.from(context);
            this.mList = mList;
            this.mcontext = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.item_msgd, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Picasso.with(mcontext)
                    .load(mList.get(position))
//                    .resize(140, 160).centerCrop()
                    .placeholder(R.color.white)
//                    .error(R.drawable.icon_kongbai)
                    .into(holder.ivMdPic);
        }


        @Override
        public int getItemCount() {
            if (null != mList)
                return mList.size();
            else return 0;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @InjectView(R.id.iv_md_pic)
            ImageView ivMdPic;

            MyViewHolder(View view) {
                super(view);
                AutoUtils.autoSize(view);
                ButterKnife.inject(this, view);
            }
        }
    }
}
