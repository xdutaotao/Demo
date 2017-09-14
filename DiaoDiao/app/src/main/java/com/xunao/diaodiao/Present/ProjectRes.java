package com.xunao.diaodiao.Present;

/**
 * Description:
 * Created by guzhenfu on 2017/9/14.
 */

public class ProjectRes {

    /**
     * project : {"project_wait":"0","project_doing":"0","project_apply":""}
     * supervisor : {"supervisor_wait":"0","supervisor_doing":"0","supervisor_apply":""}
     * maintenance : {"maintenance_wait":"0","maintenance_doing":"0","maintenance_apply":""}
     * mutual : 0
     * odd :
     */

    private ProjectBean project;
    private SupervisorBean supervisor;
    private MaintenanceBean maintenance;
    private String mutual;
    private OddBean odd;

    public ProjectBean getProject() {
        return project;
    }

    public void setProject(ProjectBean project) {
        this.project = project;
    }

    public SupervisorBean getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(SupervisorBean supervisor) {
        this.supervisor = supervisor;
    }

    public MaintenanceBean getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(MaintenanceBean maintenance) {
        this.maintenance = maintenance;
    }

    public String getMutual() {
        return mutual;
    }

    public void setMutual(String mutual) {
        this.mutual = mutual;
    }

    public OddBean getOdd() {
        return odd;
    }

    public void setOdd(OddBean odd) {
        this.odd = odd;
    }

    public static class ProjectBean {
        /**
         * project_wait : 0
         * project_doing : 0
         * project_apply :
         */

        private String project_wait;
        private String project_doing;
        private String project_apply;

        public String getProject_wait() {
            return project_wait;
        }

        public void setProject_wait(String project_wait) {
            this.project_wait = project_wait;
        }

        public String getProject_doing() {
            return project_doing;
        }

        public void setProject_doing(String project_doing) {
            this.project_doing = project_doing;
        }

        public String getProject_apply() {
            return project_apply;
        }

        public void setProject_apply(String project_apply) {
            this.project_apply = project_apply;
        }
    }

    public static class SupervisorBean {
        /**
         * supervisor_wait : 0
         * supervisor_doing : 0
         * supervisor_apply :
         */

        private String supervisor_wait;
        private String supervisor_doing;
        private String supervisor_apply;

        public String getSupervisor_wait() {
            return supervisor_wait;
        }

        public void setSupervisor_wait(String supervisor_wait) {
            this.supervisor_wait = supervisor_wait;
        }

        public String getSupervisor_doing() {
            return supervisor_doing;
        }

        public void setSupervisor_doing(String supervisor_doing) {
            this.supervisor_doing = supervisor_doing;
        }

        public String getSupervisor_apply() {
            return supervisor_apply;
        }

        public void setSupervisor_apply(String supervisor_apply) {
            this.supervisor_apply = supervisor_apply;
        }
    }

    public static class MaintenanceBean {
        /**
         * maintenance_wait : 0
         * maintenance_doing : 0
         * maintenance_apply :
         */

        private String maintenance_wait;
        private String maintenance_doing;
        private String maintenance_apply;

        public String getMaintenance_wait() {
            return maintenance_wait;
        }

        public void setMaintenance_wait(String maintenance_wait) {
            this.maintenance_wait = maintenance_wait;
        }

        public String getMaintenance_doing() {
            return maintenance_doing;
        }

        public void setMaintenance_doing(String maintenance_doing) {
            this.maintenance_doing = maintenance_doing;
        }

        public String getMaintenance_apply() {
            return maintenance_apply;
        }

        public void setMaintenance_apply(String maintenance_apply) {
            this.maintenance_apply = maintenance_apply;
        }
    }

    public static class OddBean{
        private String odd_wait;
        private String odd_doing;
        private String odd_apply;

        public String getOdd_wait() {
            return odd_wait;
        }

        public void setOdd_wait(String odd_wait) {
            this.odd_wait = odd_wait;
        }

        public String getOdd_doing() {
            return odd_doing;
        }

        public void setOdd_doing(String odd_doing) {
            this.odd_doing = odd_doing;
        }

        public String getOdd_apply() {
            return odd_apply;
        }

        public void setOdd_apply(String odd_apply) {
            this.odd_apply = odd_apply;
        }
    }
}
