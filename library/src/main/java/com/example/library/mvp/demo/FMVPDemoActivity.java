package com.example.library.mvp.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.library.R;
import com.example.library.activty.BaseActivity;

public class FMVPDemoActivity extends BaseActivity implements FDemoMVPContract.View{

    FDemoMVPContract.Presenter mPresenter;

    Button mButton;

    /**
     * layout布局id
     */
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_fdemo;
    }

    /**
     * 初始化变量,包括 Intent 带的数据和 Activity 内的变量
     */
    @Override
    protected void initVariables() {
        // Create the presenter
        new FDemoMVPPresenter(this);
    }

    /**
     * 初始化控件,为控件挂上事件方法。
     *
     * @param savedInstanceState
     */
    @Override
    protected void initViews(Bundle savedInstanceState) {
        mButton = (Button)findViewById(R.id.changeBtn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.deleteTask();
            }
        });
//        new FEasterEggHandler().setView(findViewById(R.id.changeBtn));
    }

    /**
     * 初始化本地数据、加载网络数据
     */
    @Override
    protected void loadData() {
        mPresenter.start();
    }

    @Override
    public void hideTitle() {
    }

    @Override
    public void showTitle(String title) {
        mButton.setText(title);
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setPresenter(FDemoMVPContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
