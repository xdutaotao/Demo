package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/24.
 */

public class CityBean {
    private List<CityItemBean> cityList;

    public List<CityItemBean> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityItemBean> cityList) {
        this.cityList = cityList;
    }

    public static class CityItemBean{
        /**
         * id : 1
         * region_name : 中国
         * parent_id : 0
         * region_type : 0
         */

        private String id;
        private String name;
        private String parent_id;
        private String region_type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }

        public String getRegion_type() {
            return region_type;
        }

        public void setRegion_type(String region_type) {
            this.region_type = region_type;
        }

    }


}
