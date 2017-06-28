package com.demo.cworker.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.demo.cworker.Fragment.BaseFragmentAdapter;
import com.demo.cworker.Fragment.FirstFragment;
import com.demo.cworker.R;

import java.util.ArrayList;
import java.util.List;

public class LaunchActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    private ImageView[] mIcons;
    private ViewPager viewPager;
    private List<Fragment> mFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.ll_view);
        mIcons = new ImageView[5];
        for (int i=0; i<mIcons.length; i++){
            ImageView ivIcon = new ImageView(this);
            ivIcon.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
            ivIcon.setBackgroundResource(
                    i == 0 ? R.drawable.page_indicator_focused : R.drawable.page_indicator_unfocused);
            mIcons[i] = ivIcon;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(15,15));
            params.setMargins(30,0,30,120);
            viewGroup.addView(ivIcon, params);
        }

        viewPager = (ViewPager) findViewById(R.id.vp_launcher);
        mFragmentList = new ArrayList<>();
        FirstFragment rewardFragment = FirstFragment.newInstance(R.drawable.launcher_one, false);
        FirstFragment rewardFragment1 = FirstFragment.newInstance(R.drawable.launcher_two, false);
        FirstFragment rewardFragment2 = FirstFragment.newInstance(R.drawable.launcher_three, false);
        FirstFragment rewardFragment3 = FirstFragment.newInstance(R.drawable.launcher_four, false);
        FirstFragment rewardFragment4 = FirstFragment.newInstance(R.drawable.launcher_five, true);
        mFragmentList.add(rewardFragment);
        mFragmentList.add(rewardFragment1);
        mFragmentList.add(rewardFragment2);
        mFragmentList.add(rewardFragment3);
        mFragmentList.add(rewardFragment4);

        BaseFragmentAdapter mAdapter = new BaseFragmentAdapter(getSupportFragmentManager(),mFragmentList);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {


    }

    @Override
    public void onPageSelected(int i) {
        for(int j=0; j<mFragmentList.size(); j++){
            mIcons[j].setBackgroundResource(j == i ? R.drawable.page_indicator_focused : R.drawable.page_indicator_unfocused);
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {


    }
}
