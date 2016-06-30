package com.example.library.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;

import com.baijiahulian.common.cache.disk.DiskCache;
import com.baijiahulian.common.image.CommonImageView;
import com.baijiahulian.common.image.ImageLoader;
import com.baijiahulian.common.image.ImageOptions;
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
        setupCaches(context);
        // log初始化
        setupLogs(context, type);
        // 网络图片
        setupImages(context);
        //文件缓存
        setupFileCaches(context);

        /**
         * 注册DataService
         */
//        TXAuthDataService authService = new TXAuthDataService(context);
//        TXDataServiceManager.registerService(TXAuthDataService.SERVICE_KEY, authService);
//        TXDataServiceManager.registerService(TXTypeDataService.SERVICE_KEY, new TXTypeDataService(context));

        return true;
    }
    /**
     * 设置缓存
     */
    private static void setupCaches(Context context){
        String goodCacheDir = FFileUtils.tryGetGoodDiskCacheDir(context);
        File cacheFile = new File(goodCacheDir, "cache");
        if (!cacheFile.exists()) {
            if (!cacheFile.mkdirs()) {
                Log.e(TAG, "create cache dir error");
            }
        }
        TXCacheManager.getInstance().init(context, cacheFile);
    }

    /**
     * 设置log
     */
    private static void setupLogs(Context context,FDeployManager.EnvironmentType type){
        String goodFileDir = FFileUtils.tryGetGoodDiskFilesDir(context);
        File logFile = new File(goodFileDir, "flog");
        if (!logFile.exists()) {
            if (!logFile.mkdirs()) {
                Log.e(TAG, "create log dir error");
            }
        }
        FLogFile.initLogger(logFile, type != FDeployManager.EnvironmentType.TYPE_ONLINE);
    }
    /**
     * 设置网络图片加载库 CommonImageView CircleImageView……
     */
    private static void setupImages(Context applicationContext){
        // 初始化图片缓存库
        String cacheDir = FFileUtils.tryGetGoodDiskCacheDir(applicationContext);
        File imageCache = new File(cacheDir, "image");
        if (!imageCache.exists()) {
            if (!imageCache.mkdirs()) {
                Log.e(TAG, "create image cache dir error");
            }
        }
        //是否设置阿里云图片裁剪
        ImageLoader.init(applicationContext, imageCache, new ImageLoader.IUrlProcessor() {
            @Override
            public String filter(CommonImageView civ, String url, ImageOptions options) {
                try {
                    if (!TextUtils.isEmpty(url) && url.contains("gsx")) {
                        int width = civ.getWidth();
                        int height = civ.getHeight();
                        boolean wrapWidth = false, wrapHeight = false;
                        if (civ.getLayoutParams() != null) {
                            wrapWidth = civ.getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT;
                            wrapHeight = civ.getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT;
                        }

                        boolean isFullyWrapContent = wrapWidth && wrapHeight;
                        if (width == 0 && height == 0 && !isFullyWrapContent) {
                            return url;
                        }
                        // _1e_1c 裁减
                        url = url + String.format("@%dw_%dh_1o.jpg", width, height);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "filter image url error, e:" + e.getLocalizedMessage());
                }
                return url;
            }
        });
    }
    /**
     * 设置文件缓存
     * DiskCache.put(BankListModel.CACHE_KEY, JsonUtils.toString(o));
     * DiskCache.delete(BankListModel.CACHE_KEY);
     */
    private static void setupFileCaches(Context applicationContext){
        // 文件缓存初始化
        String cacheDir = FFileUtils.tryGetGoodDiskCacheDir(applicationContext);
        File diskCache = new File(cacheDir, "files");
        if (!diskCache.exists()) {
            if (!diskCache.mkdirs()) {
                Log.e(TAG, "create file cache dir error");
            }
        }
        DiskCache.init(diskCache, 1, 0);
    }
}
