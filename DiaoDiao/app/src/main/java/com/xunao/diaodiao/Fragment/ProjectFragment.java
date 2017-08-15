package com.xunao.diaodiao.Fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Present.MessagePresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.MessageView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class ProjectFragment extends BaseFragment implements MessageView, View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    @Inject
    MessagePresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private String mParam1;

    private RecyclerArrayAdapter<String> adapter;

    public static ProjectFragment newInstance(String param1) {
        ProjectFragment fragment = new ProjectFragment();
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
        View view = inflater.inflate(R.layout.fragment_project, container, false);
        ButterKnife.bind(this, view);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        hideToolbarBack(toolBar, titleText, "项目");

        adapter = new RecyclerArrayAdapter<String>(getContext(), R.layout.project_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {

            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        adapter.addAll(list);

        return view;
    }

    @Override
    public void onClick(View v) {
    }


    @Override
    public void onFailure() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

}
