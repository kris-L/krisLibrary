package com.xw.lib.custom.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

public class ViewFlipperHorizontal extends ViewFlipper {

    public static final int MSG_SHOW_NEXT = 1000;

    public static final int MSG_SHOW_PREVIOUS = 1001;

    private boolean isOnce;

    private Handler mHandler;

    public ViewFlipperHorizontal(Context context) {
        super(context);
    }

    public ViewFlipperHorizontal(Context context, AttributeSet attrs) {
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
                R.anim.in_from_right));
        setOutAnimation(AnimationUtils.loadAnimation(getContext(),
                R.anim.out_to_left));
        int oldIndex = getDisplayedChild();
        int newIndex = oldIndex;
        if (oldIndex == getChildCount() - 1 && isOnce) {

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

    public void setOnce(boolean isOnce) {
        this.isOnce = isOnce;
    }

    @Override
    public void showPrevious() {
        // setInAnimation(AnimationUtils.makeInAnimation(getContext(), true));
        // setOutAnimation(AnimationUtils.makeOutAnimation(getContext(), true));
        setInAnimation(AnimationUtils.loadAnimation(getContext(),
                R.anim.in_from_left));
        setOutAnimation(AnimationUtils.loadAnimation(getContext(),
                R.anim.out_to_right));
        int oldIndex = getDisplayedChild();
        int newIndex = oldIndex;
        if (oldIndex == 0 && isOnce) {

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
