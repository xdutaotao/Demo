package com.demo.cworker.Fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.cworker.Present.SearchPresenter;
import com.demo.cworker.R;
import com.demo.cworker.View.SearchView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class SearchFragment extends BaseFragment implements SearchView, View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    @Inject
    SearchPresenter presenter;
    @BindView(R.id.type_image)
    ImageView typeImage;
    @BindView(R.id.edit_text)
    EditText editText;
    @BindView(R.id.cancle_action)
    ImageView cancleAction;
    @BindView(R.id.search)
    TextView search;
    @BindView(R.id.flow_layout)
    TagFlowLayout flowLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.clear)
    TextView clear;
    private String mParam1;

    private List<String> list = new ArrayList<>();
    private RecyclerArrayAdapter<String> adapter;

    public static SearchFragment newInstance(String param1) {
        SearchFragment fragment = new SearchFragment();
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        for(int i=0; i< 10; i++){
            list.add(i+"");
        }
        flowLayout.setAdapter(new TagAdapter<String>(list) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {
                TextView view = (TextView) LayoutInflater.from(parent.getContext()).inflate(android.R.layout.test_list_item, parent);
                view.setText(list.get(position));
                return view;
            }
        });


        adapter = new RecyclerArrayAdapter<String>(getContext(), R.layout.search_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setText(R.id.search_text, s);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.addAll(list);

        clear.setOnClickListener(this);

        return view;
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clear:
                adapter.clear();
                break;
        }
    }
}
