package com.xw.lib.custom.view.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.xw.lib.custom.view.R;

public class CircleImageView extends ImageView {

    private Bitmap mImage;
    private int imgWidth;
    private int imgHeight;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initParams(context, attrs, defStyle);
    }

    private void initParams(Context context, AttributeSet attrs, int defStyle) {
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.CircleImageView, defStyle, 0);
        int indexCount = ta.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = ta.getIndex(i);
            if (index == R.styleable.CircleImageView_src) {
                mImage = BitmapFactory.decodeResource(getResources(),
                        ta.getResourceId(index, 0));

            }
        }
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mImage == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int widMode = MeasureSpec.getMode(widthMeasureSpec);
        int widSize = MeasureSpec.getSize(widthMeasureSpec);
        int mWidth = widSize;
        int heiMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int mHeight = heightSize;
        if (widMode == MeasureSpec.EXACTLY) {
            mWidth = widSize;
        } else {
            int desireByImage = getPaddingLeft() + getPaddingRight()
                    + mImage.getWidth();
            if (widMode == MeasureSpec.AT_MOST) {
                mWidth = widSize < desireByImage ? widSize : desireByImage;
            } else {
                mWidth = desireByImage;
            }
        }
        imgWidth = mWidth - getPaddingLeft() - getPaddingRight();
        if (heiMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            int desireByImage = getPaddingTop() + getPaddingBottom()
                    + mImage.getHeight();
            if (heiMode == MeasureSpec.AT_MOST) {
                mHeight = heightSize < desireByImage ? heightSize
                        : desireByImage;
            } else {
                mHeight = desireByImage;
            }
        }
        imgHeight = mHeight - getPaddingTop() - getPaddingRight();
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        if (mImage != null) {
            int min = imgWidth < imgHeight ? imgWidth : imgHeight;
            mImage = Bitmap.createScaledBitmap(mImage, min, min, false);
            canvas.drawBitmap(getCircleImage(mImage, min), getPaddingLeft(), getPaddingTop(), null);
        }
    }

    private Bitmap getCircleImage(Bitmap image, int min) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(image, 0, 0, paint);
        return target;
    }

    @Override
    public void setImageBitmap(Bitmap bmp) {
        mImage = bmp;
        invalidate();
    }

    public void setImageResource(int resource) {
        mImage = BitmapFactory.decodeResource(getResources(), resource);
        invalidate();
    }

    public void setImageDrawable(Drawable drawable) {
        mImage = drawable2Bitmap(drawable);
        invalidate();
    }

    protected Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                                    : Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }
}

