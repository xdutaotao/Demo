package com.gzfgeh.text;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Description:
 * Created by guzhenfu on 2018/1/17.
 */

public class CustomView extends View {
    private static final String LOGGER = "CustomView";

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        }, 2000);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                requestLayout();
            }
        }, 4000);

        postDelayed(new Runnable() {
            @Override
            public void run() {

                ObjectAnimator.ofFloat(CustomView.this, "scaleX",1, 2 ).start();

            }
        }, 6000);

        postDelayed(new Runnable() {
            @Override
            public void run() {



            }
        }, 6000);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.d(LOGGER, "onMeasure");

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        Log.d(LOGGER, "onLayout");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.d(LOGGER, "onDraw--" + getWidth());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Log.d(LOGGER, "onSizeChanged");
    }
}
