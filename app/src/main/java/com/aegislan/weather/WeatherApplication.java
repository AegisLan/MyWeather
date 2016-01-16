package com.aegislan.weather;

import android.app.Application;
import android.content.Context;

/**
 * Created by AegisLan on 2016.1.16.
 */
public class WeatherApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        super.onCreate();
    }

    public static Context getAppContext() {
        return context;
    }
}
