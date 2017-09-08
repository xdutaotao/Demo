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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoRateFragment extends BaseFragment implements MyRatingView {
    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private String mParam1;

    private RecyclerArrayAdapter<MyRateRes.ProjBean> adapter;

    @Inject
    MyRatingPresenter presenter;

    public static NoRateFragment newInstance(String param1) {
        NoRateFragment fragment = new NoRateFragment();
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

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);


        adapter = new RecyclerArrayAdapter<MyRateRes.ProjBean>(getContext(), R.layout.my_rating_item) {
                @Override
                protected void convert(BaseViewHolder baseViewHolder, MyRateRes.ProjBean s) {
                    baseViewHolder.setText(R.id.rating_name, s.getTitle());
                    baseViewHolder.setText(R.id.address, s.getAddress());
                    baseViewHolder.setText(R.id.price, s.getPrice());
                    if (s.getEvaluate_type() == 1){
                        //发单人
                        baseViewHolder.setText(R.id.project_detail, "项目进度");
                        baseViewHolder.setText(R.id.price_detail, "价格");
                    }else{
                        baseViewHolder.setText(R.id.project_detail, "维保进度");
                        baseViewHolder.setText(R.id.price_detail, "上门费");
                    }

                }
            };

        adapter.setOnItemClickListener((view1, i) -> {
                RecommandActivity.startActivity(NoRateFragment.this.getActivity());
            });

        presenter.getRating(getContext());
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
