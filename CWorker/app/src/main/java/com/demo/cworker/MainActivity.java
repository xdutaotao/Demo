package com.demo.cworker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.demo.cworker.Activity.BaseActivity;
import com.demo.cworker.Fragment.AddFragment;
import com.demo.cworker.Fragment.BaseFragment;
import com.demo.cworker.Fragment.HomeFragment;
import com.demo.cworker.Fragment.MessageFragment;
import com.demo.cworker.Fragment.MyFragment;
import com.demo.cworker.Fragment.SearchFragment;
import com.demo.cworker.Widget.BottomNavigationViewEx;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @BindView(R.id.container)
    LinearLayout container;
    private ArrayList<Fragment> fragments;
    private Fragment preFragment;
    private ImageView imageView;
    private BottomNavigationViewEx navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //禁止侧滑返回
        setSwipeBackEnable(false);

        navigation = (BottomNavigationViewEx) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        navigation.addBadgeViewAt(this, container, 4, "9");

        fragments = getFragments();
        showFragment(0);

        imageView = (ImageView) findViewById(R.id.center_image);
        imageView.setOnClickListener(v -> {
            showFragment(2);
        });
    }

    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(HomeFragment.newInstance("主页"));
        fragments.add(SearchFragment.newInstance("我的"));
        fragments.add(AddFragment.newInstance("我的"));
        fragments.add(MessageFragment.newInstance("我的"));
        fragments.add(MyFragment.newInstance("我的"));
        return fragments;
    }

    private void showFragment(int position) {
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);
                if (((BaseFragment)fragment).isVisible)
                    return;
                if (fragment.isAdded()) {
                    ft.hide(preFragment);
                    ft.show(fragment);
                } else {
                    ft.add(R.id.content, fragment);
                }
                ft.commitAllowingStateLoss();
                preFragment = fragment;
                navigation.getBottomNavigationItemView(position).setChecked(true);
                if (position == 2){
                    setNavigationItemUnChecked();
                }
            }
        }
    }

    private void setNavigationItemUnChecked(){
        navigation.getBottomNavigationItemView(0).setChecked(false);
        navigation.getBottomNavigationItemView(1).setChecked(false);
        navigation.getBottomNavigationItemView(2).setChecked(false);
        navigation.getBottomNavigationItemView(3).setChecked(false);
        navigation.getBottomNavigationItemView(4).setChecked(false);
    }

    private BottomNavigationViewEx.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationViewEx.OnNavigationItemSelectedListener() {

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
