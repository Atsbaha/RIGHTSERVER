package com.example.saintmary.rightserver.ViewHolder;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saintmary.rightserver.Common.Common;
import  com.example.saintmary.rightserver.Interface.ItemClickListener;
import  com.example.saintmary.rightserver.R;


//import java.util.ArrayList;

public class MenuViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener,View.OnCreateContextMenuListener       //View.OnCreateContextMenuListener  is used to update the category
/*,View.OnCreateContextMenuListener*/ {
    public TextView txtMenuName;
    public ImageView imageView;
    private ItemClickListener itemClickListener;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        //the menu_name and menu_image are found in the menu_item layout resources with cardview
        txtMenuName =  itemView.findViewById(R.id.menu_name);
        imageView =  itemView.findViewById(R.id.menu_image);

//         itemView.setOnCreateContextMenuListener(this);//this  Context Menu is used to show when we hold at item malet eti update and delete zbl nmraaay malet eyu

        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);


    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        // itemClickListener.onClick(View,)
        itemClickListener.onClick(view, getAdapterPosition(), false);//but this is according to the video that  i have seen
//         itemClickListener.onClick(view,false);//its original was this

    }


   @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

    contextMenu.setHeaderTitle("Select the action");

    contextMenu.add(0,0,getAdapterPosition(),Common.UPDATE);
    contextMenu.add(0,1,getAdapterPosition(),Common.DELETE);


}

}

