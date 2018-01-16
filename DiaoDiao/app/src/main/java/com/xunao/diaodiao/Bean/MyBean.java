package com.xunao.diaodiao.Bean;

/**
 * Description:
 * Created by guzhenfu on 2017/9/8.
 */

public class MyBean {
    private String name;
    private String user_point;
    private String balance;
    private int card_number;
    private String head_img;
    private int unread_message;

    public int getUnread_message() {
        return unread_message;
    }

    public void setUnread_message(int unread_message) {
        this.unread_message = unread_message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_point() {
        return user_point;
    }

    public void setUser_point(String user_point) {
        this.user_point = user_point;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public int getCard_number() {
        return card_number;
    }

    public void setCard_number(int card_number) {
        this.card_number = card_number;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }
}
