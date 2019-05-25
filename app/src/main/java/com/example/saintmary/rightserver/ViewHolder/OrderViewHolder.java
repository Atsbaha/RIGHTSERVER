package com.example.saintmary.rightserver.ViewHolder;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saintmary.rightserver.Interface.ItemClickListener;
import com.example.saintmary.rightserver.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener{

    public TextView txtOrderId, txtOrderStatus, txtOrderPhone, txtOrderAddress,txtOrderedFoodName,txtOrderDate;
    public ImageView btn_delete;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView){
        super(itemView);

        txtOrderAddress = itemView.findViewById(R.id.order_address);//this is found in the order_layout.xml
        txtOrderId = itemView.findViewById(R.id.order_id);
        txtOrderStatus = itemView.findViewById(R.id.order_status);
        txtOrderPhone = itemView.findViewById(R.id.order_phone);
        txtOrderedFoodName=itemView.findViewById(R.id.ordered_food_name);
        txtOrderDate=itemView.findViewById(R.id.order_date);



        btn_delete=(ImageView)itemView.findViewById(R.id.btn_delete);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    @Override
    public void onClick(View view)
    {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo menuInfo) {

        contextMenu.setHeaderTitle("Select The Action");

       contextMenu.add(0,0,getAdapterPosition(),"Update");
       contextMenu.add(0,1,getAdapterPosition(),"Delete");
    }
}


