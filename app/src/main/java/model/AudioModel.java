package model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class AudioModel implements Serializable {
    private String path;
    private String title;
    private String artist;
    private String album;
    private Bitmap albumArt;

    public AudioModel(String path, String title, String artist, String album) {
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.album = album;
    }

    public AudioModel() {
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

    public void setAlbum(String album) {
        this.album = album;
    }

    public Bitmap getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(Bitmap albumArt) {
        this.albumArt = albumArt;
    }
}
