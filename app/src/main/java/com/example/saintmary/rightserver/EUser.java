package com.example.saintmary.rightserver;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.saintmary.rightserver.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EUser extends AppCompatActivity {
    Button btnPleaseEnbale;
    EditText edtPleaseEnbale;
 FirebaseDatabase database;
DatabaseReference table_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_euser);

       btnPleaseEnbale=findViewById(R.id.btnPleaseEnable);
        edtPleaseEnbale=findViewById(R.id.edtPleaseEnable);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/TIMES.TTF");
        btnPleaseEnbale.setTypeface(typeface);
        btnPleaseEnbale.setTypeface(typeface);


        FirebaseApp.initializeApp(this);
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("User").child(edtPleaseEnbale.getText().toString());

//        table_user = database.getReference("User").child(edtPleaseEnbale.getText().toString());



        btnPleaseEnbale.setOnClickListener(new View.OnClickListener() {
            //this is for yes or no like Enable and disable
//            final android.app.AlertDialog waitingDialog=new SpotsDialog(EUser.this);
//                waitingDialog.show();
            @Override
            public void onClick(View v) {

//      table_user=FirebaseDatabase.getInstance().getReference("User");
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User u = dataSnapshot.getValue(User.class);
                        String str = u.getPhone();


//                        if(dataSnapshot.child(edtPleaseEnbale.getText().toString()).exists())

//                        String str = u.getIsEnabled();

                        if(Boolean.parseBoolean(str)) //if isStaff is true
                        {

                                Toast.makeText(EUser.this,"wrong password",Toast.LENGTH_SHORT).show();
                        }

                        else {



                            Map<String, Object> passwordUpdate = new HashMap<>();
                            passwordUpdate.put("IsEnabled", "true");
//                            table_user.setValue(passwordUpdate);

                            final FirebaseDatabase database=FirebaseDatabase.getInstance();
                            final DatabaseReference table_user=database.getReference("User")/*.child(edtPleaseEnbale.getText().toString())*/;


                            table_user.child(edtPleaseEnbale.getText().toString())
                                    .updateChildren(passwordUpdate)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
//                                                waitingDialog.dismiss();
                                            Toast.makeText(EUser.this, "User is Enabled", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(EUser.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    });
                            Toast.makeText(EUser.this, "User Is Enabled", Toast.LENGTH_SHORT).show();
                        }
//                            else
//                                Toast.makeText(EUser.this,"User is Already Activated Or Enabled",Toast.LENGTH_SHORT).show();
                        }

//                        Toast.makeText(EUser.this,"User Is Not Registered",Toast.LENGTH_SHORT).show();
                       /*else if(str.equals("true")){
                            Toast.makeText(EUser.this,"User Is Not Registered",Toast.LENGTH_SHORT).show();

                        }else {
                            Map<String,Object> userEnale=new HashMap<>();
                            //password must be equal with the database field in firebase database
                            userEnale.put("isEnabled","true");
                            table_user.setValue( userEnale);

                        }*/



                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        });

    }

    /*private void EnablEUserDialog() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(EUser.this);
        alertDialog.setTitle("Enable User");
        alertDialog.setMessage("Enter Phone Number");

        LayoutInflater inflater=LayoutInflater.from(this);
        View layout_pwd=inflater.inflate(R.layout.user_enabing_layout,null);
        final EditText edtPhoneNumber=layout_pwd.findViewById(R.id.edtEbablingPhoneNumber);

//        final EditText edtPassword=layout_pwd.findViewById(R.id.edtPassword);
//        final EditText edtNewPassword=layout_pwd.findViewById(R.id.edtNewPassword);
//        final EditText edtRepeatPassword=layout_pwd.findViewById(R.id.edtRepeatPassword);

        alertDialog.setView(layout_pwd);
//        table_user = database.getReference("User");
         table_user=FirebaseDatabase.getInstance().getReference("User");




        //Button
        alertDialog.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //change password here

                //for use SpotDialog please use AlertDialog from android app not from v7 like above AlertDialog
                final android.app.AlertDialog waitingDialog=new SpotsDialog(EUser.this);
                waitingDialog.show();

//                User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);

                //check old password
                if(edtPhoneNumber.getText().toString().equals(Common.wawEnable.getPhone()))
                {
                    //check new password and the Repeat password or confirm password
//                    if (edtPhoneNumber.getText().toString().equals(edtRepeatPassword.getText().toString()))
//                    {

                    Map<String,Object> userEnale=new HashMap<>();
                    //password must be equal with the database field in firebase database
                    userEnale.put("isEnabled","true");

                    //make update
                    DatabaseReference user= FirebaseDatabase.getInstance().getReference("User");
                    user.child(Common.wawEnable.getPhone())
                            .updateChildren(userEnale)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    waitingDialog.dismiss();
                                    Toast.makeText(EUser.this,"User Is Enabled",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EUser.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                                }
                            });


//                    }
//                    else
//                    {
//                        Toast.makeText(Home.this, "New password doesn't match", Toast.LENGTH_SHORT).show();
//                    }
                }
                else{
                    Toast.makeText(EUser.this,"User in not registered",Toast.LENGTH_SHORT).show();
                }

            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();

    }*/
}
