package com.example.youngseok.myapplication.BasicFrame.Recycler;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.youngseok.myapplication.R;
import java.util.ArrayList;

public class RecyclerActivity extends AppCompatActivity {





    private ArrayList<RecyclerDTO> datas;
    private RecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);




        datas = new ArrayList<>();



        mAdapter = new RecyclerAdapter(this,datas);
        mRecyclerView = findViewById(R.id.invite_recycle);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.notifyDataSetChanged();


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

