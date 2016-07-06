package com.example.framedemo.activity.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.framedemo.R;
import com.example.library.activty.BaseActivity;
import com.example.library.http.retrofit.FApiManager;
import com.example.library.http.retrofit.FRequestProgressListener;
import com.example.library.http.retrofit.FileCallback;
import com.example.library.model.BaseModel;
import com.example.library.model.RoomListModel;
import com.example.library.utils.FFileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
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
                //下载demo
                try{
                    File file = FFileUtils.getFile(MainActivity.this,"test.apk");
                    FApiManager.getInstance().requestDownloaddemo(new FileCallback(file) {
                        @Override
                        public void onSuccess(File file) {
                            super.onSuccess(file);
                            System.err.println("onSuccess ");
                        }

                        @Override
                        public void progress(long progress, long total) {
                            System.err.println("progress "+progress +"total "+total);
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            super.onFailure(call,t);
                            System.err.println("onFailure ");
                        }
                    });
                }catch (Exception e){

                }
                //上传demo
                Map<String, String> params = new HashMap<>();
                params.put("token", "xZ/AflPuwkk5cf80pSmCEGKkEFmLbVl+5O6KFqDzdLacgXrhWpMt5ukycoHW2EO2");
                FApiManager.getInstance().requestUpload(params, "Filedata", "/storage/emulated/0/tencent/MicroMsg/WeiXin/microMsg.1467285388478.jpg", new FRequestProgressListener() {
                    @Override
                    public void onProgress(long progress, long total) {
                        System.err.println(""+ progress*100/total);
                    }
                }).subscribe(new Action1<BaseModel>() {
                    @Override
                    public void call(BaseModel baseModel) {

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

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
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
    }
}
