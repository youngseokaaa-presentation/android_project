package com.example.youngseok.myapplication.BasicFrame.Recycler;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.youngseok.myapplication.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {

    private ArrayList<RecyclerDTO> mRecycler;
    private Context mContext;

    public class RecyclerHolder extends RecyclerView.ViewHolder{

//        protected TextView Phonebook_name;
//        protected TextView Phonebook_phone;
//        protected Button invite_btn;

        public final View mView;
        public RecyclerHolder(View view){
            super(view);

            mView=view;

//            this.Phonebook_name=view.findViewById(R.id.invite_name);
//            this.Phonebook_phone=view.findViewById(R.id.invite_number);
//            this.invite_btn=view.findViewById(R.id.invite_btn);
        }
    }
    public RecyclerAdapter(Context context,ArrayList<RecyclerDTO> mRecycler){
        this.mContext=context;
        this.mRecycler=mRecycler;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.invite_list
                ,viewGroup,false);

        RecyclerHolder viewHolder = new RecyclerHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerHolder viewHolder, final int position){





//        InviteDTO data = minvite.get(position);
//        viewHolder.Phonebook_name.setText(data.getPhonebook_name());
//        viewHolder.Phonebook_phone.setText(data.getPhonebook_phone());
//
//
//
//        viewHolder.invite_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//
//                Log.e("adjlskfjwe",minvite.get(position).getPhonebook_name());
//                if(minvite.get(position).getCount()==1){
//                    Log.e("adjilsk","111");
//
//
//                }
//                else{
//                    View view = LayoutInflater.from(mContext).inflate(R.layout.invite_dialog,null,false);
//                    builder.setView(view);
//                    final AlertDialog alertdialog = builder.create();
//
//                    Button sms_btn = view.findViewById(R.id.sms_btn);
//                    Button kakao_btn = view.findViewById(R.id.kakao_btn);
//                    sms_btn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Log.e("20180408","smsbtnclick");
//
//                            String sms_text ="모임모임 어플로 초대합니다~ 우리 같이 모임 만들어요! \n"+"https://play.google.com/store/search?q=%EB%AA%A8%EC%9E%84%EB%AA%A8%EC%9E%84&c=apps";
//                            Intent intent = new Intent(Intent.ACTION_VIEW);
//                            intent.putExtra("sms_body",sms_text);
//                            intent.putExtra("address",minvite.get(position).getPhonebook_phone());
//                            intent.setType("vnd.android-dir/mms-sms");
//                            v.getContext().startActivity(intent);
//
//                            alertdialog.dismiss();
//                        }
//                    });
//
//
//
//
//                    alertdialog.show();
//                }
//
//
//            }
//        });








    }



    @Override
    public int getItemCount(){
        return (null !=mRecycler ? mRecycler.size() :0);
    }
}

