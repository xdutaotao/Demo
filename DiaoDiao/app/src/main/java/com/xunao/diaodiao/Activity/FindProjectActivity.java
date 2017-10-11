package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
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
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.FindProjectPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.FindProjectView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;
import static com.xunao.diaodiao.Common.Constants.latData;
import static com.xunao.diaodiao.Common.Constants.lngData;

/**
 *
 * https://github.com/pinguo-zhouwei/CustomPopwindow
 * create by
 */
public class FindProjectActivity extends BaseActivity implements FindProjectView, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {

    @Inject
    FindProjectPresenter presenter;
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

    private RecyclerArrayAdapter<FindProjectRes.FindProject> adapter;
    private RecyclerArrayAdapter<String> textAdapter;

    private int page;
    private FindProjReq req = new FindProjReq();
    private int type;
    private CustomPopWindow popWindow;

    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, FindProjectActivity.class);
        intent.putExtra(INTENT_KEY, type);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_project);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        type = getIntent().getIntExtra(INTENT_KEY, 0);
        backIcon.setOnClickListener(v -> {
            finish();
        });

        back.setOnClickListener(v -> {
            if (type == 0){
                //项目
                ReleaseProjActivity.startActivity(FindProjectActivity.this);
            }else if (type == 1){
                //零工
                ReleaseSkillActivity.startActivity(FindProjectActivity.this);
            }
        });

        adapter = new RecyclerArrayAdapter<FindProjectRes.FindProject>(this, R.layout.home_vertical_list) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, FindProjectRes.FindProject homeBean) {
                baseViewHolder.setText(R.id.item_content, homeBean.getTitle());
                baseViewHolder.setText(R.id.address, homeBean.getDesc());
                baseViewHolder.setText(R.id.time, homeBean.getIssue_time());
                baseViewHolder.setText(R.id.name, homeBean.getType());
                baseViewHolder.setText(R.id.distance, homeBean.getDistance());
                if (type == 0) {
                    baseViewHolder.setText(R.id.price, " ￥ " + homeBean.getPrice());
                } else if (type == 1) {
                    baseViewHolder.setText(R.id.price_text, "共" + homeBean.getTotal_day() + "天");
                    baseViewHolder.setText(R.id.price, " ￥ " + homeBean.getPrice() + " / 天");
                } else if (type == 2) {
                    baseViewHolder.setText(R.id.price_text, "上门费");
                    baseViewHolder.setText(R.id.price, " ￥ " + homeBean.getPrice() + " / 天");
                }

            }
        };

        adapter.setOnItemClickListener((view, i) -> {
            if (!TextUtils.isEmpty(User.getInstance().getUserId())) {
//                ProjectDetailActivity.startActivity(FindProjectActivity.this,
//                        adapter.getAllData().get(i).getId(), type);

//                WebViewActivity.startActivity(FindProjectActivity.this,
//                        "home_proj_detail.html",
//                        adapter.getAllData().get(i).getId(), type);
                //presenter.projectDetail(this, adapter.getAllData().get(i).getId(), 0);

                WebViewActivity.startActivity(this, adapter.getAllData().get(i).getUrl(),
                        adapter.getAllData().get(i).getId(), WebViewActivity.HOME_DETAIL);

            } else {
                LoginActivity.startActivity(FindProjectActivity.this);
            }

        });

        recyclerView.setAdapterDefaultConfig(adapter, this, this);

        textAdapter = new RecyclerArrayAdapter<String>(this, R.layout.single_recycler_item_pop) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setText(R.id.text, s);
            }
        };

        textAdapter.setOnItemClickListener((view, i) -> {
            popWindow.dissmiss();

            req.setLat(latData);
            req.setLng(lngData);
            page = 1;
            req.setPage(page);
            req.setNearby(0);
            req.setTime_type(i);
            req.setType("");
            presenter.getProjectList(FindProjectActivity.this, req, type);
        });

        List<String> timeData = new ArrayList<>();
        timeData.add("无");
        timeData.add("0 - 3");
        timeData.add("4 - 7");
        timeData.add("7以上");
        textAdapter.addAll(timeData);

        nearLayout.setOnClickListener(v -> {
            near.setTextColor(getResources().getColor(R.color.colorAccent));
            projType.setTextColor(getResources().getColor(R.color.nav_gray));
            projTime.setTextColor(getResources().getColor(R.color.nav_gray));

            //Drawable drawable = getResources().getDrawable(R.drawable.)
            req.setLat(latData);
            req.setLng(lngData);
            page = 1;
            req.setPage(page);
            req.setNearby(1);
            req.setTime_type(0);
            req.setType("");
            presenter.getProjectList(FindProjectActivity.this, req, type);
        });

        View popView = LayoutInflater.from(this).inflate(R.layout.single_recycler_pop, null);
        RecyclerView popRecyclerView = (RecyclerView) popView.findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        popRecyclerView.setLayoutManager(manager);
        popRecyclerView.setAdapter(textAdapter);

        projTimeLayout.setOnClickListener(v -> {
            near.setTextColor(getResources().getColor(R.color.nav_gray));
            projType.setTextColor(getResources().getColor(R.color.nav_gray));
            projTime.setTextColor(getResources().getColor(R.color.colorAccent));

            popWindow = new CustomPopWindow.PopupWindowBuilder(FindProjectActivity.this)
                    .setView(popView)
                    .create()
                    .showAsDropDown(projTimeLayout, 0, 10);
        });

        projTypeLayout.setOnClickListener(v -> {
            near.setTextColor(getResources().getColor(R.color.nav_gray));
            projType.setTextColor(getResources().getColor(R.color.colorAccent));
            projTime.setTextColor(getResources().getColor(R.color.nav_gray));

            req.setLat(latData);
            req.setLng(lngData);
            page = 1;
            req.setPage(page);
            req.setNearby(0);
            req.setTime_type(0);
            presenter.getProjectList(FindProjectActivity.this, req, type);

        });

        onRefresh();
    }

    @Override
    public void getData(FindProjectRes res) {
        if (page == 1) {
            adapter.clear();
        }

        if (type == 0){
            adapter.addAll(res.getProject());
        }else if (type == 1){
            adapter.addAll(res.getOdd());
        }

    }

    @Override
    public void getNoMore(String msg) {
        ToastUtil.show(msg);
        adapter.stopMore();
    }

    @Override
    public void getUrl(String url) {
        if (!TextUtils.isEmpty(url))
            WebViewActivity.startActivity(FindProjectActivity.this, url);
    }

    @Override
    public void onRefresh() {
        req.setLat(latData);
        req.setLng(lngData);
        page = 1;
        req.setPage(page);
        presenter.getProjectList(req, type);
    }

    @Override
    public void onLoadMore() {
        page++;
        req.setPage(page);
        presenter.getProjectList(req, type);
    }




    @Override
    public void onFailure() {
        adapter.stopMore();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }


}
