package com.xw.lib.custom.view.layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xw.lib.custom.view.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 波浪球形进度控件
 * Created by ms on 2017/6/30.
 */

public class WaveSphericalView extends View {
    public WaveSphericalView(Context context) {
        super(context);
    }

    public WaveSphericalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//关闭硬件加速,可以在实例化之后调用也行。不关闭加速，导致绘画出现未知错误
    }

    public WaveSphericalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//关闭硬件加速
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    private static final int DEFAULT_TEXT_COLOT = 0xFF666666;

    private static final int DEFAULT_TEXT_SIZE = 40;

    private float mPercent;
    private float mScore;

    private Paint mPaint = new Paint();

    private Bitmap mBitmap;

    private Bitmap mScaledBitmap;

    private float mLeft;

    /**
     * 偏移量
     */
    private int mSpeed = 15;

    private int mRepeatCount = 0;

    private Status mFlag = Status.NONE;

    private int mTextColor = DEFAULT_TEXT_COLOT;

    private int mTextSize = DEFAULT_TEXT_SIZE;

    public void setTextColor(int color) {
        mTextColor = color;
    }

    public void setTextSize(int size) {
        mTextSize = size;
    }

    public void setPercent(float percent) {
        mFlag = Status.RUNNING;
        mPercent = percent;
        //postInvalidate();//绘制子控件时调用
        invalidate();
    }

    public void setScore(float score) {
        BigDecimal bg = new BigDecimal(score).setScale(1, RoundingMode.UP);
        this.mScore = bg.floatValue();
        invalidate();
    }

    public void setStatus(Status status) {
        mFlag = status;
    }

    public void clear() {
        mFlag = Status.NONE;
        mScore = 0;
        mPercent = 0;
        if (mScaledBitmap != null) {
            mScaledBitmap.recycle();
            mScaledBitmap = null;
        }

        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("messon", "onDraw");
        int width = getWidth();
        int height = getHeight();

        //裁剪成圆区域
        Path path = new Path();
        canvas.save();
        path.reset();
        canvas.clipPath(path);
        path.addCircle(width / 2, height / 2, width / 2, Path.Direction.CCW);
        canvas.clipPath(path, Region.Op.REPLACE);

        if (mFlag == Status.RUNNING) {
            if (mScaledBitmap == null) {
                mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.custom__cjph_wave);
                mScaledBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth(), getHeight(), false);
                mBitmap.recycle();
                mBitmap = null;
                mRepeatCount = (int) Math.ceil(getWidth() / mScaledBitmap.getWidth() + 0.5) + 1;
            }
            //根据偏移量绘画图层，呈现波浪效果
            for (int idx = 0; idx < mRepeatCount; idx++) {
                canvas.drawBitmap(mScaledBitmap, mLeft + (idx - 1) * mScaledBitmap.getWidth(), (1 - mPercent) * getHeight(), null);
            }

            String str = mScore + "分";
            mPaint.setColor(mTextColor);
            mPaint.setTextSize(mTextSize);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawText(str, (getWidth() - mPaint.measureText(str)) / 2, getHeight() / 2, mPaint);
            String str2 = "平均分数";
            mPaint.setTextSize(mTextSize*2/3);
            canvas.drawText(str2, (getWidth() - mPaint.measureText(str2)) / 2, getHeight() / 2 + mTextSize, mPaint);
            mLeft += mSpeed;
            if (mLeft >= mScaledBitmap.getWidth())
                mLeft = 0;
            // 绘制外圆环
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(2);
            mPaint.setAntiAlias(true);
            mPaint.setColor(Color.rgb(33, 211, 39));
            canvas.drawCircle(width / 2, height / 2, width / 2 - 2, mPaint);
        }
        canvas.restore();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    public enum Status {
        RUNNING, NONE
    }
}
