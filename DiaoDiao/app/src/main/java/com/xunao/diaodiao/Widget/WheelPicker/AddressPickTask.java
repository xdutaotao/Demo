package com.xunao.diaodiao.Widget.WheelPicker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xunao.diaodiao.Bean.CitiesBean;
import com.xunao.diaodiao.Bean.CityBean;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.util.ConvertUtils;

/**
 * Description:
 * Created by guzhenfu on 2017/10/9.
 */

public class AddressPickTask extends AsyncTask<String, Void, ArrayList<Province>> {
    private Activity activity;
    private ProgressDialog dialog;
    private Callback callback;
    private String selectedProvince = "", selectedCity = "", selectedCounty = "";
    private boolean hideProvince = false;
    private boolean hideCounty = false;

    private boolean isFind = false;

    public AddressPickTask(Activity activity) {
        this.activity = activity;
    }

    public void setHideProvince(boolean hideProvince) {
        this.hideProvince = hideProvince;
    }

    public void setHideCounty(boolean hideCounty) {
        this.hideCounty = hideCounty;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(activity, null, "正在初始化数据...", true, true);
    }

    @Override
    protected ArrayList<Province> doInBackground(String... params) {
        if (params != null) {
            switch (params.length) {
                case 1:
                    selectedProvince = params[0];
                    break;
                case 2:
                    selectedProvince = params[0];
                    selectedCity = params[1];
                    break;
                case 3:
                    selectedProvince = params[0];
                    selectedCity = params[1];
                    selectedCounty = params[2];
                    break;
                default:
                    break;
            }
        }

        ArrayList<Province> data = new ArrayList<>();
        List<CitiesBean.DatasBean> sourceData = getCities();
        for(CitiesBean.DatasBean datasBean : sourceData){
            for(CitiesBean.DatasBean.AddressListBean bean: datasBean.getAddressList()){
                switch (bean.getRegion_type()){
                    case 1:
                        //省份
                        Province province = new Province(String.valueOf(bean.getId()), bean.getRegion_name());
                        data.add(province);
                        break;

                    case 2:
                        //市区
                        City city = new City(String.valueOf(bean.getId()), bean.getRegion_name());
                        city.setProvinceId(String.valueOf(bean.getParent_id()));
                        for(Province p : data){
                            if (bean.getParent_id() == Integer.valueOf(p.getAreaId())){
                                p.getCities().add(city);
                            }
                        }
                        break;

                    case 3:
                        //县
                        County county = new County(String.valueOf(bean.getId()), bean.getRegion_name());
                        county.setCityId(String.valueOf(bean.getParent_id()));
                        for(Province p: data){
                            for(City city1: p.getCities()){
                                if (bean.getParent_id() == Integer.valueOf(city1.getAreaId())){
                                    city1.getCounties().add(county);
                                }
                            }
                        }
                        break;
                }
            }
        }

        return data;
    }

    @Override
    protected void onPostExecute(ArrayList<Province> result) {
        dialog.dismiss();
        if (result.size() > 0) {
            AddressPicker picker = new AddressPicker(activity, result);
            picker.setHideProvince(hideProvince);
            picker.setHideCounty(hideCounty);
            if (hideCounty) {
                picker.setColumnWeight(0.8f, 1.0f);
            } else if (hideProvince) {
                picker.setColumnWeight(1.0f, 0.8f);
            } else {
                picker.setColumnWeight(0.8f, 1.0f, 1.0f);
            }
            picker.setSelectedItem(selectedProvince, selectedCity, selectedCounty);
            picker.setOnAddressPickListener(callback);
            picker.show();
        } else {
            callback.onAddressInitFailed();
        }
    }

    public interface Callback extends AddressPicker.OnAddressPickListener {

        void onAddressInitFailed();

    }

    private List<CitiesBean.DatasBean> getCities() {
        List<CitiesBean.DatasBean> destList = new ArrayList<>();

        try {
            InputStream is = activity.getAssets().open("city.json");
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


}
