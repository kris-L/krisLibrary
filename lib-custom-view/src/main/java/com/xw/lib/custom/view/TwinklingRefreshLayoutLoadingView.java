package com.xw.lib.custom.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.IBottomView;

/**
 * Created by Horris on 2016/12/20.
 */

public class TwinklingRefreshLayoutLoadingView extends LinearLayout implements IBottomView {
    View view;
    ImageView iv_loading;
    TextView tv_hint;

    public TwinklingRefreshLayoutLoadingView(Context context) {
        super(context);
        inflateView();
    }

    public TwinklingRefreshLayoutLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateView();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public TwinklingRefreshLayoutLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateView();
    }

    private void inflateView() {
        view = View.inflate(getContext(), R.layout.view_twinkling_refresh_layout_loading, this);
        iv_loading = (ImageView) view.findViewById(R.id.iv_loading);
        tv_hint = (TextView) view.findViewById(R.id.tv_hint);
        iv_loading.setImageResource(com.lcodecore.tkrefreshlayout.R.drawable.anim_loading_view);
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public void onPullingUp(float fraction, float maxHeadHeight, float headHeight) {
        tv_hint.setVisibility(VISIBLE);
    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        iv_loading.setVisibility(VISIBLE);
        tv_hint.setVisibility(GONE);
        ((AnimationDrawable) iv_loading.getDrawable()).start();
    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {

    }

    @Override
    public void onFinish() {
        iv_loading.setVisibility(GONE);
    }

    @Override
    public void reset() {
        iv_loading.setVisibility(GONE);
    }

    public void setHintNoneMoreData() {
        tv_hint.setText("没有更多数据");
    }

    public void setHintMoreData() {
        tv_hint.setText("释放加载更多");
    }
}
