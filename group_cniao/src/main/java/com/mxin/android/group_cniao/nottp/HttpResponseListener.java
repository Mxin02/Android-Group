package com.mxin.android.group_cniao.nottp;

import android.content.Context;
import android.content.DialogInterface;


import com.mxin.android.group_cniao.widget.WaitDialog;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

/**
 * Created by hongkl on 16/8/1.
 */
public class HttpResponseListener<T> implements OnResponseListener<T> {

    private WaitDialog mWaitDialog;

    private Request<?> mRequest;

    private HttpListner<T> callback;

    private boolean isLoading;

    /***
     * @param context
     * @param request
     * @param httpListner
     * @param canCancle
     * @param isLoading
     */
    public HttpResponseListener(
            Context context, final Request<?> request,
            HttpListner<T> httpListner, boolean canCancle, boolean isLoading) {
        this.mRequest = request;
        if (context != null && isLoading) {
            mWaitDialog = new WaitDialog(context);
            mWaitDialog.setCancelable(canCancle);
            mWaitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    mRequest.cancel();
                }
            });
        }
        this.callback = httpListner;
        this.isLoading = isLoading;

    }

    @Override
    public void onStart(int what) {
        if (isLoading && mWaitDialog != null && !mWaitDialog.isShowing() ) {
            mWaitDialog.show();
        }
    }

    @Override
    public void onSucceed(int what, Response<T> response) {
        if (callback != null){

            callback.OnSucceed(what,response);
        }
    }

    @Override
    public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
        if (callback != null){
            callback.onFailed(what,url,tag,exception.getMessage(),responseCode,networkMillis);
        }
    }

    @Override
    public void onFinish(int what) {
        if (isLoading && mWaitDialog != null && mWaitDialog.isShowing() ) {
            mWaitDialog.dismiss();
        }
    }
}
