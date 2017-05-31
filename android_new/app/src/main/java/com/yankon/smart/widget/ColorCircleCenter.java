package com.yankon.smart.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Evan on 15/3/15.
 */
public class ColorCircleCenter extends View {
    int currentColor;
    Paint mPaint;

    public ColorCircleCenter(Context context) {
        super(context);
    }

    public ColorCircleCenter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorCircleCenter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        canvas.translate(w / 2, w / 2);
        if (mPaint == null) {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(5);
        }
        mPaint.setColor(currentColor);
        canvas.drawCircle(0, 0, w / 2 - 10, mPaint);
    }

    public void setColor(int color) {
        currentColor = color;
        invalidate();
    }
}
