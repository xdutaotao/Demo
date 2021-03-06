package com.xunao.diaodiao.Fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Activity.AppealActivity;
import com.xunao.diaodiao.Activity.ApplyActivity;
import com.xunao.diaodiao.Activity.OrderProjProgressActivity;
import com.xunao.diaodiao.Activity.OrderSkillCompDetailActivity;
import com.xunao.diaodiao.Activity.RecommandActivity;
import com.xunao.diaodiao.Activity.WebViewActivity;
import com.xunao.diaodiao.Activity.WebViewDetailActivity;
import com.xunao.diaodiao.Bean.OrderCompRes;
import com.xunao.diaodiao.Bean.OrderSkillRes;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.OrderComPresenter;
import com.xunao.diaodiao.Present.OrderSkillPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.OrderCompView;
import com.xunao.diaodiao.View.OrderSkillView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_WEIBAO_WAIT;
import static com.xunao.diaodiao.Common.Constants.SKILL_RELEASE_WEIBAO;
import static com.xunao.diaodiao.Common.Constants.SKILL_RELEASE_WEIBAO_WAIT;


/**
 * Description: 技术人员 我发布 零工 待确认
 * Created by guzhenfu on 2017/8/19.
 */

public class OrderSkillTabFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener, OrderSkillView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;
    Unbinder unbinder;
    private String mParam1;

    @Inject
    OrderSkillPresenter presenter;

    private RecyclerArrayAdapter<OrderSkillRes.OddBean> adapter;
    private int page = 1;

    private int who;

    public static OrderSkillTabFragment newInstance(String param1, int who) {
        OrderSkillTabFragment fragment = new OrderSkillTabFragment();
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
        adapter = new RecyclerArrayAdapter<OrderSkillRes.OddBean>(getContext(), R.layout.order_comp_tab_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, OrderSkillRes.OddBean homeBean) {
                baseViewHolder.setText(R.id.item_content, homeBean.getTitle());
                baseViewHolder.setVisible(R.id.evaluation, false);
                baseViewHolder.setText(R.id.address, homeBean.getAddress());
                baseViewHolder.setText(R.id.time, homeBean.getIssue_time());
                //baseViewHolder.setText(R.id.time, Utils.strToDateLong(homeBean.getPublish_time()));
                baseViewHolder.setText(R.id.name, homeBean.getProject_type());
                baseViewHolder.setText(R.id.request, "申请人员");
                baseViewHolder.setText(R.id.distance, homeBean.getApply_count()+" 人申请");
                if(who == SKILL_RELEASE_WEIBAO){
                    baseViewHolder.setText(R.id.price, " ￥ "+homeBean.getDoor_fee());
                }else{
                    baseViewHolder.setText(R.id.price, " ￥ "+homeBean.getDaily_wage()+"/天");
                }


                baseViewHolder.setOnClickListener(R.id.request, v -> {
                    if(who == SKILL_RELEASE_WEIBAO){
                        ApplyActivity.startActivity(OrderSkillTabFragment.this.getContext(),
                                homeBean.getMaintenance_id(), 4);
                    }else{
                        ApplyActivity.startActivity(OrderSkillTabFragment.this.getContext(),
                                homeBean.getOdd_id(), 3);
                    }

                });
            }
        };

        adapter.setOnItemClickListener((v, i) -> {
            if(who == SKILL_RELEASE_WEIBAO){
//                WebViewActivity.startActivity(OrderSkillTabFragment.this.getContext(),
//                        adapter.getAllData().get(i).getUrl(),
//                        adapter.getAllData().get(i).getMaintenance_id(), WebViewActivity.WEIBAO_DETAIL);

                WebViewDetailActivity.startActivity(OrderSkillTabFragment.this.getContext(),
                        adapter.getAllData().get(i), SKILL_RELEASE_WEIBAO_WAIT);

            }else{
                WebViewActivity.startActivity(OrderSkillTabFragment.this.getContext(),
                        adapter.getAllData().get(i).getUrl(),
                        adapter.getAllData().get(i).getOdd_id(),
                        WebViewActivity.LG_DETAIL);
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
    public void getData(OrderSkillRes list) {
        if (page == 1)
            adapter.clear();

        if(who == Constants.SKILL_RELEASE_LINGGONG){
            if(list.getOdd().size() != 10)
                adapter.stopMore();

            adapter.addAll(list.getOdd());
        }else if(who == SKILL_RELEASE_WEIBAO){
            if(list.getMaintenance().size() != 10)
                adapter.stopMore();

            adapter.addAll(list.getMaintenance());
        }



    }


    @Override
    public void onRefresh() {
        page = 1;
        presenter.mySkillWait(page, who);
    }

    @Override
    public void onLoadMore() {
        page++;
        presenter.mySkillWait(page, who);
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
