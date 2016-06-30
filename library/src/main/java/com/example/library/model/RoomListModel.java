package com.example.library.model;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by bjhl on 16/6/29.
 */
public class RoomListModel extends BaseModel {
    @SerializedName("list")
    public ArrayList<RoomInfo> list;
}

 class RoomInfo extends BaseModel{

    @SerializedName("room_id")
    public long roomId;

    @SerializedName("title")
    public String title;

    @SerializedName("start_time")
    public Date startTime;

    @SerializedName("end_time")
    public Date endTime;

    @SerializedName("type")
    public int roomType;

    @SerializedName("cover")
    public String cover;

    @SerializedName("admin_code")
    public String adminCode;

    @SerializedName("teacher_code")
    public String teacherCode;

    @SerializedName("user_code")
    public String userCode;
}
