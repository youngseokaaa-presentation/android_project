package com.example.youngseok.myapplication.GroupContent.chat;



import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.UserHandle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.youngseok.myapplication.GroupContent.GroupContentActivity;
import com.example.youngseok.myapplication.GroupContent.chat.noti.RequestHttpURLConnection;
import com.example.youngseok.myapplication.GroupContent.chat.noti.myroomValidate;
import com.example.youngseok.myapplication.Initial.InitialActivity;
import com.example.youngseok.myapplication.MainActivity;
import com.example.youngseok.myapplication.MygroupActivity;
import com.example.youngseok.myapplication.R;
import com.example.youngseok.myapplication.invite.InviteActivity;
import com.example.youngseok.myapplication.make_group.CustomAdapter;
import com.example.youngseok.myapplication.make_group.MakeGroupActivity;
import com.example.youngseok.myapplication.make_group.basicGroup;
import com.example.youngseok.myapplication.setting.SettingActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.youngseok.myapplication.Initial.InitialActivity.save_my_id;

public class ChattingActivity extends AppCompatActivity {
    Toolbar toolbar;

    ImageButton timeline;
    ImageButton mygroup;
    ImageButton makegroup;
    ImageButton invitefriend;
    ImageButton myset;

    private EditText user_chat, user_edit;
    private Button user_next;


    private String group_name;
    private String usr_id;
    private String master_key;

    private String CHAT_NAME;
    private String USER_NAME;

    private RecyclerView chat_view;
    private EditText chat_edit;
    private Button chat_send;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private ArrayList<ChatDTO> chatArrayList;
    private ChatAdapter chatAdapter;


    private CheckBox notibox;

    private String noti_flag = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
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

        Intent intent = getIntent();
        group_name=intent.getStringExtra("group_name");

        master_key=intent.getStringExtra("master_key");

        usr_id=save_my_id;


        notibox = findViewById(R.id.noti_box);


        timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_main = new Intent(ChattingActivity.this,MainActivity.class);
                startActivity(go_main);
                overridePendingTransition(0,0);
                finish();
            }
        });

        mygroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_mygroup = new Intent(ChattingActivity.this,MygroupActivity.class);
                startActivity(go_mygroup);
                overridePendingTransition(0,0);
                finish();
            }
        });

        makegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent go_make = new Intent(ChattingActivity.this,MakeGroupActivity.class);
                startActivity(go_make);
                overridePendingTransition(0,0);
                finish();



            }
        });

        myset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_set = new Intent(ChattingActivity.this,SettingActivity.class);
                startActivity(go_set);
                overridePendingTransition(0,0);
                finish();
            }
        });

        invitefriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_invite = new Intent(ChattingActivity.this,InviteActivity.class);
                startActivity(go_invite);
                overridePendingTransition(0,0);
                finish();
            }
        });



        chat_view = findViewById(R.id.chat_view);
        chat_edit = (EditText) findViewById(R.id.chat_edit);
        chat_send = (Button) findViewById(R.id.chat_sent);

        // 로그인 화면에서 받아온 채팅방 이름, 유저 이름 저장

        CHAT_NAME = master_key;

        USER_NAME = save_my_id;





        check_my_room();


        // 채팅 방 입장
        openChat(CHAT_NAME);







        // 메시지 전송 버튼에 대한 클릭 리스너 지정
        chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chat_edit.getText().toString().equals(""))
                    return;


                SimpleDateFormat format3= new SimpleDateFormat ( "MM월 dd일 hh시 mm분");
                Date time_2 = new Date();
                String time3 = format3.format(time_2);
                final String chat_nae = chat_edit.getText().toString();







                Thread tha = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();

                        MediaType mediaType = MediaType.parse("application/json");
                        RequestBody body = RequestBody.create(mediaType, "{\n    \"to\": \"/topics/ALL\",\n    \"data\": {\n        \"title\": \""+CHAT_NAME+"\",\n        \"content\": \""+"공지 : "+chat_nae+"\"\n    }\n}");
                        Request request = new Request.Builder()
                                .url("https://fcm.googleapis.com/fcm/send")
                                .post(body)
                                .addHeader("Content-Type", "application/json")
                                .addHeader("Authorization", "key=AAAAveVy79M:APA91bGzxsbSp9kPiLXgdAfSTYXGTCmTWHAo_NF8u8Lugr99tlwQQgH6APaVLsTjOWmNcJ9nXOcKVHMYP4kFWi5f1G-BpjeJP0dkualEunGc1MBNZaNQMnVvuJHZnuH0SylrJfBtFL7k")
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                if (notibox.isChecked()==true){
                    tha.start();
                    noti_flag="1";
                }
                else if(notibox.isChecked()==false){
                    noti_flag="0";
                }


                ChatDTO chat = new ChatDTO(USER_NAME, chat_edit.getText().toString(),time3,noti_flag); //ChatDTO를 이용하여 데이터를 묶는다.
                databaseReference.child("chat").child(CHAT_NAME).push().setValue(chat); // 데이터 푸쉬

                chat_edit.setText(""); //입력창 초기화



            }
        });


        String url = "https://fcm.googleapis.com/fcm/send";
        NetworkTask networkTask = new NetworkTask(url, null);
        networkTask.execute();



    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }


    private void addMessage(DataSnapshot dataSnapshot, ArrayList adapter) {
        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);

        adapter.add(0,chatDTO);
    }



    private void openChat(String chatName) {
        // 리스트 어댑터 생성 및 세팅
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);



        chat_view.setLayoutManager(mLayoutManager);
        chatArrayList=new ArrayList<>();

        chatAdapter = new ChatAdapter(this,chatArrayList);

        chat_view.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child("chat").child(chatName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addMessage(dataSnapshot, chatArrayList);

                chatAdapter.notifyDataSetChanged();
                Log.e("LOG", "s:"+s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //      removeMessage(dataSnapshot, chatAdapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void check_my_room(){
        com.android.volley.Response.Listener<String> responseListener = new com.android.volley.Response.Listener<String>(){



                    @Override
                    public void onResponse(String response){
                        try{


                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success =jsonResponse.getBoolean("success");
                            final String joiner = jsonResponse.getString("joiner");
                            if(success){

                                if(save_my_id.equals(joiner)){
                                    notibox.setVisibility(View.VISIBLE);
                                }

                            }
                            else{

                                Log.e("tjdrhdgo","atjdrhdgoa");
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                //volley 라이브러리 이용해서 실제 서버와 통신
                myroomValidate myroomvalidate = new myroomValidate(save_my_id,group_name,responseListener);
                RequestQueue queue = Volley.newRequestQueue(ChattingActivity.this);
                queue.add(myroomvalidate);

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
