package com.mxin.android.group_cniao.listener;

import android.support.v4.view.ViewPager;

import com.mxin.android.group_cniao.widget.ViewPagerIndicator;

/**
 * ViewPager的监听事件
 */

public class MyPagerListner implements ViewPager.OnPageChangeListener {

    private ViewPagerIndicator mIndicator;

    public MyPagerListner(ViewPagerIndicator indicator) {
        mIndicator = indicator;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        //获取圆点的偏移量，监听移动
        mIndicator.setOffX(position,positionOffset);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
