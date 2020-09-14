package com.example.youngseok.myapplication;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.youngseok.myapplication.GroupContent.Schedule.Dialog_master_id_DTO;
import com.example.youngseok.myapplication.GroupContent.Schedule.MainCalendarAdapter;
import com.example.youngseok.myapplication.GroupContent.Schedule.ScheduleAdapter;
import com.example.youngseok.myapplication.GroupContent.Schedule.ScheduleDTO;
import com.example.youngseok.myapplication.GroupContent.chat.ChattingActivity;
import com.example.youngseok.myapplication.Service.Add_member_Service;
import com.example.youngseok.myapplication.calendar.EventDecorator;
//import com.example.youngseok.myapplication.calendar.OneDayDecorator;
import com.example.youngseok.myapplication.calendar.OneDayDecorator;
import com.example.youngseok.myapplication.calendar.SaturdayDecorator;
import com.example.youngseok.myapplication.calendar.SundayDecorator;
import com.example.youngseok.myapplication.invite.InviteActivity;
import com.example.youngseok.myapplication.invite.InviteDTO_confirm;
import com.example.youngseok.myapplication.invite.InviteDTO_confirm_request;
import com.example.youngseok.myapplication.make_group.CustomAdapter;
import com.example.youngseok.myapplication.make_group.MakeGroupActivity;
import com.example.youngseok.myapplication.make_group.basicGroup;
import com.example.youngseok.myapplication.setting.SettingActivity;
import com.google.firebase.messaging.FirebaseMessaging;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
import java.util.concurrent.Executors;


import static com.example.youngseok.myapplication.Initial.InitialActivity.save_my_id;

public class MainActivity extends AppCompatActivity {
     Toolbar toolbar;

     ImageButton timeline;
     ImageButton mygroup;
     ImageButton makegroup;
     ImageButton invitefriend;
     ImageButton myset;

     private ArrayList<basicGroup> mArrayList;
     private ArrayList<ScheduleDTO> scheduleArray;
     private MainCalendarAdapter mAdapter;
     private RecyclerView mRecyclerView;
     private int count = 0;

     private String mJsonString;

     private Intent serviceIntent;
     private ArrayList<InviteDTO_confirm> confirm_Arraylist;

     MaterialCalendarView calendar;
 //    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    private ArrayList<Dialog_master_id_DTO> dialog_master_id;
    private ArrayList<ScheduleDTO> schedule_clear_array;




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


        timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_main = new Intent(MainActivity.this,MainActivity.class);
                startActivity(go_main);
                overridePendingTransition(0,0);
                finish();
            }
        });

        mygroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_mygroup = new Intent(MainActivity.this,MygroupActivity.class);
                startActivity(go_mygroup);
                overridePendingTransition(0,0);
                finish();
            }
        });

        myset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_set = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(go_set);
                overridePendingTransition(0,0);
                finish();
            }
        });

        invitefriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_invite = new Intent(MainActivity.this,InviteActivity.class);
                startActivity(go_invite);
                overridePendingTransition(0,0);
                finish();
            }
        });







        makegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent go_make = new Intent(MainActivity.this,MakeGroupActivity.class);
                startActivity(go_make);
                overridePendingTransition(0,0);
                finish();



            }
        });
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용해주세요.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .setPermissions(Manifest.permission.WRITE_CONTACTS)
                .setPermissions(Manifest.permission.READ_CONTACTS)
                .setPermissions(Manifest.permission.SEND_SMS)
                .setPermissions(Manifest.permission.RECEIVE_BOOT_COMPLETED)
                .setPermissions(Manifest.permission.FOREGROUND_SERVICE)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();









        String go_chat;
        String go_vali;

        Intent intent = getIntent();
        go_chat=intent.getStringExtra("master_key");
        go_vali=intent.getStringExtra("group_name");
        if(TextUtils.isEmpty(go_chat)){

        }
        else{

            Intent chatting = new Intent(MainActivity.this,ChattingActivity.class);
            chatting.putExtra("master_key",go_chat);
            chatting.putExtra("group_name",go_vali);
            startActivity(chatting);
        }


        FirebaseMessaging.getInstance().subscribeToTopic("ALL");

        confirm_Arraylist = new ArrayList<>();








        if (Add_member_Service.serviceIntent==null) {
            serviceIntent = new Intent(this, Add_member_Service.class);
            startService(serviceIntent);
        } else {
            serviceIntent = Add_member_Service.serviceIntent;
        }

        Intent get_pending = getIntent();
        final String pending_master_key;
        String pending_master_id;
        String pending_name;
        pending_master_key = get_pending.getStringExtra("pending_main_key");
        pending_master_id=get_pending.getStringExtra("pending_main_id");
        pending_name=get_pending.getStringExtra("pending_main_name");
        if(TextUtils.isEmpty(pending_master_key)){
            Log.e("20180412-pending","null");

        }
        else{
            Log.e("20180412-pending",pending_master_key);
            GetData_invite_confirm task_confirm = new GetData_invite_confirm();
            task_confirm.execute("http://192.168.43.34/Invite/invite_confirm",pending_master_key);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("그룹에 초대합니다!");
            builder.setMessage(pending_name+"에서 초대하였습니다!");
            builder.setPositiveButton("가입",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


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
                            InviteDTO_confirm_request invite_con_req = new InviteDTO_confirm_request(
                                    confirm_Arraylist.get(0).getId(),
                                    confirm_Arraylist.get(0).getName(),
                                    confirm_Arraylist.get(0).getContent(),
                                    confirm_Arraylist.get(0).getSumnail(),
                                    confirm_Arraylist.get(0).getProfile(),
                                    "1",
                                    save_my_id,pending_master_key,responseListener);

                            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                            queue.add(invite_con_req);



                            Toast.makeText(getApplicationContext(),"가입완료 되었습니다!",Toast.LENGTH_LONG).show();
                        }
                    });




            builder.setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(),"취소되었습니다!",Toast.LENGTH_LONG).show();

                        }
                    });
            builder.show();
        }





        mRecyclerView = findViewById(R.id.main_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mArrayList = new ArrayList<>();
        scheduleArray = new ArrayList<>();


        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setYear(2050);
        scheduleDTO.setMonth(11);
        scheduleDTO.setDay(30);
        scheduleArray.add(scheduleDTO);





        mAdapter = new MainCalendarAdapter(this,scheduleArray,dialog_master_id,schedule_clear_array);
        mRecyclerView.setAdapter(mAdapter);



        String keyword = save_my_id;

//        mArrayList.clear();
//        mAdapter.notifyDataSetChanged();
        GetData task = new GetData();
        task.execute("http://192.168.43.34/basicrecycle/query.php",keyword);






        calendar = findViewById(R.id.calendar);
        calendar.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator()
                ,new OneDayDecorator(MainActivity.this)
                 );
        calendar.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017,0,1))
                .setMaximumDate(CalendarDay.from(2050,11,31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                   .commit();


        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                int Year = date.getYear();
                int Month = date.getMonth() +1;
                int Day = date.getDay();

                String shot_Day = Year + "," + Month + "," + Day;
                calendar.clearSelection();

                Toast.makeText(MainActivity.this,""+shot_Day,Toast.LENGTH_SHORT).show();
                Log.e("dayday",shot_Day);
            }
        });


        new ApiSimulator(scheduleArray).executeOnExecutor(Executors.newSingleThreadExecutor());






        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mRecyclerView.scrollToPosition(mAdapter.getMove_real());
                Log.e("sdsd",String.valueOf(mAdapter.getMove_real()));
            }
        },1500);









    }



    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        ArrayList<ScheduleDTO> time_sche;

        ApiSimulator(ArrayList<ScheduleDTO> time_sche){
            this.time_sche =time_sche;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();
            calendar.clear();

            /*특정날짜 달력에 점표시해주는곳*/
            /*월은 0이 1월 년,일은 그대로*/
            //string 문자열인 Time_Result 을 받아와서 ,를 기준으로짜르고 string을 int 로 변환
            Log.e("wkfgownskdy",String.valueOf(time_sche.size()));
            for(int i = 0 ; i < time_sche.size(); i++){
                CalendarDay day = CalendarDay.from(calendar);
//                String[] time = Time_Result[i].split(",");
                Log.e("wkfgownskdy",String.valueOf(time_sche.get(i).getDay()));

                int year = time_sche.get(i).getYear();
                int month = time_sche.get(i).getMonth();
                int dayy = time_sche.get(i).getDay();


                dates.add(day);
                calendar.set(year,month-1,dayy);

            }



            Log.e("wkfgownskdy","agag");
            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            Log.e("wkfgownskdy","flfl");

            calendar.addDecorators(new EventDecorator(Color.GREEN, calendarDays,MainActivity.this));



        }
    }












    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
        }
    };



    private class GetData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
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

            for(int index=0; index<mArrayList.size();index++){

                GetData_schedule task = new GetData_schedule();
                task.execute("http://192.168.43.34/Schedule/schedule_check.php",mArrayList.get(index).getMaster_key());
            }



        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = "id=" + params[1];


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
        String TAG_name = "name";
        String TAG_content = "content";
        String TAG_sumnail ="sumnail";
        String TAG_profile="profile";
        String TAG_master_key="master_key";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String name = item.getString(TAG_name);
                String content = item.getString(TAG_content);
                String sumnail = item.getString(TAG_sumnail);
                String profile = item.getString(TAG_profile);
                String master_key = item.getString(TAG_master_key);

                basicGroup basicgroup = new basicGroup();

                basicgroup.setGroup_name(name);
                basicgroup.setGroup_content(content);
                basicgroup.setGroup_sumnail(sumnail);
                basicgroup.setGroup_picture(profile);
                basicgroup.setMaster_key(master_key);

                mArrayList.add(basicgroup);
            }



        } catch (JSONException e) {

        }

    }

    private class GetData_invite_confirm extends AsyncTask<String, Void, String>{

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
                showResult_invite_confirm();
            }


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
    private void showResult_invite_confirm(){

        String TAG_JSON="youngseok";
        String TAG_id="id";
        String TAG_name = "name";
        String TAG_content = "content";
        String TAG_sumnail ="sumnail";
        String TAG_profile="profile";



        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String name = item.getString(TAG_name);
                String id = item.getString(TAG_id);
                String content = item.getString(TAG_content);
                String sumnail = item.getString(TAG_sumnail);
                String profile = item.getString(TAG_profile);



                InviteDTO_confirm invitedtoconfirm = new InviteDTO_confirm();

                invitedtoconfirm.setId(id);
                invitedtoconfirm.setName(name);
                invitedtoconfirm.setContent(content);
                invitedtoconfirm.setSumnail(sumnail);
                invitedtoconfirm.setProfile(profile);


                confirm_Arraylist.add(invitedtoconfirm);
            }



        } catch (JSONException e) {

        }

    }


    private class GetData_schedule extends AsyncTask<String, Void, String>{
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
                showResult_schedule();
            }
            mAdapter.notifyDataSetChanged();
            Log.e("wkfgownskdy",String.valueOf(scheduleArray.size()));
            new ApiSimulator(scheduleArray).executeOnExecutor(Executors.newSingleThreadExecutor());
            mAdapter = new MainCalendarAdapter(MainActivity.this,scheduleArray,dialog_master_id,schedule_clear_array);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();









            Comparator<ScheduleDTO> cpmasc = new Comparator<ScheduleDTO>() {
                int ret=0;
                @Override
                public int compare(ScheduleDTO o1, ScheduleDTO o2) {

                    if(o1.getYear()<o2.getYear()){
                        ret = -1;
                    }
                    if(o1.getYear()==o2.getYear()){
                        if(o1.getMonth()==(o2.getMonth())){
                            if (o1.getDay()>(o2.getDay())){
                                ret=1;
                            }else if (o1.getDay()==(o2.getDay())){
                                ret=0;
                            }else if(o1.getDay()<(o2.getDay())){
                                ret =-1;
                            }
                        } else if (o1.getMonth()<(o2.getMonth())){
                            ret =-1;
                        }else if(o1.getMonth()>(o2.getMonth())){
                            ret =1;
                        }
                    }
                    if(o1.getYear()>o2.getYear()){
                        ret =1;
                    }
                    return ret;



                }
            };
            Collections.sort(scheduleArray,cpmasc);







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
    private void showResult_schedule(){

        String TAG_JSON="youngseok";
        String TAG_master_key="master_key";
        String TAG_name = "name";
        String TAG_year="year";
        String TAG_month="month";
        String TAG_day="day";
        String TAG_schedule_content="schedule_content";
        String TAG_schedule_content_detail="schedule_content_detail";
        String TAG_time_hour="time_hour";
        String TAG_time_minute="time_minute";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String master_key = item.getString(TAG_master_key);
                String name = item.getString(TAG_name);
                int year = item.getInt(TAG_year);
                int month = item.getInt(TAG_month);
                int day = item.getInt(TAG_day);
                String schedule_content = item.getString(TAG_schedule_content);
                String schedule_content_detail=item.getString(TAG_schedule_content_detail);
                String time_hour=item.getString(TAG_time_hour);
                String time_minute=item.getString(TAG_time_minute);

                ScheduleDTO scheduleDTO = new ScheduleDTO();


                scheduleDTO.setMaster_key(master_key);
                scheduleDTO.setName(name);
                scheduleDTO.setYear(year);
                scheduleDTO.setMonth(month);
                scheduleDTO.setDay(day);
                scheduleDTO.setSchedule_content(schedule_content);
                scheduleDTO.setSchedule_content_detail(schedule_content_detail);
                scheduleDTO.setTime_hour(time_hour);
                scheduleDTO.setTime_minute(time_minute);


                scheduleArray.add(scheduleDTO);



            }
//            ScheduleDTO scheduleDTO = new ScheduleDTO();
//
//
//            scheduleDTO.setYear("2030");
//            scheduleDTO.setMonth("11");
//            scheduleDTO.setDay("30");
//            scheduleArray.add(scheduleDTO);

            mAdapter.notifyDataSetChanged();




        } catch (JSONException e) {

        }
        mAdapter.notifyDataSetChanged();

    }


    @Override
    protected void onStart(){
        super.onStart();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (serviceIntent!=null) {
            stopService(serviceIntent);
            serviceIntent = null;
        }
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
//        mAdapter.notifyDataSetChanged();
        super.onResume();
    }
    @Override
    protected void onStop(){
        super.onStop();
    }

}
