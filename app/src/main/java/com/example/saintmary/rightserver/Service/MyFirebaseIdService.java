package com.example.saintmary.rightserver.Service;

import com.example.saintmary.rightserver.Common.Common;
import com.example.saintmary.rightserver.Model.Token;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseIdService extends FirebaseInstanceIdService {

@Override
    public void onTokenRefresh(){
    super.onTokenRefresh();
    String refreshedToken= FirebaseInstanceId.getInstance().getToken();
    updateToServer(refreshedToken);
}

    private void updateToServer(String refreshedToken) {
    //copy code from  client

        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference tokens=db.getReference("Tokens");
        Token data=new Token(refreshedToken,true);//because this is Server side
        tokens.child(Common.currentUser.getPhone()).setValue(data);
    }
}
