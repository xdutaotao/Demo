package com.demo.cworker.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.demo.cworker.Activity.CheckPhoneActivity;
import com.demo.cworker.Activity.LoginActivity;
import com.demo.cworker.Model.User;
import com.demo.cworker.Model.UserInfo;
import com.demo.cworker.Present.LoginPresenter;
import com.demo.cworker.R;
import com.demo.cworker.Utils.ToastUtil;
import com.demo.cworker.View.LoginView;
import com.demo.cworker.Widget.CustomDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MyFragment extends BaseFragment implements View.OnClickListener, LoginView {
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
    @BindView(R.id.personal_word)
    RelativeLayout personalWord;
    @BindView(R.id.change_pwd)
    RelativeLayout changePwd;
    @BindView(R.id.personal_center)
    RelativeLayout personalCenter;
    @BindView(R.id.setting)
    RelativeLayout setting;
    @BindView(R.id.help)
    RelativeLayout help;
    @BindView(R.id.suggest)
    RelativeLayout suggest;
    @BindView(R.id.about)
    RelativeLayout about;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    private String mParam1;

    @Inject
    LoginPresenter presenter;

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
        getActivityComponent().inject(this);
        presenter.attachView(this);

        loginBtn.setOnClickListener(this);
        loginText.setOnClickListener(this);
        headIcon.setOnClickListener(this);

        changePwd.setOnClickListener(this);
        personalWord.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(User.getInstance().getUserId())) {
            loginBtn.setText("点击登录");
            loginText.setText("请登录");
            today.setText("0");
            month.setText("0");
            year.setText("0");
            total.setText("0");
        } else {
            loginBtn.setText("退出登录");
            if (User.getInstance().getUserInfo() != null) {
                UserInfo userInfo = User.getInstance().getUserInfo();
                if (!TextUtils.isEmpty(userInfo.getPerson().getFace())) {
                    Glide.with(this)
                            .load(userInfo.getPerson().getFace())
                            .placeholder(R.drawable.ic_launcher_round)
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
    public void updateData() {
        super.updateData();
        onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                if (TextUtils.isEmpty(User.getInstance().getUserId())){
                    LoginActivity.startActivity(getContext());
                }else{
                    presenter.logout(getContext(), User.getInstance().getUserId());
                    User.getInstance().clearUser();
                    scrollView.scrollTo(0, 0);
                    onResume();
                }
                break;
            case R.id.login_text:
            case R.id.head_icon:
                if (User.getInstance().getUserInfo() != null) {
                    CustomDialog.getInstance().show(getActivity(), User.getInstance().getUserInfo().getPerson().getFace());
                } else {
                    LoginActivity.startActivity(getContext());
                }

                break;

            case R.id.change_pwd:
                CheckPhoneActivity.startActivity(getContext(), true);
                break;

            case R.id.personal_word:
                if (User.getInstance().getUserInfo() == null){
                    ToastUtil.show("请登录");
                }else{

                }
                break;
        }
    }


    @Override
    public void onFailure() {

    }

    @Override
    public void getData(String data) {
        ToastUtil.show("退出成功");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
