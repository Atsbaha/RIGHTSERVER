
package com.example.saintmary.rightserver.Model;

public class Drink {
    private String drinkName, drinkImage, drinkDescription, drinkPrice, drinkDiscount, drinkMenuId, DrinkId, AvailabilityFlag;

    public Drink() {
    }

    public Drink(String name, String image, String description, String price, String discount, String menuId, String drinkId, String availabilityFlag) {
        drinkName= name;
        drinkImage= image;
        drinkDescription= description;
        drinkPrice = price;
        drinkDiscount = discount;
        drinkMenuId = menuId;
        DrinkId = drinkId;
        AvailabilityFlag = availabilityFlag;
    }



    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String name) {
        drinkName = name;
    }

    public String getDrinkImage() {
        return drinkImage;
    }

    public void setDrinkImage(String image) {
        drinkImage = image;
    }

    public String getDrinkDescription() {
        return drinkDescription;
    }

    public void setDrinkDescription(String description) {
        drinkDescription = description;
    }

    public String getDrinkPrice() {
        return drinkPrice;
    }

    public void setDrinkPrice(String price) {
        drinkPrice = price;
    }

    public String getDrinkDiscount() {
        return drinkDiscount;
    }

    public void setDrinkDiscount(String discount) {
        drinkDiscount = discount;
    }

    public String getDrinkMenuId() {
        return drinkMenuId;
    }

    public void setDrinkMenuId(String menuId) {
        drinkMenuId = menuId;
    }

    public String getDrinkId() {
        return DrinkId;
    }

    public void setDrinkId(String foodId) {
        DrinkId = foodId;
    }

    public String getAvailabilityFlag() {
        return AvailabilityFlag;
    }

    public void setAvailabilityFlag(String availabilityFlag) {
        AvailabilityFlag = availabilityFlag;
    }
}
