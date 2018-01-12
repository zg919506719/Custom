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
        this(context, attrs,0);
    }

    public XmlView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取我们所定义的自定义样式属性
//        defStyleAttr是设置的style样式
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.XmlView, defStyleAttr, 0);
        for (int i = 0; i < array.getIndexCount(); i++) {
            int attr = array.getIndex(i);
            switch (attr){
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
        textPaint.getTextBounds(content,0,content.length(),mBound);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),textPaint);
        textPaint.setColor(contentColor);
        canvas.drawText(content,getWidth()/2-mBound.width()/2,getHeight()/2-mBound.height()/2,textPaint);
    }
}
