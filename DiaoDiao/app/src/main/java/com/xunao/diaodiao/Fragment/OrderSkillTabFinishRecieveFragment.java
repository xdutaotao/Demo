package com.xunao.diaodiao.Fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Activity.RecommandActivity;
import com.xunao.diaodiao.Activity.WebViewActivity;
import com.xunao.diaodiao.Activity.WebViewDetailActivity;
import com.xunao.diaodiao.Bean.OrderSkillFinishRecieveRes;
import com.xunao.diaodiao.Bean.OrderSkillFinishRes;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.OrderSkillFinishPresenter;
import com.xunao.diaodiao.Present.OrderSkillFinishRecievePresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.OrderSkillFinishRecieveView;
import com.xunao.diaodiao.View.OrderSkillFinishView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.xunao.diaodiao.Common.Constants.SKILL_RECIEVE_JIANLI;
import static com.xunao.diaodiao.Common.Constants.SKILL_RECIEVE_LINGGONG;
import static com.xunao.diaodiao.Common.Constants.SKILL_RECIEVE_PROJECT;
import static com.xunao.diaodiao.Common.Constants.SKILL_RECIEVE_WEIBAO;


/**
 * Description:
 * Created by guzhenfu on 2017/8/19.
 */

public class OrderSkillTabFinishRecieveFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener, OrderSkillFinishRecieveView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;
    Unbinder unbinder;
    private String mParam1;
    private int who;

    @Inject
    OrderSkillFinishRecievePresenter presenter;

    private RecyclerArrayAdapter<OrderSkillFinishRecieveRes.OddBean> adapter;
    private int page = 1;

    public static OrderSkillTabFinishRecieveFragment newInstance(String param1, int param2) {
        OrderSkillTabFinishRecieveFragment fragment = new OrderSkillTabFinishRecieveFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
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
        adapter = new RecyclerArrayAdapter<OrderSkillFinishRecieveRes.OddBean>(getContext(), R.layout.order_comp_tab_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, OrderSkillFinishRecieveRes.OddBean homeBean) {
                baseViewHolder.setText(R.id.item_content, homeBean.getTitle());
                baseViewHolder.setVisible(R.id.evaluation, false);
                baseViewHolder.setText(R.id.address, homeBean.getAddress());

                baseViewHolder.setText(R.id.name, homeBean.getProject_type());
                baseViewHolder.setVisible(R.id.distance, false);

                if (who == Constants.SKILL_RECIEVE_LINGGONG){
                    baseViewHolder.setText(R.id.price, " ￥ "+homeBean.getDaily_wage()+" / 天");
                    baseViewHolder.setText(R.id.days, "共"+homeBean.getTotal_day()+"天");
                }else if(who == Constants.SKILL_RECIEVE_PROJECT){
                    baseViewHolder.setText(R.id.price, " ￥ "+homeBean.getProject_price());
                    baseViewHolder.setText(R.id.days, "价格");
                }else if(who == Constants.SKILL_RECIEVE_WEIBAO){
                    baseViewHolder.setText(R.id.price, " ￥ "+homeBean.getProject_price());
                    baseViewHolder.setText(R.id.days, "上门费");
                }else if(who == Constants.SKILL_RECIEVE_JIANLI){
                    baseViewHolder.setText(R.id.price, " ￥ "+homeBean.getProject_price());
                    baseViewHolder.setText(R.id.days, "价格");
                }


                if (homeBean.getStatus() == 1){
                    //已完成
                    baseViewHolder.setText(R.id.request, "查看");
                    baseViewHolder.setText(R.id.time, homeBean.getEvaluate_status() == 1 ? "查看评价" : "去评价");
                    baseViewHolder.setTextColorRes(R.id.time, R.color.accept_btn_default);

                }else{
                    if(who == Constants.SKILL_RECIEVE_WEIBAO){
                        baseViewHolder.setText(R.id.request, "维保情况");
                        baseViewHolder.setText(R.id.time, homeBean.getIssue_time());
                    }else if(who == Constants.SKILL_RECIEVE_JIANLI) {
                        baseViewHolder.setText(R.id.request, "监理进度");
                        baseViewHolder.setText(R.id.time, homeBean.getIssue_time());
                    }else {
                        baseViewHolder.setText(R.id.request, "项目进度");
                        baseViewHolder.setText(R.id.time, homeBean.getIssue_time());
                    }

                }

                baseViewHolder.setOnClickListener(R.id.time, v -> {
                    //评价 1 项目
                    if(homeBean.getEvaluate_status() != 1){
                        if(who == SKILL_RECIEVE_PROJECT){
                            RecommandActivity.startActivity(OrderSkillTabFinishRecieveFragment.this.getContext(),
                                    homeBean.getProject_id(), 1);
                        }else if(who == SKILL_RECIEVE_LINGGONG){
                            RecommandActivity.startActivity(OrderSkillTabFinishRecieveFragment.this.getContext(),
                                    homeBean.getOdd_id(), 3);
                        }else if(who == SKILL_RECIEVE_WEIBAO){
                            //维保 4
                            RecommandActivity.startActivity(OrderSkillTabFinishRecieveFragment.this.getContext(),
                                    homeBean.getMaintenance_id(), 4);
                        }else if(who == SKILL_RECIEVE_JIANLI){
                            //监理 2
                            RecommandActivity.startActivity(OrderSkillTabFinishRecieveFragment.this.getContext(),
                                    homeBean.getSupervisor_id(), 2);
                        }

                    }

                });


            }
        };

        adapter.setOnItemClickListener((v, i) -> {

            if (who == Constants.SKILL_RECIEVE_LINGGONG){
                WebViewActivity.startActivity(OrderSkillTabFinishRecieveFragment.this.getContext(),
                        adapter.getAllData().get(i).getUrl(),
                        adapter.getAllData().get(i).getOdd_id(), WebViewActivity.RECEIVE_LG_DETAIL);
            }else if(who == Constants.SKILL_RECIEVE_PROJECT){
                WebViewDetailActivity.startActivity(OrderSkillTabFinishRecieveFragment.this.getContext(),
                        adapter.getAllData().get(i), who);
            }else if(who == Constants.SKILL_RECIEVE_WEIBAO){
                WebViewDetailActivity.startActivity(OrderSkillTabFinishRecieveFragment.this.getContext(),
                        adapter.getAllData().get(i), who);
            }else if(who == Constants.SKILL_RECIEVE_JIANLI){
                WebViewDetailActivity.startActivity(OrderSkillTabFinishRecieveFragment.this.getContext(),
                        adapter.getAllData().get(i), who);
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
    public void getData(OrderSkillFinishRecieveRes list) {
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

        }else if( who == Constants.SKILL_RECIEVE_WEIBAO){
            if(list.getMaintenance().size() ==0)
                adapter.stopMore();

            adapter.addAll(list.getMaintenance());
        }


    }


    @Override
    public void onRefresh() {
        page = 1;
        presenter.myAcceptOddFinish(page, who);
    }

    @Override
    public void onLoadMore() {
        page++;
        presenter.myAcceptOddFinish(page, who);
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
