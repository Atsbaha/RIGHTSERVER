package com.example.saintmary.rightserver.ViewHolder;


        import android.support.annotation.NonNull;
        import android.support.v7.widget.RecyclerView;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;
        import  com.example.saintmary.rightserver.Interface.ItemClickListener;

        import  com.example.saintmary.rightserver.R;

public class DrinkMenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtDrinkMenuName;
    public ImageView DrinkImageView;
    private ItemClickListener itemClickListener;
    public DrinkMenuViewHolder(@NonNull View itemView) {
        super(itemView);
        //the menu_name and menu_image are found in the menu_item layout resources with cardview
        txtDrinkMenuName=(TextView)itemView.findViewById(R.id.drink_menu_name);//this  (R.id.menu_name) is from menu_item create its own xml code if you want for the drink if any error occurs
        DrinkImageView=(ImageView)itemView.findViewById(R.id.drink_menu_image);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        // itemClickListener.onClick(View,)
        itemClickListener.onClick(view,getAdapterPosition(),false);//but this is according to the video that  i have seen
        // itemClickListener.onClick(view,false);its original was this

    }
}

