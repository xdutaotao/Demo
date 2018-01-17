package com.xunao.diaodiao.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Created by GUZHENFU on 2017/9/11 10:19.
 */

public class BankListRes implements Serializable{
    private List<BankCard> bankCard;
    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<BankCard> getBankCard() {
        return bankCard;
    }

    public void setBankCard(List<BankCard> bankCard) {
        this.bankCard = bankCard;
    }

    public static class ListBean{
        private String name;
        private String logo;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }
    }

    public static class BankCard implements Serializable{
        private String card;
        private String card_name;
        private String card_type;
        private String img;
        private String card_num;
        private String name;
        private String bank_branch;
        private String bank_info;
        private int province;
        private int city;
        private int district;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBank_branch() {
            return bank_branch;
        }

        public void setBank_branch(String bank_branch) {
            this.bank_branch = bank_branch;
        }

        public String getBank_info() {
            return bank_info;
        }

        public void setBank_info(String bank_info) {
            this.bank_info = bank_info;
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

        public String getCard_num() {
            return card_num;
        }

        public void setCard_num(String card_num) {
            this.card_num = card_num;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
        }

        public String getCard_name() {
            return card_name;
        }

        public void setCard_name(String card_name) {
            this.card_name = card_name;
        }

        public String getCard_type() {
            return card_type;
        }

        public void setCard_type(String card_type) {
            this.card_type = card_type;
        }
    }
}
