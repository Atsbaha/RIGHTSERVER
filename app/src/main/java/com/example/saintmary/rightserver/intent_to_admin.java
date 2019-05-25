package com.example.saintmary.rightserver;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class intent_to_admin extends AppCompatActivity {
    Button btnFoodOrder,btnFootballProgram,btnDrinkOrder,btnEnableUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_to_admin);
        btnFoodOrder=findViewById(R.id.btnFoodingOrder);
        btnFootballProgram=findViewById(R.id.btnFootballProgram);
//      //  btnTableReservation=findViewById(R.id.btnTableReservation);a
        btnDrinkOrder=findViewById(R.id.btnDrinkOrder);

        btnEnableUser=findViewById(R.id.btnEnableUser);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/TIMES.TTF");
        btnEnableUser.setTypeface(typeface);
        btnDrinkOrder.setTypeface(typeface);
        btnFootballProgram.setTypeface(typeface);
        btnFoodOrder.setTypeface(typeface);



        btnFoodOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(intent_to_admin.this,Home.class);
                startActivity(intent);
            }
        });

        btnDrinkOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(intent_to_admin.this,Drink_Home.class);
                startActivity(intent);
            }
        });
        btnFootballProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(intent_to_admin.this,FootballProgram_Home.class);
                startActivity(intent);
            }
        });

        btnEnableUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(intent_to_admin.this,EUser.class);
                startActivity(intent);
            }
        });

    }
}
