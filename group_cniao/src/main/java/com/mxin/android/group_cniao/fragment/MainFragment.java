package com.mxin.android.group_cniao.fragment;


import android.content.Intent;
import android.content.res.TypedArray;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.mxin.android.group_cniao.R;
import com.mxin.android.group_cniao.activity.DetailActivity;
import com.mxin.android.group_cniao.adapter.MyGridAdapter;
import com.mxin.android.group_cniao.adapter.MyPagerAdapter;
import com.mxin.android.group_cniao.adapter.ViewHolder;
import com.mxin.android.group_cniao.bean.ContantsPool;
import com.mxin.android.group_cniao.entity.FilmInfo;
import com.mxin.android.group_cniao.entity.GoodsInfo;
import com.mxin.android.group_cniao.entity.HomeIconInfo;
import com.mxin.android.group_cniao.listener.MyPagerListner;
import com.mxin.android.group_cniao.nottp.CallServer;
import com.mxin.android.group_cniao.nottp.HttpListner;
import com.mxin.android.group_cniao.utils.DataCleanManager;
import com.mxin.android.group_cniao.widget.ViewPagerIndicator;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements ContantsPool, HttpListner<String> {


    @BindView(R.id.listView)
    ListView mListView;
    Unbinder unbinder;

    //private List<String> mData = new ArrayList<>();
    /***
     * girdView两页的数据
     */
    private List<HomeIconInfo> mPagerOneData = new ArrayList<>();
    private List<HomeIconInfo> mPagerTwoData = new ArrayList<>();
    private View mInflate;
    private ViewPagerIndicator mIndicator;
    /** 添加gridView的容器**/
    private List<View> mViews = new ArrayList<>();

   /** 自定义的商品存放容器**/
    private List<GoodsInfo.ResultBean.GoodlistBean> mDatalist = new ArrayList<>();
    private ViewPager mPagerAdaver;
    private ViewPagerIndicator mIndicatorAdver;

    /**
     * 自定义一个存放图片的容器
     **/
    private int[] resID = new int[]{R.mipmap.a01, R.mipmap.a01, R.mipmap.a01, R.mipmap.a01};
    //广告条的数据
    private List<View> mViewsAdver = new ArrayList<>();
    private ViewPager mViewPager;
    private MyAdapter mMyAdapter;
    private LinearLayout mLayoutFilm;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mInflate == null) {

            mInflate = inflater.inflate(R.layout.fragment_main, container, false);
            ButterKnife.bind(this, mInflate);

            mInflate.findViewById(R.id.listView);
            initData();
            initView();
        }

        unbinder = ButterKnife.bind(this, mInflate);
        return mInflate;
    }

    /***
     * 初始化gridview的数据
     */
    private void initData() {
        //获取资源文件的数据
        String[] iconName = getResources().getStringArray(R.array.home_bar_labels);
        TypedArray typedArray = getResources().obtainTypedArray(R.array.home_bar_icon);

        //初始化商品分类的数据
        for (int i = 0; i < iconName.length; i++) {
            if (i < 8) {
                mPagerOneData.add(new HomeIconInfo(iconName[i], typedArray.getResourceId(i, 0)));
            } else {
                mPagerTwoData.add(new HomeIconInfo(iconName[i], typedArray.getResourceId(i, 0)));
            }
        }

        //初始化广告条的数据
        for (int i = 0; i<4 ;i++){
            View viewAdver = View.inflate(getActivity(), R.layout.pager_image_item, null);
            ImageView ivItem = viewAdver.findViewById(R.id.iv_item);
            ivItem.setImageResource(resID[i]);
            mViewsAdver.add(viewAdver);
        }

        /** 猜你喜欢的请求 **/
        Request<String> recommentRequest = NoHttp.createStringRequest(spRecommendURL_NEW, RequestMethod.GET);
        //创建队列
        CallServer.getRequestInstance().add(getActivity(),0,recommentRequest,this,true,true);

        /** 热门电影的请求 **/
        Request<String> filmRequest = NoHttp.createStringRequest(filmHotUrl, RequestMethod.GET);
        CallServer.getRequestInstance().add(getActivity(),1,filmRequest,this,true,true);

    }

    /***
     * 初始化gridview
     */
    private void initView() {
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.home_headviewall, null);

        //广告条viewpager和小标点
        mPagerAdaver = headView.findViewById(R.id.pager_adver);
        mIndicatorAdver = headView.findViewById(R.id.indicator_adver);
        mPagerAdaver.setOnPageChangeListener(new MyPagerListner(mIndicatorAdver));
        mPagerAdaver.setAdapter(new MyPagerAdapter(mViewsAdver));

        //商品分类viewPager和下标点
        mViewPager = (ViewPager) headView.findViewById(R.id.viewPager);
        mIndicator = (ViewPagerIndicator) headView.findViewById(R.id.indicator);
        mViewPager.setOnPageChangeListener(new MyPagerListner(mIndicator));

        //电影列表
        mLayoutFilm = headView.findViewById(R.id.layout_film);

        //第一页数据
        View pagerOne = LayoutInflater.from(getActivity()).inflate(R.layout.home_gridview, null);
        GridView gridView01 = (GridView) pagerOne.findViewById(R.id.gridView);
        gridView01.setAdapter(new MyGridAdapter(mPagerOneData, getActivity()));
        gridView01.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        //第二页数据
        View pagerTwo = LayoutInflater.from(getActivity()).inflate(R.layout.home_gridview, null);
        GridView gridView02 = (GridView) pagerTwo.findViewById(R.id.gridView);
        gridView02.setAdapter(new MyGridAdapter(mPagerTwoData, getActivity()));
        gridView02.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        //添加到ViewPager中
        mViews.add(pagerOne);
        mViews.add(pagerTwo);
        mViewPager.setAdapter(new MyPagerAdapter(mViews));

        //添加listview的头部
        mListView.addHeaderView(headView);
        //添加适配器
        mMyAdapter = new MyAdapter();
        mListView.setAdapter(mMyAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String goods_id = mDatalist.get(i - 1).getGoods_id();
                int bought = mDatalist.get(i - 1).getBought();

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("goods_id",goods_id);
                intent.putExtra("bought",bought);
                startActivity(intent);

            }
        });

    }


    //请求成功
    @Override
    public void OnSucceed(int what, Response<String> response) {

        Gson gson = new Gson();
        switch (what){
            case 0:
                GoodsInfo goodsInfo = gson.fromJson(response.get(), GoodsInfo.class);
                //解析商品列表
                List<GoodsInfo.ResultBean.GoodlistBean> goodlist = goodsInfo.getResult().getGoodlist();
                mDatalist.addAll(goodlist);
                break;

            case 1:
                FilmInfo filmInfo = gson.fromJson(response.get(), FilmInfo.class);
                List<FilmInfo.ResultBean> result = filmInfo.getResult();
                for (int i =0;i<result.size();i++ ){
                    FilmInfo.ResultBean resultBean = result.get(i);
                    View filmView = View.inflate(getActivity(), R.layout.film_item,null);
                    SimpleDraweeView ivFilmIcon = filmView.findViewById(R.id.iv_filmIcon);
                    Uri uri = Uri.parse(resultBean.getImageUrl());
                    ivFilmIcon.setImageURI(uri);
                    TextView tvFilmName = (TextView) filmView.findViewById(R.id.tv_filmName);
                    TextView tvFilmCount = (TextView) filmView.findViewById(R.id.tv_film_count);
                    tvFilmName.setText(resultBean.getFilmName());
                    tvFilmCount.setText(resultBean.getGrade() + "分");
                    mLayoutFilm.addView(filmView);
                }
                break;

        }

    }

    @Override
    public void onFailed(int what, String url, Object tag, CharSequence message, int responseCode, long networkMillis) {
        Toast.makeText(getActivity(),"请求失败",Toast.LENGTH_LONG).show();
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mDatalist.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
/*
            ViewHolder holder = null;

            //第二步，判断条件的封装
            if (convertView == null){
               holder = new ViewHolder();
               //第一步
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.goods_list_item, null);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            //商品子视图
            holder.tvTitle = convertView.findViewById(R.id.title);
            holder.tvTitle.setText(mDatalist.get(position).getTitle());
*/
            ViewHolder holder  = ViewHolder.get(getActivity(),R.layout.goods_list_item,convertView,viewGroup);


            TextView tv = holder.getView(R.id.title);
            tv.setText(mDatalist.get(position).getProduct());
            //解析图片
            Uri uri = Uri.parse(mDatalist.get(position).getImages().get(0).getImage());
            SimpleDraweeView draweeView = holder.getView(R.id.iv_icon2);
            draweeView.setImageURI(uri);
            return holder.getConvertView();
        }
    }

  /*  static class ViewHolder{
        TextView tvTitle;
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
