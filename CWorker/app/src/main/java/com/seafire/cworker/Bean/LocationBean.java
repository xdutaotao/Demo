package com.seafire.cworker.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/20.
 */

public class LocationBean {
    private int status;
    private int count;
    private String info;
    private List<GeoCodesBean> geocodes;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<GeoCodesBean> getGeocodes() {
        return geocodes;
    }

    public void setGeocodes(List<GeoCodesBean> geocodes) {
        this.geocodes = geocodes;
    }

    public static class GeoCodesBean{
        private String formatted_address;
        private String province;
        private String city;
        private int citycode;
        private String district;
        private String township;
        private String street;
        private String number;
        private long adcode;
        private String location;
        private String level;

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getCitycode() {
            return citycode;
        }

        public void setCitycode(int citycode) {
            this.citycode = citycode;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getTownship() {
            return township;
        }

        public void setTownship(String township) {
            this.township = township;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public long getAdcode() {
            return adcode;
        }

        public void setAdcode(long adcode) {
            this.adcode = adcode;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }
    }
}
