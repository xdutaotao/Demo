package com.yankon.smart.model;

import com.yankon.smart.utils.Constants;

/**
 * Created by guzhenfu on 2015/8/5.
 */
public class SwitchsStatusInfo {
    public String id;

    public boolean state = true;
    public boolean key1 = true;
    public boolean key2 = true;
    public boolean key3 = true;

    public int mode = Constants.DEFAULT_MODE;

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof StatusInfo)) {
            return false;
        }
        StatusInfo other = (StatusInfo) o;
        return other.id.equals(id);
    }
}
