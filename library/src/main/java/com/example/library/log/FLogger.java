package com.example.library.log;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * todo 应该放在基础里，并且目录应该外部传入，放在应用自己的目录下而不是根目录
 */
public class FLogger {
    private static final String PATH = "/example/test/"; // sdcard目录
    private static final String FILENAME = "log.txt"; // 日志文件名
    private static final String SEGMENTATION = "  "; // 数据分割符
    private static final String TYPE_VERBOSE = "V: ";
    private static final String TYPE_INFO = "I: ";
    private static final String TYPE_DEBUG = "D: ";
    private static final String TYPE_ERROR = "E: ";
    private static final String TYPE_WARN = "W: ";
    /**
     * The override method of FLogger.
     * 
     * The default level of any tag is set to LOGLEVEL 5. This means that any level log will be logged. if your set the
     * LOGLEVEL to 0 , no log will be print out.
     */
    // public static int LOGLEVEL = BuildConfig.DEBUG ? 5
    // : 0;
    public static int LOGLEVEL = 5;
    public static boolean VERBOSE = LOGLEVEL > 4;
    public static boolean DEBUG = LOGLEVEL > 3;
    public static boolean INFO = LOGLEVEL > 2;
    public static boolean WARN = LOGLEVEL > 1;
    public static boolean ERROR = LOGLEVEL > 0;

    public static String TAG = "FLogger";

    private static ExecutorService executor;
    private static BufferedWriter bufWriter = null;
    private static StringBuilder builder = null;
    private static boolean isWriteFile = true;

    public static void initLogger(String tag, boolean debugMode, File dirFile) {
        TAG = tag;
        setDebugMode(debugMode);
        // 实例化
        if (executor == null) {
            executor = Executors.newSingleThreadExecutor();
        }
        // 实例化 bufWriter对象
        File file = getLogFile(dirFile);
        if (file != null) {
            if (bufWriter == null) {
                try {
                    /** 追加模式写数据 */
                    bufWriter = new BufferedWriter(new FileWriter(file, true), 1024);
                } catch (Exception e) {

                }
            }
        }
        if (builder == null) {
            builder = new StringBuilder();
        }
    }

    public static void setDebugMode(boolean debugMode) {
        LOGLEVEL = debugMode ? 5 : 0;
        VERBOSE = LOGLEVEL > 4;
        DEBUG = LOGLEVEL > 3;
        INFO = LOGLEVEL > 2;
        WARN = LOGLEVEL > 1;
        ERROR = LOGLEVEL > 0;
    }

    public static void v(String tag, String msg) {
        if (DEBUG) {
            Log.v(tag, msg == null ? "" : msg);
            write(TYPE_VERBOSE, tag, msg);
        }
    }

    public static void printStackTrace(Throwable e) {
        if (DEBUG) {
            e.printStackTrace();
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            Log.v(tag, msg == null ? "" : msg, tr);
            write(TYPE_VERBOSE, tag, msg, tr);
        }
    }

    public static void v(String msg) {
        if (DEBUG) {
            Log.v(TAG, msg == null ? "" : msg);
            write(TYPE_VERBOSE, TAG, msg);
        }
    }

    public static void v(String msg, Throwable tr) {
        if (DEBUG) {
            Log.v(TAG, msg == null ? "" : msg, tr);
            write(TYPE_VERBOSE, TAG, msg, tr);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg == null ? "" : msg);
            write(TYPE_DEBUG, tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            Log.d(tag, msg == null ? "" : msg, tr);
            write(TYPE_DEBUG, tag, msg, tr);
        }
    }

    public static void d(String msg) {
        if (DEBUG) {
            Log.d(TAG, msg == null ? "" : msg);
            write(TYPE_DEBUG, TAG, msg);
        }
    }

    public static void d(String msg, Throwable tr) {
        if (DEBUG) {
            Log.d(TAG, msg == null ? "" : msg, tr);
            write(TYPE_DEBUG, TAG, msg, tr);
        }
    }

    public static void e(String tag, String msg) {
        if (ERROR) {
            Log.e(tag, msg == null ? "" : msg);
            write(TYPE_ERROR, TAG, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (ERROR) {
            Log.e(tag, msg == null ? "" : msg, tr);
            write(TYPE_ERROR, TAG, msg, tr);
        }
    }

    public static void e(String msg) {
        if (ERROR) {
            Log.e(TAG, msg == null ? "" : msg);
            write(TYPE_ERROR, TAG, msg);
        }
    }

    public static void e(String msg, Throwable tr) {
        if (ERROR) {
            Log.e(TAG, msg == null ? "" : msg, tr);
            write(TYPE_ERROR, TAG, msg, tr);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg == null ? "" : msg);
            write(TYPE_INFO, tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            Log.i(tag, msg == null ? "" : msg, tr);
            write(TYPE_INFO, tag, msg, tr);
        }
    }

    public static void i(String msg) {
        if (DEBUG) {
            Log.i(TAG, msg == null ? "" : msg);
            write(TYPE_INFO, TAG, msg);
        }
    }

    public static void i(String msg, Throwable tr) {
        if (DEBUG) {
            Log.i(TAG, msg == null ? "" : msg, tr);
            write(TYPE_INFO, TAG, msg, tr);
        }
    }

    public static void w(String tag, String msg) {
        if (DEBUG) {
            Log.w(tag, msg == null ? "" : msg);
            write(TYPE_WARN, tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            Log.w(tag, msg == null ? "" : msg, tr);
            write(TYPE_WARN, tag, msg, tr);
        }
    }

    public static void w(String msg) {
        if (DEBUG) {
            Log.w(TAG, msg == null ? "" : msg);
            write(TYPE_WARN, TAG, msg);
        }
    }

    public static void w(String msg, Throwable tr) {
        if (DEBUG) {
            Log.w(TAG, msg == null ? "" : msg, tr);
            write(TYPE_WARN, TAG, msg, tr);
        }
    }

    /**
     * 释放log文件资源
     */
    public static void destory() {
        if (executor != null) {
            executor.shutdown();
        }
        if (!executor.isTerminated()) {
            try {
                executor.awaitTermination(200, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        executor = null;
        if (bufWriter != null) {
            try {
                bufWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    bufWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    bufWriter = null;
                }
            }
        }
        if (builder != null) {
            builder.setLength(0);
            builder = null;
        }
    }

    private static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();
    }

    private static void write(final String logType, final String logTag, final String logInfo, final Throwable tr) {
        if (!isWriteFile) {
            return;
        }
        if (executor == null) {
            return;
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                int len = builder.length();
                builder.delete(0, len);// 清空之前数据
                builder.append(logType).append(toNormalString(new Date()));
                builder.append(SEGMENTATION).append("[").append(logTag).append("]").append(SEGMENTATION);
                builder.append(logInfo).append(SEGMENTATION);
                builder.append(getStackTraceString(tr));
                builder.trimToSize();
                writeLog(builder.toString());
            }
        });
    }

    private static void write(final String logType, final String logTag, final String logInfo) {
        write(logType, logTag, logInfo, null);
    }

    private static String toNormalString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd|HH:mm:ss", Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * 写日志数据信息
     * 
     * @param logInfo
     */
    private static void writeLog(String logInfo) {
        if (bufWriter == null) {
            return;
        }
        try {
            bufWriter.write(logInfo + "\n");
            bufWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    /**
     * 创建并获取日志文件
     */
    private static File getLogFile(File dir) {
        try {
            boolean exists = createDir(dir);
            if (!exists) {
                return null;
            }
            File file = new File(dir + PATH + FILENAME);
            if (!file.exists()) {
                try {
                    if (!file.createNewFile()) {
                        return null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建log目录文件
     */
    private static boolean createDir(File dir) {
        try {
            File file = new File(dir, PATH);
            if (!file.exists()) {
                if (file.mkdirs()) {
                    return true;
                }
            } else {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 临时测试用
     * 
     * @param c
     * @param content
     */
    public static void sd(Class<?> c, String content) {
        if (DEBUG) {
            Log.d("s_tag", "[" + c.getSimpleName() + "]" + content);
        }
    }

    /**
     * 临时测试用
     * 
     * @param c
     * @param content
     */
    public static void si(Class<?> c, String content) {
        if (DEBUG) {
            Log.i("s_tag", "[" + c.getSimpleName() + "]" + content);
        }
    }

    /**
     * 临时测试用
     * 
     * @param c
     * @param content
     */
    public static void se(Class<?> c, String content) {
        if (DEBUG) {
            Log.e("s_tag", "[" + c.getSimpleName() + "]" + content);
        }
    }

}
