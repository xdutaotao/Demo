package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xunao.diaodiao.Fragment.BaseFragment;
import com.xunao.diaodiao.Fragment.BaseTabFragment;
import com.xunao.diaodiao.Fragment.OrderCompTabFragment;
import com.xunao.diaodiao.Fragment.OrderSkillTabDoingFragment;
import com.xunao.diaodiao.Fragment.OrderSkillTabFragment;
import com.xunao.diaodiao.Fragment.TabFragment;
import com.xunao.diaodiao.Present.OrderCompProjPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.OrderCompProjView;
import com.xunao.diaodiao.adapters.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class OrderCompProjActivity extends BaseActivity implements OrderCompProjView {

    @Inject
    OrderCompProjPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private List<BaseFragment> fragments;
    private static final String[] TAB_TITLE = {"待确认", "进行中", "已完成/取消"};
    private int type = 0;

    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, OrderCompProjActivity.class);
        intent.putExtra(INTENT_KEY, type);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, int type, int who) {
        Intent intent = new Intent(context, OrderCompProjActivity.class);
        intent.putExtra(INTENT_KEY, type);
        intent.putExtra("WHO", who);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_comp_proj);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        type = getIntent().getIntExtra(INTENT_KEY, 0);
        if (type == 0){
            showToolbarBack(toolBar, titleText, "项目信息");
        }else{
            if (getIntent().getIntExtra("WHO", 0) == 0){
                showToolbarBack(toolBar, titleText, "我发布的-零工信息");
            }else{
                showToolbarBack(toolBar, titleText, "零工信息");
            }

        }


        fragments=new ArrayList<>();
        if (type == 0){
            fragments.add(OrderCompTabFragment.newInstance("待确认"));
            fragments.add(OrderCompTabFragment.newInstance("进行中"));
            fragments.add(OrderCompTabFragment.newInstance("已完成/取消"));
        }else{
            fragments.add(OrderSkillTabFragment.newInstance("待确认"));
            fragments.add(OrderSkillTabDoingFragment.newInstance("进行中"));
            fragments.add(OrderSkillTabFragment.newInstance("已完成/取消"));
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(mSectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tab.setupWithViewPager(viewPager);

        LinearLayout linearLayout = (LinearLayout) tab.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
                R.drawable.layout_divider_vertical));
        linearLayout.setDividerPadding(20);

        for(int i=0; i<fragments.size(); i++){
            tab.getTabAt(i).setText(TAB_TITLE[i]);
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

}
