package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xunao.diaodiao.Fragment.BaseFragment;
import com.xunao.diaodiao.Fragment.MyComplaintRecordFragment;
import com.xunao.diaodiao.Fragment.TabFragment;
import com.xunao.diaodiao.Present.ComplaintRecordPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.ComplaintRecordView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class ComplaintRecordActivity extends BaseActivity implements ComplaintRecordView {

    @Inject
    ComplaintRecordPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private List<BaseFragment> fragments;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ComplaintRecordActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_record);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "申诉记录");

        fragments=new ArrayList<>();
        fragments.add(MyComplaintRecordFragment.newInstance("我的申诉"));
        fragments.add(MyComplaintRecordFragment.newInstance("对方申诉"));
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(mSectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        tab.setupWithViewPager(viewPager);

        LinearLayout linearLayout = (LinearLayout) tab.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
                R.drawable.layout_divider_vertical));
        linearLayout.setDividerPadding(20);
    }


    @Override
    public void onFailure() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private List<BaseFragment> mFragmentList;

        public SectionsPagerAdapter(FragmentManager fm, List<BaseFragment> fragmentList) {
            super(fm);
            this.mFragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return  mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "我的申诉";
                case 1:
                    return "对方申诉";
            }
            return null;
        }
    }

}
