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
import com.xunao.diaodiao.Fragment.OrderCompHZFragment;
import com.xunao.diaodiao.Fragment.OrderCompJLFragment;
import com.xunao.diaodiao.Fragment.OrderCompTabFragment;
import com.xunao.diaodiao.Fragment.OrderCompWBFragment;
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

import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_HUZHU;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_HUZHU_DOING;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_HUZHU_DONE;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_HUZHU_WAIT;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_JIANLI;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_JIANLI_DOING;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_JIANLI_DONE;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_JIANLI_WAIT;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_PROJECT_DOING;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_PROJECT_DONE;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_PROJECT_WAIT;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_WEIBAO;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_WEIBAO_DOING;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_WEIBAO_DONE;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_WEIBAO_WAIT;
import static com.xunao.diaodiao.Common.Constants.COMPANY_TYPE;
import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;
import static com.xunao.diaodiao.Common.Constants.SKILL_RECIEVE_JIANLI;
import static com.xunao.diaodiao.Common.Constants.SKILL_RECIEVE_LINGGONG;
import static com.xunao.diaodiao.Common.Constants.SKILL_RECIEVE_PROJECT;
import static com.xunao.diaodiao.Common.Constants.SKILL_RECIEVE_WEIBAO;
import static com.xunao.diaodiao.Common.Constants.SKILL_RELEASE_HUZHU;
import static com.xunao.diaodiao.Common.Constants.SKILL_RELEASE_LINGGONG;
import static com.xunao.diaodiao.Common.Constants.SKILL_RELEASE_WEIBAO;
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
        who = getIntent().getIntExtra("WHO", 0);

        if (type == COMPANY_TYPE){
            showToolbarBack(toolBar, titleText, "项目信息");
            if(who == COMPANY_RELEASE_JIANLI){
                showToolbarBack(toolBar, titleText, "监理信息");
            }else if(who == COMPANY_RELEASE_WEIBAO){
                showToolbarBack(toolBar, titleText, "维保信息");
            }else if(who == COMPANY_RELEASE_HUZHU){
                showToolbarBack(toolBar, titleText, "互助信息");
            }

        }else if (type == SKILL_TYPE){

            if (who == SKILL_RELEASE_LINGGONG){
                showToolbarBack(toolBar, titleText, "我发布的-零工信息");
            }else if (who == SKILL_RECIEVE_LINGGONG){
                showToolbarBack(toolBar, titleText, "我接的-零工信息");
            }else if (who == SKILL_RECIEVE_PROJECT){
                showToolbarBack(toolBar, titleText, "项目信息");
            }else if(who == SKILL_RELEASE_WEIBAO){
                showToolbarBack(toolBar, titleText, "我发布的-维保信息");
            }else if(who == SKILL_RECIEVE_WEIBAO){
                showToolbarBack(toolBar, titleText, "我接的-维保信息");
            }else if(who == SKILL_RECIEVE_JIANLI){
                showToolbarBack(toolBar, titleText, "监理信息");
            }else if(who == SKILL_RELEASE_HUZHU){
                showToolbarBack(toolBar, titleText, "互助信息");
            }

        }


        fragments=new ArrayList<>();
        if (type == COMPANY_TYPE){
            if(who == COMPANY_RELEASE_WEIBAO){
                fragments.add(OrderCompWBFragment.newInstance("待确认", COMPANY_RELEASE_WEIBAO_WAIT));
                fragments.add(OrderCompWBFragment.newInstance("进行中", COMPANY_RELEASE_WEIBAO_DOING));
                fragments.add(OrderCompWBFragment.newInstance("已完成/取消", COMPANY_RELEASE_WEIBAO_DONE));
            }else if(who == COMPANY_RELEASE_JIANLI){
                fragments.add(OrderCompJLFragment.newInstance("待确认", COMPANY_RELEASE_JIANLI_WAIT));
                fragments.add(OrderCompJLFragment.newInstance("进行中", COMPANY_RELEASE_JIANLI_DOING));
                fragments.add(OrderCompJLFragment.newInstance("已完成/取消", COMPANY_RELEASE_JIANLI_DONE));
            }else if(who == COMPANY_RELEASE_HUZHU) {
                fragments.add(OrderCompHZFragment.newInstance("待确认", COMPANY_RELEASE_HUZHU_WAIT));
                //fragments.add(OrderCompHZFragment.newInstance("进行中", COMPANY_RELEASE_HUZHU_DOING));
                fragments.add(OrderCompHZFragment.newInstance("已关闭", COMPANY_RELEASE_HUZHU_DONE));
            }else {
                fragments.add(OrderCompTabFragment.newInstance("待确认", COMPANY_RELEASE_PROJECT_WAIT));
                fragments.add(OrderCompTabFragment.newInstance("进行中", COMPANY_RELEASE_PROJECT_DOING));
                fragments.add(OrderCompTabFragment.newInstance("已完成/取消", COMPANY_RELEASE_PROJECT_DONE));
            }

        }else if (type == SKILL_TYPE){
            if (who == SKILL_RELEASE_LINGGONG){
                //我发的 零工
                fragments.add(OrderSkillTabFragment.newInstance("待确认", who));
                fragments.add(OrderSkillTabDoingFragment.newInstance("进行中", who));
                fragments.add(OrderSkillTabFinishFragment.newInstance("已完成/取消", who));
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
            }else if(who == SKILL_RELEASE_WEIBAO){
                //我发的 维保
                fragments.add(OrderSkillTabFragment.newInstance("待确认", who));
                fragments.add(OrderSkillTabDoingFragment.newInstance("进行中", who));
                fragments.add(OrderSkillTabFinishFragment.newInstance("已完成/取消", who));
            }else if(who == SKILL_RECIEVE_WEIBAO){
                //我接的 维保
                fragments.add(OrderSkillTabRecieveFragment.newInstance("申请中", who));
                fragments.add(OrderSkillTabDoingRecieveFragment.newInstance("进行中", who));
                fragments.add(OrderSkillTabFinishRecieveFragment.newInstance("已完成/关闭", who));
            }else if(who == SKILL_RECIEVE_JIANLI){
                //监理
                fragments.add(OrderSkillTabRecieveFragment.newInstance("申请中", who));
                fragments.add(OrderSkillTabDoingRecieveFragment.newInstance("进行中", who));
                fragments.add(OrderSkillTabFinishRecieveFragment.newInstance("已完成/关闭", who));
            }else if(who == SKILL_RELEASE_HUZHU){
                fragments.add(OrderCompHZFragment.newInstance("待确认", COMPANY_RELEASE_HUZHU_WAIT));
                //fragments.add(OrderCompHZFragment.newInstance("进行中", COMPANY_RELEASE_HUZHU_DOING));
                fragments.add(OrderCompHZFragment.newInstance("已关闭", COMPANY_RELEASE_HUZHU_DONE));
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
            if (who == COMPANY_RELEASE_WEIBAO
                    || (who == COMPANY_RELEASE_JIANLI) ||
                        (who == COMPANY_RELEASE_HUZHU) || (who == 0)){
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
