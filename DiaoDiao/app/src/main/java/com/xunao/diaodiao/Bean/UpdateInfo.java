package com.xunao.diaodiao.Bean;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by chenguo on 2016/4/12.
 */
public class UpdateInfo{
    private String download_url;
    private String version ;

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
