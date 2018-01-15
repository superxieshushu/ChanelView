package com.saltedfish.chanelview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by SaltedFish on 2018/1/14.
 * 适配器
 */

public class ParallaxAdapter extends BaseAdapter {
    private Context context;
    private int firstVisibleItem;
    private int[] resId = {
            Color.parseColor("#111111"),
            Color.parseColor("#333333"),
            Color.parseColor("#555555"),
            Color.parseColor("#777777"),
            Color.parseColor("#999999"),
            Color.parseColor("#bbbbbb"),
            Color.parseColor("#dddddd"),
            Color.parseColor("#ffffff")};

    public ParallaxAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 8;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ParallaxViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ParallaxViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_parallax, viewGroup, false);
            viewHolder.ivMask = (ImageView) view.findViewById(R.id.iv_mask);
            viewHolder.ivContent = (ImageView) view.findViewById(R.id.iv_content);
            viewHolder.parallaxItem = (ParallaxItem) view.findViewById(R.id.item);
            viewHolder.tvContent = (TextView) view.findViewById(R.id.tv_content);
            view.setTag(viewHolder);
        }
        viewHolder = (ParallaxViewHolder) view.getTag();
        viewHolder.ivContent.setBackgroundColor(resId[position]);
        viewHolder.tvContent.setText("位置 " + position);
        viewHolder.tvContent.setTextColor(Color.parseColor("#cfcfcf"));
        viewHolder.parallaxItem.parallax(firstVisibleItem >= position ? 1f : 0);
        return view;
    }

    public void setFirstVisibleItem(int firstVisibleItem) {
        this.firstVisibleItem = firstVisibleItem;
    }

    static class ParallaxViewHolder {
        ImageView ivMask;
        TextView tvContent;
        ImageView ivContent;
        ParallaxItem parallaxItem;
    }
}
