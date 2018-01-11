package com.xingjian.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * http://blog.csdn.net/guolin_blog/article/details/43376527
 * <merge/>和<include/>标签解决布局嵌套问题
 * Created by thinkpad on 2018/1/11.
 */

public class GestureView extends LinearLayout {
    //1,定义GestureDetector类
    private GestureDetector m_gestureDetector;

    private int m_max_scrollX;

    public GestureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置为可点击
        setClickable(true);
        //2,初始化手势类，同时设置手势监听
        m_gestureDetector = new GestureDetector(context, onGestureListener);

        LayoutInflater.from(context).inflate(R.layout.view_gesture, this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //3,将touch事件交给gesture处理
        m_gestureDetector.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            // GestureDetector没有处理up事件的方法，只能在这里处理了。
            int scrollX = getScrollX();
            if (scrollX > m_max_scrollX / 2) {
                show_right_view();
            } else {
                hide_right_view();
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            //测量子view的宽高，？不测量，右侧布局会不显示，这里有点疑问
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
            if (i == 1) {
                m_max_scrollX = getChildAt(i).getMeasuredWidth();
            }
        }
    }

    //初始化手势监听对象，使用GestureDetector.OnGestureListener的实现抽象类，因为实际开发中好多方法用不上
    private final GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            int scrollX = getScrollX();
            int minScrollX = -scrollX;
            int maxScrollY = m_max_scrollX - scrollX;
            // 对滑动的距离边界控制
            if (distanceX > maxScrollY) {
                distanceX = maxScrollY;
            } else if (distanceX < minScrollX) {
                distanceX = minScrollX;
            }
            scrollBy((int) distanceX, 0);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (velocityX < 0) {
                //快速向左滑动
                show_right_view();
            } else {
                hide_right_view();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };

    private void show_right_view() {
        scrollTo(m_max_scrollX, 0);
    }

    private void hide_right_view() {
        scrollTo(0, 0);
    }
}
