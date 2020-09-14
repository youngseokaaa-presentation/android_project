package com.example.youngseok.myapplication.GroupContent.Schedule;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.youngseok.myapplication.GroupContent.GroupContentDTO;
import com.example.youngseok.myapplication.MainActivity;
import com.example.youngseok.myapplication.MygroupActivity;
import com.example.youngseok.myapplication.R;
import com.example.youngseok.myapplication.calendar.ClearDecorator;
import com.example.youngseok.myapplication.calendar.EventDecorator;
import com.example.youngseok.myapplication.calendar.OneDayDecorator;
import com.example.youngseok.myapplication.calendar.SaturdayDecorator;
import com.example.youngseok.myapplication.calendar.SundayDecorator;
import com.example.youngseok.myapplication.invite.InviteActivity;
import com.example.youngseok.myapplication.make_group.MakeGroupActivity;
import com.example.youngseok.myapplication.setting.SettingActivity;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import static com.example.youngseok.myapplication.GroupContent.Schedule.ScheduleAdapter.schedule_marker_switch;
import static com.example.youngseok.myapplication.Initial.InitialActivity.save_my_id;

public class ScheduleActivity extends AppCompatActivity {

    Toolbar toolbar;

    ImageButton timeline;
    ImageButton mygroup;
    ImageButton makegroup;
    ImageButton invitefriend;
    ImageButton myset;

    MaterialCalendarView calendar;


    private ArrayList<GroupContentDTO> groupcontent_Array;
    private ArrayList<Dialog_master_id_DTO> dialog_master_id;
    private String master_key;
    private FloatingActionButton add_btn;

    private ArrayList<ScheduleDTO> scheduleArraylist;

    private ArrayList<ScheduleDTO> schedule_clear_array;
    private String shot_date ="";

    private String str_year="";
    private String str_month="";
    private String str_day="";
    int hour;
    int minute_int;

    private String mJsonString;

    private RecyclerView recyclerview;
    private ScheduleAdapter adapter;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
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
                Intent go_main = new Intent(ScheduleActivity.this,MainActivity.class);
                startActivity(go_main);
                overridePendingTransition(0,0);
                finish();
            }
        });

        mygroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_mygroup = new Intent(ScheduleActivity.this,MygroupActivity.class);
                startActivity(go_mygroup);
                overridePendingTransition(0,0);
                finish();
            }
        });

        makegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent go_make = new Intent(ScheduleActivity.this,MakeGroupActivity.class);
                startActivity(go_make);
                overridePendingTransition(0,0);
                finish();



            }
        });
        myset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_set = new Intent(ScheduleActivity.this,SettingActivity.class);
                startActivity(go_set);
                overridePendingTransition(0,0);
                finish();
            }
        });

        invitefriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_invite = new Intent(ScheduleActivity.this,InviteActivity.class);
                startActivity(go_invite);
                overridePendingTransition(0,0);
                finish();
            }
        });

        Intent intent = getIntent();
        groupcontent_Array = (ArrayList<GroupContentDTO>) intent.getSerializableExtra("array");
        master_key=intent.getStringExtra("master_key");
        dialog_master_id = new ArrayList<>();
        Dialog_master_id_DTO dialog_master_id_dto = new Dialog_master_id_DTO();
        dialog_master_id_dto.setId(groupcontent_Array.get(0).getGroup_id());
        dialog_master_id.add(dialog_master_id_dto);


        scheduleArraylist = new ArrayList<>();
        schedule_clear_array=new ArrayList<>();

        GetData task = new GetData();
        task.execute("http://192.168.43.34/Schedule/schedule_check.php",master_key);


        add_btn= findViewById(R.id.add_btn);


        if(save_my_id.equals(groupcontent_Array.get(0).getGroup_id())){
            add_btn.setVisibility(View.VISIBLE);
        }
        else
        {
            add_btn.setVisibility(View.GONE);
        }

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ddddisssco",shot_date);

                final AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleActivity.this);

                final Calendar dateandtime = Calendar.getInstance();

                    View view = LayoutInflater.from(ScheduleActivity.this).inflate(R.layout.schedule_dialog,null,false);
                    builder.setView(view);
                    final AlertDialog alertdialog = builder.create();
                    DatePicker datePicker = view.findViewById(R.id.datePicker);
                    TimePicker timePicker = view.findViewById(R.id.timePicker);
                    final EditText edit_schedule_title = view.findViewById(R.id.edit_schedule_title);
                    final EditText edit_schedule_content = view.findViewById(R.id.edit_schedule_content);
                    final Button schedule_confirm_btn = view.findViewById(R.id.schedult_confirm_btn);



                    hour = dateandtime.get(Calendar.HOUR_OF_DAY);
                    minute_int=dateandtime.get(Calendar.MINUTE);
                    if(str_year.equals("")){
                        str_year=String.valueOf(dateandtime.get(Calendar.YEAR));
                        str_month=String.valueOf(dateandtime.get(Calendar.MONTH)+1);
                        str_day=String.valueOf(dateandtime.get(Calendar.DAY_OF_MONTH));

                    }
                    datePicker.init(Integer.parseInt(str_year), Integer.parseInt(str_month)-1, Integer.parseInt(str_day), new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        }
                    });





                    timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                        @Override
                        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                            hour=hourOfDay;
                            minute_int=minute;
                        }
                    });
                    datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            str_year = String.valueOf(year);
                            str_month=String.valueOf(monthOfYear+1);
                            str_day=String.valueOf(dayOfMonth);
                        }
                    });





                    schedule_confirm_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(TextUtils.isEmpty(edit_schedule_title.getText())){
                                Toast.makeText(ScheduleActivity.this,"일정 제목을 입력해주세요",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(TextUtils.isEmpty(edit_schedule_content.getText())){
                                Toast.makeText(ScheduleActivity.this,"일정 내용을 입력해주세요",Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Log.e("qjfTj",master_key);
                            Log.e("qjfTj",groupcontent_Array.get(0).getGroup_name());
                            Log.e("qjfTj",edit_schedule_title.getText().toString());
                            Log.e("qjfTj",edit_schedule_content.getText().toString());
                            Log.e("qjfTj",str_year);
                            Log.e("qjfTj",str_month);
                            Log.e("qjfTj",str_day);
                            Log.e("qjfTj",String.valueOf(hour));
                            Log.e("qjfTj",String.valueOf(minute_int));


                            Response.Listener<String> responseListener = new Response.Listener<String>(){

                                @Override
                                public void onResponse(String response){
                                    try{


                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success =jsonResponse.getBoolean("success");
                                        if(success){
                                            scheduleArraylist = new ArrayList<>();
                                            scheduleArraylist.clear();
                                            GetData task = new GetData();
                                            task.execute("http://192.168.43.34/Schedule/schedule_check.php",master_key);

                                            adapter.notifyDataSetChanged();
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
                            ScheduleRequest scheduleRequest = new ScheduleRequest(master_key,groupcontent_Array.get(0).getGroup_name()
                                    ,str_year,str_month,str_day,edit_schedule_title.getText().toString(),edit_schedule_content.getText().toString(),String.valueOf(hour)
                                    ,String.valueOf(minute_int),responseListener);
                            RequestQueue queue = Volley.newRequestQueue(ScheduleActivity.this);
                            queue.add(scheduleRequest);
                            adapter.notifyDataSetChanged();
                            alertdialog.dismiss();




                        }
                    });

                    //날짜 기본값, 시간 기본값





                alertdialog.show();

            }
        });







        Thread mthread =new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        if(schedule_marker_switch==true){
                            schedule_marker_switch=false;

                            for (int index=0; index<schedule_clear_array.size();index++){
                                Log.e("soso",schedule_clear_array.get(index).getSchedule_content());
                            }
                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   Log.e("soso","abc");

                                   calendar.removeDecorators();
                                   calendar.addDecorators(
                                           new SundayDecorator(),
                                           new SaturdayDecorator()
                                           ,new OneDayDecorator(ScheduleActivity.this)
                                   );

                                   new ApiSimulator(scheduleArraylist).executeOnExecutor(Executors.newSingleThreadExecutor());
                               }
                           });
                        }
                        else
                        {
                            Log.e("soso","bbb");
                        }
                        Thread.sleep(2000);
                    }catch (Throwable t){

                    }
                }
            }
        });
        mthread.start();










        calendar = findViewById(R.id.calendar_schedule);
        calendar.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator()
                ,new OneDayDecorator(ScheduleActivity.this)
        );



        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                int Year = date.getYear();
                int Month = date.getMonth() +1;
                int Day = date.getDay();

                str_year=String.valueOf(Year);
                str_month=String.valueOf(Month);
                str_day=String.valueOf(Day);

                shot_date = Year + "," + Month + "," + Day;

                Toast.makeText(ScheduleActivity.this,""+shot_date,Toast.LENGTH_SHORT).show();
                Log.e("dayday",shot_date);
            }
        });


      //  String[] result = {"2019,04,13","2019,04,18","2019,05,18","2019,06,18","2019,06,22","2019,06,23","2030,11,31"};
     //   String[] result = {};









  //      new ApiSimulator(scheduleArraylist).executeOnExecutor(Executors.newSingleThreadExecutor());



        adapter = new ScheduleAdapter(this,scheduleArraylist,dialog_master_id,schedule_clear_array);

        recyclerview = findViewById(R.id.schedule_recycler);

        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();



        final SwipeRefreshLayout swipe_this = findViewById(R.id.swipe_new);
        swipe_this.setColorSchemeResources( R.color.red, R.color.black,R.color.yellow, R.color.blue);


        swipe_this.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                adapter.notifyDataSetChanged();
                swipe_this.setRefreshing(false);
            }
        });



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                recyclerview.scrollToPosition(adapter.getMove_real());
                Log.e("sdsd",String.valueOf(adapter.getMove_real()));
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
            Log.e("daydayday",String.valueOf(time_sche.size()));
            for(int i = 0 ; i < time_sche.size(); i++){
                CalendarDay day = CalendarDay.from(calendar);
//                String[] time = Time_Result[i].split(",");

                int year = time_sche.get(i).getYear();
                int month =time_sche.get(i).getMonth();
                int dayy = time_sche.get(i).getDay();
                Log.e("daydaydaydayyear",String.valueOf(year));
                Log.e("daydaydaydaymonth",String.valueOf(month));
                Log.e("daydaydaydayday",String.valueOf(dayy));

                dates.add(day);
                calendar.set(year,month-1,dayy);
            }



            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            calendar.addDecorator(new EventDecorator(Color.GREEN, calendarDays,ScheduleActivity.this));
        }
    }


    private class GetData extends AsyncTask<String, Void, String>{
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
            adapter.notifyDataSetChanged();
            new ApiSimulator(scheduleArraylist).executeOnExecutor(Executors.newSingleThreadExecutor());
            adapter = new ScheduleAdapter(ScheduleActivity.this,scheduleArraylist,dialog_master_id,schedule_clear_array);
            recyclerview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
           // scheduleArraylist.sort(Comparator.comparing(ScheduleDTO::getYear).thenComparing(ScheduleDTO::getMonth).thenComparing(ScheduleDTO::getDay));

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
            Collections.sort(scheduleArraylist,cpmasc);


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

                scheduleArraylist.add(scheduleDTO);



            }
            ScheduleDTO scheduleDTO = new ScheduleDTO();


            scheduleDTO.setYear(2050);
            scheduleDTO.setMonth(11);
            scheduleDTO.setDay(30);
            scheduleArraylist.add(scheduleDTO);

            adapter.notifyDataSetChanged();




        } catch (JSONException e) {

        }
        adapter.notifyDataSetChanged();

    }


    @Override
    protected void onStart(){
        super.onStart();
        Log.e("vlslr","start");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("vlslr","des");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.e("vlslr","pas");
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("vlslr","onrestar");
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.e("vlslr","onresum");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.e("vlslr","stop");
    }

}
