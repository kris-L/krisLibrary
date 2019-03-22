package com.xw.lib.custom.view.layout;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * Created by XWCHQ on 2016/12/29-14:14.
 */
public class ScanHorizontalScrollView extends HorizontalScrollView {
    public ScanHorizontalScrollView(Context context) {
        super(context);
        init();
    }

    public ScanHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScanHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScanHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void init() {
        setScrollBarSize(0);
    }

    final int period = 1;
    int offset = period;

    final int delay = 60;
    int times = 0;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int length = getMarqueeLength();
        if(length > 0){
            int scrollX = getScrollX();
            if(scrollX == length){ //到末端，延时后回滚
                times++;
                if(times >= delay) {
                    offset = -period;
                    times = 0;
                }
            } else if(scrollX == 0){//到首端，延时后后滚
                times++;
                if(times >= delay) {
                    offset = period;
                    times = 0;
                }
            }
            smoothScrollTo(scrollX +offset,0);
            postInvalidate();
        }
    }

    public int getMarqueeLength() {
        View child = getChildAt(0);
        return child.getWidth()-getMeasuredWidth() -getPaddingLeft() -getPaddingRight();
    }
}
