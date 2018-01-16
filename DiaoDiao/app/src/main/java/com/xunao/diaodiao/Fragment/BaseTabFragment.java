package com.xunao.diaodiao.Fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Activity.ProjectDetailActivity;
import com.xunao.diaodiao.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Description:
 * Created by guzhenfu on 2017/8/19.
 */

public abstract class BaseTabFragment<T> extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;
    Unbinder unbinder;
    private String mParam1;

    private RecyclerArrayAdapter<T> adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.single_fragment_recycler_view, container, false);
        unbinder = ButterKnife.bind(this, view);

        setRecyclerViewAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    protected abstract void setRecyclerViewAdapter(RecyclerArrayAdapter<T> adapter);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public String getTitle() {
        return mParam1;
    }
}
