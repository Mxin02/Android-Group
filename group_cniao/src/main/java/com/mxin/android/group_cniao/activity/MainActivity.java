package com.mxin.android.group_cniao.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxin.android.group_cniao.R;
import com.mxin.android.group_cniao.bean.ContantsPool;
import com.mxin.android.group_cniao.fragment.ArroundFragment;
import com.mxin.android.group_cniao.fragment.MainFragment;
import com.mxin.android.group_cniao.fragment.MineFragment;
import com.mxin.android.group_cniao.fragment.MoreFragment;
import com.mxin.android.group_cniao.utils.DataCleanManager;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mxin.android.group_cniao.bean.ContantsPool.title;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.tabHost)
    FragmentTabHost mTabHost;
    //fragment类
    private Class[] fragments = new Class[]{
            MainFragment.class,
            ArroundFragment.class,
            MineFragment.class,
            MoreFragment.class
    };

    //fragment图片资源
    private int[] imgRes = new int[]{
            R.drawable.ic_tab_artists_selector,
            R.drawable.ic_tab_albums_selector,
            R.drawable.ic_tab_songs_selector,
            R.drawable.ic_tab_playlists_selector

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initFragmentTabHost();
    }

    private void initFragmentTabHost() {
        //初始化
        mTabHost.setup(MainActivity.this,getSupportFragmentManager(),android.R.id.tabcontent);

        for (int i = 0 ;i < title.length; i++){
            View inflate = getLayoutInflater().inflate(R.layout.tab_item, null);
            ImageView ivTab = inflate.findViewById(R.id.iv_tab);
            TextView tvTab = inflate.findViewById(R.id.tv_tab);

            ivTab.setImageResource(imgRes[i]);
            tvTab.setText(title[i]);
            mTabHost.addTab(mTabHost.newTabSpec(ContantsPool.title[i]).setIndicator(inflate),fragments[i],null);
        }

    }

    @Override
    protected void onDestroy() {
        DataCleanManager.cleanSharedPreference(this);
        super.onDestroy();
    }
}
