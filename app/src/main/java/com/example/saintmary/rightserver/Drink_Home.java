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
import com.example.saintmary.rightserver.Interface.ItemClickListener;
import com.example.saintmary.rightserver.Model.DrinkCategory;
import com.example.saintmary.rightserver.Model.Token;
import com.example.saintmary.rightserver.ViewHolder.DrinkMenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
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

public class Drink_Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView name;
    FirebaseDatabase database;
    DatabaseReference categories,table_user;


    FirebaseStorage storage;
    StorageReference storageReference;


    FirebaseRecyclerAdapter<DrinkCategory, DrinkMenuViewHolder> adapter;

    RecyclerView recycler_menu;

    RecyclerView.LayoutManager layoutManager;


    //Add New Menu layout
//    MaterialEditText edtName;
    EditText edtName;
    //    FButton btnUpload, btnSelect;
    Button btnUpload, btnSelect;

    DrinkCategory newCategory;//this helps the admin to add new category to database
    //i think this is url
    Uri saveUri;//i think this url is from the gradle of project added like in the RIGHT project copy  and check if it woks


    DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink__home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.drink_toolbar);
        toolbar.setTitle("Menu Management");
        setSupportActionBar(toolbar);

        //init firebasae
        database = FirebaseDatabase.getInstance();
        categories = database.getReference("DrinkCategory");//netsa

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();//eski abzi images zbl aetiwka mokro

//the two below lines are usefull but comment if any error happened
//       storage = FirebaseStorage.getInstance();
//        storageReference = storage.getReference();//images/ was in the quatation

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.drink_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                showDialog();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drink_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.drink_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set name for user
        View headerView = navigationView.getHeaderView(0);

        name = findViewById(R.id.drinktxtFullName);
//        name.setText("Atsbaha");//  Common.currentUser.getName() please see it again if i commented it works



        //init view
        recycler_menu = findViewById(R.id.drink_recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);
        loadMenu();

        updateToken(FirebaseInstanceId.getInstance().getToken());
    }
    private void updateToken(String token)
    {

        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference tokens=db.getReference("Tokens");
        Token data=new Token(token,true);//because this token is Server side
        tokens.child(Common.currentUser.getPhone()).setValue(token);
    }
    //}
    private void showDialog() {//make this method empty if not working
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Drink_Home.this);
        alertDialog.setTitle("Add new Category");
        alertDialog.setMessage("please fill full information");

        //this inflater is used to inflate the view
//        View.inflate(getApplicationContext(), R.layout.add_new_menu_layout, null);

        LayoutInflater inflater=this.getLayoutInflater();
        View add_menu_layout=inflater.inflate(R.layout.add_new_menu_layout,null);

//        LayoutInflater inflater = LayoutInflater.from(Home.this); // or (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View viewMyLayout = inflater.inflate(R.layout.add_new_menu_layout, null);
//        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
//                (Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.add_new_menu_layout,null);

        edtName = add_menu_layout.findViewById(R.id.edtName);
        btnSelect = add_menu_layout.findViewById(R.id.btnSelect);
        btnUpload = add_menu_layout.findViewById(R.id.btnUpload);







        // Set the old name of category
        // btnUpload.setText(item.getName());

        //Event for Button
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();//let user select image from gallery and save url of this image
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
                uploadImage();
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
                if(newCategory!=null)
                {
                    categories.push().setValue(newCategory);
//                    Toast.makeText(Home.this,"New category",Toast.LENGTH_SHORT).show();
                    //view,id and duration change to Toast message if it is error
                    //the "drawer" below is from activity_home in which its id is drawer_layout
                    Snackbar.make(drawer,"New category"+newCategory.getDrinkName()+"was added",Snackbar.LENGTH_SHORT).show();


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



    private void uploadImage() {
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
            final StorageReference imageFolder=storageReference.child("images/"+ UUID.randomUUID().toString());
//            final StorageReference imageFolder =storageReference.child("Category").child(imageName);//here we cancelled the keyword "new"


            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            // mDialog.show(); try this
                            mDialog.dismiss();//this may cause to not run please comment and try
                            Toast.makeText(Drink_Home.this,"Uploaded",Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
                                @Override
                                public void onSuccess(Uri saveUri)
                                {
                                    // set value for new category if image upload and we can get downlaod link
                                    newCategory=new DrinkCategory(edtName.getText().toString(),saveUri.toString());
                                }
                            });
                        }
                    })


                    /*  .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                          @Override
                          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                              String url=taskSnapshot.getUploadSessionUri().toString();
                              DatabaseReference reference=database.getReference();
                              reference.child(filename1).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                  @Override
                                  public void onComplete(@NonNull Task<Void> task) {
                                      if(task.isSuccessful()){
                                          Toast.makeText(Home.this,"file succesfuly uploaded",Toast.LENGTH_SHORT).show();
                                      }else {
                                          Toast.makeText(Home.this,"file not succesfuly uploaded",Toast.LENGTH_SHORT).show();

                                      }


                                  }
                              });
                          }
                      })*/


                    //com.google.android.gms.tasks
                    .addOnFailureListener(new  OnFailureListener(){
                        @Override
                        public void onFailure(@NonNull Exception e){
                            mDialog.dismiss();
                            Toast.makeText(Drink_Home.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

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
            Toast.makeText(Drink_Home.this, "please select Image", Toast.LENGTH_SHORT).show();
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
            btnSelect.setText("Image is Selected");
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
    private void loadMenu() {
        adapter=new FirebaseRecyclerAdapter<DrinkCategory, DrinkMenuViewHolder>(
                DrinkCategory.class,
                R.layout.drink_menu_item,
                DrinkMenuViewHolder.class,
                categories
        ) {
            @Override
            protected void populateViewHolder(DrinkMenuViewHolder viewHolder, DrinkCategory model, int position) {


                viewHolder.txtDrinkMenuName.setText(model.getDrinkName());
                Picasso.with(Drink_Home.this).load(model.getDrinkImage())//getBaseContext() in Home.this
                        .into(viewHolder.DrinkImageView);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        //send Category id and start new activity
                        Intent drinkList=new Intent(Drink_Home.this,DrinkList.class);
                        drinkList.putExtra("CategoryId",adapter.getRef(position).getKey());
                        startActivity(drinkList);
                    }
                });


                //try this online

              /*  viewHolder.onCreateContextMenu(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

                        menu.setHeaderTitle("Select the Picture");
                       /* menu.add(0,0, Common.UPDATE,viewHolder.getAdapterPosition());//make MenuViewHolder final if you want to correct this error
                        menu.add(0,1,getAdapterPosition(),Common.DELETE);///////////
                    }
                });*/




            }
        };
        adapter.notifyDataSetChanged();//refresh data if it has been changed
        recycler_menu.setAdapter(adapter);
    }



    @Override//comment it if it is error
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drink_drawer_layout);
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
        if(id == R.id.drink_nav_orders)
        {
            Intent orders=new Intent(Drink_Home.this,DrinkOrderStatus.class);
            startActivity(orders);
        }

        else if(id == R.id.nav_change_pwd)
        {
            showChangePasswordDialog();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drink_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showChangePasswordDialog() {

        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Drink_Home.this);
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
                final android.app.AlertDialog waitingDialog=new SpotsDialog(Drink_Home.this);
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
                                        Toast.makeText(Drink_Home.this,"Password is updated",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Drink_Home.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                                    }
                                });


                    }
                    else
                    {
                        Toast.makeText(Drink_Home.this, "New password doesn't match", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(Drink_Home.this,"Wrong Old password",Toast.LENGTH_SHORT).show();
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

    private void showUpdateDialog(final String key, final DrinkCategory item) {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Drink_Home.this);
        alertDialog.setTitle("Update new Category");
        alertDialog.setMessage("please fill full information");

        //this inflater is used to inflate the view
        //        View.inflate(getApplicationContext(), R.layout.add_new_menu_layout, null);

        LayoutInflater inflater=this.getLayoutInflater();
        View addMenuLayout=inflater.inflate(R.layout.add_new_menu_layout,null);

        //        LayoutInflater inflater = LayoutInflater.from(Home.this); // or (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //        View viewMyLayout = inflater.inflate(R.layout.add_new_menu_layout, null);
        //        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
        //                (Context.LAYOUT_INFLATER_SERVICE);
        //        View view = inflater.inflate(R.layout.add_new_menu_layout,null);

        edtName = addMenuLayout.findViewById(R.id.edtName);
        btnSelect = addMenuLayout.findViewById(R.id.btnSelect);
        btnUpload = addMenuLayout.findViewById(R.id.btnUpload);

        //set default name
        edtName.setText(item.getDrinkName());

        // Set the old name of category
//          btnUpload.setText(item.getName());

        //Event for Button
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();//let user select image from gallery and save url of this image

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                changeMusic(item);
            }
        });

        alertDialog.setView(addMenuLayout);
        alertDialog.setIcon(R.drawable.ic_shopping_basket_black_24dp);
        //set Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i /*int i was int item ///////*/) {
//                dialogInterface.dismiss();//this dismisses the dialog this means as you click the button nothing to show
//                 item.setName(btnUpload.getText().toString());
                //  category.child(key).setValue(item);

                //update information

                item.setDrinkName(edtName.getText().toString());
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
    private void changeMusic(final DrinkCategory item) {
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
                            Toast.makeText(Drink_Home.this,"Uploaded",Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
                                @Override
                                public void onSuccess(Uri uri)
                                {
                                    //this is like creating new category
                                    item.setDrinkName(uri.toString());
                                    item.setDrinkImage(uri.toString());
                                }
                            });
                        }
                    })
                    //com.google.android.gms.tasks
                    .addOnFailureListener(new  OnFailureListener(){
                        @Override
                        public void onFailure(@NonNull Exception e){
                            mDialog.dismiss();
                            Toast.makeText(Drink_Home.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

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



