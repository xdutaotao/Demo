package com.demo.cworker.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.cworker.R;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        unbinder = ButterKnife.bind(this, view);

        adapter = new RecyclerArrayAdapter<String>(getContext(), R.layout.recommand_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setText(R.id.recommend_tv, s);
            }
        };
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        setList();
        return view;
    }

    private void setList(){
        String[] list = getResources().getStringArray(R.array.tabNames);
        if (TextUtils.equals(list[0], mParam1)){
            adapter.addAll(getResources().getStringArray(R.array.typeOne));
        }else if (TextUtils.equals(list[1], mParam1)){
            adapter.addAll(getResources().getStringArray(R.array.typeTwo));
        }else if (TextUtils.equals(list[2], mParam1)){
            adapter.addAll(getResources().getStringArray(R.array.typeThree));
        }else if (TextUtils.equals(list[3], mParam1)){
            adapter.addAll(getResources().getStringArray(R.array.typeFour));
        }else if (TextUtils.equals(list[4], mParam1)){
            adapter.addAll(getResources().getStringArray(R.array.typeFive));
        }else if (TextUtils.equals(list[5], mParam1)){
            adapter.addAll(getResources().getStringArray(R.array.typeSix));
        }else if (TextUtils.equals(list[6], mParam1)){
            adapter.addAll(getResources().getStringArray(R.array.typeSeven));
        }
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
