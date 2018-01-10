package com.xingjian.custom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
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
                //起始透明度
                AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                //设置动画监听
//                alphaAnimation.setAnimationListener
                //参数1：从哪个旋转角度开始
                //参数2：转到什么角度
                //后4个参数用于设置围绕着旋转的圆的圆心在哪里
                //参数3：确定x轴坐标的类型，有ABSOLUT绝对坐标、RELATIVE_TO_SELF相对于自身坐标、RELATIVE_TO_PARENT相对于父控件的坐标
                //参数4：x轴的值，0.5f表明是以自身这个控件的一半长度为x轴
                //参数5：确定y轴坐标的类型
                //参数6：y轴的值，0.5f表明是以自身这个控件的一半长度为x轴
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
        final TextView tv1 = (TextView) findViewById(R.id.tv1);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//旋转
                AnimationSet animationSet = new AnimationSet(true);
                //参数1：x轴的初始值,开始动画的x轴大小
                //参数2：x轴收缩后的值
                //参数3：y轴的初始值,开始动画的轴大小
                //参数4：y轴收缩后的值
                //参数5：确定x轴坐标的类型
                //参数6：x轴的值，0.5f表明是以自身这个控件的一半长度为x轴
                //参数7：确定y轴坐标的类型
                //参数8：y轴的值，0.5f表明是以自身这个控件的一半长度为x轴
                ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0.1f, 1, 0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animationSet.addAnimation(scaleAnimation);
                //参数1～2：x轴的开始位置
                //参数3～4：x轴的结束位置
                //参数5～6：y轴的开始位置
                //参数7～8：y轴的结束位置
                //ABSOLUT绝对坐标、RELATIVE_TO_SELF相对于自身坐标、RELATIVE_TO_PARENT相对于父控件的坐标
                TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f,
                        Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 1f);
                animationSet.addAnimation(translateAnimation);
                animationSet.setDuration(4000);
                tv1.startAnimation(animationSet);
                //设置控件的缩放等级
//                tv1.setTextScaleX(0.5f);
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
