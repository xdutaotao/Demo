package com.seafire.cworker.Activity;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.seafire.cworker.Fragment.BaseFragmentAdapter;
import com.seafire.cworker.Fragment.FirstFragment;
import com.seafire.cworker.R;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

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
        FirstFragment rewardFragment = FirstFragment.newInstance(R.drawable.launcher_one, false);
        FirstFragment rewardFragment1 = FirstFragment.newInstance(R.drawable.launcher_two, false);
        FirstFragment rewardFragment2 = FirstFragment.newInstance(R.drawable.launcher_three, false);
        FirstFragment rewardFragment3 = FirstFragment.newInstance(R.drawable.launcher_four, false);
        FirstFragment rewardFragment4 = FirstFragment.newInstance(R.drawable.launcher_five, true);
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
        AndPermission.with(this)
                .requestCode(REQUEST_CODE_PERMISSION_OTHER)
                .permission(
                        Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.MODIFY_PHONE_STATE,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .callback(this)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                        AndPermission.rationaleDialog(LaunchActivity.this, rationale)
                                .show();
                    }
                })
                .start();
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

    @PermissionYes(REQUEST_CODE_PERMISSION_OTHER)
    private void getMultiYes(@NonNull List<String> grantedPermissions) {
        Toast.makeText(this, "获取权限成功", Toast.LENGTH_SHORT).show();
    }

    @PermissionNo(REQUEST_CODE_PERMISSION_OTHER)
    private void getMultiNo(@NonNull List<String> deniedPermissions) {
        Toast.makeText(this, "获取权限失败", Toast.LENGTH_SHORT).show();

        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
                    .setTitle("友情提示")
                    .setMessage("你拒绝了我们的权限申请")
                    .setPositiveButton("确定")
                    .setNegativeButton("取消", null)
                    .show();

            // 更多自定dialog，请看上面。
        }
    }
}
