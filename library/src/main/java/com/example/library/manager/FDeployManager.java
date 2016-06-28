package com.example.library.manager;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.library.log.FLogFile;
import com.example.library.utils.FAppUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by yanglei on 15/11/28.
 * <p/>
 * 设备信息以及部署环境管理
 */
public class FDeployManager {

    private static final String TAG = FDeployManager.class.getSimpleName();

    // 环境改变观察者
    private static final ConcurrentLinkedQueue<IEnvironmentChangedListener> mListeners = new ConcurrentLinkedQueue<>();

    private static String version = "";// app 版本号
    private static String api_version = "";// ap版本号
    private static String channel = "";// 当前渠道
    private static String uuid = "";// 设备唯一 id
    private static String platform = "android";
    private static String os = "android";
    private static String imei = "";// DeviceInfo.IMEI
    private static String mac = "";// DeviceInfo.MAC
    private static String appType = "10"; // 与后端定义的 appType
    private static EnvironmentType environmentType = EnvironmentType.TYPE_TEST;

    public static boolean init(Context context, EnvironmentType type) {
        // 获取设备信息
        String deviceId;
        if(RxPermissions.getInstance(context).isGranted(Manifest.permission.READ_PHONE_STATE)){
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = tm.getDeviceId();
            imei = deviceId;
        } else {
            deviceId = "";
        }

        if (TextUtils.isEmpty(deviceId)) {
            uuid = UUID.randomUUID().toString();
        } else {
            try {
                uuid = UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")).toString();
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "create uuid error, e:" + e.getLocalizedMessage());
                uuid = UUID.randomUUID().toString();
            }
        }
        // device_mac = getMacAddress();
        mac = uuid;

        version = FAppUtils.getAppVersion(context);
        channel = FAppUtils.getMetaData(context, "channel");
        api_version = FAppUtils.getMetaData(context, "apiversion");
        platform = "android-" + Build.MANUFACTURER + ":" + Build.MODEL;
        os = "android-" + String.valueOf(Build.VERSION.SDK_INT);
        // 通知环境变化
        updateDeployType(type);
        return true;
    }

    /**
     * 更新部署环境
     *
     * @param type 环境
     */
    public static void updateDeployType(EnvironmentType type) {
        FDeployManager.environmentType = type;
        switch (type) {
            case TYPE_TEST: {
                FLogFile.setLogLevel(FLogFile.LOG_LEVEL_VERBOSE, true);
                break;
            }
            case TYPE_BETA: {
                FLogFile.setLogLevel(FLogFile.LOG_LEVEL_DEBUG, true);
                break;
            }
            case TYPE_ONLINE: {
                FLogFile.setLogLevel(FLogFile.LOG_LEVEL_ERROR, false);
                break;
            }
        }
        for (IEnvironmentChangedListener l : mListeners) {
            try {
                l.onEnvironmentChanged(type);
            } catch (Exception e) {
                FLogFile.e(TAG, "environment changed callback error, e:" + e.getLocalizedMessage());
            }
        }
    }

    public static String getVersion() {
        return version;
    }

    public static String getApiVersion() {
        return api_version;
    }

    public static String getPlatform() {
        return platform;
    }

    public static String getChannel() {
        return channel;
    }

    public static String getUuid() {
        return uuid;
    }

    public static String getOs() {
        return os;
    }

    public static String getImei() {
        return imei;
    }

    public static String getMac() {
        return mac;
    }

    public static EnvironmentType getEnvironmentType() {
        return environmentType;
    }

    public static String getAppType() {
        return appType;
    }

    /**
     * 注册环境变化回调
     */
    public static boolean registerEnvironmentChangeListener(IEnvironmentChangedListener listener) {
        return mListeners.add(listener);
    }

    /**
     * 注销环境变化回调
     */
    public static boolean unRegisterEnvironmentChangeListener(IEnvironmentChangedListener listener) {
        return mListeners.remove(listener);
    }

    /**
     * 连接的后端环境
     */
    public enum EnvironmentType {
        TYPE_TEST(1), TYPE_BETA(3), TYPE_ONLINE(4);

        private int value;

        public int getValue() {
            return this.value;
        }

        EnvironmentType(int value) {
            this.value = value;
        }

        public static EnvironmentType valueOf(int value) {
            switch (value) {
                case 1:
                    return TYPE_TEST;
                case 3:
                    return TYPE_BETA;
                case 4:
                    return TYPE_ONLINE;
                default:
                    return TYPE_ONLINE;
            }
        }
    }

    public interface IEnvironmentChangedListener {
        void onEnvironmentChanged(EnvironmentType type);
    }
}
