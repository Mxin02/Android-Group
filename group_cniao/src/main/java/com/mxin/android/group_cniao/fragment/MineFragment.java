package com.mxin.android.group_cniao.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mxin.android.group_cniao.R;
import com.mxin.android.group_cniao.activity.CollectActivity;
import com.mxin.android.group_cniao.activity.LoginActivity;
import com.mxin.android.group_cniao.entity.UserEvent;
import com.sina.weibo.sdk.openapi.models.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment {


    @BindView(R.id.btn_login)
    Button mBtnLogin;
    Unbinder unbinder;
    /** 登录与未登录时的显示 **/
    @BindView(R.id.rl_logined)
    RelativeLayout mLoginLayout;
    @BindView(R.id.rl_unlogin)
    RelativeLayout mUnLoginLayout;
    /** 头像昵称 **/
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.iv_user)
    ImageView mIvUser;

    private View mInflate;

    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mInflate == null) {

            // Inflate the layout for this fragment
            mInflate = inflater.inflate(R.layout.fragment_mine, container, false);
            unbinder = ButterKnife.bind(this, mInflate);

            //进行EventBus的注册
            EventBus.getDefault().register(this);
            mUnLoginLayout = mInflate.findViewById(R.id.rl_unlogin);
            mLoginLayout = mInflate.findViewById(R.id.rl_logined);
        }
        return mInflate;
    }

    //接受订阅事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void test(UserEvent userEvent){
        mUnLoginLayout.setVisibility(View.GONE);
        mLoginLayout.setVisibility(View.VISIBLE);
    }

    //接受订阅事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setQQNickname(String nickname) {
        mUnLoginLayout.setVisibility(View.GONE);
        mLoginLayout.setVisibility(View.VISIBLE);
        mTvName.setText(nickname);
    }

    //接受订阅事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setQQAvtar(Bitmap avtar) {
        mUnLoginLayout.setVisibility(View.GONE);
        mLoginLayout.setVisibility(View.VISIBLE);
        mIvUser.setImageBitmap(avtar);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        //eventbus反注册
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.btn_login, R.id.tv_collect, R.id.tv_recent_view, R.id.iv_arrow_more})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.btn_login:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;

            case R.id.tv_collect:
                startActivity(new Intent(getActivity(), CollectActivity.class));
                break;

            case R.id.tv_recent_view:
                break;

            case  R.id.iv_arrow_more:
                BmobUser.logOut();  //清除缓存用户对象
                mUnLoginLayout.setVisibility(View.GONE);
                mLoginLayout.setVisibility(View.VISIBLE);
                break;

        }
    }
}
