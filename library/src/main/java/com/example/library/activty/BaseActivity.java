package com.example.library.activty;

import android.app.Activity;
import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseActivity extends Activity {

    private boolean isEventBus = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        initVariables();
        initViews(savedInstanceState);
        loadData();
    }

    /**
     * layout布局id
     */
    protected abstract int getLayoutResource();

    /**
     * 初始化变量,包括 Intent 带的数据和 Activity 内的变量
     */
    protected abstract void initVariables();

    /**
     * 初始化控件,为控件挂上事件方法。
     * 
     * @param savedInstanceState
     */
    protected abstract void initViews(Bundle savedInstanceState);

    /**
     * 初始化本地数据、加载网络数据
     */
    protected abstract void loadData();

    @Override
    protected void onStart() {
        super.onStart();
        if (isEventBus){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isEventBus){
            EventBus.getDefault().unregister(this);
        }
    }
}