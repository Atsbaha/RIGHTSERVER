package com.example.saintmary.rightserver.Model;

public class Music_Order {
    private String MusicName;

    public Music_Order(){

    }

    public Music_Order(String musicName) {
        MusicName=musicName;
    }



    public String getMusicName() {
        return MusicName;
    }

    public void setMusicName(String musicName) {
        MusicName = musicName;
    }


}
