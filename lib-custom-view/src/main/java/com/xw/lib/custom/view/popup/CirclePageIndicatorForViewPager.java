package com.xw.lib.custom.view.popup;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.xw.lib.custom.view.R;


/**
 * Created by HuangXiaoHong on 15-8-7.
 */
public class CirclePageIndicatorForViewPager extends View implements ViewPager.OnPageChangeListener {

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mRadius;
    private float mSpace;
    private int mWhichChild = 0;
    private int mChildCount = 0;

    private ViewPager viewPager;

    // 保存各个圆x轴上中心点的位置值
    private float[] circlePivotX;

    public CirclePageIndicatorForViewPager(Context context) {
        super(context);
    }

    public CirclePageIndicatorForViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CirclePageIndicator);
        mRadius = typedArray.getDimension(R.styleable.CirclePageIndicator_radius, 10);
        mSpace = typedArray.getDimension(R.styleable.CirclePageIndicator_space, 10);
        typedArray.recycle();
    }

    public void setViewPager(ViewPager viewPager) {
        if (viewPager == null || viewPager.getAdapter() == null) {
            return;
        }
        this.viewPager = viewPager;
        this.mChildCount = this.viewPager.getAdapter().getCount();
        this.viewPager.addOnPageChangeListener(this);

        circlePivotX = new float[mChildCount];
        for (int i = 0; i < circlePivotX.length; i++) {
            circlePivotX[i] = mRadius + (mRadius * 2 + mSpace) * i;
        }
        mWhichChild = viewPager.getCurrentItem();
        requestLayout();
    }

    public void close() {
        viewPager = null;
        mChildCount = 0;
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        Timber.v("---getWidth---" + getWidth());
//        Timber.v("---getHeight---" + getHeight());
        if (mChildCount < 0) {
            return;
        }
        canvas.drawColor(Color.TRANSPARENT);
        for (int i = 0; i < mChildCount; i++) {
            if (mWhichChild == i) { // 画选中的圆
                mPaint.setARGB(255, 62, 196, 80);
                drawCircle(i, getWidth(), getHeight(), mPaint, canvas);
            } else {
                mPaint.setARGB(100, 255, 255, 255);
                drawCircle(i, getWidth(), getHeight(), mPaint, canvas);
            }
        }

    }

    private void drawCircle(int whichChild, int viewWidth, int viewHeight, Paint paint, Canvas canvas) {
        // 圆和圆间距占用的总宽度
        float contentWidth = mChildCount * mRadius * 2 + (mChildCount - 1) * mSpace;
        // 左边距
        float leftPadding = viewWidth - contentWidth;
        canvas.drawCircle(leftPadding + circlePivotX[whichChild], viewHeight / 2, mRadius, paint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = measureWidth(widthMeasureSpec);
        int measureHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(measureWidth, measureHeight);
    }

    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if ((specMode == MeasureSpec.EXACTLY)) {
            //We were told how big to be
            result = specSize;
        } else {
            //Measure the width
            result = (int) (getPaddingLeft() + getPaddingRight()
                    + (mChildCount * 2 * mRadius) + (mChildCount - 1) * mSpace) + 1;
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            //We were told how big to be
            result = specSize;
        } else {
            //Measure the height
            result = (int) (2 * mRadius + getPaddingTop() + getPaddingBottom() + 1);
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mWhichChild = position;
        postInvalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
