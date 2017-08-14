package com.xunao.diaodiao.Fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.gzfgeh.swipeheader.SwipeRefreshLayout;
import com.gzfgeh.viewpagecycle.BannerInfo;
import com.gzfgeh.viewpagecycle.ImageCycleView;
import com.xunao.diaodiao.Activity.HomeDetailActivity;
import com.xunao.diaodiao.Activity.SearchResultActivity;
import com.xunao.diaodiao.Activity.WebViewActivity;
import com.xunao.diaodiao.Bean.HomeResponseBean;
import com.xunao.diaodiao.Bean.SearchBean;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.HomePresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.LocationUtils;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.RxUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.HomeView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.LOGIN_AGAIN;

public class HomeFragment extends BaseFragment implements HomeView, android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
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
    @BindView(R.id.more_one)
    TextView moreOne;
    @BindView(R.id.more_two)
    TextView moreTwo;
    @BindView(R.id.more_three)
    TextView moreThree;
    @BindView(R.id.scrollView)
    LinearLayout scrollView;
    @BindView(R.id.location_add)
    TextView locationAdd;
    @BindView(R.id.type_image)
    ImageView typeImage;
    @BindView(R.id.edit_text)
    EditText editText;
    @BindView(R.id.cancle_action)
    ImageView cancleAction;
    @BindView(R.id.document)
    TextView document;
    private String mParam1;

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

        new LocationUtils().getLocationAddr(getActivity());
        RxBus.getInstance().toObservable(String.class)
                .compose(RxUtils.applyIOToMainThreadSchedulers())
                .subscribe(s -> {
                    locationAdd.setText(s);
                });

        initCreamList();
        initClassicList();
        initList();

        swipe.setOnRefreshListener(this);
        swipe.showHead();
        moreOne.setOnClickListener(this);
        moreTwo.setOnClickListener(this);
        moreThree.setOnClickListener(this);

        if (!TextUtils.isEmpty(User.getInstance().getUserId())) {
            presenter.checkToken();
        }
        return view;
    }

    private void initList() {
        adapterList = new RecyclerArrayAdapter<HomeResponseBean.TopicBean.GroupDataBean>(getContext(), R.layout.home_vertical_list) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, HomeResponseBean.TopicBean.GroupDataBean homeBean) {
                baseViewHolder.setImageUrl(R.id.item_img, homeBean.getPic(), R.drawable.ic_launcher_round);
                baseViewHolder.setText(R.id.item_title, homeBean.getTitle());
                baseViewHolder.setText(R.id.item_author, "作者:" + homeBean.getAuthor());
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
            String url = adapterList.getAllData().get(i).getUrl();
            if (url.contains(".png") || url.contains(".jpeg") ||
                    url.contains(".jpg")) {
                WebViewActivity.startActivity(getContext(), url,
                        adapterList.getAllData().get(i).getTitle());
            } else {
                HomeDetailActivity.startActivity(getContext(), adapterList.getAllData().get(i));
            }

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

        adapterClassic.setOnItemClickListener((view, i) -> {
            String url = adapterClassic.getAllData().get(i).getUrl();
            if (url.contains(".png") || url.contains(".jpeg") ||
                    url.contains(".jpg")) {
                WebViewActivity.startActivity(getContext(), url,
                        adapterClassic.getAllData().get(i).getTitle());
            } else {
                HomeDetailActivity.startActivity(getContext(), adapterClassic.getAllData().get(i));
            }


        });
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

        adapter.setOnItemClickListener((view, i) -> {
            String url = adapter.getAllData().get(i).getUrl();
            if (url.contains(".png") || url.contains(".jpeg") ||
                    url.contains(".jpg")) {
                WebViewActivity.startActivity(getContext(), url,
                        adapter.getAllData().get(i).getTitle());
            } else {
                HomeDetailActivity.startActivity(getContext(), adapter.getAllData().get(i));
            }

        });
    }

    @Override
    public void onFailure() {
        swipe.setRefreshing(false);
        //scrollView.setVisibility(View.INVISIBLE);
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
            if (bannerInfo.getLink().contains(".xlsx") ||
                    bannerInfo.getLink().contains(".xls")) {
                HomeResponseBean.TopicBean.GroupDataBean groupDataBean = new HomeResponseBean.TopicBean.GroupDataBean();
                HomeResponseBean.BannerBean bannerBean = bean.getBanner().get(i);
                groupDataBean.setAuthor(bannerBean.getAuthor());
                groupDataBean.setBannerPicUrl(bannerBean.getBannerPicUrl());
                groupDataBean.setDateline(bannerBean.getDateline());
                groupDataBean.setDescription(bannerBean.getDescription());
                groupDataBean.setUpdateTime(bannerBean.getUpdateTime());
                groupDataBean.setTitle(bannerBean.getTitle());
                groupDataBean.setPic(bannerBean.getPic());
                groupDataBean.setVipRes(bannerBean.getVipRes());
                groupDataBean.setUrl(bannerBean.getUrl());


                HomeDetailActivity.startActivity(getContext(), groupDataBean);
            } else {
                WebViewActivity.startActivity(getContext(), bannerInfo.getLink(), bean.getBanner().get(i).getTitle());
            }

        }));

        scrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void getTokenResult(String s) {
        if (TextUtils.equals(s, LOGIN_AGAIN)) {
            ToastUtil.show("用户过期，请重新登录");
            Utils.startLoginActivity();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onRefresh() {
        presenter.getFirstPage();
    }

    @Override
    public void onClick(View v) {
        if (User.getInstance().getUserInfo() == null) {
            ToastUtil.show("请登录");
            return;
        }
        SearchBean bean = new SearchBean();
        switch (v.getId()) {
            case R.id.more_one:
                bean.setGroupType(1);
                break;

            case R.id.more_two:
                bean.setGroupType(2);
                break;

            case R.id.more_three:
                bean.setGroupType(3);
                break;
        }

        bean.setPageNo(1);
        bean.setPageSize(15);
        bean.setKeywords("");
        bean.setToken(User.getInstance().getUserId());
        bean.setType(User.getInstance().getUserInfo().getPerson().getType());  //?
        bean.setVipRes(User.getInstance().getUserInfo().getPerson().getVIP());
        bean.setSort(0);
        bean.setProject("");
        SearchResultActivity.startActivity(getContext(), bean);
    }
}
