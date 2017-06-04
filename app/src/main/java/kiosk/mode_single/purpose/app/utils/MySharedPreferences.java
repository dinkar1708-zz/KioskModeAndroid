/*
 * Copyright (c) 2016 NEC Corporation All Rights Reserved.
 */
package kiosk.mode_single.purpose.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * shared preference for application
 */
public class MySharedPreferences {

    private static final String MY_APP_PREF = "application_pref";
    private static final String KIOSK_MODE_STATUS_PREF = "kiosk_mode_locked_pref";
    private static final String APP_LAUNCHED_PREF = "app_launched_pref";

    private static boolean read(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(MY_APP_PREF, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, false);
    }

    private static void write(Context context, String key, boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences(MY_APP_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void setKioskMode(Context context, boolean isLocked) {
        write(context, KIOSK_MODE_STATUS_PREF, isLocked);
    }

    public static boolean isAppInKioskMode(Context context) {
        return read(context, KIOSK_MODE_STATUS_PREF);
    }

    public static void saveAppLaunched(Context context, boolean isFirstLaunch) {
        write(context, APP_LAUNCHED_PREF, isFirstLaunch);
    }

    public static boolean isAppLaunched(Context context) {
        return read(context, APP_LAUNCHED_PREF);
    }
}
