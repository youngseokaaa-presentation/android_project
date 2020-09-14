package com.example.youngseok.myapplication.Service;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.youngseok.myapplication.Initial.InitialActivity;
import com.example.youngseok.myapplication.MainActivity;
import com.example.youngseok.myapplication.R;
import com.example.youngseok.myapplication.make_group.basicGroup;

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
import java.util.Calendar;
import java.util.Date;

public class Add_member_Service extends Service {
    private Thread mainThread;
    public static Intent serviceIntent=null;

    private ArrayList<Wait_member_DTO> wait_member_list;
    public Add_member_Service(){}
    private String mJsonString;
    private SharedPreferences appData;  //로그인 기억 체크박스, 아이디값 저장하는 변수
    private String my_id;
    private PendingIntent pendingIntent;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        wait_member_list = new ArrayList<>();
        appData = getSharedPreferences("appData",MODE_PRIVATE);
        loadData();






        Log.e("20180411","service_create");
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.e("20180411","service_start");
        serviceIntent= intent;


        mainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean run = true;
                while (run){
                    try {


                        GetData task = new GetData();
                        task.execute("http://192.168.43.34/Invite/invite_service.php",my_id);
                        Log.e("20180411",String.valueOf(wait_member_list.size()));
                        if (wait_member_list.size()>0) {



                            String channelid;
                            channelid = wait_member_list.get(0).getMaster_key();

                            Log.e("20180411", "if" + String.valueOf(wait_member_list.size()));

                            Intent intent_pend = new Intent(getApplicationContext(),InitialActivity.class);
                            intent_pend.putExtra("pending_master_key",wait_member_list.get(0).getMaster_key());
                            intent_pend.putExtra("pending_master_id",wait_member_list.get(0).getMaster_id());
                            intent_pend.putExtra("pending_name",wait_member_list.get(0).getName());
                            intent_pend.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0 /* Request code */, intent_pend,
                                    PendingIntent.FLAG_ONE_SHOT);



                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(getApplicationContext(), channelid)
                                            .setAutoCancel(true)
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setContentTitle("우리 모임 들어올래요?")
                                            .setContentText(wait_member_list.get(0).getName()+"모임에서 초대 메세지가 도착했습니다.")
                                            .setContentIntent(pendingIntent);

                            NotificationManager notificationManager =
                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);




                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                // Create channel to show notifications.
                                String channelName = "invite";
                                int importance = NotificationManager.IMPORTANCE_HIGH;
                                NotificationChannel channel = new NotificationChannel(channelid, channelName, importance);
                                notificationManager.createNotificationChannel(channel);
                            }


                            notificationManager.notify(wait_member_list.size() /* ID of notification */, mBuilder.build());

                            Response.Listener<String> responseListener = new Response.Listener<String>(){

                                @Override
                                public void onResponse(String response){
                                    try{


                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success =jsonResponse.getBoolean("success");
                                        if(success){

                                            Log.e("20180411","111111");
                                        }
                                        else{

                                            Log.e("20180411","2222");

                                        }
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            };
                            //volley 라이브러리 이용해서 실제 서버와 통신
                            Waiting_room_exit waitingroomexit = new Waiting_room_exit(
                                    wait_member_list.get(0).getMaster_key()
                                    ,wait_member_list.get(0).getMaster_id()
                                    ,wait_member_list.get(0).getJoiner()
                                    ,wait_member_list.get(0).getName()
                                    ,responseListener);
                            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                            queue.add(waitingroomexit);

                            wait_member_list.remove(0);


                        }




                        else {
                            //리스트 비어있음
                        }
                        Thread.sleep(100000);


                    } catch (InterruptedException e){
                        run = false;
                        e.printStackTrace();
                    }
                }
            }
        });
        mainThread.start();



        return START_NOT_STICKY;
    }
    protected void setAlarmTimer() {

        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, 1);
        Intent intent = new Intent(this, Add_member_Broad.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0,intent,0);

        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);


    }

    @Override
    public void onDestroy() {
        Log.e("20180411","service_des");
        super.onDestroy();
        serviceIntent = null;
        setAlarmTimer();
        Thread.currentThread().interrupt();
        if (mainThread != null) {
            mainThread.interrupt();
            mainThread = null;
        }
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
            if(result==null){}
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
        String TAG_master_id = "master_id";
        String TAG_joiner ="joiner";
        String TAG_name ="name";
        wait_member_list = new ArrayList<>();


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String master_key = item.getString(TAG_master_key);
                String master_id = item.getString(TAG_master_id);
                String joiner = item.getString(TAG_joiner);
                String name = item.getString(TAG_name);

                Wait_member_DTO waitmemberdto = new Wait_member_DTO();

                waitmemberdto.setMaster_key(master_key);
                waitmemberdto.setMaster_id(master_id);
                waitmemberdto.setJoiner(joiner);
                waitmemberdto.setName(name);


                wait_member_list.add(waitmemberdto);
            }



        } catch (JSONException e) {

        }

    }

    private void loadData(){


        my_id = appData.getString("ID","");
    }


}
