package com.xunao.diaodiao.Fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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

import com.bumptech.glide.Glide;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.gzfgeh.viewpagecycle.BannerInfo;
import com.gzfgeh.viewpagecycle.ImageCycleView;
import com.xunao.diaodiao.Activity.DocActivity;
import com.xunao.diaodiao.Activity.FindProjectActivity;
import com.xunao.diaodiao.Activity.HelpActivity;
import com.xunao.diaodiao.Activity.JoinActivity;
import com.xunao.diaodiao.Activity.SearchResultActivity;
import com.xunao.diaodiao.Activity.WebViewActivity;
import com.xunao.diaodiao.Bean.HomeProjBean;
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

public class HomeFragment extends BaseFragment implements HomeView, View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
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
    @BindView(R.id.recycler_view_proj)
    RecyclerView recyclerViewProj;
    @BindView(R.id.recycler_view_classic)
    RecyclerView recyclerViewClassic;
    @BindView(R.id.recycler_view_list)
    RecyclerView recyclerViewList;
    @BindView(R.id.banner)
    ImageView banner;
    @BindView(R.id.image_cycle_view)
    ImageCycleView imageCycleView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @Inject
    HomePresenter presenter;
    @BindView(R.id.document)
    TextView document;

    private String latData;
    private String lngData;
    private HomeResponseBean homeResponseBean;

    private int[] projImage = {R.drawable.icon_zhaoxiangmu, R.drawable.icon_zhaolingong, R.drawable.icon_zhaoweibao,
            R.drawable.icon_huzhuxinxi, R.drawable.icon_janlixinxi, R.drawable.icon_shangjia};

    private RecyclerArrayAdapter<HomeProjBean> projAdapter;
    private RecyclerArrayAdapter<HomeResponseBean.Project> adapter;
    private RecyclerArrayAdapter<HomeResponseBean.Project> adapterSkill;
    private RecyclerArrayAdapter<HomeResponseBean.Project> adapterList;

    public static HomeFragment newInstance(String param1) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
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
                    String[] data = s.split("#");
                    locationAdd.setText(data[0]);
                    if (!TextUtils.isEmpty(data[1]) && !TextUtils.isEmpty(data[2])) {
                        latData = data[1];
                        lngData = data[2];
                        if (homeResponseBean == null)
                            presenter.getFirstPage(latData, lngData);
                    }

                });

        moreOne.setOnClickListener(this);
        moreTwo.setOnClickListener(this);
        moreThree.setOnClickListener(this);

        document.setOnClickListener(v -> {
            DocActivity.startActivity(getContext());
        });

        List<HomeProjBean> homeProjBeanList = new ArrayList<>();
        for (int i = 0; i < projImage.length; i++) {
            HomeProjBean homeProjBean = new HomeProjBean();
            homeProjBean.setProjImage(projImage[i]);
            homeProjBean.setProjText(getResources().getStringArray(R.array.homeProj)[i]);
            homeProjBeanList.add(homeProjBean);
        }

        projAdapter = new RecyclerArrayAdapter<HomeProjBean>(getContext(), R.layout.home_proj_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, HomeProjBean homeProjBean) {
                baseViewHolder.setImageResource(R.id.proj_image, homeProjBean.getProjImage());
                baseViewHolder.setText(R.id.proj_text, homeProjBean.getProjText());
            }
        };

        projAdapter.setOnItemClickListener((view1, i) -> {
            switch (i) {
                case 0:
                    FindProjectActivity.startActivity(HomeFragment.this.getActivity(), 0);
                    break;
                case 1:
                    FindProjectActivity.startActivity(HomeFragment.this.getActivity(), 1);
                    break;
                case 2:
                    FindProjectActivity.startActivity(HomeFragment.this.getActivity(), 2);
                    break;
                case 3:
                    HelpActivity.startActivity(HomeFragment.this.getActivity());
                    break;
                case 4:
                    FindProjectActivity.startActivity(HomeFragment.this.getActivity(), 4);
                    break;
                case 5:
                    JoinActivity.startActivity(HomeFragment.this.getActivity());
                    break;
            }
        });

        recyclerViewProj.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerViewProj.setAdapter(projAdapter);
        projAdapter.addAll(homeProjBeanList);


        adapter = new RecyclerArrayAdapter<HomeResponseBean.Project>(getContext(), R.layout.home_vertical_list) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, HomeResponseBean.Project s) {
                baseViewHolder.setText(R.id.item_content, s.getTitle());
                baseViewHolder.setText(R.id.address, s.getDesc());
                baseViewHolder.setText(R.id.time, Utils.strToDateLong(Long.valueOf(s.getBuild_time())));
                baseViewHolder.setText(R.id.name, s.getType());
                baseViewHolder.setText(R.id.distance, s.getDistance());
                baseViewHolder.setText(R.id.price, s.getPrice());
            }
        };

        adapterSkill = new RecyclerArrayAdapter<HomeResponseBean.Project>(getContext(), R.layout.home_vertical_list) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, HomeResponseBean.Project s) {
                baseViewHolder.setText(R.id.item_content, s.getTitle());
                baseViewHolder.setText(R.id.address, s.getDesc());
                baseViewHolder.setText(R.id.time, Utils.strToDateLong(Long.valueOf(s.getBuild_time())));
                baseViewHolder.setText(R.id.name, s.getType());
                baseViewHolder.setText(R.id.distance, s.getDistance());
                baseViewHolder.setText(R.id.price, s.getPrice());
            }
        };

        adapterList = new RecyclerArrayAdapter<HomeResponseBean.Project>(getContext(), R.layout.home_vertical_list) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, HomeResponseBean.Project s) {
                baseViewHolder.setText(R.id.item_content, s.getTitle());
                baseViewHolder.setText(R.id.address, s.getDesc());
                baseViewHolder.setText(R.id.time, Utils.strToDateLong(Long.valueOf(s.getBuild_time())));
                baseViewHolder.setText(R.id.name, s.getType());
                baseViewHolder.setText(R.id.distance, s.getDistance());
                baseViewHolder.setText(R.id.price, s.getPrice());
            }
        };


        adapter.setOnItemClickListener((view1, i) -> {
        });

        recyclerViewClassic.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerViewClassic.setAdapter(adapterSkill);

        recyclerViewList.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerViewList.setAdapter(adapterList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onFailure() {
    }

    @Override
    public void getData(HomeResponseBean bean) {
        homeResponseBean = bean;
        List<BannerInfo> bannerInfos = new ArrayList<>();
        for (int i = 0; i < bean.getCarousel().size(); i++) {
            BannerInfo info = new BannerInfo();
            info.setImg(bean.getCarousel().get(i).getImage());
            info.setLink(bean.getCarousel().get(i).getLink());
            info.setOt(i);
            bannerInfos.add(info);
        }

        imageCycleView.setImageResources(bannerInfos, (bannerInfo, i, view) -> {
            WebViewActivity.startActivity(HomeFragment.this.getContext(),
                    bean.getCarousel().get(i).getLink(), bean.getCarousel().get(i).getName());
        });

        adapter.clear();
        adapter.addAll(bean.getProject());

        adapterSkill.clear();
        adapterSkill.addAll(bean.getOdd());

        adapterList.clear();
        adapterList.addAll(bean.getMaintenance());

        Glide.with(this).load(bean.getAdvertisement().get(0).getImage())
                .placeholder(R.drawable.banner02)
                .into(banner);
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
