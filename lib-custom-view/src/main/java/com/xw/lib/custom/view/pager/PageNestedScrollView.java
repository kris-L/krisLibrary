package com.xw.lib.custom.view.pager;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Created by XWCHQ on 2017/10/9-14:21
 */

public class PageNestedScrollView extends NestedScrollView {

    private OnPageScrollListener onPageScrollListener;

    public PageNestedScrollView(Context context) {
        super(context);
    }

    public PageNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PageNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public OnPageScrollListener getOnPageScrollListener() {
        return onPageScrollListener;
    }

    public void setOnPageScrollListener(OnPageScrollListener onPageScrollListener) {
        this.onPageScrollListener = onPageScrollListener;
    }

    public static interface OnPageScrollListener{
        void onPageScroll(PageNestedScrollView view,float position);
    }
}
