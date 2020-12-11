package com.loyalie.connectre.custom_views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.loyalie.connectre.R;


public class ArcProgress extends View {

    private static final String TAG = "ArcProgressBar";
    /**
     * The width of the arc
     */
    private int mStrokeWidth = dp2px(8);
    /**
     * The starting angle of the arc
     */
    private float mStartAngle = 135;
    /**
     * The angle between the starting point and the ending point
     */
    private float mAngleSize = 270;
    /**
     * Arc background color
     */
    private int mArcBgColor = Color.parseColor("#d75435");
    /**
     * The largest progress, used to calculate the ratio of the progress to the included angle
     */
    private float mMaxProgress = 500;
    /**
     * The angle between the starting point corresponding to the current progress and the current progress angle
     */
    private float mCurrentAngleSize = 0;
    /**
     * Current progress
     */
    private float mCurrentProgress = 0;
    /**
     * The execution time of the animation
     */
    private long mDuration = 3000;
    /**
     * The color of the progress arc
     */
    private int mProgressColor = Color.BLACK;
    /**
     * First line of text
     */
    private String mFirstText = "";
    /**
     * The color of the first line of text
     */
    private int mFirstTextColor = Color.BLACK;
    /**
     * The font size of the first line of text
     */
    private float mFirstTextSize = 45f;
    /**
     * The second line of text
     */
    private String mSecondText = "";
    /**
     * The color of the second line of text
     */
    private int mSecondTextColor = Color.parseColor("#bfbfbf");
    /**
     * The font size of the second line of text
     */
    private float mSecondTextSize = 16f;
int fontFamilyId=0;
    private Typeface secondTextFont = ResourcesCompat.getFont(this.getContext(), R.font.sinkinsans_600semibold);
    private Typeface firstTextFont = ResourcesCompat.getFont(this.getContext(), R.font.sinkinsans_400regular);

    public ArcProgress(Context context) {
        super(context, null);
    }

    public ArcProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);

    }

    public ArcProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
    }


    /**
     * Set initialization parameters
     *
     * @param context
     * @param attrs
     */
    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ArcProgressBar);
        mMaxProgress = array.getFloat(R.styleable.ArcProgressBar_arc_max_progress, 500f);
        mArcBgColor = array.getColor(R.styleable.ArcProgressBar_arc_bg_color, Color.YELLOW);
        mStrokeWidth = dp2px(array.getDimension(R.styleable.ArcProgressBar_arc_stroke_width, 12f));
        mCurrentProgress = array.getFloat(R.styleable.ArcProgressBar_arc_progress, 300f);
        mProgressColor = array.getColor(R.styleable.ArcProgressBar_arc_progress_color, Color.RED);
        mFirstText = array.getString(R.styleable.ArcProgressBar_arc_first_text);
        mFirstTextSize = dp2px(array.getDimension(R.styleable.ArcProgressBar_arc_first_text_size, 20f));
        mFirstTextColor = array.getColor(R.styleable.ArcProgressBar_arc_first_text_color, Color.RED);
        mSecondText = array.getString(R.styleable.ArcProgressBar_arc_second_text);
        mSecondTextSize = dp2px(array.getDimension(R.styleable.ArcProgressBar_arc_second_text_size, 20f));
        mSecondTextColor = array.getColor(R.styleable.ArcProgressBar_arc_second_text_color, Color.RED);
        mAngleSize = array.getFloat(R.styleable.ArcProgressBar_arc_angle_size, 270f);
        mStartAngle = array.getFloat(R.styleable.ArcProgressBar_arc_start_angle, 135f);
         fontFamilyId = array.getResourceId(R.styleable.LoadingButton_android_fontFamily, 0);

        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerX = getWidth() /2;
        RectF rectF = new RectF();
        rectF.left = mStrokeWidth;
        rectF.top = mStrokeWidth;
        rectF.right = centerX * 2 - mStrokeWidth;
        rectF.bottom = centerX * 2 - mStrokeWidth;
        //Draw the outermost arc
        drawArcBg(canvas, rectF);
        //Drawing progress
        drawArcProgress(canvas, rectF);
        //Draw the first level text
        drawFirstText(canvas, centerX);
        //Draw the second level text
        drawSecondText(canvas, centerX);

    }


    /**
     * Draw the first arc
     *
     * @param canvas
     * @param rectF
     */
    private void drawArcBg(Canvas canvas, RectF rectF) {
        Paint mPaint = new Paint();
        //The filling style of the brush, Paint.Style.FILL fills the interior; Paint.Style.FILL_AND_STROKE fills the interior and stroke; Paint.Style.STROKE strokes
        mPaint.setStyle(Paint.Style.STROKE);
        //The width of the arc
        mPaint.setStrokeWidth(mStrokeWidth);
        //Anti-aliasing
        mPaint.setAntiAlias(true);
        //The color of the pen
        mPaint.setColor(mArcBgColor);
        //The style of the brush Paint.Cap.Round round, Cap.SQUARE square
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //Start drawing arc
        canvas.drawArc(rectF, mStartAngle, mAngleSize, false, mPaint);
    }

    /**
     * Draw the arc of progress
     *
     * @param canvas
     * @param rectF
     */
    private void drawArcProgress(Canvas canvas, RectF rectF) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mStrokeWidth);
        paint.setColor(mProgressColor);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(rectF, mStartAngle, mCurrentAngleSize, false, paint);
    }


    /**
     * Draw the first level text
     *
     * @param canvas  brush
     * @param centerX position
     */
    private void drawFirstText(Canvas canvas, float centerX) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mFirstTextColor);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(mFirstTextSize);
        paint.setTypeface(firstTextFont);

        Rect firstTextBounds = new Rect(3,154,3,1);

        paint.getTextBounds(mFirstText, 0, mFirstText.length(), firstTextBounds);

        canvas.drawText(mFirstText, centerX, firstTextBounds.height() / 2 + getHeight() * 2 / 5, paint);
    }

    /**
     * Draw second level text
     *
     * @param canvas  brush
     * @param centerX text
     */
    private void drawSecondText(Canvas canvas, float centerX) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mSecondTextColor);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(mSecondTextSize);
        paint.setTypeface(secondTextFont);
        Rect bounds = new Rect();

        paint.getTextBounds(mSecondText, 0, mSecondText.length(), bounds);
        canvas.drawText(mSecondText, centerX, getHeight() / 2 + bounds.height() / 2 +
                getFontHeight(mSecondText, mSecondTextSize), paint);
    }

    /**
     * Set the maximum progress
     *
     * @param progress
     */
    public void setMaxProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("Progress value can not be less than 0 ");
        }
        mMaxProgress = progress;
    }

    /**
     * Set current progress
     *
     * @param progress
     */
    public void setProgress(float progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("Progress value can not be less than 0");
        }
        if (progress > mMaxProgress) {
            progress = mMaxProgress;
        }
        mCurrentProgress = progress;
        float size = mCurrentProgress / mMaxProgress;
        mCurrentAngleSize = (int) (mAngleSize * size);
        setAnimator(0, mCurrentAngleSize);
    }

    /**
     * Set the color of the progress arc
     *
     * @param color
     */
    public void setProgressColor(int color) {
        if (color == 0) {
            throw new IllegalArgumentException("Color can no be 0");
        }
        mProgressColor = color;
    }

    /**
     * Set the color of the arc
     *
     * @param color
     */
    public void setArcBgColor(int color) {
        if (color == 0) {
            throw new IllegalArgumentException("Color can no be 0");
        }
        mArcBgColor = color;
    }

    /**
     * Set the width of the arc
     *
     * @param strokeWidth
     */
    public void setStrokeWidth(int strokeWidth) {
        if (strokeWidth < 0) {
            throw new IllegalArgumentException("strokeWidth value can not be less than 0");
        }
        mStrokeWidth = dp2px(strokeWidth);
    }

    /**
     * Set the execution time of the animation
     *
     * @param duration
     */
    public void setAnimatorDuration(long duration) {
        if (duration < 0) {
            throw new IllegalArgumentException("Duration value can not be less than 0");
        }
        mDuration = duration;
    }

    /**
     * Set the first line of text
     *
     * @param text
     */
    public void setFirstText(String text) {
        mFirstText = text;
    }

    /**
     * Set the color of the first line of text
     *
     * @param color
     */
    public void setFirstTextColor(int color) {
        if (color <= 0) {
            throw new IllegalArgumentException("Color value can not be less than 0");
        }
        mFirstTextColor = color;
    }

    /**
     * Set the size of the first line of text
     *
     * @param textSize
     */
    public void setFirstTextSize(float textSize) {
        if (textSize <= 0) {
            throw new IllegalArgumentException("textSize can not be less than 0");
        }
        mFirstTextSize = textSize;
    }

    /**
     * Set the second line of text
     *
     * @param text
     */
    public void setSecondText(String text) {
        mSecondText = text;
    }

    /**
     * Set the color of the second line of text
     *
     * @param color
     */
    public void setSecondTextColor(int color) {
        if (color == 0) {
            throw new IllegalArgumentException("Color value can not be less than 0");
        }
        mSecondTextColor = color;
    }

    /**
     * Set the size of the second line of text
     *
     * @param textSize
     */
    public void setSecondTextSize(float textSize) {
        if (textSize <= 0) {
            throw new IllegalArgumentException("textSize can not be less than 0");
        }
        mSecondTextSize = textSize;
    }



    /**
     * Set the starting angle of the arc
     *
     * @param startAngle
     */
    public void setStartAngle(int startAngle) {
        mStartAngle = startAngle;
    }

    /**
     * Set the size of the arc from the start angle to the end angle
     *
     * @param angleSize
     */
    public void setAngleSize(int angleSize) {
        mAngleSize = angleSize;
    }

    /**
     * dp to px
     *
     * @param dp
     * @return
     */
    private int dp2px(float dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f * (dp >= 0 ? 1 : -1));
    }

    /**
     * Set up animation
     *
     * @param start  start position
     * @param target end position
     */
    private void setAnimator(float start, float target) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(start, target);
        valueAnimator.setDuration(mDuration);
        valueAnimator.setTarget(mCurrentAngleSize);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCurrentAngleSize = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
    }

    /**
     * Measure the height of the font
     *
     * @param textStr
     * @param fontSize
     * @return
     */
    private float getFontHeight(String textStr, float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Rect bounds = new Rect();
        paint.getTextBounds(textStr, 0, textStr.length(), bounds);
        return bounds.height();
    }

}