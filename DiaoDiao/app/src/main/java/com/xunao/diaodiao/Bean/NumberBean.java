package com.xunao.diaodiao.Bean;

/**
 * Description:
 * Created by guzhenfu on 17/5/28.
 */

public class NumberBean {

    /**
     * msg : 200
     * result : 获取成功
     * data : {"englishName":"a","code":"1","providerCode":"1","cnName":"中文名称a","dateline":0,"price":0,"sourceDistribution":"good","project":"1","disabled":0,"model":"1","providerName":"zz"}
     * hasOldData : 1
     * oldData : {"dateline":1489342322,"packageWidth":demo,"project":"1","addedLength":demo,"remark":"123123","auditType":"","partCode":"1","partName":"demo","processRecommendation":"demo","packageStypeName":"demo","singlePackageHeight":0,"packageLength":demo,"disabled":0,"roughWeight":demo,"collectorName":"Kenny2","addedWidth":demo,"partMaterialName":"demo","singlePackageWeight":0,"partLength":demo,"partWidth":demo,"singlePackageWidth":0,"addedHeight":demo,"netWeight":demo,"packageHeight":demo,"packageModelCount":demo,"singlePackageLength":0,"location":"","locations":"123123","isHistory":"1","systemResource":"","user":null,"pmJsona":"","documentCodes":"[]","partHeight":demo}
     */

    private String msg;
    private String result;
    private DataBean data;
    private int hasOldData;
    private OldDataBean oldData;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getHasOldData() {
        return hasOldData;
    }

    public void setHasOldData(int hasOldData) {
        this.hasOldData = hasOldData;
    }

    public OldDataBean getOldData() {
        return oldData;
    }

    public void setOldData(OldDataBean oldData) {
        this.oldData = oldData;
    }

    public static class DataBean {
        /**
         * englishName : a
         * code : 1
         * providerCode : 1
         * cnName : 中文名称a
         * dateline : 0
         * price : 0
         * sourceDistribution : good
         * project : 1
         * disabled : 0
         * model : 1
         * providerName : zz
         */

        private String englishName;
        private String code;
        private String providerCode;
        private String cnName;
        private int dateline;
        private int price;
        private String sourceDistribution;
        private String project;
        private int disabled;
        private String model;
        private String providerName;

        public String getEnglishName() {
            return englishName;
        }

        public void setEnglishName(String englishName) {
            this.englishName = englishName;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getProviderCode() {
            return providerCode;
        }

        public void setProviderCode(String providerCode) {
            this.providerCode = providerCode;
        }

        public String getCnName() {
            return cnName;
        }

        public void setCnName(String cnName) {
            this.cnName = cnName;
        }

        public int getDateline() {
            return dateline;
        }

        public void setDateline(int dateline) {
            this.dateline = dateline;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getSourceDistribution() {
            return sourceDistribution;
        }

        public void setSourceDistribution(String sourceDistribution) {
            this.sourceDistribution = sourceDistribution;
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

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getProviderName() {
            return providerName;
        }

        public void setProviderName(String providerName) {
            this.providerName = providerName;
        }
    }

    public static class OldDataBean {
        /**
         * dateline : 1489342322
         * packageWidth : demo
         * project : 1
         * addedLength : demo
         * remark : 123123
         * auditType :
         * partCode : 1
         * partName : demo
         * processRecommendation : demo
         * packageStypeName : demo
         * singlePackageHeight : 0
         * packageLength : demo
         * disabled : 0
         * roughWeight : demo
         * collectorName : Kenny2
         * addedWidth : demo
         * partMaterialName : demo
         * singlePackageWeight : 0
         * partLength : demo
         * partWidth : demo
         * singlePackageWidth : 0
         * addedHeight : demo
         * netWeight : demo
         * packageHeight : demo
         * packageModelCount : demo
         * singlePackageLength : 0
         * location :
         * locations : 123123
         * isHistory : 1
         * systemResource :
         * user : null
         * pmJsona :
         * documentCodes : []
         * partHeight : demo
         */

        private long dateline;
        private float packageWidth;
        private String project;
        private float addedLength;
        private String remark;
        private String auditType;
        private String partCode;
        private String partName;
        private String processRecommendation;
        private String packageStypeName;
        private float singlePackageHeight;
        private float packageLength;
        private int disabled;
        private float roughWeight;
        private String collectorName;
        private float addedWidth;
        private String partMaterialName;
        private float singlePackageWeight;
        private float partLength;
        private float partWidth;
        private float singlePackageWidth;
        private float addedHeight;
        private float netWeight;
        private float packageHeight;
        private int packageModelCount;
        private float singlePackageLength;
        private String location;
        private String locations;
        private String isHistory;
        private String systemResource;
        private Object user;
        private String pmJsona;
        private String documentCodes;
        private float partHeight;

        public long getDateline() {
            return dateline;
        }

        public void setDateline(long dateline) {
            this.dateline = dateline;
        }

        public float getPackageWidth() {
            return packageWidth;
        }

        public void setPackageWidth(float packageWidth) {
            this.packageWidth = packageWidth;
        }

        public String getProject() {
            return project;
        }

        public void setProject(String project) {
            this.project = project;
        }

        public float getAddedLength() {
            return addedLength;
        }

        public void setAddedLength(float addedLength) {
            this.addedLength = addedLength;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getAuditType() {
            return auditType;
        }

        public void setAuditType(String auditType) {
            this.auditType = auditType;
        }

        public String getPartCode() {
            return partCode;
        }

        public void setPartCode(String partCode) {
            this.partCode = partCode;
        }

        public String getPartName() {
            return partName;
        }

        public void setPartName(String partName) {
            this.partName = partName;
        }

        public String getProcessRecommendation() {
            return processRecommendation;
        }

        public void setProcessRecommendation(String processRecommendation) {
            this.processRecommendation = processRecommendation;
        }

        public String getPackageStypeName() {
            return packageStypeName;
        }

        public void setPackageStypeName(String packageStypeName) {
            this.packageStypeName = packageStypeName;
        }

        public float getSinglePackageHeight() {
            return singlePackageHeight;
        }

        public void setSinglePackageHeight(float singlePackageHeight) {
            this.singlePackageHeight = singlePackageHeight;
        }

        public float getPackageLength() {
            return packageLength;
        }

        public void setPackageLength(float packageLength) {
            this.packageLength = packageLength;
        }

        public int getDisabled() {
            return disabled;
        }

        public void setDisabled(int disabled) {
            this.disabled = disabled;
        }

        public float getRoughWeight() {
            return roughWeight;
        }

        public void setRoughWeight(float roughWeight) {
            this.roughWeight = roughWeight;
        }

        public String getCollectorName() {
            return collectorName;
        }

        public void setCollectorName(String collectorName) {
            this.collectorName = collectorName;
        }

        public float getAddedWidth() {
            return addedWidth;
        }

        public void setAddedWidth(float addedWidth) {
            this.addedWidth = addedWidth;
        }

        public String getPartMaterialName() {
            return partMaterialName;
        }

        public void setPartMaterialName(String partMaterialName) {
            this.partMaterialName = partMaterialName;
        }

        public float getSinglePackageWeight() {
            return singlePackageWeight;
        }

        public void setSinglePackageWeight(float singlePackageWeight) {
            this.singlePackageWeight = singlePackageWeight;
        }

        public float getPartLength() {
            return partLength;
        }

        public void setPartLength(float partLength) {
            this.partLength = partLength;
        }

        public float getPartWidth() {
            return partWidth;
        }

        public void setPartWidth(float partWidth) {
            this.partWidth = partWidth;
        }

        public float getSinglePackageWidth() {
            return singlePackageWidth;
        }

        public void setSinglePackageWidth(float singlePackageWidth) {
            this.singlePackageWidth = singlePackageWidth;
        }

        public float getAddedHeight() {
            return addedHeight;
        }

        public void setAddedHeight(float addedHeight) {
            this.addedHeight = addedHeight;
        }

        public float getNetWeight() {
            return netWeight;
        }

        public void setNetWeight(float netWeight) {
            this.netWeight = netWeight;
        }

        public float getPackageHeight() {
            return packageHeight;
        }

        public void setPackageHeight(float packageHeight) {
            this.packageHeight = packageHeight;
        }

        public int getPackageModelCount() {
            return packageModelCount;
        }

        public void setPackageModelCount(int packageModelCount) {
            this.packageModelCount = packageModelCount;
        }

        public float getSinglePackageLength() {
            return singlePackageLength;
        }

        public void setSinglePackageLength(float singlePackageLength) {
            this.singlePackageLength = singlePackageLength;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getLocations() {
            return locations;
        }

        public void setLocations(String locations) {
            this.locations = locations;
        }

        public String getIsHistory() {
            return isHistory;
        }

        public void setIsHistory(String isHistory) {
            this.isHistory = isHistory;
        }

        public String getSystemResource() {
            return systemResource;
        }

        public void setSystemResource(String systemResource) {
            this.systemResource = systemResource;
        }

        public Object getUser() {
            return user;
        }

        public void setUser(Object user) {
            this.user = user;
        }

        public String getPmJsona() {
            return pmJsona;
        }

        public void setPmJsona(String pmJsona) {
            this.pmJsona = pmJsona;
        }

        public String getDocumentCodes() {
            return documentCodes;
        }

        public void setDocumentCodes(String documentCodes) {
            this.documentCodes = documentCodes;
        }

        public float getPartHeight() {
            return partHeight;
        }

        public void setPartHeight(float partHeight) {
            this.partHeight = partHeight;
        }
    }
}
