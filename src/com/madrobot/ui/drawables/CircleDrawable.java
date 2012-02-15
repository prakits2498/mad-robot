package com.madrobot.ui.drawables;

import android.graphics.*;
import android.graphics.drawable.Drawable;

public class CircleDrawable extends Drawable
{

    public CircleDrawable(int radius, boolean soft)
    {
        mRadius = radius;
        mOvalRect = new RectF(0.0F, 0.0F, radius, radius);
        mPaint = new Paint(1);
        mPaint.setColor(0xff000000);
        if(soft)
            mPaint.setMaskFilter(new BlurMaskFilter(2.0F, android.graphics.BlurMaskFilter.Blur.NORMAL));
    }

    public void draw(Canvas canvas)
    {
        RectF rect = new RectF(getBounds());
        canvas.drawColor(mBackgroundColor);
        canvas.drawCircle(rect.centerX(), rect.centerY(), mRadius / 2, mPaint);
    }

    public void setBackgroundColor(int color)
    {
        mBackgroundColor = color;
    }

    public int getOpacity()
    {
        return -1;
    }

    public void setAlpha(int i)
    {
    }

    public void setColorFilter(ColorFilter colorfilter)
    {
    }

    Paint mPaint;
    int mRadius;
    int mBackgroundColor;
    RectF mOvalRect;
}
