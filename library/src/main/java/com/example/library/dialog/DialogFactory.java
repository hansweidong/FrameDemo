package com.example.library.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.library.R;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bjhl on 16/6/30.
 */
public class DialogFactory {

    /**
     * 1 创建默认无标题,可取消,取消无事件的Dialog
     *
     * @param baseContext
     * @param msg
     * @return
     */
    public static Dialog createIndetemineProgressDialog(Context baseContext, String msg) {
        Dialog progressDialog = createIndeterminateProgressDialog(baseContext, msg, true, true, null);
        return progressDialog;
    }

    /**
     * 循环滚动进度条Dialog
     *
     * @return
     */
    public static ProgressDialog createIndeterminateProgressDialog(final Context context, String title, String message, boolean indeterminate,
                                                                   boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setIndeterminate(indeterminate);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(false);
        if (cancelListener != null) {
            dialog.setOnCancelListener(cancelListener);
        }
        return dialog;

    }

    public static ProgressDialog createButtonIndeterminateProgressDialog(final Context context, String title, String message, boolean indeterminate,
                                                                         boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setIndeterminate(indeterminate);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        if (cancelListener != null) {
            dialog.setOnCancelListener(cancelListener);
        }
        return dialog;

    }

    public static Dialog createIndeterminateProgressDialog(final Context context, String message, boolean indeterminate, boolean cancelable,
                                                           DialogInterface.OnCancelListener cancelListener) {

        final Dialog dialog = new Dialog(context, R.style.MyDialog);
        dialog.setContentView(R.layout.dialog_progress_style);
        dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
        TextView progressContent = (TextView) dialog.findViewById(R.id.dialog_downloading);
        progressContent.setText(message);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(cancelable);
        if (cancelListener != null) {
            dialog.setOnCancelListener(cancelListener);
        }
        return dialog;
    }

    public interface ProgressDialogListener {
        public void onProgressDialogCancel();
    }

    /**
     * 输入型Dialog的回调
     */
    public interface InputDialogListener {
        public void onInputDialogOK(int id, String value);

        public void onInputDialogCancel(int id);
    }

    public static Dialog createInputDialogShowSoftInput(final Context context, final int id, final String title, final String content, final String edhint,
                                                        String edContent, final InputDialogListener listener, final int max) {
        return createInputDialogShowSoftInput(context, id, title, content, edhint, edContent, listener, max, InputType.TYPE_CLASS_TEXT);
    }

    /**
     * 弹出对话框的同时弹出软键盘
     *
     * @param context
     * @param id
     * @param title
     * @param content
     * @param edhint
     * @param edContent
     * @param listener
     * @param max
     * @return
     */
    public static Dialog createInputDialogShowSoftInput(final Context context, final int id, final String title, final String content, final String edhint,
                                                        String edContent, final InputDialogListener listener, final int max, int inputType) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setCancelable(true);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                if (context instanceof Activity) {
                    ((Activity) context).removeDialog(id);
                }
                if (listener != null) {
                    listener.onInputDialogCancel(id);
                }
            }
        });
        LayoutInflater factory = LayoutInflater.from(context);
        final View view = factory.inflate(R.layout.alert_dialog_text_entry, null);
        final TextView tvHint = (TextView) view.findViewById(R.id.tv_hint);
        if (content == null || content.equals("")) {
            tvHint.setVisibility(View.GONE);
        }
        else {
            tvHint.setVisibility(View.VISIBLE);
            tvHint.setText(content);
        }
        final EditText etInput = (EditText) view.findViewById(R.id.et_input);
        etInput.setText(edContent);

        etInput.setHint(edhint);

        etInput.requestFocus();
        etInput.setInputType(inputType);
        etInput.setSingleLine(false);
        etInput.setMinLines(1);
        etInput.setMaxLines(3);

        openSoftKeybroad(context, etInput);// 打开键盘

        if (edContent != null) {
            etInput.setSelection(edContent.length());
        }

        final Toast toast = Toast.makeText(context, context.getString(R.string.dialog_input_number_count_limit), Toast.LENGTH_SHORT);
        TextWatcher mCountWatcher = new TextWatcher() {
            private CharSequence temp;
            private int startEdit;
            private int endEdit;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                startEdit = etInput.getSelectionStart();
                endEdit = etInput.getSelectionEnd();
                etInput.setVisibility(View.VISIBLE);
                String textString = temp.toString();
                int currentLength = getGBKLength(temp.toString());
                if (currentLength > max) {
                    toast.cancel();
                    toast.show();
                    textString = subStringByMaxByteCount(textString, max);
                    // s.delete(startEdit - (currentLength - max), endEdit);
                    etInput.setText(textString);
                    etInput.setSelection(textString.length());
                }
            }
        };
        etInput.addTextChangedListener(mCountWatcher);

        builder.setView(view);
        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = etInput.getText().toString();
                if (context instanceof Activity) {
                    ((Activity) context).removeDialog(id);
                }
                if (listener != null) {
                    listener.onInputDialogOK(id, value);
                }
                // 关闭键盘
                closeSoftKeybroad(context, etInput);

            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (context instanceof Activity) {
                    ((Activity) context).removeDialog(id);
                }
                if (listener != null) {
                    listener.onInputDialogCancel(id);
                }
                // 关闭键盘
                closeSoftKeybroad(context, etInput);
            }
        });
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        // dialog.show();
        return dialog;
    }

    public static String subStringByMaxByteCount(String s, int maxByteCount) {
        if (TextUtils.isEmpty(s))
            return s;
        int count = 0;
        StringBuilder buidler = new StringBuilder();
        int i = 0;
        for (; i < s.length(); i++) {
            if (count < maxByteCount) {
                int ascii = Character.codePointAt(s, i);
                if (ascii >= 0 && ascii <= 255) {
                    count++;
                }
                else {
                    if ((maxByteCount - count) == 1) {
                        i--;
                        break;
                    }
                    else {
                        count += 2;
                    }
                }
            }
            else {
                break;
            }
        }

        return buidler.append(s.subSequence(0, i)).toString();
    }

    public static int getGBKLength(String s) {
        // s.getBytes(Charset.forName("GBK"))
        int length = 0;
        try {
            length = s.getBytes("GBK").length;
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return length;
    }

    /**
     * 自动打开键盘
     *
     * @param context
     * @param v
     */
    private static void openSoftKeybroad(final Context context, final View v) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() { // 弹出软键盘的代码
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(v, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }, 300);
    }

    /**
     * 自动关闭键盘
     *
     * @param context
     * @param v
     */
    private static void closeSoftKeybroad(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * <p>
     * Create an input dialog
     * </p>
     *
     * @param context
     *            {@link Context} object
     * @param id
     *            dialog id
     * @param title
     *            hint message
     * @param listener
     *            {@link InputDialogListener} instance, should not be null
     * @return an input dialog instance
     */
    public static Dialog createInputDialog(final Context context, final int id, final String title, final String content, final String edhint,
                                           String edContent, final InputDialogListener listener, final int max) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setCancelable(true);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                if (context instanceof Activity) {
                    ((Activity) context).removeDialog(id);
                }
                if (listener != null) {
                    listener.onInputDialogCancel(id);
                }
            }
        });
        LayoutInflater factory = LayoutInflater.from(context);
        final View view = factory.inflate(R.layout.alert_dialog_text_entry, null);
        final TextView tvHint = (TextView) view.findViewById(R.id.tv_hint);
        if (content == null || content.equals("")) {
            tvHint.setVisibility(View.GONE);
        }
        else {
            tvHint.setVisibility(View.VISIBLE);
            tvHint.setText(content);
        }
        final EditText etInput = (EditText) view.findViewById(R.id.et_input);
        etInput.setText(edContent);
        if (edContent != null) {
            etInput.setSelection(edContent.length());
        }
        etInput.setHint(edhint);
        etInput.setSingleLine(false);
        etInput.setMinLines(1);
        etInput.setMaxLines(3);
        // InputFilter.LengthFilter[] lengthFilters = new
        // InputFilter.LengthFilter[] { new InputFilter.LengthFilter(
        // max) };
        // etInput.setFilters(lengthFilters);
        final Toast toast = Toast.makeText(context, context.getString(R.string.dialog_input_number_count_limit), Toast.LENGTH_SHORT);
        TextWatcher mCountWatcher = new TextWatcher() {
            private CharSequence temp;
            private int startEdit;
            private int endEdit;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                startEdit = etInput.getSelectionStart();
                endEdit = etInput.getSelectionEnd();
                etInput.setVisibility(View.VISIBLE);
                int currentLength = temp.length();
                if (currentLength > max) {
                    toast.cancel();
                    toast.show();
                    s = s.delete(startEdit - (currentLength - max), endEdit);
                    int tempSelection = endEdit;
                    etInput.setText(s);
                    etInput.setSelection(tempSelection);
                }
            }
        };
        etInput.addTextChangedListener(mCountWatcher);

        builder.setView(view);
        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = etInput.getText().toString();
                if (context instanceof Activity) {
                    ((Activity) context).removeDialog(id);
                }
                if (listener != null) {
                    listener.onInputDialogOK(id, value);
                }
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (context instanceof Activity) {
                    ((Activity) context).removeDialog(id);
                }
                if (listener != null) {
                    listener.onInputDialogCancel(id);
                }
            }
        });
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        // dialog.show();
        return dialog;
    }

    /** dialog窗口当前聚焦的选项 */
    public static int dialog_Which;

    /**
     * 包含List控件的Dialog（不用）
     *
     * @param context
     * @param id
     * @param title
     * @param items
     * @param initialSelection
     * @param listener
     * @return
     */
    public static Dialog createListDialog(final Context context, boolean needChoice, final int id, String title, final CharSequence[] items,
                                          int initialSelection, final ListDialogListener listener) {
        if (needChoice) {
            return createSingleChoiceListDialog(context, true, id, title, items, initialSelection, listener);
        }
        else {
            return createListDialog(context, id, title, items, initialSelection, listener);
        }
    }

    // private static Dialog createListDialog(final Context context, final int
    // id, String title, final CharSequence[] items, int initialSelection, final
    // ListDialogListener listener) {
    // dialog_Which = initialSelection;
    //
    // Builder builder = new AlertDialog.Builder(context);
    // builder.setTitle(title);
    // builder.setCancelable(true);
    // builder.setItems(items, new DialogInterface.OnClickListener() {
    //
    // @Override
    // public void onClick(DialogInterface dialog, int which) {
    // if (listener != null) {
    // listener.onListDialogOK(id, items, which);
    // }
    // }
    // });
    // builder.setOnCancelListener(new OnCancelListener() {
    //
    // @Override
    // public void onCancel(DialogInterface dialog) {
    // if (listener != null) {
    // listener.onListDialogCancel(id, items);
    // }
    // }
    // });
    // Dialog dialog = builder.create();
    // dialog.setCanceledOnTouchOutside(true);
    // return dialog;
    // }

    private static Dialog createListDialog(final Context context, final int id, String title, final CharSequence[] items, int initialSelection,
                                           final ListDialogListener listener) {

        dialog_Which = initialSelection;

        final Dialog dialog = new Dialog(context, R.style.MyDialog);
        dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
        dialog.setContentView(R.layout.dialog_list);
        ListView listview = (ListView) dialog.findViewById(R.id.listview);
        TextView textView = (TextView) dialog.findViewById(R.id.dialog_title);
        if (!TextUtils.isEmpty(title)) {
            textView.setText(title);
        }
        else {
            textView.setVisibility(View.GONE);
        }
        listview.setDivider(null);
        MyListAdapter mAdapter = new MyListAdapter(context, items);
        listview.setAdapter(mAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                if (listener != null) {
                    listener.onListDialogOK((int) id, items, position);
                    dialog.dismiss();
                }
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
                if (listener != null) {
                    listener.onListDialogCancel(id, items);
                }
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        // dialog.show();
        return dialog;

    }

    static class MyListAdapter extends BaseAdapter {
        CharSequence[] mItems;
        LayoutInflater mInflater;

        public MyListAdapter(Context context, CharSequence[] items) {

            mItems = items;

            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mItems.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mItems[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder = null;

            if (convertView == null) {
                holder = new Holder();
                convertView = mInflater.inflate(R.layout.dialog_list_item, null);
                holder.tvContent = (TextView) convertView.findViewById(R.id.list_itme);
                holder.view = convertView.findViewById(R.id.view);
                convertView.setTag(holder);
            }
            else {
                holder = (Holder) convertView.getTag();
            }
            CharSequence item = mItems[position];
            if (item != null) {
                if (position == mItems.length - 1) {
                    holder.view.setVisibility(View.INVISIBLE);
                }
                else {
                    holder.view.setVisibility(View.VISIBLE);
                }
            }
            holder.tvContent.setText(item.toString());
            return convertView;
        }

        class Holder {
            TextView tvContent;
            View view;
        }

    }

    public static Dialog createSingleChoiceListDialog(final Context context, final boolean needConfirm, final int id, String title, final CharSequence[] items,
                                                      int initialSelection, final ListDialogListener listener) {
        dialog_Which = initialSelection;
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setCancelable(true);
        builder.setSingleChoiceItems(items, initialSelection, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog_Which = which;

                if (!needConfirm) {
                    dialog.dismiss();
                    if (listener != null) {
                        listener.onListDialogOK(id, items, dialog_Which);
                    }
                }
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                if (listener != null) {
                    listener.onListDialogCancel(id, items);
                }
            }
        });
        if (needConfirm) {
            builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) {
                        listener.onListDialogOK(id, items, dialog_Which);
                    }
                }
            });
        }
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.onListDialogCancel(id, items);
                }
            }
        });
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        // dialog.show();
        return dialog;
    }

    public static class WarningDialogAdapter implements WarningDialogListener {
        @Override
        public void onWarningDialogOK(int id) {
        }

        @Override
        public void onWarningDialogCancel(int id) {
        }

        @Override
        public void onWarningDialogMiddle(int id) {
        }
    }

    /**
     * 列表型Dialog的回调
     */
    public interface ListDialogListener {
        public void onListDialogOK(int id, CharSequence[] items, int selectedItem);

        public void onListDialogCancel(int id, CharSequence[] items);

    }

    /**
     * <p>
     * 4Create a warning dialog
     * </p>
     *
     * @param context
     *            {@link Context} object
     * @param id
     *            dialog id
     * @param title
     *            title
     * @param warning
     *            warning message, should not be null
     * @param listener
     *            {@link WarningDialogListener} instance, should not be null
     * @return a warning dialog instance
     */

    public static Dialog createSureCancleWarningDialog(Context context, int id, String title, String warning, int resId, WarningDialogListener listener) {
        Dialog dlg = createWarningDialog(context, id, title, warning, context.getString(R.string.dialog_ok), context.getString(R.string.dialog_cancel), resId,
                listener);
        return dlg;
    }

    public static Dialog createYesNoWarningDialog(Context context, int id, String title, String warning, int resId, WarningDialogListener listener) {
        Dialog dlg = createWarningDialog(context, id, title, warning, context.getString(R.string.dialog_yes), context.getString(R.string.dialog_no), resId,
                listener);
        return dlg;
    }

    public static Dialog createYesNoWarningDialog(Context context, int id, String title, String warning, String positiveButton, String negativeButton,
                                                  int resId, WarningDialogListener listener) {
        Dialog dlg = createWarningDialog(context, id, title, warning, positiveButton, negativeButton, resId, listener);
        return dlg;
    }

    public static Dialog createWarningDialog(final Context context, final int id, String title, String warning, String positiveButton, String negativeButton,
                                             int resId, final WarningDialogListener listener) {
        return createWarningDialog(context, id, title, warning, positiveButton, negativeButton, null, resId, listener);
    }

    public static Dialog createWarningDialog(final Context context, final int id, String title, String warning, String positiveButton, String negativeButton,
                                             String middleButton, int resId, final WarningDialogListener listener) {
        return createWarningDialog(context, id, title, warning, positiveButton, negativeButton, middleButton, resId, listener, true);
    }

    public static Dialog createWarningDialog(final Context context, final int id, String title, String warning, String positiveButton, String negativeButton,
                                             String middleButton, int resId, final WarningDialogListener listener, final boolean isback) {
        final Dialog dialog = new Dialog(context, R.style.MyDialog);
        dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
        dialog.setContentView(R.layout.dialog_warningdialog_normal);

        TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialog_warning_title);
        TextView dialogContent = (TextView) dialog.findViewById(R.id.dialog_warning_content);
        LinearLayout doubleBtnLayout = (LinearLayout) dialog.findViewById(R.id.ll_dialog_bottom_button);
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btnOK = (Button) dialog.findViewById(R.id.btn_ok);
        Button btnSingleOK = (Button) dialog.findViewById(R.id.btn_single_ok);
        dialog.setCanceledOnTouchOutside(true);
        if (TextUtils.isEmpty(title)) {
            dialogTitle.setVisibility(View.GONE);
        }
        else {
            dialogTitle.setVisibility(View.VISIBLE);
            dialogTitle.setText(title);
        }

        dialogContent.setText(warning);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
                if (listener != null) {
                    listener.onWarningDialogCancel(id);
                }
            }
        });
        if (!TextUtils.isEmpty(positiveButton)) {
            btnOK.setText(positiveButton);
            btnSingleOK.setText(positiveButton);
            btnOK.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (listener != null) {
                        listener.onWarningDialogOK(id);
                    }

                    dialog.dismiss();
                }
            });
        }
        if (!TextUtils.isEmpty(negativeButton)) {
            btnCancel.setText(negativeButton);
            btnCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (listener != null) {
                        listener.onWarningDialogCancel(id);
                    }
                    dialog.dismiss();
                }
            });
        }
        else {
            doubleBtnLayout.setVisibility(View.INVISIBLE);
            btnSingleOK.setVisibility(View.VISIBLE);

        }
        btnSingleOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (listener != null) {
                    listener.onWarningDialogOK(id);
                }
                dialog.dismiss();
            }
        });
        // dialog.show();
        return dialog;
    }

    /**
     * 警告型Dialog的回调
     */
    public interface WarningDialogListener {
        public void onWarningDialogOK(int id);

        public void onWarningDialogCancel(int id);

        public void onWarningDialogMiddle(int id);

    }

    public interface CheckDialogListener {
        public void onCheckDialogCancel();

        public void onCheckDialogOk(boolean[] selected);
    }

    private static boolean[] selected;

    public static Dialog createCheckDialog(final Context context, final String title, final CharSequence[] items, final CheckDialogListener listener) {
        return createCheckDialog(context, title, items, null, listener);
    }

    public static Dialog createCheckDialog(final Context context, final String title, final CharSequence[] items, boolean[] checked,
                                           final CheckDialogListener listener) {
        selected = new boolean[items.length];
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setCancelable(true);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                if (listener != null) {
                    listener.onCheckDialogCancel();
                }
            }
        });

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LayoutInflater factory = LayoutInflater.from(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        CharSequence item = null;
        for (int i = 0; i < items.length; i++) {
            item = items[i];
            View view = factory.inflate(R.layout.alert_dialog_check, null);
            ((TextView) view.findViewById(R.id.cd_hint)).setText(item);
            if (checked != null && i < checked.length) {
                if (checked[i]) {
                    ((CheckBox) view.findViewById(R.id.cd_checkbox)).setChecked(true);
                    selected[i] = true;
                }
            }
            view.setBackgroundResource(R.drawable.selectable_item_background);
            view.setOnClickListener(mCheckDialogItemClickListener);
            view.setTag(i);
            linearLayout.addView(view, params);
            if (i != items.length - 1) {
                ImageView line = new ImageView(context);
                line.setBackgroundResource(R.drawable.line_gray);
                linearLayout.addView(line, lineParams);
            }
        }

        builder.setView(linearLayout);
        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.onCheckDialogOk(selected);
                }
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.onCheckDialogCancel();
                }
            }
        });
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        // dialog.show();
        return dialog;
    }

    private static View.OnClickListener mCheckDialogItemClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int index = (Integer) v.getTag();
            CheckBox checkBox = (CheckBox) v.findViewById(R.id.cd_checkbox);
            checkBox.setChecked(!checkBox.isChecked());
            selected[index] = checkBox.isChecked();
        }
    };

}