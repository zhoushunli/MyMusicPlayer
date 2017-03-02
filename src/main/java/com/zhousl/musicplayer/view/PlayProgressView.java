package com.zhousl.musicplayer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.zhousl.musicplayer.R;

/**
 * Created by Administrator on 2017/3/2.
 */

public class PlayProgressView extends View {

    private Paint mPaint;
    private static final int BG_DEFAULT_COLOR = Color.parseColor("#929CB3");
    private static final int PROGRESS_DEFAULT_COLOR = Color.WHITE;
    private int mProgressColor;
    private static final float DOT_RADIUS = 10;
    private int mProgressBgColor;
    private int mDotColor;
    private float mDotRadius;
    private int mDotImage;
    private float mProgress;
    private int measuredHeight;
    private int measuredWidth;
    private float mProgressHeight;

    public PlayProgressView(Context context) {
        super(context);
        init(context, null);
    }

    public PlayProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initPaint();
        initAttr(context, attrs);
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        if (context == null || attrs == null)
            return;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PlayProgressView);
        mProgressBgColor = ta.getColor(R.styleable.PlayProgressView_progress_bg, BG_DEFAULT_COLOR);
        mProgressColor = ta.getColor(R.styleable.PlayProgressView_progress_foreground, PROGRESS_DEFAULT_COLOR);
        mDotColor = ta.getColor(R.styleable.PlayProgressView_dot_color, Color.WHITE);
        mDotRadius = ta.getDimension(R.styleable.PlayProgressView_dot_radius, DOT_RADIUS);
        mDotImage = ta.getResourceId(R.styleable.PlayProgressView_dot_image, 0);
        mProgressHeight = ta.getDimension(R.styleable.PlayProgressView_progress_height, 5);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = measureHeight(heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.getMode(widthMeasureSpec)),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec)));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        measuredHeight = getMeasuredHeight();
        measuredWidth = getMeasuredWidth();
        mDotRadius = Math.min(mDotRadius, measuredHeight / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBG(canvas);
        drawProgress(canvas);
        drawDot(canvas);
    }

    private void drawBG(Canvas canvas) {
        mPaint.setColor(mProgressBgColor);
        mPaint.setStrokeWidth(mProgressHeight);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        canvas.drawLine(mDotRadius, measuredHeight / 2, -mDotRadius + measuredWidth, measuredHeight / 2, mPaint);
    }

    private void drawProgress(Canvas canvas) {
        mPaint.setColor(mProgressColor);
        canvas.drawLine(mDotRadius, measuredHeight / 2, mDotRadius + mProgress * 1.0f / 100 * (measuredWidth - mDotRadius * 2), measuredHeight / 2, mPaint);
    }

    private void drawDot(Canvas canvas) {
        if (mDotImage != 0) {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), mDotImage);
            int height = bmp.getHeight();
            int width = bmp.getWidth();
            canvas.drawBitmap(bmp, Math.max(0, mProgress - width / 2), -height / 2, null);
        } else {
            mPaint.setColor(mDotColor);
            mPaint.setStrokeWidth(measuredHeight);
            mPaint.setStrokeCap(Paint.Cap.BUTT);
            float cx = mProgress / 100 * (measuredWidth - mDotRadius * 2) + mDotRadius;
            canvas.drawCircle(cx, measuredHeight / 2, mDotRadius, mPaint);
        }
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 5;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        }
        return result;
    }

    private int measureWidth(int widthMeasureSpec) {
        int result = getContext().getResources().getDisplayMetrics().widthPixels - 20;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = result * 2 / 3;
        }
        return result;
    }

    long downTime;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                float transP = transProToLocation(mProgress);
                Log.i("movements---","transp="+transP+"========"+"x="+x);
                if (Math.abs(transP - x )< 100){
                    float v = x * 100 / (measuredWidth - mDotRadius * 2);
                    setProgress(v);
                }
                break;
            case MotionEvent.ACTION_UP:
                long dT = System.currentTimeMillis() - downTime;
                if (dT <= 500) {
                    float v = x * 100 / (measuredWidth - mDotRadius * 2);
                    setProgress(v);
                }
                downTime = 0;
                break;
            case MotionEvent.ACTION_HOVER_EXIT:
                event.setAction(MotionEvent.ACTION_CANCEL);
                break;
        }
        return true;
    }

    private float transProToLocation(float progress) {//根椐progress推算出当前对应的坐标
        if (progress < 0)
            progress = 0;
        if (progress > 100)
            progress = 100;
        float curPos = progress / 100 * (measuredWidth - mDotRadius * 2) + mDotRadius;
        return curPos;
    }

    public int getProgressColor() {
        return mProgressColor;
    }

    public void setProgressColor(int mProgressColor) {
        this.mProgressColor = mProgressColor;
    }

    public int getProgressBgColor() {
        return mProgressBgColor;
    }

    public void setProgressBgColor(int mProgressBgColor) {
        this.mProgressBgColor = mProgressBgColor;
    }

    public int getDotColor() {
        return mDotColor;
    }

    public void setDotColor(int mDotColor) {
        this.mDotColor = mDotColor;
    }

    public float getDotRadius() {
        return mDotRadius;
    }

    public void setDotRadius(float mDotRadius) {
        this.mDotRadius = mDotRadius;
    }

    public int getDotImage() {
        return mDotImage;
    }

    public void setDotImage(int mDotImage) {
        this.mDotImage = mDotImage;
    }

    public void setProgress(float progress) {
        if (progress < 0)
            progress = 0;
        else if (progress > 100)
            progress = 100;
        this.mProgress = progress;
        postInvalidate();
    }

    public float getProgress() {
        return mProgress;
    }
}
