package com.example.youngseok.myapplication.GroupContent.Location;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.youngseok.myapplication.R;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListHolder> {

    private ArrayList<ListDTO> mRecycler;
    private Context mContext;

    private ItemClick itemClick;
    public interface ItemClick{
        public void onClick(View view ,int position);
    }
    public void setItemClick(ItemClick itemClick){
        this.itemClick = itemClick;
    }

    public class ListHolder extends RecyclerView.ViewHolder{


        protected TextView title_item;
        protected TextView sub_title_item;
        protected ImageView list_image;

        public final View mView;
        public ListHolder(View view){
            super(view);

            mView=view;


            this.title_item=view.findViewById(R.id.title_item);
            this.sub_title_item=view.findViewById(R.id.sub_title_item);
            this.list_image=view.findViewById(R.id.list_image);
        }
    }
    public ListAdapter(Context context,ArrayList<ListDTO> mRecycler){
        this.mContext=context;
        this.mRecycler=mRecycler;
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fab_list_item
                ,viewGroup,false);

        ListHolder viewHolder = new ListHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListHolder viewHolder, int position){

        final int Position = position;




        ListDTO data = mRecycler.get(position);

        viewHolder.title_item.setText(data.getTitle());
        viewHolder.sub_title_item.setText(data.getSub_title());
        if(data.getType_code().equals("sharing")){
            viewHolder.list_image.setImageResource(android.R.drawable.ic_menu_myplaces);

        }
        else if(data.getType_code().equals("marker")){
            viewHolder.list_image.setImageResource(R.drawable.map_marker);
        }
        else{}

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClick != null){
                    itemClick.onClick(v,Position);
                }
            }
        });


    }



    @Override
    public int getItemCount(){
        return (null !=mRecycler ? mRecycler.size() :0);
    }
}

