
package com.example.saintmary.rightserver.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saintmary.rightserver.Interface.ItemClickListener;
import com.example.saintmary.rightserver.R;

public class FootballProgramMenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //    public TextView txtMenuName;
    public ImageView imageView;
    public   TextView scheduledGame,schechuledDate,schechuledTime;
    public EditText edtscheduledGame,edtschechuledDate,edtschechuledTime;


    private ItemClickListener itemClickListener;
    public FootballProgramMenuViewHolder(@NonNull View itemView) {
        super(itemView);
        //the menu_name and menu_image are found in the menu_item layout resources with cardview
//        txtMenuName=(TextView)itemView.findViewById(R.id.menu_name);
        imageView=(ImageView)itemView.findViewById(R.id.football_image);

        scheduledGame=itemView.findViewById(R.id.txtOnGoingGame);
        schechuledDate=itemView.findViewById(R.id.txtOnGoingDate);
        schechuledTime=itemView.findViewById(R.id.txtOnGoingTime);

       /* edtscheduledGame=itemView.findViewById(R.id.edtOnGoingGame);
        edtschechuledDate=itemView.findViewById(R.id.edtDate);
        edtschechuledTime=itemView.findViewById(R.id.edtTime);*/// i think this EditText Are used for admin so add them there

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
