package com.lank.autovideo.tools;

import android.util.Log;

import com.lank.autovideo.BuildConfig;

public class LogUtil {
    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            Log.d("ALog", msg);
        }
    }

    public static void e(String msg) {
        if (BuildConfig.DEBUG) {
            Log.e("ALog", msg);
        }
    }
}
