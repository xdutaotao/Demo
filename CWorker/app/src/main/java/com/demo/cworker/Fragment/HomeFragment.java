package com.demo.cworker.Fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.cworker.Activity.HomeDetailActivity;
import com.demo.cworker.Activity.WebViewActivity;
import com.demo.cworker.Bean.HomeBean;
import com.demo.cworker.Bean.HomeResponseBean;
import com.demo.cworker.Present.HomePresenter;
import com.demo.cworker.R;
import com.demo.cworker.View.HomeView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.gzfgeh.swipeheader.SwipeRefreshLayout;
import com.gzfgeh.viewpagecycle.BannerInfo;
import com.gzfgeh.viewpagecycle.ImageCycleView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends BaseFragment implements HomeView, android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.recycler_view_classic)
    RecyclerView recyclerViewClassic;
    @BindView(R.id.recycler_view_list)
    RecyclerView recyclerViewList;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    @BindView(R.id.type_one)
    TextView typeOne;
    @BindView(R.id.typeTwo)
    TextView typeTwo;
    @BindView(R.id.typeThree)
    TextView typeThree;
    private String mParam1;

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.image_cycle_view)
    ImageCycleView imageCycleView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @Inject
    HomePresenter presenter;

    private RecyclerArrayAdapter<HomeResponseBean.TopicBean.GroupDataBean> adapter;
    private RecyclerArrayAdapter<HomeResponseBean.TopicBean.GroupDataBean> adapterClassic;
    private RecyclerArrayAdapter<HomeResponseBean.TopicBean.GroupDataBean> adapterList;

    public static HomeFragment newInstance(String param1) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getActivityComponent().inject(this);
        ButterKnife.bind(this, view);
        presenter.attachView(this);
        titleText.setText(mParam1);

        initCreamList();
        initClassicList();
        initList();

        presenter.getFirstPage(getContext());
        swipe.setOnRefreshListener(this);
        return view;
    }

    private void initList() {
        adapterList = new RecyclerArrayAdapter<HomeResponseBean.TopicBean.GroupDataBean>(getContext(), R.layout.home_vertical_list) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, HomeResponseBean.TopicBean.GroupDataBean homeBean) {
                baseViewHolder.setImageUrl(R.id.item_img, homeBean.getPic(), R.drawable.ic_launcher_round);
                baseViewHolder.setText(R.id.item_title, homeBean.getTitle());
                baseViewHolder.setText(R.id.item_author, "作者:"+homeBean.getAuthor());
                baseViewHolder.setVisible(R.id.item_vip, homeBean.getVipRes() != 0);
                baseViewHolder.setText(R.id.item_txt, homeBean.getDescription());
            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerViewList.setLayoutManager(linearLayoutManager);
        recyclerViewList.setAdapter(adapterList);

        adapterList.setOnItemClickListener((view, i) -> {
            HomeDetailActivity.startActivity(getContext(), adapterList.getAllData().get(i));
        });
    }

    private void initClassicList() {
        adapterClassic = new RecyclerArrayAdapter<HomeResponseBean.TopicBean.GroupDataBean>(getContext(), R.layout.home_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, HomeResponseBean.TopicBean.GroupDataBean homeBean) {
                baseViewHolder.setImageUrl(R.id.item_img, homeBean.getPic(), R.drawable.ic_launcher_round);
                baseViewHolder.setText(R.id.item_txt, homeBean.getTitle());
                baseViewHolder.setText(R.id.item_author, homeBean.getAuthor());
                baseViewHolder.setVisible(R.id.item_vip, homeBean.getVipRes() != 0);
            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewClassic.setLayoutManager(linearLayoutManager);
        recyclerViewClassic.setAdapter(adapterClassic);

        adapterClassic.setOnItemClickListener((view, i) -> HomeDetailActivity.startActivity(getContext(), adapterClassic.getAllData().get(i)));
    }

    private void initCreamList() {
        adapter = new RecyclerArrayAdapter<HomeResponseBean.TopicBean.GroupDataBean>(getContext(), R.layout.home_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, HomeResponseBean.TopicBean.GroupDataBean homeBean) {
                baseViewHolder.setImageUrl(R.id.item_img, homeBean.getPic(), R.drawable.ic_launcher_round);
                baseViewHolder.setText(R.id.item_txt, homeBean.getTitle());
                baseViewHolder.setText(R.id.item_author, homeBean.getAuthor());
                baseViewHolder.setVisible(R.id.item_vip, homeBean.getVipRes() != 0);
            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((view, i) -> HomeDetailActivity.startActivity(getContext(), adapter.getAllData().get(i)));
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void getData(HomeResponseBean bean) {
        swipe.setRefreshing(false);
        for (HomeResponseBean.TopicBean topicBean : bean.getTopic()) {
            if (TextUtils.equals("jingHua", topicBean.getGroupTitle())) {
                typeOne.setText("精华");
                adapter.addAll(topicBean.getGroupData());
            } else if (TextUtils.equals("caiNiXiHuan", topicBean.getGroupTitle())) {
                typeTwo.setText("猜你喜欢");
                adapterClassic.addAll(topicBean.getGroupData());
            } else if (TextUtils.equals("jingDian", topicBean.getGroupTitle())) {
                typeThree.setText("经典");
                adapterList.addAll(topicBean.getGroupData());
            }
        }

        List<BannerInfo> list = new ArrayList<>();
        for (HomeResponseBean.BannerBean bannerBean : bean.getBanner()) {
            BannerInfo info = new BannerInfo();
            info.setImg(bannerBean.getBannerPicUrl());
            info.setLink(bannerBean.getUrl());
            info.setOt(1);
            list.add(info);
        }

        imageCycleView.setImageResources(list, ((bannerInfo, i, view) -> {
            WebViewActivity.startActivity(getContext(), bannerInfo.getLink(), bean.getBanner().get(i).getTitle());
        }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onRefresh() {
        presenter.getFirstPage(getContext());
    }
}
