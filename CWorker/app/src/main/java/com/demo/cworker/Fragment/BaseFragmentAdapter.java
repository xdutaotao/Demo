package com.demo.cworker.Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by guzhenfu on 15/9/22.
 */
public class BaseFragmentAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> list;

    public BaseFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public BaseFragmentAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int i) {
        return list.get(i);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
