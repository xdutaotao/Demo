package com.xunao.diaodiao.Fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Activity.LoginActivity;
import com.xunao.diaodiao.Activity.ReleaseCompanyActivity;
import com.xunao.diaodiao.Activity.ReleaseSkillActivity;
import com.xunao.diaodiao.Activity.SelectMemoryActivity;
import com.xunao.diaodiao.Bean.SearchResponseBean;
import com.xunao.diaodiao.MainActivity;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.SearchPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.View.SearchView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.TYPE_KEY;

/**
 * create by
 */
public class ReleaseFragment extends BaseFragment implements View.OnClickListener {
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

    private String mParam1;
    private int index = 0;
    private int selectIndex = 0;

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
        //presenter.attachView(this);

        hideToolbarBack(toolBar, titleText, "发布");


        companyRelease.setOnClickListener(this);
        skillRelease.setOnClickListener(this);
        homeRelease.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        int type = ShareUtils.getValue(TYPE_KEY, 0);
        if (TextUtils.isEmpty(User.getInstance().getUserId())){
            index++;
            if (index == 1) {
                LoginActivity.startActivity(ReleaseFragment.this.getContext());
                ((MainActivity) getActivity()).goToFragment(1);
            }
        }else if(type == 0){
            selectIndex++;
            if (selectIndex == 1) {
                SelectMemoryActivity.startActivity(ReleaseFragment.this.getContext());
                ((MainActivity) getActivity()).goToFragment(1);
            }
        }else if(type == 1){
            companyRelease.setVisibility(View.VISIBLE);
            skillRelease.setVisibility(View.GONE);
            homeRelease.setVisibility(View.GONE);
        }else if(type == 2){
            companyRelease.setVisibility(View.GONE);
            skillRelease.setVisibility(View.VISIBLE);
            homeRelease.setVisibility(View.GONE);
        }else{
            companyRelease.setVisibility(View.GONE);
            skillRelease.setVisibility(View.GONE);
            homeRelease.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden){
            if (TextUtils.isEmpty(User.getInstance().getUserId())) {
                LoginActivity.startActivity(ReleaseFragment.this.getContext());
                ((MainActivity) getActivity()).goToFragment(1);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.company_release:
                ReleaseCompanyActivity.startActivity(getContext());
                break;

            case R.id.skill_release:
                ReleaseSkillActivity.startActivity(getContext());
                break;

            case R.id.home_release:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

}
