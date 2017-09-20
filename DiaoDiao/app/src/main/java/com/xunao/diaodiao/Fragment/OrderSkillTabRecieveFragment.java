package com.xunao.diaodiao.Fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Activity.OrderSkillCompDetailActivity;
import com.xunao.diaodiao.Activity.OrderSkillCompRecieveDetailActivity;
import com.xunao.diaodiao.Activity.RecommandActivity;
import com.xunao.diaodiao.Bean.OrderSkillRecieveRes;
import com.xunao.diaodiao.Bean.OrderSkillRes;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.OrderSkillPresenter;
import com.xunao.diaodiao.Present.OrderSkillRecievePresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.OrderSkillRecieveView;
import com.xunao.diaodiao.View.OrderSkillView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Description:
 * Created by guzhenfu on 2017/8/19.
 */

public class OrderSkillTabRecieveFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener, OrderSkillRecieveView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;
    Unbinder unbinder;
    private String mParam1;
    private int who;

    @Inject
    OrderSkillRecievePresenter presenter;

    private RecyclerArrayAdapter<OrderSkillRecieveRes.OddBean> adapter;
    private int page = 1;

    public static OrderSkillTabRecieveFragment newInstance(String param1, int who) {
        OrderSkillTabRecieveFragment fragment = new OrderSkillTabRecieveFragment();
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
        adapter = new RecyclerArrayAdapter<OrderSkillRecieveRes.OddBean>(getContext(), R.layout.order_comp_tab_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, OrderSkillRecieveRes.OddBean homeBean) {
                baseViewHolder.setText(R.id.item_content, homeBean.getTitle());
                baseViewHolder.setVisible(R.id.evaluation, false);
                baseViewHolder.setText(R.id.address, homeBean.getAddress());
                baseViewHolder.setText(R.id.time, Utils.strToDateLong(homeBean.getPublish_time()));
                baseViewHolder.setText(R.id.name, homeBean.getProject_type());
                baseViewHolder.setVisible(R.id.distance, false);
                if (who == Constants.SKILL_RECIEVE_LINGGONG){
                    baseViewHolder.setText(R.id.days, "共"+homeBean.getTotal_day()+"天");
                    baseViewHolder.setText(R.id.price, " ￥ "+homeBean.getDaily_wage() +"/天");
                }else if(who == Constants.SKILL_RECIEVE_PROJECT){
//                    baseViewHolder.setText(R.id.days, "共"+homeBean.getTotal_day()+"天");
                    baseViewHolder.setText(R.id.price, " ￥ "+homeBean.getProject_price() +"/天");
                }

                baseViewHolder.setText(R.id.request, "取消申请");

                baseViewHolder.setOnClickListener(R.id.request, v -> {
                    if (who == Constants.SKILL_RECIEVE_LINGGONG){
                        presenter.myAcceptOddCancel(homeBean.getOdd_id(), who);
                    }else if(who == Constants.SKILL_RECIEVE_PROJECT){
                        presenter.myAcceptOddCancel(homeBean.getProject_id(), who);
                    }


                });

                baseViewHolder.setOnClickListener(R.id.evaluation, v -> {
                    //RecommandActivity.startActivity(OrderSkillTabRecieveFragment.this.getContext());
                });

                baseViewHolder.setOnClickListener(R.id.distance, v -> {
                    //ApplyActivity.startActivity(OrderCompTabFragment.this.getContext());
                });
            }
        };

        adapter.setOnItemClickListener((v, i) -> {

            if (who == Constants.SKILL_RECIEVE_LINGGONG){
                OrderSkillCompRecieveDetailActivity.startActivity(OrderSkillTabRecieveFragment.this.getContext(),
                        adapter.getAllData().get(i).getOdd_id());
            }else if(who == Constants.SKILL_RECIEVE_PROJECT){
                OrderSkillCompRecieveDetailActivity.startActivity(OrderSkillTabRecieveFragment.this.getContext(),
                        adapter.getAllData().get(i).getProject_id());
            }


        });

        recyclerView.setAdapterDefaultConfig(adapter, this, this);
        onRefresh();
        return view;
    }
    @Override
    public void getData(OrderSkillRecieveRes list) {
        if (page == 1)
            adapter.clear();

        if (who == Constants.SKILL_RECIEVE_LINGGONG){
            if(list.getOdd().size() ==0){
                adapter.stopMore();
            }else{
                adapter.addAll(list.getOdd());
            }
        }else if(who == Constants.SKILL_RECIEVE_PROJECT){
            if(list.getProject().size() ==0){
                adapter.stopMore();
            }else{
                adapter.addAll(list.getProject());
            }
        }



    }

    @Override
    public void cancle(String list) {
        ToastUtil.show("取消成功");
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
        adapter.stopMore();
    }


}
