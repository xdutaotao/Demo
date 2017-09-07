package com.xunao.diaodiao.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Activity.FeedBackDetailActivity;
import com.xunao.diaodiao.Activity.RecommandActivity;
import com.xunao.diaodiao.Activity.RecordDetailActivity;
import com.xunao.diaodiao.Bean.MyRateRes;
import com.xunao.diaodiao.Present.MyRatingPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.MyRatingView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment extends BaseFragment implements MyRatingView {
    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private String mParam1;

    private RecyclerArrayAdapter<MyRateRes.ProjBean> adapter;

    @Inject
    MyRatingPresenter presenter;

    private int type = 0;

    public static TabFragment newInstance(String param1) {
        TabFragment fragment = new TabFragment();
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

        if (TextUtils.equals("我的申诉", mParam1)){
            adapter = new RecyclerArrayAdapter<MyRateRes.ProjBean>(getContext(), R.layout.record_item) {
                @Override
                protected void convert(BaseViewHolder baseViewHolder, MyRateRes.ProjBean s) {
                }
            };

            adapter.setOnItemClickListener((view1, i) -> {
                RecordDetailActivity.startActivity(TabFragment.this.getActivity());
            });

        }else if (TextUtils.equals("对方申诉", mParam1)){
            adapter = new RecyclerArrayAdapter<MyRateRes.ProjBean>(getContext(), R.layout.record_item) {
                @Override
                protected void convert(BaseViewHolder baseViewHolder, MyRateRes.ProjBean s) {
                }
            };

            adapter.setOnItemClickListener((view1, i) -> {
                RecordDetailActivity.startActivity(TabFragment.this.getActivity());
            });

        }else if (TextUtils.equals("已评价", mParam1)){
            adapter = new RecyclerArrayAdapter<MyRateRes.ProjBean>(getContext(), R.layout.my_already_rating_item) {
                @Override
                protected void convert(BaseViewHolder baseViewHolder, MyRateRes.ProjBean s) {
                }
            };

            adapter.setOnItemClickListener((view1, i) -> {
                FeedBackDetailActivity.startActivity(TabFragment.this.getActivity());
            });

            type = 2;
            presenter.getRating(getContext(), type);
        }else if (TextUtils.equals("待评价", mParam1)){
            adapter = new RecyclerArrayAdapter<MyRateRes.ProjBean>(getContext(), R.layout.my_rating_item) {
                @Override
                protected void convert(BaseViewHolder baseViewHolder, MyRateRes.ProjBean s) {
                }
            };

            adapter.setOnItemClickListener((view1, i) -> {
                RecommandActivity.startActivity(TabFragment.this.getActivity());
            });

            type = 1;
            presenter.getRating(getContext(), type);
        }

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);



        return view;
    }

    @Override
    public void getData(MyRateRes res) {
        adapter.clear();
        adapter.addAll(res.getProject());
    }


    public String getTitle() {
        return mParam1;
    }


    @Override
    public void onFailure() {

    }


}
