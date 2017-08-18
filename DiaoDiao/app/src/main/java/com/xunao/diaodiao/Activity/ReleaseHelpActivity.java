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
import android.widget.TextView;

import com.xunao.diaodiao.Fragment.ReleaseTabItemFragment;
import com.xunao.diaodiao.Present.ReleaseHelpPresenter;
import com.xunao.diaodiao.R;
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

    private List<Fragment> list;
    private SimpleFragmentPagerAdapter adapter;
    private String[] titles = {"壁挂炉", "中央空调", "分体空调", "新风系统", "水处理", "空调"};

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

        list = new ArrayList<>();
        for(int i=0; i<titles.length; i++){
            tabLayout.addTab(tabLayout.newTab().setText(titles[i]));
            list.add(ReleaseTabItemFragment.newInstance(titles[i]));
            tabLayout.getTabAt(i).setText(titles[i]);
        }

        adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), list);
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        for(int i=0; i<titles.length; i++){
            tabLayout.getTabAt(i).setText(titles[i]);
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
        private List<Fragment> fragments;
        public SimpleFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
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
