package com.shenni.lives.adapter;


import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017-04-29.
 */

public class HotAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private Context mcontext;
    List<AnchorListBean.ListBean> mList;


    public HotAdapter(Context context, List<AnchorListBean.ListBean> mList) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mList = mList;
        this.mcontext = context;
    }


    @Override
    public int getCount() {
        if (null != mList)
            return mList.size();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_hot, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (!StringUtil.isNullOrEmpty(mList.get(position).getSmallpic()))
            Picasso.with(mcontext)
                    .load(mList.get(position).getSmallpic())
                    .resize(50, 50).centerCrop()
                    .placeholder(R.color.white)
                    .error(R.drawable.icon_kongbai)
                    .into(holder.ivFollowHead);

        if (!StringUtil.isNullOrEmpty(mList.get(position).getBigpic()))
            Picasso.with(mcontext)
                    .load(mList.get(position).getBigpic())
                    .resize(260, 280).centerCrop()
                    .placeholder(R.color.white)
                    .error(R.drawable.icon_kongbai)
                    .into(holder.photo);

        holder.ivFollowtype.setBackgroundResource(levetype(mList.get(position).getLevel()));


//        Picasso.with(mcontext)
//                .load(levetype(mList.get(position).getLevel()))
//                .placeholder(R.drawable.icon_load)
//                .error(R.drawable.icon_kongbai)
//                .into( holder.ivFollowtype);


        //部分文字改变颜色
//ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan redSpan = new ForegroundColorSpan(mcontext.getResources().getColor(R.color.colorAccent));
        holder.tvNums.setText(String.valueOf(mList.get(position).getAudience()) + "人在看");
//这里注意一定要先给textview赋值
        SpannableStringBuilder builder = new SpannableStringBuilder(holder.tvNums.getText().toString());
//为不同位置字符串设置不同颜色
        builder.setSpan(redSpan, 0, holder.tvNums.getText().toString().length() - 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//最后为textview赋值
        holder.tvNums.setText(builder);


//        holder.tvNums.setText(String.valueOf(mList.get(position).getAudience()) + "人在看");
//        holder.tvFollowGolds.setText(mList.get(position).getAddress());

//        String city = (String) SPUtils.get("city", "");
//        holder.tvFollowGolds.setText(StringUtil.isNullOrEmpty(city) ? mList.get(position).getAddress() : city);
        holder.tvFollowName.setText(mList.get(position).getNickname());
        return convertView;
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

    static class ViewHolder {
        @InjectView(R.id.iv_follow_head)
        CircleImageView ivFollowHead;
        @InjectView(R.id.tv_follow_name)
        TextView tvFollowName;
        @InjectView(R.id.tv_follow_golds)
        TextView tvFollowGolds;
        @InjectView(R.id.tv_nums)
        TextView tvNums;
        @InjectView(R.id.photo)
        ImageView photo;
        @InjectView(R.id.iv_follow_type)
        ImageView ivFollowtype;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}


