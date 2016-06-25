package com.example.library.base;

import com.orhanobut.logger.Logger;

/**
 * Created by bjhl on 16/6/24.
 *
 * 打印log日志
 */
public class FLog {
    static {
        Logger.init()
                .setMethodCount(4);

    }
    private static final String TAG = "FLog";

    public static void d(String message) {
        Logger.d(TAG, message);
    }

    public static void e(String message) {
        Logger.e(TAG, message);
    }

    public static void e(Exception e) {
        Logger.e(TAG, e);
    }

    public static void e(String message, Exception e) {
        Logger.e(TAG, message, e);
    }

    public static void w(String message) {
        Logger.w(TAG, message);
    }

    public static void i(String message) {
        Logger.i(TAG, message);
    }

    public static void v(String message) {
        Logger.v(TAG, message);
    }

    public static void wtf(String message) {
        Logger.wtf(TAG, message);
    }

    //Unusable
//    public static void json(String json) {
//        Logger.json(json);
//    }
}
