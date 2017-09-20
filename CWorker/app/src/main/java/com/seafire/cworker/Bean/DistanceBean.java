package com.seafire.cworker.Bean;

import java.util.List;

/**
 * Description:
 * Created by GUZHENFU on 2017/9/20 09:40.
 */

public class DistanceBean {
    private int status;
    private String info;
    private List<ResultBean> results;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<ResultBean> getResults() {
        return results;
    }

    public void setResults(List<ResultBean> results) {
        this.results = results;
    }

    public static class ResultBean{
        private int origin_id;
        private int dest_id;
        private long distance;
        private int duration;
        private String info;
        private int code;

        public int getOrigin_id() {
            return origin_id;
        }

        public void setOrigin_id(int origin_id) {
            this.origin_id = origin_id;
        }

        public int getDest_id() {
            return dest_id;
        }

        public void setDest_id(int dest_id) {
            this.dest_id = dest_id;
        }

        public long getDistance() {
            return distance;
        }

        public void setDistance(long distance) {
            this.distance = distance;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
}
