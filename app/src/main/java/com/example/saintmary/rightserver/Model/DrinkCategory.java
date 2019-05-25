package com.example.saintmary.rightserver.Model;
//this Model package is used to bind the firebase database with the recyclerview
public class DrinkCategory {
    private String drinkName;
    private String drinkImage;

    public DrinkCategory(){

    }

    public DrinkCategory(String drinkname, String drinkimage) {
        drinkName =drinkname;
        drinkImage=drinkimage;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName=drinkName;
    }

    public String getDrinkImage() {
        return drinkImage;
    }

    public void setDrinkImage(String image) {
        this.drinkImage=drinkImage;
    }
}
