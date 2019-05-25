package com.example.saintmary.rightserver;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saintmary.rightserver.Common.Common;
import com.example.saintmary.rightserver.Model.FootballCategory;
import com.example.saintmary.rightserver.ViewHolder.FootballProgramMenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

//import android.net.Uri;

public class FootballProgram_Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView name;
    FirebaseDatabase database;
    DatabaseReference footballProgram;


    FirebaseStorage storage;
    StorageReference storageReference;


    FirebaseRecyclerAdapter<FootballCategory, FootballProgramMenuViewHolder> adapter;

    RecyclerView recycler_menu;

    RecyclerView.LayoutManager layoutManager;


    //Add New Menu layout
//    MaterialEditText edtName;
    EditText scheduled_footballGame,getScheduled_football_Date,getScheduled_football_Time;
    //    FButton btnUpload, btnSelect;
    Button btnFootballUpload,btnFootballSelect;

    FootballCategory newFootballProgram;//this helps the admin to add new category to database
    //i think this is url
    Uri saveUri;//i think this url is from the gradle of project added like in the RIGHT project copy  and check if it woks


    DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_football_program__home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Football Management");
        setSupportActionBar(toolbar);

        //init firebasae
        database = FirebaseDatabase.getInstance();
        footballProgram = database.getReference("Football Program");//netsa

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();//eski abzi images zbl aetiwka mokro

//the two below lines are usefull but comment if any error happened
//       storage = FirebaseStorage.getInstance();
//        storageReference = storage.getReference();//images/ was in the quatation

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                showDialog();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.football_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.football_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set name for user
        View headerView = navigationView.getHeaderView(0);

        name = findViewById(R.id.ifwork);
//        name.setText("Atsbaha");//  Common.currentUser.getName() please see it again if i commented it works



        //init view
        recycler_menu = findViewById(R.id.football_recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);
        loadMenu();
    }

    //}
    private void showDialog() {//make this method empty if not working
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(FootballProgram_Home.this);
        alertDialog.setTitle("Add new Football Program");
        alertDialog.setMessage("please fill full information");

        //this inflater is used to inflate the view
//        View.inflate(getApplicationContext(), R.layout.add_new_menu_layout, null);

        LayoutInflater inflater=this.getLayoutInflater();
        View add_football_prigram_layout=inflater.inflate(R.layout.add_football_prigram_layout,null);

//        LayoutInflater inflater = LayoutInflater.from(Home.this); // or (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View viewMyLayout = inflater.inflate(R.layout.add_new_menu_layout, null);
//        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
//                (Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.add_new_menu_layout,null);

        scheduled_footballGame = add_football_prigram_layout.findViewById(R.id.scheduled_FootballGame);
        getScheduled_football_Date = add_football_prigram_layout.findViewById(R.id.scheduled_football_date);
        getScheduled_football_Time = add_football_prigram_layout.findViewById(R.id.scheduled_football_Time);
        btnFootballSelect= add_football_prigram_layout.findViewById(R.id.btnFootballSelect);
        btnFootballUpload = add_football_prigram_layout.findViewById(R.id.btnFootballUpload);

        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/TIMES.TTF");
        btnFootballUpload.setTypeface(typeface);
        btnFootballSelect.setTypeface(typeface);
        getScheduled_football_Date.setTypeface(typeface);
       scheduled_footballGame.setTypeface(typeface);
        getScheduled_football_Time.setTypeface(typeface);



        btnFootballSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();//let user select image from gallery and save url of this image

            }
        });





        btnFootballUpload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                uploadFootballProgram();
            }
        });

        alertDialog.setView(add_football_prigram_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_basket_black_24dp);



        //set Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item /*item was which*/) {
                dialogInterface.dismiss();//this dismisses the dialog this means as you click the button nothing to show
                // item.setName(btnUpload.getText().toString());
                //  category.child(key).setValue(item);

                //Here just create new category
                if(newFootballProgram!=null)
                {
                    footballProgram.push().setValue(newFootballProgram);
//                    Toast.makeText(Home.this,"New category",Toast.LENGTH_SHORT).show();
                    //view,id and duration change to Toast message if it is error
                    //the "drawer" below is from activity_home in which its id is drawer_layout
                    Snackbar.make(drawer,"New Football Program "+newFootballProgram.getOnGoingGame()+"Is added",Snackbar.LENGTH_SHORT).show();


                }


            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        alertDialog.show();
    }
    //do not delete this because it is original i commented it first run



    private void uploadFootballProgram() {
        if(saveUri!=null)
        {
            final ProgressDialog mDialog=new ProgressDialog(this);
            mDialog.setMessage("Uploading");
            mDialog.show();


            final StorageReference imageFolder=storageReference.child("images/"+ UUID.randomUUID().toString());
//            final StorageReference imageFolder =storageReference.child("Category").child(imageName);//here we cancelled the keyword "new"


            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            // mDialog.show(); try this
                            mDialog.dismiss();//this may cause to not run please comment and try
                            Toast.makeText(FootballProgram_Home.this,"Uploaded",Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
                                @Override
                                public void onSuccess(Uri saveUri)
                                {
                                    // set value for new category if image upload and we can get downlaod link
//                                    newFootballProgram=new FootballCategory(edtName.getText().toString(),saveUri.toString());


                                    newFootballProgram =new FootballCategory();
//                                    newFootballProgram.setName(edtName.getText().toString());
                                    newFootballProgram.setOnGoingGame(scheduled_footballGame.getText().toString());
                                    newFootballProgram.setDate(getScheduled_football_Date.getText().toString());
                                    newFootballProgram.setTime(getScheduled_football_Time.getText().toString());
                                    newFootballProgram.setFootballImage(saveUri.toString());
                                }
                            });
                        }
                    })



                    //com.google.android.gms.tasks
                    .addOnFailureListener(new  OnFailureListener(){
                        @Override
                        public void onFailure(@NonNull Exception e){
                            mDialog.dismiss();
                            Toast.makeText(FootballProgram_Home.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>(){
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            //donot worry about this error
                            double progress=(100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploaded"+progress+"%");

                        }
                    });

        }
        else
        {
            Toast.makeText(FootballProgram_Home.this, "please select Image", Toast.LENGTH_SHORT).show();
        }

    }


    //press ctrl + o
    @Override
    //this method is used for getting the uri of selected image you have to implement onActivityResult in your activity:
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Common.PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData() != null)
//        if(requestCode==Common.PICK_IMAGE_REQUEST && resultCode==RESULT_OK)
        {
            saveUri=data.getData();
            btnFootballSelect.setText("Image is Selected");
//        saveUri=data.getData();


        }


    }
    private void chooseImage() {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//i think this is images in the storageDatabase
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),Common.PICK_IMAGE_REQUEST);
//         startActivityForResult(intent,Common.PICK_IMAGE_REQUEST);

    }
    // adapter=new FirebaseRecyclerAdapter<Category, MenuViewHolder> means from Category to MenuViewHolder

    //use of necessary to load all the football program
    private void loadMenu() {
        adapter=new FirebaseRecyclerAdapter<FootballCategory, FootballProgramMenuViewHolder>(
                FootballCategory.class,
                R.layout.football_menu_item,
                FootballProgramMenuViewHolder.class,
                footballProgram
        ) {
            @Override
            protected void populateViewHolder(FootballProgramMenuViewHolder viewHolder, FootballCategory model, int position) {


//                viewHolder.txtMenuName.setText(model.getName());
                viewHolder.scheduledGame.setText(model.getOnGoingGame());
                viewHolder.schechuledDate.setText(model.getDate());
                viewHolder.schechuledTime.setText(model.getTime());

                Picasso.with(FootballProgram_Home.this).load(model.getFootballImage())//getBaseContext() in Home.this
                        .into(viewHolder.imageView);

               /* viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        //send Category id and start new activity
                        Intent foodList=new Intent(FootballProgram_Home.this,FoodList.class);
                        foodList.putExtra("CategoryId",adapter.getRef(position).getKey());
                        startActivity(foodList);
                    }
                });*/





            }
        };
        adapter.notifyDataSetChanged();//refresh data if it has been changed
        recycler_menu.setAdapter(adapter);
    }



    @Override//comment it if it is error
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.football_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement



        if (id == R.id.action_settings) {
            return false;
        }



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id == R.id.nav_menu)
        {

        }

        else if(id == R.id.nav_change_pwd)
        {
            showChangePasswordDialog();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showChangePasswordDialog() {

        AlertDialog.Builder alertDialog=new AlertDialog.Builder(FootballProgram_Home.this);
        alertDialog.setTitle("Change Password");
        alertDialog.setMessage("please Fill all Information");

        LayoutInflater inflater=LayoutInflater.from(this);
        View layout_pwd=inflater.inflate(R.layout.change_password_layout,null);

        final EditText edtPassword=layout_pwd.findViewById(R.id.edtPassword);
        final EditText edtNewPassword=layout_pwd.findViewById(R.id.edtNewPassword);
        final EditText edtRepeatPassword=layout_pwd.findViewById(R.id.edtRepeatPassword);

        alertDialog.setView(layout_pwd);

        //Button
        alertDialog.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //change password here

                //for use SpotDialog please use AlertDialog from android app not from v7 like above AlertDialog
                final android.app.AlertDialog waitingDialog=new SpotsDialog(FootballProgram_Home.this);
                waitingDialog.show();

                //check old password
                if(edtPassword.getText().toString().equals(Common.currentUser.getPassword()))
                {
                    //check new password and the Repeat password or confirm password
                    if (edtNewPassword.getText().toString().equals(edtRepeatPassword.getText().toString()))
                    {

                        Map<String,Object> passwordUpdate=new HashMap<>();
                        //password must be equal with the database field in firebase database
                        passwordUpdate.put("password",edtNewPassword.getText().toString());

                        //make update
                        DatabaseReference user=FirebaseDatabase.getInstance().getReference("User");
                        user.child(Common.currentUser.getPhone())
                                .updateChildren(passwordUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        waitingDialog.dismiss();
                                        Toast.makeText(FootballProgram_Home.this,"Password is updated",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(FootballProgram_Home.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                                    }
                                });


                    }
                    else
                    {
                        Toast.makeText(FootballProgram_Home.this, "New password doesn't match", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(FootballProgram_Home.this,"Wrong Old password",Toast.LENGTH_SHORT).show();
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
    }

    //update or delete


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE))//ab wushti getRef(int position) yemelkt
        {
//            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else if(item.getTitle().equals(Common.DELETE))//ab wushti getRef(int position) yemelkt
        {
            deleteFootballProgram(adapter.getRef(item.getOrder()).getKey());//deleting by using only the Reference
        }

        return super.onContextItemSelected(item);
    }
    private void deleteFootballProgram(String key) {
        footballProgram.child(key).removeValue();
        Toast.makeText(this,"The Program IS  Deleted",Toast.LENGTH_SHORT).show();
    }



}
