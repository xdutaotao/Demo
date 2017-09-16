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
import com.xunao.diaodiao.Bean.MyComplaintRes;
import com.xunao.diaodiao.Present.MyComplaintRecordPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.MyComplaintRecordView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class MyComplaintRecordFragment extends BaseFragment implements MyComplaintRecordView, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {
    private static final String ARG_PARAM1 = "param1";
    @Inject
    MyComplaintRecordPresenter presenter;
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;
    private String mParam1;

    private RecyclerArrayAdapter<MyComplaintRes.Appeal> adapter;
    private int page;

    public static MyComplaintRecordFragment newInstance(String param1) {
        MyComplaintRecordFragment fragment = new MyComplaintRecordFragment();
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

        adapter = new RecyclerArrayAdapter<MyComplaintRes.Appeal>(getContext(), R.layout.record_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, MyComplaintRes.Appeal s) {
                baseViewHolder.setText(R.id.rating_name, s.getTitle());
                if (s.getStatus() == 1){
                    //已处理
                    baseViewHolder.setText(R.id.rating_text, "已处理");
                    baseViewHolder.setTextColorRes(R.id.rating_text, R.color.accept_btn_default);
                }else{
                    baseViewHolder.setText(R.id.rating_text, "处理中");
                    baseViewHolder.setTextColorRes(R.id.rating_text, R.color.colorAccent);
                }

                baseViewHolder.setText(R.id.content, s.getContent());
                baseViewHolder.setText(R.id.content_num, s.getAppeal_order()+"");

            }
        };

        adapter.setOnItemClickListener((view1, i) -> {

        });

        recyclerView.setAdapterDefaultConfig(adapter, this, this);
        onRefresh();

        return view;
    }

    @Override
    public void getData(MyComplaintRes res) {
        if (page == 1){
            adapter.clear();
            adapter.addAll(res.getAppeal());
        }else{
            if (res.getAppeal().size() < 10){
                adapter.stopMore();
            }
            adapter.addAll(res.getAppeal());

        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        presenter.getMyComplaint(page);
    }

    @Override
    public void onLoadMore() {
        page++;
        presenter.getMyComplaint(page);
    }

    @Override
    public void onFailure() {
        //adapter.stopMore();

        recyclerView.showError();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }



}
