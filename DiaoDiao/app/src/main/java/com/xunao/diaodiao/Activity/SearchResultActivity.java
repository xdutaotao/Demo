package com.xunao.diaodiao.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gzfgeh.iosdialog.IOSDialog;
import com.xunao.diaodiao.Bean.CitiesBean;
import com.xunao.diaodiao.Bean.CityBean;
import com.xunao.diaodiao.Present.SearchResultPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.SearchResultView;
import com.xunao.diaodiao.Widget.QuickIndexView;
import com.xunao.diaodiao.adapters.CitiesAdapter;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.observables.SyncOnSubscribe;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;
import static com.xunao.diaodiao.Common.Constants.address;
import static com.xunao.diaodiao.Common.Constants.city;
import static com.xunao.diaodiao.Common.Constants.selectCity;
import static com.xunao.diaodiao.Fragment.HomeFragment.REQUEST_KEY;

/**
 * create by
 */
public class SearchResultActivity extends BaseActivity implements SearchResultView, CitiesAdapter.MyHotItemClickListener {
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
    @BindView(R.id.search_city)
    EditText searchCity;
    @BindView(R.id.current_city)
    TextView currentCity;
    @BindView(R.id.content_layout)
    RelativeLayout contentLayout;

    private CitiesAdapter adapter;

    private List<String> hotCities;
    private boolean isFind = false;
    private IOSDialog dialog;
    private List<CitiesBean.DatasBean> allData = new ArrayList<>();

    public static void startActivityForResult(Fragment context, String address) {
        Intent intent = new Intent(context.getActivity(), SearchResultActivity.class);
        intent.putExtra(INTENT_KEY, address);
        context.startActivityForResult(intent, REQUEST_KEY);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "选择城市");

        currentCity.setText("当前定位城市："+city);

        dialog = new IOSDialog(this).builder()
                .setLoadingView();
        dialog.show();

        initView();

        searchCity.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId== EditorInfo.IME_ACTION_SEARCH ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER)){
                //returnHome(searchCity.getText().toString());
                for(CitiesBean.DatasBean bean: allData){
                    for(CitiesBean.DatasBean.AddressListBean addressListBean: bean.getAddressList()){
                        if (TextUtils.equals(addressListBean.getName(), searchCity.getText().toString())){
                            returnHome(searchCity.getText().toString());
                            return true;
                        }
                    }
                }

                ToastUtil.show("没有找到该城市");

                return true;
            }else{
                return false;
            }
        });
        //getCities();
    }

    private List<CitiesBean.DatasBean> getCities() {
        List<CitiesBean.DatasBean> destList = new ArrayList<>();

        try {
            InputStream is = getAssets().open("city2.json");
            String text = readTextFromSDcard(is);
            Gson gson = new Gson();
            List<CityBean.CityItemBean> resourceList = gson.fromJson(text, new TypeToken<List<CityBean.CityItemBean>>() {
            }.getType());


            for (CityBean.CityItemBean itemBean : resourceList) {
                isFind = false;
                if (Integer.valueOf(itemBean.getRegion_type()) != 2) {
                    continue;
                }

                CitiesBean.DatasBean bean = new CitiesBean.DatasBean();

                CitiesBean.DatasBean.AddressListBean addressListBean = new CitiesBean.DatasBean.AddressListBean();
                addressListBean.setId(Integer.valueOf(itemBean.getId()));
                addressListBean.setParent_id(Integer.valueOf(itemBean.getParent_id()));
                addressListBean.setName(itemBean.getName());
                addressListBean.setRegion_type(Integer.valueOf(itemBean.getRegion_type()));
                if (Integer.valueOf(itemBean.getId()) == 394){
                    isFind = false;
                }
                HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
                format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
                String alifName = PinyinHelper.toHanyuPinyinStringArray(itemBean.getName().charAt(0),
                        format)[0].substring(0, 1);

                if (destList.size() == 0) {
                    //第一次
                    bean.setAlifName(alifName);
                    List<CitiesBean.DatasBean.AddressListBean> listBean = new ArrayList<>();
                    listBean.add(addressListBean);
                    bean.setAddressList(listBean);
                    destList.add(bean);
                } else {
                    for (CitiesBean.DatasBean datasBean : destList) {
                        if (TextUtils.equals(datasBean.getAlifName(), alifName)) {
                            datasBean.getAddressList().add(addressListBean);
                            isFind = true;
                            break;
                        }
                    }
                    if (!isFind) {
                        bean.setAlifName(alifName);
                        List<CitiesBean.DatasBean.AddressListBean> listBean = new ArrayList<>();
                        listBean.add(addressListBean);
                        bean.setAddressList(listBean);
                        destList.add(bean);
                    }
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
        }


        return destList;
    }

    private String readTextFromSDcard(InputStream is) throws Exception {
        InputStreamReader reader = new InputStreamReader(is, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer("");
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
            buffer.append("\n");
        }
        return buffer.toString();//把读取的数据返回
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        hotCities = new ArrayList<>();
        hotCities.add("北京市");
        hotCities.add("上海市");
        hotCities.add("广州市");
        hotCities.add("杭州市");
        hotCities.add("南京市");
        hotCities.add("深圳市");

        Observable.create(new Observable.OnSubscribe<List<CitiesBean.DatasBean>>() {
            @Override
            public void call(Subscriber<? super List<CitiesBean.DatasBean>> subscriber) {
                subscriber.onNext(getCities());
                subscriber.onCompleted();
            }
        }).compose(RxUtils.applyIOToMainThreadSchedulers())
                .subscribe(new Subscriber<List<CitiesBean.DatasBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<CitiesBean.DatasBean> datasBeen) {
                        allData.clear();
                        allData.addAll(datasBeen);
                        adapter = new CitiesAdapter(SearchResultActivity.this, datasBeen, hotCities);
                        adapter.setHotItemClickListener(SearchResultActivity.this);
                        recyclerView.setAdapter(adapter);
                        contentLayout.setVisibility(View.VISIBLE);
                        dialog.dismiss();

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
                });

        //getCities();



    }

    @Override
    public void onItemClick(View view, String data) {
        returnHome(data);
    }

    private void returnHome(String data){
        Intent intent = new Intent();
        intent.putExtra(INTENT_KEY, data);
        //selectCity = data;
        setResult(RESULT_OK, intent);
        finish();
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
