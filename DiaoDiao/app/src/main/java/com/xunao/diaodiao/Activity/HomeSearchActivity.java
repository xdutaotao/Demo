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
import android.widget.TextView;

import com.example.zhouwei.library.CustomPopWindow;
import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.FindProjReq;
import com.xunao.diaodiao.Bean.FindProjectRes;
import com.xunao.diaodiao.Bean.GetMoneyReq;
import com.xunao.diaodiao.Bean.HomeSearchRes;
import com.xunao.diaodiao.Fragment.HomeFragment;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.HomeSearchPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.View.HomeSearchView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;
import static com.xunao.diaodiao.Common.Constants.TYPE_KEY;
import static com.xunao.diaodiao.Common.Constants.address;
import static com.xunao.diaodiao.Common.Constants.latData;
import static com.xunao.diaodiao.Common.Constants.lngData;

/**
 * create by
 */
public class HomeSearchActivity extends BaseActivity implements HomeSearchView, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {

    @Inject
    HomeSearchPresenter presenter;
    @BindView(R.id.back_icon)
    ImageView backIcon;
    @BindView(R.id.type_image)
    ImageView typeImage;
    @BindView(R.id.edit_text)
    EditText editText;
    @BindView(R.id.cancle_action)
    ImageView cancleAction;
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.near)
    TextView near;
    @BindView(R.id.near_layout)
    LinearLayout nearLayout;
    @BindView(R.id.proj_type)
    TextView projType;
    @BindView(R.id.proj_type_layout)
    LinearLayout projTypeLayout;
    @BindView(R.id.proj_time)
    TextView projTime;
    @BindView(R.id.proj_time_layout)
    LinearLayout projTimeLayout;
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;

    private FindProjReq req;
    private int page = 1;

    private String projectType="";
    private String timeType="";

    private RecyclerArrayAdapter<HomeSearchRes.ProjectBean> adapter;
    private RecyclerArrayAdapter<String> textAdapter;
    private RecyclerArrayAdapter<String> projAdapter;
    private CustomPopWindow popWindow;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HomeSearchActivity.class);
        //intent.putExtra(INTENT_KEY, key);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_search);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        backIcon.setOnClickListener(v -> {
            finish();
        });

        back.setVisibility(View.INVISIBLE);

        adapter = new RecyclerArrayAdapter<HomeSearchRes.ProjectBean>(this, R.layout.home_vertical_list) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, HomeSearchRes.ProjectBean homeBean) {
                baseViewHolder.setText(R.id.item_content, homeBean.getTitle());
                baseViewHolder.setText(R.id.address, homeBean.getDesc());
                baseViewHolder.setText(R.id.time, homeBean.getIssue_time());
                baseViewHolder.setText(R.id.name, homeBean.getType());
                baseViewHolder.setText(R.id.distance, homeBean.getDistance());
                if (homeBean.getProject_type() == 1) {
                    //项目
                    baseViewHolder.setText(R.id.price, " ￥ " + homeBean.getPrice());
                } else if (homeBean.getProject_type() == 2) {
                    //监理
                    //baseViewHolder.setText(R.id.price_text, "共" + homeBean.getTotal_day() + "天");
                    baseViewHolder.setText(R.id.price, " ￥ " + homeBean.getPrice() + " / 天");
                } else if (homeBean.getProject_type() == 3) {
                    //零工
                    baseViewHolder.setText(R.id.price_text, "上门费");
                    baseViewHolder.setText(R.id.price, " ￥ " + homeBean.getPrice() + " / 天");
                }else if (homeBean.getProject_type() == 4){
                    //维保

                }else if (homeBean.getProject_type() == 5){
                    //资料库
                }

            }
        };

        adapter.setOnItemClickListener((view, i) -> {
            if (!TextUtils.isEmpty(User.getInstance().getUserId())) {
                WebViewActivity.startActivity(this, adapter.getAllData().get(i).getUrl(),
                        Integer.valueOf(adapter.getAllData().get(i).getId()), WebViewActivity.HOME_DETAIL);

            } else {
                LoginActivity.startActivity(HomeSearchActivity.this);
            }

        });

        recyclerView.setAdapterDefaultConfig(adapter, this, this);

        textAdapter = new RecyclerArrayAdapter<String>(this, R.layout.single_recycler_item_pop) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setText(R.id.text, s);
                if (TextUtils.equals(timeType, s)){
                    baseViewHolder.setTextColorRes(R.id.text, R.color.colorAccent);
                }else{
                    baseViewHolder.setTextColorRes(R.id.text, R.color.nav_gray);
                }
            }
        };

        textAdapter.setOnItemClickListener((view, i) -> {
            popWindow.dissmiss();

            if (i == req.getTime_type()){
                projTime.setTextColor(getResources().getColor(R.color.nav_gray));
                req.setTime_type(0);
                timeType="";
            }else{
                projTime.setTextColor(getResources().getColor(R.color.colorAccent));
                req.setTime_type(i);
                timeType = textAdapter.getAllData().get(i);
            }

            page = 1;
            req.setPage(page);
            presenter.indexSearch(HomeSearchActivity.this, req);

        });

        projAdapter = new RecyclerArrayAdapter<String>(this, R.layout.single_recycler_item_pop) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setText(R.id.text, s);

                if (TextUtils.equals(projectType, s)){
                    baseViewHolder.setTextColorRes(R.id.text, R.color.colorAccent);
                }else{
                    baseViewHolder.setTextColorRes(R.id.text, R.color.nav_gray);
                }
            }
        };

        projAdapter.setOnItemClickListener((view, i) -> {
            popWindow.dissmiss();
            if (TextUtils.equals(projAdapter.getAllData().get(i), projectType)){
                projType.setTextColor(getResources().getColor(R.color.nav_gray));
                projectType = "";
            }else{
                projType.setTextColor(getResources().getColor(R.color.colorAccent));
                projectType = projAdapter.getAllData().get(i);
            }

            page = 1;
            req.setPage(page);
            req.setType(i+1);
            presenter.indexSearch(HomeSearchActivity.this, req);

        });

        View popView = LayoutInflater.from(this).inflate(R.layout.single_recycler_pop, null);
        RecyclerView popRecyclerView = (RecyclerView) popView.findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        popRecyclerView.setLayoutManager(manager);
        popRecyclerView.setAdapter(textAdapter);

        List<String> timeData = new ArrayList<>();
        timeData.add("无");
        timeData.add("0 - 3");
        timeData.add("4 - 7");
        timeData.add("7以上");
        textAdapter.addAll(timeData);
        projTimeLayout.setOnClickListener(v -> {
            popRecyclerView.setAdapter(textAdapter);
            if (TextUtils.equals(timeType, "")){
                projTime.setTextColor(getResources().getColor(R.color.nav_gray));
            }else{
                projTime.setTextColor(getResources().getColor(R.color.colorAccent));
            }

            popWindow = new CustomPopWindow.PopupWindowBuilder(HomeSearchActivity.this)
                    .setView(popView)
                    .create()
                    .showAsDropDown(projTimeLayout, 0, 10);
        });


        nearLayout.setOnClickListener(v -> {
            if (req.getNearby() == 0){
                near.setTextColor(getResources().getColor(R.color.colorAccent));
                req.setNearby(1);
            }else{
                near.setTextColor(getResources().getColor(R.color.nav_gray));
                req.setNearby(0);
            }
            page = 1;
            req.setPage(page);
            presenter.indexSearch(HomeSearchActivity.this, req);
        });

        List<String> typeData = new ArrayList<>();
        typeData.add("项目");
        typeData.add("监理");
        typeData.add("零工");
        typeData.add("维保");
        typeData.add("资料库");
        projAdapter.addAll(typeData);
        projTypeLayout.setOnClickListener(v -> {
            popRecyclerView.setAdapter(projAdapter);

            if (TextUtils.equals(projectType, "")){
                projType.setTextColor(getResources().getColor(R.color.nav_gray));
            }else{
                projType.setTextColor(getResources().getColor(R.color.colorAccent));
            }

            popWindow = new CustomPopWindow.PopupWindowBuilder(HomeSearchActivity.this)
                    .setView(popView)
                    .create()
                    .showAsDropDown(projTypeLayout, 0, 10);
        });


        req = new FindProjReq();
        req.setLat(latData);
        req.setLng(lngData);
        req.setKeyword("");
        req.setPageSize(10);
        req.setPage(page);
        req.setUserid(0);
        req.setUsertype(ShareUtils.getValue(TYPE_KEY, 0));
        req.setNearby(0);
        req.setTime_type(0);
        req.setType(0);
        req.setProject_type(0);
        onRefresh();

        editText.setText(getIntent().getStringExtra(INTENT_KEY));
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId== EditorInfo.IME_ACTION_SEARCH ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER)){
                req.setKeyword(editText.getText().toString());
                page = 1;
                req.setPage(1);
                presenter.indexSearch(this, req);
                return true;
            }else{
                return false;
            }
        });
    }

    @Override
    public void onRefresh() {
        page = 1;
        req.setPage(page);
        presenter.indexSearch(this, req);
    }

    @Override
    public void onLoadMore() {
        page++;
        req.setPage(page);
        presenter.indexSearch(this, req);
    }

    @Override
    public void getData(HomeSearchRes res) {
        if (page == 1) {
            adapter.clear();
        }
        adapter.addAll(res.getProject());
    }

    @Override
    public void onFailure() {
        recyclerView.showEmpty();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }


}
