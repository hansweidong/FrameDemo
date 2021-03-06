package com.example.library.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by bjhl on 16/6/27.
 *
 * 类 Jockey 管理 url 跳转
 */
public class BJAction {

    private static final String TAG = BJAction.class.getName();

    public interface BJActionHandler {
        void doPerform(Context context, String type, Map<String, String> payload);
    }

    public BJAction() {
    }

    private static BJAction mInstance = null;

    private static byte[] mLock = new byte[0];
    public static BJAction getInstance() {
        if (mInstance == null) {
            synchronized (mLock) {
                if (mInstance == null)
                    mInstance = new BJAction();
            }
        }

        return mInstance;
    }

    private String mSchema;
    private String mHost;
    private String mPath;
    private String mAction;

    private ConcurrentHashMap<String, ArrayList<BJActionHandler>> mListeners =
            new ConcurrentHashMap<String, ArrayList<BJActionHandler>>();

    /**
     * @param schema
     * @param host
     * @param path
     * @param actionKey
     */
    public void initialAction(String schema, String host,
                              String path, String actionKey) {
        this.mSchema = schema;
        this.mHost = host;
        this.mPath = path;
        this.mAction = actionKey;
    }

    private boolean isInitialized() {
        return (!TextUtils.isEmpty(mSchema));
    }

    public void on(String type, BJActionHandler handler) {
        if (TextUtils.isEmpty(type)) throw new NullPointerException("type 不能为 null");

        ArrayList<BJActionHandler> list = mListeners.get(type);
        if (list == null) {
            list = new ArrayList<BJActionHandler>();
            mListeners.put(type, list);
        }

        list.add(handler);
    }

    public void off(String type) {
        if (TextUtils.isEmpty(type)) throw new NullPointerException("type 不能为 null");

        mListeners.remove(type);
    }

    /**
     * <p>schema 匹配失败直接返回 false。</br>
     * schema 匹配成功; host， path 匹配失败，属于能够处理但是没有处理的类型，返回 true，同时打印 Log</p>
     * @param context
     * @param url
     * @return false 表示未处理. 外部根据返回值再自行处理
     */
    public boolean sendToTarget(Context context, String url) {
        if (! isInitialized()) throw new RuntimeException("还未初始化");

        if (TextUtils.isEmpty(mSchema))
            return false;
        BJUrl bjUrl = BJUrl.parse(url, false);

        if (mSchema.equals(bjUrl.getProtocol())) {
            if (TextUtils.isEmpty(mHost)) {

                if (! TextUtils.isEmpty(bjUrl.getHost())) {
                    // host 为空，以 url 中的 host 为 action
                    triggerEvent(context, bjUrl.getHost(), bjUrl.getParameters());
                    return true;
                }
            } else {
                if (!mHost.equals(bjUrl.getHost())) {
                    Log.e(TAG, "schema 相同， host 不同。匹配失败");
                    return true;
                }
            }

            if (TextUtils.isEmpty(mPath)) {
                if (! TextUtils.isEmpty(bjUrl.getPath())) {
                    Log.e(TAG, "schema 相同，host 相同， path 不同。 匹配失败");
                    return true;
                }
            } else {
                if (! mPath.equals(bjUrl.getPath())) {
                    Log.e(TAG, "schema 相同，host 相同， path 不同。 匹配失败");
                    return true;
                }
            }

            if (bjUrl.getParameters() == null || bjUrl.getParameters().isEmpty()) {
                Log.e(TAG, "url action 不存在。 匹配失败");
                return true;
            }

            String type = bjUrl.getParameters().get(mAction);
            if (TextUtils.isEmpty(type)) {
                Log.e(TAG, "type 匹配失败");
                return true;
            }

            triggerEvent(context, type, bjUrl.getParameters());

        } else {
            return false;
        }

        return true;
    }

    private void triggerEvent(Context context, String type, Map<String, String> payload) {
        List<BJActionHandler> list = mListeners.get(type);
        if (list == null || list.size() == 0) return;

        Iterator<BJActionHandler> iterator = list.iterator();
        while (iterator.hasNext()) {
            iterator.next().doPerform(context, type, payload);
        }
    }
}
