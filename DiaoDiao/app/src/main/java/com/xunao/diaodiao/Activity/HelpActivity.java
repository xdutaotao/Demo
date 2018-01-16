package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.AddPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.AddView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;

import static com.xunao.diaodiao.Common.Constants.TYPE_KEY;
import static com.xunao.diaodiao.Common.Constants.latData;
import static com.xunao.diaodiao.Common.Constants.lngData;

public class HelpActivity extends BaseActivity implements AddView, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {

    @Inject
    AddPresenter presenter;
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
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.address_layout)
    LinearLayout addressLayout;

    private RecyclerArrayAdapter<FindProjectRes.FindProject> adapter;
    private RecyclerArrayAdapter<String> textAdapter;
    private int page = 1;
    private FindProjReq req = new FindProjReq();
    private FindProjectRes.FindProject res;
    private CustomPopWindow popWindow;
    private List<County> cityList = new ArrayList<>();
    private List<String> addressList = new ArrayList<>();

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HelpActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
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

        textAdapter = new RecyclerArrayAdapter<String>(this, R.layout.select_skill_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setText(R.id.skill_text, s);
                if (TextUtils.equals(address.getText(), s)) {
                    baseViewHolder.setBackgroundRes(R.id.skill_text, R.drawable.btn_blue_bg);
                    baseViewHolder.setTextColorRes(R.id.skill_text, R.color.white);
                } else {
                    baseViewHolder.setBackgroundRes(R.id.skill_text, R.drawable.btn_blank_bg);
                    baseViewHolder.setTextColorRes(R.id.skill_text, R.color.gray);
                }

                baseViewHolder.setOnClickListener(R.id.skill_text, v -> {
                    address.setText(s);
                    popWindow.dissmiss();
                    for(County city: cityList){
                        if(TextUtils.equals(city.getAreaName(), s)){
                            req.setDistrict(Integer.valueOf(city.getAreaId()));
                            page = 1;
                            presenter.getMutualList(HelpActivity.this ,req, 3);
                            return;
                        }
                    }

                });

            }
        };


        adapter = new RecyclerArrayAdapter<FindProjectRes.FindProject>(this, R.layout.help_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, FindProjectRes.FindProject homeBean) {
                baseViewHolder.setText(R.id.title, homeBean.getTitle());
                baseViewHolder.setText(R.id.time, homeBean.getIssue_time());
                baseViewHolder.setText(R.id.address, homeBean.getDesc());
                baseViewHolder.setText(R.id.distance, homeBean.getDistance());
            }
        };

        adapter.setOnItemClickListener((view, i) -> {
            WebViewDetailActivity.startActivity(this,
                    adapter.getAllData().get(i));
        });

        recyclerView.setAdapterDefaultConfig(adapter, this, this);
        onRefresh();

        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                req.setKeyword(editText.getText().toString());
                page = 1;
                req.setPage(1);
                presenter.getMutualList(HelpActivity.this, req, 3);
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

        addressLayout.setVisibility(View.GONE);
        View popView = LayoutInflater.from(this).inflate(R.layout.single_help_recycler, null);
        RecyclerView popRecyclerView = (RecyclerView) popView.findViewById(R.id.recycler_view);
        popRecyclerView.setAdapter(textAdapter);
        addressLayout.setOnClickListener(v -> {
            textAdapter.notifyDataSetChanged();
            popWindow = new CustomPopWindow.PopupWindowBuilder(HelpActivity.this)
                    .setView(popView)
                    .create()
                    .showAsDropDown(addressLayout, 0, 10);
        });

    }

    @Override
    public void onRefresh() {
        req.setLat(latData);
        req.setLng(lngData);
        page = 1;
        req.setPage(page);
        req.setPageSize(10);
        presenter.getMutualList(this, req, 3);
    }

    @Override
    public void onLoadMore() {
        page++;
        req.setPage(page);
        presenter.getMutualList(this, req, 3);
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
        adapter.addAll(s.getMutual());
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
                            return;
                        }
                    }
                }

            }
        }
    }
}
