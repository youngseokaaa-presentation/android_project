package com.example.youngseok.myapplication.GroupContent;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.youngseok.myapplication.GroupContent.Financial.FinancialActivity;
import com.example.youngseok.myapplication.GroupContent.Location.LocationActivity;
import com.example.youngseok.myapplication.GroupContent.Schedule.ScheduleActivity;
import com.example.youngseok.myapplication.GroupContent.Shot.ShotActivity;
import com.example.youngseok.myapplication.GroupContent.android_ML.ReceiptActivity;
import com.example.youngseok.myapplication.GroupContent.ar.FaceActivity;
import com.example.youngseok.myapplication.GroupContent.ar.ar_sub.ZooActivity;
import com.example.youngseok.myapplication.GroupContent.chat.ChattingActivity;
import com.example.youngseok.myapplication.GroupContent.member_list.Member_listActivity;
import com.example.youngseok.myapplication.GroupContent.photo.PhotoActivity;
import com.example.youngseok.myapplication.MainActivity;
import com.example.youngseok.myapplication.MygroupActivity;
import com.example.youngseok.myapplication.R;
import com.example.youngseok.myapplication.invite.InviteActivity;
import com.example.youngseok.myapplication.make_group.CustomAdapter;
import com.example.youngseok.myapplication.make_group.MakeGroupActivity;
import com.example.youngseok.myapplication.make_group.basicGroup;
import com.example.youngseok.myapplication.setting.SettingActivity;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.acl.Group;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.youngseok.myapplication.Initial.InitialActivity.save_my_id;

public class GroupContentActivity extends AppCompatActivity {
    Toolbar toolbar;

    ImageButton timeline;
    ImageButton mygroup;
    ImageButton makegroup;
    ImageButton invitefriend;
    ImageButton myset;

    Button Chat_btn;

    private String group_name;
    private String master_key;

    private TextView member_number;

    private String mJsonString;

    private ArrayList<GroupContentDTO> mArraylist_content;

    Button member_list;
    Button moim_exit;

    Button schedule_check;

    Button location;

    Button financial_btn;
    Button photo_btn;
    Button recept_btn;
    Button shot_btn;
    Button go_ar;

   ArrayList<String> spinnerarray = new ArrayList<>();
   private String bank_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupcontent);
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

        Chat_btn = findViewById(R.id.Chat_btn);

        member_list=findViewById(R.id.member_list);
        moim_exit=findViewById(R.id.moim_exit);

        location=findViewById(R.id.location_btn);

        financial_btn=findViewById(R.id.financial_btn);
        photo_btn=findViewById(R.id.photo_btn);
        recept_btn=findViewById(R.id.add_Recep);
        shot_btn=findViewById(R.id.go_shot);
        go_ar=findViewById(R.id.go_ar);


        timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_main = new Intent(GroupContentActivity.this,MainActivity.class);
                startActivity(go_main);
                overridePendingTransition(0,0);
                finish();
            }
        });

        mygroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_mygroup = new Intent(GroupContentActivity.this,MygroupActivity.class);
                startActivity(go_mygroup);
                overridePendingTransition(0,0);
                finish();
            }
        });

        makegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent go_make = new Intent(GroupContentActivity.this,MakeGroupActivity.class);
                startActivity(go_make);
                overridePendingTransition(0,0);
                finish();



            }
        });
        myset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_set = new Intent(GroupContentActivity.this,SettingActivity.class);
                startActivity(go_set);
                overridePendingTransition(0,0);
                finish();
            }
        });

        invitefriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_invite = new Intent(GroupContentActivity.this,InviteActivity.class);
                startActivity(go_invite);
                overridePendingTransition(0,0);
                finish();
            }
        });

        Chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_chat = new Intent(GroupContentActivity.this,ChattingActivity.class);
                go_chat.putExtra("group_name",group_name);
                go_chat.putExtra("master_key",master_key);
                startActivity(go_chat);
                overridePendingTransition(0,0);
            }
        });
        photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_photo = new Intent(GroupContentActivity.this, PhotoActivity.class);
                go_photo.putExtra("master_key",master_key);
                startActivity(go_photo);
                overridePendingTransition(0,0);
            }
        });

        member_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_member_list = new Intent(GroupContentActivity.this,Member_listActivity.class);
                go_member_list.putExtra("array",mArraylist_content);
                go_member_list.putExtra("master_key",master_key);
                startActivity(go_member_list);
                overridePendingTransition(0,0);
            }
        });

        go_ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(GroupContentActivity.this);
                builder.setMessage("어디로갈까용");
                builder.setNegativeButton("ar가면",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("sinsae","fdsa");
                                Intent go_ar = new Intent(GroupContentActivity.this, FaceActivity.class);
                                startActivity(go_ar);
                            }
                        });
                builder.setPositiveButton("ar동물원", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        Log.e("sinsae","asdf");

                        Intent go_ar = new Intent(GroupContentActivity.this, ZooActivity.class);

                        startActivity(go_ar);

                    }
                });

                builder.show();

            }
        });

        moim_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(GroupContentActivity.this);
                builder.setTitle(group_name);
                builder.setMessage("정말로 이 모임을 나가시겠어요?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                Response.Listener<String> responseListener = new Response.Listener<String>(){

                                    @Override
                                    public void onResponse(String response){
                                        try{


                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success =jsonResponse.getBoolean("success");
                                            if(success){

                                               Intent go_main =new Intent(GroupContentActivity.this,MygroupActivity.class);
                                               startActivity(go_main);
                                               finish();
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
                                Moim_exitRequest moim_exitRequest = new Moim_exitRequest(save_my_id,master_key,responseListener);
                                RequestQueue queue = Volley.newRequestQueue(GroupContentActivity.this);
                                queue.add(moim_exitRequest);



                                Toast.makeText(getApplicationContext(),"모임 탈퇴를 완료하였습니다.",Toast.LENGTH_LONG).show();
                            }
                        });
                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();



            }
        });



        schedule_check = findViewById(R.id.schedule_check);
        schedule_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_schedule = new Intent(GroupContentActivity.this,ScheduleActivity.class);
                go_schedule.putExtra("array",mArraylist_content);
                go_schedule.putExtra("master_key",master_key);
                startActivity(go_schedule);
                overridePendingTransition(0,0);
            }
        });



        recept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder builder = new AlertDialog.Builder(GroupContentActivity.this);
                builder.setMessage("어떤방식으로 인식할까요?");
                builder.setNegativeButton("촬영",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("sinsae","fdsa");

                                int a=1;
                                Intent go_recep = new Intent(GroupContentActivity.this, ReceiptActivity.class);
                                go_recep.putExtra("pic",a);
                                startActivity(go_recep);
                            }
                        });
                builder.setPositiveButton("갤러리에서 가져오기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        Log.e("sinsae","asdf");
                        int a=2;
                        Intent go_recep = new Intent(GroupContentActivity.this, ReceiptActivity.class);
                        go_recep.putExtra("pic",a);
                        startActivity(go_recep);

                    }
                });

                builder.show();







            }
        });

        shot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_shot = new Intent(GroupContentActivity.this, ShotActivity.class);
                startActivity(go_shot);
            }
        });


        Intent intent = getIntent();
        group_name=intent.getStringExtra("group_name");
        master_key=intent.getStringExtra("group_master_key");
        Log.e("groupname",group_name);
        Log.e("groupmaster",master_key);

        toolbar.setTitle(group_name);

        member_number=findViewById(R.id.member_number);

        mArraylist_content=new ArrayList<>();

        GetData task = new GetData();
        task.execute("http://192.168.43.34/group_content/groupContentRequest.php",master_key);


        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_loca = new Intent(GroupContentActivity.this,LocationActivity.class);
                go_loca.putExtra("master_key",master_key);
                startActivity(go_loca);
                overridePendingTransition(0,0);
            }
        });

        spinnerarray.add("기업은행 문자sms통지");
        spinnerarray.add("기업은행 i-one 알림");
        spinnerarray.add("수기로 작성");

        financial_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Response.Listener<String> responseListener = new Response.Listener<String>(){



                    @Override
                    public void onResponse(String response){
                        try{


                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success =jsonResponse.getBoolean("success");
                            if(success){
                                if(mArraylist_content.get(0).getGroup_id().equals(save_my_id)){
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(GroupContentActivity.this);
                                    builder.setMessage("등록된 계좌가 없습니다! 지금 등록하시겠습니까");
                                    builder.setNegativeButton("아니오",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });
                                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialog, int which) {




                                            final AlertDialog.Builder builder_sub = new AlertDialog.Builder(GroupContentActivity.this);


                                            View view = LayoutInflater.from(GroupContentActivity.this).inflate(R.layout.create_account_dialog,null,false);

                                            builder_sub.setView(view);

                                            final AlertDialog alertdialog_sub = builder_sub.create();


                                            final Spinner account_spinner = view.findViewById(R.id.account_spinner);
                                            final TextView textView26 = view.findViewById(R.id.textView26);
                                            final EditText account_edit = view.findViewById(R.id.account_edit);
                                            final Button account_btn = view.findViewById(R.id.account_btn);




                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(GroupContentActivity.this,R.layout.support_simple_spinner_dropdown_item,spinnerarray);

                                            account_spinner.setAdapter(adapter);



                                            account_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                                    Log.e("peeeeeeeeeeeeeee",String.valueOf(position));

                                                    if(position==2){
                                                        textView26.setVisibility(View.GONE);
                                                        account_edit.setVisibility(View.GONE);
                                                        account_btn.setText("바로만들기");
                                                        bank_type="2";
                                                    }
                                                    else{
                                                        textView26.setVisibility(View.VISIBLE);
                                                        account_edit.setVisibility(View.VISIBLE);
                                                        account_btn.setText("확인");
                                                        bank_type=String.valueOf(position);
                                                    }

                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {

                                                }
                                            });





                                            account_btn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    String bb="";
                                                    if(bank_type.equals("0")){
                                                        bb="sms";
                                                        if(account_edit.getText().toString().equals("")){
                                                            Toast.makeText(getApplicationContext(), "공백을 채워주세요~", Toast.LENGTH_LONG).show();
                                                            return;
                                                        }
                                                    }
                                                    else if(bank_type.equals("1")){
                                                        bb="ione";
                                                        if(account_edit.getText().toString().equals("")){
                                                            Toast.makeText(getApplicationContext(), "공백을 채워주세요~", Toast.LENGTH_LONG).show();
                                                            return;
                                                        }

                                                    }
                                                    else if(bank_type.equals("2")){
                                                        bb="hand";
                                                        account_edit.setText("");
                                                    }
                                                    else{}



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
                                                    insert_account_Request insert_account_request = new insert_account_Request(master_key,account_edit.getText().toString(),bb,responseListener);
                                                    RequestQueue queue = Volley.newRequestQueue(GroupContentActivity.this);
                                                    queue.add(insert_account_request);



                                                    alertdialog_sub.dismiss();

                                                    Intent go_finan = new Intent(GroupContentActivity.this, FinancialActivity.class);
                                                    go_finan.putExtra("master_key",master_key);
                                                    go_finan.putExtra("master_id",mArraylist_content.get(0).getGroup_id());
                                                    startActivity(go_finan);
                                                    overridePendingTransition(0,0);
                                                    finish();





                                                }
                                            });
                                            alertdialog_sub.show();
                                        }
                                    });

                                    builder.show();
                                    Log.e("peeeeeeee",mArraylist_content.get(0).getGroup_id());
                                }
                                else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(GroupContentActivity.this);
                                    AlertDialog dialog = builder.setMessage("아직 모임장이 재정관리를 시작하지 않았습니다!").setPositiveButton("ok",null).create();
                                    dialog.show();
                                    Log.e("peeeeeeee",mArraylist_content.get(0).getGroup_id());
                                }





                            }
                            else{
                                Intent go_finan = new Intent(GroupContentActivity.this, FinancialActivity.class);
                                go_finan.putExtra("master_key",master_key);
                                go_finan.putExtra("master_id",mArraylist_content.get(0).getGroup_id());
                                startActivity(go_finan);
                                overridePendingTransition(0,0);
                                finish();
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                //volley 라이브러리 이용해서 실제 서버와 통신
                check_account_Request check_account_request = new check_account_Request(master_key,responseListener);
                RequestQueue queue = Volley.newRequestQueue(GroupContentActivity.this);
                queue.add(check_account_request);





            }
        });


    }



    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(GroupContentActivity.this,
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

            String msg = String.valueOf(mArraylist_content.size());
            member_number.setText(msg+"명");

        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = "master_key=" + params[1];


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
        String TAG_id = "id";
        String TAG_name = "name";
        String TAG_joiner ="joiner";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String id = item.getString(TAG_id);
                String name = item.getString(TAG_name);
                String joiner = item.getString(TAG_joiner);

                GroupContentDTO groupcontentdto = new GroupContentDTO();

                groupcontentdto.setGroup_id(id);
                groupcontentdto.setGroup_name(name);
                groupcontentdto.setGroup_joiner(joiner);

                mArraylist_content.add(groupcontentdto);
            }
            Log.e("20180408",String.valueOf(mArraylist_content.size()));

            for (int index=0;index<mArraylist_content.size();index++){
                Log.e("20180408",mArraylist_content.get(index).getGroup_id());
                Log.e("20180408",mArraylist_content.get(index).getGroup_name());
                Log.e("20180408",mArraylist_content.get(index).getGroup_joiner());
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
