package com.xunao.diaodiao.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.gzfgeh.iosdialog.IOSDialog;
import com.xunao.diaodiao.Bean.AddressBeanReq;
import com.xunao.diaodiao.Bean.CitiesBean;
import com.xunao.diaodiao.Bean.CityBean;
import com.xunao.diaodiao.Present.AddPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.AddView;
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

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;
import static com.xunao.diaodiao.Common.Constants.address;
import static com.xunao.diaodiao.Common.Constants.city;

public class AddressActivity extends BaseActivity implements AddView, CitiesAdapter.MyHotItemClickListener {


    @Inject
    AddPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.search_city)
    EditText searchCity;
    @BindView(R.id.current_city)
    TextView currentCity;
    @BindView(R.id.district)
    TextView district;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.quickIndexView)
    QuickIndexView quickIndexView;
    @BindView(R.id.content_layout)
    RelativeLayout contentLayout;

    private CitiesAdapter adapter;

    private List<String> hotCities;
    private boolean isFind = false;
    private IOSDialog dialog;
    private List<CitiesBean.DatasBean> allData = new ArrayList<>();
    private RecyclerArrayAdapter<String> textAdapter;

    public static void startActivityForResult(Activity context) {
        Intent intent = new Intent(context, AddressActivity.class);
        context.startActivityForResult(intent, PersonalActivity.REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "地区选择");

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
                        if (TextUtils.equals(addressListBean.getRegion_name(), searchCity.getText().toString())){
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

        district.setOnClickListener(v -> {
            AddressBeanReq req = new AddressBeanReq();
            req.setName(city);
            //presenter.getRegionId(this, req);
        });

        textAdapter = new RecyclerArrayAdapter<String>(this, R.layout.single_text_view) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setText(R.id.hot_city_text, s);
            }
        };

        textAdapter.setOnItemClickListener((view, i) -> {

        });

    }

    @Override
    public void getData(String s) {
        showPop();
    }

    private void showPop(){
        View popView = LayoutInflater.from(this).inflate(R.layout.single_recycler_pop, null);
        RecyclerView popRecyclerView = (RecyclerView) popView.findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        popRecyclerView.setLayoutManager(manager);
        popRecyclerView.setAdapter(textAdapter);
    }

    private List<CitiesBean.DatasBean> getCities() {
        List<CitiesBean.DatasBean> destList = new ArrayList<>();

        try {
            InputStream is = getAssets().open("city.json");
            String text = readTextFromSDcard(is);
            Gson gson = new Gson();
            List<CityBean.CityItemBean> resourceList = gson.fromJson(text, new TypeToken<List<CityBean.CityItemBean>>() {
            }.getType());


            for (CityBean.CityItemBean itemBean : resourceList) {
                CitiesBean.DatasBean bean = new CitiesBean.DatasBean();

                CitiesBean.DatasBean.AddressListBean addressListBean = new CitiesBean.DatasBean.AddressListBean();
                addressListBean.setId(Integer.valueOf(itemBean.getId()));
                addressListBean.setParent_id(Integer.valueOf(itemBean.getParent_id()));
                addressListBean.setRegion_name(itemBean.getRegion_name());
                addressListBean.setRegion_type(Integer.valueOf(itemBean.getRegion_type()));
                HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
                format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
                String alifName = PinyinHelper.toHanyuPinyinStringArray(itemBean.getRegion_name().charAt(0),
                        format)[0].substring(0, 1);

                if (addressListBean.getParent_id() == 0) {
                    continue;
                }

                if (destList.size() == 0) {
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
                        isFind = false;
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
        hotCities.add("北京");
        hotCities.add("上海");
        hotCities.add("广州");
        hotCities.add("杭州");
        hotCities.add("南京");
        hotCities.add("深圳");

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
                        adapter = new CitiesAdapter(AddressActivity.this, datasBeen, hotCities);
                        adapter.setHotItemClickListener(AddressActivity.this);
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
//        Intent intent = new Intent();
//        intent.putExtra(INTENT_KEY, data);
//        setResult(RESULT_OK, intent);
//        finish();
    }


    @Override
    public void onFailure() {

    }


}
