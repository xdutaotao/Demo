package com.xunao.diaodiao.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Activity.ReleaseSkillActivity;
import com.xunao.diaodiao.Activity.ReleaseSkillInforActivity;
import com.xunao.diaodiao.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

public class ReleaseTabItemFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String SELECT_KEY = "SELECT_KEY";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mParam1;

    private RecyclerArrayAdapter<String> adapter;
    private ArrayList<String> listBean;
    private ArrayList<String> selectList;

    public static ReleaseTabItemFragment newInstance(String param1, ArrayList<String> list, ArrayList<String> selectList) {
        ReleaseTabItemFragment fragment = new ReleaseTabItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putStringArrayList(SELECT_KEY, selectList);
        args.putStringArrayList(INTENT_KEY, list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            selectList = getArguments().getStringArrayList(SELECT_KEY);
            listBean = getArguments().getStringArrayList(INTENT_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_release_tab_item, container, false);
        unbinder = ButterKnife.bind(this, view);

        adapter = new RecyclerArrayAdapter<String>(getContext(), R.layout.release_tab_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setText(R.id.recommend_tv, s);
                for(String item: selectList){
                    if (TextUtils.equals(s, item)){
                        baseViewHolder.setTextColorRes(R.id.recommend_tv, R.color.white);
                        baseViewHolder.setBackgroundRes(R.id.recommend_tv, R.drawable.btn_blue_bg);
                    }else{
                        baseViewHolder.setTextColorRes(R.id.recommend_tv, R.color.nav_gray);
                        baseViewHolder.setBackgroundRes(R.id.recommend_tv, R.drawable.btn_blank_bg);
                    }
                }

                baseViewHolder.setOnClickListener(R.id.recommend_tv, v -> {
                    if(selectList.contains(s)){
                        v.setBackgroundResource(R.drawable.btn_blank_bg);
                        ((TextView) v).setTextColor(getResources().getColor(R.color.gray));
                        selectList.remove(s);
                    }else {
                        v.setBackgroundResource(R.drawable.btn_blue_bg);
                        ((TextView) v).setTextColor(Color.WHITE);
                        selectList.add(s);
                    }

                });
            }
        };



        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        recyclerView.setAdapter(adapter);
        adapter.addAll(listBean);

        return view;
    }

    public String getTitle() {
        return mParam1;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
