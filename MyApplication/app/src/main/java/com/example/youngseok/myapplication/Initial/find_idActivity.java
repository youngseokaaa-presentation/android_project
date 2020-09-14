package com.example.youngseok.myapplication.Initial;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.youngseok.myapplication.Initial.find_id_pw.GMailSender;
import com.example.youngseok.myapplication.Initial.find_id_pw.find_id_request;
import com.example.youngseok.myapplication.MainActivity;
import com.example.youngseok.myapplication.R;

import org.json.JSONObject;


public class find_idActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        final EditText insert_name = findViewById(R.id.insert_name);
        final EditText insert_email = findViewById(R.id.insert_email);
        Button find_id = findViewById(R.id.find_id);


        find_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = insert_name.getText().toString();
                String email = insert_email.getText().toString();



                Response.Listener<String> responseLisner = new Response.Listener<String>(){



                    @Override

                    public void onResponse(String response) {

                        try{

                            JSONObject jsonResponse = new JSONObject(response);

                            boolean success = jsonResponse.getBoolean("success");
                            final String id = jsonResponse.getString("id");



                            if(success){


                                AlertDialog.Builder builder = new AlertDialog.Builder(find_idActivity.this);

                                AlertDialog dialog = builder.setMessage("아이디 찾기 결과를 이메일로 보냈습니다!")

                                        .setNegativeButton("확인", null)

                                        .create();

                                dialog.show();


                                Thread th = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            GMailSender sender = new GMailSender("moimoooomoim@gmail.com", "dmqzissycvkzafyj");
                                            sender.sendMail("모임?모임! 어플 아이디 찾기 결과입니다!",
                                                    "아이디 : "+id+" 입니다!",
                                                    "moimoooomoim@gmail.com",
                                                    insert_email.getText().toString());


                                        } catch (Exception e) {
                                            Log.e("SendMail", e.getMessage(), e);
                                        }
                                    }
                                });
                                th.start();




                            }else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(find_idActivity.this);

                                AlertDialog dialog = builder.setMessage("일치하는 정보가 없습니다!")

                                        .setNegativeButton("다시시도", null)

                                        .create();

                                dialog.show();



                            }



                        }catch (Exception e){

                            e.printStackTrace();

                        }

                    }

                };



                find_id_request find_id1 = new find_id_request(email, name, responseLisner);

                RequestQueue queue = Volley.newRequestQueue(find_idActivity.this);

                queue.add(find_id1);




            }
        });

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
