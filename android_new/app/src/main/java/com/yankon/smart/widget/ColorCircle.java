/* 
 * Copyright (C) 2008 OpenIntents.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yankon.smart.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.yankon.smart.R;


public class ColorCircle extends FrameLayout {

    public static final int MODE_PREDEFINED = 0;
    public static final int MODE_PICKER = 1;

    private OnColorChangedListener mListener;
    int currentColor;
    int mode;
    ColorCircleCenter centerButton;
    boolean touchDownInCenter;
    boolean centerClicked;


    /**
     * Constructor. This version is only needed for instantiating the object
     * manually (not from a layout XML file).
     *
     * @param context
     */
    public ColorCircle(Context context) {
        super(context);
    }

    /**
     * Construct object, initializing with any attributes we understand from a
     * layout file.
     * <p/>
     * These attributes are defined in res/values/attrs.xml .
     * <p/>
     * android.util.AttributeSet, java.util.Map)
     */
    public ColorCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Initializes variables.
     */
    public void init() {
        setDrawingCacheEnabled(true);
        centerButton = (ColorCircleCenter) getChildAt(0);
        switchMode(MODE_PICKER);
    }


    public void switchMode(int m) {
        mode = m;
        switch (mode) {
            case MODE_PREDEFINED:
                setBackgroundResource(R.drawable.color_bg1);
                centerButton.setBackgroundResource(R.drawable.color_mode1);
                break;
            case MODE_PICKER:
                setBackgroundResource(R.drawable.color_bg2);
                centerButton.setBackgroundResource(R.drawable.color_mode2);
                break;
        }
    }


    public void setColor(int color) {
        currentColor = color;
        if (centerButton != null) {
            centerButton.setColor(color);
        }
    }

    public int getColor() {
        return currentColor;
    }

    public void setOnColorChangedListener(
            OnColorChangedListener colorListener) {
        mListener = colorListener;
    }

    int getColorFromCache(MotionEvent event) {
        setDrawingCacheEnabled(true);
        Bitmap bmp = getDrawingCache();
        if (bmp != null) {
            int color;
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (x < 0 || x >= bmp.getWidth() || y < 0 || y >= bmp.getHeight()) {
                color = currentColor;
            } else {
                color = bmp.getPixel(x, y);
            }
            if (color == 0) {
                color = currentColor;
            }
            setDrawingCacheEnabled(false);
            return color;
        }
        setDrawingCacheEnabled(false);
        return currentColor;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX() - getWidth() / 2;
        float y = event.getY() - getHeight() / 2;
        float center_radius = 0;
        if (centerButton != null) {
            center_radius = centerButton.getWidth() / 2;
        }
        boolean inCenter = PointF.length(x, y) <= center_radius;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (inCenter) {
                    touchDownInCenter = true;
                    centerClicked = true;
                    break;
                } else {
                    touchDownInCenter = false;
                    centerClicked = false;
                }
            case MotionEvent.ACTION_MOVE: {
                if (touchDownInCenter) {
                    if (!inCenter) {
                        centerClicked = false;
                    }
                    break;
                }
                if (inCenter)
                    break;

                int newcolor = getColorFromCache(event);
                setColor(newcolor);

                if (mListener != null) {
                    mListener.onColorChanged(this, newcolor);
                }
            }
            break;
            case MotionEvent.ACTION_UP: {
                if (inCenter && centerClicked) {
                    mode = 1 - mode;
                    switchMode(mode);
                    break;
                }
                int newcolor = getColorFromCache(event);
                setColor(newcolor);
                if (mListener != null) {
                    mListener.onColorPicked(this, currentColor);
                }
            }
            break;
        }
        return true;
    }

}
