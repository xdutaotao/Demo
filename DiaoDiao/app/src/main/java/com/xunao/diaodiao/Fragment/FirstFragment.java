package com.xunao.diaodiao.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.MainActivity;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ShareUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.background)
    ImageView background;
    @BindView(R.id.come_in)
    Button comeIn;

    // TODO: Rename and change types of parameters
    private int mParam1;
    private boolean mParam2;

    public static FirstFragment newInstance(int res, boolean isShow) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, res);
        args.putBoolean(ARG_PARAM2, isShow);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getBoolean(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        ButterKnife.bind(this, view);
        background.setImageResource(mParam1);
        comeIn.setVisibility(mParam2 ? View.VISIBLE : View.GONE);
        comeIn.setOnClickListener(v -> {
            ShareUtils.putValue(Constants.IS_COME_OVER, true);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        });
        return view;
    }
}
