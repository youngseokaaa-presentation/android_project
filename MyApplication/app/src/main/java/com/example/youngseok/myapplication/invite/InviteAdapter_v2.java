package com.example.youngseok.myapplication.invite;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.youngseok.myapplication.GroupContent.member_list.Member_listActivity;
import com.example.youngseok.myapplication.R;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InviteAdapter_v2 extends RecyclerView.Adapter<InviteAdapter_v2.InviteHolder> {

    private ArrayList<InviteDTO> minvite;
    private Context mContext;
    private ArrayList <InviteDTO> save_invite_list;

    public class InviteHolder extends RecyclerView.ViewHolder{

        protected TextView Phonebook_name;
        protected TextView Phonebook_phone;
        protected Button invite_btn;
        protected CheckBox checkbox;

        public final View mView;
        public InviteHolder(View view){
            super(view);

            mView=view;

            this.Phonebook_name=view.findViewById(R.id.invite_name);
            this.Phonebook_phone=view.findViewById(R.id.invite_number);
            this.invite_btn=view.findViewById(R.id.invite_btn);
            this.checkbox=view.findViewById(R.id.checkBox);
        }
    }
    public InviteAdapter_v2(Context context,ArrayList<InviteDTO> minvite,ArrayList <InviteDTO>save_invite_list){
        this.mContext=context;
        this.minvite=minvite;
        this.save_invite_list=save_invite_list;
    }

    @Override
    public InviteHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.invite_list
                ,viewGroup,false);

        InviteHolder viewHolder = new InviteHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final InviteHolder viewHolder, final int position){





        InviteDTO data = minvite.get(position);
        viewHolder.checkbox.setVisibility(View.VISIBLE);
        viewHolder.checkbox.setText("");
        viewHolder.Phonebook_name.setText(data.getPhonebook_name());
        viewHolder.Phonebook_phone.setText(data.getPhonebook_phone());
        viewHolder.invite_btn.setVisibility(View.GONE);



        viewHolder.mView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(viewHolder.checkbox.isChecked()==true){
                    Log.e("20180410","111");
                    viewHolder.checkbox.setChecked(false);
                    save_invite_list.remove(minvite.get(position));
                    //여기서 리스트 빼고 -갓
                }
                else if(viewHolder.checkbox.isChecked()==false){
                    viewHolder.checkbox.setChecked(true);
                    save_invite_list.add(minvite.get(position));
                    Log.e("20180410","222");
                   // 여기서 리스트 넣고
                }
                else{}

            }
        });
        if(minvite.get(position).getCount()==999){
            viewHolder.invite_btn.setVisibility(View.VISIBLE);
            viewHolder.invite_btn.setText("초대하기");
            viewHolder.checkbox.setText("");
            viewHolder.Phonebook_name.setText("");
            viewHolder.Phonebook_phone.setText("");
            viewHolder.checkbox.setVisibility(View.GONE);
        }


        viewHolder.invite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int index=0; index<save_invite_list.size();index++) {
                    Log.e("20180410", save_invite_list.get(index).getPhonebook_name());
                }

                if(save_invite_list.size()==0){
                    Intent go_list = new Intent();
                    go_list.putExtra("save_invite_list",save_invite_list);
                    String msg = "아무도 선택하지 않으셧습니다!";
                    go_list.putExtra("msg",msg);
                    ((Activity)mContext).setResult(Activity.RESULT_OK,go_list);
                    ((Activity)mContext).finish();
                }
                else{
                    Intent go_list = new Intent();
                    go_list.putExtra("save_invite_list",save_invite_list);
                    String msg = (save_invite_list.size())+"명에게 초대메세지를 보냈습니다!";
                    go_list.putExtra("msg",msg);
                    ((Activity)mContext).setResult(Activity.RESULT_OK,go_list);
                    ((Activity)mContext).finish();
                }








            }
        });
















    }

    @Override
    public int getItemCount(){
        return (null !=minvite ? minvite.size() :0);
    }
}

