package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xunao.diaodiao.Bean.CitiesBean;
import com.xunao.diaodiao.Bean.Data;
import com.xunao.diaodiao.Bean.SearchBean;
import com.xunao.diaodiao.Present.SearchResultPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.SearchResultView;
import com.xunao.diaodiao.Widget.QuickIndexView;
import com.xunao.diaodiao.adapters.CitiesAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class SearchResultActivity extends BaseActivity implements SearchResultView {
    private static final String[] titles = {"精华", "猜你喜欢", "经典"};
    @Inject
    SearchResultPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.quickIndexView)
    QuickIndexView quickIndexView;

    private CitiesAdapter adapter;

    private List<String> hotCities;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SearchResultActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "选择城市");

//        adapter = new RecyclerArrayAdapter<HomeResponseBean>(this, R.layout.home_vertical_list) {
//            @Override
//            protected void convert(BaseViewHolder baseViewHolder, HomeResponseBean dataBean) {
//            }
//        };
//        recyclerView.setAdapterDefaultConfig(adapter, this, this);
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        Gson gson = new Gson();
        CitiesBean citiesBean = gson.fromJson(Data.citiesJson, CitiesBean.class);
        hotCities = new ArrayList<>();
        hotCities.add("北京");
        hotCities.add("上海");
        hotCities.add("广州");
        hotCities.add("杭州");
        hotCities.add("南京");
        hotCities.add("深圳");
        adapter = new CitiesAdapter(this, citiesBean.getDatas(), hotCities);
        recyclerView.setAdapter(adapter);

        quickIndexView.setOnIndexChangeListener(new QuickIndexView.OnIndexChangeListener() {
            @Override
            public void onIndexChange(String words) {
                List<CitiesBean.DatasBean> datas = adapter.getData();
                if (datas != null && datas.size() > 0) {
                    int count = 0;
                    for (int i = 0; i < datas.size(); i++) {
                        CitiesBean.DatasBean datasBean = datas.get(i);
                        if (datasBean.getAlifName().equals(words)) {
                            LinearLayoutManager llm = (LinearLayoutManager) recyclerView
                                    .getLayoutManager();
                            llm.scrollToPositionWithOffset(count + 1, 0);
                            return;
                        }
                        count += datasBean.getAddressList().size() + 1;
                    }
                }
            }
        });
    }


    @Override
    public void onFailure() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }


}
