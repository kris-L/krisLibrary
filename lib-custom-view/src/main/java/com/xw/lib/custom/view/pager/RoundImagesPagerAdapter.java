package com.xw.lib.custom.view.pager;

import android.content.Context;

import java.util.List;

/**
 * Created by XWCHQ on 2017/10/26-14:12
 */

public class RoundImagesPagerAdapter extends ImagesBannerPagerAdapter {

    private boolean isRoundMode = true;

    public RoundImagesPagerAdapter(Context context, List<? extends Object> imagePaths, LoadImageStrategy strategy) {
        super(context, imagePaths, strategy);
    }

    public RoundImagesPagerAdapter(Context context, LoadImageStrategy strategy) {
        super(context, strategy);
    }

    public RoundImagesPagerAdapter(Context context, List<? extends Object> imagePaths) {
        super(context, imagePaths);
    }

    public RoundImagesPagerAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
        if(isRoundMode && isRoundModeEnable()) {
            return super.getCount() + 2;
        }else{
            return super.getCount();
        }
    }

    public boolean isRoundModeEnable() {
        return super.getCount() > 1;
    }

    @Override
    public Object getItem(int position) {
        if(isRoundMode && isRoundModeEnable()) {
            if (position == 0) {
                return super.getItem(super.getCount() - 1);
            } else if (position == getCount() - 1) {
                return super.getItem(0);
            } else {
                return super.getItem(position - 1);
            }
        }else{
            return super.getItem(position % super.getCount());
        }
    }

    public boolean isRoundMode() {
        return isRoundMode && isRoundModeEnable();
    }

    public void setRoundMode(boolean roundMode) {
        isRoundMode = roundMode;
    }
}
