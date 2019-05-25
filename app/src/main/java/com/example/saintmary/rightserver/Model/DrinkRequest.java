

package com.example.saintmary.rightserver.Model;

import java.util.List;

public class DrinkRequest {
    private String phone;
    private String FirstName;
    private String LastName;
    private String address;
    private String total;
    private String status;
    private List<DrinkOrder> drinks;//list of food order

    public DrinkRequest() {
    }

    public DrinkRequest(String phone, String firstName, String lastName, String address, String total, List<DrinkOrder> drinks) {
        this.phone = phone;
        FirstName =firstName;
        LastName=lastName;
        this.address = address;
        this.total = total;
        this.drinks = drinks;
        this.status="0";//default is 0,0:placed,1:shipping,2:shipped
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String toatl) {
        this.total = toatl;
    }

    public List<DrinkOrder> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<DrinkOrder> drinks) {
        this.drinks = drinks;
    }

//    public String getStatus() {
//        return status;
//    }

    public void setStatus(String status) {
        this.status =status;
    }

    public String getStatus() {
        return status;
    }
}
