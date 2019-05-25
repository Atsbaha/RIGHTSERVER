package com.example.saintmary.rightserver.ViewHolder;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.saintmary.rightserver.Interface.ItemClickListener;
import com.example.saintmary.rightserver.R;

public class MusicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    //the below declared variables are found in music_item xml code
    public TextView musicName;
    public TextView musicianName;


    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public MusicViewHolder(View itemView) {
        super(itemView);

        musicName =(TextView)itemView.findViewById(R.id.music_name);
        musicianName = itemView.findViewById(R.id.MusicianName);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view, getAdapterPosition(), false);// itemClickListener.onClick(view, false);//this was like this




    }

}
