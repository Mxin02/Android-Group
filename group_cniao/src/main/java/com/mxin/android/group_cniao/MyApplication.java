package com.mxin.android.group_cniao;

import android.app.Application;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.yolanda.nohttp.NoHttp;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.update.BmobUpdateAgent;

/**
 * Created by Mxin on 2018/3/30.
 */

public class MyApplication extends Application {

    private boolean flag = true;

    @Override
    public void onCreate() {
        super.onCreate();

        //Nohttp的初始化
        NoHttp.initialize(this);
        //Fresco图片加载框架的初始化
        Fresco.initialize(this);
        //bmob的初始化
        Bmob.initialize(this,"3bb64c8e02ecd42a22f26f0b2a589782");
        //初始化bmob自动更新表
        if (flag){
            flag = false;
            BmobUpdateAgent.initAppVersion();
        }
        //新浪微博的初始化
       // WbSdk.install(this,new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE));
    }
}
