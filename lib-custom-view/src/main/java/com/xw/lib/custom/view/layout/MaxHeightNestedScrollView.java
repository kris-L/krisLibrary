package com.xw.lib.custom.view.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.xw.lib.custom.view.R;

/**
 * Created by XWCHQ on 2017/10/25-11:38
 */

public class MaxHeightNestedScrollView extends NestedScrollView {
    private float maxHeight;

    public MaxHeightNestedScrollView(Context context) {
        this(context,null);
    }

    public MaxHeightNestedScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MaxHeightNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCustomAttr(context,attrs,defStyleAttr);
    }

    private void initCustomAttr(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightNestedScrollView);
        TypedValue typeValue = new TypedValue();
        boolean result = typedArray.getValue(R.styleable.MaxHeightNestedScrollView_maxLayHeight, typeValue);
        if(result) {
            if(typeValue.type == TypedValue.TYPE_DIMENSION) {
                this.maxHeight = typeValue.getDimension(getResources().getDisplayMetrics());
            }else if(typeValue.type == TypedValue.TYPE_FRACTION) {
                float fraction = typeValue.getFraction(1,1);
                if (fraction != 1) {
                    maxHeight = getResources().getDisplayMetrics().heightPixels * fraction;
                }
            }
        }
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredHeight = getMeasuredHeight();
        if(maxHeight > 0 && measuredHeight > maxHeight){
            setMeasuredDimension(getMeasuredWidth(), (int) maxHeight);
        }
    }
}
