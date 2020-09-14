package com.example.youngseok.myapplication.GroupContent.Financial;

import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FinancialNotificationListenerService extends NotificationListenerService {

    private String mJsonString;




    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("NotificationListener", "[snowdeer] onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("NotificationListener", "[snowdeer] onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("NotificationListener", "[snowdeer] onDestroy()");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i("NotificationListener", "[snowdeer] onNotificationPosted() - " + sbn.toString());
        Log.i("NotificationListener", "[snowdeer] PackageName:" + sbn.getPackageName());
        Log.i("NotificationListener", "[snowdeer] PostTime:" + sbn.getPostTime());

        Notification notificatin = sbn.getNotification();
        Bundle extras = notificatin.extras;
        String title = extras.getString(Notification.EXTRA_TITLE);
        int smallIconRes = extras.getInt(Notification.EXTRA_SMALL_ICON);
        Bitmap largeIcon = ((Bitmap) extras.getParcelable(Notification.EXTRA_LARGE_ICON));
        CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);
        String aa = extras.getString(Notification.EXTRA_TEXT);
        CharSequence subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);

        Log.i("NotificationListener", "[snowdeer] Title:" + title);
        Log.i("NotificationListener", "[snowdeer] Text:" + text);
        Log.i("NotificationListener", "[snowdeer] Sub Text:" + subText);
        Log.i("NotificationListener","hihihihihihihihihihiihihihihihihihihihihiih");

        if(TextUtils.isEmpty(aa)){

        }
        else{
            if(sbn.getPackageName().equals("com.IBK.SmartPush.app")){   //ibk은행
                Log.e("NotificationListeneree",aa);
                String[] bb = aa.split("\\n");

                for(int index=0; index<bb.length;index++){
                    Log.e("NotificationListeneree",index+"hi"+bb[index]);
                }

                String[] cc = bb[0].split("\\s");
                String[] dd = bb[1].split("\\s");
                String[] ee = bb[2].split("\\s");

                for(int index=0; index<cc.length;index++){
                    Log.e("zxcv",cc[index]);
                }
                for(int index=0; index<dd.length;index++){
                    Log.e("zxcv",dd[index]);
                }
                for(int index=0; index<ee.length;index++){
                    Log.e("zxcv",ee[index]);
                }

                String[] ff = cc[0].split("\\[");
                String[] gg = ff[1].split("\\]");
                Log.e("last_try",gg[0]);

                String[] hh= cc[1].split("원");
                String[] ii= hh[0].split(",");
                String iii=ii[0]+ii[1];
                Log.e("last_try",iii);

                Log.e("last_try",cc[2]);

                String[] jj=dd[0].split("-");
                String jjj=jj[0]+jj[1]+jj[2]+jj[3];
                Log.e("last_try",jjj);


                String eee = ee[0]+" "+ee[1];
                Log.e("last_try",eee);

                String[] kk = ee[3].split("원");
                String[] ll = kk[0].split(",");
                String lll = ll[0]+ll[1];
                Log.e("last_try",lll);












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
                financialRequest financialrequest = new financialRequest("youngseoka_test_room",jjj,gg[0],iii,cc[2],eee,"기업은행","no",lll,responseListener);
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(financialrequest);




            }


            else{}


        }

//        String s1 = text.toString();
//
//
//        if(TextUtils.isEmpty(s1)){
//
//        }
//        else{
//            Log.e("notificationlisteneree",s1);
//            String[] s2 = s1.split("\\n");
//
//            for(int index=0; index<s2.length;index++){
//                Log.e("notificationlisteneree",index+"ddd"+s2[index]);
//            }
//        }
//




    }



    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("NotificationListener", "[snowdeer] onNotificationRemoved() - " + sbn.toString());
    }
}
