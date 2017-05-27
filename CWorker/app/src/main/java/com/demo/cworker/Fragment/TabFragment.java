package com.demo.cworker.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.cworker.Activity.RecommandActivity;
import com.demo.cworker.Bean.PackageBean;
import com.demo.cworker.R;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.demo.cworker.Common.Constants.INTENT_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String SELECT_KEY = "SELECT_KEY";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private String mParam1;

    private RecyclerArrayAdapter<String> adapter;
    private ArrayList<String> listBean;
    private ArrayList<String> selectList;

    private List<String> changeSelectList = new ArrayList<>();

    public static TabFragment newInstance(String param1, ArrayList<String> list, ArrayList<String> selectList) {
        TabFragment fragment = new TabFragment();
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
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        unbinder = ButterKnife.bind(this, view);

        adapter = new RecyclerArrayAdapter<String>(getContext(), R.layout.recommand_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setText(R.id.recommend_tv, s);
                for(String item: selectList){
                    if (TextUtils.equals(s, item)){
                        baseViewHolder.setVisible(R.id.recommend_img, true);
                        return;
                    }
                }
                baseViewHolder.setVisible(R.id.recommend_img, false);
            }
        };

        adapter.setOnItemClickListener((view1, i) -> {
            if (selectList != null && selectList.size() > 0){
                int length = selectList.size();
                for(int j=0; j<length; j++){
                    String temp = selectList.get(j);
                    if (TextUtils.equals(temp, listBean.get(i))){
                        selectList.remove(temp);
                        ((RecommandActivity)getActivity()).changeList.remove(temp);
                        view1.findViewById(R.id.recommend_img).setVisibility(View.GONE);
                        return;
                    }
                }
                selectList.add(listBean.get(i));
                ((RecommandActivity)getActivity()).changeList.add(listBean.get(i));
                view1.findViewById(R.id.recommend_img).setVisibility(View.VISIBLE);
            }else{
                selectList.add(listBean.get(i));
                ((RecommandActivity)getActivity()).changeList.add(listBean.get(i));
                view1.findViewById(R.id.recommend_img).setVisibility(View.VISIBLE);
            }

        });

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
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
