package com.demo.cworker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.demo.cworker.Activity.BaseActivity;
import com.demo.cworker.Activity.CollectActivity;
import com.demo.cworker.Activity.LoginActivity;
import com.demo.cworker.Fragment.AddFragment;
import com.demo.cworker.Fragment.HomeFragment;
import com.demo.cworker.Fragment.MessageFragment;
import com.demo.cworker.Fragment.MyFragment;
import com.demo.cworker.Fragment.SearchFragment;
import com.demo.cworker.Model.User;
import com.gzfgeh.iosdialog.IOSDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {
    @BindView(R.id.container)
    LinearLayout container;
    private ArrayList<Fragment> fragments;
    private ImageView imageView;
    private BottomNavigationBar bottomNavigationBar;
    private String[] strings = {"主页","搜索"," ","消息","我的"};

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

        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.setBarBackgroundColor(R.color.activity_background);
        bottomNavigationBar.setInActiveColor(R.color.nav_gray);
        bottomNavigationBar.setActiveColor(R.color.colorPrimary);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_home_black_24dp, strings[0]))
                .addItem(new BottomNavigationItem(R.drawable.ic_home_black_24dp, strings[1]))
                .addItem(new BottomNavigationItem(R.drawable.empty, strings[2]))
                .addItem(new BottomNavigationItem(R.drawable.ic_home_black_24dp, strings[3]))
                .addItem(new BottomNavigationItem(R.drawable.ic_home_black_24dp, strings[4])
                    .setBadgeItem(new BadgeItem().setBackgroundColor(Color.RED).setText("9")));

        fragments = getFragments();
        bottomNavigationBar.setFirstSelectedPosition(0).initialise();
        setDefaultFragment();
        bottomNavigationBar.setTabSelectedListener(this);

        imageView = (ImageView) findViewById(R.id.center_image);
        imageView.setOnClickListener(v -> {
            showCenterFragment();
        });
    }

    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.content, getFragments().get(0));
        transaction.commit();
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

    @SuppressWarnings("RestrictedApi")
    private void showCenterFragment() {
        if (User.getInstance().getUserInfo() == null){
            new IOSDialog(this).builder()
                    .setTitle("提示")
                    .setMsg("登陆后才可使用此功能")
                    .setPositiveButton("立即登陆", v -> LoginActivity.startActivity(this))
                    .setPositiveBtnColor(R.color.colorPrimary)
                    .setNegativeButton("暂不登陆", null)
                    .setNegativeBtnColor(R.color.colorPrimary)
                    .show();
        }else{
            CollectActivity.startActivity(this);
        }
    }




    @Override
    public void onTabSelected(int position) {
        if (position == 2){
            imageView.setImageResource(R.drawable.icon_512);
            showCenterFragment();
            return;
        }
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
                if (position == 2){
                    imageView.setImageResource(R.drawable.icon_512);
                }
            }
        }
    }

    @Override
    public void onTabReselected(int position) {

    }
}
