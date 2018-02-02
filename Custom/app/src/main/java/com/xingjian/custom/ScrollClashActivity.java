package com.xingjian.custom;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by thinkpad on 2018/1/26.
 */

public class ScrollClashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        final ClashListview listView = (ClashListview) findViewById(R.id.lv_clash);
        ClashAdapter adapter = new ClashAdapter(this);
        listView.setAdapter(adapter);
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        final Display display = getWindowManager().getDefaultDisplay();
        layoutParams.height = display.getHeight() / 2;
        listView.setLayoutParams(layoutParams);
        final int height = getResources().getDisplayMetrics().heightPixels;
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
//                • OnScrollListener.SCROLL_STATE_IDLE：滚动停止时的状态
//
//• OnScrollListener.SCROLL_STATE_STOUCH_SCROLL：触摸正在滚动，手指还没离开界面时的状态
//
//• OnScrollListener.SCROLL_STATE_FLING：用户在用力滑动后，ListView由于惯性将继续滑动时的状态
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
//• firstVisibleItem：当前能看见的第一个item的ID（从0开始）
//
//• visibleItemCount：当前可见的item总数
//
//• totalItemCount：列表中适配器总数量，也就是整个ListView中item总数
//
//                注意：当前可见的item总数，包括屏幕中没有显示完整的item，如显示一半的item也会算在可见范围内
                View childAt = listView.getChildAt(0);
                if (childAt != null) {
                    int height1 = childAt.getHeight();
                    int scrollY = height1 * i;

                    TextView textView = (TextView) findViewById(R.id.tv);
                    textView.setAlpha(1 - (float) scrollY / height);
                }

            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            listView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
//                    int scrollX, int scrollY, int oldScrollX, int oldScrollY
                    Log.i("haha", "onCreate: " + i + "," + i1 + "," + i2 + "," + i3);
                }
            });
        }
        MyScrollView scrollView = (MyScrollView) findViewById(R.id.scroll);
        scrollView.setListener(new MyScrollView.Listener() {
            @Override
            public void dealScrool(int l, int t, int oldl, int oldt) {
                Log.i("haha", "dealScrool: " + l + "," + t + "," + oldl + "," + oldt + ",");
            }


        });
    }
}
