package com.example.library.log;

import java.io.File;

/**
 * Created by hyj on 11/5/15.
 */
public class FLogFile {
    public static final int LOG_FLAG_VERBOSE = 1 << 0;
    public static final int LOG_FLAG_DEBUG = 1 << 1;
    public static final int LOG_FLAG_INFO = 1 << 2;
    public static final int LOG_FLAG_WARN = 1 << 3;
    public static final int LOG_FLAG_ERROR = 1 << 4;

    public static final int LOG_LEVEL_VERBOSE =
        (LOG_FLAG_VERBOSE | LOG_FLAG_DEBUG | LOG_FLAG_INFO | LOG_FLAG_WARN | LOG_FLAG_ERROR);
    public static final int LOG_LEVEL_DEBUG = (LOG_FLAG_DEBUG | LOG_FLAG_INFO | LOG_FLAG_WARN | LOG_FLAG_ERROR);
    public static final int LOG_LEVEL_INFO = (LOG_FLAG_INFO | LOG_FLAG_WARN | LOG_FLAG_ERROR);
    public static final int LOG_LEVEL_WARN = (LOG_FLAG_WARN | LOG_FLAG_ERROR);
    public static final int LOG_LEVEL_ERROR = (LOG_FLAG_ERROR);
    public static final int LOG_LEVEL_OFF = 0;

    private static int mLogLevel = LOG_LEVEL_VERBOSE;
    private static boolean VERBOSE = (mLogLevel & LOG_FLAG_VERBOSE) > 0;
    private static boolean DEBUG = (mLogLevel & LOG_FLAG_DEBUG) > 0;
    private static boolean INFO = (mLogLevel & LOG_FLAG_INFO) > 0;
    private static boolean WARN = (mLogLevel & LOG_FLAG_WARN) > 0;
    private static boolean ERROR = (mLogLevel & LOG_FLAG_ERROR) > 0;

    public static void initLogger(File logDir, boolean isDebug) {
        FLogger.TAG = "FLogFile";
        FLogger.VERBOSE = VERBOSE;
        FLogger.DEBUG = DEBUG;
        FLogger.INFO = INFO;
        FLogger.WARN = WARN;
        FLogger.ERROR = ERROR;
        FLogger.initLogger(FLogger.TAG, isDebug, logDir);
    }

    public static void setLogLevel(int logLevel, boolean isDebug) {
        mLogLevel = logLevel;
        VERBOSE = (mLogLevel & LOG_FLAG_VERBOSE) > 0;
        DEBUG = (mLogLevel & LOG_FLAG_DEBUG) > 0;
        INFO = (mLogLevel & LOG_FLAG_INFO) > 0;
        WARN = (mLogLevel & LOG_FLAG_WARN) > 0;
        ERROR = (mLogLevel & LOG_FLAG_ERROR) > 0;
        FLogger.setDebugMode(isDebug);
        // initLogger();
    }

    public static void v(String tag, String msg) {
        if (VERBOSE) {
            FLogger.v(tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable throwable) {
        if (VERBOSE) {
            FLogger.v(tag, msg, throwable);
        }
    }

    public static void v(String msg) {
        if (VERBOSE) {
            FLogger.v(msg);
        }
    }

    public static void v(String msg, Throwable throwable) {
        if (VERBOSE) {
            FLogger.v(msg, throwable);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            FLogger.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable throwable) {
        if (DEBUG) {
            FLogger.d(tag, msg, throwable);
        }
    }

    public static void d(String msg) {
        if (DEBUG) {
            FLogger.d(msg);
        }
    }

    public static void d(String msg, Throwable throwable) {
        if (DEBUG) {
            FLogger.d(msg, throwable);
        }
    }

    public static void e(String tag, String msg) {
        if (ERROR) {
            FLogger.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable throwable) {
        if (ERROR) {
            FLogger.e(tag, msg, throwable);
        }
    }

    public static void e(String msg) {
        if (ERROR) {
            FLogger.e(msg);
        }
    }

    public static void e(String msg, Throwable throwable) {
        if (ERROR) {
            FLogger.e(msg, throwable);
        }
    }

    public static void i(String tag, String msg) {
        if (INFO) {
            FLogger.i(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable throwable) {
        if (INFO) {
            FLogger.i(tag, msg, throwable);
        }
    }

    public static void i(String msg) {
        if (INFO) {
            FLogger.i(msg);
        }
    }

    public static void i(String msg, Throwable throwable) {
        if (INFO) {
            FLogger.i(msg, throwable);
        }
    }

    public static void w(String tag, String msg) {
        if (WARN) {
            FLogger.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable throwable) {
        if (WARN) {
            FLogger.w(tag, msg, throwable);
        }
    }

    public static void w(String msg) {
        if (WARN) {
            FLogger.w(msg);
        }
    }

    public static void w(String msg, Throwable throwable) {
        if (WARN) {
            FLogger.w(msg, throwable);
        }
    }

    public static void printStackTrace(Throwable e) {
        FLogger.printStackTrace(e);
    }

}
