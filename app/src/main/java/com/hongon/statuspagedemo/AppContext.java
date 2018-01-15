package com.hongon.statuspagedemo;

import android.app.Application;
import android.content.Context;

/**
 * Created by CoCO on 2018/1/15.
 */

public class AppContext extends Application{
    private static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance =getApplicationContext();

    }

    public static Context getContext(){
        return instance;
    }
}
