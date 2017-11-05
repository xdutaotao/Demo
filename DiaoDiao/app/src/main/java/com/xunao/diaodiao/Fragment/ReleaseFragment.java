package com.xunao.diaodiao.Fragment;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzfgeh.iosdialog.IOSDialog;
import com.xunao.diaodiao.Activity.EditCompanyActivity;
import com.xunao.diaodiao.Activity.EditPersonalActivity;
import com.xunao.diaodiao.Activity.EditSkillActivity;
import com.xunao.diaodiao.Activity.LoginActivity;
import com.xunao.diaodiao.Activity.ReleaseCompanyActivity;
import com.xunao.diaodiao.Activity.ReleaseHelpActivity;
import com.xunao.diaodiao.Activity.ReleaseHelpInfoActivity;
import com.xunao.diaodiao.Activity.ReleaseProjActivity;
import com.xunao.diaodiao.Activity.ReleaseSkillActivity;
import com.xunao.diaodiao.Activity.SelectMemoryActivity;
import com.xunao.diaodiao.Bean.CheckFinishRes;
import com.xunao.diaodiao.Bean.PersonalRes;
import com.xunao.diaodiao.MainActivity;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.SearchPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.SearchView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.STATUS;
import static com.xunao.diaodiao.Common.Constants.TYPE_KEY;

/**
 * create by
 */
public class ReleaseFragment extends BaseFragment implements View.OnClickListener, SearchView {
    private static final String ARG_PARAM1 = "param1";
    @Inject
    SearchPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.company_release)
    LinearLayout companyRelease;
    @BindView(R.id.skill_release)
    LinearLayout skillRelease;
    @BindView(R.id.home_release)
    LinearLayout homeRelease;
    @BindView(R.id.release_proj_info)
    LinearLayout releaseProjInfo;
    @BindView(R.id.release_skill_info)
    LinearLayout releaseSkillInfo;
    @BindView(R.id.release_help_info)
    LinearLayout releaseHelpInfo;
    @BindView(R.id.release_find_people)
    LinearLayout releaseFindPeople;
    @BindView(R.id.skill_release_skill_info)
    LinearLayout skillReleaseSkillInfo;
    @BindView(R.id.skill_release_weibao_info)
    LinearLayout skillReleaseWeibaoInfo;
    @BindView(R.id.skill_release_help_info)
    LinearLayout skillReleaseHelpInfo;
    @BindView(R.id.custom_release_weibao_info)
    LinearLayout customReleaseWeibaoInfo;

    private String mParam1;
    private int index = 0;
    private int selectIndex = 0;
    private int type;

    private int status;

    private PersonalRes.CompanyInfo companyInfo;
    private PersonalRes.TechnicianInfo technicianInfo;
    private PersonalRes.FamilyInfo familyInfo;

    public static ReleaseFragment newInstance(String param1) {
        ReleaseFragment fragment = new ReleaseFragment();
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
        View view = inflater.inflate(R.layout.fragment_release, container, false);
        ButterKnife.bind(this, view);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        hideToolbarBack(toolBar, titleText, "发布");


        releaseProjInfo.setOnClickListener(this);
        releaseFindPeople.setOnClickListener(this);
        releaseHelpInfo.setOnClickListener(this);
        releaseSkillInfo.setOnClickListener(this);
        skillReleaseHelpInfo.setOnClickListener(this);
        skillReleaseSkillInfo.setOnClickListener(this);
        skillReleaseWeibaoInfo.setOnClickListener(this);
        customReleaseWeibaoInfo.setOnClickListener(this);


        presenter.checkFinish();
        presenter.getPersonalInfo(ReleaseFragment.this.getContext());

        type = ShareUtils.getValue(TYPE_KEY, 0);
        if (TextUtils.isEmpty(User.getInstance().getUserId())) {
            LoginActivity.startActivity(ReleaseFragment.this.getContext());
            ((MainActivity) getActivity()).goToFragment(1);

        } else if (type == 0) {

            SelectMemoryActivity.startActivity(ReleaseFragment.this.getContext());
            ((MainActivity) getActivity()).goToFragment(1);
        } else if (type == 1) {
            companyRelease.setVisibility(View.VISIBLE);
            skillRelease.setVisibility(View.GONE);
            homeRelease.setVisibility(View.GONE);
        } else if (type == 2) {
            companyRelease.setVisibility(View.GONE);
            skillRelease.setVisibility(View.VISIBLE);
            homeRelease.setVisibility(View.GONE);
        } else {
            companyRelease.setVisibility(View.GONE);
            skillRelease.setVisibility(View.GONE);
            homeRelease.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();



    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {

            type = ShareUtils.getValue(TYPE_KEY, 0);
            if (TextUtils.isEmpty(User.getInstance().getUserId())) {
                index++;
                if (index == 1) {
                    LoginActivity.startActivity(ReleaseFragment.this.getContext());
                    ((MainActivity) getActivity()).goToFragment(1);
                }
            } else if (type == 0) {

                SelectMemoryActivity.startActivity(ReleaseFragment.this.getContext());
                ((MainActivity) getActivity()).goToFragment(1);
                return;
            } else if (type == 1) {
                companyRelease.setVisibility(View.VISIBLE);
                skillRelease.setVisibility(View.GONE);
                homeRelease.setVisibility(View.GONE);
            } else if (type == 2) {
                companyRelease.setVisibility(View.GONE);
                skillRelease.setVisibility(View.VISIBLE);
                homeRelease.setVisibility(View.GONE);
            } else {
                companyRelease.setVisibility(View.GONE);
                skillRelease.setVisibility(View.GONE);
                homeRelease.setVisibility(View.VISIBLE);
            }

            if (TextUtils.isEmpty(User.getInstance().getUserId())) {
                LoginActivity.startActivity(ReleaseFragment.this.getContext());
                ((MainActivity) getActivity()).goToFragment(1);
            }else{
                presenter.checkFinish();
                presenter.getPersonalInfo(ReleaseFragment.this.getContext());
            }

        }

    }

    @Override
    public void onClick(View v) {
        if (status != 1){
            if (status == 4){
                SpannableStringBuilder string = new SpannableStringBuilder("请先完善资料后才能 发布/接单");
                ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.colorAccent));
                string.setSpan(span, 10, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                new IOSDialog(getContext()).builder()
                        .setMsg(string)
                        .setNegativeButton("稍后再说", null)
                        .setNegativeBtnColor(R.color.nav_gray)
                        .setPositiveBtnColor(R.color.black)
                        .setPositiveButton("去完善", v1 -> {
                            if (type == 1){
                                if (companyInfo == null){
                                    EditCompanyActivity.startActivity(getContext());
                                }else{
                                    EditCompanyActivity.startActivity(getContext(), companyInfo);
                                }

                            }else if (type == 2){
                                if (technicianInfo == null){
                                    EditSkillActivity.startActivity(getContext());
                                }else{
                                    EditSkillActivity.startActivity(getContext(), technicianInfo);
                                }

                            }else{
                                if (familyInfo == null){
                                    EditPersonalActivity.startActivity(getContext());
                                }else{
                                    EditPersonalActivity.startActivity(getContext(), familyInfo);
                                }

                            }
                        })
                        .show();
            }else{
                SpannableStringBuilder string = new SpannableStringBuilder("审核通过后才能 发布/接单");
                ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.colorAccent));
                string.setSpan(span, 8, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                new IOSDialog(getContext()).builder()
                        .setMsg(string)
                        .setNegativeButton("稍后再说", null)
                        .setNegativeBtnColor(R.color.nav_gray)
                        .setPositiveBtnColor(R.color.black)
                        .setPositiveButton("去完善", v1 -> {
                            if (type == 1){
                                if (companyInfo == null){
                                    EditCompanyActivity.startActivity(getContext());
                                }else{
                                    EditCompanyActivity.startActivity(getContext(), companyInfo);
                                }

                            }else if (type == 2){
                                if (technicianInfo == null){
                                    EditSkillActivity.startActivity(getContext());
                                }else{
                                    EditSkillActivity.startActivity(getContext(), technicianInfo);
                                }

                            }else{
                                if (familyInfo == null){
                                    EditPersonalActivity.startActivity(getContext());
                                }else{
                                    EditPersonalActivity.startActivity(getContext(), familyInfo);
                                }

                            }
                        })
                        .show();
            }

            return;
        }

        switch (v.getId()) {
            case R.id.release_proj_info:
                ReleaseProjActivity.startActivity(getContext(), false);
                break;

            case R.id.release_find_people:
                //
                ReleaseProjActivity.startActivity(getContext(), true);
                break;

            case R.id.release_help_info:
                ToastUtil.show("敬请期待");
                //
                //ReleaseHelpInfoActivity.startActivity(getContext());
                break;

            case R.id.release_skill_info:
                ToastUtil.show("敬请期待");
                //ReleaseHelpActivity.startActivity(getContext());
                break;

            case R.id.skill_release_skill_info:
                ReleaseSkillActivity.startActivity(getContext());
                break;

            case R.id.skill_release_help_info:
                ToastUtil.show("敬请期待");
                //ReleaseHelpInfoActivity.startActivity(getContext());
                break;

            case R.id.skill_release_weibao_info:
                ToastUtil.show("敬请期待");
                //ReleaseHelpActivity.startActivity(getContext());
                break;

            case R.id.custom_release_weibao_info:
                ToastUtil.show("敬请期待");
                //ReleaseHelpActivity.startActivity(getContext());
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void getData(CheckFinishRes bean) {
        ShareUtils.putValue(STATUS, bean.getStatus());
        status = bean.getStatus();
    }

    @Override
    public void getPersonalData(PersonalRes result) {
        companyInfo = result.getCompanyInfo();
        technicianInfo = result.getTechnicianInfo();
        familyInfo = result.getFamilyInfo();
    }
}
