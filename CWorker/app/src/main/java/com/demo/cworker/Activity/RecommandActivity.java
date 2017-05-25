package com.demo.cworker.Activity;

import android.app.Activity;
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

import com.demo.cworker.Fragment.TabFragment;
import com.demo.cworker.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.demo.cworker.Common.Constants.INTENT_KEY;

public class RecommandActivity extends BaseActivity {

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private List<Fragment> fragments;

    public static void startActivityForResult(Activity context, String s) {
        Intent intent = new Intent(context, RecommandActivity.class);
        intent.putExtra(INTENT_KEY, s);
        context.startActivityForResult(intent, CollectActivity.REQUEST_RECOMMAND_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommand);
        ButterKnife.bind(this);
        showToolbarBack(toolBar, titleText, "");

        String[] tabList = getResources().getStringArray(R.array.tabNames);

        fragments = new ArrayList<>();
        fragments.add(TabFragment.newInstance(tabList[0]));
        fragments.add(TabFragment.newInstance(tabList[1]));
        fragments.add(TabFragment.newInstance(tabList[2]));
        fragments.add(TabFragment.newInstance(tabList[3]));
        fragments.add(TabFragment.newInstance(tabList[4]));
        fragments.add(TabFragment.newInstance(tabList[5]));
        fragments.add(TabFragment.newInstance(tabList[6]));
        SimpleFragmentPagerAdapter pagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(),fragments);

        tabs.addTab(tabs.newTab().setText(tabList[0]));
        tabs.addTab(tabs.newTab().setText(tabList[1]));
        tabs.addTab(tabs.newTab().setText(tabList[2]));
        tabs.addTab(tabs.newTab().setText(tabList[3]));
        tabs.addTab(tabs.newTab().setText(tabList[4]));
        tabs.addTab(tabs.newTab().setText(tabList[5]));
        tabs.addTab(tabs.newTab().setText(tabList[6]));

        viewpager.setAdapter(pagerAdapter);
        tabs.setupWithViewPager(viewpager);
        for (int i=0; i<tabs.getTabCount(); i++){
            tabs.getTabAt(i).setText(tabList[i]);
        }
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
            TabFragment fragment = (TabFragment) fragments.get(position);
            return fragment.getTitle();
        }
    }
}
