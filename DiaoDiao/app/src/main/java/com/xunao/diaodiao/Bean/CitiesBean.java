package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/5.
 */

public class CitiesBean {

    /**
     * alifName : C
     * addressList : [{"id":37,"name":"潮州"}]
     */

    private List<DatasBean> datas;

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        private String alifName;
        /**
         * id : 37
         * name : 潮州
         */

        private List<AddressListBean> addressList;

        public String getAlifName() {
            return alifName;
        }

        public void setAlifName(String alifName) {
            this.alifName = alifName;
        }

        public List<AddressListBean> getAddressList() {
            return addressList;
        }

        public void setAddressList(List<AddressListBean> addressList) {
            this.addressList = addressList;
        }

        public static class AddressListBean {
            private int id;
            private String name;
            private int parent_id;
            private int region_type;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getParent_id() {
                return parent_id;
            }

            public void setParent_id(int parent_id) {
                this.parent_id = parent_id;
            }

            public int getRegion_type() {
                return region_type;
            }

            public void setRegion_type(int region_type) {
                this.region_type = region_type;
            }
        }
    }
}
