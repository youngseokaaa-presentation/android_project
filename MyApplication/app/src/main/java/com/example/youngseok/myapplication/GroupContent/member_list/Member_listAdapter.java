package com.example.youngseok.myapplication.GroupContent.member_list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.youngseok.myapplication.R;

import java.util.ArrayList;

public class Member_listAdapter extends RecyclerView.Adapter<Member_listAdapter.Member_listHolder> {

    private ArrayList<Member_listDTO> mMember_list;
    private Context mContext;

    public class Member_listHolder extends RecyclerView.ViewHolder{


        protected TextView name;
        protected TextView nickname;
        protected TextView id;
        protected TextView phone;
        protected TextView status;

        public final View mView;
        public Member_listHolder(View view){
            super(view);

            mView=view;

            this.name=view.findViewById(R.id.member_name);
            this.nickname=view.findViewById(R.id.member_nick);
            this.id=view.findViewById(R.id.member_id);
            this.phone=view.findViewById(R.id.member_phone);
            this.status=view.findViewById(R.id.member_status);
        }
    }
    public Member_listAdapter(Context context,ArrayList<Member_listDTO> mGroup){
        this.mContext=context;
        this.mMember_list=mGroup;
    }

    @Override
    public Member_listHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.member_list
                ,viewGroup,false);

        Member_listHolder viewHolder = new Member_listHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Member_listHolder viewHolder, final int position){



        viewHolder.name.setText(mMember_list.get(position).getName());
        viewHolder.nickname.setText(mMember_list.get(position).getNickname());
        viewHolder.id.setText(mMember_list.get(position).getId());
        viewHolder.phone.setText(mMember_list.get(position).getPhone());
        viewHolder.status.setText(mMember_list.get(position).getStatus());














    }



    @Override
    public int getItemCount(){
        return (null !=mMember_list ? mMember_list.size() :0);
    }
}


