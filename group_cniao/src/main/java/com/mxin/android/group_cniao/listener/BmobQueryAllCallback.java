package com.mxin.android.group_cniao.listener;

import com.mxin.android.group_cniao.entity.FavorInfo;
import com.mxin.android.group_cniao.entity.GoodsPayInfo;

import java.util.List;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by Mxin on 2018/4/18.
 */

public abstract class BmobQueryAllCallback implements IBmobListener {
    @Override
    public void loginFailure() {

    }

    @Override
    public void loginSuccess() {

    }

    @Override
    public void querySuccess(FavorInfo object) {

    }

    @Override
    public void queryFailure(BmobException e) {

    }



    @Override
    public void queryOrderSuccess(List<GoodsPayInfo> object) {

    }

    @Override
    public void queryOrderFailure(BmobException e) {

    }
}
