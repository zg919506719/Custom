package com.xingjian.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 滑动冲突
 * Created by thinkpad on 2018/1/26.
 */

public class ClashAdapter extends BaseAdapter {
    Context context;

    public ClashAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 55;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_clash, viewGroup, false);
            viewHolder=new ViewHolder();
            viewHolder.tv= (TextView) view.findViewById(R.id.tv_adapter);
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.tv.setText(String.valueOf(i));
        return view;
    }

    static class ViewHolder {
        TextView tv;
    }
}
