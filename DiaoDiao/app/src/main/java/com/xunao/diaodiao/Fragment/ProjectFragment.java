package com.xunao.diaodiao.Fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Activity.LoginActivity;
import com.xunao.diaodiao.Activity.OrderCompProjActivity;
import com.xunao.diaodiao.Activity.SelectMemoryActivity;
import com.xunao.diaodiao.Bean.HomeProjBean;
import com.xunao.diaodiao.MainActivity;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.MessagePresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.View.MessageView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.TYPE_KEY;

/**
 * create by
 */
public class ProjectFragment extends BaseFragment implements MessageView, View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    @Inject
    MessagePresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private String mParam1;

    private static final int[] COMPANY_IMAGES = {R.drawable.icon_wodexiangmu, R.drawable.icon_janlixinxi,
            R.drawable.icon_fabulingong, R.drawable.icon_fabuweibao, R.drawable.icon_fabuhuzhu};

    private static final String[] COMPANY_TEXTS = {"我的项目信息", "我的监理信息", "零工信息",
            "维保信息", "互助信息"};

    private RecyclerArrayAdapter<HomeProjBean> adapter;

    private int index = 0;
    private int selectIndex = 0;

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
        View view = inflater.inflate(R.layout.single_recycler_view, container, false);
        ButterKnife.bind(this, view);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        hideToolbarBack(toolBar, titleText, "项目");

        adapter = new RecyclerArrayAdapter<HomeProjBean>(getContext(), R.layout.project_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, HomeProjBean s) {
                baseViewHolder.setText(R.id.item_text, s.getProjText());
                baseViewHolder.setImageResource(R.id.item_image, s.getProjImage());
            }
        };

        adapter.setOnItemClickListener((view1, i) -> {
            switch (i) {
                case 0:
                    OrderCompProjActivity.startActivity(ProjectFragment.this.getContext());
                    break;

                case 1:
                    break;

                case 2:
                    break;
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


        List<HomeProjBean> list = new ArrayList<>();

        switch (ShareUtils.getValue("TYPE", 1)) {
            case 1:
                for (int i = 0; i < COMPANY_IMAGES.length; i++) {
                    HomeProjBean bean = new HomeProjBean();
                    bean.setProjImage(COMPANY_IMAGES[i]);
                    bean.setProjText(COMPANY_TEXTS[i]);
                    list.add(bean);
                }
                break;

            case 2:
                break;

            case 3:
                break;
        }
        adapter.addAll(list);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        int type = ShareUtils.getValue(TYPE_KEY, 0);
        if (TextUtils.isEmpty(User.getInstance().getUserId())){
            recyclerView.setVisibility(View.GONE);
            index++;
            if (index == 1) {
                LoginActivity.startActivity(ProjectFragment.this.getContext());
                ((MainActivity) getActivity()).goToFragment(1);
            }
        }else if(type ==0){
            recyclerView.setVisibility(View.GONE);

            selectIndex++;
            if (selectIndex == 1) {
                SelectMemoryActivity.startActivity(ProjectFragment.this.getContext());
                ((MainActivity) getActivity()).goToFragment(1);
            }

        }else{
            recyclerView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            if (TextUtils.isEmpty(User.getInstance().getUserId())) {
                LoginActivity.startActivity(ProjectFragment.this.getContext());
                ((MainActivity) getActivity()).goToFragment(1);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
