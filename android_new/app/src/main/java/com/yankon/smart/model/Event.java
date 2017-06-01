package com.yankon.smart.model;

import android.os.Message;

import com.yankon.smart.music.MusicInfo;

import java.util.ArrayList;

/**
 * Created by sunup_002 on 2015/12/28.
 */
public class Event {
    public static class GetMusicListEvent{
        private ArrayList<MusicInfo> musicList;
        public GetMusicListEvent(ArrayList<MusicInfo> musicList){
            this.musicList = musicList;
        }

        public ArrayList<MusicInfo> getMusicList() {
            return musicList;
        }
    }

    public static class SendMusicMsgEvent{
        private Message message;
        public SendMusicMsgEvent(Message msg){
            this.message = msg;
        }
        public Message getMusicMsg(){
            return message;
        }
    }

    public static class PostDeviceEvent{
        private int postDevice;
        public PostDeviceEvent(int postDevice){
            this.postDevice = postDevice;
        }

        public int getPostDevice(){
            return postDevice;
        }
    }

    public static class VideoEvent{
        private int type;
        public VideoEvent(int postDevice){
            this.type = postDevice;
        }

        public int getType(){
            return type;
        }
    }
}
