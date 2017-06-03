package com.demo.cworker.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.demo.cworker.Activity.AboutActivity;
import com.demo.cworker.Activity.CheckPhoneActivity;
import com.demo.cworker.Activity.CollectHistoryActivity;
import com.demo.cworker.Activity.HelpActivity;
import com.demo.cworker.Activity.LoginActivity;
import com.demo.cworker.Activity.PersonalActivity;
import com.demo.cworker.Activity.SettingActivity;
import com.demo.cworker.Activity.SuggestActivity;
import com.demo.cworker.Activity.VIPActivity;
import com.demo.cworker.Common.Constants;
import com.demo.cworker.Model.User;
import com.demo.cworker.Model.UserInfo;
import com.demo.cworker.Present.MyPresenter;
import com.demo.cworker.R;
import com.demo.cworker.Utils.NetWorkUtils;
import com.demo.cworker.Utils.RxBus;
import com.demo.cworker.Utils.ShareUtils;
import com.demo.cworker.Utils.ToastUtil;
import com.demo.cworker.Utils.Utils;
import com.demo.cworker.View.MyView;
import com.demo.cworker.Widget.CustomDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MyFragment extends BaseFragment implements View.OnClickListener, MyView {
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
    @BindView(R.id.level)
    TextView level;
    @BindView(R.id.sign)
    TextView sign;
    @BindView(R.id.today_layout)
    LinearLayout todayLayout;
    private String mParam1;

    @Inject
    MyPresenter presenter;
    private int continueDays;

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
        personalCenter.setOnClickListener(this);
        suggest.setOnClickListener(this);
        about.setOnClickListener(this);
        setting.setOnClickListener(this);
        sign.setOnClickListener(this);
        help.setOnClickListener(this);
        todayLayout.setOnClickListener(this);
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
            Glide.with(this)
                    .load(R.drawable.ic_launcher_round)
                    .bitmapTransform(new CropCircleTransformation(getContext()))
                    .into(headIcon);
            level.setVisibility(View.GONE);
            sign.setVisibility(View.GONE);
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

                if (ShareUtils.getValue(Constants.POST_COLLECT_TIME, 0) != 0) {
                    today.setText(String.valueOf(ShareUtils.getValue(Constants.POST_COLLECT_TIME, 0)));
                    RxBus.getInstance().post(Constants.POST_COLLECT_TIME);
                } else {
                    today.setText("0");
                }

                month.setText(userInfo.getUps().getMonth() + "");
                year.setText(userInfo.getUps().getYear() + "");
                total.setText(userInfo.getUps().getTotal() + "");
                level.setText("LV" + ((int) (User.getInstance().getUserInfo().getPerson().getExperience() / 3500)));
                level.setVisibility(View.VISIBLE);
                sign.setVisibility(View.VISIBLE);
                sign.setText(TextUtils.equals(Utils.getNowDate(), ShareUtils.getValue("sign", "")) ? "已签到" : "点击签到");
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
                if (TextUtils.isEmpty(User.getInstance().getUserId())) {
                    LoginActivity.startActivity(getContext());
                } else {
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
                if (User.getInstance().getUserInfo() == null) {
                    ToastUtil.show("请登录");
                } else {
                    PersonalActivity.startActivity(getContext());
                }
                break;

            case R.id.personal_center:
                if (User.getInstance().getUserInfo() == null) {
                    ToastUtil.show("请登录");
                } else {
                    VIPActivity.startActivity(getContext());
                }

                break;

            case R.id.suggest:
                SuggestActivity.startActivity(getContext());
                break;

            case R.id.about:
                AboutActivity.startActivity(getContext());
                break;

            case R.id.setting:
                SettingActivity.startActivity(getContext());
                break;

            case R.id.sign:
                if (!NetWorkUtils.isNetworkAvailable()) {
                    ToastUtil.show("没有网络不能签到");
                    return;
                }
                if (!TextUtils.isEmpty(ShareUtils.getValue("sign", ""))) {
                    long time = Utils.convert2long(ShareUtils.getValue("sign", ""));
                    long now = Utils.convert2long(Utils.getNowDate());
                    if (now - time > 3600 * 1000 * 24) {
                        ShareUtils.putValue("sign", Utils.getNowDate());
                        ShareUtils.putValue("continue", 1);
                        continueDays = 1;
                        setSign(0);
                    } else {
                        if (!TextUtils.equals(ShareUtils.getValue("sign", ""), Utils.getNowDate())) {
                            continueDays = ShareUtils.getValue("continue", 0);
                            continueDays++;
                            if (continueDays % 7 == 0){
                                setSign(continueDays/7);
                            }else{
                                setSign(0);
                            }

                            ShareUtils.putValue("continue", continueDays);
                            ShareUtils.putValue("sign", Utils.getNowDate());
                        }else{
                            continueDays = ShareUtils.getValue("continue", 0);
                        }
                    }
                } else {
                    ShareUtils.putValue("continue", 1);
                    continueDays = 1;
                    ShareUtils.putValue("sign", Utils.getNowDate());
                    setSign(0);
                }
                sign.setText("已签到");
                CustomDialog.showContinuePop(getActivity(), continueDays, User.getInstance().getUserInfo().getPerson().getVIP() != 1);
                break;

            case R.id.help:
                HelpActivity.startActivity(getContext());
                break;

            case R.id.today_layout:
                if (!TextUtils.equals(today.getText().toString(), "0"))
                    CollectHistoryActivity.startActivity(getContext());
                break;
        }
    }

    private void setSign(int days){
        if (User.getInstance().getUserInfo().getPerson().getVIP() != 1){
            presenter.signToday(6 + days*10, 12);
        }else{
            presenter.signToday(5 + days*10, 10);
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
    public void signToday(UserInfo.PersonBean bean) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

}
