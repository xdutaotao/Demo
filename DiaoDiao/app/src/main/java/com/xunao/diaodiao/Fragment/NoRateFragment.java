package com.xunao.diaodiao.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Activity.FeedBackDetailActivity;
import com.xunao.diaodiao.Activity.RecommandActivity;
import com.xunao.diaodiao.Activity.RecordDetailActivity;
import com.xunao.diaodiao.Bean.MyRateRes;
import com.xunao.diaodiao.Present.MyRatingPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.MyRatingView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoRateFragment extends BaseFragment implements MyRatingView, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {
    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;
    private String mParam1;

    private RecyclerArrayAdapter<MyRateRes.ProjBean> adapter;

    @Inject
    MyRatingPresenter presenter;
    private int page = 1;

    public static NoRateFragment newInstance(String param1) {
        NoRateFragment fragment = new NoRateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        ButterKnife.bind(this, view);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        adapter = new RecyclerArrayAdapter<MyRateRes.ProjBean>(getContext(), R.layout.my_rating_item) {
                @Override
                protected void convert(BaseViewHolder baseViewHolder, MyRateRes.ProjBean s) {
                    baseViewHolder.setText(R.id.rating_name, s.getTitle());
                    baseViewHolder.setText(R.id.address, s.getAddress());
                    baseViewHolder.setText(R.id.type, s.getType());
                    baseViewHolder.setVisible(R.id.project_detail, false);
                    if (s.getProject_type() == 1){
                        //项目
                        baseViewHolder.setText(R.id.price_detail, "价格");
                        baseViewHolder.setText(R.id.price, " ￥"+s.getPrice());
                    }else if(s.getProject_type() == 4) {
                        //维保
                        baseViewHolder.setText(R.id.price_detail, "上门费");
                        baseViewHolder.setText(R.id.price, " ￥"+s.getDaily_wage());
                    }else if(s.getProject_type() == 2) {
                        //监理
                        baseViewHolder.setText(R.id.price_detail, "价格");
                        baseViewHolder.setText(R.id.price, " ￥"+s.getPrice());
                    }else if(s.getProject_type() == 3) {
                        //零工
                        baseViewHolder.setText(R.id.price_detail, "共"+s.getTotal_day()+"天");
                        baseViewHolder.setText(R.id.price, " ￥"+s.getDaily_wage()+"/天");
                    }else{
                        baseViewHolder.setText(R.id.project_detail, "维保进度");
                        baseViewHolder.setText(R.id.price_detail, "上门费");
                        baseViewHolder.setText(R.id.price, " ￥"+s.getDaily_wage());
                    }

                }
            };

        adapter.setOnItemClickListener((view1, i) -> {
                RecommandActivity.startActivity(NoRateFragment.this.getActivity(),
                        adapter.getAllData().get(i).getProject_id(),
                        adapter.getAllData().get(i).getProject_type());
            });


        recyclerView.setAdapterDefaultConfig(adapter, this, this);
        onRefresh();
        return view;
    }

    @Override
    public void onRefresh() {
        page = 1;
        presenter.getRating(page);
    }

    @Override
    public void onLoadMore() {
        page++;
        presenter.getRating(page);
    }

    @Override
    public void getData(MyRateRes res) {
        if (page == 1){
            adapter.clear();
            adapter.addAll(res.getProject());
        }else{
            adapter.addAll(res.getProject());
        }

    }


    public String getTitle() {
        return mParam1;
    }


    @Override
    public void onFailure() {
        adapter.stopMore();
    }



}
