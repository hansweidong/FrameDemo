package com.example.library.http.retrofit;

/**
 * Created by bjhl on 16/6/29.
 */

import com.example.library.model.RoomListModel;

import retrofit2.http.GET;
import rx.Observable;

public interface FApi {

    @GET("/demo/room/list")
    Observable<FResponse<RoomListModel>> requestdemo();
}