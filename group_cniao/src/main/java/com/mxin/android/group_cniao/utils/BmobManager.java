package com.mxin.android.group_cniao.utils;

import android.util.Log;

import com.mxin.android.group_cniao.entity.BaseModel;
import com.mxin.android.group_cniao.entity.FavorInfo;
import com.mxin.android.group_cniao.entity.GoodsPayInfo;
import com.mxin.android.group_cniao.entity.MyUser;
import com.mxin.android.group_cniao.listener.IBmobListener;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Mxin on 2018/4/16.
 * 单例
 */

public class BmobManager {

    private static BmobManager manager = null;
    private static IBmobListener mListener;
    private static String objID;


    public  static BmobManager getInstance(IBmobListener listener){
        mListener = listener;
        if (manager == null){
            return  new BmobManager();
        }
        return manager;

    }

    public void initListener(IBmobListener listener){
          mListener = listener;
    }
    /**
     * 发送手机验证码
     * @param phoneNumber
     */
    public void requestSMSCode(String phoneNumber){
        BmobSMS.requestSMSCode(phoneNumber, "Mxin", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException ex) {
                if (ex == null){ //验证码发送成功
                  //  mListener.loginFailure();
                }
            }
        });
    }

    /**
     * 通过验证码登录
     * @param phoneNumber
     * @param code
     */
    public void loginBySMSCode(String phoneNumber, String code) {
        BmobUser.loginBySMSCode(phoneNumber, code, new LogInListener<MyUser>() {

            @Override
            public void done(MyUser user, BmobException e) {
                if (user != null) {
                    mListener.loginSuccess();
                }
            }
        });
    }

    public void insertData(BaseModel model){
        model.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    objID = objectId;
                    Log.e("insertData","收藏成功");
                } else {
                    Log.e("insertData","收藏失败");
                }
            }
        });

    }
    public void deleteData(BaseModel model){
        model.setObjectId(objID);
        Log.e("objID", "objID: " + objID);
        model.delete(new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {

                } else {

                }
            }
        });

    }

    /**
     * 根据ID查询数据
     * @param model
     */
    public void queryData(FavorInfo model){
        BmobQuery<FavorInfo> query = new BmobQuery<>();
        query.getObject(objID, new QueryListener<FavorInfo>() {
            @Override
            public void done(FavorInfo object, BmobException e) {
                if (e == null){
                    if (mListener != null){
                        mListener.querySuccess(object);
                    }
                }else {
                    if (mListener != null){
                        mListener.queryFailure(e);
                    }
                }
            }
        });
    }

    public void queryAllData(String queryKey, Object queryValue) {
        BmobQuery<FavorInfo> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo(queryKey, queryValue);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<FavorInfo>() {
            @Override
            public void done(List<FavorInfo> object, BmobException e) {
                if (e == null) {
                    if (mListener != null) {
                        mListener.queryAllSuccess(object);
                    }
                } else {
                    if (mListener != null) {
                        mListener.queryAllFailure(e);
                    }
                }
            }
        });
    }

    /**
     * 订单查询
     * @param queryKey
     * @param queryValue
     */
    public void queryOrderData(String queryKey, Object queryValue) {
        BmobQuery<GoodsPayInfo> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo(queryKey, queryValue);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<GoodsPayInfo>() {
            @Override
            public void done(List<GoodsPayInfo> object, BmobException e) {
                if (e == null) {
                    if (mListener != null) {
                        mListener.queryOrderSuccess(object);
                    }
                } else {
                    if (mListener != null) {
                        mListener.queryOrderFailure(e);
                    }
                }
            }
        });
    }
    public void updateData(){

    }

}
