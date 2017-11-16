package com.xunao.diaodiao.Fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Activity.OrderProjProgressActivity;
import com.xunao.diaodiao.Activity.OrderSkillCompDetailActivity;
import com.xunao.diaodiao.Activity.RecommandActivity;
import com.xunao.diaodiao.Activity.WebViewActivity;
import com.xunao.diaodiao.Activity.WeiBaoProgActivity;
import com.xunao.diaodiao.Bean.OrderSkillDoingRes;
import com.xunao.diaodiao.Bean.OrderSkillRes;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.OrderSkillDoingPresenter;
import com.xunao.diaodiao.Present.OrderSkillPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.OrderSkillDoingView;
import com.xunao.diaodiao.View.OrderSkillView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Description: 我发布的零工 进行中
 * Created by guzhenfu on 2017/8/19.
 */

public class OrderSkillTabDoingFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener, OrderSkillDoingView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;
    Unbinder unbinder;
    private String mParam1;

    @Inject
    OrderSkillDoingPresenter presenter;

    private RecyclerArrayAdapter<OrderSkillDoingRes.OddBean> adapter;
    private int page = 1;

    private int who;

    public static OrderSkillTabDoingFragment newInstance(String param1, int who) {
        OrderSkillTabDoingFragment fragment = new OrderSkillTabDoingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, who);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            who = getArguments().getInt(ARG_PARAM2);
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
        adapter = new RecyclerArrayAdapter<OrderSkillDoingRes.OddBean>(getContext(), R.layout.order_comp_tab_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, OrderSkillDoingRes.OddBean homeBean) {
                baseViewHolder.setText(R.id.item_content, homeBean.getTitle());
                baseViewHolder.setVisible(R.id.evaluation, false);
                baseViewHolder.setText(R.id.address, homeBean.getAddress());
                baseViewHolder.setText(R.id.time, homeBean.getIssue_time());
                //baseViewHolder.setText(R.id.time, Utils.strToDateLong(homeBean.getPublish_time()));
                baseViewHolder.setText(R.id.name, homeBean.getProject_type());
                baseViewHolder.setVisible(R.id.distance, false);
                if(who == Constants.SKILL_RELEASE_WEIBAO){
                    baseViewHolder.setText(R.id.price, " ￥ "+homeBean.getDoor_fee()+" / 天");
                }else{
                    baseViewHolder.setText(R.id.price, " ￥ "+homeBean.getDaily_wage()+" / 天");
                }

                if (!TextUtils.isEmpty(homeBean.getTotal_day()))
                    baseViewHolder.setText(R.id.days, "共"+homeBean.getTotal_day()+"天");

                baseViewHolder.setText(R.id.request, "项目进度");

                baseViewHolder.setOnClickListener(R.id.request, v -> {
                    if(who == Constants.SKILL_RELEASE_WEIBAO){
                        WeiBaoProgActivity.startActivity(OrderSkillTabDoingFragment.this.getContext(),
                                homeBean.getMaintenance_id(), who);
                    }else{
                        OrderProjProgressActivity.startActivity(OrderSkillTabDoingFragment.this.getContext(),
                                homeBean.getOdd_id());
                    }

                });
            }
        };

        adapter.setOnItemClickListener((v, i) -> {
            if(who == Constants.SKILL_RELEASE_WEIBAO){
                WebViewActivity.startActivity(OrderSkillTabDoingFragment.this.getContext(),
                        adapter.getAllData().get(i).getUrl(),
                        adapter.getAllData().get(i).getMaintenance_id());
            }else{
                WebViewActivity.startActivity(OrderSkillTabDoingFragment.this.getContext(),
                        adapter.getAllData().get(i).getUrl(),
                        adapter.getAllData().get(i).getOdd_id());
            }

        });

        recyclerView.setAdapterDefaultConfig(adapter, this, this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public void getData(OrderSkillDoingRes list) {
        if (page == 1)
            adapter.clear();
        if(who == Constants.SKILL_RELEASE_WEIBAO){
            if(list.getMaintenance().size() != 10)
                adapter.stopMore();

            adapter.addAll(list.getMaintenance());
        }else{
            if(list.getOdd().size() != 10)
                adapter.stopMore();

            adapter.addAll(list.getOdd());
        }



    }


    @Override
    public void onRefresh() {
        page=1;
        presenter.mySkillDoing(page, who);
    }

    @Override
    public void onLoadMore() {
        page++;
        presenter.mySkillDoing(page, who);
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
        if(adapter.getAllData().size() == 0){
            recyclerView.showEmpty();
        }else{
            adapter.stopMore();
        }

    }


}
