package com.shenni.lives.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.shenni.lives.R;
import com.squareup.picasso.Picasso;
import com.zhy.autolayout.utils.AutoUtils;

import org.dync.giftlibrary.util.GiftStringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 横向listview的数据适配器，也就是观众列表，后居者可以直接根据自己的需求在这里改功能以及布局文件就ok
 * <p>
 * Success is the sum of small efforts, repeated day in and day out.
 * 成功就是日复一日那一点点小小努力的积累。
 * AndroidGroup：158423375
 * Author：Johnny
 * AuthorQQ：956595454
 * AuthorWX：Qiang_it
 * AuthorPhone：nothing
 * Created by 2016/9/21.
 */
public class AudienceAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public AudienceAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_audienceadapter, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(mContext)
                .load(GiftStringUtils.getHeanPic())
//                .load(GiftStringUtils.getWebHeanPic())
                .error(GiftStringUtils.getHeanPic())
                .resize(100, 100).centerCrop()
                .placeholder(GiftStringUtils.getHeanPic())
//                .placeholder(R.drawable.icon_load)
                .into(holder.crvheadimage);

        return convertView;

    }

    static class ViewHolder {
        @InjectView(R.id.crvheadimage)
        CircleImageView crvheadimage;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}