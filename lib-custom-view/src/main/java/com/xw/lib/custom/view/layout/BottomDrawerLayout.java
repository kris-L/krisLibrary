package com.xw.lib.custom.view.layout;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import com.xw.lib.custom.view.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 简易版本底部抽屉,子控件间不要有margin;
 * Created by XWCHQ on 2017/4/18-09:34
 */

public class BottomDrawerLayout extends LinearLayout implements ValueAnimator.AnimatorUpdateListener {

    private static final String TAG = "drawer";
    private boolean isReverse = false;
    private boolean draggable = false;
    private int defaultNum;
    private List<Point> childSizes = new ArrayList<>();
    private int showNum = -1;
    private ValueAnimator objectAnimator;
    private long animationDuration = 500;

    public BottomDrawerLayout(Context context) {
        this(context, null);
    }

    public BottomDrawerLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomDrawerLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BottomDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        final TypedArray typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.BottomDrawerLayout, defStyleAttr, defStyleRes);
        defaultNum = typedArray.getInteger(R.styleable.BottomDrawerLayout_defaultNum, getChildCount());
        isReverse = typedArray.getBoolean(R.styleable.BottomDrawerLayout_reverse,false);
        draggable = typedArray.getBoolean(R.styleable.BottomDrawerLayout_reverse,false);
        typedArray.recycle();
    }

    private Point maxSize;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(childSizes == null || childSizes.size() != getChildCount()) {
            getChildSizes();
        }
        if (defaultNum >= 0) {
            showFirst(defaultNum);
            defaultNum = -1;
        }
    }

    private void getChildSizes() {
        childSizes.clear();
        int maxWidth = 0;
        int maxHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            Point size = new Point(child.getMeasuredWidth(), child.getMeasuredHeight());
            childSizes.add(size);
            if(getOrientation() == HORIZONTAL) {
                maxWidth += child.getMeasuredWidth();
                if(child.getMeasuredHeight() > maxHeight){
                    maxHeight = child.getMeasuredHeight();
                }
            }else {
                maxHeight += child.getMeasuredHeight();
                if(child.getMeasuredWidth() > maxWidth){
                    maxWidth = child.getMeasuredWidth();
                }
            }
        }
        maxSize = new Point(maxWidth,maxHeight);
    }

    public void showFirst(int num) {
        showFirst(num, false);
    }

    public int getShowNum() {
        return showNum;
    }

    public void showFirst(int num, boolean animate) {
        int tempNum = num;
        if (tempNum > getChildCount()) {
            tempNum = getChildCount();
        }
        showNum = tempNum;
        if (getOrientation() == HORIZONTAL) {
            dispatchHorizontal(showNum, animate);
        } else {
            dispatchVertical(showNum, animate);
        }
    }

    private void dispatchVertical(int showNum, boolean animate) {
        int desHeight;
        if (isReverse) {
            desHeight = measureChildHeight(getChildCount() - showNum, showNum);
        } else {
            desHeight = measureChildHeight(0, showNum);
        }
        if (animate) {
            playAnimateToHeight(desHeight);
        } else {
            setNewSize(getMeasuredWidth(), desHeight);
        }
    }

    private void setNewSize(int width, int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        int tempWidth = width;
        int tempHeight = height;
        if(maxSize != null){
            if(tempWidth > maxSize.x){
                tempWidth = maxSize.x;
            }
            if(tempHeight > maxSize.y){
                tempHeight = maxSize.y;
            }
        }
        if (layoutParams == null) {
            layoutParams = new LayoutParams(tempWidth, tempHeight);
        } else {
            layoutParams.width = tempWidth;
            layoutParams.height = tempHeight;
        }
        setLayoutParams(layoutParams);
    }

    private void playAnimateToHeight(int desHeight) {
        playAnimate(getMeasuredHeight(), desHeight);
    }

    private void playAnimate(int startValue, int endValue) {
        if (objectAnimator != null && objectAnimator.isStarted()) {
            objectAnimator.cancel();
        }
        if (objectAnimator == null) {
            objectAnimator = ObjectAnimator.ofInt(startValue, endValue);
            objectAnimator.addUpdateListener(this);
            objectAnimator.setInterpolator(new LinearInterpolator());
        }
        objectAnimator.setDuration(getAnimationDuration());
        objectAnimator.setIntValues(startValue, endValue);
        objectAnimator.start();
    }

    private void playAnimateToWidth(int desWidth) {
        playAnimate(getMeasuredWidth(), desWidth);
    }

    private int measureChildHeight(int start, int num) {
        if (childSizes != null && start < childSizes.size()) {
            int sumHeight = 0;
            for (int i = 0; i < num && start + i < childSizes.size(); i++) {
                sumHeight += childSizes.get(start + i).y;
            }
            return sumHeight;
        }
        return 0;
    }

    private int measureChildWidth(int start, int num) {
        if (childSizes != null && start < childSizes.size()) {
            int sumWidth = 0;
            for (int i = 0; i < num && start + i < childSizes.size(); i++) {
                sumWidth += childSizes.get(start + i).x;
            }
            return sumWidth;
        }
        return 0;
    }

    private void dispatchHorizontal(int showNum, boolean animate) {
        int desWidth;
        if (isReverse) {
            desWidth = measureChildWidth(getChildCount() - showNum, showNum);
        } else {
            desWidth = measureChildWidth(0, showNum);
        }
        if (animate) {
            playAnimateToWidth(desWidth);
        } else {
            setNewSize(desWidth, getMeasuredHeight());
        }
    }

    public boolean isReverse() {
        return isReverse;
    }

    public void setReverse(boolean reverse) {
        isReverse = reverse;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        int value = (int) valueAnimator.getAnimatedValue();
        if (getOrientation() == HORIZONTAL) {
            setNewSize(value, getMeasuredHeight());
        } else {
            setNewSize(getMeasuredWidth(), value);
        }
    }

    public long getAnimationDuration() {
        return animationDuration;
    }

    public void setAnimationDuration(long animationDuration) {
        this.animationDuration = animationDuration;
    }

    private Point downPoint;
    private Point beginSize;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!draggable){
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downPoint = new Point((int)event.getRawX(), (int)event.getRawY());
                beginSize = new Point(getMeasuredWidth(),getMeasuredHeight());
                return true;
            case MotionEvent.ACTION_UP:
                Log.d(TAG,"actionUp");
                 swapSize();
                downPoint = null;
                beginSize = null;
                return true;
            case MotionEvent.ACTION_MOVE:
                if(downPoint != null && beginSize != null) {
                    if(getOrientation() == HORIZONTAL) {
                        return dispatchMoveHorizontal((int) (event.getRawX() - downPoint.x));
                    }else{
                        return dispatchMoveVertical((int) (event.getRawY() - downPoint.y));
                    }
                }
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    private boolean dispatchMoveVertical(int moveSize) {
        int tempSize = -moveSize;
        if(isReverse){
            tempSize = moveSize;
        }
        setNewSize(beginSize.x, beginSize.y + tempSize);
        return true;
    }

    private boolean dispatchMoveHorizontal(int moveSize) {
        int tempSize = -moveSize;
        if(isReverse){
            tempSize = moveSize;
        }
        setNewSize(beginSize.x + tempSize,beginSize.y);
        return true;
    }

    /**
     * 调整到显示完整子控件
     */
    private void swapSize() {
        int desNum = getOrientation() == HORIZONTAL?getHorizontalNum():getVerticalNum();
        showFirst(desNum,true);
    }

    private int getVerticalNum() {
        if(getMeasuredHeight() == 0){
            return 0;
        }
        int insideIndex = 0;
        int leftSize = 0;
        int desNum = 0;
        if(isReverse){
            for(int i = childSizes.size() - 1;i<childSizes.size();i--){
                if(leftSize <= getMeasuredHeight() && (leftSize + childSizes.get(i).y) > getMeasuredHeight() || i == 0){
                    insideIndex = i;
                    break;
                }else{
                    leftSize = leftSize + childSizes.get(i).y;
                }
            }
        }else{
            for(int i = 0;i<childSizes.size();i++){
                if(leftSize <= getMeasuredHeight() && (leftSize + childSizes.get(i).y) > getMeasuredHeight() || i == childSizes.size() -1){
                    insideIndex = i;
                    break;
                }else{
                    leftSize = leftSize + childSizes.get(i).y;
                }
            }
        }
        int childHeight = childSizes.get(insideIndex).y;
        if(getMeasuredHeight() - leftSize > childHeight / 2f){
            desNum = insideIndex + 1;
        }else{
            desNum = insideIndex;
        }
        return desNum;
    }

    private int getHorizontalNum() {
        if(getMeasuredWidth() == 0){
            return 0;
        }
        int insideIndex = 0;
        int leftSize = 0;
        int desNum = 0;
        if(isReverse){
            for(int i = childSizes.size() - 1;i<childSizes.size();i--){
                if(leftSize <= getMeasuredWidth() && (leftSize + childSizes.get(i).x) > getMeasuredWidth() || i == 0){
                    insideIndex = i;
                    break;
                }else{
                    leftSize = leftSize + childSizes.get(i).x;
                }
            }
        }else{
            for(int i = 0;i<childSizes.size();i++){
                if(leftSize <= getMeasuredWidth() && (leftSize + childSizes.get(i).x) > getMeasuredWidth() || i == childSizes.size() -1){
                    insideIndex = i;
                    break;
                }else{
                    leftSize = leftSize + childSizes.get(i).x;
                }
            }
        }
        int childWidth = childSizes.get(insideIndex).x;
        if(getMeasuredWidth() - leftSize > childWidth / 2f){
            desNum = insideIndex + 1;
        }else{
            desNum = insideIndex;
        }
        return desNum;
    }

}
