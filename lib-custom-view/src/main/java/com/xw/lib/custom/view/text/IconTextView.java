package com.xw.lib.custom.view.text;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by XWCHQ on 2016/12/20-21:26.
 */
public class IconTextView extends TextView {
    private Drawable[] compoundDrawables;

    public IconTextView(Context context) {
        super(context);
        resetBounds();
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        resetBounds();
    }

    public IconTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        resetBounds();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IconTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        resetBounds();
    }

    private void resetBounds() {
        compoundDrawables = getCompoundDrawables();
        setCompoundDrawables(null, null, null, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(compoundDrawables != null && (compoundDrawables[0] != null || compoundDrawables[2] != null)){
            float scale = 1f;
            int iconSize = (int) ((getMeasuredHeight() - getPaddingTop() - getPaddingBottom()) * scale);

            int drawableNum = 0;
            if(compoundDrawables[0] != null) {
                compoundDrawables[0].setBounds(0, 0, iconSize, iconSize);
                drawableNum++;
            }
            if(compoundDrawables[2] != null){
                compoundDrawables[2].setBounds(0, 0, iconSize, iconSize);
                drawableNum++;
            }
            setCompoundDrawables(compoundDrawables[0],compoundDrawables[1],compoundDrawables[2],compoundDrawables[3]);
            int newWidth = getMeasuredWidth() + (iconSize+getCompoundDrawablePadding()) * drawableNum;
            setMeasuredDimension(newWidth,getMeasuredHeight());
            compoundDrawables = null;
        }
    }

}
