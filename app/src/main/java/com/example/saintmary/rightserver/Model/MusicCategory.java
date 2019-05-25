package com.example.saintmary.rightserver.Model;
//this Model package is used to bind the firebase database with the recyclerview
public class MusicCategory {
    private String musicName;
    private String MusicianName;

    public MusicCategory(){

    }

    public MusicCategory(String name, String musicianName) {
        musicName = name;
        MusicianName=musicianName;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String name) {
        musicName= name;
    }

    public String getMusicianName() {
        return MusicianName;
    }

    public void setMusianName(String musicianName) {
        MusicianName = musicianName;
    }
}
