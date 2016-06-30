package com.example.framedemo.activity.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.framedemo.R;
import com.example.library.activty.BaseActivity;
import com.example.library.http.retrofit.FApiManager;
import com.example.library.model.RoomListModel;

import rx.functions.Action1;

public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isEventBus = true;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ((Button)findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FApiManager.getInstance().requestdemo().subscribe(new Action1<RoomListModel>() {
                    @Override
                    public void call(RoomListModel roomListModel) {
                        int k = 0;
                    }
                });
            }
        });
    }

    @Override
    protected void loadData() {
        FApiManager.getInstance().requestdemo().subscribe(new Action1<RoomListModel>() {
            @Override
            public void call(RoomListModel roomListModel) {
                int k = 0;
            }
        });
    }
}
