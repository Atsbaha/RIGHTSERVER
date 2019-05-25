package com.example.saintmary.rightserver;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.saintmary.rightserver.Common.Common;
import com.example.saintmary.rightserver.Interface.ItemClickListener;
import com.example.saintmary.rightserver.Model.Category;
import com.example.saintmary.rightserver.Model.Food.*;
import com.example.saintmary.rightserver.Model.Food;
import com.example.saintmary.rightserver.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import java.lang.String;

import java.util.UUID;

import info.hoang8f.widget.FButton;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    RelativeLayout rootlayout;

    FloatingActionButton fab;
    FirebaseDatabase db;
    DatabaseReference foodList;
    FirebaseStorage storage;
    StorageReference storageReference;
//    DrawerLayout drawer;

    String categoryId=" ";
    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;
//    MaterialEditText edtName,edtDescription,edtPrice,edtDiscount;
//FButton btnSelect,btnUpload;
    EditText edtName,edtDescription,edtPrice,edtDiscount;
    Button btnSelect,btnUpload;
    ImageView imageView;
    Food newFood;
    Uri saveUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //firebase
        db=FirebaseDatabase.getInstance();
        foodList=db.getReference("Foods");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        //init
        recyclerView=(RecyclerView)findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rootlayout=(RelativeLayout)findViewById(R.id.rootLayout);

        fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFoodDialog();

            }
        });
       if(getIntent()!=null)
           categoryId=getIntent().getStringExtra("CategoryId");
          loadListFood(categoryId);
    }

    private void showAddFoodDialog() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(FoodList.this);
        alertDialog.setTitle("Add new Food");
        alertDialog.setMessage("please fill full information");

        //this inflater is used to inflate the view
//        View.inflate(getApplicationContext(), R.layout.add_new_menu_layout, null);

        LayoutInflater inflater=this.getLayoutInflater();
        View addMenuLayout=inflater.inflate(R.layout.add_new_food_layout,null);

//        LayoutInflater inflater = LayoutInflater.from(Home.this); // or (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View viewMyLayout = inflater.inflate(R.layout.add_new_menu_layout, null);
//        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
//                (Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.add_new_menu_layout,null);

        edtName = addMenuLayout.findViewById(R.id.edtName);
        edtDescription = addMenuLayout.findViewById(R.id.edtDescription);
        edtPrice = addMenuLayout.findViewById(R.id.edtPrice);
        edtDiscount = addMenuLayout.findViewById(R.id.edtDiscount);

        imageView=findViewById(R.id.imageView);

        btnSelect = addMenuLayout.findViewById(R.id.btnSelect);
        btnUpload = addMenuLayout.findViewById(R.id.btnUpload);

        // Set the old name of category
        // btnUpload.setText(item.getName());

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
                uploadImage();
            }
        });

        alertDialog.setView(addMenuLayout);
        alertDialog.setIcon(R.drawable.ic_shopping_basket_black_24dp);



        //set Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item /*item was which*/) {
                dialogInterface.dismiss();//this dismisses the dialog this means as you click the button nothing to show
                // item.setName(btnUpload.getText().toString());
                //  category.child(key).setValue(item);

                //Here just create new category
                if(newFood!=null)
                {
                    foodList.push().setValue(newFood);
//                    Toast.makeText(Home.this,"New category",Toast.LENGTH_SHORT).show();
                    //view,id and duration change to Toast message if it is error
                    //the "drawer" below is from activity_home in which its id is drawer_layout
                    Snackbar.make(rootlayout,"New Food"+newFood.getName()+"was added",Snackbar.LENGTH_SHORT).show();


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

    private void uploadImage() {
        if(saveUri!=null)
        {
            final ProgressDialog mDialog=new ProgressDialog(this);
            mDialog.setMessage("Uploading");
            mDialog.show();
//            String imageName=UUID.randomUUID().toString();//.child("images/*"+imageName);
            final StorageReference imageFolder=storageReference.child("images/"+ UUID.randomUUID().toString());
//            final StorageReference imageFolder =storageReference.child("foodImages/"+ UUID.randomUUID().toString());//this line was  final StorageReference imageFolder =storageReference.child("images/*"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            mDialog.dismiss();//this may cause to not run please comment and try
                            Toast.makeText(FoodList.this,"Uploaded",Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
                                @Override
                                public void onSuccess(Uri uri)
                                {
                                    // set value for new category if image upload and we can get downlaod link
//                                    newCategory=new Category(edtName.getText().toString(),uri.toString());
                                    newFood =new Food();
                                    newFood.setName(edtName.getText().toString());
                                    newFood.setDescription(edtDescription.getText().toString());
                                    newFood.setPrice(edtPrice.getText().toString());
                                    newFood.setDiscount(edtDiscount.getText().toString());
                                    newFood.setMenuId(categoryId);
                                    newFood.setImage(uri.toString());

//                                    newFood=new Food(edtName.getText().toString(),edtDescription.getText().toString(),edtPrice.getText().toString(),edtDiscount.getText().toString(),categoryId,uri.toString());



                                }
                            });
                        }
                    })
                    //com.google.android.gms.tasks
                    .addOnFailureListener(new  OnFailureListener(){
                        @Override
                        public void onFailure(@NonNull Exception e){
                            mDialog.dismiss();
                            Toast.makeText(FoodList.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

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
            Toast.makeText(FoodList.this, "please select Image", Toast.LENGTH_SHORT).show();
        }

    }
    private void chooseImage() {
        Intent intent=new Intent();
        intent.setType("image/*");//i think this is images in the storageDatabase
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),Common.PICK_IMAGE_REQUEST);


    }

    private void loadListFood(String categoryId) {
        adapter=new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("menuId").equalTo(categoryId)//this "menuId" should be thesame to name in notepad++ please check it

        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {
                viewHolder.food_name.setText(model.getName());
                Picasso.with(getBaseContext())
                        .load(model.getImage())
                        .into(viewHolder.food_image);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
             }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Common.PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData() != null)
        {
            saveUri=data.getData();
            btnSelect.setText("Image is Selected");
           /* try {
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),saveUri);
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE))
        {
         showUpdateFoodDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else  if(item.getTitle().equals(Common.DELETE))
        {
         deleteFood(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteFood(String key) {
        foodList.child(key).removeValue();
    }

    private void showUpdateFoodDialog(final String key, final Food item) {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(FoodList.this);
        alertDialog.setTitle("Edit Food");
        alertDialog.setMessage("please fill full information");

        //this inflater is used to inflate the view
//        View.inflate(getApplicationContext(), R.layout.add_new_menu_layout, null);

        LayoutInflater inflater=this.getLayoutInflater();
        View addMenuLayout=inflater.inflate(R.layout.add_new_food_layout,null);

//        LayoutInflater inflater = LayoutInflater.from(Home.this); // or (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View viewMyLayout = inflater.inflate(R.layout.add_new_menu_layout, null);
//        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
//                (Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.add_new_menu_layout,null);

        edtName = addMenuLayout.findViewById(R.id.edtName);
        edtDescription = addMenuLayout.findViewById(R.id.edtDescription);
        edtPrice = addMenuLayout.findViewById(R.id.edtPrice);
        edtDiscount = addMenuLayout.findViewById(R.id.edtDiscount);


        //set default value for view ezi malet dhri edit mgbarna etom zhabnayom values kemzneberwo kkonu
        edtName.setText(item.getName());
        edtDiscount.setText(item.getDiscount());
        edtPrice.setText(item.getPrice());
        edtDescription.setText(item.getDescription());

        imageView=findViewById(R.id.imageView);

        btnSelect = addMenuLayout.findViewById(R.id.btnSelect);
        btnUpload = addMenuLayout.findViewById(R.id.btnUpload);

        // Set the old name of category
        // btnUpload.setText(item.getName());

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
                changeImage(item);
            }
        });

        alertDialog.setView(addMenuLayout);
        alertDialog.setIcon(R.drawable.ic_shopping_basket_black_24dp);


        //set Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item /*item was which*/) {
                dialogInterface.dismiss();//this dismisses the dialog this means as you click the button nothing to show
                // item.setName(btnUpload.getText().toString());
                //  category.child(key).setValue(item);

                //Here just create new food
                 //update information
                    //set the default value for the view
                   /* item.setName(edtName.getText().toString());
                    item.setPrice(edtPrice.getText().toString());
                    item.setDiscount(edtDiscount.getText().toString());
                    item.setDescription(edtDescription.getText().toString());*



                  /*  newFood.setName(edtName.getText().toString());
                    newFood.setPrice(edtPrice.getText().toString());
                    newFood.setDiscount(edtDiscount.getText().toString());
                    newFood.setDescription(edtDescription.getText().toString());
*/

//                    foodList.push().setValue(newFood);
                    foodList.child(key).setValue(item);
                    //the below error can be corrected by
//                Snackbar.make(rootlayout,"Food"+newFood.getName()+"was edited",Snackbar.LENGTH_SHORT).show();
//                  Snackbar.make(rootlayout,"Food"+item.getName()+"was edited",Snackbar.LENGTH_SHORT).show();





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
    private void changeImage(final Food item) {
        if(saveUri!=null)
        {
            final ProgressDialog mDialog=new ProgressDialog(this);
            mDialog.setMessage("Uploading");
            mDialog.show();

            //the ff UUID is in import java.util
            //UUID stands for universally unique identifier (UUID)
            String imageName=UUID.randomUUID().toString();//.child("images/*"+imageName);
            //final StorageReference imageFolder=new storageReference.child("images"+imageName);
            final StorageReference imageFolder = storageReference.child("images/"+imageName);//here we cancelled the keyword "new"
            // final StorageReference imageFolder=new storageReference.child("images/*"+imageName);// i tried this simply storageReference.child("images);it works
            // storageReference.child imageFolder = new storageReference.child("images");//this works check it
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            mDialog.dismiss();
                            Toast.makeText(FoodList.this,"Uploaded",Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
                                @Override
                                public void onSuccess(Uri uri)
                                {
                                    //this is like creating new category
//                                     item.setName(uri.toString());
                                    item.setImage(uri.toString());
                                }
                            });
                        }
                    })
                    //com.google.android.gms.tasks
                    .addOnFailureListener(new  OnFailureListener(){
                        @Override
                        public void onFailure(@NonNull Exception e){
                            mDialog.dismiss();
                            Toast.makeText(FoodList.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

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
