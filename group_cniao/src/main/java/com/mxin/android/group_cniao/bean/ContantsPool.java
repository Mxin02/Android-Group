package com.mxin.android.group_cniao.bean;

/**
 * Created by Mxin on 2018/3/30.
 * 常量池
 */

public interface ContantsPool {

    //TabHost的fragent标题
    String[] title = new String[]{"首页","周边","我的","更多"};

    String baseUrl = "http://7xij5m.com1.z0.glb.clouddn.com/";

    /**猜你喜欢**/
    String spRecommendURL = baseUrl + "spRecommend.txt";

    String spRecommendURL_NEW = baseUrl + "spRecommend_new.txt";

    /**热门电影**/
    String filmHotUrl = baseUrl + "filmHot_refresh.txt";
}
