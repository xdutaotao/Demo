package com.xunao.diaodiao.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xunao.diaodiao.Activity.AboutActivity;
import com.xunao.diaodiao.Activity.BankActivity;
import com.xunao.diaodiao.Activity.ComplaintRecordActivity;
import com.xunao.diaodiao.Activity.LoginActivity;
import com.xunao.diaodiao.Activity.MessageActivity;
import com.xunao.diaodiao.Activity.MoneyActivity;
import com.xunao.diaodiao.Activity.MyFavoriteActivity;
import com.xunao.diaodiao.Activity.MyRatingActivity;
import com.xunao.diaodiao.Activity.PersonalActivity;
import com.xunao.diaodiao.Activity.SelectCompanyActivity;
import com.xunao.diaodiao.Activity.SelectMemoryActivity;
import com.xunao.diaodiao.Activity.SelectNormalActivity;
import com.xunao.diaodiao.Activity.SelectSkillActivity;
import com.xunao.diaodiao.Activity.SettingActivity;
import com.xunao.diaodiao.Activity.SuggestActivity;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.MyPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.MyView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.TYPE_KEY;

public class MyFragment extends BaseFragment implements View.OnClickListener, MyView {
    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.head_icon)
    ImageView headIcon;
    @BindView(R.id.money)
    LinearLayout money;
    @BindView(R.id.bank)
    LinearLayout bank;
    @BindView(R.id.my_favorite)
    RelativeLayout myFavorite;
    @BindView(R.id.my_rating)
    RelativeLayout myRating;
    @BindView(R.id.complaint_record)
    RelativeLayout complaintRecord;
    @BindView(R.id.about_diaodiao)
    RelativeLayout aboutDiaodiao;
    @BindView(R.id.feedback)
    RelativeLayout feedback;
    @BindView(R.id.contact_service)
    RelativeLayout contactService;
    @BindView(R.id.setting)
    ImageView setting;
    @BindView(R.id.message)
    ImageView message;
    @BindView(R.id.name)
    TextView name;
    private String mParam1;

    @Inject
    MyPresenter presenter;

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

        myFavorite.setOnClickListener(this);
        myRating.setOnClickListener(this);
        complaintRecord.setOnClickListener(this);
        aboutDiaodiao.setOnClickListener(this);
        contactService.setOnClickListener(this);
        feedback.setOnClickListener(this);
        setting.setOnClickListener(this);
        message.setOnClickListener(this);
        headIcon.setOnClickListener(this);
        money.setOnClickListener(this);
        bank.setOnClickListener(this);
        name.setOnClickListener(this);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (User.getInstance().getUserInfo() != null){
            String mobile = User.getInstance().getUserInfo().getMobile();
            name.setText(mobile);
        }else{
            name.setText("未登录");
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
            case R.id.my_favorite:
                MyFavoriteActivity.startActivity(getActivity());
                break;

            case R.id.my_rating:
                MyRatingActivity.startActivity(getActivity());
                break;

            case R.id.complaint_record:
                ComplaintRecordActivity.startActivity(getActivity());
                break;

            case R.id.about_diaodiao:
                AboutActivity.startActivity(getActivity());
                break;

            case R.id.contact_service:
//                if (TextUtils.isEmpty(User.getInstance().getUserId())){
//                    LoginActivity.startActivity(getActivity());
//                }else{
//                    if (ShareUtils.getValue(TYPE_KEY, 0) == 0)
//                        SelectMemoryActivity.startActivity(getActivity());
//                    else{
//
//                    }
//                }

                LoginActivity.startActivity(getContext());

                break;

            case R.id.setting:
                SettingActivity.startActivity(getActivity());
                break;

            case R.id.message:
                MessageActivity.startActivity(getActivity());
                break;

            case R.id.feedback:
                SuggestActivity.startActivity(getActivity());
                break;

            case R.id.head_icon:
                PersonalActivity.startActivity(getActivity());
                break;

            case R.id.money:
                MoneyActivity.startActivity(getActivity());
                break;

            case R.id.bank:
                BankActivity.startActivity(getActivity());
                break;

            case R.id.name:
                if (TextUtils.isEmpty(User.getInstance().getUserId())){
                    LoginActivity.startActivity(MyFragment.this.getContext());
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
    public void signToday(String bean) {
        ToastUtil.show("签到成功");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

}
