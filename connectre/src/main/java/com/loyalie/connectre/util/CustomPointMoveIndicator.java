package com.loyalie.connectre.util;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.ColorInt;

import com.kekstudio.dachshundtablayout.indicators.AnimatedIndicatorInterface;

public class CustomPointMoveIndicator implements AnimatedIndicatorInterface, ValueAnimator.AnimatorUpdateListener {
    private Paint paint;
    private RectF rectF;
    private Rect rect;
    private int height;
    private ValueAnimator valueAnimatorLeft;
    private ValueAnimator valueAnimatorRight;
    private CustomTabLayout dachshundTabLayout;
    private AccelerateInterpolator accelerateInterpolator;
    private DecelerateInterpolator decelerateInterpolator;
    private int leftX;
    private int rightX;

    public CustomPointMoveIndicator(CustomTabLayout dachshundTabLayout) {
        this.dachshundTabLayout = dachshundTabLayout;
        this.valueAnimatorLeft = new ValueAnimator();
        this.valueAnimatorLeft.setDuration(500L);
        this.valueAnimatorLeft.addUpdateListener(this);
        this.valueAnimatorRight = new ValueAnimator();
        this.valueAnimatorRight.setDuration(500L);
        this.valueAnimatorRight.addUpdateListener(this);
        this.accelerateInterpolator = new AccelerateInterpolator();
        this.decelerateInterpolator = new DecelerateInterpolator();
        this.rectF = new RectF();
        this.rect = new Rect();
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Paint.Style.FILL);
        this.leftX = (int) dachshundTabLayout.getChildXCenter(dachshundTabLayout.getCurrentPosition());
        this.rightX = this.leftX;
    }

    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.leftX = (Integer) this.valueAnimatorLeft.getAnimatedValue();
        this.rightX = (Integer) this.valueAnimatorRight.getAnimatedValue();
        this.rect.top = this.dachshundTabLayout.getHeight() - this.height;
        this.rect.left = this.leftX - this.height / 2;
        this.rect.right = this.rightX + this.height / 2;
        //this.rect.bottom = 0;
        this.rect.bottom = this.dachshundTabLayout.getHeight();
        this.dachshundTabLayout.invalidate(this.rect);
    }

    public void setSelectedTabIndicatorColor(@ColorInt int color) {
        this.paint.setColor(color);
    }

    public void setSelectedTabIndicatorHeight(int height) {
        this.height = height;
    }

    public void setIntValues(int startXLeft, int endXLeft, int startXCenter, int endXCenter, int startXRight, int endXRight) {
        boolean toRight = endXCenter - startXCenter >= 0;
        if (toRight) {
            this.valueAnimatorLeft.setInterpolator(this.accelerateInterpolator);
            this.valueAnimatorRight.setInterpolator(this.decelerateInterpolator);
        } else {
            this.valueAnimatorLeft.setInterpolator(this.decelerateInterpolator);
            this.valueAnimatorRight.setInterpolator(this.accelerateInterpolator);
        }

        this.valueAnimatorLeft.setIntValues(new int[]{startXCenter, endXCenter});
        this.valueAnimatorRight.setIntValues(new int[]{startXCenter, endXCenter});
    }

    public void setCurrentPlayTime(long currentPlayTime) {
        this.valueAnimatorLeft.setCurrentPlayTime(currentPlayTime);
        this.valueAnimatorRight.setCurrentPlayTime(currentPlayTime);
    }

    public void draw(Canvas canvas) {
        this.rectF.top = (float) (this.dachshundTabLayout.getHeight() - this.height / 2);
        this.rectF.left = (float) (this.leftX - this.height);
        this.rectF.right = (float) (this.rightX + this.height);
        this.rectF.bottom = (float) this.dachshundTabLayout.getHeight() + this.height;
        canvas.drawArc(this.rectF, 180, 180, false, this.paint);
        // canvas.drawRoundRect(this.rectF, (float)this.height, (float)this.height, this.paint);
    }

    public long getDuration() {
        return this.valueAnimatorLeft.getDuration();
    }
}
