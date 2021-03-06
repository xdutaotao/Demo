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

import com.bumptech.glide.Glide;
import com.gzfgeh.iosdialog.IOSDialog;
import com.xunao.diaodiao.Activity.AboutActivity;
import com.xunao.diaodiao.Activity.BankActivity;
import com.xunao.diaodiao.Activity.ComplaintRecordActivity;
import com.xunao.diaodiao.Activity.EditCompanyActivity;
import com.xunao.diaodiao.Activity.EditPersonalActivity;
import com.xunao.diaodiao.Activity.EditSkillActivity;
import com.xunao.diaodiao.Activity.LoginActivity;
import com.xunao.diaodiao.Activity.MessageActivity;
import com.xunao.diaodiao.Activity.MoneyActivity;
import com.xunao.diaodiao.Activity.MyFavoriteActivity;
import com.xunao.diaodiao.Activity.MyRatingActivity;
import com.xunao.diaodiao.Activity.PersonalActivity;
import com.xunao.diaodiao.Activity.SelectMemoryActivity;
import com.xunao.diaodiao.Activity.SettingActivity;
import com.xunao.diaodiao.Activity.SuggestActivity;
import com.xunao.diaodiao.Bean.MyBean;
import com.xunao.diaodiao.Bean.PersonalRes;
import com.xunao.diaodiao.Bean.TypeInfoRes;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.MyPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.MyView;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.xunao.diaodiao.Common.Constants.COMPANY_TYPE;
import static com.xunao.diaodiao.Common.Constants.CUSTOM_TYPE;
import static com.xunao.diaodiao.Common.Constants.MESSAGE;
import static com.xunao.diaodiao.Common.Constants.SKILL_TYPE;
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
    @BindView(R.id.login)
    TextView login;
    @BindView(R.id.money_text)
    TextView moneyText;
    @BindView(R.id.bank_text)
    TextView bankText;
    @BindView(R.id.first_star)
    ImageView firstStar;
    @BindView(R.id.second_star)
    ImageView secondStar;
    @BindView(R.id.third_star)
    ImageView thirdStar;
    @BindView(R.id.fourth_star)
    ImageView fourthStar;
    @BindView(R.id.msg_icon)
    View msgIcon;
    @BindView(R.id.five_star)
    ImageView fiveStar;
    @BindView(R.id.star_layout)
    LinearLayout starLayout;
    @BindView(R.id.my_doc)
    RelativeLayout myDoc;
    private String mParam1;

    @Inject
    MyPresenter presenter;

    private String path;

    private PersonalRes.CompanyInfo companyInfo;
    private PersonalRes.TechnicianInfo technicianInfo;
    private PersonalRes.FamilyInfo familyInfo;

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
        login.setOnClickListener(this);
        myDoc.setOnClickListener(this);


        RxBus.getInstance().toObservable(String.class)
                .filter(s -> TextUtils.equals(s, MESSAGE))
                .subscribe(s -> {
                    msgIcon.setVisibility(View.VISIBLE);
                });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(User.getInstance().getUserId())) {
            name.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
            if (User.getInstance().getUserInfo() != null) {
                String mobile = User.getInstance().getUserInfo().getMobile();
                if (!TextUtils.isEmpty(mobile))
                    name.setText(mobile);
            }

            starLayout.setVisibility(View.VISIBLE);
        } else {
            name.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
            moneyText.setText("- / -");
            bankText.setText("- / -");
            starLayout.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.weidenglu)
                    .bitmapTransform(new CropCircleTransformation(getContext())).into(headIcon);
        }

        if (!TextUtils.isEmpty(User.getInstance().getUserId()))
            presenter.getInfo();
    }

    @Override
    public void updateData() {
        super.updateData();
        onResume();
    }

    @Override
    public void getData(MyBean data) {
        //name.setText(data.getName());
        path = data.getHead_img();
        Glide.with(this).load(path).placeholder(R.drawable.weidenglu)
                .bitmapTransform(new CropCircleTransformation(getContext())).into(headIcon);
        moneyText.setText(data.getBalance());
        bankText.setText(data.getCard_number() + "张");
        msgIcon.setVisibility(data.getUnread_message() == 1 ? View.VISIBLE : View.GONE);

        if (data.getUnread_message() == 1) {
            ShareUtils.putValue("message", 1);
        } else {
            ShareUtils.putValue("message", 0);
            RxBus.getInstance().post("dismiss");
        }

        switch (Integer.valueOf(data.getUser_point())) {
            case 0:
                firstStar.setImageResource(R.drawable.pinfeng2);
                secondStar.setImageResource(R.drawable.pinfeng2);
                thirdStar.setImageResource(R.drawable.pinfeng2);
                fourthStar.setImageResource(R.drawable.pinfeng2);
                fiveStar.setImageResource(R.drawable.pinfeng2);
                break;
            case 1:
                firstStar.setImageResource(R.drawable.pinfeng);
                secondStar.setImageResource(R.drawable.pinfeng2);
                thirdStar.setImageResource(R.drawable.pinfeng2);
                fourthStar.setImageResource(R.drawable.pinfeng2);
                fiveStar.setImageResource(R.drawable.pinfeng2);
                break;
            case 2:
                firstStar.setImageResource(R.drawable.pinfeng);
                secondStar.setImageResource(R.drawable.pinfeng);
                thirdStar.setImageResource(R.drawable.pinfeng2);
                fourthStar.setImageResource(R.drawable.pinfeng2);
                fiveStar.setImageResource(R.drawable.pinfeng2);
                break;
            case 3:
                firstStar.setImageResource(R.drawable.pinfeng);
                secondStar.setImageResource(R.drawable.pinfeng);
                thirdStar.setImageResource(R.drawable.pinfeng);
                fourthStar.setImageResource(R.drawable.pinfeng2);
                fiveStar.setImageResource(R.drawable.pinfeng2);
                break;
            case 4:
                firstStar.setImageResource(R.drawable.pinfeng);
                secondStar.setImageResource(R.drawable.pinfeng);
                thirdStar.setImageResource(R.drawable.pinfeng);
                fourthStar.setImageResource(R.drawable.pinfeng);
                fiveStar.setImageResource(R.drawable.pinfeng2);
                break;
            case 5:
                firstStar.setImageResource(R.drawable.pinfeng);
                secondStar.setImageResource(R.drawable.pinfeng);
                thirdStar.setImageResource(R.drawable.pinfeng);
                fourthStar.setImageResource(R.drawable.pinfeng);
                fiveStar.setImageResource(R.drawable.pinfeng);
                break;
        }
    }

    @Override
    public void getPersonalData(PersonalRes s) {
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        switch (type) {
            case COMPANY_TYPE:
                companyInfo = s.getCompanyInfo();
                EditCompanyActivity.startActivity(getContext(), companyInfo);
                break;

            case SKILL_TYPE:
                technicianInfo = s.getTechnicianInfo();
                EditSkillActivity.startActivity(getContext(), technicianInfo);
                break;

            case CUSTOM_TYPE:
                familyInfo = s.getFamilyInfo();
                EditPersonalActivity.startActivity(getContext(), familyInfo);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_favorite:
                if (TextUtils.isEmpty(User.getInstance().getUserId())) {
                    LoginActivity.startActivity(getContext());
                } else {
                    MyFavoriteActivity.startActivity(getActivity());
                }

                break;

            case R.id.my_rating:
                if (TextUtils.isEmpty(User.getInstance().getUserId())) {
                    LoginActivity.startActivity(getContext());
                } else {
                    MyRatingActivity.startActivity(getActivity());
                }


                break;

            case R.id.complaint_record:
                if (TextUtils.isEmpty(User.getInstance().getUserId())) {
                    LoginActivity.startActivity(getContext());
                } else {
                    ComplaintRecordActivity.startActivity(getActivity());
                }

                break;

            case R.id.about_diaodiao:
                AboutActivity.startActivity(getActivity());
                break;

            case R.id.login:
                LoginActivity.startActivity(getContext());
                break;

            case R.id.contact_service:
                new IOSDialog(this.getContext()).builder()
                        .setMsg(Constants.tel)
                        .setNegativeButton("取消", null)
                        .setNegativeBtnColor(R.color.accept_btn_default)
                        .setPositiveBtnColor(R.color.accept_btn_default)
                        .setPositiveButton("呼叫", v1 -> {
                            Utils.startCallActivity(MyFragment.this.getContext(), Constants.tel);
                        })
                        .show();


                break;

            case R.id.setting:
                if (User.getInstance().getUserInfo() != null &&
                        !TextUtils.isEmpty(User.getInstance().getUserInfo().getMobile())) {
                    SettingActivity.startActivity(getActivity(), User.getInstance().getUserInfo().getMobile());
                } else {
                    SettingActivity.startActivity(getActivity(), "");
                }

                break;

            case R.id.message:
                if (TextUtils.isEmpty(User.getInstance().getUserId())) {
                    ToastUtil.show("未登录");
                } else {
                    msgIcon.setVisibility(View.GONE);
                    MessageActivity.startActivity(getActivity());
                }

                break;

            case R.id.feedback:
                SuggestActivity.startActivity(getActivity());
                break;

            case R.id.head_icon:
                if (TextUtils.isEmpty(User.getInstance().getUserId())) {
                    LoginActivity.startActivity(getContext());
                } else {
                    PersonalActivity.startActivity(getActivity(), path);
                }

                break;

            case R.id.money:
                if (TextUtils.isEmpty(User.getInstance().getUserId())) {
                    LoginActivity.startActivity(getContext());
                } else {
                    MoneyActivity.startActivity(getActivity());
                }

                break;

            case R.id.bank:
                if (TextUtils.isEmpty(User.getInstance().getUserId())) {
                    LoginActivity.startActivity(getContext());
                } else {
                    BankActivity.startActivity(getActivity());
                }

                break;

            case R.id.name:
                if (TextUtils.isEmpty(User.getInstance().getUserId())) {
                    LoginActivity.startActivity(MyFragment.this.getContext());
                }
                break;

            case R.id.my_doc:
                if (TextUtils.isEmpty(User.getInstance().getUserId())) {
                    LoginActivity.startActivity(getContext());
                } else {
//                    PersonalActivity.startActivity(getActivity(), path);
                    presenter.getPersonalInfo(getContext());


                }
                break;
        }
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
