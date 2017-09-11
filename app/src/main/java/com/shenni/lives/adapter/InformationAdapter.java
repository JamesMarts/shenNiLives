package com.shenni.lives.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shenni.lives.R;
import com.shenni.lives.bean.MessageBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 消息页面
 */
public class InformationAdapter extends BaseAdapter {

    private Context mContext;
    private List<MessageBean.ListBean> data;;
    private LayoutInflater mLayoutInflater;

    public InformationAdapter(Context context, List<MessageBean.ListBean> data) {
        this.mContext = context;
        this.data = data;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void NotifyAdapter(List<MessageBean.ListBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_message, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }



        viewHolder.tvMessTitle.setText(data.get(position).getTitle());
        viewHolder.tvMessTime.setText(data.get(position).getAddtime());
        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.tv_mess_title)
        TextView tvMessTitle;
        @InjectView(R.id.tv_mess_time)
        TextView tvMessTime;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}