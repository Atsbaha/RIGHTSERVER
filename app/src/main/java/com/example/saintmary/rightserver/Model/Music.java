package com.example.saintmary.rightserver.Model;

public class Music {
    private String musicName, MusicianName,MenuId;

    public Music() {
    }

    public Music(String name, String musicianName) {
        musicName = name;
        MusicianName = musicianName;

    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String name) {
        musicName = name;
    }

    public String getMusicianName() {
        return MusicianName;
    }

    public void setMusicImage(String musicianName) {
        MusicianName= musicianName;
    }
    public void setMenuId(String menuId) {
        MenuId = menuId;
    }


}
