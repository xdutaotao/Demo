package com.demo.cworker.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/26 20:57.
 */

public class PackageBean {

    /**
     * msg : 200
     * result : {"mts":[{"dateline":0,"name":"玻璃","project":"1","disabled":0,"remark":""}],"pss":[{"dateline":0,"packageWeight":"","description":"该种零件到库时已进行有效的包装，一般工艺为：纸管、三角管、PVC管等管状包装","packageWidth":"","project":"","remark":"","packageLenth":0,"hasOutPackage":1,"packageHeight":"","name":"管类包装","disabled":0,"shortName":""}],"pmts":[{"id":"类型; id用于搜索包材列表","project":"所属项目","name":"包材名称","description":"包材描述内容"}],"pms":[{"englishName":"","dateline":0,"specRemark":"","materialSpec":"(200/150/150/150/200)g/m3","sizeSpec":"300*200*500","weight":30,"remark":"","type":"1","corrugatedBox":"","applyRemark":"0","name":"PE膜","disabled":0}],"ats":[{"name":"名称","project":"所属项目","dateline":"添加时间"}]}
     */

    private String msg;
    private ResultBean result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private List<MtsBean> mts;
        private List<PssBean> pss;
        private List<PmtsBean> pmts;
        private List<PmsBean> pms;
        private List<AtsBean> ats;

        public List<MtsBean> getMts() {
            return mts;
        }

        public void setMts(List<MtsBean> mts) {
            this.mts = mts;
        }

        public List<PssBean> getPss() {
            return pss;
        }

        public void setPss(List<PssBean> pss) {
            this.pss = pss;
        }

        public List<PmtsBean> getPmts() {
            return pmts;
        }

        public void setPmts(List<PmtsBean> pmts) {
            this.pmts = pmts;
        }

        public List<PmsBean> getPms() {
            return pms;
        }

        public void setPms(List<PmsBean> pms) {
            this.pms = pms;
        }

        public List<AtsBean> getAts() {
            return ats;
        }

        public void setAts(List<AtsBean> ats) {
            this.ats = ats;
        }

        public static class MtsBean {
            /**
             * dateline : 0
             * name : 玻璃
             * project : 1
             * disabled : 0
             * remark :
             */

            private int dateline;
            private String name;
            private String project;
            private int disabled;
            private String remark;

            public int getDateline() {
                return dateline;
            }

            public void setDateline(int dateline) {
                this.dateline = dateline;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getProject() {
                return project;
            }

            public void setProject(String project) {
                this.project = project;
            }

            public int getDisabled() {
                return disabled;
            }

            public void setDisabled(int disabled) {
                this.disabled = disabled;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }
        }

        public static class PssBean implements Serializable{
            /**
             * dateline : 0
             * packageWeight :
             * description : 该种零件到库时已进行有效的包装，一般工艺为：纸管、三角管、PVC管等管状包装
             * packageWidth :
             * project :
             * remark :
             * packageLenth : 0
             * hasOutPackage : 1
             * packageHeight :
             * name : 管类包装
             * disabled : 0
             * shortName :
             */

            private int dateline;
            private String packageWeight;
            private String description;
            private String packageWidth;
            private String project;
            private String remark;
            private int packageLenth;
            private int hasOutPackage;
            private String packageHeight;
            private String name;
            private int disabled;
            private String shortName;

            public int getDateline() {
                return dateline;
            }

            public void setDateline(int dateline) {
                this.dateline = dateline;
            }

            public String getPackageWeight() {
                return packageWeight;
            }

            public void setPackageWeight(String packageWeight) {
                this.packageWeight = packageWeight;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getPackageWidth() {
                return packageWidth;
            }

            public void setPackageWidth(String packageWidth) {
                this.packageWidth = packageWidth;
            }

            public String getProject() {
                return project;
            }

            public void setProject(String project) {
                this.project = project;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getPackageLenth() {
                return packageLenth;
            }

            public void setPackageLenth(int packageLenth) {
                this.packageLenth = packageLenth;
            }

            public int getHasOutPackage() {
                return hasOutPackage;
            }

            public void setHasOutPackage(int hasOutPackage) {
                this.hasOutPackage = hasOutPackage;
            }

            public String getPackageHeight() {
                return packageHeight;
            }

            public void setPackageHeight(String packageHeight) {
                this.packageHeight = packageHeight;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getDisabled() {
                return disabled;
            }

            public void setDisabled(int disabled) {
                this.disabled = disabled;
            }

            public String getShortName() {
                return shortName;
            }

            public void setShortName(String shortName) {
                this.shortName = shortName;
            }
        }

        public static class PmtsBean {
            /**
             * id : 类型; id用于搜索包材列表
             * project : 所属项目
             * name : 包材名称
             * description : 包材描述内容
             */

            private String id;
            private String project;
            private String name;
            private String description;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getProject() {
                return project;
            }

            public void setProject(String project) {
                this.project = project;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }
        }

        public static class PmsBean {
            /**
             * englishName :
             * dateline : 0
             * specRemark :
             * materialSpec : (200/150/150/150/200)g/m3
             * sizeSpec : 300*200*500
             * weight : 30
             * remark :
             * type : 1
             * corrugatedBox :
             * applyRemark : 0
             * name : PE膜
             * disabled : 0
             */

            private String englishName;
            private int dateline;
            private String specRemark;
            private String materialSpec;
            private String sizeSpec;
            private int weight;
            private String remark;
            private String type;
            private String corrugatedBox;
            private String applyRemark;
            private String name;
            private int disabled;

            public String getEnglishName() {
                return englishName;
            }

            public void setEnglishName(String englishName) {
                this.englishName = englishName;
            }

            public int getDateline() {
                return dateline;
            }

            public void setDateline(int dateline) {
                this.dateline = dateline;
            }

            public String getSpecRemark() {
                return specRemark;
            }

            public void setSpecRemark(String specRemark) {
                this.specRemark = specRemark;
            }

            public String getMaterialSpec() {
                return materialSpec;
            }

            public void setMaterialSpec(String materialSpec) {
                this.materialSpec = materialSpec;
            }

            public String getSizeSpec() {
                return sizeSpec;
            }

            public void setSizeSpec(String sizeSpec) {
                this.sizeSpec = sizeSpec;
            }

            public int getWeight() {
                return weight;
            }

            public void setWeight(int weight) {
                this.weight = weight;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getCorrugatedBox() {
                return corrugatedBox;
            }

            public void setCorrugatedBox(String corrugatedBox) {
                this.corrugatedBox = corrugatedBox;
            }

            public String getApplyRemark() {
                return applyRemark;
            }

            public void setApplyRemark(String applyRemark) {
                this.applyRemark = applyRemark;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getDisabled() {
                return disabled;
            }

            public void setDisabled(int disabled) {
                this.disabled = disabled;
            }
        }

        public static class AtsBean {
            /**
             * name : 名称
             * project : 所属项目
             * dateline : 添加时间
             */

            private String name;
            private String project;
            private String dateline;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getProject() {
                return project;
            }

            public void setProject(String project) {
                this.project = project;
            }

            public String getDateline() {
                return dateline;
            }

            public void setDateline(String dateline) {
                this.dateline = dateline;
            }
        }
    }
}
