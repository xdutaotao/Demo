package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/14.
 */

public class TypeInfoRes {
    private List<Type_Info> type_info;

    public List<Type_Info> getType_info() {
        return type_info;
    }

    public void setType_info(List<Type_Info> type_info) {
        this.type_info = type_info;
    }

    public static class Type_Info{
        private int id;
        private int parent_id;
        private String title;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getParent_id() {
            return parent_id;
        }

        public void setParent_id(int parent_id) {
            this.parent_id = parent_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

}
