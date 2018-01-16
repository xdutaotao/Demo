package com.xunao.diaodiao.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/14.
 */

public class TypeInfoRes implements Serializable{
    private List<Type_Info> type_info;

    public List<Type_Info> getType_info() {
        return type_info;
    }

    public void setType_info(List<Type_Info> type_info) {
        this.type_info = type_info;
    }

    public static class Type_Info implements Serializable{
        private String id;
        private String parent_id;
        private String title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
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
