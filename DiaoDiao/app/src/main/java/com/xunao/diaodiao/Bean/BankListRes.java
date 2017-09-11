package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by GUZHENFU on 2017/9/11 10:19.
 */

public class BankListRes {
    private List<BankCard> bankCard;

    public List<BankCard> getBankCard() {
        return bankCard;
    }

    public void setBankCard(List<BankCard> bankCard) {
        this.bankCard = bankCard;
    }

    public static class BankCard{
        private String card;
        private String card_name;
        private String card_type;

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
