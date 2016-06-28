package com.example.library.manager;

import android.content.Context;
import android.util.Log;

import com.example.library.error.FErrorConst;
import com.example.library.log.FLogFile;
import com.example.library.utils.FFileUtils;

import java.io.File;

/**
 * Created by houlijiang on 15/12/2.
 *
 * 所有manager的统一管理，主要是进行初始化操作
 */
public class TXManager {

    private static final String TAG = TXManager.class.getSimpleName();

    /**
     * 只在主进程中初始化
     *
     * 主要初始化主页面相关
     */
    public static boolean initForMain(Context context) {
//        TXIMManager.getInstance(context).init();
//        TXWebViewJockeyRegister.registerCommonHandler();
//
//        // 启动后更新用户设置信息
//        ((TXAuthDataService) TXDataServiceManager.getService(TXAuthDataService.SERVICE_KEY))
//                .refreshSettingConfig(context);
        return true;
    }

    /**
     * 释放资源
     */
    public static boolean release(Context context) {
//        TXIMManager.getInstance(context).release();
//        TXDataServiceManager.unRegisterService(TXTypeDataService.SERVICE_KEY);
//        TXDataServiceManager.unRegisterService(TXAuthDataService.SERVICE_KEY);
        return true;
    }

    /**
     * 每个进程中都要初始化的
     *
     * 初始化基础模块，需要在多个进程中都初始化
     * 注意 这里初始化必须使用内部存储，因为6.0可能没有外部存储权限，如果实时获取权限，代码逻辑不好处理
     */
    public static boolean initForProcess(Context context, FDeployManager.EnvironmentType type) {
        // 配置环境
        FDeployManager.init(context, type);
        // 错误const初始化
        FErrorConst.init(context);
        // 缓存初始化，需要比较稳定的存储位置
        String goodCacheDir = FFileUtils.tryGetGoodDiskCacheDir(context);
        File cacheFile = new File(goodCacheDir, "cache");
        if (!cacheFile.exists()) {
            if (!cacheFile.mkdirs()) {
                Log.e(TAG, "create cache dir error");
            }
        }
        TXCacheManager.getInstance().init(context, cacheFile);
        // log初始化
        String goodFileDir = FFileUtils.tryGetGoodDiskFilesDir(context);
        File logFile = new File(goodFileDir, "txlog");
        if (!logFile.exists()) {
            if (!logFile.mkdirs()) {
                Log.e(TAG, "create log dir error");
            }
        }
        FLogFile.initLogger(logFile, type != FDeployManager.EnvironmentType.TYPE_ONLINE);

        /**
         * 注册DataService
         */
//        TXAuthDataService authService = new TXAuthDataService(context);
//        TXDataServiceManager.registerService(TXAuthDataService.SERVICE_KEY, authService);
//        TXDataServiceManager.registerService(TXTypeDataService.SERVICE_KEY, new TXTypeDataService(context));

        return true;
    }
}
