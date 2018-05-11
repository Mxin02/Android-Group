package com.mxin.android.group_cniao.nottp;

import com.yolanda.nohttp.rest.Response;

/**
 * Created by hongkl on 16/8/1.
 */
public interface HttpListner<T> {
    void OnSucceed(int what, Response<T> response);
    void onFailed(int what, String url, Object tag, CharSequence message,
                  int responseCode, long networkMillis);
}
