package com.shenni.lives.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shenni.lives.R;
import com.shenni.lives.bean.ChatBean;
import com.shenni.lives.utils.SPUserUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 这里是聊天列表的数据适配器，比如大家使用的是环信或者第三方直播的聊天室功能，都会用的listview，
 * 对于聊天列表里面的交互以及显示方式，大家都可以在这里修改，以及布局文件
 * <p>
 * Success is the sum of small efforts, repeated day in and day out.
 * 成功就是日复一日那一点点小小努力的积累。
 * AndroidGroup：158423375
 * Author：Johnny
 * AuthorQQ：956595454
 * AuthorWX：Qiang_it
 * AuthorPhone：nothing
 * Created by 2016/9/22.
 */
public class MessageAdapter2 extends BaseAdapter {

    private Context mContext;
    private ViewHolder holder;
    private List<ChatBean> data;

    public MessageAdapter2(Context context, List<ChatBean> data) {
        this.mContext = context;
        this.data = data;
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

    public void NotifyAdapter(List<ChatBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_messageadapter2, null);
            holder.tvnick = (TextView) convertView.findViewById(R.id.tvnick);
            holder.tvcontent = (TextView) convertView.findViewById(R.id.tvcontent);
            holder.ivtitleleft = (CircleImageView) convertView.findViewById(R.id.iv_title_left);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.tvcontent.setText(data.get(position).getText());
//        holder.tvnick.setText(data.get(position).getUsername());

//        if (data.get(position).getLevel() == -2)
//            holder.ivtitleleft.setBackgroundResource(levetype(Integer.valueOf(SPUserUtils.sharedInstance().getLevel())));
//        else
//            holder.ivtitleleft.setBackgroundResource(levetype(data.get(position).getLevel()));
        Picasso.with(mContext)
                .load(SPUserUtils.sharedInstance().getHeadpic())
                .resize(50, 50).centerCrop()
                .placeholder(R.color.white)
                .into( holder.ivtitleleft);
        return convertView;
    }




    private final class ViewHolder {
        TextView tvcontent;
        TextView tvnick;
        CircleImageView ivtitleleft;
    }
}