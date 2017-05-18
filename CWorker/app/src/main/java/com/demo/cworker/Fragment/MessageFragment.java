package com.demo.cworker.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.cworker.R;
import com.demo.cworker.View.MessageView;
import com.demo.cworker.Present.MessagePresenter;
import com.tencent.bugly.crashreport.CrashReport;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * create by
 */
public class MessageFragment extends BaseFragment implements MessageView {
    private static final String ARG_PARAM1 = "param1";
    @Inject
    MessagePresenter presenter;
    private String mParam1;

    public static MessageFragment newInstance(String param1) {
        MessageFragment fragment = new MessageFragment();
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
        View view = inflater.inflate(R.layout.fragment_message, container, false);
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

}
