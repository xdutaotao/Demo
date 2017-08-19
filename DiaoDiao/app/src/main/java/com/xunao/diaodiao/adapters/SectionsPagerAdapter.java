package com.xunao.diaodiao.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xunao.diaodiao.Fragment.BaseFragment;
import com.xunao.diaodiao.Fragment.BaseTabFragment;
import com.xunao.diaodiao.Fragment.TabFragment;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/8/19.
 */

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
        return "";
    }
}
