package com.xw.lib.custom.view.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;

/**
 * @author 陈海钦
 * @ClassName TipImageView
 * @Description 在普通ImageView上多画个图形，使用{@link #setTipBitmap(Bitmap)}、{@link #setTipResource(int)}、{@link#setTipDrawable(Drawable)}设置显示图形，通过{@link #setTipVisibilly(boolean)}}设置显示与否；
 * {@link #setTipPosition(int, int)}}、{@link #setTipOffset(int, int)}}改变位置,{@link #setTipText(String)}}会创建默认{@link TextDrawable}的子类{@link TextCricleDrawable}或修改已有的{@link TextDrawable}
 * @date 2016-5-18 下午2:18:40
 * @see 设置位置及大小{@link #setTipPosition(int, int)}}，{@link #setTipOffset(int, int)}}
 */
public class TipImageView extends ImageView {

    private Drawable mTipDrawable = null;
    private Rect mTipBounds = null;
    private boolean isTipVisible = false;
    private int mTipGravity = Gravity.RIGHT | Gravity.TOP;
    private int mTipSize = 25;
    private int mXOffset, mYOffset;

    public TipImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TipImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TipImageView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isTipVisible && mTipDrawable != null) {
            mTipBounds = measureBounds();
            mTipDrawable.setBounds(mTipBounds);
            mTipDrawable.draw(canvas);
        }
    }

    private Rect measureBounds() {
        int left = 0;
        int top = 0;

        if ((mTipGravity & Gravity.RIGHT) == Gravity.RIGHT) {
            left = getMeasuredWidth() - mTipSize;
        } else if ((mTipGravity & Gravity.LEFT) == Gravity.LEFT) {
            left = 0;
        } else if ((mTipGravity & Gravity.CENTER_HORIZONTAL) == Gravity.CENTER_HORIZONTAL) {
            left = (getMeasuredWidth() - mTipSize) / 2;
        }

        if ((mTipGravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
            top = getMeasuredHeight() - mTipSize;
        } else if ((mTipGravity & Gravity.TOP) == Gravity.TOP) {
            top = 0;
        } else if ((mTipGravity & Gravity.CENTER_VERTICAL) == Gravity.CENTER_VERTICAL) {
            top = (getMeasuredHeight() - mTipSize) / 2;
        }
        left += mXOffset;
        top += mYOffset;
        Rect bounds = new Rect(left, top, left + mTipSize, top + mTipSize);
        return bounds;
    }

    public void setTipVisibilly(boolean isVisible) {
        isTipVisible = isVisible;
        invalidate();
    }

    public void setTipDrawable(Drawable drawable) {
        mTipDrawable = drawable;
        invalidate();
    }

    public void setTipResource(int resId) {
        mTipDrawable = getContext().getResources().getDrawable(resId);
        invalidate();
    }

    public void setTipBitmap(Bitmap bmp) {
        mTipDrawable = new BitmapDrawable(getContext().getResources(), bmp);
        invalidate();
    }

    /**
     * @param gravity
     * @param size
     * @Title setTipPosition
     * @Description 设置相对原ImageView的位置gravity及大小
     * @author 陈海钦
     * @date 2016-5-18 下午2:23:59
     */
    public void setTipPosition(int gravity, int size) {
        mTipGravity = gravity;
        mTipSize = size;
        invalidate();
    }

    /**
     * @param x
     * @param y
     * @Title setOffset
     * @Description 偏移
     * @author 陈海钦
     * @date 2016-5-18 下午2:18:30
     */
    public void setTipOffset(int x, int y) {
        mXOffset = x;
        mYOffset = y;
        invalidate();
    }

    public void setTipText(String text) {
        if (mTipDrawable == null || !(mTipDrawable instanceof TextDrawable)) {
            mTipDrawable = new TextCricleDrawable(text);
        } else if (mTipDrawable instanceof TextDrawable) {
            ((TextDrawable) mTipDrawable).setText(text);
        }
        invalidate();
    }

    public String getTipText() {
        if (mTipDrawable == null || !(mTipDrawable instanceof TextDrawable)) {
            return null;
        } else if (mTipDrawable instanceof TextDrawable) {
            return ((TextDrawable) mTipDrawable).getText();
        }
        return null;
    }

    public Drawable getTipDrawable() {
        return mTipDrawable;
    }

    public abstract static class TextDrawable extends Drawable {
        public abstract void setText(String text);

        public abstract String getText();
    }

    public static class TextCricleDrawable extends TextDrawable {

        private String mText = null;
        private int textColor = Color.parseColor("#FFFFFF");
        private int bgColor = Color.parseColor("#b00b11");
        private Paint mPaint;
        private int textPadding;

        public TextCricleDrawable(String text, int textColor, int bgColor) {
            this.mText = text;
            mPaint = new Paint();
            setColor(textColor, bgColor);
        }

        public TextCricleDrawable(int textColor, int bgColor) {
            mPaint = new Paint();
            setColor(textColor, bgColor);
        }

        public TextCricleDrawable(String text) {
            mPaint = new Paint();
            this.mText = text;
        }

        @Override
        public void draw(Canvas canvas) {
            Rect rect = getBounds();
            float width = rect.right - rect.left;
            float height = rect.bottom - rect.top;
            mPaint.setColor(bgColor);
            float radius = width / 2;
            float cx = rect.left + radius;
            float cy = rect.top + radius;
            canvas.drawCircle(cx, cy, radius, mPaint);
            if (mText != null) {
                mPaint.setColor(textColor);
                int textSize = (int) ((width - textPadding * 2) / mText.getBytes().length * 25 / 19);
                if (textSize > (width - textPadding * 2)) {
                    textSize = (int) (width - textPadding * 2);
                }
                mPaint.setTextSize(textSize);
                float textLen = mPaint.measureText(mText);
                float x = cx - textLen / 2;
                float y = cy + textSize / 2 - 2;
                canvas.drawText(mText, x, y, mPaint);
            }
        }

        @Override
        public void setAlpha(int alpha) {
            mPaint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            mPaint.setColorFilter(cf);
        }

        @Override
        public int getOpacity() {
            return 0;
        }

        public int getTextColor() {
            return textColor;
        }

        public void setTextColor(int textColor) {
            this.textColor = textColor;
        }

        public int getBgColor() {
            return bgColor;
        }

        public void setBgColor(int bgColor) {
            this.bgColor = bgColor;
        }

        public String getText() {
            return mText;
        }

        public void setText(String text) {
            this.mText = text;
        }

        public void setColor(int textColor, int bgColor) {
            this.textColor = textColor;
            this.bgColor = bgColor;
        }

        public int getTextPadding() {
            return textPadding;
        }

        public void setTextPadding(int textPadding) {
            this.textPadding = textPadding;
        }
    }

}
