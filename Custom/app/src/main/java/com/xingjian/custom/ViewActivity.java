package com.xingjian.custom;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * Created by thinkpad on 2018/1/10.
 */

public class ViewActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建的时候，外面就已经存在framelayout
        setContentView(R.layout.activity_view);
        viewFlipper = (ViewFlipper) findViewById(R.id.flipper);
        viewFlipper.setOnTouchListener(touchListener);
        btn = (Button)findViewById(R.id.btn_count);
        //数字增长动画
        final TestBean bean = new TestBean();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectAnimator objectAnimator = ObjectAnimator.ofInt(bean, "age", 1, 100);
                objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int value = (int) valueAnimator.getAnimatedValue();
                        bean.setAge(value);
                        btn.setText(bean.toString());
                    }
                });
                objectAnimator.setDuration(5000);
                objectAnimator.start();
            }
        });

        //
        ListView listView = (ListView) findViewById(R.id.lv_show);
        TextView textView = (TextView) findViewById(R.id.tv_count);
        //获取最顶层的viewgroup
        ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();
        listView.setAdapter(new BasselAdapter(textView,ViewActivity.this,viewGroup));
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {

        private float endX;
        private float startX;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            //判断捕捉到的动作为按下，则设置按下点的x坐标startX
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = motionEvent.getX();
                    break;
                case MotionEvent.ACTION_UP:
                    endX = motionEvent.getX();
                    //由右边到左边滑动屏幕，X值会减小，图片由屏幕右侧进入屏幕
                    if (startX>endX){
                        //进入动画成对
                        viewFlipper.setInAnimation(ViewActivity.this,R.anim.left_to_right);
                        viewFlipper.setOutAnimation(ViewActivity.this,R.anim.right_to_left_out);
                        viewFlipper.showNext();
                    }else {
                        viewFlipper.setInAnimation(ViewActivity.this,R.anim.right_to_left);
                        viewFlipper.setOutAnimation(ViewActivity.this,R.anim.left_to_right_out);
                        viewFlipper.showPrevious();
                    }
            }
            return true;
        }
    };

}
