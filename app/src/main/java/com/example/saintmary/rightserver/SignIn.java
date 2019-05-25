package com.example.saintmary.rightserver;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saintmary.rightserver.Common.Common;
import com.example.saintmary.rightserver.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class SignIn extends AppCompatActivity {
    EditText edtPhone,edtPassword;
    Button btnSignIn;
    CheckBox ckbRemember;
    TextView txtForgetPwd;



    FirebaseDatabase db;
    DatabaseReference users;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        edtPassword=findViewById(R.id.edtPassword);
        edtPhone= findViewById(R.id.edtPhone);
        btnSignIn=findViewById(R.id.btnSignIn);

        ckbRemember=(CheckBox)findViewById(R.id.ckbRemember);
        txtForgetPwd=findViewById(R.id.txtForgetPwd);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/TIMES.TTF");
        edtPassword.setTypeface(typeface);
        edtPhone.setTypeface(typeface);
        btnSignIn.setTypeface(typeface);
        txtForgetPwd.setTypeface(typeface);




        //init Firebase
        db=FirebaseDatabase.getInstance();
        users=db.getReference("User");

        txtForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgetPwdDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ckbRemember.isChecked())
                {
                    Paper.book().write(Common.USER_KEY,edtPhone.getText().toString());
                    Paper.book().write(Common.PWD_KEY,edtPassword.getText().toString());

                }
                signInUser(edtPhone.getText().toString(),edtPassword.getText().toString());


            }
        });

    }

    private void showForgetPwdDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Forget password");
        builder.setMessage("Enter your secure code");

        LayoutInflater inflater=this.getLayoutInflater();
        View forget_view=inflater.inflate(R.layout.forget_password_layout,null);

        builder.setView(forget_view);
        builder.setIcon(R.drawable.ic_security_black_24dp);

        final EditText edtPhone=forget_view.findViewById(R.id.edtPhone);
        final EditText edtSecureCode=forget_view.findViewById(R.id.edtSecureCode);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //check if user available
                users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user =dataSnapshot.child(edtPhone.getText().toString())
                                .getValue(User.class);

                        if(user.getSecureCode().equals(edtSecureCode.getText().toString()))
                            Toast.makeText(SignIn.this, "Your Password is: "+user.getPassword(), Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(SignIn.this,"Wrong secure code",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    private void signInUser(String phone, String password) {
        final ProgressDialog mDialog=new ProgressDialog(SignIn.this);
        mDialog.setMessage("please waiting");
        mDialog.show();

        final String localPhone=phone;
        final String localPassword=password;
        //To read data at a path and listen for changes, use the addValueEventListener()
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(localPhone).exists())
                {
                    mDialog.dismiss();
                    User user =dataSnapshot.child(localPhone).getValue(User.class);
                    user.setPhone(localPhone);
                    if(Boolean.parseBoolean(user.getIsAdmin())) //if isStaff is true
                    {
                        if(user.getPassword().equals(localPassword))
                        {
                            //login ok
                            Intent login=new Intent(SignIn.this,intent_to_admin.class);
                            Common.currentUser=user;
                            startActivity(login);
                            finish();

                        }
                        else
                            Toast.makeText(SignIn.this,"wrong password",Toast.LENGTH_SHORT).show();
                    }

                    else if(Boolean.parseBoolean(user.getIsDJ())) //if isStaff is true
                    {
                        if(user.getPassword().equals(localPassword))
                        {
                            //login ok
                            Intent login=new Intent(SignIn.this,Music_Home.class);
                            Common.currentUser=user;
                            startActivity(login);
                            finish();

                        }
                        else
                            Toast.makeText(SignIn.this,"wrong password",Toast.LENGTH_SHORT).show();
                    }
                    //the system manager only see
                    else if(Boolean.parseBoolean(user.getIsSystemManager())) //if isStaff is true
                    {
                        if(user.getPassword().equals(localPassword))
                        {
                            //login ok
                            //  Intent login=new Intent(SignIn.this,Home.class);
                            Intent login=new Intent(SignIn.this,intent_to_admin.class);
                            Common.currentUser=user;
                            startActivity(login);
                            finish();

                        }
                        else
                            Toast.makeText(SignIn.this,"wrong password",Toast.LENGTH_SHORT).show();
                    }

                    else
                        Toast.makeText(SignIn.this,"please login with Admin account",Toast.LENGTH_SHORT).show();
                }
                else
                    mDialog.dismiss();
//                Toast.makeText(SignIn.this,"User not existed in Database",Toast.LENGTH_SHORT).show();//uncomment if needed

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
