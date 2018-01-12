package com.xingjian.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by thinkpad on 2018/1/11.
 */

public class BarrageView extends View {
    private TextPaint textPaint;
    private ArrayList<TextItem> textItems;
    private int mDanmuViewWidth;
    private int mDanmuViewHeight;
    private Random random;

    public BarrageView(Context context) {
        super(context);
    }

    public BarrageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarrageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    String TAG = "haha";

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDanmuViewWidth > 0 && mDanmuViewHeight > 0) {
            for (TextItem item : textItems) {
                canvas.drawText(item.getContent(), item.getmLocX(), item.getmLocY(), textPaint);
            }
        }
    }

    private void init() {
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(30);
        textItems = new ArrayList<>();
        addTextItem("hahahaha");
        handler.postDelayed(r, 1000);
    }

    public void addTextItem(String content) {
        TextItem textItem = new TextItem(content);
        textItem.setmColor(R.color.colorAccent);
        random = new Random();
        textItem.setmLocX(mDanmuViewWidth * random.nextFloat());
        textItem.setmLocY(mDanmuViewHeight * random.nextFloat());
        textItem.setmStep(100);
        textItems.add(textItem);
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            for (TextItem textItem : textItems) {
                textItem.moveToLeft();
            }
            postInvalidate();
            handler.postDelayed(r, 1000);
        }
    };
    private Handler handler = new Handler();


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDanmuViewWidth = w;
        mDanmuViewHeight = h;
    }

    public class TextItem {
        private float mLocX;
        private float mLocY;
        private int mColor;
        private float mStep;
        private String content;

        public TextItem(String content) {
            this.content = content;
        }

        public void moveToLeft() {
            mLocX += mStep;
        }

        public float getmLocX() {
            return mLocX;
        }

        public void setmLocX(float mLocX) {
            this.mLocX = mLocX;
        }

        public float getmLocY() {
            return mLocY;
        }

        public void setmLocY(float mLocY) {
            this.mLocY = mLocY;
        }

        public int getmColor() {
            return mColor;
        }

        public void setmColor(int mColor) {
            this.mColor = mColor;
        }

        public float getmStep() {
            return mStep;
        }

        public void setmStep(float mStep) {
            this.mStep = mStep;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
