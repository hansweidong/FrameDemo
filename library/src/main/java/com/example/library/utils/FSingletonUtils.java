package com.example.library.utils;

/**
 * Created by bjhl on 16/6/27.
 *
 * Singleton helper class for lazily initialization.
 */
public abstract class FSingletonUtils<T> {

    private T instance;

    protected abstract T newInstance();

    public final T getInstance() {
        if (instance == null) {
            synchronized (FSingletonUtils.class) {
                if (instance == null) {
                    instance = newInstance();
                }
            }
        }
        return instance;
    }
}
