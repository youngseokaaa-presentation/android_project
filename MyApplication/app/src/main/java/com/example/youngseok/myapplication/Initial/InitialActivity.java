package com.example.youngseok.myapplication.Initial;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.youngseok.myapplication.Initial.facebook.FacebookLogin;
import com.example.youngseok.myapplication.Initial.kakao.GetHash;
import com.example.youngseok.myapplication.Initial.kakao.SessionCallback;
import com.example.youngseok.myapplication.MainActivity;
import com.example.youngseok.myapplication.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;

import org.json.JSONObject;

import java.util.List;

public class InitialActivity extends AppCompatActivity {

    private Button signup_btn;  //회원가입 버튼

    public static String instance_save_id = ""; //static 선언해서 회원가입시 아이디 전달받게할거임. 처음에는 아무 문자 표시안함.

    private EditText insert_id; //아이디 입력창
    private CheckBox remember_id;   //아이디 기억 체크박스
    private SharedPreferences appData;  //로그인 기억 체크박스, 아이디값 저장하는 변수
    private boolean saveLoginData;  //체크박스 체크했는지 안했는지 확인하는친구

    private String id;
    private String pass;


    private SessionCallback callback;
    Button login_kakao;


    private FacebookLogin facebookLogin;
    private Button but;

    private EditText insert_password;

    private Button login;

    public static String save_my_id;

    private String pending;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Intent intent_loading = new Intent(InitialActivity.this,loadingActivity.class);
        startActivity(intent_loading);
        //로딩화면으로 이동
        Log.i("여기 실행되냐",  "확인하러왔다");









        signup_btn = findViewById(R.id.signup_btn);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v_signup_btn) {
                Intent go_signup = new Intent(InitialActivity.this,signupActivity.class);
                startActivity(go_signup);
                //회원가입 버튼 누를시 회원가입 페이지로 이동
            }
        });

        insert_id = findViewById(R.id.insert_id);
        remember_id = findViewById(R.id.Remember_id);
        appData = getSharedPreferences("appData",MODE_PRIVATE);

        insert_password = findViewById(R.id.insert_password);



        loadData();
        if(saveLoginData){
            insert_id.setText(id);
            insert_password.setText(pass);
            remember_id.setChecked(saveLoginData);
            save_my_id=id;
            login();
            //이거랑 로그인 하는거까지 넣자
        }



        GetHash getHash = new GetHash(this);
        callback = new SessionCallback(InitialActivity.this);
        Session.getCurrentSession().addCallback(callback);


        login_kakao = findViewById(R.id.kakao_btn);


        login_kakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton login_kakao_real=(LoginButton)findViewById(R.id.kakao_btn_real);
                login_kakao_real.performClick();
            }
        });



        facebookLogin = new FacebookLogin(InitialActivity.this);



        Button but = findViewById(R.id.button4);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.facebook.login.widget.LoginButton login_button = (com.facebook.login.widget.LoginButton)findViewById(R.id.login_button);
                facebookLogin.setLoginButton(login_button);
                login_button.performClick();
            }
        });



        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

            }

        });


        Button find_id = findViewById(R.id.find_id);
        Button find_pw = findViewById(R.id.find_pw);

        find_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_find_id = new Intent(InitialActivity.this,find_idActivity.class);
                startActivity(go_find_id);

            }
        });

        find_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_find_pw = new Intent(InitialActivity.this,find_pwActivity.class);
                startActivity(go_find_pw);
            }
        });












    }









    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        } else if(facebookLogin.getCallbackManager().onActivityResult(requestCode, resultCode, data)) {
            return;
        }

    }



    private void login(){



        String id = insert_id.getText().toString();
        String password = insert_password.getText().toString();


        Response.Listener<String> responseLisner = new Response.Listener<String>(){



            @Override

            public void onResponse(String response) {

                try{

                    JSONObject jsonResponse = new JSONObject(response);

                    boolean success = jsonResponse.getBoolean("success");



                    if(success){


                        String group_name;
                        String group_name_tw;

                        Intent intent_getgroup = getIntent();
                        group_name=intent_getgroup.getStringExtra("master_key");
                        group_name_tw=intent_getgroup.getStringExtra("group_name");



                        if(TextUtils.isEmpty(group_name)){
                            Intent intent = new Intent(InitialActivity.this, MainActivity.class);
                            save_my_id = insert_id.getText().toString();

                            Intent get_pending = getIntent();

                            pending=get_pending.getStringExtra("pending_master_key");
                            String master_id;
                            String name;
                            master_id=get_pending.getStringExtra("pending_master_id");
                            name=get_pending.getStringExtra("pending_name");

                            if(TextUtils.isEmpty(pending)){
                                Log.e("20180412","empty");
                            }
                            else{
                                Log.e("20180412",pending);
                                intent.putExtra("pending_main_key",pending);
                                intent.putExtra("pending_main_id",master_id);
                                intent.putExtra("pending_main_name",name);
                            }

                            startActivity(intent);

                            finish();
                        }
                        else{
                            Log.e("pleasedd",group_name);

                            Intent intent = new Intent(InitialActivity.this, MainActivity.class);
                            intent.putExtra("master_key",group_name);
                            intent.putExtra("group_name",group_name_tw);








                            save_my_id = insert_id.getText().toString();

                            startActivity(intent);

                            finish();
                        }



                    }else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(InitialActivity.this);

                        AlertDialog dialog = builder.setMessage("아이디와 비밀번호를 다시 확인해주세요!")

                                .setNegativeButton("확인", null)

                                .create();

                        dialog.show();



                    }



                }catch (Exception e){

                    e.printStackTrace();

                }

            }

        };



        LoginRequest loginRequest = new LoginRequest(id, password, responseLisner);

        RequestQueue queue = Volley.newRequestQueue(InitialActivity.this);

        queue.add(loginRequest);
    }









    @Override
    public void onBackPressed(){
    //취소버튼을 눌러도 메인액티비티로 가는게 아니라 아무 동작 안하게 설정
    }

    private void saveData(){
        SharedPreferences.Editor editor = appData.edit();
        editor.putBoolean("SAVE_LOGIN_DATA",remember_id.isChecked());
        editor.putString("ID",insert_id.getText().toString().trim());
        editor.putString("PASS",insert_password.getText().toString().trim());
        editor.apply();
    }
    private void loadData(){
        saveLoginData=appData.getBoolean("SAVE_LOGIN_DATA",false);

        id = appData.getString("ID","");
        pass=appData.getString("PASS","");
    }





    @Override
    protected void onStart(){
        super.onStart();
        android.util.Log.i("test","onStart_initial");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
        android.util.Log.i("test","onDestroy_initial");
    }
    @Override
    protected void onPause(){
        super.onPause();
        android.util.Log.i("test","onPause_initial");
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        insert_id.setText(instance_save_id);
        loadData();
        if(saveLoginData){
            insert_id.setText(id);
            insert_password.setText(pass);
            remember_id.setChecked(saveLoginData);
            login();
        }

        android.util.Log.i("test","onRestart_initial");
    }
    @Override
    protected void onResume(){
        super.onResume();
        android.util.Log.i("test","onResume_initial");

    }
    @Override
    protected void onStop(){
        super.onStop();
        saveData();
        android.util.Log.i("test","onStop_initial");
    }


}

