package com.tsy.hencoder_view_mock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by tangsiyuan on 2017/10/15.
 */

public class NumberView extends View {

    private int mNumber;
    private int mOldNumber;
    private Paint mPaint = new Paint();

    public NumberView(Context context) {
        super(context);
    }

    public NumberView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 设置数字
     * @param number
     */
    public void setNumber(int number) {
        mNumber = number;
        mOldNumber = number;
    }

    /**
     * 数字增加
     * @param value
     */
    public void add(int value) {
        mOldNumber = mNumber;
        mNumber = mNumber + value;
    }

    /**
     * 数字减少
     * @param value
     */
    public void min(int value) {
        if(mNumber < value) {
            throw new IllegalArgumentException("number can not be negative");
        }
        mOldNumber = mNumber;
        mNumber = mNumber - value;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

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

        return chars;
    }
}
