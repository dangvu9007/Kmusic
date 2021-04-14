package com.dang.kmusic;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import model.AudioModel;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_WRITE_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addFragment();
    }

    private void addFragment() {
        AudioModel model = new AudioModel();
        Fragment fragment = ControlPlayerFragment.newInstance(false, model);
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frm_control, fragment);
        transaction.commit();
    }

    private boolean hasStoragePermission() {
        int permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    private void checkPermission() {
        if (hasStoragePermission()) {
            loadMusic();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadMusic();
        }
    }

    private void loadMusic() {
        Uri allSongMedia = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";

        List<AudioModel> data = new ArrayList<>();
        String[] projection = {MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION};


        Cursor cursor = getContentResolver().query(allSongMedia,projection,selection, new String[]{"%utm%"}, MediaStore.Audio.Media.TITLE);

        if (cursor!=null && cursor.moveToFirst()){
            do {
                AudioModel audio = new AudioModel();

                audio.setPath(cursor.getString(0));
                audio.setTitle(cursor.getString(1));
                audio.setArtist(cursor.getString(2));
                audio.setAlbum(cursor.getString(3));
                data.add(audio);

            }while (cursor.moveToNext());
            cursor.close();
        }
    }
}