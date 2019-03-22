package com.xw.lib.custom.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.TypedValue;

/**
 * Created by XWCHQ on 2017/2/24-10:03
 */
public class SpaceDividerDecoration extends android.support.v7.widget.DividerItemDecoration{
    public SpaceDividerDecoration(Context context, int orientation,int spaceSize) {
        super(context, orientation);
        if(orientation == HORIZONTAL){
            setDrawable(getHorizontalDrawable(getPx(context,spaceSize)));
        }else if(orientation == VERTICAL){
            setDrawable(getVerticalDrawable(getPx(context,spaceSize)));
        }
    }

    private int getPx(Context context,int spaceSize) {
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, spaceSize,
                context.getResources().getDisplayMetrics());
        return size;
    }

    private Drawable getVerticalDrawable(int spaceSize) {
        Shape shape = new RectShape();
        ShapeDrawable shapeDrawable = new ShapeDrawable(shape);
        shapeDrawable.setIntrinsicHeight(spaceSize);
        shapeDrawable.getPaint().setColor(Color.TRANSPARENT);
        return shapeDrawable;
    }


    private Drawable getHorizontalDrawable( int spaceSize) {
        Shape shape = new RectShape();
        ShapeDrawable shapeDrawable = new ShapeDrawable(shape);
        shapeDrawable.setIntrinsicWidth(spaceSize);
        shapeDrawable.getPaint().setColor(Color.TRANSPARENT);
        return shapeDrawable;
    }
}
