package com.example.framedemo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.orhanobut.logger.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * Created by bjhl on 16/6/28.
 *
 * 设备信息
 */

public class DeviceInfo {
    public static final String TAG = "DeviceInfo";
    // /** TV版本号 */
    // public static String VER;
    public static String VER_KEY = "android_general";
    public static String BRAND = "";
    /** 手机型号 */
    public static String BTYPE = "";
    /** 操作系统 */
    public static String OS = "Android";
    /** 屏幕宽 */
    public static int WT = 0;
    /** 屏幕高 */
    public static int HT = 0;
    /** 机器串号 */
    public static String IMEI = "";
    public static String IMSI = "";
    /** 运营商 */
    public static String OPERATOR = "";
    /** mac地址 */
    public static String MAC = "";

    public static String M_UUID = "";
    /** 激活时间 */
    public static String ACTIVE_TIME = "";
    /** 手机号码 */
    public static String MOBILE = "";
    public static String NETWORKTYPE = "";
    /**
     * 手机CPU
     */
    public static String CPU = "";
    public static String MEM_TOTAL = "";
    public static String MEM_FREE = "";
    public static String ROM_TOTAL = "";
    public static String ROM_FREE = "";
    public static String SDCARD_TOTAL = "";
    public static String SDCARD_FREE = "";
    public static String NETWORKINFO = "";

    public static String DEVICEID;

    public static String OS_VER;

    public static void init(Context context) {
        getWidthNHeight(context);
        // try {
        // DeviceInfo.VER =
        // context.getPackageManager().getPackageInfo(context.getPackageName(),
        // PackageManager.GET_CONFIGURATIONS).versionName;
        // } catch (NameNotFoundException e1) {
        // e1.printStackTrace();
        // }
        TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        DeviceInfo.DEVICEID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        DeviceInfo.ACTIVE_TIME = System.currentTimeMillis() + "";
        DeviceInfo.IMEI = URLEncoder(tMgr.getDeviceId());
        DeviceInfo.IMSI = URLEncoder(tMgr.getSimSerialNumber());
        if (!TextUtils.isEmpty(tMgr.getSimOperatorName()) || !TextUtils.isEmpty(tMgr.getSimOperator())) {
            DeviceInfo.OPERATOR = URLEncoder(tMgr.getSimOperatorName() + "_" + tMgr.getSimOperator());
        }
        else {
            DeviceInfo.OPERATOR = "";
        }
        if (DeviceInfo.OS.length() == 7) {
            // DeviceInfo.OS = URLEncoder(DeviceInfo.OS + android.os.Build.VERSION.RELEASE);
            DeviceInfo.OS = "android" + String.valueOf(Build.VERSION.SDK_INT);
        }
        DeviceInfo.BRAND = URLEncoder(Build.BRAND);
        DeviceInfo.BTYPE = URLEncoder(Build.MODEL);

        DeviceInfo.MAC = getMac(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        DeviceInfo.NETWORKTYPE = networkInfo == null ? "OTHER" : networkInfo.getTypeName();
        DeviceInfo.NETWORKINFO = networkInfo == null ? "OTHER" : networkInfo.toString();
        if (!DeviceInfo.IMEI.trim().equals("") || !DeviceInfo.MAC.trim().equals("")) {
            // 如果能取到这两个中间任意一值，则不需要传UUID

        }
        else {
            DeviceInfo.M_UUID = UUID.randomUUID().toString();
        }
        Logger.d(TAG, "DeviceInfo.DEVICEID:" + DeviceInfo.DEVICEID);
        Logger.d(TAG, "DeviceInfo.ACTIVE_TIME:" + DeviceInfo.ACTIVE_TIME);
        Logger.d(TAG, "DeviceInfo.IMEI:" + DeviceInfo.IMEI);
        Logger.d(TAG, "DeviceInfo.IMSI:" + DeviceInfo.IMSI);
        Logger.d(TAG, "DeviceInfo.OPERATOR:" + DeviceInfo.OPERATOR);
        Logger.d(TAG, "DeviceInfo.OS:" + DeviceInfo.OS);
        Logger.d(TAG, "DeviceInfo.BRAND:" + DeviceInfo.BRAND);
        Logger.d(TAG, "DeviceInfo.BTYPE:" + DeviceInfo.BTYPE);
        Logger.d(TAG, "DeviceInfo.MAC:" + DeviceInfo.MAC);
        Logger.d(TAG, "DeviceInfo.NETWORKTYPE:" + DeviceInfo.NETWORKTYPE);
        Logger.d(TAG, "DeviceInfo.NETWORKINFO:" + DeviceInfo.NETWORKINFO);

    }

    public static String URLEncoder(String s) {
        if (s == null || "".equals(s))
            return "";
        try {
            s = URLEncoder.encode(s, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            return "";
        }
        catch (NullPointerException e) {
            return "";
        }
        return s;
    }

    private static void getWidthNHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        DeviceInfo.WT = dm.widthPixels;
        DeviceInfo.HT = dm.heightPixels;
    }

    public static String getMac1(Context context) {
        String mac = getMac(context);
        if (TextUtils.isEmpty(mac)) {
            mac = "";
        }
        else {
            mac = URLEncoder(mac.replace(":", "%3A"));
        }

        return mac;
    }

    public static String getMac(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        WifiInfo info = wifi.getConnectionInfo();

        return info.getMacAddress();
    }
}
