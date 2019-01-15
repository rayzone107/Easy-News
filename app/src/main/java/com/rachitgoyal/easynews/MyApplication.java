package com.rachitgoyal.easynews;

import android.app.Application;

import com.rachitgoyal.easynews.network.RetrofitSingleton;

/**
 * Created by Rachit Goyal on 11/01/19.
 */
public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        RetrofitSingleton.init(this);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
}
