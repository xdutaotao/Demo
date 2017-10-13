package com.xunao.diaodiao.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Created by GUZHENFU on 2017/9/28 15:26.
 */

public class ProjectTypeRes implements Serializable{
    private List<TypeBean> types;

    public List<TypeBean> getTypes() {
        return types;
    }

    public void setTypes(List<TypeBean> types) {
        this.types = types;
    }

    public static class TypeBean implements Serializable{
        private String name;
        private int id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
