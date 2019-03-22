package com.xw.lib.custom.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

public class ViewFlipperVertical extends ViewFlipper {

    public static final int MSG_SHOW_NEXT = 1000;

    public static final int MSG_SHOW_PREVIOUS = 1001;

    private boolean isOnce;

    private Handler mHandler;

    public ViewFlipperVertical(Context context) {
        super(context);
    }

    public ViewFlipperVertical(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    public void showNext() {
        // setInAnimation(AnimationUtils.makeInAnimation(getContext(), false));
        // setOutAnimation(AnimationUtils.makeOutAnimation(getContext(),
        // false));
        setInAnimation(AnimationUtils.loadAnimation(getContext(),
                R.anim.in_from_down));
        setOutAnimation(AnimationUtils.loadAnimation(getContext(),
                R.anim.out_to_up));
        int oldIndex = getDisplayedChild();
        int newIndex = oldIndex;
        if (isOnce && oldIndex == getChildCount() - 1) {

        } else {
            super.showNext();
            newIndex = getDisplayedChild();
        }
        if (mHandler != null) {
            Message msg = mHandler.obtainMessage(MSG_SHOW_NEXT);
            msg.arg1 = oldIndex;
            msg.arg2 = newIndex;
            mHandler.sendMessage(msg);
        }
    }

    /**
     * @param isOnce
     * @Title setOnce
     * @Description 设置循环标志，true为单链，false是环链；默认为false;如：为true时显示最后一个view时调用showNext()将无效，为false时回到第一个
     * @author 陈海钦
     * @date 2016-3-5 下午8:22:22
     */
    public void setOnce(boolean isOnce) {
        this.isOnce = isOnce;
    }

    @Override
    public void showPrevious() {
        // setInAnimation(AnimationUtils.makeInAnimation(getContext(), true));
        // setOutAnimation(AnimationUtils.makeOutAnimation(getContext(), true));
        setInAnimation(AnimationUtils.loadAnimation(getContext(),
                R.anim.in_from_up));
        setOutAnimation(AnimationUtils.loadAnimation(getContext(),
                R.anim.out_to_down));
        int oldIndex = getDisplayedChild();
        int newIndex = oldIndex;
        if (isOnce && oldIndex == 0) {

        } else {
            super.showPrevious();
            newIndex = getDisplayedChild();
        }
        if (mHandler != null) {
            Message msg = mHandler.obtainMessage(MSG_SHOW_PREVIOUS);
            msg.arg1 = oldIndex;
            msg.arg2 = newIndex;
            mHandler.sendMessage(msg);
        }
    }
}
