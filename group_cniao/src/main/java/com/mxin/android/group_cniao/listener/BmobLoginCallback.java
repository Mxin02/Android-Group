package com.mxin.android.group_cniao.listener;



import com.mxin.android.group_cniao.entity.FavorInfo;
import com.mxin.android.group_cniao.entity.GoodsPayInfo;

import java.util.List;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by hongkl on 16/9/6.
 */
public abstract class BmobLoginCallback implements IBmobListener {

    @Override
    public void queryOrderSuccess(List<GoodsPayInfo> object) {

    }

    @Override
    public void queryOrderFailure(BmobException e) {

    }

    @Override
    public void querySuccess(FavorInfo object) {

    }

    @Override
    public void queryFailure(BmobException e) {

    }

    @Override
    public void queryAllSuccess(List<FavorInfo> object) {

    }

    @Override
    public void queryAllFailure(BmobException e) {

    }
}
