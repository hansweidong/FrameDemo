package com.example.framedemo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;

import com.example.library.manager.FDeployManager;
import com.example.library.manager.TXManager;
import com.facebook.stetho.Stetho;

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
        mSharedPreferences = getSharedPreferences("app", Context.MODE_PRIVATE);
        // 初始化TX基础模块
        TXManager.initForProcess(this, getVersionType());

        if (shouldInit()) {// 在主进程中初始化：当app有多个进程的时候，process:remote application会oncreate多次
            // 初始化图片缓存模块
            // 恢复下载
            // 初始化全局类
            DeviceInfo.init(this);
            // AppContext.initialize(this);

            // TX INIT 初始化deploy manager等各种manager
            TXManager.initForMain(this);

            // catch捕获的异常
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(this);
            Thread.setDefaultUncaughtExceptionHandler(crashHandler);

            Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this)).build());
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
        int version = mSharedPreferences.getInt(PREF_VERSION_TYPE, -1);
        if (-1 == version) {// 没有设置过环境
            if (FDeployManager.isOnline) {
                return FDeployManager.EnvironmentType.TYPE_ONLINE;
            } else if (FDeployManager.isBeta) {
                return FDeployManager.EnvironmentType.TYPE_BETA;
            }
            return FDeployManager.EnvironmentType.TYPE_TEST;
        }
        return FDeployManager.EnvironmentType.valueOf(version);
    }

    // 改变环境的时候
    public void setVersionType(FDeployManager.EnvironmentType type) {
        mSharedPreferences.edit().putInt(PREF_VERSION_TYPE, type.getValue()).commit();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        TXManager.release(this);
    }

}
