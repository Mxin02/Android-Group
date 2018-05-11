package com.mxin.android.group_cniao.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mxin.android.group_cniao.R;
import com.mxin.android.group_cniao.adapter.CommenAdapter;
import com.mxin.android.group_cniao.entity.FavorInfo;
import com.mxin.android.group_cniao.listener.BmobQueryAllCallback;
import com.mxin.android.group_cniao.utils.BmobManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by Mxin on 2018/4/13.
 */

public class CollectActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.list_collect)
    ListView mListCollect;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        ButterKnife.bind(this);

        BmobManager.getInstance(new BmobQueryAllCallback() {
            @Override
            public void queryAllSuccess(final List<FavorInfo> object) {
                mListCollect.setAdapter(new CommenAdapter<FavorInfo>(object){
                    @Override
                    public View getView(int i, View view, ViewGroup viewGroup) {
                        View collectView = getLayoutInflater().inflate(R.layout.goods_list_item,null);
                        TextView tvTitle = collectView.findViewById(R.id.title);
                        tvTitle.setText(object.get(i).getPruduct());
                        return collectView;
                    }
                });

            }

            @Override
            public void queryAllFailure(BmobException e) {

            }
        }).queryAllData("isFavor", true);

    }
}
