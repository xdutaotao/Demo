package com.xunao.diaodiao.Fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Activity.ApplyActivity;
import com.xunao.diaodiao.Activity.FindProjectActivity;
import com.xunao.diaodiao.Activity.OrderCompProjDetailActivity;
import com.xunao.diaodiao.Activity.OrderProjProgressActivity;
import com.xunao.diaodiao.Activity.ProjectDetailActivity;
import com.xunao.diaodiao.Activity.RecommandActivity;
import com.xunao.diaodiao.Bean.OrderCompRes;
import com.xunao.diaodiao.Present.OrderComPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.OrderCompView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Description:
 * Created by guzhenfu on 2017/8/19.
 */

public class OrderCompTabFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener, OrderCompView {
    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;
    Unbinder unbinder;
    private String mParam1;

    @Inject
    OrderComPresenter presenter;

    private RecyclerArrayAdapter<OrderCompRes.Project> adapter;
    private int page = 1;

    public static OrderCompTabFragment newInstance(String param1) {
        OrderCompTabFragment fragment = new OrderCompTabFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.single_fragment_recycler_view, container, false);
        getActivityComponent().inject(this);
        unbinder = ButterKnife.bind(this, view);
        presenter.attachView(this);
        adapter = new RecyclerArrayAdapter<OrderCompRes.Project>(getContext(), R.layout.order_comp_tab_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, OrderCompRes.Project homeBean) {
                baseViewHolder.setText(R.id.item_content, homeBean.getTitle());
                baseViewHolder.setVisible(R.id.evaluation, false);
                baseViewHolder.setText(R.id.address, homeBean.getAddress());
                baseViewHolder.setText(R.id.time, Utils.strToDateLong(homeBean.getPublish_time()));
                baseViewHolder.setText(R.id.name, homeBean.getProject_type());
                baseViewHolder.setText(R.id.distance, homeBean.getApply_count()+" 人申请");
                baseViewHolder.setText(R.id.price, " ￥ "+homeBean.getProject_fee());

                baseViewHolder.setOnClickListener(R.id.request, v -> {
                    //OrderProjProgressActivity.startActivity(OrderCompTabFragment.this.getContext());
                });

                baseViewHolder.setOnClickListener(R.id.evaluation, v -> {
                    RecommandActivity.startActivity(OrderCompTabFragment.this.getContext());
                });

                baseViewHolder.setOnClickListener(R.id.distance, v -> {
                    //ApplyActivity.startActivity(OrderCompTabFragment.this.getContext());
                });
            }
        };

        adapter.setOnItemClickListener((v, i) -> {
            ApplyActivity.startActivity(OrderCompTabFragment.this.getContext(),
                    adapter.getAllData().get(i).getProject_id());
        });

        recyclerView.setAdapterDefaultConfig(adapter, this, this);
        onRefresh();
        return view;
    }
    @Override
    public void getData(OrderCompRes list) {
        if (page == 1)
            adapter.clear();

        if (list.getProject().size() == 0){
            adapter.stopMore();
        }else{
            adapter.addAll(list.getProject());
        }

    }


    @Override
    public void onRefresh() {
        presenter.myProjectWait(page);
    }

    @Override
    public void onLoadMore() {
        page++;
        presenter.myProjectWait(page);
    }

    public String getTitle(){
        return mParam1;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onFailure() {
        adapter.stopMore();
    }


}
