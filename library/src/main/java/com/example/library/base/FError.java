package com.example.library.base;

/**
 * Created by bjhl on 16/6/24.
 *
 * 错误
 */
public class FError {

    public static final int CODE_ERROR_SUCCESS = 0;// 成功
    public static final int CODE_ERROR_NETWORK_FAILURE = -0x01;// 失败、无网
    public static final int CODE_ERROR_NETWORK_MOBILE = -0x02;// 当前网络为mobile
    public static final int CODE_ERROR_NETWORK_WIFI = -0x03;// wifi
    public static final int CODE_ERROR_UNKNOWN = -0x04;// 未知错误
    public static final int CODE_ERROR_JSON_PARSE_FAIL = -0x05;// 数据解析失败
    public static final int CODE_ERROR_INVALID_PARAMS = -0x06; // 无效参数
}
