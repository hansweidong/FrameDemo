package com.example.library.mvp.demo;

import com.example.library.http.retrofit.FApiManager;
import com.example.library.model.RoomListModel;

import rx.functions.Action1;

/**
 * Created by bjhl on 16/7/4.
 */
public class FDemoMVPPresenter implements FDemoMVPContract.Presenter {

    FDemoMVPContract.View mView;

    public FDemoMVPPresenter(FDemoMVPContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void deleteTask() {
        //去调用删除任务的网络接口
    }

    @Override
    public void editTask() {

    }

    @Override
    public void start() {
        //load datas
        FApiManager.getInstance().requestdemo().subscribe(new Action1<RoomListModel>() {
            @Override
            public void call(RoomListModel roomListModel) {
                mView.showTitle("");
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
    }
}
