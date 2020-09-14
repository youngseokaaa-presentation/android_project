package com.example.youngseok.myapplication.GroupContent.photo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.example.youngseok.myapplication.GroupContent.Financial.Financial_dialog.Financial_dialog_DTO;
import com.example.youngseok.myapplication.MainActivity;
import com.example.youngseok.myapplication.MygroupActivity;
import com.example.youngseok.myapplication.R;
import com.example.youngseok.myapplication.invite.InviteActivity;
import com.example.youngseok.myapplication.make_group.CustomAdapter;
import com.example.youngseok.myapplication.make_group.MakeGroupActivity;
import com.example.youngseok.myapplication.make_group.basicGroup;
import com.example.youngseok.myapplication.setting.SettingActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.youngseok.myapplication.Initial.InitialActivity.save_my_id;

public class PhotoActivity extends AppCompatActivity {
    Toolbar toolbar;

    ImageButton timeline;
    ImageButton mygroup;
    ImageButton makegroup;
    ImageButton invitefriend;
    ImageButton myset;

    private ArrayList<Financial_dialog_DTO> mArrayList;
    private ArrayList<Financial_dialog_DTO> mArrayList1;
    private ArrayList<Financial_dialog_DTO> mArrayList2;
    private ArrayList<Financial_dialog_DTO> mArrayList3;
    private PhotoAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private int count = 0;

    private String mJsonString;

    private String master_key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
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

        String keyword = save_my_id;


        timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_main = new Intent(PhotoActivity.this, MainActivity.class);
                startActivity(go_main);
                overridePendingTransition(0,0);
                finish();
            }
        });

        mygroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_mygroup = new Intent(PhotoActivity.this, MygroupActivity.class);
                startActivity(go_mygroup);
                overridePendingTransition(0,0);
                finish();
            }
        });

        makegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent go_make = new Intent(PhotoActivity.this, MakeGroupActivity.class);
                startActivity(go_make);
                overridePendingTransition(0,0);
                finish();



            }
        });
        myset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_set = new Intent(PhotoActivity.this, SettingActivity.class);
                startActivity(go_set);
                overridePendingTransition(0,0);
                finish();
            }
        });

        invitefriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_invite = new Intent(PhotoActivity.this, InviteActivity.class);
                startActivity(go_invite);
                overridePendingTransition(0,0);
                finish();
            }
        });
        Intent intent = getIntent();
        master_key=intent.getStringExtra("master_key");






        mArrayList = new ArrayList<>();
        mArrayList1 = new ArrayList<>();
        mArrayList2 = new ArrayList<>();
        mArrayList3 = new ArrayList<>();



        mAdapter = new PhotoAdapter(this,mArrayList,mArrayList1,mArrayList2,mArrayList3);
        mRecyclerView = findViewById(R.id.photo_recycler);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.notifyDataSetChanged();
        GetData task = new GetData();
        task.execute("http://192.168.43.34/group_content/photo/show_photo.php",master_key);
        mAdapter.notifyDataSetChanged();




    }


    private class GetData extends AsyncTask<String, Void, String> {

        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);



            if(result==null){

            }
            else{
                mJsonString=result;
                showResult();
            }
            mAdapter.notifyDataSetChanged();

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
        String TAG_master_key="master_key";
        String TAG_financial_dialog_picture="financial_dialog_picture";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i=i+3){

                JSONObject item = jsonArray.getJSONObject(i);


                String master_key = item.getString(TAG_master_key);
                String financial_dialog_picture = item.getString(TAG_financial_dialog_picture);

                Financial_dialog_DTO photodto =new Financial_dialog_DTO();
                photodto.setMaster_key(master_key);
                photodto.setFinancial_dialog_picture("http://192.168.43.34/group_content/financial/financial_image/"+financial_dialog_picture);
                mArrayList1.add(photodto);
                mArrayList.add(photodto);

                JSONObject item1 = jsonArray.getJSONObject(i+1);


                String master_key1 = item1.getString(TAG_master_key);
                String financial_dialog_picture1 = item1.getString(TAG_financial_dialog_picture);

                Financial_dialog_DTO photodto1 =new Financial_dialog_DTO();
                photodto1.setMaster_key(master_key1);
                photodto1.setFinancial_dialog_picture("http://192.168.43.34/group_content/financial/financial_image/"+financial_dialog_picture1);
                mArrayList2.add(photodto1);
                mArrayList.add(photodto1);
                JSONObject item2 = jsonArray.getJSONObject(i+2);


                String master_key2 = item2.getString(TAG_master_key);
                String financial_dialog_picture2 = item2.getString(TAG_financial_dialog_picture);

                Financial_dialog_DTO photodto2 =new Financial_dialog_DTO();
                photodto2.setMaster_key(master_key2);
                photodto2.setFinancial_dialog_picture("http://192.168.43.34/group_content/financial/financial_image/"+financial_dialog_picture2);
                mArrayList3.add(photodto2);
                mArrayList.add(photodto2);
                mAdapter.notifyDataSetChanged();

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

