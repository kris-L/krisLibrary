package com.xw.lib.custom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.math.BigDecimal;

public class CustomRatingBar extends LinearLayout implements View.OnClickListener
{
    /**
     * 是否可点击
     */
    private boolean mClickable;
    /**
     * 星星总数
     */
    private int starCount;
    /**
     * 星星的点击事件
     */
    private OnRatingChangeListener onRatingChangeListener;
    /**
     * 每个星星的大小
     */
    private float starImageSize;
    /**
     * 每个星星的间距
     */
    private int starPadding;
    /**
     * 星星的显示数量，支持小数点
     */
    private float selectStarValue;
    /**
     * 空白的默认星星图片
     */
    private Drawable starEmptyDrawable;
    /**
     * 选中后的星星填充图片
     */
    private Drawable starFillDrawable;
    /**
     * 半颗星的图片
     */
    private Drawable starHalfDrawable;
    /**
     * 每次点击星星所增加的量是整个还是半个
     */
    private StepSize stepSize;

    /**
     * 设置半星的图片资源文件
     *
     * @param starHalfDrawable
     */
    public void setStarHalfDrawable(Drawable starHalfDrawable) {
        this.starHalfDrawable = starHalfDrawable;
    }

    /**
     * 设置满星的图片资源文件
     *
     * @param starFillDrawable
     */
    public void setStarFillDrawable(Drawable starFillDrawable) {
        this.starFillDrawable = starFillDrawable;
    }

    /**
     * 设置空白和默认的图片资源文件
     *
     * @param starEmptyDrawable
     */
    public void setStarEmptyDrawable(Drawable starEmptyDrawable) {
        this.starEmptyDrawable = starEmptyDrawable;
    }

    /**
     * 设置星星是否可以点击操作
     *
     * @param clickable
     */
    public void setClickable(boolean clickable) {
        this.mClickable = clickable;
    }

    /**
     * 设置星星点击事件
     *
     * @param onRatingChangeListener
     */
    public void setOnRatingChangeListener(OnRatingChangeListener onRatingChangeListener) {
        this.onRatingChangeListener = onRatingChangeListener;
    }

    /**
     * 设置星星的大小
     *
     * @param starImageSize
     */
    public void setStarImageSize(float starImageSize) {
        this.starImageSize = starImageSize;
    }

    public void setStepSize(StepSize stepSize) {
        this.stepSize = stepSize;
    }

    /**
     * 构造函数
     * 获取xml中设置的资源文件
     *
     * @param context
     * @param attrs
     */
    public CustomRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomRatingBar);
        starImageSize = mTypedArray.getDimension(R.styleable.CustomRatingBar_starImageSize, 20);
        starPadding = (int)mTypedArray.getDimension(R.styleable.CustomRatingBar_starPadding, 10);
        selectStarValue = mTypedArray.getFloat(R.styleable.CustomRatingBar_curSelectValue, 1.0f);
        stepSize = StepSize.fromStep(mTypedArray.getInt(R.styleable.CustomRatingBar_stepSize, 1));
        starCount = mTypedArray.getInteger(R.styleable.CustomRatingBar_allStarsCount, 5);
        starEmptyDrawable = mTypedArray.getDrawable(R.styleable.CustomRatingBar_starEmpty);
        starFillDrawable = mTypedArray.getDrawable(R.styleable.CustomRatingBar_starFill);
        starHalfDrawable = mTypedArray.getDrawable(R.styleable.CustomRatingBar_starHalf);
        if (starHalfDrawable == null) {
            starHalfDrawable = starFillDrawable;
        }
        mClickable = mTypedArray.getBoolean(R.styleable.CustomRatingBar_clickable, true);

        final float scale = context.getResources().getDisplayMetrics().density;
        Log.i("CustomRatingBar", "starImageSize = " + starImageSize + ",  starPadding = " + starPadding );
        Log.i("CustomRatingBar", "scale = " + scale );

        mTypedArray.recycle();
        for (int i = 0; i < starCount; ++i) {
            final ImageView imageView = getStarImageView();
            imageView.setImageDrawable(starEmptyDrawable);

            addView(imageView);
        }
        setStar(selectStarValue);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        View root = getRootView();
        if (root == null || !(root instanceof ViewGroup)) {
            return false;
        }
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                Log.i("CustomRatingBar", "ACTION_DOWN x = " + event.getX() + ",  y = " + event.getRawY());
                float stars = caculateStarsValue(event.getX());
                setStar(stars);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("CustomRatingBar", "ACTION_MOVE x = " + event.getX() + ",  y = " + event.getRawY());
                float stars2 = caculateStarsValue(event.getX());
                setStar(stars2);
                break;

            default:
                break;
        }

        return true;
    }

    /**
     * 根据当前点击,或者滑动的位置，计算所需要显示的星星的个数
     *
     * @param x 传入当前点击或滑动位置（单位像素，不是dp），
     * 返回值 返回0.5的倍数，返回多少个星星，比如3.5个星星，就是第3个和第4个星星中间被点击
     */
    private float caculateStarsValue(float x)
    {
        float fResult = 0.0f;
        float iOneStarWidth = starImageSize + starPadding;
        float value = x * 2  / iOneStarWidth;
        fResult = ((float) Math.round(value) / 2) + 0.5f;
        Log.i("CustomRatingBar", "iOneStarWidth = " + iOneStarWidth + ",  fResult = " + fResult);
        return fResult;
    }

    /**
     * 设置每颗星星的参数
     *
     * @return
     */
    private ImageView getStarImageView() {
        ImageView imageView = new ImageView(getContext());

        LayoutParams layout = new LayoutParams(
                Math.round(starImageSize), Math.round(starImageSize));//设置每颗星星在线性布局的大小
        layout.setMargins(0, 0, Math.round(starPadding), 0);//设置每颗星星在线性布局的间距

        imageView.setLayoutParams(layout);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageDrawable(starEmptyDrawable);
        imageView.setMinimumWidth(10);
        imageView.setMaxHeight(10);



        return imageView;

    }


    /**
     * 设置星星的个数
     *
     * @param rating 传入的值，是0.5的整数倍，比如共5个星，显示3.5个星
     */

    public void setStar(float rating) {
        if(selectStarValue == rating)
        {
            return;
        }

        if(rating >= starCount)
        {
            rating = starCount;
        }

        if(rating <= 0 )
        {
            rating = 0;
        }

        if (onRatingChangeListener != null) {
            onRatingChangeListener.onRatingChange(rating);
        }

        this.selectStarValue = rating;
        // 浮点数的整数部分
        int fint = (int) rating;
        BigDecimal b1 = new BigDecimal(Float.toString(rating));
        BigDecimal b2 = new BigDecimal(Integer.toString(fint));

        // 浮点数的小数部分
        float fPoint = b1.subtract(b2).floatValue();

        // 设置选中的星星
        for (int i = 0; i < fint; ++i) {
            ((ImageView) getChildAt(i)).setImageDrawable(starFillDrawable);
        }
        // 设置没有选中的星星
        for (int i = fint; i < starCount; i++) {
            ((ImageView) getChildAt(i)).setImageDrawable(starEmptyDrawable);
        }
        // 小数点默认增加半颗星
        if (fPoint > 0) {
            ((ImageView) getChildAt(fint)).setImageDrawable(starHalfDrawable);
        }
    }

    @Override
    public void onClick(View v)
    {

    }


    /**
     * 操作星星的点击事件
     */
    public interface OnRatingChangeListener {
        /**
         * 选中的星星的个数
         *
         * @param ratingCount
         */
        void onRatingChange(float ratingCount);

    }

    /**
     * 星星每次增加的方式整星还是半星，枚举类型
     * 类似于View.GONE
     */
    public enum StepSize {
        Half(0), Full(1);
        int step;

        StepSize(int step) {
            this.step = step;
        }

        public static StepSize fromStep(int step) {
            for (StepSize f : values()) {
                if (f.step == step) {
                    return f;
                }
            }
            throw new IllegalArgumentException();
        }
    }
}
