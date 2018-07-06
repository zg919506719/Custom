package com.xingjian.custom;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.luying.ui.Capture1Activity;
import com.xingjian.custom.obsessive.zbar.CaptureActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initService();
        initHandle();
    }

    private Messenger handleMessagen;
    private ServiceConnection handleConn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            handleMessagen = new Messenger(iBinder);
            try {
                Message message = new Message();
                message.what = 1;
                handleMessagen.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private void initHandle() {
        Intent intent = new Intent(MainActivity.this, HandleService.class);
        bindService(intent, handleConn, Context.BIND_AUTO_CREATE);
    }

    private IMusic iMusic;
    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iMusic = IMusic.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private void initService() {
//        Intent intent = new Intent(MainActivity.this, MusicService.class);
        Intent intent = new Intent();
        intent.setPackage("com.xingjian.custom");
        intent.setAction("com.service.custom");
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        Button btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iMusic != null) {
                    try {
                        iMusic.start("haha");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        Button btn_stop = (Button) findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iMusic != null) {
                    try {
                        iMusic.stop("");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void initView() {
        final TextView tv = (TextView) findViewById(R.id.tv);
//        1.Tweened Animations：该类Animations提供了旋转、移动、伸展和淡出等效果。Alpha——淡入淡出，Scale——缩放效果，Rotate——旋转，Translate——移动效果。
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
                TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 1f);
                animationSet.addAnimation(translateAnimation);
                animationSet.setDuration(4000);
                tv1.startAnimation(animationSet);
                //设置控件的缩放等级
//                tv1.setTextScaleX(0.5f);
            }
        });
        final TextView tv2 = (TextView) findViewById(R.id.tv2);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//移动缩放着旋转消失，draw文件
                //如果分开的set没有设置时间，将执行每个动画的时间
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.test_all);
                tv2.startAnimation(animation);
            }
        });
        final TextView tv3 = (TextView) findViewById(R.id.tv3);
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//        2.Frame-by-frame Animations
                AnimationDrawable background = (AnimationDrawable) tv3.getBackground();
                if (background.isRunning()) {
                    background.stop();
                } else {
                    background.start();
                }
            }
        });
        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//页面之间的跳转动画
                startActivity(new Intent(MainActivity.this, ViewActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        Button btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ScrollClashActivity.class));
            }
        });
        Button btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, WebActivity.class));
            }
        });
        Button btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, WindowActivity.class));
            }
        });
        //属性动画 http://blog.csdn.net/lmj623565791/article/details/38067475
        //LayoutTransition布局动画 http://blog.csdn.net/lmj623565791/article/details/38092093
        final TextView tv4 = (TextView) findViewById(R.id.tv4);
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ValueAnimator animator = ValueAnimator.ofFloat(0, 200f);
                animator.setTarget(tv4);
                animator.setDuration(10000).start();
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        tv4.setTranslationY((Float) valueAnimator.getAnimatedValue());
                    }
                });
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        //将view放在最外层，5.0后添加了Z轴
                        tv4.bringToFront();
                    }
                });
            }
        });
        final TextView tv5 = (TextView) findViewById(R.id.tv5);
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animator anim = AnimatorInflater.loadAnimator(MainActivity.this, R.animator.test_animator);
                anim.setTarget(tv5);
                anim.start();
            }
        });
        Button btn_scan = (Button) findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CaptureActivity.class));
            }
        });
        Button btn_scan1 = (Button) findViewById(R.id.btn_scan1);
        btn_scan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Capture1Activity.class));
            }
        });
    }
}
