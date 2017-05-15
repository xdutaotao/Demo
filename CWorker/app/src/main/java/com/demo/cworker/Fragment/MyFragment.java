package com.demo.cworker.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.demo.cworker.Activity.LoginActivity;
import com.demo.cworker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.login_btn)
    Button loginBtn;
    private String mParam1;


    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance(String param1) {
        MyFragment fragment = new MyFragment();
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
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        ButterKnife.bind(this, view);

        loginBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:
                LoginActivity.startActivity(getContext());
                break;
        }
    }
}
