package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/16.
 */

public class ApplyProjRes {
    private int applyCount;
    private List<ApplicantBean> applicants;

    public int getApplyCount() {
        return applyCount;
    }

    public void setApplyCount(int applyCount) {
        this.applyCount = applyCount;
    }

    public List<ApplicantBean> getApplicants() {
        return applicants;
    }

    public void setApplicants(List<ApplicantBean> applicants) {
        this.applicants = applicants;
    }

    public static class ApplicantBean{
        private String name;
        private String point;
        private String experience;
        private int technician_id;
        private int apply_id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPoint() {
            return point;
        }

        public void setPoint(String point) {
            this.point = point;
        }

        public String getExperience() {
            return experience;
        }

        public void setExperience(String experience) {
            this.experience = experience;
        }

        public int getTechnician_id() {
            return technician_id;
        }

        public void setTechnician_id(int technician_id) {
            this.technician_id = technician_id;
        }

        public int getApply_id() {
            return apply_id;
        }

        public void setApply_id(int apply_id) {
            this.apply_id = apply_id;
        }
    }

}
