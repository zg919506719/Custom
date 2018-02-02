package com.xingjian.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;

/**
 *  让listview在scrollview中自由滑动
 * http://blog.csdn.net/wanghao200906/article/details/51084975
 * Created by thinkpad on 2018/2/2.
 */

public class ClashListview extends ListView {
    private float down;
    private float y;

    public ClashListview(Context context) {
        super(context,null);
    }

    public ClashListview(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public ClashListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    /**
     * 改listview滑到底端了
     *
     * @return
     */
    public boolean isBottom() {
        int firstVisibleItem = getFirstVisiblePosition();//屏幕上显示的第一条是list中的第几条
        int childcount = getChildCount();//屏幕上显示多少条item
        int totalItemCount = getCount();//一共有多少条
        if ((firstVisibleItem + childcount) >=totalItemCount) {
            return true;
        }
        return false;
    }

    /**
     * 改listview在顶端
     *
     * @return
     */
    public boolean isTop() {
        int firstVisibleItem = getFirstVisiblePosition();
        if (firstVisibleItem ==0) {
            return true;
        }
        return false;
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case ACTION_DOWN:
                down = ev.getRawY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case ACTION_MOVE:
                y = ev.getRawY();
                if (isTop()){
                    if (y - down > 1) {
//                        到顶端,向下滑动 把事件教给父类
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else {
                        //                        到顶端,向上滑动 把事件拦截 由自己处理
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                if (isBottom()) {
                    if (y - down > 1) {
//                        到底端,向下滑动 把事件拦截 由自己处理
                        getParent().requestDisallowInterceptTouchEvent(true);
                    } else {
//                        到底端,向上滑动 把事件教给父类
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
