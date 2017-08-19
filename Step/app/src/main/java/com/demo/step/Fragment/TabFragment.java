package com.demo.step.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.step.R;
import com.demo.step.Widget.CustomWebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class TabFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.web_view)
    CustomWebView webView;

    Unbinder unbinder;
    private String mParam1;

    // TODO: Rename and change types and number of parameters
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
        View view = inflater.inflate(R.layout.fragment_tab, container, false);

        unbinder = ButterKnife.bind(this, view);

        webView.loadUrl(mParam1);

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
