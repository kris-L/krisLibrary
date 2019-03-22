package com.xw.lib.custom.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by XWCHQ on 2017/9/25-11:32
 */

public class XWToolBar extends Toolbar {
    private TextView titleView;

    public XWToolBar(Context context) {
        this(context,null);
    }

    public XWToolBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public XWToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupTitleView(context,attrs,defStyleAttr);
    }

    private void setupTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        titleView = new TextView(getContext());
        LayoutParams params = generateDefaultLayoutParams();
        params.gravity = Gravity.CENTER;
        titleView.setLayoutParams(params);
        addView(titleView);

        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                android.support.v7.appcompat.R.styleable.Toolbar, defStyleAttr, 0);
        int mTitleTextAppearance = a.getResourceId(android.support.v7.appcompat.R.styleable.Toolbar_titleTextAppearance, R.style.TitleTextAppearance);
        final CharSequence title = a.getString(android.support.v7.appcompat.R.styleable.Toolbar_title);
        titleView.setTextAppearance(getContext(),mTitleTextAppearance);
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }
        super.setTitle("");
        a.recycle();
    }

    @Override
    public void setTitle(@StringRes int resId) {
        if(titleView != null) {
            titleView.setText(resId);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if(titleView != null) {
            titleView.setText(title);
        }
    }
}
