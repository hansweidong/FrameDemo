package com.example.library.http.retrofit;

import com.example.library.base.FException;
import com.example.library.constant.FApiConstants;
import com.example.library.manager.FDeployManager;
import com.example.library.model.BaseModel;
import com.example.library.model.RoomListModel;
import com.example.library.utils.FJsonUtil;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by bjhl on 16/6/29.
 *
 * 网络api
 */
public class FApiManager {

    public static final String TAG = FApiManager.class.getSimpleName();

    private FApi mFApi;

    private FApiManager() {
        Retrofit.Builder build =
            new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(FJsonUtil.gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())// add rxjava
                // .setErrorHandler(new LPErrorHandler())
                .baseUrl(FApiConstants.HOSTS[FDeployManager.getmEnvironmentType().getValue()]);
        if (FDeployManager.getmEnvironmentType() == FDeployManager.EnvironmentType.TYPE_TEST) {
            OkHttpClient client = new OkHttpClient.Builder().addNetworkInterceptor(new StethoInterceptor()).build();
            build.client(client);
        }
        mFApi = build.build().create(FApi.class);
    }

    // 在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final FApiManager INSTANCE = new FApiManager();
    }

    // 获取单例
    public static FApiManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 对网络接口返回的Response进行分割操作
     *
     * @param response
     * @return
     */
    private RuntimeException analyticsResponse(FResponse response) {
        if (response == null) {
            return new NullPointerException("response is null.");
        }
        long errNo = response.code;

        if (!response.isSuccess()) {
            return new FException(errNo, response.message);
        }
        return null;
    }

    /**
     * demo
     *
     * @return
     */

    public Observable<RoomListModel> requestdemo() {
        return mFApi.requestdemo().map(new Func1<FResponse<RoomListModel>, RoomListModel>() {
            @Override
            public RoomListModel call(FResponse<RoomListModel> result) {
                RuntimeException exception = analyticsResponse(result);
                if (exception != null) {
                    throw exception;
                }
                return result.data;
            }
        }).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 下载文件 进度条
     * 
     * @param callback
     */
    public void requestDownloaddemo(FileCallback callback) {
        mFApi.requestDownloaddemo().enqueue(callback);
    }

    /**
     * 上传文件  test
     * @param fileKey 前后端约定
     * @param imagePath
     * @return
     */
    public Observable<BaseModel> requestUpload(Map<String, String> kv, String fileKey, String imagePath, FRequestProgressListener listener) {
        File imgFile = new File(imagePath);
        RequestBody requestBody = FProgressRequestBody.create(MediaType.parse("image/*"), imgFile, listener);

        Map<String, RequestBody> params = new HashMap<>();
        params.put(fileKey+"\"; filename=\""+imgFile.getName()+"",requestBody);
//        params.put("Filedata\"; filename=\""+imgFile.getName()+"",progressRequestBody);

        if (kv != null && !kv.isEmpty()) {
            Iterator<String> iterator = kv.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = kv.get(key);
                RequestBody temp = RequestBody.create(MediaType.parse("text/plain"), value);
                params.put(key, temp);
            }
        }
        return mFApi.requestUpload(params)
                .map(new Func1<FResponse<BaseModel>, BaseModel>() {
                    @Override
                    public BaseModel call(FResponse<BaseModel> result) {
                        RuntimeException exception = analyticsResponse(result);
                        if (exception != null) {
                            throw exception;
                        }
                        return result.data;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
