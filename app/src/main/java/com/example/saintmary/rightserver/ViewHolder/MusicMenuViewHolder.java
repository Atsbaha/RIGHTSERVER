package com.example.saintmary.rightserver.ViewHolder;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saintmary.rightserver.Interface.ItemClickListener;
import com.example.saintmary.rightserver.R;

public class MusicMenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView btn_delete;
    public TextView txtMusicMenuName;
    public TextView MusicianMenuName;
    private ItemClickListener itemClickListener;
    public MusicMenuViewHolder(@NonNull View itemView) {
        super(itemView);
        //the menu_name and menu_image are found in the menu_item layout resources with cardview
        txtMusicMenuName=(TextView)itemView.findViewById(R.id.music_menu_name);
        MusicianMenuName=itemView.findViewById(R.id.musician_menu_name);
        btn_delete=(ImageView)itemView.findViewById(R.id.music_btn_delete);
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
