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
import com.xunao.diaodiao.Fragment.OrderSkillTabDoingRecieveFragment;
import com.xunao.diaodiao.Fragment.OrderSkillTabFinishFragment;
import com.xunao.diaodiao.Fragment.OrderSkillTabFinishRecieveFragment;
import com.xunao.diaodiao.Fragment.OrderSkillTabFragment;
import com.xunao.diaodiao.Fragment.OrderSkillTabRecieveFragment;
import com.xunao.diaodiao.Fragment.TabFragment;
import com.xunao.diaodiao.Present.OrderCompProjPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.View.OrderCompProjView;
import com.xunao.diaodiao.adapters.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_PROJECT_DOING;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_PROJECT_DONE;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_PROJECT_WAIT;
import static com.xunao.diaodiao.Common.Constants.COMPANY_TYPE;
import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;
import static com.xunao.diaodiao.Common.Constants.SKILL_RECIEVE_LINGGONG;
import static com.xunao.diaodiao.Common.Constants.SKILL_RECIEVE_PROJECT;
import static com.xunao.diaodiao.Common.Constants.SKILL_RELEASE_LINGGONG;
import static com.xunao.diaodiao.Common.Constants.SKILL_TYPE;
import static com.xunao.diaodiao.Common.Constants.TYPE_KEY;

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
    private static final String[] TAB_TITLE_WHO = {"申请中", "进行中", "已完成/关闭"};
    private int type = 0;
    private int who = 0;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, OrderCompProjActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, int who) {
        Intent intent = new Intent(context, OrderCompProjActivity.class);
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

        showToolbarBack(toolBar, titleText, "零工信息");

        type = ShareUtils.getValue(TYPE_KEY, 0);
        if (type == COMPANY_TYPE){
            showToolbarBack(toolBar, titleText, "项目信息");
        }else if (type == SKILL_TYPE){
            who = getIntent().getIntExtra("WHO", 0);
            if (who == SKILL_RELEASE_LINGGONG){
                showToolbarBack(toolBar, titleText, "我发布的-零工信息");
            }else if (who == SKILL_RECIEVE_LINGGONG){
                showToolbarBack(toolBar, titleText, "我接的-零工信息");
            }else if (who == SKILL_RECIEVE_PROJECT){
                showToolbarBack(toolBar, titleText, "项目信息");
            }

        }


        fragments=new ArrayList<>();
        if (type == COMPANY_TYPE){
            fragments.add(OrderCompTabFragment.newInstance("待确认", COMPANY_RELEASE_PROJECT_WAIT));
            fragments.add(OrderCompTabFragment.newInstance("进行中", COMPANY_RELEASE_PROJECT_DOING));
            fragments.add(OrderCompTabFragment.newInstance("已完成/取消", COMPANY_RELEASE_PROJECT_DONE));
        }else if (type == SKILL_TYPE){
            if (who == SKILL_RELEASE_LINGGONG){
                //我发的 零工
                fragments.add(OrderSkillTabFragment.newInstance("待确认"));
                fragments.add(OrderSkillTabDoingFragment.newInstance("进行中"));
                fragments.add(OrderSkillTabFinishFragment.newInstance("已完成/取消"));
            }else if (who == SKILL_RECIEVE_LINGGONG){
                //我接的 零工
                fragments.add(OrderSkillTabRecieveFragment.newInstance("申请中", who));
                fragments.add(OrderSkillTabDoingRecieveFragment.newInstance("进行中", who));
                fragments.add(OrderSkillTabFinishRecieveFragment.newInstance("已完成/关闭", who));
            }else if (who == SKILL_RECIEVE_PROJECT){
                //我接的 项目
                fragments.add(OrderSkillTabRecieveFragment.newInstance("申请中", who));
                fragments.add(OrderSkillTabDoingRecieveFragment.newInstance("进行中", who));
                fragments.add(OrderSkillTabFinishRecieveFragment.newInstance("已完成/关闭", who));
            }

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
            if (who == 0){
                tab.getTabAt(i).setText(TAB_TITLE[i]);
            }else{
                tab.getTabAt(i).setText(TAB_TITLE_WHO[i]);
            }

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
