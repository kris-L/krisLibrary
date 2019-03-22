package com.xw.lib.custom.view.image;

import android.graphics.BitmapFactory.Options;

public class DefaultOptions extends Options {

    private static final int SIZE = 25 * 1024;

    public DefaultOptions() {
        inTempStorage = new byte[SIZE];
        inPurgeable = true;
        inSampleSize = 4;
        inInputShareable = true;
    }

}
