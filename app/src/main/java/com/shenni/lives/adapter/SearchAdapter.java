package com.shenni.lives.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shenni.lives.R;
import com.shenni.lives.bean.SearchBean;
import com.squareup.picasso.Picasso;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/5/2.
 * function：查找页面
 */

public class SearchAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<SearchBean.ListBean> mList;
    private View.OnClickListener onItemClickListener;

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onItemClickListener = onClickListener;
    }


    public SearchAdapter(Context context, List<SearchBean.ListBean> list) {
        this.context = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_search, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvMineGolds.setText(String.valueOf(mList.get(position).getAudience()));
        viewHolder.tvMineName.setText(mList.get(position).getNickname());
//        viewHolder.ivMineHead.setBackgroundResource(mList.get(position).getCover());

        Picasso.with(context.getApplicationContext())
                .load(mList.get(position).getSmallpic())
                .resize(100, 100).centerCrop()
                .error(R.drawable.icon_kongbai)
                .into(viewHolder.ivMineHead);
        if (mList.get(position).getIs_focus() == 0)
            viewHolder.tvNums.setText("关注");
        else
            viewHolder.tvNums.setText("取消");

        viewHolder.tvNums.setOnClickListener(onItemClickListener);
        viewHolder.tvNums.setTag(position);


        return convertView;
    }


    class ViewHolder {
        @InjectView(R.id.iv_search_head)
        CircleImageView ivMineHead;
        @InjectView(R.id.tv_search_name)
        TextView tvMineName;
        @InjectView(R.id.tv_search_gold)
        TextView tvMineGold;
        @InjectView(R.id.tv_search_golds)
        TextView tvMineGolds;
        @InjectView(R.id.tv_nums)
        TextView tvNums;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
