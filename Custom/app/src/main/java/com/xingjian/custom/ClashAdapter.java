package com.xingjian.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
        return 5;
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
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_clash, viewGroup, false);
        }
        return view;
    }

    static class ViewHolder {

    }
}
