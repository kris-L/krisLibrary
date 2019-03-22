package com.xw.lib.custom.view.image;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SquareImageByWidth extends ImageView {

    private boolean isWidth = true;

    public SquareImageByWidth(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SquareImageByWidth(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageByWidth(Context context) {
        super(context);
    }

    public SquareImageByWidth(Context context, boolean isWidth) {
        super(context);
        this.isWidth = isWidth;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = getMeasuredHeight();
        if (isWidth) {
            size = getMeasuredWidth();
        }
        setMeasuredDimension(size, size);
    }
}
