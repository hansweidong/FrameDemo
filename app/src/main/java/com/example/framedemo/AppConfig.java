package com.example.framedemo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;

import com.example.library.cache.sp.FSharePreferenceUtil;

/**
 * Created by bjhl on 16/6/28.
 */
public class AppConfig {
    public static String ENVIRONMENT = AppConfig.MODE_OFFICIAL;


    public static final String APP_KEY = "Fohqu0bo";

    public static String APP_VERSION_NAME;

    public static int APP_VERSION_CODE;

    public static String UUID;

    public static boolean isDebug;

    public static boolean isBeta;

    public static int screenWidth;

    public static int screenHeight;

    public static float screenDensity;

    public static String APP_CHANNEL;

    public static String APP_CHANNEL_ID;

    public static String APP_CHANNEL_TAG;

    public static final String MODE_DEV = "dev";
    public static final String MODE_BETA = "beta";
    public static final String MODE_OFFICIAL = "official";

    public static void init(Context context) {
        getAppInfo(context);
        initEnvironment(context);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        screenDensity = dm.density;
    }

    private static void getAppInfo(Context context) {
        PackageInfo packageInfo = null;
        ApplicationInfo appInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            // get ctype info from meta-data of AndroidManifest.xml
//			appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo != null) {
            APP_VERSION_NAME = packageInfo.versionName;
            APP_VERSION_CODE = packageInfo.versionCode;
        }
    }

    /**
     * 程序某个地方设置彩蛋，可以切换环境，要保存到sharedpreference
     * @param context
     */
    private static void initEnvironment(Context context) {
        String debugMode;
        if (ENVIRONMENT.equals(AppConfig.MODE_OFFICIAL)) {
            debugMode = ENVIRONMENT;
        }
        else {
            FSharePreferenceUtil sp = new FSharePreferenceUtil(context, "environment");
            debugMode = sp.getStringValue("run_environment", null);
            if (debugMode == null) {
                debugMode = ENVIRONMENT;
            }
        }

        if (debugMode == null) {
            isDebug = true;
            isBeta = false;
        } else {
            if (MODE_DEV.equals(debugMode)) {
                isDebug = true;
                isBeta = false;
            } else if (MODE_BETA.equals(debugMode)) {
                isDebug = false;
                isBeta = true;
            } else if (MODE_OFFICIAL.equals(debugMode)) {
                isDebug = false;
                isBeta = false;
            } else {
                isDebug = true;
                isBeta = false;
            }
        }
    }
}
