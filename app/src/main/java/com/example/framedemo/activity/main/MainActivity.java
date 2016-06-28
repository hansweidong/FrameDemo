package com.example.framedemo.activity.main;

import android.os.Bundle;

import com.example.library.activty.FBaseActivity;
import com.example.framedemo.R;


public class MainActivity extends FBaseActivity {

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

    }

    @Override
    protected void loadData() {

    }
}
