package com.xw.lib.custom.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Horris on 2016/12/15.
 */

public class BannerView extends LinearLayout {

    public BannerView(Context context) {
        super(context);
        inflateView();
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
        inflateView();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs);
        inflateView();
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BannerView);
        titleText = ta.getString(R.styleable.BannerView_title_text);
        backLayoutGone = ta.getBoolean(R.styleable.BannerView_back_layout_gone, false);
        dividerGone = ta.getBoolean(R.styleable.BannerView_divider_gone, false);
        ta.recycle();
    }

    RelativeLayout layBack;
    ImageView ivBackIcon;
    TextView tvBack;
    TextView tvTitle;
    TextView rightTextBtn;
    LinearLayout layRightIcon;
    ImageView rightIcon;
    View view;
    String titleText = "";
    boolean backLayoutGone;
    boolean dividerGone;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void inflateView() {
        view = View.inflate(getContext(), R.layout.view_banner, this);
        layBack = (RelativeLayout) view.findViewById(R.id.lay_back);
        ivBackIcon = (ImageView) view.findViewById(R.id.iv_back);
        tvBack = (TextView) view.findViewById(R.id.tv_back);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        rightTextBtn = (TextView) view.findViewById(R.id.right_btn);
        layRightIcon = (LinearLayout) view.findViewById(R.id.banner_view_lay_right_icon);
        rightIcon = (ImageView) view.findViewById(R.id.right_icon);
        layBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getContext();
                if(context instanceof Activity){
                    ((Activity) context).onBackPressed();
                }
            }
        });
        tvTitle.setText(titleText);
        if (backLayoutGone) {
            layBack.setVisibility(View.INVISIBLE);
        }
        if(dividerGone){
            findViewById(R.id.banner_divider).setVisibility(View.GONE);
        }
    }

    public void setBackText(String str, boolean showBackIcon) {
        ivBackIcon.setVisibility(showBackIcon ? VISIBLE : GONE);
        if(TextUtils.isEmpty(str)) {
            return;
        }
        tvBack.setText(str);
    }

    public void setBackBtnOnClickListener(OnClickListener onClickListener) {
        layBack.setOnClickListener(onClickListener);
    }

    public void setTitle(String str) {
        if(str == null) {
            return;
        }
        tvTitle.setText(str);
    }

    public void setBackGone(){
        layBack.setVisibility(View.INVISIBLE);
    }

    public boolean isBackVisible(){
        return layBack.getVisibility() == View.VISIBLE;
    }

    public void setBackVisible(){
        layBack.setVisibility(View.VISIBLE);
    }

    public void setRightBtn(String str, OnClickListener onClickListener) {
        if(TextUtils.isEmpty(str)) {
            return;
        }
        rightTextBtn.setVisibility(VISIBLE);
        rightTextBtn.setText(str);
        rightTextBtn.setOnClickListener(onClickListener);
    }

    public void setRightBtnColor(int color) {
        rightTextBtn.setTextColor(color);
    }

    public void setRightIcon(Drawable drawable, OnClickListener onClickListener) {
        rightIcon.setImageDrawable(drawable);
        layRightIcon.setVisibility(VISIBLE);
        layRightIcon.setOnClickListener(onClickListener);
    }

    public void setRightGone(){
        layRightIcon.setVisibility(INVISIBLE);
    }
}
