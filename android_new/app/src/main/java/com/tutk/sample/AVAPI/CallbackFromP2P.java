package com.tutk.sample.AVAPI;

/**
 * Created by anand on 9/10/15.
 */
public interface CallbackFromP2P {
    void message(String message);
    void enableBackButton(boolean b);
    void streamSucess(boolean b);
}
