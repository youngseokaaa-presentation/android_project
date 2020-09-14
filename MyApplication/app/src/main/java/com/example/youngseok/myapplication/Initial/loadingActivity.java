package com.example.youngseok.myapplication.Initial;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;

import com.example.youngseok.myapplication.R;

public class loadingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        startLoading();

    }

    private void startLoading(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                finish();
            }
        }, 1000);
    }








    @Override
    public void onBackPressed(){
        //취소버튼을 눌러도 메인액티비티로 가는게 아니라 아무 동작 안하게 설정
    }




}
