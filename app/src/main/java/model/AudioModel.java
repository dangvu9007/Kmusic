package model;

public class AudioModel {
   private int id;
   private String path;
   private String nameMusic;
   private String artist;
   private String album;


    public AudioModel(int id, String path, String nameMusic, String artist, String album) {
        this.id = id;
        this.path = path;
        this.nameMusic = nameMusic;
        this.artist = artist;
        this.album = album;
    }

    public AudioModel() {
    }

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getNameMusic() {
        return nameMusic;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setNameMusic(String nameMusic) {
        this.nameMusic = nameMusic;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
