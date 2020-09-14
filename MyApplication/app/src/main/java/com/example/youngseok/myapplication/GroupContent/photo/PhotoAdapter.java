package com.example.youngseok.myapplication.GroupContent.photo;



import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.youngseok.myapplication.GroupContent.Financial.Financial_dialog.Financial_dialog_DTO;
import com.example.youngseok.myapplication.GroupContent.Financial.Financial_dialog.Financial_dialog_image;
import com.example.youngseok.myapplication.R;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    private ArrayList<Financial_dialog_DTO> mRecycler;
    private ArrayList<Financial_dialog_DTO> mRecycler1;
    private ArrayList<Financial_dialog_DTO> mRecycler2;
    private ArrayList<Financial_dialog_DTO> mRecycler3;
    private Context mContext;

    private int count;

    public class PhotoHolder extends RecyclerView.ViewHolder{

        protected ImageView imageview1;
        protected ImageView imageview2;
        protected ImageView imageview3;

        public final View mView;
        public PhotoHolder(View view){
            super(view);

            mView=view;


            this.imageview1=view.findViewById(R.id.photo_1);
            this.imageview2=view.findViewById(R.id.photo_2);
            this.imageview3=view.findViewById(R.id.photo_3);
        }
    }
    public PhotoAdapter(Context context,ArrayList<Financial_dialog_DTO> mRecycler,ArrayList<Financial_dialog_DTO> mRecycler1,ArrayList<Financial_dialog_DTO> mRecycler2,ArrayList<Financial_dialog_DTO> mRecycler3){
        this.mContext=context;
        this.mRecycler=mRecycler;
        this.mRecycler1=mRecycler1;
        this.mRecycler2=mRecycler2;
        this.mRecycler3=mRecycler3;
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_list
                ,viewGroup,false);

        PhotoHolder viewHolder = new PhotoHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PhotoHolder viewHolder, final int position){

        count = (mRecycler1.size()+mRecycler2.size()+mRecycler3.size())-1;

        int index = position;
        if(index<mRecycler1.size()){
            Glide.with(mContext).asBitmap().load(mRecycler1.get(index).getFinancial_dialog_picture()).override(150,150).into(viewHolder.imageview1);
        }
        if(index<mRecycler2.size()){
            Glide.with(mContext).asBitmap().load(mRecycler2.get(index).getFinancial_dialog_picture()).override(150,150).into(viewHolder.imageview2);
        }
        if(index<mRecycler3.size()){
            Glide.with(mContext).asBitmap().load(mRecycler3.get(index).getFinancial_dialog_picture()).override(150,150).into(viewHolder.imageview3);
        }

        viewHolder.imageview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("coldenough",String.valueOf((position*3)));
                Intent intent = new Intent(mContext,Financial_dialog_image.class);
                intent.putExtra("image_resource",mRecycler1.get(position).getFinancial_dialog_picture());
                intent.putExtra("image_resource_arr",mRecycler);
                intent.putExtra("image_resource_position",position*3);

                mContext.startActivity(intent);
            }
        });

        viewHolder.imageview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("coldenough",String.valueOf((position*3)+1));
                Intent intent = new Intent(mContext,Financial_dialog_image.class);
                intent.putExtra("image_resource",mRecycler2.get(position).getFinancial_dialog_picture());
                intent.putExtra("image_resource_arr",mRecycler);
                intent.putExtra("image_resource_position",(position*3)+1);

                mContext.startActivity(intent);
            }
        });

        viewHolder.imageview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("coldenough",String.valueOf(((position*3)+2)));
                Intent intent = new Intent(mContext,Financial_dialog_image.class);
                intent.putExtra("image_resource",mRecycler3.get(position).getFinancial_dialog_picture());
                intent.putExtra("image_resource_arr",mRecycler);
                intent.putExtra("image_resource_position",(position*3)+2);

                mContext.startActivity(intent);
            }
        });






    }



    @Override
    public int getItemCount(){
        return (null !=mRecycler ? mRecycler.size() :0);
    }
}

