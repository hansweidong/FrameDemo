package com.example.library.http.retrofit;

import com.example.library.error.FErrorConst;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bjhl on 16/6/29.
 */
public class FResponse<T> {
    @SerializedName("code")
    public int code;

    @SerializedName("msg")
    public String message;

    @SerializedName("data")
    public T data;

    public boolean isSuccess() {
        return code == FErrorConst.ERROR_CODE_SUCCESS;
    }

}