package com.example.library.http.retrofit;

/**
 * Created by bjhl on 16/6/29.
 */

import com.example.library.model.BaseModel;
import com.example.library.model.RoomListModel;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import rx.Observable;

public interface FApi {

    @GET("/demo/room/list")
    Observable<FResponse<RoomListModel>> requestdemo();

    @GET("http://hengdawb-app.oss-cn-hangzhou.aliyuncs.com/app-debug.apk")
    Call<ResponseBody> requestDownloaddemo();

    @Multipart
    @POST("http://openapi.dev.genshuixue.com/appapi/doc/upload")
    Observable<FResponse<BaseModel>> requestUpload(@PartMap Map<String, RequestBody> params);

}