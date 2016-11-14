package org.xielipeng.retrofitokhttprxjavademo;

import android.app.Application;

/**
 * Created by xielipeng on 2016/11/14.
 */

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";

    public static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
