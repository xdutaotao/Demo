package com.xunao.diaodiao;

import android.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.umeng.analytics.MobclickAgent;
import com.xunao.diaodiao.Activity.AboutActivity;
import com.xunao.diaodiao.Activity.BaseActivity;
import com.xunao.diaodiao.Activity.CollectActivity;
import com.xunao.diaodiao.Activity.LoginActivity;
import com.xunao.diaodiao.Activity.WebViewActivity;
import com.xunao.diaodiao.Activity.WebViewDetailActivity;
import com.xunao.diaodiao.Fragment.HomeFragment;
import com.xunao.diaodiao.Fragment.ProjectFragment;
import com.xunao.diaodiao.Fragment.MyFragment;
import com.xunao.diaodiao.Fragment.ReleaseFragment;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Utils.PermissionsUtils;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.gzfgeh.iosdialog.IOSDialog;
import com.xunao.diaodiao.Utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {
    @BindView(R.id.container)
    LinearLayout container;
    private ArrayList<Fragment> fragments;
    private BottomNavigationBar bottomNavigationBar;
    private String[] strings = {"首页","发布","订单","我的"};

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(INTENT_KEY, url);
        context.startActivity(intent);
    }

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
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.home_fill, strings[0]).setInactiveIconResource(R.drawable.home))
                .addItem(new BottomNavigationItem(R.drawable.fabu_fill, strings[1]).setInactiveIconResource(R.drawable.fabu))
                .addItem(new BottomNavigationItem(R.drawable.dindan_fill, strings[2]).setInactiveIconResource(R.drawable.dindan))
                .addItem(new BottomNavigationItem(R.drawable.wode_fill, strings[3]).setInactiveIconResource(R.drawable.wode));

        fragments = getFragments();
        bottomNavigationBar.setFirstSelectedPosition(0).initialise();
        setDefaultFragment();
        bottomNavigationBar.setTabSelectedListener(this);

        if (!TextUtils.isEmpty(getIntent().getStringExtra(INTENT_KEY))){
            WebViewActivity.startActivity(this, getIntent().getStringExtra(INTENT_KEY));
        }

        showContacts();

        RxBus.getInstance().toObservable(String.class)
                .filter(s -> TextUtils.equals(s, "release"))
                .subscribe(s -> {
                    goToFragment(0);
                });

        /**
         * 通知栏点击 传递消息
         */
        if (getIntent()!= null && getIntent().getStringExtra(Utils.MAIN_INTENT_KEY) != null){
            WebViewDetailActivity.startActivity(this, getIntent().getStringExtra(Utils.MAIN_INTENT_KEY));
        }
    }

    private static final int BAIDU_READ_PHONE_STATE =100;

    public void showContacts(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"没有权限,请手动开启定位权限",Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.READ_PHONE_STATE
            }, BAIDU_READ_PHONE_STATE);
        }else{

        }
    }


    //Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）

                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
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
        fragments.add(ReleaseFragment.newInstance("发布"));
        fragments.add(ProjectFragment.newInstance("订单"));
        fragments.add(MyFragment.newInstance("我的"));
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


    public void goToFragment(int index){
        if (index == 1)
            bottomNavigationBar.selectTab(0);
        else
            bottomNavigationBar.selectTab(1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragments.get(bottomNavigationBar.getCurrentSelectedPosition()).onActivityResult(requestCode, resultCode, data);
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
            MobclickAgent.onKillProcess(this);
            finish();
            System.exit(0);
        }
    }
}
