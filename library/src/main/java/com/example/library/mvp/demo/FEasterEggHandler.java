package com.example.library.mvp.demo;

import android.app.Dialog;
import android.view.MotionEvent;
import android.view.View;

import com.example.library.dialog.DialogFactory;

/**
 * Created by bjhl on 16/6/30.
 *
 * 彩蛋
 */
public class FEasterEggHandler {
    private int count;

    private long endTime;

    private View easterView;


    public void setView(View view) {
        easterView = view;
        easterView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                long startTime = System.currentTimeMillis();
                if((startTime - endTime) < 1000) {
                    count++;
                } else {
                    count = 0;
                }
                if(count >= 9) {
                    count = 0;
                    onEvent();
                }
                endTime = System.currentTimeMillis();
                return false;
            }
        });
    }

    public void onEvent() {
        String[] array = {"official", "beta", "dev", "test", "Show App Information"};
        Dialog dialog = DialogFactory.createListDialog(easterView.getContext(), true, 0, "Options", array, 0, new DialogFactory.ListDialogListener() {

            @Override
            public void onListDialogOK(int id, CharSequence[] items, int selectedItem) {
//                if(selectedItem <= 3) {
//                    easterView.getContext().getSharedPreferences("app_setting", 0).edit().putInt("ENVIRNMENT_TYPE", selectedItem).commit();
//                    if (AppContext.getInstance().isLogon()) {
//                        AppContext.getInstance().onLogoff(false);
//                    }
//
//                    AppContext.getInstance().mHandler.postDelayed(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            AppContext.getBroadcast().sendBroadcast(Const.NOTIFY_ACTION.ACTION_EXIT, Const.NOTIFY_TYPE.TYPE_OPERATION_DEFAULT);
//                        }
//                    }, 2000);
//                } else {
//                    StringBuffer buffer = new StringBuffer();
//                    buffer.append("Envirnment:" + AppConfig.getEnvirnmentName()).append("\n");
//                    buffer.append("VersionName:" + AppConfig.APP_VERSION_NAME).append("\n");
//                    buffer.append("VersionCode:" + AppConfig.APP_VERSION_CODE).append("\n\n");
//
//                    buffer.append("Host:" + UrlConstainer.getApiHost()).append("\n");
//                    buffer.append("mHost:" + UrlConstainer.getMHost()).append("\n\n");
//
//                    buffer.append("Channel:" + AppConfig.APP_CHANNEL).append("\n");
//                    buffer.append("Screen(W/H/D):" + AppConfig.screenWidth + "/" + AppConfig.screenHeight + "/" + AppConfig.screenDensity).append("\n");
//                    buffer.append("Mac:" + DeviceInfo.MAC).append("\n");
//                    buffer.append("Mobile:" + DeviceInfo.MOBILE).append("\n");
//                    DialogFactory.createYesNoWarningDialog(easterView.getContext(), 0, "Information", buffer.toString(), 0, null).show();
//                }
            }

            @Override
            public void onListDialogCancel(int id, CharSequence[] items) {

            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }
}

