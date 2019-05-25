package com.example.saintmary.rightserver;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.saintmary.rightserver.Common.Common;
import com.example.saintmary.rightserver.Model.Category;
import com.example.saintmary.rightserver.Model.MyResponse;
import com.example.saintmary.rightserver.Model.Notification;
import com.example.saintmary.rightserver.Model.Request;
import com.example.saintmary.rightserver.Model.Sender;
import com.example.saintmary.rightserver.Model.Token;
import com.example.saintmary.rightserver.Remote.APIService;
import com.example.saintmary.rightserver.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.example.saintmary.rightserver.Model.MyResponse;
//import retrofit2.Response;

//import android.telecom.Call;
//import com.google.android.gms.common.api.Response;

public class OrderStatus extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;
    FirebaseDatabase db;
    DatabaseReference requests;

    MaterialSpinner spinner;

    APIService mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        //init Service
        mService=Common.getFCMClient();

        //Firebase
        db=FirebaseDatabase.getInstance();
        requests=db.getReference("Requests");

        //Init
        recyclerView=(RecyclerView)findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        loadOrders();//load all orders
    }

    private void loadOrders() {
        adapter=new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
              Request.class,
              R.layout.order_layout,
              OrderViewHolder.class,
              requests
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, final int position) {
                Category c=new Category();
               /* viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderAddress.setText(model.getAddress());
                viewHolder.txtOrderPhone.setText(model.getPhone());
                viewHolder.txtOrderedFoodName.setText(c.getName());

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });*/
                Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/TIMES.TTF");
                viewHolder.txtOrderId.setTypeface(typeface);
                viewHolder.txtOrderAddress.setTypeface(typeface);
                viewHolder.txtOrderStatus.setTypeface(typeface);
                viewHolder.txtOrderAddress.setTypeface(typeface);
                viewHolder.txtOrderPhone.setTypeface(typeface);
                viewHolder.txtOrderedFoodName.setTypeface(typeface);
                viewHolder.txtOrderDate.setTypeface(typeface);


                viewHolder.txtOrderId.setText("Order Id: "+adapter.getRef(position).getKey());
                viewHolder.txtOrderAddress.setText("Order Address:"+model.getAddress());
                viewHolder.txtOrderStatus.setText("Order Status: "+Common.convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderAddress.setText("Food Name And Address: "+model.getAddress());
                viewHolder.txtOrderPhone.setText("Order Phone: "+model.getPhone());
//                viewHolder.txtOrderedFoodName.setText("Food Name: "+model.getFoodName());

                viewHolder.txtOrderDate.setText("Order DATE"+Common.getDate(Long.parseLong(adapter.getRef(position).getKey())));






                /*viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderAddress.setText(model.getTotal());
                viewHolder.txtOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderAddress.setText(model.getAddress());
//                viewHolder.txtOrderPhone.setText(model.getPhone());
                viewHolder.txtOrderedFoodName.setText(model.getFoodName());*/

                viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(adapter.getItem(position).getStatus().equals("0"))
                            deleteOrder(adapter.getRef(position).getKey());
                        else
                            Toast.makeText(OrderStatus.this,"you can not delete the order",Toast.LENGTH_SHORT).show();

                    }
                });

            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals("Update"))
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        else if(item.getTitle().equals("Delete"))
            deleteOrder(adapter.getRef(item.getOrder()).getKey());
        return super.onContextItemSelected(item);
    }

    private void deleteOrder(String key) {
        requests.child(key).removeValue();
    }

    private void showUpdateDialog(String key, final Request item) {

        final AlertDialog.Builder alertDialog=new AlertDialog.Builder(OrderStatus.this);
        alertDialog.setTitle("Update Order");
        alertDialog.setMessage("please choose status");

        LayoutInflater inflater=this.getLayoutInflater();
        final View view=inflater.inflate(R.layout.update_order_layout,null);

        spinner=(MaterialSpinner)view.findViewById(R.id.statusSpinner);
        spinner.setItems("Placed","On my way","Shipped");

        alertDialog.setView(view);

        final String localKey=key;
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
             dialogInterface.dismiss();
             item.setStatus(String.valueOf(spinner.getSelectedIndex()));

             requests.child(localKey).setValue(item);
             
             sendOrderStatusToUser(localKey,item);

            }
        });

       alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
             dialogInterface.dismiss();
           }
       });
       alertDialog.show();
    }

    private void sendOrderStatusToUser(final String key,final Request item) {
        DatabaseReference tokens=db.getReference("Tokens");
        tokens.orderByKey().equalTo(item.getPhone())
                .addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                    for(DataSnapshot postSnapShot:dataSnapshot.getChildren())
                    {
                        Token token=postSnapShot.getValue(Token.class);

                        //make raw payload
                        Notification notification=new Notification("Right","Your order"+key+"was updated");
                        Sender content=new Sender(token.getToken(),notification);

                        mService.sendNotification(content)
                                .enqueue(new Callback<MyResponse>(){
                                    @Override
                                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response){
                                        if(response.body().success == 1)
                                        {
                                            Toast.makeText(OrderStatus.this,"Order was updated",Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(OrderStatus.this,"Order was updated but failed to send Notification",Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<MyResponse> call,Throwable t){
                                        Log.e("ERROR",t.getMessage());
                                    }
                                });
                        mService.sendNotification(content)
                                .enqueue(new Callback<MyResponse>(){
                                    @Override
                                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response){
                                        if(response.body().success == 1)
                                        {
                                            Toast.makeText(OrderStatus.this,"Thank you,Order place",Toast.LENGTH_SHORT).show();
                                            finish();
                                        }else{
                                            Toast.makeText(OrderStatus.this,"Failed",Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<MyResponse> call, Throwable t){
                                        Log.e("ERROR",t.getMessage());
                                    }
                                });
                    }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                } );
    }
}
