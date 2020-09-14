package com.example.youngseok.myapplication.Initial;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.youngseok.myapplication.Initial.find_id_pw.GMailSender;
import com.example.youngseok.myapplication.Initial.find_id_pw.find_pw_request;
import com.example.youngseok.myapplication.R;

import org.json.JSONObject;

public class find_pwActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        final EditText insert_name = findViewById(R.id.insert_name);
        final EditText insert_email = findViewById(R.id.insert_email);
        final EditText insert_id = findViewById(R.id.insert_id);

        Button find_pw = findViewById(R.id.find_pw);


        find_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = insert_name.getText().toString();
                String email = insert_email.getText().toString();
                String id = insert_id.getText().toString();



                Response.Listener<String> responseLisner = new Response.Listener<String>(){



                    @Override

                    public void onResponse(String response) {

                        try{

                            JSONObject jsonResponse = new JSONObject(response);

                            boolean success = jsonResponse.getBoolean("success");
                            final String password = jsonResponse.getString("password");



                            if(success){


                                AlertDialog.Builder builder = new AlertDialog.Builder(find_pwActivity.this);

                                AlertDialog dialog = builder.setMessage("비밀번호 찾기 결과를 이메일로 보냈습니다!")

                                        .setNegativeButton("확인", null)

                                        .create();

                                dialog.show();


                                Thread th = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            GMailSender sender = new GMailSender("moimoooomoim@gmail.com", "dmqzissycvkzafyj");
                                            sender.sendMail("모임?모임! 어플 비밀번호 찾기 결과입니다!",
                                                    "비밀번호 : "+password+" 입니다!",
                                                    "moimoooomoim@gmail.com",
                                                    insert_email.getText().toString());


                                        } catch (Exception e) {
                                            Log.e("SendMail", e.getMessage(), e);
                                        }
                                    }
                                });
                                th.start();




                            }else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(find_pwActivity.this);

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



                find_pw_request find_pw1 = new find_pw_request(email, name,id, responseLisner);

                RequestQueue queue = Volley.newRequestQueue(find_pwActivity.this);

                queue.add(find_pw1);




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
