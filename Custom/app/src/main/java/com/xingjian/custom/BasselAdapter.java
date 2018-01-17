package com.xingjian.custom;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by thinkpad on 2018/1/17.
 */

public class BasselAdapter extends BaseAdapter {
    TextView textView;
    Context context;
    ViewGroup myGroup;

    public BasselAdapter(TextView textView, Context context, ViewGroup myGroup) {
        this.textView = textView;
        this.context = context;
        this.myGroup = myGroup;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    int count = 0;

    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_bassel, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.btn = (Button) view.findViewById(R.id.btn);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasselView basselView = new BasselView(context);
                int[] position = new int[2];
                view.getLocationInWindow(position);
                basselView.setStartPosition(new Point(position[0], position[1]));
                myGroup.addView(basselView);
                int[] endPosition = new int[2];
                textView.getLocationInWindow(endPosition);
                basselView.setEndPosition(new Point(endPosition[0], endPosition[1]));
                basselView.setAnimationFinish(new BasselView.AnimationFinish() {
                    @Override
                    public void setFinishListener() {
                        count++;
                        textView.setText("收到个数为："+count);
                    }
                });
                basselView.startBasselAnimation();
            }
        });
        return view;
    }

    private class ViewHolder {
        Button btn;
    }
}
