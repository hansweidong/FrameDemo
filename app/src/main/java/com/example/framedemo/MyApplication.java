package com.example.framedemo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;

import com.example.library.manager.FDeployManager;
import com.example.library.manager.TXManager;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.List;

/**
 * Created by bjhl on 16/6/25.
 */
public class MyApplication extends MultiDexApplication {

    /**
     * 数据库名称
     */
    private static final String DB_NAME = "common_db";
    /**
     * 数据库版本号
     */
    private static final int DB_VERSION = 2;

    private final String PREF_VERSION_TYPE = "version_type";

    private SharedPreferences mSharedPreferences;
    private static MyApplication sS;

    public static MyApplication getInstance() {
        return sS;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sS = this;
        // 初始化TX基础模块
        TXManager.initForProcess(this, getVersionType());
        mSharedPreferences = getSharedPreferences("app", Context.MODE_PRIVATE);

        if (shouldInit()) {// 在主进程中初始化：当app有多个进程的时候，process:remote application会oncreate多次
            // 初始化图片缓存模块
            // 恢复下载
            // 初始化全局类
            AppConfig.init(this);
            DeviceInfo.init(this);
            Fresco.initialize(getInstance());
            // AppContext.initialize(this);


            // TX INIT 初始化deploy manager等各种manager
            TXManager.initForMain(this);

            // catch捕获的异常
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(this);
            Thread.setDefaultUncaughtExceptionHandler(crashHandler);
        }
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    private FDeployManager.EnvironmentType getVersionType() {
        int version = FDeployManager.EnvironmentType.TYPE_ONLINE.getValue();
        // 这个在gradle里配置的，有限使用那个配置
//        if (!BuildConfig.IS_ONLINE) {
//            version =
//                mSharedPreferences.getInt(PREF_VERSION_TYPE, FDeployManager.EnvironmentType.TYPE_TEST.getValue());
//        }
        return FDeployManager.EnvironmentType.valueOf(version);
    }

    //改变环境的时候
    public void setVersionType(FDeployManager.EnvironmentType type) {
        mSharedPreferences.edit().putInt(PREF_VERSION_TYPE, type.getValue()).commit();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        TXManager.release(this);
    }
}
