package com.yankon.smart.music;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.yankon.smart.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guzhenfu on 2015/11/10.
 */
public class MusicLoader {
    private static final String TAG = "com.example.nature.MusicLoader";

    private static ArrayList<MusicInfo> musicList = new ArrayList<>();

    private static MusicLoader musicLoader;

    private static ContentResolver contentResolver;

    private Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

    private String[] projection = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE
    };
    private String where =  "mime_type in ('audio/mpeg','audio/x-ms-wma') and bucket_display_name <> 'audio' and is_music > 0 " ;
    private String sortOrder = MediaStore.Audio.Media.DATA;

    public static MusicLoader instance(ContentResolver pContentResolver){
        if (musicLoader == null){
            synchronized (MusicLoader.class){
                if(musicLoader == null){
                    contentResolver = pContentResolver;
                    musicLoader = new MusicLoader();
                }

            }
        }
        return musicLoader;
    }

    private MusicLoader(){
        Cursor cursor = contentResolver.query(contentUri, null, null, null, null);
        if(cursor == null){
            LogUtils.v(TAG, "Line(37	)	Music Loader cursor == null.");
        }else if(!cursor.moveToFirst()){
            LogUtils.v(TAG,"Line(39	)	Music Loader cursor.moveToFirst() returns false.");
        }else{
            int displayNameCol = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int albumCol = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int idCol = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int durationCol = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int sizeCol = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE);
            int artistCol = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int urlCol = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            do{
                String title = cursor.getString(displayNameCol);
                String album = cursor.getString(albumCol);
                long id = cursor.getLong(idCol);
                int duration = cursor.getInt(durationCol);
                long size = cursor.getLong(sizeCol);
                String artist = cursor.getString(artistCol);
                String url = cursor.getString(urlCol);
                title = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));

                MusicInfo musicInfo = new MusicInfo( );
                musicInfo.setId(title);
                musicInfo.setTitle(title);
                musicInfo.setAuthor(album);
                musicInfo.setCover(album);
                musicInfo.setPlaytime(duration);
                musicInfo.setLength(duration + "");
                //musicInfo.setArtist(artist);
                musicInfo.setSrc(url);
                musicInfo.setSDsrc(url);
                musicList.add(musicInfo);

            }while(cursor.moveToNext());
        }
        cursor.close();
    }

    public ArrayList<MusicInfo> getMusicList(){
        return musicList;
    }

    public Uri getMusicUriById(long id){
        Uri uri = ContentUris.withAppendedId(contentUri, id);
        return uri;
    }

}
