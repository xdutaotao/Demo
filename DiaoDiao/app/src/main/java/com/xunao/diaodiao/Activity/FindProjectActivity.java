package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.xunao.diaodiao.Bean.CheckFinishRes;
import com.xunao.diaodiao.Bean.FindProjReq;
import com.xunao.diaodiao.Bean.FindProjectRes;
import com.xunao.diaodiao.Bean.TypeInfoRes;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.FindProjectPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.FindProjectView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;
import static com.xunao.diaodiao.Common.Constants.TYPE_KEY;
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
    private RecyclerArrayAdapter<TypeInfoRes.Type_Info> projAdapter;

    private int page;
    private FindProjReq req = new FindProjReq();
    private int type;
    private CustomPopWindow popWindow;
    private int projectType = -1;
    private String timeType = "";

    private int status;

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
            if(TextUtils.isEmpty(User.getInstance().getUserId())){
                ToastUtil.show("请登录");
                return;
            }

            if(ShareUtils.getValue(TYPE_KEY, 0) == 0){
                ToastUtil.show("请选择角色");
                return;
            }

            RxBus.getInstance().post("release");
            finish();

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
                    //项目
                    baseViewHolder.setText(R.id.price, " ￥ " + homeBean.getPrice());
                } else if (type == 1) {
                    baseViewHolder.setText(R.id.price_text, "共" + homeBean.getTotal_day() + "天");
                    baseViewHolder.setText(R.id.price, " ￥ " + homeBean.getPrice() + " / 天");
                } else if (type == 2) {
                    baseViewHolder.setText(R.id.price_text, "上门费");
                    baseViewHolder.setText(R.id.price, " ￥ " + homeBean.getPrice());
                }else if(type == 4){
                    //监理
                    baseViewHolder.setText(R.id.price_text, "价格");
                    baseViewHolder.setText(R.id.price, " ￥ " + homeBean.getPrice());
                }

            }
        };

        adapter.setOnItemClickListener((view, i) -> {
            if (!TextUtils.isEmpty(User.getInstance().getUserId())) {
                if (type == 0){
                    WebViewActivity.startActivity(this,
                            adapter.getAllData().get(i), WebViewActivity.HOME_DETAIL);
                }else if(type == 1){
                    WebViewActivity.startActivity(this,
                            adapter.getAllData().get(i), WebViewActivity.HOME_SKILL_DETAIL);
                }else if(type == 2){
                    //维保
                    WebViewActivity.startActivity(this,
                            adapter.getAllData().get(i), WebViewActivity.HOME_WEIBAO_DETAIL);
                }else if(type == 4){
                    //监理
                    WebViewActivity.startActivity(this,
                            adapter.getAllData().get(i), WebViewActivity.HOME_JIANLI_DETAIL);
                }


            } else {
                LoginActivity.startActivity(FindProjectActivity.this);
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
                timeType="全部";
            }else{
                projTime.setTextColor(getResources().getColor(R.color.colorAccent));
                req.setTime_type(i);
                timeType = textAdapter.getAllData().get(i);
            }
            req.setLat(latData);
            req.setLng(lngData);
            page = 1;
            req.setPage(page);
            req.setTime_type(i);
            presenter.getProjectList(FindProjectActivity.this, req, type);
        });

        List<String> timeData = new ArrayList<>();
        timeData.add("全部");
        timeData.add("0 - 3 天");
        timeData.add("4 - 7 天");
        timeData.add("7天以上");
        textAdapter.addAll(timeData);

        View popView = LayoutInflater.from(this).inflate(R.layout.single_recycler_pop, null);
        RecyclerView popRecyclerView = (RecyclerView) popView.findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        popRecyclerView.setLayoutManager(manager);

        projTimeLayout.setOnClickListener(v -> {

            if (TextUtils.equals(timeType, "")){
                projTime.setTextColor(getResources().getColor(R.color.nav_gray));
            }else{
                projTime.setTextColor(getResources().getColor(R.color.colorAccent));
            }
            popRecyclerView.setAdapter(textAdapter);
            popWindow = new CustomPopWindow.PopupWindowBuilder(FindProjectActivity.this)
                    .setView(popView)
                    .create()
                    .showAsDropDown(projTimeLayout, 0, 10);
        });


        projAdapter = new RecyclerArrayAdapter<TypeInfoRes.Type_Info>(this, R.layout.single_recycler_item_pop) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, TypeInfoRes.Type_Info s) {
                baseViewHolder.setText(R.id.text, s.getTitle());

                if (projectType == Integer.valueOf(s.getId())){
                    baseViewHolder.setTextColorRes(R.id.text, R.color.colorAccent);
                }else{
                    baseViewHolder.setTextColorRes(R.id.text, R.color.nav_gray);
                }
            }
        };

        projAdapter.setOnItemClickListener((view, i) -> {
            popWindow.dissmiss();
            if (projectType == Integer.valueOf(projAdapter.getAllData().get(i).getId())){
                projType.setTextColor(getResources().getColor(R.color.nav_gray));
                projectType = -1;
            }else{
                projType.setTextColor(getResources().getColor(R.color.colorAccent));
                projectType = Integer.valueOf(projAdapter.getAllData().get(i).getId());
            }

            req.setLat(latData);
            req.setLng(lngData);
            page = 1;
            req.setPage(page);
            req.setType(Integer.valueOf(projAdapter.getAllData().get(i).getId()));
            presenter.getProjectList(FindProjectActivity.this, req, type);
        });

        projTypeLayout.setOnClickListener(v -> {
            if (projectType == -1){
                projType.setTextColor(getResources().getColor(R.color.nav_gray));
            }else{
                projType.setTextColor(getResources().getColor(R.color.colorAccent));
            }

            popRecyclerView.setAdapter(projAdapter);
            popWindow = new CustomPopWindow.PopupWindowBuilder(FindProjectActivity.this)
                    .setView(popView)
                    .create()
                    .showAsDropDown(projTypeLayout, 0, 10);
        });


        projAdapter = new RecyclerArrayAdapter<TypeInfoRes.Type_Info>(this, R.layout.single_recycler_item_pop) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, TypeInfoRes.Type_Info s) {
                baseViewHolder.setText(R.id.text, s.getTitle());
                if (projectType == Integer.valueOf(s.getId())){
                    baseViewHolder.setTextColorRes(R.id.text, R.color.colorAccent);
                }
            }
        };

        projAdapter.setOnItemClickListener((view, i) -> {
            if (projectType == Integer.valueOf(projAdapter.getAllData().get(i).getId())){
                projType.setTextColor(getResources().getColor(R.color.nav_gray));
                projectType = -1;
            }else{
                projType.setTextColor(getResources().getColor(R.color.colorAccent));
                projectType = Integer.valueOf(projAdapter.getAllData().get(i).getId());
            }

            req.setLat(latData);
            req.setLng(lngData);
            page = 1;
            req.setPage(page);
            req.setType(Integer.valueOf(projAdapter.getAllData().get(i).getId()));

            if(popWindow != null){
                popWindow.dissmiss();
            }
            presenter.getProjectList(FindProjectActivity.this, req, type);
        });

        projTypeLayout.setOnClickListener(v -> {
            projType.setTextColor(getResources().getColor(R.color.colorAccent));
            popRecyclerView.setAdapter(projAdapter);
            popWindow = new CustomPopWindow.PopupWindowBuilder(FindProjectActivity.this)
                    .setView(popView)
                    .create()
                    .showAsDropDown(projTypeLayout, 0, 10);
        });

        nearLayout.setOnClickListener(v -> {
            if (req.getNearby() == 0){
                near.setTextColor(getResources().getColor(R.color.colorAccent));
                req.setNearby(1);
            }else{
                near.setTextColor(getResources().getColor(R.color.nav_gray));
                req.setNearby(0);
            }

            req.setLat(latData);
            req.setLng(lngData);
            page = 1;
            req.setPage(page);
            presenter.getProjectList(FindProjectActivity.this, req, type);
        });

        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId== EditorInfo.IME_ACTION_SEARCH ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER)){
                req.setKeyword(editText.getText().toString());
                page = 1;
                req.setPage(1);
                presenter.getProjectList(FindProjectActivity.this, req, type);
                return true;
            }else{
                return false;
            }
        });

        if(type == 2){
            //找维保
            List<TypeInfoRes.Type_Info> itemData = new ArrayList<>();

            TypeInfoRes.Type_Info type_info = new TypeInfoRes.Type_Info();
            type_info.setTitle("全部");
            type_info.setId("0");
            type_info.setParent_id("0");
            itemData.add(type_info);

            TypeInfoRes.Type_Info type_info1 = new TypeInfoRes.Type_Info();
            type_info1.setTitle("维修");
            type_info1.setId("1");
            type_info1.setParent_id("0");
            itemData.add(type_info1);

            TypeInfoRes.Type_Info type_info2 = new TypeInfoRes.Type_Info();
            type_info2.setTitle("保养");
            type_info2.setId("2");
            type_info2.setParent_id("0");
            itemData.add(type_info2);


            projAdapter.addAll(itemData);
        }else{
            presenter.getTypeInfo();
        }

        presenter.checkFinish();
        onRefresh();
    }

    @Override
    public void getData(FindProjectRes res) {
        if (page == 1) {
            adapter.clear();
        }

        if (type == 0){
            //找项目
            adapter.addAll(res.getProject());
        }else if (type == 1){
            //找零工
            adapter.addAll(res.getOdd());
        }else if(type == 2){
            //找维保
            adapter.addAll(res.getMaintenance());
        }else if(type == 4){
            //找维保
            adapter.addAll(res.getSupervisor());
        }

    }

    @Override
    public void getData(CheckFinishRes list) {
        status = list.getStatus();
    }

    @Override
    public void getNoMore(String msg) {
//        ToastUtil.show(msg);
//        adapter.stopMore();
        if(adapter.getAllData().size() == 0){
            recyclerView.showEmpty();
        }else{
            adapter.stopMore();
        }
    }

    @Override
    public void getUrl(String url) {
        if (!TextUtils.isEmpty(url))
            WebViewActivity.startActivity(FindProjectActivity.this, url);
    }

    @Override
    public void getProjType(TypeInfoRes res) {
        if(type == 2){
            //找维保
            List<TypeInfoRes.Type_Info> itemData = new ArrayList<>();

            TypeInfoRes.Type_Info type_info = new TypeInfoRes.Type_Info();
            type_info.setTitle("全部");
            type_info.setId("0");
            type_info.setParent_id("0");
            itemData.add(type_info);

            for (TypeInfoRes.Type_Info typeInfo : res.getType_info()){
                if (Integer.valueOf(typeInfo.getParent_id()) == 0){
                    itemData.add(typeInfo);
                }
            }
            projAdapter.addAll(itemData);
        }else{
            List<TypeInfoRes.Type_Info> itemData = new ArrayList<>();

            TypeInfoRes.Type_Info type_info = new TypeInfoRes.Type_Info();
            type_info.setTitle("全部");
            type_info.setId("0");
            type_info.setParent_id("0");
            itemData.add(type_info);

            for (TypeInfoRes.Type_Info typeInfo : res.getType_info()){
                if (Integer.valueOf(typeInfo.getParent_id()) == 0){
                    itemData.add(typeInfo);
                }
            }
            projAdapter.addAll(itemData);
        }




    }

    @Override
    public void onRefresh() {
        req.setLat(latData);
        req.setLng(lngData);
        page = 1;
        req.setPage(page);
        req.setPageSize(10);
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
        if(adapter.getAllData().size() == 0){
            recyclerView.showEmpty();
        }else{
            adapter.stopMore();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }


}
