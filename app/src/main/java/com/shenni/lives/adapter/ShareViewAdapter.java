package com.shenni.lives.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shenni.lives.R;
import com.shenni.lives.bean.ShareBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017-04-29.
 */

public class ShareViewAdapter extends RecyclerView.Adapter<ShareViewAdapter.MyViewHolder> implements View.OnClickListener {
    private OnItemClickListener mOnItemClickListener = null;

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private LayoutInflater mLayoutInflater;
    private Context mcontext;
    private List<ShareBean> mList;

    public ShareViewAdapter(Context context, List<ShareBean> list) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mList = list;
        this.mcontext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new Hot2Adapter(mLayoutInflater.inflate(R.layout.item_hot, parent, false));
        View view;
        if (getItemCount() == 6)
            view = mLayoutInflater.inflate(R.layout.item_share, parent, false);
        else
            view = mLayoutInflater.inflate(R.layout.item_share, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return myViewHolder;

    }


    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }


    @Override
    public void onBindViewHolder(MyViewHolder mHolder, int position) {
        mHolder.tvFollowName.setText(mList.get(position).getText());
        Glide.with(mcontext)
                .load(mList.get(position).getImg())
                .placeholder(R.drawable.icon_load)
                .error(R.drawable.icon_kongbai)
                .into(mHolder.ivShareSocia);

        //将position保存在itemView的Tag中，以便点击时进行获取
        mHolder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    class MyViewHolder extends ViewHolder {
        @InjectView(R.id.iv_share_socia)
        ImageView ivShareSocia;
        @InjectView(R.id.tv_follow_name)
        TextView tvFollowName;
        @InjectView(R.id.ll_share)
        LinearLayout llShare;

        public MyViewHolder(View view) {
            super(view);
            AutoUtils.autoSize(view);
            ButterKnife.inject(this, view);
        }
    }

}


