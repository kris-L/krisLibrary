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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 只有指定的索引后才有这个divider，而不是每个item都有
 */
public class IndexesDividerDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private List<Integer> indexList = new ArrayList<>();
    private int mOrientation = VERTICAL;
    private int mItemSize = 1;
    private Paint mPaint;
    private int paddingStart;
    private int paddingEnd;
    private Context context;
    private int itemColor;

    public IndexesDividerDecoration(Context context, int orientation) {
        this(context, orientation, Color.rgb(237, 237, 237));
    }

    public IndexesDividerDecoration(Context context, int orientation, int color) {
        this(context, orientation, color, 1);
    }

    public IndexesDividerDecoration(Context context, int orientation, int color, int itemSize) {
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

    public void setFilter(List<Integer> indexes) {
        indexList.clear();
        if (indexes != null) {
            indexList.addAll(indexes);
        }
    }

    public void addAll(List<Integer> indexes) {
        if (indexes != null) {
            indexList.addAll(indexes);
        }
    }

    public void addAll(Integer... indexes) {
        if (indexes != null) {
            indexList.addAll(Arrays.asList(indexes));
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int size = 0;
        int viewIndex = parent.indexOfChild(view);
        if (indexList.contains(viewIndex)) {
            size = getItemSize();
        }
        if (getOrientation() == VERTICAL) {
            outRect.set(0, 0, 0, size);
        } else {
            outRect.set(0, size, 0, 0);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }

    }

    public IndexesDividerDecoration setPaddingEnd(int paddingEnd) {
        this.paddingEnd = PxUtil.dip2px(getContext(), paddingEnd);
        return this;
    }

    public IndexesDividerDecoration setPaddingStart(int paddingStart) {
        this.paddingStart = PxUtil.dip2px(getContext(), paddingStart);
        return this;
    }

    protected void drawVertical(Canvas canvas, RecyclerView parent) {
        final int left = parent.getPaddingLeft() + paddingStart;
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight() - paddingEnd;
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize - 1; i++) {
            if(!indexList.contains(i)){
                continue;
            }
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
        for (int i = 0; i < childSize - 1; i++) {
            if(!indexList.contains(i)){
                continue;
            }
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin;
            final int right = left + mItemSize;
            canvas.drawRect(left, top, right, bottom, mPaint);
        }
    }

    public Context getContext() {
        return context;
    }

    public int getItemSize() {
        return mItemSize;
    }

    public IndexesDividerDecoration setItemSize(int itemSize) {
        this.mItemSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, itemSize,
                context.getResources().getDisplayMetrics());
        return this;
    }

    public int getItemColor() {
        return itemColor;
    }

    public IndexesDividerDecoration setItemColor(int itemColor) {
        this.itemColor = itemColor;
        mPaint.setColor(itemColor);
        return this;
    }

    public int getOrientation() {
        return mOrientation;
    }
}
