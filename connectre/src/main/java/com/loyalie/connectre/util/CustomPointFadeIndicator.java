package com.loyalie.connectre.util;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.animation.LinearInterpolator;

import androidx.annotation.ColorInt;

import com.kekstudio.dachshundtablayout.indicators.AnimatedIndicatorInterface;

public class CustomPointFadeIndicator implements AnimatedIndicatorInterface, ValueAnimator.AnimatorUpdateListener {
    private Paint paint;
    private int height;
    private ValueAnimator valueAnimator;
    private CustomTabLayout dachshundTabLayout;
    private int startX;
    private int endX;
    private int originColor;
    private int startColor;
    private int endColor;

    public CustomPointFadeIndicator(CustomTabLayout dachshundTabLayout) {
        this.dachshundTabLayout = dachshundTabLayout;
        this.valueAnimator = new ValueAnimator();
        this.valueAnimator.setInterpolator(new LinearInterpolator());
        this.valueAnimator.setDuration(500L);
        this.valueAnimator.addUpdateListener(this);
        this.valueAnimator.setIntValues(new int[]{0, 255});
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Paint.Style.FILL);
        this.startX = (int) dachshundTabLayout.getChildXCenter(dachshundTabLayout.getCurrentPosition());
    }

    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        int startAlpha = 255 - (Integer) valueAnimator.getAnimatedValue();
        this.startColor = Color.argb(startAlpha, Color.red(this.originColor), Color.green(this.originColor), Color.blue(this.originColor));
        int endAlpha = (Integer) valueAnimator.getAnimatedValue();
        this.endColor = Color.argb(endAlpha, Color.red(this.originColor), Color.green(this.originColor), Color.blue(this.originColor));
        this.dachshundTabLayout.invalidate();
    }

    public void setSelectedTabIndicatorColor(@ColorInt int color) {
        this.originColor = color;
        this.startColor = color;
        this.endColor = 0;
    }

    public void setSelectedTabIndicatorHeight(int height) {
        this.height = height;
    }

    public void setIntValues(int startXLeft, int endXLeft, int startXCenter, int endXCenter, int startXRight, int endXRight) {
        this.startX = startXCenter;
        this.endX = endXCenter;
    }

    public void setCurrentPlayTime(long currentPlayTime) {
        this.valueAnimator.setCurrentPlayTime(currentPlayTime);
    }

    public void draw(Canvas canvas) {

        //canvas.drawArc((float)this.endX, (float)(canvas.getHeight() - this.height / 2),(float)(this.height / 2),0,180,180,false,paint);
        this.paint.setColor(this.startColor);
        canvas.drawCircle((float) this.startX, (float) (canvas.getHeight() - this.height / 2), (float) (this.height / 2), this.paint);
        this.paint.setColor(this.endColor);
        canvas.drawCircle((float) this.endX, (float) (canvas.getHeight() - this.height / 2), (float) (this.height / 2), this.paint);
    }

    public long getDuration() {
        return this.valueAnimator.getDuration();
    }
}

