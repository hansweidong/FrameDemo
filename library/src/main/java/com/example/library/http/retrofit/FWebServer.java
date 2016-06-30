package com.example.library.http.retrofit;

import com.example.library.utils.FJsonUtil;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bjhl on 16/6/29.
 *
 * 多个域名的扩展
 */
public class FWebServer {

    private FApi apiInterface;

    public FWebServer(FApi apiInterface) {
        this.apiInterface = apiInterface;
    }

    public static FWebServer getNewInstance(String endPoint) {

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(FJsonUtil.gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .setErrorHandler(new LPErrorHandler())
                .baseUrl(endPoint)
                .build();

        return new FWebServer(retrofit.create(FApi.class));
    }
}
