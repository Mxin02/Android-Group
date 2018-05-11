package com.mxin.android.group_cniao.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mxin.android.group_cniao.R;
import com.mxin.android.group_cniao.utils.DataCleanManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.b.I;
import cn.bmob.v3.update.BmobUpdateAgent;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment {

    //wifi分享设置
    @BindView(R.id.iv_wifi_switch)
    CheckBox mIvWifiSwitch;
    //消息提示设置
    @BindView(R.id.iv_remind_switch)
    CheckBox mIvRemindSwitch;
    //缓存大小
    @BindView(R.id.cache_size)
    TextView mTvCacheSize;
    //清除缓存
    @BindView(R.id.clear_cache_layout)
    RelativeLayout mClearCacheLayout;
    //赏个好评
    @BindView(R.id.good_comment_layout)
    RelativeLayout mGoodCommentLayout;
    //联系客服
    @BindView(R.id.kefu_layout)
    RelativeLayout mKefuLayout;
    //应用更新
    @BindView(R.id.rl_softvare_update)
    RelativeLayout mRlSoftvareUpdate;
    //帮助
    @BindView(R.id.help_layout)
    RelativeLayout mHelpLayout;
    Unbinder unbinder;

    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_more, container, false);
        unbinder = ButterKnife.bind(this, inflate);

        try{
            String cacheSize = DataCleanManager.getTotalCacheSize(getActivity());
            mTvCacheSize.setText(cacheSize);
        }catch (Exception e){
            e.printStackTrace();
        }
        return inflate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.share_setting_layout, R.id.good_comment_layout, R.id.clear_cache_layout, R.id.kefu_layout, R.id.rl_softvare_update})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.share_setting_layout:

                break;

            case R.id.good_comment_layout:
                openApplicationMarket("");
                break;

            case R.id.clear_cache_layout:
                DataCleanManager.clearAllCache(getActivity());
                Toast.makeText(getActivity(),"缓存清除成功！",Toast.LENGTH_LONG).show();
                mTvCacheSize.setText("0KB");
                break;

            case R.id.kefu_layout:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + "13713963614"));
                //开启系统拨号器
                startActivity(intent);
                break;

            case R.id.rl_softvare_update:

                BmobUpdateAgent.forceUpdate(getActivity());
                break;

        }
    }


    /**
     * 打开应用商店
     * @param packName
     */
    public void openApplicationMarket(String packName){

        try {
            String str = "market://detail?id=" + packName;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(str));
            startActivity(intent);
        }catch (Exception e){
            //打开应用商店失败，可能是手机没有安装应用商店
            Toast.makeText(getActivity(),"打开应用商店失败！",Toast.LENGTH_LONG).show();
            e.printStackTrace();
            //当应用上线
            String url = "";
            //调用浏览器上的应用商店
            openLinkByUri(url);
        }
    }

    /**
     * 调用系统的浏览器打开网页
     * @param url
     */
    private void openLinkByUri(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);

    }

}
