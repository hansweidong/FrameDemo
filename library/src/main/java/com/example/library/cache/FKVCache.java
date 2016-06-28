package com.example.library.cache;

import com.example.library.cache.disk.DiskCache;

import java.io.File;
import java.io.InputStream;

/**
 * Created by houlijiang on 15/11/28.
 *
 * key value存储数据的cache
 */
public class FKVCache {

    private File cacheFile;
    private static FKVCache instance;

    private FKVCache() {
    }

    public static FKVCache getInstance(File cacheFile) {
        if (instance == null) {
            instance = new FKVCache();
            instance.cacheFile = cacheFile;
        }
        return instance;
    }

    public boolean init() {
        return DiskCache.init(cacheFile, 1, 0);
    }

    /**
     * 查找数据
     *
     * @param key 用户使用的key
     * @return 数据或null
     */
    public InputStream getInputStream(String key) {
        return DiskCache.getInputStream(key);
    }

    /**
     * 查找数据
     *
     * @param key 用户使用的key
     * @return 数据或null
     */
    public String getString(String key) {
        return DiskCache.getString(key);
    }

    /**
     * 是否包含该数据
     *
     * @param key 用户使用的key
     * @return 是否包含该数据
     */
    public boolean contains(String key) {
        return DiskCache.contains(key);
    }

    /**
     * 向cache中添加数据
     *
     * @param key 用户的key
     * @param is cache的数据
     * @return 是否成功
     */
    public boolean put(String key, InputStream is) {
        return DiskCache.put(key, is);
    }

    /**
     * 向cache中添加数据，并设定超时时间
     *
     * @param key 用户的key
     * @param is cache的数据
     * @param timeout 超时时间 毫秒
     * @return 是否成功
     */
    public boolean put(String key, InputStream is, long timeout) {
        return DiskCache.put(key, is, timeout);
    }

    /**
     * 向cache中添加数据
     *
     * @param key 用户的key
     * @param value cache的数据
     * @return 是否成功
     */
    public boolean put(String key, String value) {
        return DiskCache.put(key, value);
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
        return DiskCache.put(key, value, timeout);
    }

    /**
     * 删除数据
     *
     * @param key 用户的key
     * @return 是否成功
     */
    public boolean remove(String key) {
        return DiskCache.delete(key);
    }

}