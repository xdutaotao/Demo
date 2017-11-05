package com.xunao.diaodiao.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.bumptech.glide.Glide;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.gzfgeh.iosdialog.IOSDialog;
import com.gzfgeh.swipeheader.SwipeRefreshLayout;
import com.gzfgeh.viewpagecycle.BannerInfo;
import com.gzfgeh.viewpagecycle.ImageCycleView;
import com.xunao.diaodiao.Activity.DocActivity;
import com.xunao.diaodiao.Activity.FindProjectActivity;
import com.xunao.diaodiao.Activity.HelpActivity;
import com.xunao.diaodiao.Activity.HomeSearchActivity;
import com.xunao.diaodiao.Activity.JoinActivity;
import com.xunao.diaodiao.Activity.LoginActivity;
import com.xunao.diaodiao.Activity.SearchResultActivity;
import com.xunao.diaodiao.Activity.WebViewActivity;
import com.xunao.diaodiao.Activity.WebViewDetailActivity;
import com.xunao.diaodiao.Activity.WebViewOutActivity;
import com.xunao.diaodiao.Bean.CitiesBean;
import com.xunao.diaodiao.Bean.HomeProjBean;
import com.xunao.diaodiao.Bean.HomeResponseBean;
import com.xunao.diaodiao.Bean.SearchBean;
import com.xunao.diaodiao.Bean.UpdateInfo;
import com.xunao.diaodiao.Common.ApiConstants;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.MainActivity;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.HomePresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.LocationUtils;
import com.xunao.diaodiao.Utils.PermissionsUtils;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.RxUtils;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.HomeView;
import com.xunao.diaodiao.Widget.DownloadDialog.DownloadDialogFactory;
import com.zhy.base.fileprovider.FileProvider7;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;
import static com.xunao.diaodiao.Common.Constants.LOGIN_AGAIN;
import static com.xunao.diaodiao.Common.Constants.TYPE_KEY;
import static com.xunao.diaodiao.Common.Constants.city;
import static com.xunao.diaodiao.Common.Constants.latData;
import static com.xunao.diaodiao.Common.Constants.lngData;
import static com.xunao.diaodiao.Common.Constants.selectCity;

public class HomeFragment extends BaseFragment implements HomeView, View.OnClickListener, android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener, GeocodeSearch.OnGeocodeSearchListener
{
    private static final String ARG_PARAM1 = "param1";
    public static final int REQUEST_KEY = 8888;
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
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;

    private HomeResponseBean homeResponseBean;

    private File file;
    private String url;

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

                    if (data.length > 1 && !TextUtils.isEmpty(data[1]) && !TextUtils.isEmpty(data[2])) {
                        latData = data[1];
                        lngData = data[2];
                        if (homeResponseBean == null) {
                            locationAdd.setText(data[0]);
                            presenter.getFirstPage(latData, lngData);
                        }
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
                    //ToastUtil.show("敬请期待");
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
                baseViewHolder.setText(R.id.time, s.getIssue_time());
                baseViewHolder.setText(R.id.name, s.getType());
                baseViewHolder.setText(R.id.distance, s.getDistance());
                baseViewHolder.setText(R.id.price, " ￥ "+s.getPrice());
            }
        };

        adapter.setOnItemClickListener((view1, i) -> {
            WebViewActivity.startActivity(HomeFragment.this.getContext(), adapter.getAllData().get(i).getUrl(),
                    adapter.getAllData().get(i).getId(),
                    WebViewActivity.HOME_DETAIL, adapter.getAllData().get(i).getCollected());
        });

        adapterSkill = new RecyclerArrayAdapter<HomeResponseBean.Project>(getContext(), R.layout.home_vertical_list) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, HomeResponseBean.Project s) {
                baseViewHolder.setText(R.id.item_content, s.getTitle());
                baseViewHolder.setText(R.id.address, s.getDesc());
                baseViewHolder.setText(R.id.time, s.getIssue_time());
                baseViewHolder.setText(R.id.name, s.getType());
                baseViewHolder.setText(R.id.distance, s.getDistance());
                baseViewHolder.setText(R.id.price, " ￥ "+s.getPrice()+"/天");
            }
        };

        adapterSkill.setOnItemClickListener((view1, i) -> {
            WebViewActivity.startActivity(HomeFragment.this.getContext(), adapterSkill.getAllData().get(i).getUrl(),
                    adapterSkill.getAllData().get(i).getId(),
                    WebViewActivity.HOME_SKILL_DETAIL);
        });

        adapterList = new RecyclerArrayAdapter<HomeResponseBean.Project>(getContext(), R.layout.home_vertical_list) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, HomeResponseBean.Project s) {
                baseViewHolder.setText(R.id.item_content, s.getTitle());
                baseViewHolder.setText(R.id.address, s.getDesc());
                baseViewHolder.setText(R.id.time, s.getIssue_time());
                baseViewHolder.setText(R.id.name, s.getType());
                baseViewHolder.setText(R.id.distance, s.getDistance());
                baseViewHolder.setText(R.id.price, " ￥ "+s.getPrice());
            }
        };

        adapterList.setOnItemClickListener((view1, i) -> {
            WebViewActivity.startActivity(HomeFragment.this.getContext(),
                    adapterList.getAllData().get(i).getUrl(),
                    adapterList.getAllData().get(i).getId(),
                    WebViewActivity.HOME_WEIBAO_DETAIL);
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

        locationAdd.setOnClickListener(v -> {
            SearchResultActivity.startActivityForResult(HomeFragment.this, locationAdd.getText().toString());
        });

        swipe.setOnRefreshListener(this);


        editText.setFocusable(false);
        editText.setOnClickListener(v -> {
            HomeSearchActivity.startActivity(HomeFragment.this.getContext());
        });

        moreOne.setOnClickListener(v -> {
            //项目
            FindProjectActivity.startActivity(HomeFragment.this.getContext(), 0);
        });

        moreTwo.setOnClickListener(v -> {
            //零工
            FindProjectActivity.startActivity(HomeFragment.this.getContext(), 1);
        });

        moreThree.setOnClickListener(v -> {

        });

        presenter.getUpdateVersion();
        presenter.checkApp();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getFirstPage(latData, lngData);
    }

    @Override
    public void onRefresh() {
        locationAdd.setText(Constants.selectCity);
        //刷新切换城市
         city = selectCity;
        presenter.getFirstPage(latData, lngData);


    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if(presenter != null)
                presenter.getFirstPage(latData, lngData);
        }
    }


    private void changeAddress(String address){
        locationAdd.setText(address);
        Constants.city = address;
        GeocodeSearch geocoderSearch = new GeocodeSearch(getContext());
        geocoderSearch.setOnGeocodeSearchListener(this);
        GeocodeQuery query = new GeocodeQuery(address, Utils.stringToPinyin(address));
        geocoderSearch.getFromLocationNameAsyn(query);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        if (geocodeResult.getGeocodeAddressList() != null && geocodeResult.getGeocodeAddressList().size()>0){
            LatLonPoint point =  geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
            presenter.getFirstPage(getContext(), String.valueOf(point.getLatitude()),
                    String.valueOf(point.getLongitude()));
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_KEY){
                changeAddress(data.getStringExtra(INTENT_KEY));
            }
        }
    }

    @Override
    public void onFailure() {
        swipe.setRefreshing(false);
    }

    @Override
    public void getData(HomeResponseBean bean) {
        homeResponseBean = bean;
        swipe.setRefreshing(false);
        if(bean.getCarousel() != null){
            List<BannerInfo> bannerInfos = new ArrayList<>();
            for (int i = 0; i < bean.getCarousel().size(); i++) {
                BannerInfo info = new BannerInfo();
                info.setImg(bean.getCarousel().get(i).getImage());
                info.setLink(bean.getCarousel().get(i).getLink());
                info.setOt(i);
                bannerInfos.add(info);
            }

            imageCycleView.setImageResources(bannerInfos, (bannerInfo, i, view) -> {


                if(bean.getCarousel().get(i).getType() == 1){
                    WebViewOutActivity.startActivity(HomeFragment.this.getContext(),
                            bean.getCarousel().get(i).getLink());
                }else{
                    WebViewDetailActivity.startActivity(HomeFragment.this.getContext(),
                            bean.getCarousel().get(i));
                }
            });
        }

        if(bean.getProject() != null){
            adapter.clear();
            adapter.addAll(bean.getProject());
        }

        if(bean.getOdd() != null){
            adapterSkill.clear();
            adapterSkill.addAll(bean.getOdd());
        }

        if(bean.getMaintenance() != null){
            adapterList.clear();
            adapterList.addAll(bean.getMaintenance());
        }

        if(bean.getAdvertisement() != null){
            Glide.with(this).load(bean.getAdvertisement().get(0).getImage())
                    .placeholder(R.drawable.banner02)
                    .into(banner);
        }else{
            Glide.with(this).load("")
                    .placeholder(R.drawable.banner02)
                    .into(banner);
        }

        banner.setOnClickListener(v -> {
            if(bean.getAdvertisement().get(0).getType() == 1){
                WebViewOutActivity.startActivity(HomeFragment.this.getContext(),
                        bean.getAdvertisement().get(0).getLink());
            }else{
                WebViewDetailActivity.startActivity(HomeFragment.this.getContext(),
                        bean.getAdvertisement().get(0));
            }
        });


    }

    @Override
    public void getData(String bean) {
        //ToastUtil.show(bean);
        if(!TextUtils.equals(bean, "1")){
            System.exit(0);
        }
    }


    @Override
    public void getData(UpdateInfo s) {
        if (TextUtils.equals(s.getVersion(), Utils.getVersionCode())){
            return;
        }else{
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), ApiConstants.APPFILENAME);
            url = s.getDownload_url();
            if (url != null) {   //有更新的包
                new IOSDialog(HomeFragment.this.getContext()).builder()
                        .setTitle("更新版本", null)
                        .setMsg(String.format(getString(R.string.get_new_version), s.getVersion()))
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", v -> {
                            if (!PermissionsUtils.hasPermission(HomeFragment.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                return;
                            }

                            DownloadDialogFactory.getDownloadDialogManager().showDialog(HomeFragment.this.getContext());
                            presenter.apkFileDownload(url, file);

                        })
                        .show();
            }
        }

    }

    @Override
    public void getProgress(float progress) {
        DownloadDialogFactory.getDownloadDialogManager().showProgress(progress);
        if (progress == 1.0f) {
            DownloadDialogFactory.getDownloadDialogManager().dismissDialog();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Utils.startActionFile(HomeFragment.this.getContext(), file);

                File file = new File(Environment.getExternalStorageDirectory(),
                        ApiConstants.APPFILENAME);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                // 仅需改变这一行
                FileProvider7.setIntentDataAndType(this.getContext(),
                        intent, "application/vnd.android.package-archive", file, true);
                startActivity(intent);

            }else {
                Utils.installApk(HomeFragment.this.getContext(), file);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // TODO: 2016/12/26 grantResults[0]
        if (grantResults != null && grantResults.length > 0) {
            switch (requestCode) {
                //权限申请成功
                case PermissionsUtils.WRITE_EXTERNAL_STORAGE_CODE:
                    //showDownloadDialog();
                    DownloadDialogFactory.getDownloadDialogManager().showDialog(HomeFragment.this.getContext());
                    presenter.apkFileDownload(url, file);
                    break;
            }
        } else {
            PermissionsUtils.permissionNotice(HomeFragment.this.getActivity(), requestCode);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onClick(View v) {
    }



}
