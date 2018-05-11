package com.mxin.android.group_cniao.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.mxin.android.group_cniao.R;
import com.mxin.android.group_cniao.bean.ContantsPool;
import com.mxin.android.group_cniao.entity.DetailInfo;
import com.mxin.android.group_cniao.entity.FavorInfo;
import com.mxin.android.group_cniao.listener.BmobQueryAllCallback;
import com.mxin.android.group_cniao.nottp.CallServer;
import com.mxin.android.group_cniao.nottp.HttpListner;
import com.mxin.android.group_cniao.utils.BmobManager;
import com.mxin.android.group_cniao.widget.ObservableScrollView;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by Mxin on 2018/4/9.
 */

public class DetailActivity extends AppCompatActivity implements HttpListner<String>,ObservableScrollView.ScrollViewListener {

    @BindView(R.id.iv_detail)
    SimpleDraweeView mIvDetail;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_decs)
    TextView mTvDecs;
    @BindView(R.id.textView)
    TextView mTextView;
    @BindView(R.id.tv_bought)
    TextView mTvBought;
    @BindView(R.id.tv_title2)
    TextView mTvTitle2;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.web_detail)
    WebView mWebDetail;
    @BindView(R.id.web_notice)
    WebView mWebNotice;
    @BindView(R.id.list_recommend)
    ListView mListRecommend;

    /** 自定义的scrollView **/
    @BindView(R.id.scrollView)
    ObservableScrollView mScrollView;
    /** titlebar的标题 **/
    @BindView(R.id.tv_titlebar)
    TextView mTvTitlebar;

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_favor)
    ImageView mIvFavor;
    @BindView(R.id.iv_share)
    ImageView mIvShare;
    @BindView(R.id.layout_title)
    RelativeLayout mLayoutTitle;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_value)
    TextView mTvValue;
    @BindView(R.id.btn_buy)
    Button mBtnBuy;
    @BindView(R.id.layout_buy)
    RelativeLayout mLayoutBuy;
    private DetailInfo mDetailInfo;
    private float mImageHeight;
    private boolean isFavor = false;//是否喜欢收藏
    private String mObjID;//bmob收藏的商品id
    private String mGoods_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mGoods_id = intent.getStringExtra("goods_id");

        /** 商品详情的请求 **/
        Request<String> recommentRequest = NoHttp.createStringRequest(ContantsPool.baseUrl + mGoods_id + ".txt", RequestMethod.GET);
        //创建队列
        CallServer.getRequestInstance().add(this, 0, recommentRequest, this, true, true);

        initData();
        initListener();
    }

    /**
     * 初始化操作
     */
    private void initData() {
        //查询数据表，该商品有没有被收藏
        BmobManager.getInstance(new BmobQueryAllCallback() {
            @Override
            public void queryAllSuccess(List<FavorInfo> object) {
                boolean favor = object.get(0).isFavor();
                mIvFavor.setImageResource(favor ?R.drawable.icon_uncollect_black:R.drawable.icon_collected_black);
                isFavor = favor?false :true;
                mObjID = object.get(0).getObjectId();
            }

            @Override
            public void queryAllFailure(BmobException e) {

            }
        }).queryAllData("goods_id",mGoods_id);

    }

    /***
     * 获取顶部的图片高度，设置滚动监听
     */
    private void initListener() {

        ViewTreeObserver vto = mIvDetail.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mIvDetail.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                mImageHeight = mIvDetail.getHeight();
                mScrollView.setScrollViewListener(DetailActivity.this);
            }
        });
    }

    @Override
    public void OnSucceed(int what, Response<String> response) {

        switch (what) {
            case 0:
                Gson gson = new Gson();
                mDetailInfo = gson.fromJson(response.get(), DetailInfo.class);
                //本单详情的网页信息
                String details = mDetailInfo.getResult().getDetails();
                //温馨提示
                String notice = mDetailInfo.getResult().getNotice();

                mWebDetail.loadDataWithBaseURL("about:blank",details,"text/html","utf-8",null);
                mWebNotice.loadDataWithBaseURL("about:blank",notice,"text/html","utf-8",null);
                //标题
                mTvTitle.setText(mDetailInfo.getResult().getProduct());
                //描述
                mTvDecs.setText(mDetailInfo.getResult().getTitle());
                //已售
                /*mTvBought.setText(mDetailInfo.getResult().getComment());*/
                Uri uri = Uri.parse(mDetailInfo.getResult().getImages().get(0).getImage());
                mIvDetail.setImageURI(uri);
                mTvTitle2.setText(mDetailInfo.getResult().getProduct());
                mTvPrice.setText(mDetailInfo.getResult().getPrice());
                mTvValue.setText(mDetailInfo.getResult().getValue());
             //   mTvValue.setPaintFlags(mTvValue.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                break;
        }
    }

    @Override
    public void onFailed(int what, String url, Object tag, CharSequence message, int responseCode, long networkMillis) {

    }

    @OnClick({R.id.iv_detail, R.id.iv_back, R.id.iv_favor, R.id.iv_share, R.id.btn_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_detail:
                Intent intent = new Intent(DetailActivity.this, ImageGalleryActivity.class);
                intent.putExtra("detailInfo",mDetailInfo);
                startActivity(intent);

                break;
            case R.id.iv_back:
                finish();
                break;
            //收藏功能
            case R.id.iv_favor:
                FavorInfo favorInfo = new FavorInfo();
                favorInfo.setGoods_id(mDetailInfo.getResult().getGoods_id());
                favorInfo.setFavor(true);
                favorInfo.setPruduct(mDetailInfo.getResult().getProduct());
                favorInfo.setPrice(mDetailInfo.getResult().getPrice());
                favorInfo.setValue(mDetailInfo.getResult().getValue());
                favorInfo.setImageUrl(mDetailInfo.getResult().getImages().get(0).getImage());
                if (!isFavor){
                    Toast.makeText(DetailActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                    mIvFavor.setImageResource(R.drawable.icon_collected_black);
                    BmobManager.getInstance(null).insertData(favorInfo);
                    isFavor = true;
                }else {
                    Toast.makeText(DetailActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                    mIvFavor.setImageResource(R.drawable.icon_uncollect_black);
                    BmobManager.getInstance(null).deleteData(favorInfo);
                    isFavor = false;
                }
                break;
            case R.id.iv_share:
                break;
            case R.id.btn_buy:
                break;
        }
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        /** 高度没有改变 **/
        if (y <= 0){
            mTvTitlebar.setVisibility(View.GONE);
            mLayoutTitle.setBackgroundColor(Color.argb(0,0,0,0));
        }else if (y > 0 && y <= mImageHeight){
            float scale = y / mImageHeight;
            float alpha = (255 * scale);
            mTvTitlebar.setVisibility(View.VISIBLE);
            mTvTitlebar.setText(mDetailInfo.getResult().getProduct());
            mTvTitlebar.setTextColor(Color.argb((int) alpha,0,0,0));
            mLayoutTitle.setBackgroundColor(Color.argb((int)alpha,255,255,255));
        } else if (y>mImageHeight){
            mTvTitlebar.setVisibility(View.VISIBLE);
            mTvTitlebar.setText(mDetailInfo.getResult().getProduct());
            mTvTitlebar.setTextColor(Color.argb(0,0,0,0));
            mLayoutTitle.setBackgroundColor(Color.argb(0,255,255,255));

        }
    }
}
