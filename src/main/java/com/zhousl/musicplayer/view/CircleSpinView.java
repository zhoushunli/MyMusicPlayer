package com.zhousl.musicplayer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.zhousl.musicplayer.R;

/**
 * Created by Administrator on 2017/3/2.
 */

public class CircleSpinView extends View {

    private Paint mPaint;
    private int mCircleRingColor;
    private boolean mCircleEnable;
    private float mCircleWidth;
    private int measuredWidth;
    private int measuredHeight;
    private int srcRes;
    private Bitmap mCircleImage;

    public CircleSpinView(Context context) {
        super(context);
        init(context, null);
    }

    public CircleSpinView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initPaint();
        initAttrs(context, attrs);
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (context == null || attrs == null)
            return;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleSpinView);
        mCircleRingColor = ta.getColor(R.styleable.CircleSpinView_circle_ring_color, getResources().getColor(R.color.playBg));
        mCircleEnable = ta.getBoolean(R.styleable.CircleSpinView_circle_ring_enable, true);
        mCircleWidth = ta.getDimension(R.styleable.CircleSpinView_circle_ring_width, 20);
        srcRes = ta.getResourceId(R.styleable.CircleSpinView_circle_src, R.mipmap.ic_launcher);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.AT_MOST) {
            size = getResources().getDisplayMetrics().heightPixels * 2 / 3;
        }
        return size;
    }

    private int measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.AT_MOST) {
            size = getResources().getDisplayMetrics().widthPixels * 2 / 3;
        }
        return size;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        measuredWidth = getMeasuredWidth();
        measuredHeight = getMeasuredHeight();
        BitmapFactory.Options op=new BitmapFactory.Options();
        op.inJustDecodeBounds=true;
        int outHeight = op.outHeight;
        int outWidth = op.outWidth;
        //TODO 下次再做
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), srcRes,op);

        if (mCircleImage == null) {
            mCircleImage = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRing(canvas);
        drawCircleImage(canvas);
    }

    private void drawRing(Canvas canvas) {
        mPaint.setColor(mCircleRingColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mCircleWidth);
    }

    private void drawCircleImage(Canvas canvas) {

    }

    public int getCircleRingColor() {
        return mCircleRingColor;
    }

    public void setCircleRingColor(int color) {
        this.mCircleRingColor = color;
    }

    public boolean isCircleEnable() {
        return mCircleEnable;
    }

    public void setCircleEnable(boolean enable) {
        this.mCircleEnable = enable;
    }

    public float getCircleWidth() {
        return mCircleWidth;
    }

    public void setCircleWidth(float circleWidth) {
        this.mCircleWidth = circleWidth;
    }
}
