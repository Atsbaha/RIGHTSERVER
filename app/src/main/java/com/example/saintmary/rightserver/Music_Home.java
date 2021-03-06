

package com.example.saintmary.rightserver;

//29 and 50 secods check if you are allowed to read and write the database firebase
//go to storage->rules make it true after if

        import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.saintmary.rightserver.Model.MusicCategory;
import com.example.saintmary.rightserver.ViewHolder.MusicMenuViewHolder;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

//import android.net.Uri;

public class Music_Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView name;
    FirebaseDatabase database;
    DatabaseReference categories;


    FirebaseStorage storage;
    StorageReference storageReference;


    FirebaseRecyclerAdapter<MusicCategory, MusicMenuViewHolder> adapter;

    RecyclerView music_recycler_menu;

    RecyclerView.LayoutManager layoutManager;


    //Add New Menu layout
//    MaterialEditText edtName;
    EditText edtNameOfMusician,edtNameOfMusic;
    //    FButton btnUpload, btnSelect;
    Button btnUpload, btnSelect;

    MusicCategory newMusicCategory;//this helps the admin to add new category to database
    //i think this is url
    Uri saveUri;//i think this url is from the gradle of project added like in the RIGHT project copy  and check if it woks


    DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music__home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu Management");
        setSupportActionBar(toolbar);

        //init firebasae
        database = FirebaseDatabase.getInstance();
        categories = database.getReference("MusicCategory");//netsa

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();//eski abzi images zbl aetiwka mokro

//the two below lines are usefull but comment if any error happened
//       storage = FirebaseStorage.getInstance();
//        storageReference = storage.getReference();//images/ was in the quatation

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                showDialog();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.music_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.music_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set name for user
        View headerView = navigationView.getHeaderView(0);

        name = findViewById(R.id.ifwork);
//        name.setText("Atsbaha");//  Common.currentUser.getName() please see it again if i commented it works



        //init view
        music_recycler_menu = findViewById(R.id.music_recycler_menu);
        music_recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        music_recycler_menu.setLayoutManager(layoutManager);
        loadMusicMenu();
    }
    //}
    private void showDialog() {//make this method empty if not working
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Music_Home.this);
        alertDialog.setTitle("Add new Music Category");
        alertDialog.setMessage("please fill full information");

        //this inflater is used to inflate the view
//        View.inflate(getApplicationContext(), R.layout.add_new_menu_layout, null);

        LayoutInflater inflater=this.getLayoutInflater();
        View add_menu_layout=inflater.inflate(R.layout.add_new_music_layout,null);

//        LayoutInflater inflater = LayoutInflater.from(Home.this); // or (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View viewMyLayout = inflater.inflate(R.layout.add_new_menu_layout, null);
//        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
//                (Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.add_new_menu_layout,null);

        edtNameOfMusician = add_menu_layout.findViewById(R.id.edtNameOfMusician);
        edtNameOfMusic=add_menu_layout.findViewById(R.id.edtNameOfMusic);

        btnSelect = add_menu_layout.findViewById(R.id.btnSelect);
        btnUpload = add_menu_layout.findViewById(R.id.btnUpload);







        // Set the old name of category
        // btnUpload.setText(item.getName());

        //Event for Button
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseMusic();//let user select image from gallery and save url of this image
                /*

                if(ContextCompat.checkSelfPermission(Home.this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){

                uploadImage();
                }else
                    ActivityCompat.requestPermissions(Home.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Common.PICK_IMAGE_REQUEST);

                 */

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                uploadMusic();
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_basket_black_24dp);



        //set Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item /*item was which*/) {
                dialogInterface.dismiss();//this dismisses the dialog this means as you click the button nothing to show
                // item.setName(btnUpload.getText().toString());
                //  category.child(key).setValue(item);

                //Here just create new category
                if(newMusicCategory!=null)
                {
                    categories.push().setValue(newMusicCategory);
//                    Toast.makeText(Home.this,"New category",Toast.LENGTH_SHORT).show();
                    //view,id and duration change to Toast message if it is error
                    //the "drawer" below is from activity_home in which its id is drawer_layout
                    Snackbar.make(drawer,"New category"+newMusicCategory.getMusicName()+"was added",Snackbar.LENGTH_SHORT).show();


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



    private void uploadMusic() {
        if(saveUri!=null)
        {
            final ProgressDialog mDialog=new ProgressDialog(this);
            mDialog.setMessage("Uploading");
            mDialog.show();

            //the ff UUID is in import java.util
            //UUID stands for universally unique identifier (UUID)
//            String imageName=UUID.randomUUID().toString();//.child("images/*"+imageName);
//            final String imageName=System.currentTimeMillis()+"";
//            final String filename=System.currentTimeMillis()+".jpg";
//            final String filename1=System.currentTimeMillis()+"";
//            String imageName=UUID.randomUUID().toString();
//            storage = FirebaseStorage.getInstance();
//            storageReference = storage.getReference("images");
//            StorageReference  storageReference=storage.getReference();
            final StorageReference imageFolder=storageReference.child("music/"+ UUID.randomUUID().toString());
//            final StorageReference imageFolder =storageReference.child("Category").child(imageName);//here we cancelled the keyword "new"


            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            // mDialog.show(); try this
                            mDialog.dismiss();//this may cause to not run please comment and try
                            Toast.makeText(Music_Home.this,"Uploaded",Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
                                @Override
                                public void onSuccess(Uri saveUri)
                                {
                                    // set value for new category if image upload and we can get downlaod link
                                    newMusicCategory= new MusicCategory();
                                    newMusicCategory.setMusianName(edtNameOfMusician.getText().toString());
                                    newMusicCategory.setMusicName(edtNameOfMusic.getText().toString());
                                    saveUri.toString();



                                   /*


                                    newMusicCategory= new MusicCategory(edtName.getText().toString(),saveUri.toString());*/


                                }
                            });
                        }
                    })




                    //com.google.android.gms.tasks
                    .addOnFailureListener(new  OnFailureListener(){
                        @Override
                        public void onFailure(@NonNull Exception e){
                            mDialog.dismiss();
                            Toast.makeText(Music_Home.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

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
            Toast.makeText(Music_Home.this, "please select Music", Toast.LENGTH_SHORT).show();
        }

    }



    //press ctrl + o
    @Override
    //this method is used for getting the uri of selected image you have to implement onActivityResult in your activity:
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== Common.PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData() != null)
//        if(requestCode==Common.PICK_IMAGE_REQUEST && resultCode==RESULT_OK)
        {
            saveUri=data.getData();
            btnSelect.setText("Music is Selected");
//        saveUri=data.getData();


        }


    }
    private void chooseMusic() {
        Intent intent=new Intent(Intent.ACTION_PICK);
//        setContentType("audio/mpeg")
        intent.setType("audio/*");//i think this is images in the storageDatabase
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Music"),Common.PICK_IMAGE_REQUEST);
//         startActivityForResult(intent,Common.PICK_IMAGE_REQUEST);

    }
    // adapter=new FirebaseRecyclerAdapter<Category, MenuViewHolder> means from Category to MenuViewHolder
    private void loadMusicMenu() {
        adapter=new FirebaseRecyclerAdapter<MusicCategory, MusicMenuViewHolder>(
                MusicCategory.class,
                R.layout.music_menu_item,
                MusicMenuViewHolder.class,
                categories
        ) {
            @Override
            protected void populateViewHolder(MusicMenuViewHolder viewHolder, MusicCategory model, final int position) {


                viewHolder.txtMusicMenuName.setText(model.getMusicName());
                viewHolder.MusicianMenuName.setText(model.getMusicianName());
                /*Picasso.with(Music_Home.this).load(model.getAudioMusic())//getBaseContext() in Home.this
                        .into((Target) viewHolder.MusicAudioView);*/



              /*  viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        //send Category id and start new activity
                        Intent foodList=new Intent(Music_Home.this,FoodList.class);
                        foodList.putExtra("CategoryId",adapter.getRef(position).getKey());
                        startActivity(foodList);
                    }
                });*/




            }
        };
        adapter.notifyDataSetChanged();//refresh data if it has been changed
        music_recycler_menu.setAdapter(adapter);
    }



    @Override//comment it if it is error
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.music_drawer_layout);
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
        if(id == R.id.nav_music_menu)
        {
//            Intent orders=new Intent(Music_Home.this,OrderStatus.class);
//            startActivity(orders);
        }
        else if (id == R.id.nav_music_cart) {
//            Intent cartIntent=new Intent(Music_Home.this,music_cart.class);//create musicCart class
//            startActivity(cartIntent);
        } else if (id == R.id.nav_music_orders) {
            Intent orderIntent=new Intent(Music_Home.this,music_orderstatus.class);//create MusicOrderStatus class
            startActivity(orderIntent);

        } else if (id == R.id.nav_log_out) {
            Intent signIn=new Intent(Music_Home.this,SignIn.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signIn);

        }

        else if(id == R.id.nav_change_pwd)
        {
            showChangePasswordDialog();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.music_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showChangePasswordDialog() {

        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Music_Home.this);
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
                final android.app.AlertDialog waitingDialog=new SpotsDialog(Music_Home.this);
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
                                        Toast.makeText(Music_Home.this,"Password is updated",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Music_Home.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                                    }
                                });


                    }
                    else
                    {
                        Toast.makeText(Music_Home.this, "New password doesn't match", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(Music_Home.this,"Wrong Old password",Toast.LENGTH_SHORT).show();
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
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else if(item.getTitle().equals(Common.DELETE))//ab wushti getRef(int position) yemelkt
        {
            deleteCategory(adapter.getRef(item.getOrder()).getKey());//deleting by using only the Reference
        }

        return super.onContextItemSelected(item);
    }
    private void deleteCategory(String key) {
        categories.child(key).removeValue();
        Toast.makeText(this,"Item Deleted",Toast.LENGTH_SHORT).show();
    }

    private void showUpdateDialog(final String key, final MusicCategory item) {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Music_Home.this);
        alertDialog.setTitle("Update new Category");
        alertDialog.setMessage("please fill full information");

        //this inflater is used to inflate the view
        //        View.inflate(getApplicationContext(), R.layout.add_new_menu_layout, null);

        LayoutInflater inflater=this.getLayoutInflater();
        View addMenuLayout=inflater.inflate(R.layout.add_new_music_layout,null);

        //        LayoutInflater inflater = LayoutInflater.from(Home.this); // or (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //        View viewMyLayout = inflater.inflate(R.layout.add_new_menu_layout, null);
        //        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
        //                (Context.LAYOUT_INFLATER_SERVICE);
        //        View view = inflater.inflate(R.layout.add_new_menu_layout,null);

        edtNameOfMusician = addMenuLayout.findViewById(R.id.edtNameOfMusician);
        edtNameOfMusic = addMenuLayout.findViewById(R.id.edtNameOfMusic);

        btnSelect = addMenuLayout.findViewById(R.id.btnSelect);
        btnUpload = addMenuLayout.findViewById(R.id.btnUpload);

        //set default name
        edtNameOfMusician.setText(item.getMusicianName());
        edtNameOfMusic.setText(item.getMusicName());

        // Set the old name of category
//          btnUpload.setText(item.getName());

        //Event for Button
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseMusic();//let user select image from gallery and save url of this image

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                changeImage(item);
            }
        });

        alertDialog.setView(addMenuLayout);
        alertDialog.setIcon(R.drawable.ic_shopping_basket_black_24dp);
        //set Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i /*int i was int item ///////*/) {
                dialogInterface.dismiss();//this dismisses the dialog this means as you click the button nothing to show
                item.setMusianName(btnUpload.getText().toString());
                item.setMusicName(btnUpload.getText().toString());

//                category.child(key).setValue(item);

                //update information

                item.setMusianName(btnUpload.getText().toString());
                item.setMusicName(btnUpload.getText().toString());
                categories.child(key).setValue(item);//check by replacing the item by "i" and run




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
    //to update the image
    private void changeImage(final MusicCategory item) {
        if(saveUri!=null)
        {
            final ProgressDialog mDialog=new ProgressDialog(this);
            mDialog.setMessage("Uploading");
            mDialog.show();

            //the ff UUID is in import java.util
            //UUID stands for universally unique identifier (UUID)
            String imageName=UUID.randomUUID().toString();//.child("images/*"+imageName);
            //final StorageReference imageFolder=new storageReference.child("images"+imageName);
            final StorageReference imageFolder = storageReference.child("music/"+imageName);//here we cancelled the keyword "new"
            // final StorageReference imageFolder=new storageReference.child("images/*"+imageName);// i tried this simply storageReference.child("images);it works
            // storageReference.child imageFolder = new storageReference.child("images");//this works check it
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            mDialog.dismiss();
                            Toast.makeText(Music_Home.this,"Uploaded",Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
                                @Override
                                public void onSuccess(Uri uri)
                                {
                                    //this is like creating new category
                                    item.setMusianName(btnUpload.getText().toString());
                                    item.setMusicName(btnUpload.getText().toString());
                                }
                            });
                        }
                    })
                    //com.google.android.gms.tasks
                    .addOnFailureListener(new  OnFailureListener(){
                        @Override
                        public void onFailure(@NonNull Exception e){
                            mDialog.dismiss();
                            Toast.makeText(Music_Home.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

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

    }
}

