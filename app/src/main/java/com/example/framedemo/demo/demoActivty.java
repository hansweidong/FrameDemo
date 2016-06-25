package com.example.framedemo.demo;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.framedemo.R;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

public class demoActivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_activty);

        RxPermissions.getInstance(this).request(Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean granted) {
                if (granted) {
                } else {
                    Toast.makeText(demoActivty.this,
                            "Permission denied, can't enable the camera",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
