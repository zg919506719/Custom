package com.xingjian.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

/**
 * http://blog.csdn.net/harvic880925/article/details/51010839
 * 关于画笔
 * Created by thinkpad on 2018/1/12.
 */

public class TearView extends View {
    private Bitmap preBitmap;
    private Path path;
    private Paint paint;
    private Canvas mCanvas;
    private Bitmap clearBitmap;



    public TearView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TearView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundResource(R.mipmap.img1);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img2);
        //屏幕的宽高
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        preBitmap = Bitmap.createScaledBitmap(bitmap,displayMetrics.widthPixels,displayMetrics.heightPixels,true);

        path = new Path();
        paint = new Paint();
//        reset()
//        重置画笔
//        setColor(int color)
//        给画笔设置颜色值
//        setARGB(int a, int r, int g, int b)
//        同样是设置颜色，但是利用ARGB分开设置
//        setAlpha(int a)
//        设置画笔透明度
//        setStyle(Paint.Style style)
//        设置画笔样式，取值有
//        Paint.Style.FILL :填充内部
//        Paint.Style.FILL_AND_STROKE ：填充内部和描边
//        Paint.Style.STROKE ：仅描边
//        setStrokeWidth(float width)
//        设置画笔宽度
//        setAntiAlias(boolean aa)
//        设置画笔是否抗锯齿
//        上面这些函数，我们在前面几篇已经详细讲过了，难度也都不大，不再细讲。下面几个函数我们是没有讲到过的，下面做下补充
//        setStrokeCap(Paint.Cap cap)
//        设置线冒样式，取值有Cap.ROUND(圆形线冒)、Cap.SQUARE(方形线冒)、Paint.Cap.BUTT(无线冒)
//        setStrokeJoin(Paint.Join join)
//        设置线段连接处样式，取值有：Join.MITER（结合处为锐角）、Join.Round(结合处为圆弧)、Join.BEVEL(结合处为直线)
//        setStrokeMiter(float miter)
//        设置笔画的倾斜度，90度拿画笔与30拿画笔，画出来的线条样式肯定是不一样的吧。（事实证明，根本看不出来什么区别好吗……囧……）
//        setPathEffect(PathEffect effect)
//        设置路径样式;取值类型是所有派生自PathEffect的子类：ComposePathEffect, CornerPathEffect, DashPathEffect, DiscretePathEffect, PathDashPathEffect, SumPathEffect
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        paint.setStrokeCap(Paint.Cap.ROUND);
        //设置图形混合模式,相交透明效果
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        //位数越高，图像越逼真，最后一个属性控制
        clearBitmap = Bitmap.createBitmap(preBitmap.getWidth(), preBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        //画布
        mCanvas = new Canvas(clearBitmap);
        mCanvas.drawBitmap(preBitmap,0,0,null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mCanvas.drawPath(path,paint);

        canvas.drawBitmap(clearBitmap,0,0,null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_UP:
                path.lineTo(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(event.getX(),event.getY());
                break;
        }
        invalidate();
        return true;
    }
}
