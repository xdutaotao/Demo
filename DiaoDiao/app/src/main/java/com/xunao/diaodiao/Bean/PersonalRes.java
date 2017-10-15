package com.xunao.diaodiao.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/12.
 */

public class PersonalRes {
    private CompanyInfo companyInfo;
    private TechnicianInfo technicianInfo;
    private FamilyInfo familyInfo;


    public CompanyInfo getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(CompanyInfo companyInfo) {
        this.companyInfo = companyInfo;
    }

    public TechnicianInfo getTechnicianInfo() {
        return technicianInfo;
    }

    public void setTechnicianInfo(TechnicianInfo technicianInfo) {
        this.technicianInfo = technicianInfo;
    }

    public FamilyInfo getFamilyInfo() {
        return familyInfo;
    }

    public void setFamilyInfo(FamilyInfo familyInfo) {
        this.familyInfo = familyInfo;
    }

    public static class CompanyInfo implements Serializable{
        private String name;
        private String tel;
        private int province;
        private int city;
        private int district;
        private String years;
        private String contact;
        private String contact_mobile;
        private String contact_card;
        private String card_front;
        private String card_back;
        private List<String> pictures;
        private String address;
        private String region;
        private long establish_time;

        public long getEstablish_time() {
            return establish_time;
        }

        public void setEstablish_time(long establish_time) {
            this.establish_time = establish_time;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public int getProvince() {
            return province;
        }

        public void setProvince(int province) {
            this.province = province;
        }

        public int getCity() {
            return city;
        }

        public void setCity(int city) {
            this.city = city;
        }

        public int getDistrict() {
            return district;
        }

        public void setDistrict(int district) {
            this.district = district;
        }

        public String getYears() {
            return years;
        }

        public void setYears(String years) {
            this.years = years;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getContact_mobile() {
            return contact_mobile;
        }

        public void setContact_mobile(String contact_mobile) {
            this.contact_mobile = contact_mobile;
        }

        public String getContact_card() {
            return contact_card;
        }

        public void setContact_card(String contact_card) {
            this.contact_card = contact_card;
        }

        public String getCard_front() {
            return card_front;
        }

        public void setCard_front(String card_front) {
            this.card_front = card_front;
        }

        public String getCard_back() {
            return card_back;
        }

        public void setCard_back(String card_back) {
            this.card_back = card_back;
        }

        public List<String> getPictures() {
            return pictures;
        }

        public void setPictures(List<String> pictures) {
            this.pictures = pictures;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }


    public static class TechnicianInfo implements Serializable{
        private String name;
        private String mobile;
        private String card;
        private int province;
        private int city;
        private int district;
        private String address;
        private String experience;
        private int team_number;
        private String project_type;
        private String evaluate;
        private String card_front;
        private String card_back;
        private String certificate;
        private String region;

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
        }

        public int getProvince() {
            return province;
        }

        public void setProvince(int province) {
            this.province = province;
        }

        public int getCity() {
            return city;
        }

        public void setCity(int city) {
            this.city = city;
        }

        public int getDistrict() {
            return district;
        }

        public void setDistrict(int district) {
            this.district = district;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getExperience() {
            return experience;
        }

        public void setExperience(String experience) {
            this.experience = experience;
        }

        public int getTeam_number() {
            return team_number;
        }

        public void setTeam_number(int team_number) {
            this.team_number = team_number;
        }

        public String getProject_type() {
            return project_type;
        }

        public void setProject_type(String project_type) {
            this.project_type = project_type;
        }

        public String getEvaluate() {
            return evaluate;
        }

        public void setEvaluate(String evaluate) {
            this.evaluate = evaluate;
        }

        public String getCard_front() {
            return card_front;
        }

        public void setCard_front(String card_front) {
            this.card_front = card_front;
        }

        public String getCard_back() {
            return card_back;
        }

        public void setCard_back(String card_back) {
            this.card_back = card_back;
        }

        public String getCertificate() {
            return certificate;
        }

        public void setCertificate(String certificate) {
            this.certificate = certificate;
        }
    }


    public static class FamilyInfo implements Serializable{
        private String name;
        private String mobile;
        private int province;
        private int city;
        private int district;
        private String address;
        private String region;

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getProvince() {
            return province;
        }

        public void setProvince(int province) {
            this.province = province;
        }

        public int getCity() {
            return city;
        }

        public void setCity(int city) {
            this.city = city;
        }

        public int getDistrict() {
            return district;
        }

        public void setDistrict(int district) {
            this.district = district;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
