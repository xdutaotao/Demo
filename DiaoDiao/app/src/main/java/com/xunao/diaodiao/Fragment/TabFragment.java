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
import com.xunao.diaodiao.Activity.RecordDetailActivity;
import com.xunao.diaodiao.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private String mParam1;

    private RecyclerArrayAdapter<String> adapter;

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

        if (TextUtils.equals("我的申诉", mParam1)){
            adapter = new RecyclerArrayAdapter<String>(getContext(), R.layout.record_item) {
                @Override
                protected void convert(BaseViewHolder baseViewHolder, String s) {
                }
            };
        }else if (TextUtils.equals("对方申诉", mParam1)){
            adapter = new RecyclerArrayAdapter<String>(getContext(), R.layout.record_item) {
                @Override
                protected void convert(BaseViewHolder baseViewHolder, String s) {
                }
            };
        }else{
            adapter = new RecyclerArrayAdapter<String>(getContext(), R.layout.my_rating_item) {
                @Override
                protected void convert(BaseViewHolder baseViewHolder, String s) {
                }
            };
        }

        adapter.setOnItemClickListener((view1, i) -> {
            RecordDetailActivity.startActivity(TabFragment.this.getActivity());
        });

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("1");
        list.add("1");
        adapter.addAll(list);


        return view;
    }


    public String getTitle() {
        return mParam1;
    }


}
