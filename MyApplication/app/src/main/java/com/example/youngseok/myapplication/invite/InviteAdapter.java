package com.example.youngseok.myapplication.invite;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.youngseok.myapplication.R;

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.InviteHolder> {

    private ArrayList<InviteDTO> minvite;
    private Context mContext;

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
    public InviteAdapter(Context context,ArrayList<InviteDTO> minvite){
        this.mContext=context;
        this.minvite=minvite;
    }

    @Override
    public InviteHolder onCreateViewHolder(ViewGroup viewGroup,int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.invite_list
        ,viewGroup,false);

        InviteHolder viewHolder = new InviteHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final InviteHolder viewHolder, final int position){





        InviteDTO data = minvite.get(position);
        viewHolder.Phonebook_name.setText(data.getPhonebook_name());
        viewHolder.Phonebook_phone.setText(data.getPhonebook_phone());

        if (data.getCount()==1){
            viewHolder.invite_btn.setText("모임에 초대하기");

        }
        else{
            viewHolder.invite_btn.setText("초대하기");
        }


        viewHolder.invite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                Log.e("adjlskfjwe",minvite.get(position).getPhonebook_name());
                if(minvite.get(position).getCount()==1){
                    Log.e("adjilsk","111");


                }
                else{
                    View view = LayoutInflater.from(mContext).inflate(R.layout.invite_dialog,null,false);
                    builder.setView(view);
                    final AlertDialog alertdialog = builder.create();

                    Button sms_btn = view.findViewById(R.id.sms_btn);
                    Button kakao_btn = view.findViewById(R.id.kakao_btn);
                    sms_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("20180408","smsbtnclick");

                            String sms_text ="모임모임 어플로 초대합니다~ 우리 같이 모임 만들어요! \n"+"https://play.google.com/store/search?q=%EB%AA%A8%EC%9E%84%EB%AA%A8%EC%9E%84&c=apps";
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.putExtra("sms_body",sms_text);
                            intent.putExtra("address",minvite.get(position).getPhonebook_phone());
                            intent.setType("vnd.android-dir/mms-sms");
                            v.getContext().startActivity(intent);

                            alertdialog.dismiss();
                        }
                    });

                    kakao_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("20180408","kakaobtnclick");

                            String templateId = "15718";

                            Map<String, String> templateArgs = new HashMap<String, String>();
                            templateArgs.put("template_arg1", "value1");
                            templateArgs.put("template_arg2", "value2");

                            Map<String, String> serverCallbackArgs = new HashMap<String, String>();
                            serverCallbackArgs.put("user_id", "${current_user_id}");
                            serverCallbackArgs.put("product_id", "${shared_product_id}");

                            KakaoLinkService.getInstance().sendCustom(mContext, templateId, templateArgs, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
                                @Override
                                public void onFailure(ErrorResult errorResult) {

                                }

                                @Override
                                public void onSuccess(KakaoLinkResponse result) {
                                    // 템플릿 밸리데이션과 쿼터 체크가 성공적으로 끝남. 톡에서 정상적으로 보내졌는지 보장은 할 수 없다. 전송 성공 유무는 서버콜백 기능을 이용하여야 한다.
                                }
                            });

                            alertdialog.dismiss();
                        }
                    });


                    alertdialog.show();
                }


            }
        });








    }

    public void check_id(){

    }

    @Override
    public int getItemCount(){
        return (null !=minvite ? minvite.size() :0);
    }
}
