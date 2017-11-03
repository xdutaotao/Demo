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
import com.xunao.diaodiao.Activity.OrderProjRecieveProgressActivity;
import com.xunao.diaodiao.Activity.OrderSkillCompRecieveDetailActivity;
import com.xunao.diaodiao.Activity.SkillProjReceiveProgressActivity;
import com.xunao.diaodiao.Activity.WebViewActivity;
import com.xunao.diaodiao.Activity.WeiBaoProjActivity;
import com.xunao.diaodiao.Bean.OrderSkillDoingRes;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.OrderSkillDoingPresenter;
import com.xunao.diaodiao.Present.OrderSkillDoingRecievePresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.OrderSkillDoingView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Description:
 * Created by guzhenfu on 2017/8/19.
 */

public class OrderSkillTabDoingRecieveFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener, OrderSkillDoingView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;
    Unbinder unbinder;
    private String mParam1;
    private int who;

    @Inject
    OrderSkillDoingRecievePresenter presenter;

    private RecyclerArrayAdapter<OrderSkillDoingRes.OddBean> adapter;
    private int page = 1;

    public static OrderSkillTabDoingRecieveFragment newInstance(String param1,int mParam2) {
        OrderSkillTabDoingRecieveFragment fragment = new OrderSkillTabDoingRecieveFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, mParam2);
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
                baseViewHolder.setText(R.id.name, homeBean.getProject_type());
                baseViewHolder.setVisible(R.id.distance, false);

                if (who == Constants.SKILL_RECIEVE_LINGGONG){
                    baseViewHolder.setText(R.id.price, " ￥ "+homeBean.getDaily_wage()+" / 天");
                    if (!TextUtils.isEmpty(homeBean.getTotal_day()))
                        baseViewHolder.setText(R.id.days, "共"+homeBean.getTotal_day()+"天");
                }else if(who == Constants.SKILL_RECIEVE_PROJECT){
                    baseViewHolder.setText(R.id.price, " ￥ "+homeBean.getProject_price());
                    baseViewHolder.setText(R.id.days, "价格");
                }else if(who == Constants.SKILL_RECIEVE_WEIBAO){
                    baseViewHolder.setText(R.id.price, " ￥ "+homeBean.getProject_price());
                    baseViewHolder.setText(R.id.days, "上门费");
                }

                if(who == Constants.SKILL_RECIEVE_WEIBAO){
                    baseViewHolder.setText(R.id.request, "维保情况");
                }else{
                    baseViewHolder.setText(R.id.request, "项目进度");
                }


                baseViewHolder.setOnClickListener(R.id.request, v -> {
                    if (who == Constants.SKILL_RECIEVE_LINGGONG){
                        //技术人员 我接的 零工
                        OrderProjRecieveProgressActivity.startActivity(OrderSkillTabDoingRecieveFragment.this.getContext(),
                                homeBean.getOdd_id());
                    }else if(who == Constants.SKILL_RECIEVE_PROJECT){
                        //技术人员 我接的  项目
                        SkillProjReceiveProgressActivity.startActivity(OrderSkillTabDoingRecieveFragment.this.getContext(),
                                homeBean.getProject_id(), who);
                    }else if(who == Constants.SKILL_RECIEVE_WEIBAO){
                        WeiBaoProjActivity.startActivity(OrderSkillTabDoingRecieveFragment.this.getContext(),
                                homeBean.getMaintenance_id());
                    }


                });
            }

        };

        adapter.setOnItemClickListener((v, i) -> {
            if (who == Constants.SKILL_RECIEVE_LINGGONG){
                WebViewActivity.startActivity(OrderSkillTabDoingRecieveFragment.this.getContext(),
                        adapter.getAllData().get(i).getUrl(),
                        adapter.getAllData().get(i).getOdd_id(), WebViewActivity.RECEIVE_LG_DETAIL);
            }else if (who == Constants.SKILL_RECIEVE_PROJECT){
                WebViewActivity.startActivity(OrderSkillTabDoingRecieveFragment.this.getContext(),
                        adapter.getAllData().get(i).getUrl(),
                        adapter.getAllData().get(i).getProject_id(), WebViewActivity.SKILL_RECIEVE_PROJECT_DOING);
            }else if(who == Constants.SKILL_RECIEVE_WEIBAO){
                WebViewActivity.startActivity(OrderSkillTabDoingRecieveFragment.this.getContext(),
                        adapter.getAllData().get(i).getUrl(),
                        adapter.getAllData().get(i).getMaintenance_id(), WebViewActivity.SKILL_RECIEVE_PROJECT_DOING);
            }


//
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

        if (who == Constants.SKILL_RECIEVE_LINGGONG){
            if(list.getOdd().size() != 10)
                adapter.stopMore();

            adapter.addAll(list.getOdd());

        }else if (who == Constants.SKILL_RECIEVE_PROJECT){
            if(list.getProject().size() ==0)
                adapter.stopMore();

            adapter.addAll(list.getProject());

        }else if(who == Constants.SKILL_RECIEVE_WEIBAO){
            if(list.getMaintenance().size() ==0)
                adapter.stopMore();

            adapter.addAll(list.getMaintenance());
        }


    }


    @Override
    public void onRefresh() {
        page=1;
        presenter.myAcceptOddDoing(page, who);
    }

    @Override
    public void onLoadMore() {
        page++;
        presenter.myAcceptOddDoing(page, who);
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
