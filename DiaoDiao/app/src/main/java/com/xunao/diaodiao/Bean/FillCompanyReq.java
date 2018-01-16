package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/2.
 */

public class FillCompanyReq {
    private int userid;
    private int type;
    private String name;
    private int province;
    private int city;
    private int district;
    private String address;
    private long years;
    private String tel;
    private String contact;
    private String contact_mobile;
    private String contact_card;
    private String verify;
    private String card_front;
    private String card_back;
    private List<String> authentication;
    private long establish_time;

    public long getEstablish_time() {
        return establish_time;
    }

    public void setEstablish_time(long establish_time) {
        this.establish_time = establish_time;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
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

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public long getYears() {
        return years;
    }

    public void setYears(long years) {
        this.years = years;
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

    public List<String> getAuthentication() {
        return authentication;
    }

    public void setAuthentication(List<String> authentication) {
        this.authentication = authentication;
    }
}
