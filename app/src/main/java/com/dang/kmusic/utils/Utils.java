package com.dang.kmusic.utils;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dang.kmusic.model.AudioModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Utils {
    private final String STORAGE_AUDIO = "com.dang.kmusic.STORAGE";
    private final String AUDIO_LIST_KEY = "com.dang.kmusic.AUDIOS";
    private final String AUDIO_INDEX_KEY = "com.dang.kmusic.INDEX";
    private final String AUDIO_DURATION_KEY = "com.dang.kmusic.DURATION";


    private SharedPreferences sharedPreferences;
    private Context context;

    private  ArrayList<AudioModel> list;

    public Utils(Context context) {
        this.context = context;
    }

    public void storeAudio(ArrayList<AudioModel> audios) {
        sharedPreferences = context.getSharedPreferences(STORAGE_AUDIO, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(audios);
        editor.putString(AUDIO_LIST_KEY, json);
        editor.apply();

    }

    public void storeIndex(int index) {
        sharedPreferences = context.getSharedPreferences(STORAGE_AUDIO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(AUDIO_INDEX_KEY, index);
        editor.apply();
    }
    public void storeDuration(int index,int duration) {
        sharedPreferences = context.getSharedPreferences(STORAGE_AUDIO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(AUDIO_INDEX_KEY, index);
        editor.putInt(AUDIO_DURATION_KEY,duration);
        editor.apply();
    }

    public int loadAudioLastIndex(){
        sharedPreferences  = context.getSharedPreferences(STORAGE_AUDIO,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(AUDIO_INDEX_KEY,-1);
    }

    public ArrayList<AudioModel> loadAudios(){
        sharedPreferences = context.getSharedPreferences(STORAGE_AUDIO,Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(AUDIO_LIST_KEY,null);
        Type type = new TypeToken<ArrayList<AudioModel>>() {
        }.getType();
        return gson.fromJson(json,type);

    }
    public void getAllAudios() {
        Uri allSongMedia = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";

        list = new ArrayList<AudioModel>();
        String[] projection = {MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION};


        Cursor cursor = context.getContentResolver().query(allSongMedia, projection, null, null, selection);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                AudioModel audio = new AudioModel();

                audio.setPath(cursor.getString(cursor.getColumnIndex(projection[0])));
                audio.setTitle(cursor.getString(cursor.getColumnIndex(projection[1])));
                audio.setArtist(cursor.getString(cursor.getColumnIndex(projection[2])));
                audio.setAlbum(cursor.getString(cursor.getColumnIndex(projection[3])));
                audio.setDuration(cursor.getInt(cursor.getColumnIndex(projection[4])));
                list.add(audio);

            } while (cursor.moveToNext());
            cursor.close();
            storeAudio(list);
        }

    }


    public void clearCache(){
        sharedPreferences = context.getSharedPreferences(STORAGE_AUDIO,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
    //time conversion
    public String timerConversion(int value) {
        String audioTime;
        int dur = (int) value;
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        if (hrs > 0) {
            audioTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        } else {
            audioTime = String.format("%02d:%02d", mns, scs);
        }
        return audioTime;
    }
}
