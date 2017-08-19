package com.demo.step;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.demo.step.Activity.BaseActivity;
import com.demo.step.Fragment.TabFragment;
import com.demo.step.Utils.ToastUtil;
import com.gzfgeh.iosdialog.IOSDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    private ArrayList<Fragment> fragments;
    private ImageView imageView;
    private BottomNavigationBar bottomNavigationBar;
    private String[] strings = {"主页","搜索","消息","我的"};
    private int failTime = 0;
    private BadgeItem badgeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //禁止侧滑返回
        setSwipeBackEnable(false);

        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar_container);
        bottomNavigationBar.setAutoHideEnabled(true);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);

        badgeItem = new BadgeItem();

        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.setBarBackgroundColor(R.color.activity_background);
        bottomNavigationBar.setInActiveColor(R.color.nav_gray);
        bottomNavigationBar.setActiveColor(R.color.colorPrimary);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.home, strings[0]))
                .addItem(new BottomNavigationItem(R.drawable.search, strings[1]))
                .addItem(new BottomNavigationItem(R.drawable.message, strings[2]))
                .addItem(new BottomNavigationItem(R.drawable.my, strings[3]));

        fragments = getFragments();
        bottomNavigationBar.setFirstSelectedPosition(0).initialise();
        setDefaultFragment();
        bottomNavigationBar.setTabSelectedListener(this);

    }



    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.content, getFragments().get(0));
        transaction.commit();
    }

    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(TabFragment.newInstance("http://www.baidu.com"));
        fragments.add(TabFragment.newInstance("http://www.baidu.com"));
        fragments.add(TabFragment.newInstance("http://www.baidu.com"));
        fragments.add(TabFragment.newInstance("http://www.baidu.com"));
        return fragments;
    }




    @Override
    public void onTabSelected(int position) {
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);
                //解决快速点击 bug
                if (fragment.isVisible())
                    return;
                if (fragment.isHidden()) {
                    ft.show(fragment);
                } else {
                    ft.add(R.id.content, fragment);
                }
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabUnselected(int position) {
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);
                ft.hide(fragment);
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabReselected(int position) {

    }

    /**
     * 双击返回键退出
     **/
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            ToastUtil.show("再按一次退出");
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
}
