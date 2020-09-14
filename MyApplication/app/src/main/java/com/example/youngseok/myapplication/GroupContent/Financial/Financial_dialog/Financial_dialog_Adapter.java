package com.example.youngseok.myapplication.GroupContent.Financial.Financial_dialog;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.youngseok.myapplication.GroupContent.Financial.FinancialAdapter;
import com.example.youngseok.myapplication.GroupContent.Financial.financialDTO;
import com.example.youngseok.myapplication.GroupContent.Financial.financial_delete_Request;
import com.example.youngseok.myapplication.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class Financial_dialog_Adapter extends RecyclerView.Adapter<Financial_dialog_Adapter.Financial_dialog_Holder> {


    private ArrayList<Financial_dialog_DTO> mRecycler;

    private Context mContext;
    private static final int PROFILE_PICTURE = 3503;





    public class Financial_dialog_Holder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{


        protected ImageView financial_dialog_recycler_imageview;

        public final View mView;
        public Financial_dialog_Holder(View view){
            super(view);

            mView=view;

            this.financial_dialog_recycler_imageview=view.findViewById(R.id.financial_dialog_recycler_imageview);
            view.setOnCreateContextMenuListener(this);
        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {  // 3. 메뉴 추가U


            MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");

            Delete.setOnMenuItemClickListener(onEditMenu);

        }


        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {


            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case 1002:

                        Response.Listener<String> responseListener = new Response.Listener<String>(){

                            @Override
                            public void onResponse(String response){
                                try{


                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success =jsonResponse.getBoolean("success");
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
                        //volley 라이브러리 이용해서 실제 서버와 통신

                        String abc = mRecycler.get(getAdapterPosition()).getFinancial_dialog_picture();
                        String[] aa = abc.split("/");
                        String acb=aa[aa.length-1];
                        financial_dialog_image_delete_request financial_dialog_image_delete_request = new financial_dialog_image_delete_request(
                                mRecycler.get(getAdapterPosition()).getMaster_key()
                                ,mRecycler.get(getAdapterPosition()).getMoney_type(),mRecycler.get(getAdapterPosition()).getMoney(),
                                mRecycler.get(getAdapterPosition()).getMoney_explain(),mRecycler.get(getAdapterPosition()).getAccount_time(),
                                acb,responseListener);
                        RequestQueue queue = Volley.newRequestQueue(mContext);
                        queue.add(financial_dialog_image_delete_request);




                        mRecycler.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), mRecycler.size());
                        notifyDataSetChanged();


                        break;

                }
                return true;
            }

        };

    }
    public Financial_dialog_Adapter(Context context,ArrayList<Financial_dialog_DTO> mRecycler){
        this.mContext=context;
        this.mRecycler=mRecycler;
    }
    Financial_dialog_Adapter(Context context){
        mContext=context;
    }

    @Override
    public Financial_dialog_Holder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.financial_dialog_recycler
                ,viewGroup,false);

        Financial_dialog_Holder viewHolder = new Financial_dialog_Holder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Financial_dialog_Holder viewHolder, final int position) {






        if(mRecycler.get(position).getFinancial_dialog_picture().equals("basic_click")){
            Glide.with(mContext).asBitmap().load(R.drawable.add_picture_white).override(300,300).into(viewHolder.financial_dialog_recycler_imageview);
        }
        else{
            Glide.with(mContext).asBitmap().load(mRecycler.get(position).getFinancial_dialog_picture()).override(300,300).into(viewHolder.financial_dialog_recycler_imageview);
        }

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mRecycler.get(position).getFinancial_dialog_picture().equals("basic_click")){
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI); // 외부 저장소에 있는 이미지 파일들에 대한 모든 정보를 얻을 수 있다.
                    intent.setType("image/*"); // image/* 형식의 Type 호출 -> 파일을 열 수 있는 앱들이 나열된다.

                    ((Activity)mContext).startActivityForResult(intent,PROFILE_PICTURE);

                }
                else{
                    Intent intent = new Intent(mContext,Financial_dialog_image.class);
                    intent.putExtra("image_resource",mRecycler.get(position).getFinancial_dialog_picture());
                    intent.putExtra("image_resource_arr",mRecycler);
                    intent.putExtra("image_resource_position",position);

                    mContext.startActivity(intent);


                }

            }
        });




    }



    @Override
    public int getItemCount(){
        return (null !=mRecycler ? mRecycler.size() :0);
    }










}


