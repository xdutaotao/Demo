package com.xunao.diaodiao.Fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

public class ReleaseTabItemFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;

    @BindView(R.id.next)
    Button next;

    // TODO: Rename and change types of parameters
    private String mParam1;

    private RecyclerArrayAdapter<String> adapter;

    public static ReleaseTabItemFragment newInstance(String param1) {
        ReleaseTabItemFragment fragment = new ReleaseTabItemFragment();
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
        View view = inflater.inflate(R.layout.fragment_release_tab_item, container, false);
        unbinder = ButterKnife.bind(this, view);

        adapter = new RecyclerArrayAdapter<String>(getContext(), R.layout.release_tab_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {

            }
        };

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        recyclerView.setAdapter(adapter);

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("1");
        list.add("2");
        list.add("3");
        adapter.addAll(list);

        next.setOnClickListener(v -> {
            ReleaseSkillInforActivity.startActivity(ReleaseTabItemFragment.this.getContext());
        });

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
