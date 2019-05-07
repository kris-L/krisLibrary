package com.krislibrary;

import android.content.Context;
import android.support.multidex.MultiDexApplication;


public class MyApplication extends MultiDexApplication {


    private static MyApplication instance = null;

    public static MyApplication getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
    }

    public void init(){


    }



}
