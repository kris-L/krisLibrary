package com.xw.lib.custom.view.util;

import android.graphics.Bitmap;
import android.view.View;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by XWCHQ on 2017/8/14-11:43
 */

public class ViewUtil {
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    /**
     * 兼容 API < 17 的View.generateViewId()
     */
    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    public static Bitmap captureBitmap(View v) {
        if (v == null) {
            return null;
        }
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        Bitmap bitmap = v.getDrawingCache();
        return bitmap;
    }
}
