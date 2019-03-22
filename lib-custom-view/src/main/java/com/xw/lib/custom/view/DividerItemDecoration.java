package com.xw.lib.custom.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.xw.lib.custom.view.util.PxUtil;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private int mOrientation = VERTICAL;
    private int mItemSize = 1;
    private Paint mPaint;
    private int paddingStart;
    private int paddingEnd;
    private Context context;
    private int itemColor;
    private boolean isLastEnable = false;

    public boolean isLastEnable() {
        return isLastEnable;
    }

    public DividerItemDecoration setLastEnable(boolean lastEnable) {
        isLastEnable = lastEnable;
        return this;
    }

    public DividerItemDecoration(Context context, int orientation) {
        this(context,orientation, Color.rgb(237, 237, 237));
    }

    public DividerItemDecoration(Context context, int orientation,int color) {
        this(context,orientation,color,1);
    }

    public DividerItemDecoration(Context context, int orientation,int color,int itemSize) {
        this.context = context;
        this.mOrientation = orientation;
        if (orientation != VERTICAL && orientation != HORIZONTAL) {
            throw new IllegalArgumentException("invalid orientation");
        }

        mItemSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, itemSize,
                context.getResources().getDisplayMetrics());

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.itemColor = color;
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }

    }

    public DividerItemDecoration setPaddingEnd(int paddingEnd) {
        this.paddingEnd = PxUtil.dip2px(getContext(),paddingEnd);
        return this;
    }

    public DividerItemDecoration setPaddingStart(int paddingStart) {
        this.paddingStart = PxUtil.dip2px(getContext(),paddingStart);
        return this;
    }

    protected void drawVertical(Canvas canvas, RecyclerView parent) {
        final int left = parent.getPaddingLeft() + paddingStart;
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight() - paddingEnd;
        final int childSize = parent.getChildCount();
        int loopSize = childSize - (isLastEnable?0:1);
        for (int i = 0; i < loopSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin;
            final int bottom = top + mItemSize;
            canvas.drawRect(left, top, right, bottom, mPaint);
        }
    }

    protected void drawHorizontal(Canvas canvas, RecyclerView parent) {
        final int top = parent.getPaddingTop() + paddingStart;
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom() - paddingEnd;
        final int childSize = parent.getChildCount();
        int loopSize = childSize - (isLastEnable?0:1);
        for (int i = 0; i < loopSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin;
            final int right = left + mItemSize;
            canvas.drawRect(left, top, right, bottom, mPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL) {
            outRect.set(0, 0, mItemSize, mItemSize);
        } else {
            outRect.set(0, mItemSize, 0, 0);
        }
    }

    public Context getContext() {
        return context;
    }

    public int getItemSize() {
        return mItemSize;
    }

    public DividerItemDecoration setItemSize(int itemSize) {
        this. mItemSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, itemSize,
                context.getResources().getDisplayMetrics());
        return this;
    }

    public int getItemColor() {
        return itemColor;
    }

    public DividerItemDecoration setItemColor(int itemColor) {
        this.itemColor = itemColor;
        mPaint.setColor(itemColor);
        return this;
    }

    public int getOrientation() {
        return mOrientation;
    }
}
