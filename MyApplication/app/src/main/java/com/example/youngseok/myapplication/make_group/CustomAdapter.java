package com.example.youngseok.myapplication.make_group;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;


import com.example.youngseok.myapplication.GroupContent.GroupContentActivity;
import com.example.youngseok.myapplication.R;
import com.example.youngseok.myapplication.recycler_drag_drop.ItemTouchHelperListener;
import com.example.youngseok.myapplication.recycler_drag_drop.sortRequest;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.youngseok.myapplication.Initial.InitialActivity.save_my_id;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder>


implements ItemTouchHelperListener{

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if(fromPosition < 0 || fromPosition >= mbasic.size() || toPosition < 0 || toPosition >= mbasic.size()){
            Log.e("Tlqkf","TlqkfTlqkfTlqkf1111");
            return false;
        }

        Log.e("Tlqkf","TlqkfTlqkfTlqkf222");
        basicGroup fromItem = mbasic.get(fromPosition);

        Log.e("Tlqkf","TlqkfTlqkfTlqkf3333");
        mbasic.remove(fromPosition);
        Log.e("Tlqkf","TlqkfTlqkfTlqkf44");
        mbasic.add(toPosition, fromItem);
        Log.e("Tlqkf","TlqkfTlqkfTlqkf5555");

        notifyItemMoved(fromPosition, toPosition);

        //여기다가 데이터베이스 순서 바꾸는걸 넣자.



        for(int index=0; index<mbasic.size();index++){

            Response.Listener<String> responseListener = new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);


                        boolean success = jsonResponse.getBoolean("success");

                        if(success){
                        }
                        else{
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            };
            String idx = String.valueOf(index+1);

            sortRequest sortrequest =new sortRequest(save_my_id,mbasic.get(index).getGroup_name(),idx,responseListener);
            Log.e("eleewlkjsdlkff",idx);
            Log.e("eleewlkjsdlkff",mbasic.get(index).getGroup_name());
            RequestQueue queue = Volley.newRequestQueue(mContext);

            queue.add(sortrequest);
        }
        return true;
    }

    @Override
    public void onItemRemove(int position) {
        mbasic.remove(position);
        notifyItemRemoved(position);
    }


    private ArrayList<basicGroup> mbasic;

    private Context mContext;

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        protected TextView group_name;
        protected TextView group_content;
        protected TextView group_sumnail;
        protected ImageView group_picture;

        public final View mView;
        public CustomViewHolder(View view){
            super(view);

            mView=view;

            this.group_name=view.findViewById(R.id.textview_group_name);
            this.group_content=view.findViewById(R.id.textview_group_content);
            this.group_sumnail=view.findViewById(R.id.textview_group_sumnail);
            this.group_picture=view.findViewById(R.id.imageview_group_profile);
        }
    }
    public CustomAdapter(Context context,ArrayList<basicGroup> basic){
        this.mContext=context;
        this.mbasic=basic;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.basic_list,viewGroup,false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewHolder, final int position){



        viewHolder.group_name.setText(mbasic.get(position).getGroup_name());
        viewHolder.group_content.setText(mbasic.get(position).getGroup_content());
        viewHolder.group_sumnail.setText(mbasic.get(position).getGroup_sumnail());
        Glide.with(mContext).asBitmap().load(mbasic.get(position).getGroup_picture()).override(300,300).into(viewHolder.group_picture);

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent intent = new Intent(v.getContext(),GroupContentActivity.class);
             intent.putExtra("group_name",mbasic.get(position).getGroup_name());
             intent.putExtra("group_master_key",mbasic.get(position).getMaster_key());
             v.getContext().startActivity(intent);
               Log.e("adjlskfjwe",mbasic.get(position).getMaster_key());
            }
        });

    }

    @Override
    public int getItemCount(){
        return (null !=mbasic ? mbasic.size() :0);
    }
}
