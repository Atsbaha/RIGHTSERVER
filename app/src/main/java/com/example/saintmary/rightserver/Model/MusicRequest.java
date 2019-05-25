

package com.example.saintmary.rightserver.Model;

public class MusicRequest {
    private String phone;
    //    private String address;
    private String status;
    private String MusicName;
    private String MusicianName;
//    private List<Order> foods;//list of food order

    public MusicRequest () {
    }


    public MusicRequest(String phone, String MusicName, String MusicianName) {
        this.phone = phone;
        this.status="0";//default is 0,0:placed,1:shipping,2:shipped
        this.MusicianName=MusicianName;
        this.MusicName=MusicName;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public void setStatus(String status) {
        this.status =status;
    }

    public String getStatus() {
        return status;
    }

    public String getMusicName() {
        return MusicName;
    }

    public void setMusicName(String musicName) {
        MusicName = musicName;
    }

    public String getMusicianName() {
        return MusicianName;
    }

    public void setMusicianName(String musicianName) {
        MusicianName = musicianName;
    }

}


