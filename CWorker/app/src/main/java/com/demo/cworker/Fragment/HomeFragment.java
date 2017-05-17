package com.demo.cworker.Fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    private RecyclerArrayAdapter<HomeBean> adapter;
    private RecyclerArrayAdapter<HomeBean> adapterClassic;
    private RecyclerArrayAdapter<HomeBean> adapterList;

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
        ButterKnife.bind(this, view);
        presenter.attachView(this);
        titleText.setText(mParam1);

        initBanner();
        initCreamList();
        initClassicList();
        initList();

        presenter.getFirstPage(getContext());
        swipe.setOnRefreshListener(this);
        return view;
    }

    private void initList() {
        adapterList = new RecyclerArrayAdapter<HomeBean>(getContext(), R.layout.home_vertical_list) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, HomeBean homeBean) {
                baseViewHolder.setImageResource(R.id.item_img, R.drawable.ic_launcher_round);
                baseViewHolder.setText(R.id.item_txt, homeBean.getText());
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
        List<HomeBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            HomeBean bean = new HomeBean();
            bean.setText("111" + i);
            bean.setUrl("123");
            list.add(bean);
        }
        adapterList.addAll(list);
    }

    private void initClassicList() {
        adapterClassic = new RecyclerArrayAdapter<HomeBean>(getContext(), R.layout.home_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, HomeBean homeBean) {
                baseViewHolder.setImageResource(R.id.item_img, R.drawable.ic_launcher_round);
                baseViewHolder.setText(R.id.item_txt, homeBean.getText());
            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewClassic.setLayoutManager(linearLayoutManager);
        recyclerViewClassic.setAdapter(adapterClassic);
        List<HomeBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            HomeBean bean = new HomeBean();
            bean.setText("111" + i);
            bean.setUrl("123");
            list.add(bean);
        }
        adapterClassic.addAll(list);
    }

    private void initCreamList() {
        adapter = new RecyclerArrayAdapter<HomeBean>(getContext(), R.layout.home_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, HomeBean homeBean) {
                baseViewHolder.setImageResource(R.id.item_img, R.drawable.ic_launcher_round);
                baseViewHolder.setText(R.id.item_txt, homeBean.getText());
            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        List<HomeBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            HomeBean bean = new HomeBean();
            bean.setText("111" + i);
            bean.setUrl("123");
            list.add(bean);
        }
        adapter.addAll(list);

    }

    private void initBanner() {
        List<BannerInfo> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            BannerInfo info = new BannerInfo();
            info.setImg("http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg");
            info.setLink("http://www.baidu.com");
            info.setOt(i);
            list.add(info);
        }

        imageCycleView.setImageResources(list, ((bannerInfo, i, view) -> {
            WebViewActivity.startActivity(getContext(), bannerInfo.getLink());
        }));
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void getData(HomeResponseBean bean) {
        swipe.setRefreshing(false);
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
