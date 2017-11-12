package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.zhouwei.library.CustomPopWindow;
import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.gzfgeh.iosdialog.IOSDialog;
import com.xunao.diaodiao.Bean.FindProjReq;
import com.xunao.diaodiao.Bean.FindProjectRes;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.JoinPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.JoinView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qqtheme.framework.entity.Area;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;

import static com.xunao.diaodiao.Common.Constants.TYPE_KEY;
import static com.xunao.diaodiao.Common.Constants.addressResult;

/**
 * create by
 */
public class JoinActivity extends BaseActivity implements JoinView, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {

    @Inject
    JoinPresenter presenter;
    @BindView(R.id.type_image)
    ImageView typeImage;
    @BindView(R.id.edit_text)
    EditText editText;
    @BindView(R.id.cancle_action)
    ImageView cancleAction;
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;
    @BindView(R.id.back_icon)
    ImageView backIcon;
    @BindView(R.id.point_layout)
    LinearLayout pointLayout;
    @BindView(R.id.point)
    TextView point;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.address_layout)
    LinearLayout addressLayout;
    @BindView(R.id.distance)
    TextView distance;
    @BindView(R.id.distance_layout)
    LinearLayout distanceLayout;

    private RecyclerArrayAdapter<FindProjectRes.FindProject> adapter;
    private RecyclerArrayAdapter<String> textAdapter;
    private int page = 1;
    private FindProjReq req = new FindProjReq();
    private List<String> addressList = new ArrayList<>();
    private CustomPopWindow popWindow;
    private List<County> cityList = new ArrayList<>();
    private String selectCity;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, JoinActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        backIcon.setOnClickListener(v -> {
            finish();
        });

        back.setOnClickListener(v -> {
            if (TextUtils.isEmpty(User.getInstance().getUserId())) {
                ToastUtil.show("请登录");
                return;
            }

            if (ShareUtils.getValue(TYPE_KEY, 0) == 0) {
                ToastUtil.show("请选择角色");
                return;
            }

            RxBus.getInstance().post("release");
            finish();

        });

        pointLayout.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(req.getSort())) {
                point.setTextColor(getResources().getColor(R.color.nav_gray));
                req.setSort("");
            } else {
                point.setTextColor(getResources().getColor(R.color.colorAccent));
                req.setSort("desc");
            }

            page = 1;
            req.setPage(page);
            presenter.businesses(JoinActivity.this, req);
        });

        adapter = new RecyclerArrayAdapter<FindProjectRes.FindProject>(this, R.layout.join_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, FindProjectRes.FindProject homeBean) {
                baseViewHolder.setText(R.id.item_content, homeBean.getName());
                RatingBar bar = (RatingBar) baseViewHolder.getConvertView().findViewById(R.id.rating_star);
                bar.setIsIndicator(true);
                try {
                    bar.setRating(Float.valueOf(homeBean.getUser_point()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                baseViewHolder.setText(R.id.time, homeBean.getUser_point());
                baseViewHolder.setText(R.id.address, homeBean.getAddress());
                baseViewHolder.setText(R.id.days, homeBean.getTel());

                baseViewHolder.setOnClickListener(R.id.request, v -> {
                    new IOSDialog(JoinActivity.this).builder()
                            .setMsg(homeBean.getTel())
                            .setNegativeButton("呼叫", v1 -> {
                                Utils.startCallActivity(JoinActivity.this, homeBean.getTel());
                            })
                            .setPositiveButton("取消", null)
                            .show();
                });
            }
        };

        adapter.setOnItemClickListener((view, i) -> {
            if(TextUtils.isEmpty(User.getInstance().getUserId())){
                ToastUtil.show("请登录");
                return;
            }
            JoinDetailActivity.startActivity(this, adapter.getAllData().get(i).getId());
        });

        textAdapter = new RecyclerArrayAdapter<String>(this, R.layout.select_skill_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setText(R.id.skill_text, s);
                if (TextUtils.equals(selectCity, s)) {
                    baseViewHolder.setBackgroundRes(R.id.skill_text, R.drawable.btn_blue_bg);
                    baseViewHolder.setTextColorRes(R.id.skill_text, R.color.white);
                } else {
                    baseViewHolder.setBackgroundRes(R.id.skill_text, R.drawable.btn_blank_bg);
                    baseViewHolder.setTextColorRes(R.id.skill_text, R.color.gray);
                }

                baseViewHolder.setOnClickListener(R.id.skill_text, v -> {
                    selectCity = s;
                    address.setText(selectCity);
                    popWindow.dissmiss();
                    for(County city: cityList){
                        if(TextUtils.equals(city.getAreaName(), selectCity)){
                            req.setDistrict(Integer.valueOf(city.getAreaId()));
                            page = 1;
                            presenter.businesses(JoinActivity.this ,req);
                            return;
                        }
                    }

                });

            }
        };

        recyclerView.setAdapterDefaultConfig(adapter, this, this);
        onRefresh();

        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                req.setKeyword(editText.getText().toString());
                page = 1;
                req.setPage(1);
                presenter.businesses(this, req);
                return true;
            } else {
                return false;
            }
        });
        req.setKeyword("");

        if(Constants.addressResult.size() == 0){
            presenter.getAddressData(this);
        }else{
            getAddressData(Constants.addressResult);
        }

        View popView = LayoutInflater.from(this).inflate(R.layout.single_help_recycler, null);
        RecyclerView popRecyclerView = (RecyclerView) popView.findViewById(R.id.recycler_view);
        popRecyclerView.setAdapter(textAdapter);
        addressLayout.setOnClickListener(v -> {
            textAdapter.notifyDataSetChanged();
            popWindow = new CustomPopWindow.PopupWindowBuilder(JoinActivity.this)
                    .setView(popView)
                    .create()
                    .showAsDropDown(addressLayout, 0, 10);
        });

        distanceLayout.setOnClickListener(v -> {
            if (req.getNearby() == 0){
                distance.setTextColor(getResources().getColor(R.color.colorAccent));
                req.setNearby(1);
            }else{
                distance.setTextColor(getResources().getColor(R.color.nav_gray));
                req.setNearby(0);
            }

            page = 1;
            req.setPage(1);
            presenter.businesses(this, req);
        });


    }

    @Override
    public void onRefresh() {
        page = 1;
        req.setPage(page);
        req.setPageSize(10);
        req.setLat(Constants.latData);
        req.setLng(Constants.lngData);
        req.setCity(Constants.city);
        presenter.businesses(this, req);
    }

    @Override
    public void onLoadMore() {
        page++;
        req.setPage(page);
        presenter.businesses(this, req);
    }

    @Override
    public void onFailure() {
        if (adapter.getAllData().size() == 0) {
            recyclerView.showEmpty();
        } else {
            adapter.stopMore();
        }
    }

    @Override
    public void getData(FindProjectRes s) {
        if (page == 1) {
            adapter.clear();
        }
        adapter.addAll(s.getInfo());
    }

    @Override
    public void getAddressData(ArrayList<Province> res) {
        Constants.addressResult.addAll(res);
        for(Province province : res){
            if(TextUtils.equals(province.getAreaName(), Constants.city)){
                if(province.getAreaName().contains("市")){
                    //比如上海市  继续找
                    for(City city : province.getCities()){
                        if(TextUtils.equals(city.getAreaName(), Constants.city)){
                            cityList.addAll(city.getCounties());
                            for(County county: city.getCounties()){
                                addressList.add(county.getAreaName());
                            }
                            textAdapter.addAll(addressList);
                            address.setText(addressList.get(0));
                            selectCity = address.getText().toString();
                            return;
                        }
                    }
                }

            }
        }
    }


}
