package com.example.library.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Toast;

/**
 * Created by bjhl on 16/6/27.
 *
 * 弹出提示对话框
 */
public class FTips {

    private FTips() {

    }

    private static Toast toast = null;
    private static Object synObj = new Object();

    /**
     * Toast发送消息，默认Toast.LENGTH_SHORT
     */
    public static void show(final Context act, final String msg) {
        show(act, msg, Toast.LENGTH_SHORT);
    }

    /**
     * Toast发送消息，默认Toast.LENGTH_LONG
     */
    public static void showLong(final Context act, final String msg) {
        show(act, msg, Toast.LENGTH_LONG);
    }

    /**
     * Toast发送消息，默认Toast.LENGTH_SHORT
     */
    public static void show(final Context act, final int resId) {
        show(act, resId, Toast.LENGTH_SHORT);
    }

    /**
     * Toast发送消息，默认Toast.LENGTH_LONG
     */
    public static void showLong(final Context act, final int msg) {
        show(act, msg, Toast.LENGTH_LONG);
    }

    /**
     * 如果已经在显示就不显示了
     */
    public static void showDeadline(final Context act, final String msg) {
        if (toast == null || toast.getView() == null || !toast.getView().isShown()) {
            show(act, msg);
        }
    }

    /**
     * Toast发送消息
     */
    public static void show(final Context act, final int msg, final int len) {
        synchronized (synObj) {
            if (toast != null) {
                // toast.cancel();
                toast.setText(msg);
                toast.setDuration(len);
            } else {
                toast = Toast.makeText(act, msg, len);
            }
            toast.show();
        }
    }

    /**
     * Toast发送消息
     */
    public static void show(final Context act, final String msg, final int len) {
        synchronized (synObj) {
            if (toast != null) {
                // toast.cancel();
                toast.setText(msg);
                toast.setDuration(len);
            } else {
                toast = Toast.makeText(act.getApplicationContext(), msg, len);
            }
            toast.show();
        }
    }

    /**
     * toast消失有回调的显示接口
     * @param context
     * @param message
     * @param listener
     */
    public static void showForResult(Context context, String message, final ITXTipListener listener) {
        synchronized (synObj) {
            if (toast != null) {
                // toast.cancel();
                toast.setText(message);
                toast.setDuration(Toast.LENGTH_SHORT);
            } else {
                toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT);
            }
            toast.show();
            //toast 为LENGTH_SHORT的时长
            CountDownTimer timer = new CountDownTimer(2000, 2000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    if (listener != null) {
                        listener.onDismiss();
                    }
                }
            };
            timer.start();
        }
    }

    /**
     * 关闭当前Toast
     */
    public static void cancelCurrentToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

    public interface ITXTipListener{
        void onDismiss();
    }
}

