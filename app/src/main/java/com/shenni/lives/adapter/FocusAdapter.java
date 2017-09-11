package com.shenni.lives.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shenni.lives.R;
import com.shenni.lives.bean.AnchorListBean;
import com.shenni.lives.utils.StringUtil;
import com.squareup.picasso.Picasso;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by Administrator on 2017-04-29.
 */

public class FocusAdapter extends RecyclerView.Adapter<FocusAdapter.MyViewHolder> implements View.OnClickListener {
    private OnItemClickListener mOnItemClickListener = null;

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private LayoutInflater mLayoutInflater;
    private Context mcontext;
    private List<AnchorListBean.ListBean> mList;

    public FocusAdapter(Context context, List<AnchorListBean.ListBean> mList) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mList = mList;
        this.mcontext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new Hot2Adapter(mLayoutInflater.inflate(R.layout.item_hot, parent, false));
        View view = mLayoutInflater.inflate(R.layout.item_focus, parent, false);
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
        if (!StringUtil.isNullOrEmpty(mList.get(position).getBigpic()))
            Picasso.with(mcontext)
                    .load(mList.get(position).getBigpic())
                    .resize(140, 160).centerCrop()
                    .placeholder(R.color.white)
                    .error(R.drawable.icon_kongbai)
                    .into(mHolder.ivItemBg);
        Picasso.with(mcontext)
                .load(levetype(mList.get(position).getLevel()))
                .placeholder(R.color.white)
                .error(R.drawable.icon_kongbai)
                .into(mHolder.ivLiveType);



        //部分文字改变颜色
//ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan redSpan = new ForegroundColorSpan(mcontext.getResources().getColor(R.color.colorAccent));
        mHolder.tvItemHotAudience.setText(String.valueOf(mList.get(position).getAudience()) + "人在看");
//这里注意一定要先给textview赋值
        SpannableStringBuilder builder = new SpannableStringBuilder(mHolder.tvItemHotAudience.getText().toString());
//为不同位置字符串设置不同颜色
        builder.setSpan(redSpan, 0, mHolder.tvItemHotAudience.getText().toString().length() - 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//最后为textview赋值
        mHolder.tvItemHotAudience.setText(builder);


//        mHolder.tvItemHotAudience.setText(mList.get(position).getAudience() + "人在看");
//        mHolder.tvItemHotAdd.setText(mList.get(position).getAddress());

//        String city = (String) SPUtils.get("city", "");
//        mHolder.tvItemHotAdd.setText(StringUtil.isNullOrEmpty(city) ? mList.get(position).getAddress() : city);


        mHolder.tvItemHotAdd.setText(StringUtil.intRandom(50,200)+"km");

        //将position保存在itemView的Tag中，以便点击时进行获取
        mHolder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        if (null != mList)
            return mList.size();
        else return 0;
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    private int levetype(int level) {
        switch (level) {
            case 0:
            case 1:
                return R.drawable.icon_zhibo;
            case 2:
                return R.drawable.icon_biaoche;
            case 3:
                return R.drawable.icon_zousi;
            default:
                return R.drawable.icon_zhibo;
        }
    }

    static class MyViewHolder extends ViewHolder {
        @InjectView(R.id.iv_item_bg)
        ImageView ivItemBg;
        @InjectView(R.id.iv_add)
        ImageView ivAdd;
        @InjectView(R.id.tv_item_hot_add)
        TextView tvItemHotAdd;
        @InjectView(R.id.iv_live_type)
        ImageView ivLiveType;
        @InjectView(R.id.tv_peolpe)
        TextView tvPeolpe;
        @InjectView(R.id.tv_item_hot_audience)
        TextView tvItemHotAudience;

        MyViewHolder(View view) {
            super(view);
            AutoUtils.autoSize(view);
            ButterKnife.inject(this, view);
        }

    }


}


