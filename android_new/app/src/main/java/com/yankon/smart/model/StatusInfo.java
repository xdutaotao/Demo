package com.yankon.smart.model;

import com.yankon.smart.utils.Constants;

/**
 * Created by Evan on 15/1/15.
 */
public class StatusInfo {
    public String id;

    public boolean state = true;
    public int color = Constants.DEFAULT_COLOR;
    public int brightness = Constants.DEFAULT_BRIGHTNESS;
    public int CT = Constants.DEFAULT_CT;                   //Color & Temperature
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
