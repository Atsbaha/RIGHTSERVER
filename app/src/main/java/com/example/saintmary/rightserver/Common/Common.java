package com.example.saintmary.rightserver.Common;


import com.example.saintmary.rightserver.Model.User;
import com.example.saintmary.rightserver.Remote.APIService;
import com.example.saintmary.rightserver.Remote.FCMRetrofitClient;

import java.util.Calendar;
import java.util.Locale;

//this class is used to hold the data of the current user
public class Common {
    public static User currentUser;
    public static User wawEnable;

    public static final String DELETE = "Delete";
    public static final String USER_KEY = "Delete";
    public static final String PWD_KEY= "Delete";

    public static final String UPDATE="Update";
//    public static final String DELETE="Delete";

   public static final int PICK_IMAGE_REQUEST = 71;


   public static final String baseUrl="https://maps.googleapis.com";

    public static final String fcmUrl="https://fcm.googleapis.com/";

    public static String convertCodeToStatus(String code)
    {
        if(code.equals("0"))
            return "placed";
        else if(code.equals("1"))
            return "On The way";
        else
            return "Shipped";
    }

    public static APIService getFCMClient(){
        return FCMRetrofitClient.getClient(fcmUrl).create(APIService.class);
    }

public static String getDate(long time)
{
    Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
    calendar.setTimeInMillis(time);
    StringBuilder date=new StringBuilder(android.text.format.DateFormat.format("dd-MM-yyyy HH:mm"
            ,calendar)
            .toString());
    return date.toString();
}

}
