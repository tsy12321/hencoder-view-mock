package com.tsy.hencoder_view_mock;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mLlLayout;
    private Button mBtnAdd;
    private Button mBtnMin;

    private int mNumber = 2021;
    private int mOldNumber = mNumber;

    private ArrayList<TextView> mTxtNumbers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLlLayout = (LinearLayout) findViewById(R.id.ll_layout);
        mBtnAdd = (Button) findViewById(R.id.btn_add);
        mBtnMin = (Button) findViewById(R.id.btn_min);

        mBtnAdd.setOnClickListener(this);
        mBtnMin.setOnClickListener(this);

        ArrayList<Integer> numbers = numberToChars(mNumber);
        for(int i=0; i < numbers.size(); ++i) {
            TextView textView = new TextView(this);
            textView.setText("" + numbers.get(i));
            mLlLayout.addView(textView);
            mTxtNumbers.add(textView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                add(1);
                break;

            case R.id.btn_min:
                min(1);
                break;
        }
    }

    private void add(int value) {
        mOldNumber = mNumber;
        mNumber += value;

        final ArrayList<Integer> numbers = numberToChars(mNumber);
        ArrayList<Integer> oldNumbers = numberToChars(mOldNumber);

        for(int i = 0; i < numbers.size(); ++ i) {
            if(!numbers.get(i).equals(oldNumbers.get(i))) {
                startAnimator(mTxtNumbers.get(i), numbers.get(i), true);
            }
        }
    }

    private void min(int value) {
        mOldNumber = mNumber;
        mNumber -= value;

        final ArrayList<Integer> numbers = numberToChars(mNumber);
        ArrayList<Integer> oldNumbers = numberToChars(mOldNumber);

        for(int i = 0; i < numbers.size(); ++ i) {
            if(!numbers.get(i).equals(oldNumbers.get(i))) {
                startAnimator(mTxtNumbers.get(i), numbers.get(i), false);
            }
        }
    }

    /**
     * 从中间往上动画、隐藏 -> 隐藏 -> 改变数字 -> 显示、从下往中间动画
     * @param view 当前变动的view
     * @param newNumber 变动后的数字
     * @param up 方向向上还是向下
     */
    private void startAnimator(final TextView view, final int newNumber, boolean up) {
        int offset = 30;
        if(up) {
            offset *= -1;
        }
        final int finalOffset = offset;
        int duration = 300;

        //当前位置
        final float curY = view.getY();

        //动画1 Y偏移offset 然后隐藏、更改数字、修改Y位置、显示
        PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("Y", curY, curY + offset);
        PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.8f);

        ObjectAnimator animator1 = ObjectAnimator
                .ofPropertyValuesHolder(view, holder1, holder2)
                .setDuration(duration);
        animator1.setInterpolator(new AccelerateInterpolator());
        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.INVISIBLE);
                view.setText("" + newNumber);
                view.setY(curY - finalOffset);
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        //动画2 Y偏移回中间位置
        holder1 = PropertyValuesHolder.ofFloat("Y", curY - offset, curY);
        holder2 = PropertyValuesHolder.ofFloat("alpha", 0.8f, 1.0f);

        ObjectAnimator animator2 = ObjectAnimator
                .ofPropertyValuesHolder(view, holder1, holder2)
                .setDuration(duration);
        animator2.setInterpolator(new AccelerateInterpolator());

        //两个动画依次执行
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(animator1, animator2);
        animatorSet.start();
    }

    /**
     * 数字转单个字符数组
     */
    private ArrayList<Integer> numberToChars(int number) {
        ArrayList<Integer> chars = new ArrayList<>();

        int i = 0;  //防止死循环
        while(number > 0 && i++<10) {
            chars.add(number % 10);
            number /= 10;
        }

        Collections.reverse(chars);

        return chars;
    }
}
