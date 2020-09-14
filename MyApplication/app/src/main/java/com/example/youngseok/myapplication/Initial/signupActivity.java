package com.example.youngseok.myapplication.Initial;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.nsd.NsdManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.youngseok.myapplication.Initial.join.emailValidate;
import com.example.youngseok.myapplication.Initial.join.idValidate;
import com.example.youngseok.myapplication.Initial.join.joinRequest;
import com.example.youngseok.myapplication.Initial.join.nicknameValidate;
import com.example.youngseok.myapplication.Initial.join.phoneValidate;
import com.example.youngseok.myapplication.R;

import org.json.JSONObject;

import static com.example.youngseok.myapplication.Initial.InitialActivity.instance_save_id;

public class signupActivity extends AppCompatActivity {

    private LinearLayout layout_1, layout_2, layout_3,layout_4,layout_5,layout_6;
    private Button agree_btn,disagree_btn;
    //레이아웃들

    private CheckBox checkbox;


    private Boolean validate_id = false;   //아이디 중복확인시 boolean으로 중복확인
    private Boolean validate_nick=false;    //닉네임 중복확인
    private Boolean validate_email = false;
    private Boolean validate_phone = false;





    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_signup);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        layout_1=findViewById(R.id.layout_1);
        layout_2=findViewById(R.id.layout_2);
        layout_3=findViewById(R.id.layout_3);
        layout_4=findViewById(R.id.layout_4);
        layout_5=findViewById(R.id.layout_5);
        layout_6=findViewById(R.id.layout_6);

        checkbox=findViewById(R.id.checkBox3);



        agree_btn=findViewById(R.id.agree_btn);
        //동의 버튼 누르면 위에 약관들 다 사라지고 회원가입 폼 나타남
        agree_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkbox.isChecked()==true){
                    layout_1.setVisibility(View.GONE);
                    layout_2.setVisibility(View.GONE);
                    layout_3.setVisibility(View.GONE);
                    layout_4.setVisibility(View.GONE);
                    layout_6.setVisibility(View.VISIBLE);
                }

            }
        });

        disagree_btn=findViewById(R.id.disagree_btn);
        //기본 디폴트 화면인데 동의 누른상태에서 반대를 누르면 폼 다 사라지고 다시 약관 나타나게함
        disagree_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_1.setVisibility(View.VISIBLE);
                layout_2.setVisibility(View.VISIBLE);
                layout_3.setVisibility(View.VISIBLE);
                layout_4.setVisibility(View.VISIBLE);
                layout_6.setVisibility(View.GONE);
            }
        });


        final EditText name = findViewById(R.id.signup_name);
        final EditText id = findViewById(R.id.signup_id);
        final EditText password = findViewById(R.id.signup_passwd);
        final EditText password_chk = findViewById(R.id.signup_passwd_chk);
        final EditText nickname = findViewById(R.id.signup_nick);
        final EditText email = findViewById(R.id.signup_email);
        final EditText phone = findViewById(R.id.signup_phone);
        // edit_text로 회원가입폼들 입력받는곳

        final Button idvalidate_btn = findViewById(R.id.signup_id_btn);
        idvalidate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID=id.getText().toString();
                if(validate_id){
                    return;//중복확인 완료시
                }

                if(userID.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(signupActivity.this);
                    AlertDialog dialog = builder.setMessage("아이디를 입력해주세요").setPositiveButton("ok",null).create();
                    dialog.show();
                    return;
                    //공백확인

                }

                //중복확인 시작하는 부분
                //여기서 리스너를 만들고 아래에서 해당 클래스와 함께 파라미터로 넣어서 중복확인을한다.
                Response.Listener<String> responseListener = new Response.Listener<String>(){



                    @Override
                    public void onResponse(String response){
                        try{


                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success =jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder = new AlertDialog.Builder(signupActivity.this);
                                AlertDialog dialog = builder.setMessage("사용가능합니다").setPositiveButton("ok",null).create();
                                dialog.show();

                                id.setEnabled(false);
                                validate_id=true;

                                idvalidate_btn.setBackgroundColor(getResources().getColor(R.color.colorGray));

                            }
                            else{
                                AlertDialog.Builder builder=new AlertDialog.Builder(signupActivity.this);
                                AlertDialog dialog = builder.setMessage("이미 존재하는 아이디 입니다.").setNegativeButton("ok",null).create();
                                dialog.show();
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                //volley 라이브러리 이용해서 실제 서버와 통신
                idValidate idvalidate = new idValidate(userID,responseListener);
                RequestQueue queue = Volley.newRequestQueue(signupActivity.this);
                queue.add(idvalidate);

            }
        });


        final Button nicknamevalidate_btn = findViewById(R.id.signup_nick_btn);
        nicknamevalidate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNICK=nickname.getText().toString();
                if(validate_nick){
                    return;//중복확인 완료시
                }

                if(userNICK.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(signupActivity.this);
                    AlertDialog dialog = builder.setMessage("닉네임을 입력해주세요").setPositiveButton("ok",null).create();
                    dialog.show();
                    return;
                    //공백확인

                }

                //중복확인시작
                //위와 마찬가지로 리스너 만들고 nickname클래스 가져와 같이 파라미터로 전송해서 확인한다요.
                Response.Listener<String> responseListener = new Response.Listener<String>(){


                    @Override
                    public void onResponse(String response){
                        try{


                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success =jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder = new AlertDialog.Builder(signupActivity.this);
                                AlertDialog dialog = builder.setMessage("사용가능합니다").setPositiveButton("ok",null).create();
                                dialog.show();

                                nickname.setEnabled(false);
                                validate_nick=true;

                                nicknamevalidate_btn.setBackgroundColor(getResources().getColor(R.color.colorGray));

                            }
                            else{
                                AlertDialog.Builder builder=new AlertDialog.Builder(signupActivity.this);
                                AlertDialog dialog = builder.setMessage("이미 존재하는 닉네임 입니다.").setNegativeButton("ok",null).create();
                                dialog.show();
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };

                nicknameValidate nicknamevalidate = new nicknameValidate(userNICK,responseListener);
                RequestQueue queue = Volley.newRequestQueue(signupActivity.this);
                queue.add(nicknamevalidate);

            }
        });

        final Button emailvalidate_btn = findViewById(R.id.signup_email_btn);
        emailvalidate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEMAIL=email.getText().toString();
                if(validate_email){
                    return;//중복확인 완료시
                }

                if(userEMAIL.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(signupActivity.this);
                    AlertDialog dialog = builder.setMessage("이메일을 입력해주세요").setPositiveButton("ok",null).create();
                    dialog.show();
                    return;
                    //공백확인

                }

                //중복확인 시작하는 부분
                //여기서 리스너를 만들고 아래에서 해당 클래스와 함께 파라미터로 넣어서 중복확인을한다.
                Response.Listener<String> responseListener = new Response.Listener<String>(){



                    @Override
                    public void onResponse(String response){
                        try{


                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success =jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder = new AlertDialog.Builder(signupActivity.this);
                                AlertDialog dialog = builder.setMessage("사용가능합니다").setPositiveButton("ok",null).create();
                                dialog.show();

                                email.setEnabled(false);
                                validate_email=true;

                                emailvalidate_btn.setBackgroundColor(getResources().getColor(R.color.colorGray));

                            }
                            else{
                                AlertDialog.Builder builder=new AlertDialog.Builder(signupActivity.this);
                                AlertDialog dialog = builder.setMessage("이미 존재하는 이메일 입니다.").setNegativeButton("ok",null).create();
                                dialog.show();
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                //volley 라이브러리 이용해서 실제 서버와 통신
                emailValidate emailvalidate = new emailValidate(userEMAIL,responseListener);
                RequestQueue queue = Volley.newRequestQueue(signupActivity.this);
                queue.add(emailvalidate);

            }
        });

        final Button phonevalidate_btn = findViewById(R.id.signup_phone_btn);
        phonevalidate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPHONE=phone.getText().toString();
                if(validate_phone){
                    return;//중복확인 완료시
                }

                if(userPHONE.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(signupActivity.this);
                    AlertDialog dialog = builder.setMessage("전화번호를 입력해주세요").setPositiveButton("ok",null).create();
                    dialog.show();
                    return;
                    //공백확인

                }

                //중복확인 시작하는 부분
                //여기서 리스너를 만들고 아래에서 해당 클래스와 함께 파라미터로 넣어서 중복확인을한다.
                Response.Listener<String> responseListener = new Response.Listener<String>(){



                    @Override
                    public void onResponse(String response){
                        try{


                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success =jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder = new AlertDialog.Builder(signupActivity.this);
                                AlertDialog dialog = builder.setMessage("사용가능합니다").setPositiveButton("ok",null).create();
                                dialog.show();

                                phone.setEnabled(false);
                                validate_phone=true;

                                phonevalidate_btn.setBackgroundColor(getResources().getColor(R.color.colorGray));

                            }
                            else{
                                AlertDialog.Builder builder=new AlertDialog.Builder(signupActivity.this);
                                AlertDialog dialog = builder.setMessage("이미 존재하는 전화번호 입니다.").setNegativeButton("ok",null).create();
                                dialog.show();
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                //volley 라이브러리 이용해서 실제 서버와 통신
                phoneValidate phonevalidate = new phoneValidate(userPHONE,responseListener);
                RequestQueue queue = Volley.newRequestQueue(signupActivity.this);
                queue.add(phonevalidate);

            }
        });



        //가입완료! 누를시

        Button confirm_btn= findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID=id.getText().toString();
                String userNAME=name.getText().toString();
                String userpassword=password.getText().toString();
                String userpassword_chk=password_chk.getText().toString();
                String userNICK=nickname.getText().toString();
                String userEMAIL=email.getText().toString();
                String userPHONE=phone.getText().toString();
                //입력한 값들 다 가져온다


                //중복확인을 했는지 안했는지 확인한다
                if(!validate_id){
                    AlertDialog.Builder builder = new AlertDialog.Builder(signupActivity.this);
                    AlertDialog dialog = builder.setMessage("아이디 중복확인 해주세요!").setNegativeButton("ok",null).create();
                    dialog.show();
                    return;
                }
                if(!validate_nick){
                    AlertDialog.Builder builder = new AlertDialog.Builder(signupActivity.this);
                    AlertDialog dialog = builder.setMessage("닉네임 중복확인 해주세요!").setNegativeButton("ok",null).create();
                    dialog.show();
                    return;
                }
                if(!validate_email){
                    AlertDialog.Builder builder = new AlertDialog.Builder(signupActivity.this);
                    AlertDialog dialog = builder.setMessage("이메일 중복확인 해주세요!").setNegativeButton("ok",null).create();
                    dialog.show();
                    return;
                }
                if(!validate_phone){
                    AlertDialog.Builder builder = new AlertDialog.Builder(signupActivity.this);
                    AlertDialog dialog = builder.setMessage("핸드폰 번호 중복확인 해주세요!").setNegativeButton("ok",null).create();
                    dialog.show();
                    return;
                }

                //입력사항에 빈칸이 있는지 확인
                if(userpassword.equals("")||userpassword_chk.equals("")||userNAME.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(signupActivity.this);
                    AlertDialog dialog=builder.setMessage("빈칸이 있는거같아요!").setNegativeButton("ok",null).create();
                    dialog.show();
                    return;
                }

                if(!(userpassword.equals(userpassword_chk))){
                    AlertDialog.Builder builder = new AlertDialog.Builder(signupActivity.this);
                    AlertDialog dialog=builder.setMessage("비밀번호가 일치하지않습니다! 다시입력해주세요!").setNegativeButton("ok",null).create();
                    dialog.show();
                    return;
                }


                //모든 폼이 완성된걸 확인했고 회원가입정보를 서버로 전송
                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            boolean success = jsonResponse.getBoolean("success");

                            if(success){

                                instance_save_id = id.getText().toString(); //회원가입 성공시 static변수에 아이디 저장해서 초기화면으로 보냄
                                finish();

                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(signupActivity.this);
                                AlertDialog dialog=builder.setMessage("가입에 실패하였습니다.").setNegativeButton("ok",null).create();
                                dialog.show();

                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };



                joinRequest joinrequest =new joinRequest(userNAME,userID,userpassword,userNICK,userEMAIL,userPHONE,responseListener);
                RequestQueue queue = Volley.newRequestQueue(signupActivity.this);

                queue.add(joinrequest);


            }
        });



        Intent intent = getIntent();
        name.setText(intent.getStringExtra("name"));
        email.setText(intent.getStringExtra("email"));






    }
}
