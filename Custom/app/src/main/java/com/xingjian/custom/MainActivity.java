package com.xingjian.custom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView tv = (TextView) findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//旋转着消失
                //true 表示使用animation的intrpolator
                AnimationSet animationSet = new AnimationSet(true);
                AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                //设置动画监听
//                alphaAnimation.setAnimationListener
                RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animationSet.addAnimation(alphaAnimation);
                animationSet.addAnimation(rotateAnimation);
//www.cnblogs.com/wikiki/p/4211870.html
//                详细讲解了interpolator的用法，动画的变化速率，先快，后减速等
//                animationSet.setInterpolator(new CycleInterpolator());
                //animationset添加了两个动画，两个动画在规定时间内同时进行，如果设置时间，则前面的两个事件没用
                animationSet.setDuration(4000);
                tv.startAnimation(animationSet);
            }
        });
        TextView tv1 = (TextView) findViewById(R.id.tv1);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//旋转
            }
        });
        TextView tv2 = (TextView) findViewById(R.id.tv2);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//缩放
            }
        });
        TextView tv3 = (TextView) findViewById(R.id.tv3);
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//移动
            }
        });
    }
}
