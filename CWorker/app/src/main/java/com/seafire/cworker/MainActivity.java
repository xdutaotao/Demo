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
import com.seafire.cworker.Bean.CollectBean;
import com.seafire.cworker.Bean.RxBusEvent;
import com.seafire.cworker.Common.Constants;
import com.seafire.cworker.Common.RetrofitConfig;
import com.seafire.cworker.Fragment.AddFragment;
import com.seafire.cworker.Fragment.HomeFragment;
import com.seafire.cworker.Fragment.MessageFragment;
import com.seafire.cworker.Fragment.MyFragment;
import com.seafire.cworker.Fragment.SearchFragment;
import com.seafire.cworker.Model.User;
import com.seafire.cworker.Utils.BadgeUtil;
import com.seafire.cworker.Utils.JsonUtils;
import com.seafire.cworker.Utils.LocationUtils;
import com.seafire.cworker.Utils.RxBus;
import com.seafire.cworker.Utils.RxUtils;
import com.seafire.cworker.Utils.ShareUtils;
import com.seafire.cworker.Utils.ToastUtil;
import com.gzfgeh.iosdialog.IOSDialog;
import com.seafire.cworker.Utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;

import static com.seafire.cworker.Common.Constants.COLLECT_LIST;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {
    @BindView(R.id.container)
    LinearLayout container;
    private ArrayList<Fragment> fragments;
    private ImageView imageView;
    private BottomNavigationBar bottomNavigationBar;
    private String[] strings = {"主页","搜索"," ","消息","我的"};
    private int failTime = 0;
    private BadgeItem badgeItem;
    private BadgeItem msgBadgeItem;

    private boolean post = false;

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
        msgBadgeItem = new BadgeItem();

        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.setBarBackgroundColor(R.color.activity_background);
        bottomNavigationBar.setInActiveColor(R.color.nav_gray);
        bottomNavigationBar.setActiveColor(R.color.colorPrimary);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.home, strings[0]))
                .addItem(new BottomNavigationItem(R.drawable.home_search, strings[1]))
                .addItem(new BottomNavigationItem(R.drawable.empty, strings[2]))
                .addItem(new BottomNavigationItem(R.drawable.message, strings[3])
                        .setBadgeItem(msgBadgeItem.setBackgroundColor(Color.RED)))
                .addItem(new BottomNavigationItem(R.drawable.my, strings[4])
                        .setBadgeItem(badgeItem.setBackgroundColor(Color.RED)));

        fragments = getFragments();
        bottomNavigationBar.setFirstSelectedPosition(0).initialise();
        setDefaultFragment();
        bottomNavigationBar.setTabSelectedListener(this);

        imageView = (ImageView) findViewById(R.id.center_image);
        imageView.setOnClickListener(v -> {
            showCenterFragment();
        });

        //VIP 到期 重新登录
        if (User.getInstance().getUserInfo() != null){
            if (User.getInstance().getUserInfo().getPerson().getVIP() == 2){
                //超级管理员
                postAdd();
            }else{
                if (User.getInstance().getUserInfo().getProject() == null){

                }else{
                    postAdd();
                }

            }
        }
    }

    private void postAdd(){
        if (!post){
            new LocationUtils().getLocationAddr(this);
            RxBus.getInstance().toObservable(String.class)
                    .compose(RxUtils.applyIOToMainThreadSchedulers())
                    .subscribe(s -> {
                        String[] data = s.split("#");
                        if (data.length> 1 && !TextUtils.isEmpty(data[1]) && !TextUtils.isEmpty(data[2])) {
                            String latData = data[1];
                            String lngData = data[2];
                            if (!post){
                                Map<String ,String> map = new HashMap<>();
                                map.put("token", User.getInstance().getUserId());
                                map.put("longitude", lngData);
                                map.put("latitude", latData);
                                post = true;


                                Observable.create(new Observable.OnSubscribe<Long>() {
                                    @Override
                                    public void call(Subscriber<? super Long> subscriber) {
                                        if (User.getInstance().getUserInfo().getPerson().getVIP() != 2)
                                            subscriber.onNext(Utils.postAddr(lngData, latData, User.getInstance().getUserInfo().getProject().getAddress()));
                                            subscriber.onCompleted();
                                    }
                                }).compose(RxUtils.applyIOToMainThreadSchedulers())
                                 .subscribe(aLong -> {
                                     ToastUtil.show(aLong+"");
                                     if (aLong > 1000){
                                         new IOSDialog(MainActivity.this).builder()
                                                 .setTitle("退出APP")
                                                 .setMsg("项目超出距离")
                                                 .setNegativeButton("确定", v -> {
                                                     System.exit(0);
                                                 })
                                                 .setPositiveButton("取消", v -> {
                                                     System.exit(0);
                                                 })
                                                 .show();
                                     }
                                 });

                                RetrofitConfig.getInstance().getRetrofitService()
                                        .postAddr(map)
                                        .compose(RxUtils.handleResult())
                                        .subscribe(s1 -> {

                                            ToastUtil.show("上传成功");
                                        });
                            }

                        }

                    });

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        failTime = 0;
        List<CollectBean> collectBeanList = JsonUtils.getInstance()
                .JsonToCollectList(ShareUtils.getValue(COLLECT_LIST, null));
        if (collectBeanList != null){
            for(CollectBean bean: collectBeanList){
                if (!bean.getIsSuccess()){
                    failTime++;
                }
            }
        }

        if (failTime == 0){
            badgeItem.hide();
        }else{
            badgeItem.show().setText(failTime+"");
        }

        int msgCnt = ShareUtils.getValue("message", 0);
        if (msgCnt == 0){
            msgBadgeItem.hide();
        }else{
            msgBadgeItem.show().setText(msgCnt+"");
        }

        int appCnt = failTime+msgCnt;
        if (appCnt != 0){
            BadgeUtil.setBadgeCount(this, appCnt, R.mipmap.ic_launcher);
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
