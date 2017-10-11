package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.MyFavoriteItem;
import com.xunao.diaodiao.Bean.MyFavoriteRes;
import com.xunao.diaodiao.Present.MyFavoritePresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.MyFavoriteView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class MyFavoriteActivity extends BaseActivity implements MyFavoriteView, RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    MyFavoritePresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;

    private RecyclerArrayAdapter<MyFavoriteRes.Project> adapter;
    private int page;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MyFavoriteActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "我的收藏");

        adapter = new RecyclerArrayAdapter<MyFavoriteRes.Project>(this, R.layout.my_favorite_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, MyFavoriteRes.Project s) {
                if (TextUtils.equals(s.getClasses(), "项目")){
                    baseViewHolder.setBackgroundRes(R.id.bg, R.drawable.bg_xiangmu);
                    baseViewHolder.setText(R.id.title, s.getTitle());
                    baseViewHolder.setImageResource(R.id.shou_cang, R.drawable.icon_shoucang02_fill);
                    baseViewHolder.setText(R.id.address, s.getAddress());
                    baseViewHolder.setText(R.id.price, "￥ "+s.getProject_fee());
                    baseViewHolder.setText(R.id.type, s.getType());
                }else if (TextUtils.equals(s.getClasses(), "零工")){
                    baseViewHolder.setBackgroundRes(R.id.bg, R.drawable.bg_lingong);
                    baseViewHolder.setText(R.id.title, s.getTitle());
                    baseViewHolder.setImageResource(R.id.shou_cang, R.drawable.icon_shoucang02_fill);
                    baseViewHolder.setText(R.id.address, s.getAddress());
                    //baseViewHolder.setText(R.id.price_text, "共"+s.getTotal_day()+"天");
                    baseViewHolder.setText(R.id.price, "￥ "+s.getProject_fee() + "/天");
                    baseViewHolder.setVisible(R.id.type, false);
                }else{
                    baseViewHolder.setBackgroundRes(R.id.bg, R.drawable.bg_jianli);
                    baseViewHolder.setText(R.id.title, s.getTitle());
                    baseViewHolder.setImageResource(R.id.shou_cang, R.drawable.icon_shoucang02_fill);
                    baseViewHolder.setText(R.id.address, s.getAddress());
                    if (TextUtils.isEmpty(s.getProject_fee())){
                        baseViewHolder.setVisible(R.id.price, false);
                        baseViewHolder.setVisible(R.id.price_text, false);
                    }else{
                        baseViewHolder.setText(R.id.price, "￥ "+s.getProject_fee());
                    }
                    baseViewHolder.setText(R.id.type, s.getType());
                }

                baseViewHolder.setOnClickListener(R.id.shou_cang, v -> {
                    //presenter.cancelCollect(s.getCollect_id());
                    //baseViewHolder.setImageResource(R.id.shou_cang, R.drawable.icon_shoucang);
                });
            }
        };

        recyclerView.setAdapterDefaultConfig(adapter, this, this);

        onRefresh();
    }

    @Override
    public void onLoadMore() {
        page++;
        presenter.getCollectList(page);
    }

    @Override
    public void onRefresh() {
        page = 1;
        presenter.getCollectList(page);
    }

    @Override
    public void getData(MyFavoriteRes data) {
        if (page == 1)
            adapter.clear();

        if (data.getProject() == null){
            adapter.stopMore();
        }else{
            adapter.addAll(data.getProject());
        }
    }

    @Override
    public void cancelCollect(String s) {
        ToastUtil.show("取消收藏成功");
    }


    @Override
    public void onFailure() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }



}
