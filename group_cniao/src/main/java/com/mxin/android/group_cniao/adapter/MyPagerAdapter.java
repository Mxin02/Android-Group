package com.mxin.android.group_cniao.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mxin on 2018/4/2.
 */

public class MyPagerAdapter extends PagerAdapter {

    private List<View> mViews = new ArrayList<>();

    public MyPagerAdapter(List<View> views) {
        mViews = views;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = mViews.get(position);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        container.removeView(mViews.get(position));

    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
