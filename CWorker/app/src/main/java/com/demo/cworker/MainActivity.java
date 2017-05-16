package com.demo.cworker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.ImageView;

import com.demo.cworker.Activity.BaseActivity;
import com.demo.cworker.Fragment.HomeFragment;
import com.demo.cworker.Fragment.MyFragment;
import com.demo.cworker.Widget.BottomNavigationViewHelper;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private ArrayList<Fragment> fragments;
    private Fragment preFragment;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //禁止侧滑返回
        setSwipeBackEnable(false);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(0).setChecked(true);
        BottomNavigationViewHelper.disableShiftMode(navigation);

        fragments = getFragments();
        setDefaultFragment(0);

        imageView = (ImageView) findViewById(R.id.center_image);
        imageView.setOnClickListener(v -> showFragment(2));
    }

    /**
     * 设置默认的
     */
    private void setDefaultFragment(int position) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.content, getFragments().get(position));
        transaction.commit();
    }

    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(HomeFragment.newInstance("主页"));
        fragments.add(HomeFragment.newInstance("我的"));
        fragments.add(HomeFragment.newInstance("我的"));
        fragments.add(MyFragment.newInstance("我的"));
        fragments.add(MyFragment.newInstance("我的"));
        return fragments;
    }

    private void showFragment(int position){
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);
                if (fragment.isAdded()) {
                    ft.hide(preFragment);
                    ft.show(fragment);
                } else {
                    ft.add(R.id.content, fragment);
                }
                ft.commitAllowingStateLoss();
                preFragment = fragment;
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showFragment(0);
                    return true;
                case R.id.search:
                    showFragment(1);
                    return true;
                case R.id.add:
                    showFragment(2);
                    return true;
                case R.id.message:
                    showFragment(3);
                    return true;
                case R.id.my:
                    showFragment(4);
                    return true;
            }
            return false;
        }

    };


}
