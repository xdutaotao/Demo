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
import com.xunao.diaodiao.Activity.ApplyDetailActivity;
import com.xunao.diaodiao.Activity.FindProjectActivity;
import com.xunao.diaodiao.Activity.OrderCompProjDetailActivity;
import com.xunao.diaodiao.Activity.OrderProjProgressActivity;
import com.xunao.diaodiao.Activity.ProjectDetailActivity;
import com.xunao.diaodiao.Activity.RecommandActivity;
import com.xunao.diaodiao.Activity.SkillProjReceiveProgressActivity;
import com.xunao.diaodiao.Activity.WebViewActivity;
import com.xunao.diaodiao.Activity.WebViewDetailActivity;
import com.xunao.diaodiao.Bean.ApplyPassReq;
import com.xunao.diaodiao.Bean.OrderCompRes;
import com.xunao.diaodiao.Common.Constants;
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
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;
    Unbinder unbinder;
    private String mParam1;
    private int who;


    @Inject
    OrderComPresenter presenter;

    private RecyclerArrayAdapter<OrderCompRes.Project> adapter;
    private int page = 1;

    public static OrderCompTabFragment newInstance(String param1, int mParam2) {
        OrderCompTabFragment fragment = new OrderCompTabFragment();
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
        adapter = new RecyclerArrayAdapter<OrderCompRes.Project>(getContext(), R.layout.order_comp_tab_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, OrderCompRes.Project homeBean) {
                baseViewHolder.setText(R.id.item_content, homeBean.getTitle());
                baseViewHolder.setText(R.id.time, homeBean.getIssue_time());
                if (who == Constants.COMPANY_RELEASE_PROJECT_DONE){

                    if(homeBean.getStatus() == 4){
                        //已取消

                        baseViewHolder.setVisible(R.id.time, true);
                        baseViewHolder.setText(R.id.request, "查看");
                        baseViewHolder.setVisible(R.id.evaluation, false);
                        baseViewHolder.setVisible(R.id.contact, false);

                    }else{
                        baseViewHolder.setText(R.id.request, "工作进度");
                        baseViewHolder.setVisible(R.id.contact, false);

                        if(homeBean.getEvaluate_status() == 1){
                            //已评价
                            baseViewHolder.setVisible(R.id.time, true);
                            baseViewHolder.setText(R.id.time, "已评价");
                            baseViewHolder.setVisible(R.id.evaluation, false);
                        }else{
                            //未评价
                            baseViewHolder.setVisible(R.id.time, false);
                            baseViewHolder.setVisible(R.id.evaluation, true);
                            baseViewHolder.setText(R.id.evaluation, "去评价");
                            baseViewHolder.setTextColorRes(R.id.evaluation, R.color.accept_btn_default);
                        }

                    }

                }else{
                    baseViewHolder.setVisible(R.id.evaluation, false);
                    baseViewHolder.setVisible(R.id.time, true);

                }

                baseViewHolder.setText(R.id.address, homeBean.getAddress());
                baseViewHolder.setText(R.id.name, homeBean.getProject_type());

                if (who == Constants.COMPANY_RELEASE_PROJECT_WAIT){
                    baseViewHolder.setText(R.id.distance, homeBean.getApply_count()+" 人申请");
                    baseViewHolder.setText(R.id.request, "申请人员");
                }else if (who == Constants.COMPANY_RELEASE_PROJECT_DOING){
                    baseViewHolder.setVisible(R.id.contact, true);
                    baseViewHolder.setVisible(R.id.distance, false);
                    baseViewHolder.setText(R.id.request, "工作进度");
                }else if (who == Constants.COMPANY_RELEASE_PROJECT_DONE){
                    baseViewHolder.setVisible(R.id.distance, false);
                }

                baseViewHolder.setText(R.id.price, " ￥ "+homeBean.getProject_fee());
                baseViewHolder.setOnClickListener(R.id.request, v -> {

                    if (who == Constants.COMPANY_RELEASE_PROJECT_WAIT){
                        //查看申请人  1项目2监理3零工4维保
                        ApplyActivity.startActivity(OrderCompTabFragment.this.getContext(),
                                homeBean.getProject_id(), 1);
                    }else if (who == Constants.COMPANY_RELEASE_PROJECT_DOING){
                        //项目进度
                        SkillProjReceiveProgressActivity.startActivity(OrderCompTabFragment.this.getContext(),
                                homeBean.getProject_id(), who);
                    }else if (who == Constants.COMPANY_RELEASE_PROJECT_DONE){
                        //项目进度
                        if(homeBean.getStatus() == 4){
                            //已取消
                            WebViewDetailActivity.startActivity(OrderCompTabFragment.this.getContext(),
                                    homeBean,who);
                        }else{
                            SkillProjReceiveProgressActivity.startActivity(OrderCompTabFragment.this.getContext(),
                                    homeBean.getProject_id(), who);
                        }

                    }


                });

                baseViewHolder.setOnClickListener(R.id.contact, v -> {
                    ApplyPassReq applyPassReq = new ApplyPassReq();
                    applyPassReq.setTechnician_id(homeBean.getApply_id());
                    applyPassReq.setProject_id(1000);
                    ApplyDetailActivity.startActivity(OrderCompTabFragment.this.getContext(),
                            applyPassReq);
                });

                baseViewHolder.setOnClickListener(R.id.evaluation, v -> {
                    //评价 1 项目
                    if(homeBean.getEvaluate_status() != 1){
                        //去评价
                        RecommandActivity.startActivity(OrderCompTabFragment.this.getContext(),
                                homeBean.getProject_id(), 1);
                    }
                });
            }
        };

        adapter.setOnItemClickListener((v, i) -> {
            //评价 1 项目
            WebViewDetailActivity.startActivity(OrderCompTabFragment.this.getContext(),
                    adapter.getAllData().get(i)
                    ,who);
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
    public void getData(OrderCompRes list) {
        if (page == 1)
            adapter.clear();

        if (list.getProject().size() != 10)
            adapter.stopMore();

        adapter.addAll(list.getProject());


    }


    @Override
    public void onRefresh() {
        page = 1;
        presenter.myProjectWait(page, who);
    }

    @Override
    public void onLoadMore() {
        page++;
        presenter.myProjectWait(page, who);
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
