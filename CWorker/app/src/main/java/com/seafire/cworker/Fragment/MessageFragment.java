package com.seafire.cworker.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.seafire.cworker.Present.MessagePresenter;
import com.seafire.cworker.R;
import com.seafire.cworker.View.MessageView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class MessageFragment extends BaseFragment implements MessageView {
    private static final String ARG_PARAM1 = "param1";
    @Inject
    MessagePresenter presenter;
    @BindView(R.id.msg)
    TextView msg;
    @BindView(R.id.contact)
    TextView contact;
    @BindView(R.id.content)
    FrameLayout content;
    private String mParam1;

    private ArrayList<Fragment> fragments;

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

        fragments = new ArrayList<>();
        fragments.add(MsgFragment.newInstance());
        fragments.add(ContactFragment.newInstance());

        msg.setOnClickListener(v -> {
            v.setBackgroundResource(android.R.color.white);
            msg.setTextColor(getResources().getColor(R.color.colorPrimary));

            contact.setBackgroundResource(R.color.colorPrimary);
            contact.setTextColor(getResources().getColor(android.R.color.white));
            setFragment(0);
        });

        contact.setOnClickListener(v -> {
            v.setBackgroundResource(android.R.color.white);
            contact.setTextColor(getResources().getColor(R.color.colorPrimary));

            msg.setBackgroundResource(R.color.colorPrimary);
            msg.setTextColor(getResources().getColor(android.R.color.white));
            setFragment(1);
        });

        return view;
    }

    private void setFragment(int index) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.content, fragments.get(index));
        transaction.commit();
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
