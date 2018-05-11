package com.mxin.android.group_cniao.listener;

import com.mxin.android.group_cniao.entity.FavorInfo;
import com.mxin.android.group_cniao.entity.GoodsPayInfo;

import java.util.List;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by Mxin on 2018/4/16.
 */

public interface IBmobListener {

    void loginFailure();
    void loginSuccess();
    void querySuccess(FavorInfo object);
    void queryFailure(BmobException e);
    void queryAllSuccess(List<FavorInfo> object);
    void queryAllFailure(BmobException e);
    void queryOrderSuccess(List<GoodsPayInfo> object);
    void queryOrderFailure(BmobException e);
}
