package com.example.library.utils;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by bjhl on 16/6/27.
 *
 * action相关工具类
 */
public class FSchemaUtils {

    private static final String TAG = FSchemaUtils.class.getSimpleName();

    /**
     * 解析schema
     *
     * @param schemaUri schema字符串
     * @return 解析结果
     */
    public static ParseResult parseSchema(String schemaUri) {
        ParseResult result = new ParseResult();
        Uri uri = Uri.parse(schemaUri);
        String a = uri.getQueryParameter("a");
        if (TextUtils.isEmpty(a)) {
            Log.e(TAG, "a is null");
            return null;
        }
        result.name = a;
        result.params = new HashMap<>();
        Set<String> keys = uri.getQueryParameterNames();
        for (String key : keys) {
            result.params.put(key, uri.getQueryParameter(key));
        }
        return result;
    }

    /**
     * action解析返回的结果
     */
    public static class ParseResult {
        public String name;
        public Map<String, Object> params;
    }
}

