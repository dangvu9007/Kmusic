package com.dang.kmusic.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class AudioModelP implements Serializable {
    private String path;
    private String title;
    private String artist;
    private String duration;
    private String album;
    private Bitmap albumArt;
    private boolean isfavorite = false;

    public AudioModelP(String path, String title, String artist, String duration, String album, Bitmap albumArt, boolean isfavorite) {
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.album = album;
        this.albumArt = albumArt;
        this.isfavorite = isfavorite;
    }

    public AudioModelP() {
    }


    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }


    public void setPath(String path) {
        this.path = path;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Bitmap getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(Bitmap albumArt) {
        this.albumArt = albumArt;
    }

    public boolean isIsfavorite() {
        return isfavorite;
    }

    public void setIsfavorite(boolean isfavorite) {
        this.isfavorite = isfavorite;
    }
}
