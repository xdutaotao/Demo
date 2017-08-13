package com.xunao.diaodiao.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.xunao.diaodiao.Bean.PackageBean;
import com.xunao.diaodiao.Fragment.TabFragment;
import com.xunao.diaodiao.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

public class RecommandActivity extends BaseActivity {
    private static final String LIST_KEY = "LIST_KEY";
    private static final String TAB_LIST_KEY = "TAB_LIST_KEY";

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private List<Fragment> fragments;
    private ArrayList<String> selectList;
    private List<PackageBean.ResultBean.PmtsBean> tabList;
    private List<PackageBean.ResultBean.PmsBean> dataList;
    public List<String> changeList = new ArrayList<>();

    public static void startActivityForResult(Activity context, List<PackageBean.ResultBean.PmtsBean> tabList, List<PackageBean.ResultBean.PmsBean> nameList, ArrayList<String> selectList) {
        Intent intent = new Intent(context, RecommandActivity.class);
        intent.putExtra(LIST_KEY, (Serializable) nameList);
        intent.putExtra(TAB_LIST_KEY, (Serializable) tabList);
        intent.putStringArrayListExtra(INTENT_KEY, selectList);
        context.startActivityForResult(intent, CollectActivity.REQUEST_RECOMMAND_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommand);
        ButterKnife.bind(this);
        showToolbarBack(toolBar, titleText, "");

        fragments = new ArrayList<>();
        if (getIntent().getStringArrayListExtra(INTENT_KEY) != null){
            selectList = getIntent().getStringArrayListExtra(INTENT_KEY);
            changeList.addAll(selectList);
        }

        if (getIntent().getSerializableExtra(TAB_LIST_KEY) != null){
            tabList = (List<PackageBean.ResultBean.PmtsBean>) getIntent().getSerializableExtra(TAB_LIST_KEY);
        }

        if (getIntent().getSerializableExtra(LIST_KEY) != null){
            dataList = (List<PackageBean.ResultBean.PmsBean>) getIntent().getSerializableExtra(LIST_KEY);
            for(PackageBean.ResultBean.PmtsBean bean: tabList){
                ArrayList<String> listData = new ArrayList<>();
                for(PackageBean.ResultBean.PmsBean pmsBean: dataList){
                    if (TextUtils.equals(pmsBean.getType(), bean.getId())){
                        listData.add(pmsBean.getName());
                    }
                }

                ArrayList<String> selectData = new ArrayList<>();
                for(String listItem : listData){
                    for (String selectItem : selectList){
                        if (TextUtils.equals(selectItem, listItem)){
                            selectData.add(selectItem);
                        }
                    }
                }
                fragments.add(TabFragment.newInstance(bean.getName(), listData, selectData));
            }
        }

        for(PackageBean.ResultBean.PmtsBean bean: tabList){
            tabs.addTab(tabs.newTab().setText(bean.getName()));
        }

        SimpleFragmentPagerAdapter pagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(),fragments);
        viewpager.setAdapter(pagerAdapter);
        tabs.setupWithViewPager(viewpager);
        for(int i=0; i<tabs.getTabCount(); i++){
            tabs.getTabAt(i).setText(tabList.get(i).getName());
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra(INTENT_KEY, (Serializable) changeList);
                setResult(RESULT_OK, intent);
                finish();
                break;

        }
        return false;
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
