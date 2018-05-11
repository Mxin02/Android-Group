package com.mxin.android.group_cniao.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

/**
 * Created by Mxin on 2018/3/26.
 */

public class WaitDialog extends ProgressDialog {
    public WaitDialog(Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setProgressStyle(STYLE_SPINNER);
        setMessage("正在请求，请稍后……");
    }

    public WaitDialog(Context context, int theme) {
        super(context, theme);
    }
}
