package com.mxin.android.group_cniao.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mxin.android.group_cniao.R;
import com.mxin.android.group_cniao.adapter.MyPagerAdapter;
import com.mxin.android.group_cniao.entity.DetailInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mxin on 2018/4/12.
 */

public class ImageGalleryActivity extends AppCompatActivity {


    @BindView(R.id.pager_image)
    ViewPager mPagerImage;

    private List<View>  mViewList = new ArrayList<>();
    private List<String> mDetail_imags;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        DetailInfo detailInfo = (DetailInfo) intent.getSerializableExtra("detailInfo");
        mDetail_imags = detailInfo.getResult().getDetail_imags();

        for (int i = 0; i< mDetail_imags.size(); i++){
            View inflate = getLayoutInflater().inflate(R.layout.pager_image_item, null);
            SimpleDraweeView ivItem = inflate.findViewById(R.id.iv_item);
            Uri uri = Uri.parse(mDetail_imags.get(i));
            ivItem.setImageURI(uri);
            mViewList.add(inflate);
        }

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(mViewList);
        mPagerImage.setAdapter(myPagerAdapter);

    }
}
