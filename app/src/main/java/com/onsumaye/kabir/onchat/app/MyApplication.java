package com.onsumaye.kabir.onchat.app;


import android.app.Application;

import com.onsumaye.kabir.onchat.helper.MyPreferenceManager;

public class MyApplication extends Application
{
    public static final String TAG = MyApplication.class
            .getSimpleName();

    public static final String PACKAGE_NAME = MyApplication.class.getPackage().getName();

    private static MyApplication mInstance;

    private MyPreferenceManager pref;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized MyApplication getInstance()
    {
        return mInstance;
    }


    public MyPreferenceManager getPrefManager() {
        if (pref == null) {
            pref = new MyPreferenceManager(this);
        }

        return pref;
    }
}
