package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.GetOddInfoRes;
import com.xunao.diaodiao.Bean.JoinDetailRes;
import com.xunao.diaodiao.Present.JoinDetailPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.JoinDetailView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class JoinDetailActivity extends BaseActivity implements JoinDetailView, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {

    @Inject
    JoinDetailPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.rating_star)
    RatingBar ratingStar;
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.score)
    TextView score;

    private RecyclerArrayAdapter<JoinDetailRes.EvaluateInfoBean> adapter;
    private RecyclerArrayAdapter<GetOddInfoRes.EvaluateInfoBean> oddAdapter;
    //首页type
    private int type;
    private int page = 1;

    public static void startActivity(Context context, int id, int type) {
        Intent intent = new Intent(context, JoinDetailActivity.class);
        intent.putExtra(INTENT_KEY, id);
        intent.putExtra("TYPE", type);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_detail);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "公司介绍");

        type = getIntent().getIntExtra("TYPE", 0);

        if (type == 0){
            //项目
            adapter = new RecyclerArrayAdapter<JoinDetailRes.EvaluateInfoBean>(this, R.layout.join_detail_item) {
                @Override
                protected void convert(BaseViewHolder baseViewHolder, JoinDetailRes.EvaluateInfoBean homeBean) {
                    baseViewHolder.setText(R.id.content, homeBean.getContent());
                    baseViewHolder.setText(R.id.head_info, homeBean.getName());
                    baseViewHolder.setImageUrl(R.id.head_icon, homeBean.getHead_img());
                }
            };

            recyclerView.setAdapterDefaultConfig(adapter, this, this);
        }else if(type == 2){
            //零工
            oddAdapter = new RecyclerArrayAdapter<GetOddInfoRes.EvaluateInfoBean>(this, R.layout.join_detail_item) {
                @Override
                protected void convert(BaseViewHolder baseViewHolder, GetOddInfoRes.EvaluateInfoBean homeBean) {
                    baseViewHolder.setText(R.id.content, homeBean.getContent());
                    baseViewHolder.setText(R.id.head_info, homeBean.getName());
                    baseViewHolder.setImageUrl(R.id.head_icon, homeBean.getHead_img());
                }
            };

            recyclerView.setAdapterDefaultConfig(oddAdapter, this, this);
        }



        onRefresh();


    }

    @Override
    public void onRefresh() {
        page = 1;
        if (type == 0) {
            presenter.getCompanyInfo(getIntent().getIntExtra(INTENT_KEY, 0), page);
        } else if (type == 1) {
            //零工
            presenter.getOddInfo(getIntent().getIntExtra(INTENT_KEY, 0), page);
        }
    }

    @Override
    public void onLoadMore() {
        page++;
        if (type == 0) {
            presenter.getCompanyInfo(getIntent().getIntExtra(INTENT_KEY, 0), page);
        } else if (type == 1) {
            presenter.getOddInfo(getIntent().getIntExtra(INTENT_KEY, 0), page);
        }
    }

    @Override
    public void getData(JoinDetailRes s) {
        if (page == 1) {
            adapter.clear();
            name.setText(s.getCompany_info().getName());
            address.setText(s.getCompany_info().getAddress());
            score.setText(s.getCompany_info().getPoint());
            ratingStar.setRating(Float.valueOf(s.getCompany_info().getPoint()));
        }

        if (s.getEvaluate_Info().size() == 0) {
            adapter.stopMore();
        } else {
            adapter.addAll(s.getEvaluate_Info());
        }
    }

    @Override
    public void getOddInfo(GetOddInfoRes s) {
        if (page == 1) {
            oddAdapter.clear();
            name.setText(s.getOdd_info().getName());
            address.setText(s.getOdd_info().getAddress());
            score.setText(s.getOdd_info().getPoint());
            ratingStar.setRating(Float.valueOf(s.getOdd_info().getPoint()));
        }

        if (s.getEvaluate_Info() == null){
            recyclerView.showEmpty();
            return;
        }

        if (s.getEvaluate_Info().size() == 0) {
            oddAdapter.stopMore();
        } else {
            oddAdapter.addAll(s.getEvaluate_Info());
        }
    }


    @Override
    public void onFailure() {
        if (type == 0){
            adapter.stopMore();
        }else{
            oddAdapter.stopMore();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }


}
