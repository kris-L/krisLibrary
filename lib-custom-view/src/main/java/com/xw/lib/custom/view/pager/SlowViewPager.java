package com.xw.lib.custom.view.pager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by XWCHQ on 2018/1/15-10:08
 */

public class SlowViewPager extends ViewPager {
    private Method mFlipMethod;

    public SlowViewPager(@NonNull Context context) {
        super(context);
    }

    public SlowViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setCurrentItem(int item) {
        this.setCurrentItem(item,true);
    }

    /**
     *  使用手动滑时的翻页速度(默认的setCurrentItem(int position) 的速度200ms太快
     *  )
     */
    @Override
    public void setCurrentItem(int nextIndex, boolean isSmooth) {
        if(mFlipMethod == null) {
            Class<?>[] paramTypes = new Class[4];
            paramTypes[0] = Integer.TYPE;
            paramTypes[1] = Boolean.TYPE;
            paramTypes[2] = Boolean.TYPE;
            paramTypes[3] = Integer.TYPE;
            try {
                mFlipMethod = ViewPager.class.getDeclaredMethod("setCurrentItemInternal", paramTypes);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        if (mFlipMethod != null) {
            mFlipMethod.setAccessible(true);
            try {
                mFlipMethod.invoke(this, nextIndex, isSmooth, true, 1);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
