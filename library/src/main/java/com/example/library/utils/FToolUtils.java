package com.example.library.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.example.library.R;
import com.example.library.base.FLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bjhl on 16/6/27.
 *
 * 一些通用工具方法
 */
public class FToolUtils {
    /**
     * 打电话
     *
     * @param context  上下文
     * @param phoneNum 电话号
     */
    public static void callPhone(Context context, String phoneNum) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNum));
            context.startActivity(intent);
        } catch (Exception e) {
            FLog.e("call phone error, e:" + e.getLocalizedMessage());
            FTips.show(context, context.getString(R.string.tx_no_call_app_installed));
        }
    }

    /**
     * 发短信
     *
     * @param context  上下文
     * @param phoneNum 电话号
     * @param text     短信内容
     */
    public static void sendSms(Context context, String phoneNum, String text) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNum));
            if (!TextUtils.isEmpty(text)) {
                intent.putExtra("sms_body", text);
            }
            context.startActivity(intent);
        } catch (Exception e) {
            FLog.e("send sms error, e:" + e.getLocalizedMessage());
            FTips.show(context, context.getString(R.string.tx_no_sms_app_installed));
        }
    }

    /**
     * 打开浏览器
     *
     * @param context
     * @param url
     */
    public static void openBrowser(Context context, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception e) {
            FLog.e("open browser error, e:" + e.getLocalizedMessage());
            FTips.show(context, context.getString(R.string.tx_no_browser_app_installed));
        }
    }
    /**
     * 检查是否合法的手机号 以1开头的11位数字，不是连续数字，不是重复数字
     */
    public static boolean checkPhone(String mobiles) {
        Pattern p = Pattern.compile("^1[0-9]{10}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 是合法的人名 只能是中文以及“·”这个特殊字符，且位数为20位以内
     */
    public static boolean checkUserName(String name) {
        Pattern p = Pattern.compile("^[\u4e00-\u9fa5\\.]{1,20}$");
        Matcher m = p.matcher(name);
        return m.matches();
    }

    /**
     * 是合法的身份证号 15或18位，最后一位可以是字母，其他都必须是数字
     */
    public static boolean checkUserID(String id) {
        Pattern p = Pattern.compile("^[0-9]{14,17}[A-Za-z0-9]$");
        Matcher m = p.matcher(id);
        return m.matches();
    }

    /**
     * 是合法的信用卡有效期 4位数字，前两位大于0小于13
     */
    public static boolean checkCreditPeriod(String id) {
        Pattern p = Pattern.compile("^[0-9]{4}$");
        Matcher m = p.matcher(id);
        if (!m.matches()) {
            return false;
        }
        String month = id.substring(0, 2);
        int monthInt;
        try {
            monthInt = Integer.parseInt(month);
        } catch (Exception e) {
            return false;
        }
        return !(monthInt < 0 || monthInt > 12);
    }

    /**
     * 是合法的信用卡效验码 3或4为数字
     */
    public static boolean checkCreditCVV(String cvv) {
        Pattern p = Pattern.compile("^[0-9]{3,4}$");
        Matcher m = p.matcher(cvv);
        return m.matches();
    }

    /**
     * 检查是否是连续数字
     */
    public static boolean checkSerialNum(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        int[] num = new int[str.length()];
        for (int i = 0; i < num.length; i++) {
            num[i] = str.charAt(i) - '0';
        }
        for (int i = 0; i < num.length - 1; i++) {
            if (num[i + 1] != num[i] + 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查是否是连续重复数字
     */
    public static boolean checkSameChars(String str) {
        Pattern p = Pattern.compile("([\\d])\\1+");
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
