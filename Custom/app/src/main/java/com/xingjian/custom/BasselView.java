package com.xingjian.custom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

/**
 * 贝塞尔动画
 * Created by thinkpad on 2018/1/17.
 */

public class BasselView extends TextView {
    private Context context;
    private Paint circlePaint;
    private int radius;
    private Point endPosition;
    private Point startPosition;

    public BasselView(Context context) {
        this(context, null);
    }

    public BasselView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BasselView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setGravity(Gravity.CENTER);
        setText("1");
        setTextColor(Color.WHITE);
        setTextSize(12);

        circlePaint = new Paint();
        circlePaint.setColor(Color.RED);
        circlePaint.setAntiAlias(true);
    }

    public static final int VIEW_SIZE = 20;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int PX4SIZE = (int) convertDpToPixel(VIEW_SIZE);
        setMeasuredDimension(PX4SIZE, PX4SIZE);
        radius = PX4SIZE / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, radius, circlePaint);
        super.onDraw(canvas);
    }

    public float convertDpToPixel(float dp) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        float px = dp * (displayMetrics.densityDpi / 160f);
        return px;
    }

    public void setStartPosition(Point startPosition) {
        startPosition.y -= 10;
        this.startPosition = startPosition;
    }

    public void setEndPosition(Point endPosition) {
        this.endPosition = endPosition;
    }

    public void startBasselAnimation() {
        if (startPosition == null || endPosition == null) return;
        final int pointX = (startPosition.x + endPosition.x) / 2;
        int pointY = (int) (startPosition.y - convertDpToPixel(100));
        Point point = new Point(pointX, pointY);
        BasselEvaluator basselEvaluator = new BasselEvaluator(point);
        ValueAnimator anim = ValueAnimator.ofObject(basselEvaluator, startPosition, endPosition);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Point point1 = (Point) valueAnimator.getAnimatedValue();
                setX(point1.x);
                setY(point1.y);
                invalidate();
            }
        });
        anim.setDuration(1000);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ViewGroup viewGroup = (ViewGroup) getParent();
                viewGroup.removeView(BasselView.this);
                animationFinish.setFinishListener();
            }
        });
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }

    AnimationFinish animationFinish;

    public void setAnimationFinish(AnimationFinish animationFinish) {
        this.animationFinish = animationFinish;
    }

    interface AnimationFinish {
        void setFinishListener();
    }

    //评估器
    public class BasselEvaluator implements TypeEvaluator<Point> {
        private Point controllPoint;

        public BasselEvaluator(Point controllPoint) {
            this.controllPoint = controllPoint;
        }

        @Override
        public Point evaluate(float v, Point point, Point t1) {
            int x = (int) ((1 - v) * (1 - v) * point.x + 2 * v * (1 - v) * controllPoint.x + v * v * t1.x);
            int y = (int) ((1 - v) * (1 - v) * point.y + 2 * v * (1 - v) * controllPoint.y + v * v * t1.y);
            return new Point(x, y);
        }
    }
}
