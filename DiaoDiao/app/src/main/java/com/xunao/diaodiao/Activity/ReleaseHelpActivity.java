package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xunao.diaodiao.Bean.MaintenanceTypeRes;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Fragment.ReleaseTabItemFragment;
import com.xunao.diaodiao.Present.ReleaseHelpPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.ReleaseHelpView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class ReleaseHelpActivity extends BaseActivity implements ReleaseHelpView {

    @Inject
    ReleaseHelpPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.repair)
    TextView repair;
    @BindView(R.id.maintain)
    TextView maintain;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.main_tab_layout)
    TabLayout mainTabLayout;
    @BindView(R.id.main_viewpager)
    ViewPager mainViewpager;

    private boolean isMaintain = false;
    private List<MaintenanceTypeRes.RepairBean> repairList = new ArrayList<>();
    private List<MaintenanceTypeRes.RepairBean> maintainList = new ArrayList<>();

    private List<String> repairTitles = new ArrayList<>();
    private List<String> mainTainTitles = new ArrayList<>();
    public static List<String> selectRepairTitle = new ArrayList<>();
    public static List<String> selectMainTainTitle = new ArrayList<>();
    public static List<String> repairIDs = new ArrayList<>();
    public static List<String> mainTainIDs = new ArrayList<>();
    public static List<String> typeIDs = new ArrayList<>();
    public static List<String> repairNames = new ArrayList<>();
    public static List<String> mainTainNames = new ArrayList<>();
    public static int project_class, project_brand, project_type;
    public static String projectClassName, projectBrandName, projectTypeName;

    private List<ReleaseTabItemFragment> list = new ArrayList<>();
    private List<ReleaseTabItemFragment> mainList = new ArrayList<>();
    private SimpleFragmentPagerAdapter adapter;
    private SimpleFragmentPagerAdapter mainAdapter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ReleaseHelpActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_help);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "选择维保类型");
        presenter.maintenanceType(this);

        repair.setOnClickListener(v -> {
            if (isMaintain) {
                repair.setTextColor(getResources().getColor(R.color.white));
                repair.setBackgroundResource(R.drawable.btn_blue_bg);
                maintain.setTextColor(getResources().getColor(R.color.nav_gray));
                maintain.setBackgroundResource(R.drawable.btn_blank_bg);

            }
            isMaintain = false;

            mainViewpager.setVisibility(View.GONE);
            mainTabLayout.setVisibility(View.GONE);
            viewpager.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);

            for(ReleaseTabItemFragment fragment: list){
                fragment.update();
            }

            for(ReleaseTabItemFragment fragment: mainList){
                fragment.update();
            }

        });

        maintain.setOnClickListener(v -> {
            if (!isMaintain) {
                maintain.setTextColor(getResources().getColor(R.color.white));
                maintain.setBackgroundResource(R.drawable.btn_blue_bg);
                repair.setTextColor(getResources().getColor(R.color.nav_gray));
                repair.setBackgroundResource(R.drawable.btn_blank_bg);

            }
            isMaintain = true;

            mainViewpager.setVisibility(View.VISIBLE);
            mainTabLayout.setVisibility(View.VISIBLE);
            viewpager.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);

            for(ReleaseTabItemFragment fragment: list){
                fragment.update();
            }

            for(ReleaseTabItemFragment fragment: mainList){
                fragment.update();
            }
        });

        next.setOnClickListener(v -> {
            if(project_brand == 0){
                ToastUtil.show("请选择");
                return;
            }
            ReleaseSkillInforActivity.startActivity(this);
        });

        RxBus.getInstance().toObservable(String.class)
                .filter(s -> TextUtils.equals(s, Constants.DESTORY))
                .subscribe(s -> {
                    finish();
                });


        mainViewpager.setVisibility(View.GONE);
        mainTabLayout.setVisibility(View.GONE);

    }

    @Override
    public void getData(MaintenanceTypeRes res) {
        repairList = res.getRepair();
        maintainList = res.getMaintain();

        //切到维修
        tabLayout.removeAllTabs();
        repairTitles.clear();
        list.clear();
        for (MaintenanceTypeRes.RepairBean bean : repairList) {
            repairTitles.add(bean.getClass_name());

            tabLayout.addTab(tabLayout.newTab().setText(bean.getClass_name()));
            list.add(ReleaseTabItemFragment.newInstance(bean.getClass_name(), bean, true));

        }

        adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), list);
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                list.get(tab.getPosition()).update();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setText(repairTitles.get(i));
        }


        //切到 保养
        mainTabLayout.removeAllTabs();
        mainList.clear();
        mainTainTitles.clear();
        for (MaintenanceTypeRes.RepairBean bean : maintainList) {
            mainTainTitles.add(bean.getClass_name());

            mainTabLayout.addTab(mainTabLayout.newTab().setText(bean.getClass_name()));
            mainList.add(ReleaseTabItemFragment.newInstance(bean.getClass_name(), bean, false));
        }

        mainAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), mainList);
        mainViewpager.setAdapter(mainAdapter);
        mainTabLayout.setupWithViewPager(mainViewpager);
        mainTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mainList.get(tab.getPosition()).update();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        for (int i = 0; i < mainTabLayout.getTabCount(); i++) {
            mainTabLayout.getTabAt(i).setText(mainTainTitles.get(i));
        }

    }


    @Override
    public void onFailure() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }


    public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<ReleaseTabItemFragment> fragments;

        public SimpleFragmentPagerAdapter(FragmentManager fm, List<ReleaseTabItemFragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        //这个方法返回Tab显示的文字。这里通过在实例化TabFragment的时候，传入的title参数返回标题。
        @Override
        public CharSequence getPageTitle(int position) {
            ReleaseTabItemFragment fragment = (ReleaseTabItemFragment) fragments.get(position);
            return fragment.getTitle();
        }
    }

}
