package com.xingjian.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * 拖动小球
 * Created by thinkpad on 2018/1/11.
 */

public class BallView extends View {
    private Paint m_paint;
    //小球的半径
    private int radius;
    private GestureDetector gestureDetector;
    //小球的中心坐标
    private int centerX;
    private int centerY;
    //是否在小球上
    private boolean touch_ball;

    public BallView(Context context) {
        super(context);
    }

    public BallView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BallView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始画笔
        m_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        m_paint.setColor(getResources().getColor(android.R.color.holo_blue_light));
        setClickable(true);
        //初始化手势，同时设置手势监听
        gestureDetector = new GestureDetector(context, listener);
        radius = 50;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //touch事件交给gesture处理
        gestureDetector.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //判断手指落在了小球上
            if (getDistanceByPoint(centerX, centerY, (int) event.getX(), (int) event.getY()) < radius) {
                touch_ball = true;
            } else {
                touch_ball = false;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
        //默认圆心在中心点
        if (w > 0) {
            centerX = w / 2;
        }
        if (h > 0) {
            centerY = h / 2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        canvas.drawCircle(centerX, centerY, radius, m_paint);
    }

    GestureDetector.OnGestureListener listener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float distanceX, float distanceY) {
            if (touch_ball) {
                centerY -= distanceY;
                centerX -= distanceX;
//                处理边界问题
                if (centerX < radius) {
                    centerX = radius;
                } else if (centerX > getWidth() - radius) {
                    centerX = getWidth() - radius;
                }
                if (centerY < radius) {
                    centerY = radius;
                } else if (centerY > getHeight() - radius) {
                    centerY = getHeight() - radius;
                }
                //修改圆心后，通知重绘
                postInvalidate();
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }
    };

    /*
    * 计算两点之间的距离
    * */
    private int getDistanceByPoint(int x1, int y1, int x2, int y2) {
        double temp = Math.abs((x2 - x1) * (x2 - x1) - (y2 - y1) * (y2 - y1));
        return (int) Math.sqrt(temp);
    }
}
