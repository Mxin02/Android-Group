package com.mxin.android.group_cniao.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mxin.android.group_cniao.R;

/**
 * Created by Mxin on 2018/4/3.
 * 通用的ViewHolder,通过控件ID查找控件
 */

public class ViewHolder {
    private View mConvertView;

    //1.效率高，
    //2.key值只能是Integer
    SparseArray<View> mViews = null;

    public ViewHolder(Context context, int layoutID, ViewGroup parent) {
        //第一步封装容器
        mViews = new SparseArray<>();
        mConvertView = LayoutInflater.from(context).inflate(R.layout.goods_list_item, null);
        mConvertView.setTag(this);
    }


    /***
     * 获取一个ViewHolder的对象
     * @param context
     * @param layoutID
     * @param convertView
     * @param parent
     * @return
     */
    public static ViewHolder get(Context context,int layoutID,View convertView,ViewGroup parent){

        if (convertView == null){
            return new ViewHolder(context,layoutID,parent);
        }
        return (ViewHolder) convertView.getTag();

    }

    /***
     * 获取视图控件
     * @param viewID
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewID){

        View view = mViews.get(viewID);
        if (view == null){
            view = mConvertView.findViewById(viewID);
            mViews.put(viewID,view);
        }
        return (T)view;
    }

    public View getConvertView(){
        return mConvertView;
    }

}
