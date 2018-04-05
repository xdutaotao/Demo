package com.luck.number.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luck.number.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LotteryFragment extends BaseFragment {


    public static LotteryFragment newInstance() {
        LotteryFragment fragment = new LotteryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lottery, container, false);
        return view;
    }

}
