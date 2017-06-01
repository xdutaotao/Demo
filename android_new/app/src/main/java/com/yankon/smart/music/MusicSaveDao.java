package com.yankon.smart.music;

import org.cybergarage.upnp.Device;

/**
 * Created by guzhenfu on 2015/11/13.
 */
public class MusicSaveDao {
    private volatile static MusicSaveDao musicSaveDao;
    public static DeviceDao dao = DeviceDao.getDeviceDao();
    private Device mDevice;
    private String mMusicLengthString;
    private String mTitle;
    private int mVolume;
    private int postDevice;

    private MusicSaveDao(){}

    public static MusicSaveDao getInstance(){
        if (musicSaveDao == null){
            synchronized (MusicSaveDao.class) {
                if (musicSaveDao == null) {
                    musicSaveDao = new MusicSaveDao();
                }
            }
        }
        return musicSaveDao;
    }

    public Device getDevice() {
        return mDevice;
    }

    public void setDevice(Device mDevice) {
        this.mDevice = mDevice;
    }

    public String getMusicLengthString() {
        return mMusicLengthString;
    }

    public void setMusicLengthString(String mMusicLengthString) {
        this.mMusicLengthString = mMusicLengthString;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getVolume() {
        return mVolume;
    }

    public void setVolume(int mVolume) {
        this.mVolume = mVolume;
    }

    public int getPostDevice() {
        return postDevice;
    }

    public void setPostDevice(int postDevice) {
        this.postDevice = postDevice;
    }
}
