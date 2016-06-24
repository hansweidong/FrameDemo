package com.example.library.utils;

/**
 * Created by bjhl on 16/6/24.
 * 类型转换
 */
public class TypeConverterUtils {

    /**
     * Object-------int
     * @param value
     * @param defaultValue
     * @return
     */
    public final static int convertToInt(Object value, int defaultValue) {
        if (value == null || "".equals(value.toString().trim())) {
            return defaultValue;
        }
        try {
            return Integer.valueOf(value.toString());
        } catch (Exception e) {
            try {
                return Double.valueOf(value.toString()).intValue();
            } catch (Exception e1) {

                return defaultValue;
            }
        }
    }
}
