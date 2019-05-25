package com.example.saintmary.rightserver.Model;
public class User {
    private String IsDJ ;
    private String FirstName;
    private String LastName;
    private String Password;
    private String Phone;//this is used to call the requester if his or her address is not found
    private String IsAdmin;
    private String IsEnabled;
    private String secureCode;

    private String IsSystemManager;

    public User() {

    }

    //   we removed the String phone from the constructor
    public User(String firstName,String lastName, String password,String secureCode/*String phone*/) {
        FirstName = firstName;
        LastName=lastName;
        Password = password;
//        Phone=phone;
        IsAdmin = "false";//this is from server side
        IsEnabled="false";
//        IsDJ="false";
        this.secureCode=secureCode;

    }
    //press Alt+insert


    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getIsEnabled() {
        return IsEnabled;
    }

    public void setIsEnabled(String isEnabled) {
        IsEnabled = isEnabled;
    }

    public String getSecureCode() {
        return secureCode;
    }

    public void setSecureCode(String secureCode) {
        this.secureCode = secureCode;
    }

    public String getIsAdmin() {

        return IsAdmin;
    }
    public String getIsDJ() {
        return IsDJ;
    }

    public void setIsDJ(String isDJ) {
        IsDJ = isDJ;
    }

    public void setIsAdmin(String isAdmin) {
        IsAdmin= isAdmin;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getFirstName(){
        return FirstName;
    }
    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void LasttName(String lastName) {
        FirstName =lastName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
    public String getIsSystemManager() {
        return IsSystemManager;
    }

    public void setIsSystemManager(String isSystemManager) {
        IsSystemManager = isSystemManager;
    }

}
