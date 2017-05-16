package com.demo.cworker.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.demo.cworker.Activity.LoginActivity;
import com.demo.cworker.Model.User;
import com.demo.cworker.Model.UserInfo;
import com.demo.cworker.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MyFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.login_btn)
    TextView loginBtn;
    @BindView(R.id.head_icon)
    ImageView headIcon;
    @BindView(R.id.login_text)
    TextView loginText;
    @BindView(R.id.today)
    TextView today;
    @BindView(R.id.month)
    TextView month;
    @BindView(R.id.year)
    TextView year;
    @BindView(R.id.total)
    TextView total;
    private String mParam1;

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
        loginText.setOnClickListener(this);
        headIcon.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(User.getInstance().getUserId())){
            loginBtn.setText("点击登录");
        }else{
            loginBtn.setText("退出登录");
            if (User.getInstance().getUserInfo() != null){
                UserInfo userInfo = User.getInstance().getUserInfo();
                if (!TextUtils.isEmpty(userInfo.getPerson().getFace())){
                    Glide.with(this)
                            .load(userInfo.getPerson().getFace())
                            .bitmapTransform(new CropCircleTransformation(getContext()))
                            .into(headIcon);
                }

                loginText.setText(userInfo.getPerson().getName());
                today.setText(userInfo.getUps().getToday() + "");
                month.setText(userInfo.getUps().getMonth() + "");
                year.setText(userInfo.getUps().getYear() + "");
                total.setText(userInfo.getUps().getTotal() + "");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
            case R.id.login_text:
            case R.id.head_icon:
                LoginActivity.startActivity(getContext());
                break;
        }
    }
}
