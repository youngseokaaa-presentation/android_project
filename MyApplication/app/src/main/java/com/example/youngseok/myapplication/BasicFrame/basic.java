package com.example.youngseok.myapplication.BasicFrame;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.example.youngseok.myapplication.MainActivity;
import com.example.youngseok.myapplication.MygroupActivity;
import com.example.youngseok.myapplication.R;
import com.example.youngseok.myapplication.invite.InviteActivity;
import com.example.youngseok.myapplication.make_group.CustomAdapter;
import com.example.youngseok.myapplication.make_group.MakeGroupActivity;
import com.example.youngseok.myapplication.make_group.basicGroup;
import com.example.youngseok.myapplication.setting.SettingActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.youngseok.myapplication.Initial.InitialActivity.save_my_id;

public class basic extends AppCompatActivity {
    Toolbar toolbar;

    ImageButton timeline;
    ImageButton mygroup;
    ImageButton makegroup;
    ImageButton invitefriend;
    ImageButton myset;

    private ArrayList<basicGroup> mArrayList;
    private CustomAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private int count = 0;

    private String mJsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                Intent go_main = new Intent(basic.this,MainActivity.class);
                startActivity(go_main);
                overridePendingTransition(0,0);
                finish();
            }
        });

        mygroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_mygroup = new Intent(basic.this,MygroupActivity.class);
                startActivity(go_mygroup);
                overridePendingTransition(0,0);
                finish();
            }
        });

        makegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent go_make = new Intent(basic.this,MakeGroupActivity.class);
                startActivity(go_make);
                overridePendingTransition(0,0);
                finish();



            }
        });
        myset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_set = new Intent(basic.this,SettingActivity.class);
                startActivity(go_set);
                overridePendingTransition(0,0);
                finish();
            }
        });

        invitefriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_invite = new Intent(basic.this,InviteActivity.class);
                startActivity(go_invite);
                overridePendingTransition(0,0);
                finish();
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
