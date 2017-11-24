package com.xunao.diaodiao.Fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Activity.ApplyDetailActivity;
import com.xunao.diaodiao.Activity.OrderProjProgressActivity;
import com.xunao.diaodiao.Activity.OrderSkillCompDetailActivity;
import com.xunao.diaodiao.Activity.RecommandActivity;
import com.xunao.diaodiao.Activity.WebViewActivity;
import com.xunao.diaodiao.Activity.WeiBaoProgActivity;
import com.xunao.diaodiao.Bean.ApplyPassReq;
import com.xunao.diaodiao.Bean.OrderSkillDoingRes;
import com.xunao.diaodiao.Bean.OrderSkillFinishRes;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.OrderSkillDoingPresenter;
import com.xunao.diaodiao.Present.OrderSkillFinishPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.OrderSkillDoingView;
import com.xunao.diaodiao.View.OrderSkillFinishView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.xunao.diaodiao.Common.Constants.SKILL_RELEASE_LINGGONG;
import static com.xunao.diaodiao.Common.Constants.SKILL_RELEASE_LINGGONG_DONE;
import static com.xunao.diaodiao.Common.Constants.SKILL_RELEASE_WEIBAO_DONE;


/**
 * Description:
 * Created by guzhenfu on 2017/8/19.
 */

public class OrderSkillTabFinishFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener, OrderSkillFinishView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;
    Unbinder unbinder;
    private String mParam1;

    @Inject
    OrderSkillFinishPresenter presenter;

    private RecyclerArrayAdapter<OrderSkillFinishRes.OddBean> adapter;
    private int page = 1;

    private int who;

    public static OrderSkillTabFinishFragment newInstance(String param1, int who) {
        OrderSkillTabFinishFragment fragment = new OrderSkillTabFinishFragment();
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
        adapter = new RecyclerArrayAdapter<OrderSkillFinishRes.OddBean>(getContext(), R.layout.order_comp_tab_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, OrderSkillFinishRes.OddBean homeBean) {
                baseViewHolder.setText(R.id.item_content, homeBean.getTitle());
                //baseViewHolder.setVisible(R.id.evaluation, false);
                baseViewHolder.setText(R.id.address, homeBean.getAddress());
                baseViewHolder.setText(R.id.time, homeBean.getIssue_time());
                baseViewHolder.setText(R.id.name, homeBean.getProject_type());
                baseViewHolder.setVisible(R.id.distance, false);
                if(who == Constants.SKILL_RELEASE_WEIBAO){
                    baseViewHolder.setText(R.id.price, " ￥ "+homeBean.getDoor_fee());
                    baseViewHolder.setText(R.id.days, "上门费");
                }else{
                    baseViewHolder.setText(R.id.price, " ￥ "+homeBean.getDaily_wage()+" / 天");
                    baseViewHolder.setText(R.id.days, "共"+homeBean.getTotal_day()+"天");
                }



                if(homeBean.getStatus() == 4){
                    //已取消
                    baseViewHolder.setVisible(R.id.time, true);
                    baseViewHolder.setText(R.id.request, "工作进度");
                    baseViewHolder.setVisible(R.id.evaluation, false);
                    baseViewHolder.setVisible(R.id.contact, false);
                }else{
                    //已完成
                    baseViewHolder.setText(R.id.request, "工作进度");
                    baseViewHolder.setVisible(R.id.contact, false);
                    if (homeBean.getEvaluate_status() == 2){
                        //未评价
                        baseViewHolder.setVisible(R.id.time, false);
                        baseViewHolder.setVisible(R.id.evaluation, true);
                        baseViewHolder.setText(R.id.evaluation, "去评价");
                        baseViewHolder.setTextColorRes(R.id.evaluation, R.color.accept_btn_default);

                        baseViewHolder.setOnClickListener(R.id.evaluation, v -> {
                            //评价 1 项目 待改
                            if(homeBean.getEvaluate_status() == 2){
                                if(who == Constants.SKILL_RELEASE_WEIBAO){
                                    RecommandActivity.startActivity(OrderSkillTabFinishFragment.this.getContext(),
                                            homeBean.getMaintenance_id(), 4);
                                }else{
                                    RecommandActivity.startActivity(OrderSkillTabFinishFragment.this.getContext(),
                                            homeBean.getOdd_id(), 3);
                                }

                            }

                        });

                    }else{
                        //已评价
                        baseViewHolder.setVisible(R.id.time, true);
                        baseViewHolder.setText(R.id.time, "已评价");
                        baseViewHolder.setVisible(R.id.evaluation, false);
                    }
                }


                baseViewHolder.setOnClickListener(R.id.request, v -> {
                    if(homeBean.getStatus() != 4){
                        if(who == Constants.SKILL_RELEASE_WEIBAO){
                            WeiBaoProgActivity.startActivity(OrderSkillTabFinishFragment.this.getContext(),
                                    homeBean.getMaintenance_id(), who);
                        }else{
                            OrderProjProgressActivity.startActivity(OrderSkillTabFinishFragment.this.getContext(),
                                    homeBean.getOdd_id(), SKILL_RELEASE_LINGGONG_DONE);
                        }
                    }else{
                        //查看
                        if(who == Constants.SKILL_RELEASE_WEIBAO){
                            WebViewActivity.startActivity(OrderSkillTabFinishFragment.this.getContext(),
                                    homeBean.getUrl(),
                                    homeBean.getMaintenance_id(),
                                    (homeBean.getStatus() != 4 && (homeBean.getEvaluate_status() == 2))
                                            ? SKILL_RELEASE_WEIBAO_DONE:0);
                        }else{
                            WebViewActivity.startActivity(OrderSkillTabFinishFragment.this.getContext(),
                                    homeBean.getUrl(),
                                    homeBean.getOdd_id(),
                                    (homeBean.getStatus() != 4 && (homeBean.getEvaluate_status() == 2))
                                            ? SKILL_RELEASE_LINGGONG_DONE:0);
                        }
                    }
                });

                baseViewHolder.setOnClickListener(R.id.contact, v -> {
                    ApplyPassReq applyPassReq = new ApplyPassReq();
                    applyPassReq.setTechnician_id(homeBean.getTechnician_id());
                    applyPassReq.setProject_id(1000);
                    ApplyDetailActivity.startActivity(OrderSkillTabFinishFragment.this.getContext(),
                            applyPassReq);
                });

            }
        };

        adapter.setOnItemClickListener((v, i) -> {
            if(who == Constants.SKILL_RELEASE_WEIBAO){
                WebViewActivity.startActivity(OrderSkillTabFinishFragment.this.getContext(),
                        adapter.getAllData().get(i).getUrl(),
                        adapter.getAllData().get(i).getMaintenance_id(),
                        (adapter.getAllData().get(i).getStatus() != 4 &&
                                (adapter.getAllData().get(i).getEvaluate_status() == 2 ))
                                ? SKILL_RELEASE_WEIBAO_DONE:0);
            }else{
                WebViewActivity.startActivity(OrderSkillTabFinishFragment.this.getContext(),
                        adapter.getAllData().get(i).getUrl(),
                        adapter.getAllData().get(i).getOdd_id(),
                        (adapter.getAllData().get(i).getStatus() != 4 &&
                                (adapter.getAllData().get(i).getEvaluate_status() == 2 ))
                                ? SKILL_RELEASE_LINGGONG_DONE:0);
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
    public void getData(OrderSkillFinishRes list) {
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
        page = 1;
        presenter.mySkillFinish(page, who);
    }

    @Override
    public void onLoadMore() {
        page++;
        presenter.mySkillFinish(page, who);
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
