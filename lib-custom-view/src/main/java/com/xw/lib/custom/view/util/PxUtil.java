package com.xw.lib.custom.view.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by XWCHQ on 2017/2/22-15:44
 */
public class PxUtil {
    public static int dip2px(Context context, float dip) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,
                context.getResources().getDisplayMetrics()));
    }

    public static int sp2px(Context context, float sp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                context.getResources().getDisplayMetrics()));
    }
}
