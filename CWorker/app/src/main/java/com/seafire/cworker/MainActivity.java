package com.seafire.cworker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.seafire.cworker.Activity.BaseActivity;
import com.seafire.cworker.Activity.CollectActivity;
import com.seafire.cworker.Activity.LoginActivity;
import com.seafire.cworker.Bean.RxBusEvent;
import com.seafire.cworker.Common.Constants;
import com.seafire.cworker.Fragment.AddFragment;
import com.seafire.cworker.Fragment.HomeFragment;
import com.seafire.cworker.Fragment.MessageFragment;
import com.seafire.cworker.Fragment.MyFragment;
import com.seafire.cworker.Fragment.SearchFragment;
import com.seafire.cworker.Model.User;
import com.seafire.cworker.Utils.RxBus;
import com.seafire.cworker.Utils.ToastUtil;
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

        BadgeItem badgeItem = new BadgeItem();

        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.setBarBackgroundColor(R.color.activity_background);
        bottomNavigationBar.setInActiveColor(R.color.nav_gray);
        bottomNavigationBar.setActiveColor(R.color.colorPrimary);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.home, strings[0]))
                .addItem(new BottomNavigationItem(R.drawable.search, strings[1]))
                .addItem(new BottomNavigationItem(R.drawable.empty, strings[2]))
                .addItem(new BottomNavigationItem(R.drawable.message, strings[3]))
                .addItem(new BottomNavigationItem(R.drawable.my, strings[4])
                        .setBadgeItem(badgeItem.setBackgroundColor(Color.RED).hide().setText("0")));

        RxBus.getInstance().toObservable(RxBusEvent.class)
                .filter(s -> TextUtils.equals(s.getType(), Constants.POST_COLLECT_TIME))
                .subscribe(s -> {
                    if (TextUtils.equals("0", s.getMsg())){
                        badgeItem.hide();
                    }else{
                        badgeItem.show().setText(s.getMsg());
                    }
                });

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
        fragments.add(SearchFragment.newInstance("搜索"));
        fragments.add(AddFragment.newInstance(""));
        fragments.add(MessageFragment.newInstance("消息"));
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