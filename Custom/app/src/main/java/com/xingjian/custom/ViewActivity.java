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

    //程序界面不可见的监听
    //onStop方法中去释放一些资源，比如取消网络连接和注销广播接收器
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        switch (level){
            case TRIM_MEMORY_UI_HIDDEN:
                //进行UI相关资源释放操作
//                TRIM_MEMORY_RUNNING_MODERATE
//                表示应用程序正常运行，并且不会被杀掉。但是目前手机的内存已经有点低了，系统可能会开始根据LRU缓存规则来去杀死进程了。
//                TRIM_MEMORY_RUNNING_LOW
//                表示应用程序正常运行，并且不会被杀掉。但是目前手机的内存已经非常低了，我们应该去释放掉一些不必要的资源以提升系统的性能，
//                同时这也会直接影响到我们应用程序的性能。
//                TRIM_MEMORY_RUNNING_CRITICAL
//                表示应用程序仍然正常运行，但是系统已经根据LRU缓存规则杀掉了大部分缓存的进程了。
//                这个时候我们应当尽可能地去释放任何不必要的资源，不然的话系统可能会继续杀掉所有缓存中的进程，
//                并且开始杀掉一些本来应当保持运行的进程，比如说后台运行的服务。
//                TRIM_MEMORY_BACKGROUND
//                表示手机目前内存已经很低了，系统准备开始根据LRU缓存来清理进程。
//                这个时候我们的程序在LRU缓存列表的最近位置，是不太可能被清理掉的，
//                但这时去释放掉一些比较容易恢复的资源能够让手机的内存变得比较充足，
//                从而让我们的程序更长时间地保留在缓存当中，这样当用户返回我们的程序时会感觉非常顺畅，而不是经历了一次重新启动的过程。
//                TRIM_MEMORY_MODERATE
//                表示手机目前内存已经很低了，并且我们的程序处于LRU缓存列表的中间位置，
//                如果手机内存还得不到进一步释放的话，那么我们的程序就有被系统杀掉的风险了。
//                TRIM_MEMORY_COMPLETE
//                表示手机目前内存已经很低了，并且我们的程序处于LRU缓存列表的最边缘位置，
//                系统会最优先考虑杀掉我们的应用程序，在这个时候应当尽可能地把一切可以释放的东西都进行释放。
                break;
        }
    }
}
