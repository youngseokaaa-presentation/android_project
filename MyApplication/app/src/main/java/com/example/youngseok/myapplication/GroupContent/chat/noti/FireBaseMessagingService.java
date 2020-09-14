package com.example.youngseok.myapplication.GroupContent.chat.noti;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.youngseok.myapplication.GroupContent.chat.ChattingActivity;
import com.example.youngseok.myapplication.Initial.InitialActivity;
import com.example.youngseok.myapplication.MainActivity;
import com.example.youngseok.myapplication.R;
import com.example.youngseok.myapplication.make_group.basicGroup;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class FireBaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebase";

    private String joiner;

    private SharedPreferences appData;

    private String mJsonString;

    private ArrayList<noti_content> notiArrayList;

    private String title;
    private String body;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {  //data payload로 보내면 실행
        // ...


        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        //여기서 메세지의 두가지 타입(1. data payload 2. notification payload)에 따라 다른 처리를 한다.
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());




        }



        //String title = remoteMessage.getNotification().getTitle();
        //String body = remoteMessage.getNotification().getBody();
        title = remoteMessage.getData().get("title");
        body = remoteMessage.getData().get("content");

        appData = getSharedPreferences("appData",MODE_PRIVATE);

        joiner = appData.getString("ID","");
        Log.e("idididddddi",joiner);
        notiArrayList = new ArrayList<>();

        GetData task = new GetData();
        task.execute("http://192.168.43.34/basicrecycle/noti_query.php",joiner);





        if (body.equals("abc")) {

        }
        else{}

    }

    private void scheduleJob() {
        //이건 아직 나중에 알아 볼것.
        Log.d(TAG, "이것에 대해서는 나중에 알아 보자.");
    }

    private void handleNow() {
        Log.d(TAG, "10초이내 처리됨");
    }

    private void sendNotification(String title, String messageBody,String master_key) {
        if (title == null){
            //제목이 없는 payload이면
            title = "푸시알림"; //기본제목을 적어 주자.
        }
        Intent intent = new Intent(this, InitialActivity.class);
        intent.putExtra("master_key",master_key);
        intent.putExtra("group_name",title);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{1000, 1000})
                .setLights(Color.BLUE, 1,1)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelName = getString(R.string.default_notification_channel_name);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(channel);
        }





        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    @Override
    public void onNewToken(String s){
        super.onNewToken(s);
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

        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = "joiner=" + params[1];
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
        String TAG_master_key = "master_key";
        String TAG_name ="name";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);


                String master_key = item.getString(TAG_master_key);
                String name= item.getString(TAG_name);

                Log.e("idid", master_key);


                if(title.equals(master_key)){
                    sendNotification(name, body,master_key);
                }

                noti_content noti_content_1 = new noti_content();

                noti_content_1.setGroup_name(name);
                noti_content_1.setMaster_key(master_key);



                Log.e("ididid",noti_content_1.toString());
                notiArrayList.add(noti_content_1);


            }



        } catch (JSONException e) {

        }

    }



}