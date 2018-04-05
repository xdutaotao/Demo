package com.luck.number;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.luck.number.activity.BaseActivity;
import com.luck.number.fragment.BaseFragment;
import com.luck.number.fragment.HomeFragment;
import com.luck.number.fragment.LotteryFragment;
import com.luck.number.tools.ToastUtil;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private BaseFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSwipeBackEnable(false);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        currentFragment = BaseFragment.newInstance();
        setDefaultFragment();
    }

    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.layFrame,  currentFragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            exit();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(currentFragment);
        BaseFragment fragment = null;

        if (id == R.id.nav_home) {
            toolbar.setTitle(R.string.nav_home);
            fragment = HomeFragment.newInstance();

        } else if (id == R.id.nav_lottery) {
            toolbar.setTitle(R.string.nav_lottery);
            fragment = LotteryFragment.newInstance();

        } else if (id == R.id.nav_trend) {
            toolbar.setTitle(R.string.nav_trend);
        } else if (id == R.id.nav_my) {
            toolbar.setTitle(R.string.nav_my);
        }

        if(fragment != null){
            if (fragment.isHidden()) {
                ft.show(fragment);
            } else {
                ft.add(R.id.layFrame, fragment);
            }
            ft.commitAllowingStateLoss();
            currentFragment = fragment;
        }




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 双击返回键退出
     **/
    private long mExitTime;

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
