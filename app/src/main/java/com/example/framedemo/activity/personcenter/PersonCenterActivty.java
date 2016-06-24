package com.example.framedemo.activity.personcenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.framedemo.R;
import com.example.library.activty.BaseActivity;

public class PersonCenterActivty extends BaseActivity {

    public static final String INTENT_IN_ID = "intent-in-id";

    /**
     * 跳转封装
     * @param context
     * @param id
     * @return
     */
    public static Intent createIntent(Context context, long id) {
        Intent i = new Intent(context, PersonCenterActivty.class);
        i.putExtra(INTENT_IN_ID, id);
        return i;
    }

    public static void launch(Context context, long id) {
        Intent i = createIntent(context, id);
        context.startActivity(i);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_person_center_activty;
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
