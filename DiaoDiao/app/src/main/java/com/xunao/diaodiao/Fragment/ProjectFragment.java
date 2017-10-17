package com.xunao.diaodiao.Fragment;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Activity.LoginActivity;
import com.xunao.diaodiao.Activity.SelectMemoryActivity;
import com.xunao.diaodiao.Bean.HomeProjBean;
import com.xunao.diaodiao.MainActivity;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.ProjectPresenter;
import com.xunao.diaodiao.Present.ProjectRes;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.View.ProjectView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.TYPE_KEY;

/**
 * create by
 */
public class ProjectFragment extends BaseFragment implements ProjectView, View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    @Inject
    ProjectPresenter presenter;
    @BindView(R.id.proj_waiting)
    TextView projWaiting;
    @BindView(R.id.proj_doing)
    TextView projDoing;
    @BindView(R.id.jianli_waiting)
    TextView jianliWaiting;
    @BindView(R.id.jianli_doing)
    TextView jianliDoing;
    @BindView(R.id.lg_waiting)
    TextView lgWaiting;
    @BindView(R.id.lg_apply)
    TextView lgApply;
    @BindView(R.id.lg_doing)
    TextView lgDoing;
    @BindView(R.id.wb_waiting)
    TextView wbWaiting;
    @BindView(R.id.wb_doing)
    TextView wbDoing;
    @BindView(R.id.hz_doing)
    TextView hzDoing;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;


    private String mParam1;

    private static final int[] COMPANY_IMAGES = {R.drawable.icon_wodexiangmu, R.drawable.icon_janlixinxi,
            R.drawable.icon_fabulingong, R.drawable.icon_fabuweibao, R.drawable.icon_fabuhuzhu};

    private static final String[] COMPANY_TEXTS = {"我的项目信息", "我的监理信息", "零工信息",
            "维保信息", "互助信息"};

    private static final String[] PROJECT_TYPE = {"待确认 ", "进行中 "};

    private RecyclerArrayAdapter<HomeProjBean> adapter;

    private int index = 0;
    private int selectIndex = 0;

    private ProjectRes.ProjectBean projectBean;
    private ProjectRes.SupervisorBean supervisorBean;
    private ProjectRes.MaintenanceBean maintenanceBean;
    private ProjectRes.OddBean oddBean;
    private String mutual;

    private int type;

    List<HomeProjBean> list = new ArrayList<>();

    public static ProjectFragment newInstance(String param1) {
        ProjectFragment fragment = new ProjectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.single_recycler_view, container, false);
        View view = inflater.inflate(R.layout.fragment_project, container, false);
        ButterKnife.bind(this, view);
        getActivityComponent().inject(this);
        presenter.attachView(this);
        hideToolbarBack(toolBar, titleText, "订单");
        type = ShareUtils.getValue("TYPE", 0);

        presenter.getMyWork();
        return view;
    }

    @Override
    public void getData(ProjectRes s) {
        projectBean = s.getProject();
        supervisorBean = s.getSupervisor();
        maintenanceBean = s.getMaintenance();
        oddBean = s.getOdd();
        mutual = s.getMutual();

        if (projectBean != null) {
            if (!TextUtils.isEmpty(projectBean.getProject_wait())) {
                projWaiting.setText(projectBean.getProject_wait());
            }
            if (!TextUtils.isEmpty(projectBean.getProject_doing())) {
                projDoing.setText(projectBean.getProject_doing());
            }
        } else if (supervisorBean != null) {
            if (!TextUtils.isEmpty(supervisorBean.getSupervisor_wait())) {
                jianliWaiting.setText(supervisorBean.getSupervisor_wait());

            }
            if (!TextUtils.isEmpty(supervisorBean.getSupervisor_doing())) {
                jianliDoing.setText(supervisorBean.getSupervisor_doing());
            }
        } else if (oddBean != null) {
            if (!TextUtils.isEmpty(oddBean.getOdd_wait())) {
                lgWaiting.setText(oddBean.getOdd_wait());

            }
            if (!TextUtils.isEmpty(oddBean.getOdd_doing())) {
                lgDoing.setText(oddBean.getOdd_doing());
            }

            if (!TextUtils.isEmpty(oddBean.getOdd_apply())) {
                lgApply.setText(oddBean.getOdd_apply());
            }

        } else if (maintenanceBean != null) {
            if (!TextUtils.isEmpty(maintenanceBean.getMaintenance_doing())) {
                wbDoing.setText(maintenanceBean.getMaintenance_doing());

            }
            if (!TextUtils.isEmpty(maintenanceBean.getMaintenance_wait())) {
                wbWaiting.setText(maintenanceBean.getMaintenance_wait());
            }
        } else if (!TextUtils.isEmpty(mutual)) {
            hzDoing.setText(mutual);
        }


//        list.clear();
//        switch (type) {
//            case SKILL_TYPE:
//            case COMPANY_TYPE:
//                for (int i = 0; i < COMPANY_IMAGES.length; i++) {
//                    HomeProjBean bean = new HomeProjBean();
//                    bean.setProjImage(COMPANY_IMAGES[i]);
//                    bean.setProjText(COMPANY_TEXTS[i]);
//                    list.add(bean);
//                }
//                break;
//
//            case 3:
//                break;
//        }
//        adapter.clear();
//        adapter.addAll(list);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void updateData() {
        super.updateData();
        presenter.getMyWork();
    }

    @Override
    public void onResume() {
        super.onResume();

        type = ShareUtils.getValue(TYPE_KEY, 0);
        if (TextUtils.isEmpty(User.getInstance().getUserId())) {
            //recyclerView.setVisibility(View.GONE);
            index++;
            if (index == 1) {
                LoginActivity.startActivity(ProjectFragment.this.getContext());
                ((MainActivity) getActivity()).goToFragment(1);
            }
        } else if (type == 0) {
            //recyclerView.setVisibility(View.GONE);

            selectIndex++;
            if (selectIndex == 1) {
                SelectMemoryActivity.startActivity(ProjectFragment.this.getContext());
                ((MainActivity) getActivity()).goToFragment(1);
            }

        } else {
            //recyclerView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (TextUtils.isEmpty(User.getInstance().getUserId())) {
                LoginActivity.startActivity(ProjectFragment.this.getContext());
                ((MainActivity) getActivity()).goToFragment(1);
            } else {
                presenter.getMyWork();
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
