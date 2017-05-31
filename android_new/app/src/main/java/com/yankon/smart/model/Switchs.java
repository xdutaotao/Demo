package com.yankon.smart.model;

/**
 * Created by guzhenfu on 2015/8/5.
 */
public class Switchs {
    public String name;
    public String model;
    public String mac;
    public String firmwareVersion;
    public int ip;
    public int subIP;
    public String remotePassword = "";
    public String adminPassword = "";
    public boolean state;
    public boolean key1;
    public boolean key2;
    public boolean key3;

    public int id = -1;
    public boolean added;
    public boolean connected;
    public boolean selected;

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Switchs)) {
            return false;
        }
        Switchs other = (Switchs) o;
        return (id >= 0 && id == other.id) || mac.equals(other.mac);
    }
}
