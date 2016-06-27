package com.example.library.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by bjhl on 16/6/27.
 */
public class FJsonUtil {

    // 默认所有日期格式都是一样的
    // public static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    // 支持转换成Date类型
    public static final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
            return new Date(json.getAsJsonPrimitive().getAsLong());
        }
    }).registerTypeHierarchyAdapter(Calendar.class, new CalendarAdapter()).create();

    public static class CalendarAdapter implements JsonSerializer<Calendar>, JsonDeserializer<Calendar> {

        public Calendar deserialize(JsonElement json, Type type, JsonDeserializationContext context)
            throws JsonParseException {
            Calendar calendar = Calendar.getInstance();
            // Calendar 的 time 有问题, 使用 Date
            calendar.setTime(new Date(json.getAsJsonPrimitive().getAsLong()));
            return calendar;
        }

        public JsonElement serialize(Calendar calendar, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(Long.valueOf(calendar.getTimeInMillis()));
        }
    }

    public static int getInt(JsonObject jsonObject, String key, int defaultValue) {
        if (checkEmpty(jsonObject, key))
            return defaultValue;

        try {
            return jsonObject.get(key).getAsInt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defaultValue;
    }

    public static int getInt(String jsonString, String key, int defaultValue) {
        if (checkEmpty(jsonString, key))
            return defaultValue;

        try {
            return getInt(parse(jsonString), key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defaultValue;
    }

    public static long getLong(JsonObject jsonObject, String key, long defaultValue) {
        if (checkEmpty(jsonObject, key))
            return defaultValue;

        try {
            return jsonObject.get(key).getAsLong();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defaultValue;
    }

    public static long getLong(String jsonString, String key, long defaultValue) {
        if (checkEmpty(jsonString, key))
            return defaultValue;

        try {
            return getLong(parse(jsonString), key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defaultValue;
    }

    public static float getFloat(JsonObject jsonObject, String key, float defaultValue) {
        if (checkEmpty(jsonObject, key))
            return defaultValue;

        try {
            return jsonObject.get(key).getAsFloat();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defaultValue;
    }

    public static float getFloat(String jsonString, String key, float defaultValue) {
        if (checkEmpty(jsonString, key))
            return defaultValue;

        try {
            return getFloat(parse(jsonString), key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defaultValue;
    }

    public static double getDouble(JsonObject jsonObject, String key, double defaultValue) {
        if (checkEmpty(jsonObject, key))
            return defaultValue;

        try {
            return jsonObject.get(key).getAsDouble();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defaultValue;
    }

    public static double getDouble(String jsonString, String key, double defaultValue) {
        if (checkEmpty(jsonString, key))
            return defaultValue;

        try {
            return getDouble(parse(jsonString), key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defaultValue;
    }

    public static boolean getBoolean(JsonObject jsonObject, String key, boolean defaultValue) {
        if (checkEmpty(jsonObject, key))
            return defaultValue;

        try {
            return jsonObject.get(key).getAsBoolean();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defaultValue;
    }

    public static boolean getBoolean(String jsonString, String key, boolean defaultValue) {
        if (checkEmpty(jsonString, key))
            return defaultValue;

        try {
            return getBoolean(parse(jsonString), key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defaultValue;
    }

    public static String getString(JsonObject jsonObject, String key, String defaultValue) {
        if (checkEmpty(jsonObject, key))
            return defaultValue;

        try {
            return jsonObject.get(key).getAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defaultValue;
    }

    public static String getString(String jsonString, String key, String defaultValue) {
        if (checkEmpty(jsonString, key))
            return defaultValue;

        try {
            return getString(parse(jsonString), key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defaultValue;
    }

    public static JsonObject getJsonObject(JsonObject jsonObject, String key) {
        if (checkEmpty(jsonObject, key))
            return null;

        try {
            return jsonObject.get(key).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JsonObject getJsonObject(String jsonString, String key) {
        if (checkEmpty(jsonString, key))
            return null;

        try {
            return getJsonObject(parse(jsonString), key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JsonArray getJsonArray(JsonObject jsonObject, String key) {
        if (checkEmpty(jsonObject, key))
            return null;

        try {
            return jsonObject.get(key).getAsJsonArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JsonArray getJsonArray(String jsonString, String key) {
        if (checkEmpty(jsonString, key))
            return null;

        try {
            return getJsonArray(parse(jsonString), key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String parse(Object json) {
        try {
            return gson.toJson(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JsonObject parse(String jsonString) {
        try {
            return new JsonParser().parse(jsonString).getAsJsonObject();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JsonArray parseArray(String jsonString) {
        try {
            return new JsonParser().parse(jsonString).getAsJsonArray();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JsonObject getJsonObject(JsonArray jsonArray, int index) {
        return jsonArray.get(index).getAsJsonObject();
    }

    public static String getString(JsonArray jsonArray, int index) {
        return FJsonUtil.parse(jsonArray.get(index));
    }

    public static <T> T getModel(String jsonString, Class<T> clazz) {
        try {
            return gson.fromJson(jsonString, clazz);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T getModel(JsonObject jsonObject, Class<T> clazz) {
        try {
            return gson.fromJson(jsonObject, clazz);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Deprecated
    public static <T> List<T> getModelList(JsonArray jsonArray, Type typeOfT) {
        try {
            return gson.fromJson(jsonArray, typeOfT);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Deprecated
    public static <T> List<T> getModelList(String jsonString, Type typeOfT) {
        try {
            return gson.fromJson(parseArray(jsonString), typeOfT);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> List<T> getModelList(JsonArray jsonArray, TypeToken<List<T>> typeToken) {
        try {
            return gson.fromJson(jsonArray, typeToken.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> List<T> getModelList(String jsonString, TypeToken<List<T>> typeToken) {
        try {
            return gson.fromJson(parseArray(jsonString), typeToken.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void setString(JsonObject jsonObject, String property, String value) {
        if (checkEmpty(jsonObject, property))
            return;
        try {
            jsonObject.addProperty(property, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setBoolean(JsonObject jsonObject, String property, boolean value) {
        if (checkEmpty(jsonObject, property))
            return;
        try {
            jsonObject.addProperty(property, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean checkEmpty(JsonObject jsonObject, String key) {
        if (jsonObject == null || TextUtils.isEmpty(key))
            return true;

        return false;
    }

    private static boolean checkEmpty(String jsonString, String key) {
        if (TextUtils.isEmpty(jsonString) || TextUtils.isEmpty(key))
            return true;

        return false;
    }
}
