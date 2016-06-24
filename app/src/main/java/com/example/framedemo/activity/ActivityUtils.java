package com.example.framedemo.activity;

import android.content.Context;

import com.example.framedemo.activity.personcenter.PersonCenterActivty;

/**
 * Created by bjhl on 16/6/24.
 */
public class ActivityUtils {

    public void gotoPersonCenterActivity(Context context, long id) {
        PersonCenterActivty.launch(context, id);
    }
}
