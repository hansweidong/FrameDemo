package com.example.library.utils;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.example.library.R;

/**
 * Created by bjhl on 16/6/27.
 *
 * 拒绝输入emoj
 */
public class FRejectEmojiWatcher implements TextWatcher {

    public static final String TAG = FRejectEmojiWatcher.class.getSimpleName();

    private Context mContext;
    private EditText mEditText;

    private boolean isResetText;
    private String mBeforeText;

    public FRejectEmojiWatcher(Context context, EditText editText) {
        mContext = context;
        mEditText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.v(TAG, "CharSequence:" + s + " start:" + start + " count:" + count + " after:" + after);
        if (!isResetText) {
            mBeforeText = s.toString();
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.v(TAG, "CharSequence:"+ s+ " start: "+ start+ " before: "+ before+" count: "+count);
        if (!isResetText) {
            CharSequence input = s.subSequence(start, start + count);
            if (input.length() >= 2) {    // 表情的长度不小于2
                if (containsEmoji(input.toString())) {
                    FTips.show(mContext, mContext.getString(R.string.org_init_task_input_emoji_illegitimate));
                    isResetText = true;
                    mEditText.setText(mBeforeText);
                    mEditText.invalidate();
                    CharSequence  c = mEditText.getText();
                    if (c instanceof Spannable) {
                        Spannable spannableText = (Spannable)c;
                        Selection.setSelection(spannableText, mBeforeText.length());
                    }
                }
            }
        } else {
            isResetText = false;
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

        Log.v(TAG, "s: "+s.toString());
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }
}
