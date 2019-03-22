package com.xw.lib.custom.view.image;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SquareImageView extends ImageView {

    private boolean isWidth = false;

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, boolean isWidth) {
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
