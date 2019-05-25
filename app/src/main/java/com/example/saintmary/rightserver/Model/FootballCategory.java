package com.example.saintmary.rightserver.Model;
//this Model package is used to bind the firebase database with the recyclerview
public class FootballCategory {
    private String Name;
    private String footballImage;
    private String OnGoingGame;
    private String Date;
    private String Time;


   /* scheduledGame=itemView.findViewById(R.id.txtOnGoingGame);
    schechuledDate=itemView.findViewById(R.id.txtDate);
    schechuledTime=itemView.findViewById(R.id.txtTime);*/



    public FootballCategory(){

    }


    public FootballCategory(String onGoingGame, String date, String time, String image) {
        OnGoingGame=onGoingGame;
        Date=date;
        Time=time;
        footballImage=image;
    }

    public String getOnGoingGame() {
        return OnGoingGame;
    }

    public void setOnGoingGame(String onGoingGame) {
        OnGoingGame = onGoingGame;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getFootballImage() {
        return footballImage;
    }

    public void setFootballImage(String footballImage) {
        this.footballImage = footballImage;
    }





}
