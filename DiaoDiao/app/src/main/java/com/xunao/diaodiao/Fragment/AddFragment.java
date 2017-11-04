package com.xunao.diaodiao.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xunao.diaodiao.Bean.FindProjectRes;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.AddView;
import com.xunao.diaodiao.Present.AddPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * create by
 */
public class AddFragment extends BaseFragment implements AddView {
    private static final String ARG_PARAM1 = "param1";
    @Inject
    AddPresenter presenter;
    private String mParam1;

    public static AddFragment newInstance(String param1) {
        AddFragment fragment = new AddFragment();
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
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        ButterKnife.bind(this, view);
        getActivityComponent().inject(this);
        presenter.attachView(this);
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
    public void getData(FindProjectRes s) {

    }
}
