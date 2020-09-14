package com.example.youngseok.myapplication.GroupContent.member_list;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.youngseok.myapplication.GroupContent.GroupContentActivity;
import com.example.youngseok.myapplication.GroupContent.GroupContentDTO;
import com.example.youngseok.myapplication.GroupContent.chat.ChattingActivity;
import com.example.youngseok.myapplication.MainActivity;
import com.example.youngseok.myapplication.MygroupActivity;
import com.example.youngseok.myapplication.R;
import com.example.youngseok.myapplication.invite.InviteActivity;
import com.example.youngseok.myapplication.invite.InviteDTO;
import com.example.youngseok.myapplication.make_group.MakeGroupActivity;
import com.example.youngseok.myapplication.setting.SettingActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static com.example.youngseok.myapplication.Initial.InitialActivity.save_my_id;

public class Member_listActivity extends AppCompatActivity {
    Toolbar toolbar;

    ImageButton timeline;
    ImageButton mygroup;
    ImageButton makegroup;
    ImageButton invitefriend;
    ImageButton myset;

    private TextView member_number;

    private ArrayList<GroupContentDTO> groupcontent_Array;

    private ArrayList<Member_listDTO> member_list_Array;
    private Member_listAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private String mJsonString;

    private FloatingActionButton add_btn;

    private ArrayList <InviteDTO>save_invite_list;

    private ArrayList<Member_invite_DTO> invite_list;
    private String master_key;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = findViewById(R.id.toolbar);

        SimpleDateFormat format2 = new SimpleDateFormat ( "yyyy년 MM월 dd일");
        Date time = new Date();
        String time2 = format2.format(time);
        toolbar.setSubtitle(time2);

        timeline=findViewById(R.id.timeline_btn);
        mygroup=findViewById(R.id.new_my);
        makegroup=findViewById(R.id.new_make);
        invitefriend=findViewById(R.id.invite_btn);
        myset=findViewById(R.id.setting_btn);





        timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_main = new Intent(Member_listActivity.this,MainActivity.class);
                startActivity(go_main);
                overridePendingTransition(0,0);
                finish();
            }
        });

        mygroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_mygroup = new Intent(Member_listActivity.this,MygroupActivity.class);
                startActivity(go_mygroup);
                overridePendingTransition(0,0);
                finish();
            }
        });

        makegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent go_make = new Intent(Member_listActivity.this,MakeGroupActivity.class);
                startActivity(go_make);
                overridePendingTransition(0,0);
                finish();



            }
        });
        myset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_set = new Intent(Member_listActivity.this,SettingActivity.class);
                startActivity(go_set);
                overridePendingTransition(0,0);
                finish();
            }
        });

        invitefriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_invite = new Intent(Member_listActivity.this,InviteActivity.class);
                startActivity(go_invite);
                overridePendingTransition(0,0);
                finish();
            }
        });

        member_number=findViewById(R.id.member_number);

        Intent intent = getIntent();
        groupcontent_Array = (ArrayList<GroupContentDTO>) intent.getSerializableExtra("array");
        master_key=intent.getStringExtra("master_key");
        for (int index=0;index<groupcontent_Array.size();index++){

            Log.e("20180408",groupcontent_Array.get(index).getGroup_name());
        }
        toolbar.setTitle(groupcontent_Array.get(0).getGroup_name()+"_회원관리");
        String member_num = String.valueOf(groupcontent_Array.size());
        member_number.setText(member_num+"명");



        member_list_Array=new ArrayList<>();
        mAdapter = new Member_listAdapter(this,member_list_Array);
        mRecyclerView = findViewById(R.id.member_list);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.notifyDataSetChanged();



        String abc = "member_name";

        for(int index = 0; index<groupcontent_Array.size();index++){
            GetData task = new GetData();
            Log.e("20180409",groupcontent_Array.get(index).getGroup_joiner());
            task.execute("http://192.168.43.34/group_content/member_list.php",groupcontent_Array.get(index).getGroup_joiner());
        }





        //여기서 나누고 지지고 볶고 해보장


        add_btn= findViewById(R.id.add_btn);

        String keyword = save_my_id;
        if(save_my_id.equals(groupcontent_Array.get(0).getGroup_id())){
            add_btn.setVisibility(View.VISIBLE);
        }
        else
        {
            add_btn.setVisibility(View.GONE);
        }


        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent invite = new Intent(Member_listActivity.this,InviteActivity.class);
                String master = "master";
                invite.putExtra("moim_master",master);
                invite.putExtra("member_list",member_list_Array);
                startActivityForResult(invite,1993);



            }
        });


        save_invite_list = new ArrayList<>();

        invite_list = new ArrayList<>();




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){


                case 1993:


                    Log.e("20180410",data.getStringExtra("msg"));
                    save_invite_list = (ArrayList)data.getSerializableExtra("save_invite_list");
                    for(int index=0;index<save_invite_list.size();index++){
                        Log.e("201804100000",save_invite_list.get(index).getPhonebook_phone());

                        GetData_check task = new GetData_check();
                        task.execute("http://192.168.43.34/group_content/check_id.php",save_invite_list.get(index).getPhonebook_phone());


                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(Member_listActivity.this);
                    builder.setTitle(groupcontent_Array.get(0).getGroup_name());
                    builder.setMessage(data.getStringExtra("msg"));

                    builder.setNegativeButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.show();

                    break;
            }
        }
    }





    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Member_listActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            if(result==null){

            }
            else{
                mJsonString=result;
                showResult();

            }


        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = "joiner=" + params[1];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                errorString = e.toString();

                return null;
            }

        }

    }
    private void showResult(){

        String TAG_JSON="youngseok";
        String TAG_name = "name";
        String TAG_nickname = "nickname";
        String TAG_id ="id";
        String TAG_phone = "phone";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String name = item.getString(TAG_name);
                String nickname = item.getString(TAG_nickname);
                String id = item.getString(TAG_id);
                String phone = item.getString(TAG_phone);

                Member_listDTO member_listDTO = new Member_listDTO();

                member_listDTO.setName(name);
                member_listDTO.setNickname(nickname);
                member_listDTO.setId(id);
                member_listDTO.setPhone(phone);
                if(groupcontent_Array.get(0).getGroup_id().equals(id)){
                    member_listDTO.setStatus("모임장");
                }
                else
                {
                    member_listDTO.setStatus("회원");
                }

                member_list_Array.add(member_listDTO);
            }
            Log.e("20180409",String.valueOf(member_list_Array.size()));









        } catch (JSONException e) {

        }
        Collections.reverse(member_list_Array);


        mAdapter.notifyDataSetChanged();

    }

    private class GetData_check extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Member_listActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            if(result==null){

            }
            else{
                mJsonString=result;
                showResult_check();

            }
            for (int index=0; index<invite_list.size();index++){
                Log.e("2018Tlqkf",groupcontent_Array.get(0).getGroup_id());
                //invite_list.get(index).getId() ==joiner
                //groupcontent_Array.get(0).getGroup_name() ==name
                //groupcontent_Array.get(0).getGroup_id() ==master_id
                //master_key;

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
                Insert_waiting_request insertwaitingrequest = new Insert_waiting_request(master_key,groupcontent_Array.get(0).getGroup_id()
                        ,invite_list.get(index).getId(),groupcontent_Array.get(0).getGroup_name(),responseListener);
                RequestQueue queue = Volley.newRequestQueue(Member_listActivity.this);
                queue.add(insertwaitingrequest);
            }
            invite_list = new ArrayList<>();


        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = "phone=" + params[1];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                errorString = e.toString();

                return null;
            }

        }

    }
    private void showResult_check(){

        String TAG_JSON="youngseok";
        String TAG_id = "id";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String id = item.getString(TAG_id);

                Member_invite_DTO memberinvitedto = new Member_invite_DTO();

                memberinvitedto.setId(id);

                invite_list.add(memberinvitedto);
            }

            Log.e("TlqkfwlsWk",String.valueOf(invite_list.size()));
            for(int index=0; index<invite_list.size();index++){
                Log.e("20181010101",invite_list.get(index).getId());
            }

        } catch (JSONException e) {

        }




    }




    @Override
    protected void onStart(){
        super.onStart();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
    @Override
    protected void onPause(){
        super.onPause();
    }
    @Override
    protected void onRestart(){
        super.onRestart();
    }
    @Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    protected void onStop(){
        super.onStop();
    }

}