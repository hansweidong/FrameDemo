package com.example.library.manager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.library.cache.FKVCache;
import com.example.library.cache.model.FLightModelCache;
import com.example.library.cache.model.IBaseCacheModel;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.List;

/**
 * Created by houlijiang on 15/11/28.
 *
 * 缓存的统一管理
 * 根据缓存数据类型使用合适的方式进行存储
 */
public class TXCacheManager {

    private static final String TAG = TXCacheManager.class.getSimpleName();

    private FKVCache mKvCache;
    private FLightModelCache mModelCache;

    private static class InstanceHolder {
        public final static TXCacheManager instance = new TXCacheManager();
    }

    public static TXCacheManager getInstance() {
        return InstanceHolder.instance;
    }

    /**
     * 必须程序初始化时初始化
     *
     * @param context 上下文
     * @param cacheFile 缓存位置
     * @return 是否成功
     */
    public boolean init(Context context, File cacheFile) {
        if (mModelCache == null) {
            mModelCache = new FLightModelCache(context, "model_cache");
        }
        if (mKvCache == null) {
            mKvCache = FKVCache.getInstance(cacheFile);
            return mKvCache.init();
        }
        return true;
    }

    /**
     * 查找数据
     *
     * @param key 用户使用的key
     * @return 数据或null
     */
    public @Nullable
    String getString(@Nullable String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        return mKvCache.getString(key);
    }

    /**
     * 查找结果
     *
     * @param key key
     * @return 数据或者null
     */
    public @Nullable Long getLong(@Nullable String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        String value = mKvCache.getString(key);
        if (TextUtils.isEmpty(value)) {
            return null;
        } else {
            try {
                return Long.parseLong(value);
            } catch (Exception e) {
                Log.e(TAG, "parse long error, e:" + e.getLocalizedMessage());
                return null;
            }
        }
    }

    /**
     * 是否包含该数据
     *
     * @param key 用户使用的key
     * @return 是否包含该数据
     */
    public boolean contains(String key) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        return mKvCache.contains(key) || mModelCache.contains(key);
    }

    /**
     * 向cache中添加数据
     *
     * @param key 用户的key
     * @param value cache的数据
     * @return 是否成功
     */
    public boolean put(String key, String value) {
        return mKvCache.put(key, value);
    }

    /**
     * 向cache中添加数据
     *
     * @param key 用户的key
     * @param value cache的数据
     * @param timeout 超时时间 毫秒
     * @return 是否成功
     */
    public boolean put(String key, String value, long timeout) {
        return mKvCache.put(key, value, timeout);
    }

    /**
     * 向cache中添加数据
     *
     * @param key 用户的key
     * @param value cache的数据
     * @return 是否成功
     */
    public boolean put(String key, long value) {
        return mKvCache.put(key, String.valueOf(value));
    }

    /**
     * 向cache中添加数据
     *
     * @param key 用户的key
     * @param value cache的数据
     * @param timeout 超时时间 毫秒
     * @return 是否成功
     */
    public boolean put(String key, long value, long timeout) {
        return mKvCache.put(key, String.valueOf(value), timeout);
    }

    /**
     * 删除数据
     *
     * @param key 用户的key
     * @return 是否成功
     */
    public boolean remove(String key) {
        if (mKvCache.remove(key)) {
            return true;
        }
        return mModelCache.removeModel(key);
    }

    /**
     * 存储model数据
     *
     * @param key 用户的key
     * @param model 数据model
     * @return 是否成功
     */
    public <T extends IBaseCacheModel> boolean putModel(String key, T model) {
        return mModelCache.putModel(key, model);
    }

    /**
     * 存储model数据列表
     *
     * @param key 用户的key
     * @param modelList 数据model列表
     * @return 是否成功
     */
    public <T extends IBaseCacheModel> boolean putModelList(String key, List<T> modelList) {
        return mModelCache.putModelList(key, modelList);
    }

    /**
     * 获取数据model
     *
     * @param key 用户的key
     * @param clazz 数据model类型
     * @return 是否成功
     */
    public <T extends IBaseCacheModel> T getModel(String key, Class<T> clazz) {
        try {
            return mModelCache.getModel(key, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取数据model列表
     *
     * @param key 用户的key
     * @param typeToken 数据model类型
     * @return 是否成功
     */
    public <T extends IBaseCacheModel> List<T> getModelList(String key, TypeToken<List<T>> typeToken) {
        try {
            return mModelCache.getModelList(key, typeToken);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 删除model
     *
     * @param key 用户的key
     * @return 是否成功
     */
    public boolean removeModel(String key) {
        return mModelCache.removeModel(key);
    }

    public boolean putBoolean(String key, boolean value) {
        return mModelCache.putBoolean(key, value);
    }

    public boolean putString(String key, String value) {
        return mModelCache.putString(key, value);
    }

    public boolean putInt(String key, int value) {
        return mModelCache.putInt(key, value);
    }

    public boolean putLong(String key, long value) {
        return mModelCache.putLong(key, value);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mModelCache.getBooleanValue(key, defaultValue);
    }

    public String getString(String key, String defaultValue) {
        return mModelCache.getStringValue(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return mModelCache.getIntValue(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return mModelCache.getLongValue(key, defaultValue);
    }
}
