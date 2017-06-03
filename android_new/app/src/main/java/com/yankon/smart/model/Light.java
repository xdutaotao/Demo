package com.yankon.smart.model;

/**
 * Created by tian on 14/11/25:上午8:25.
 */
public class Light {
    public String name;
    public String model;
    public String mac;
    public String firmwareVersion;
    public int ip;
    public int subIP;
    public String remotePassword = "";
    public String adminPassword = "";
    public boolean state;
    public int color;
    public int brightness;
    public int CT;

    public int type;
    public int number;
    public int sens;
    public int lux;
    public boolean runmodel;

    public int id = -1;
    public boolean added;
    public boolean connected;
    public boolean selected;

    public boolean mediaState = false;
    public String uid = null;

    public boolean ap_state = false;
    public String ap_bssid;
    public String ap_ssid;
    public String ap_pass;

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Light)) {
            return false;
        }
        Light other = (Light) o;
        return (id >= 0 && id == other.id) || mac.equals(other.mac);
    }
}
