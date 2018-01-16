package com.xunao.diaodiao.Activity;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xunao.diaodiao.Fragment.BaseFragmentAdapter;
import com.xunao.diaodiao.Fragment.FirstFragment;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.PermissionsUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class LaunchActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {
    private static final int REQUEST_CODE_PERMISSION_OTHER = 101;
    private static final int REQUEST_CODE_SETTING = 300;

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
            params.setMargins(10,0,10,50);
            viewGroup.addView(ivIcon, params);
        }

        viewPager = (ViewPager) findViewById(R.id.vp_launcher);
        mFragmentList = new ArrayList<>();
        FirstFragment rewardFragment = FirstFragment.newInstance(R.drawable.loading, false);
        FirstFragment rewardFragment1 = FirstFragment.newInstance(R.drawable.loading, false);
        FirstFragment rewardFragment2 = FirstFragment.newInstance(R.drawable.loading, false);
        FirstFragment rewardFragment3 = FirstFragment.newInstance(R.drawable.loading, false);
        FirstFragment rewardFragment4 = FirstFragment.newInstance(R.drawable.loading, true);
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

        getPermission();
    }

    private void getPermission(){
        PermissionsUtils.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        PermissionsUtils.hasPermission(this, Manifest.permission.WRITE_SETTINGS);


        PermissionsUtils.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        PermissionsUtils.hasPermission(this, Manifest.permission.CAMERA);

        PermissionsUtils.hasPermission(this, Manifest.permission.SEND_SMS);

        PermissionsUtils.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (Utils.isActiveTime()){
            finish();
            System.exit(0);
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults != null && grantResults.length > 0) {
            ToastUtil.show("权限申请成功");
        } else {
            PermissionsUtils.permissionNotice(this, requestCode);
        }
    }

}
