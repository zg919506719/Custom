package com.xingjian.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by thinkpad on 2018/1/12.
 */

public class XmlView extends View {
    private String content;
    private int contentColor;
    private TextPaint textPaint;
    private Rect mBound;

    public XmlView(Context context) {
        super(context);
    }

    public XmlView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XmlView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取我们所定义的自定义样式属性
//        defStyleAttr是设置的style样式
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.XmlView, defStyleAttr, 0);
        for (int i = 0; i < array.getIndexCount(); i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.XmlView_titleText:
                    content = array.getString(attr);
                    break;
                case R.styleable.XmlView_titleTextColor:
                    contentColor = array.getColor(attr, Color.BLACK);
                    break;
            }
        }
        array.recycle();
        //绘制文本
        textPaint = new TextPaint();
        mBound = new Rect();
        textPaint.setTextSize(80);
        textPaint.getTextBounds(content, 0, content.length(), mBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        EXACTLY：一般是设置了明确的值或者是MATCH_PARENT
//        AT_MOST：表示子布局限制在一个最大值内，一般为WARP_CONTENT
//        UNSPECIFIED：表示子布局想要多大就多大，很少使用
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = 0;
        int height = 0;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            textPaint.getTextBounds(content, 0, content.length(), mBound);
            width = (int) (getPaddingLeft() + mBound.width() + getPaddingRight());
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            textPaint.getTextBounds(content, 0, content.length(), mBound);
            height = (int) (getPaddingLeft() + mBound.height() + getPaddingRight());
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), textPaint);
        textPaint.setColor(contentColor);
        canvas.drawText(content, getWidth() / 2 - mBound.width() / 2, getHeight() / 2 - mBound.height() / 2, textPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
