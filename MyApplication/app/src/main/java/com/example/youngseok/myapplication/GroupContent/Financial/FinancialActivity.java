package com.example.youngseok.myapplication.GroupContent.Financial;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.youngseok.myapplication.GroupContent.Financial.Financial_dialog.Financial_dialog_picture_insert_request;
import com.example.youngseok.myapplication.GroupContent.Financial.Financial_dialog.OnItemClick;
import com.example.youngseok.myapplication.R;
import com.example.youngseok.myapplication.make_group.MakeGroupActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import static com.example.youngseok.myapplication.Initial.InitialActivity.save_my_id;

public class FinancialActivity extends AppCompatActivity implements OnItemClick {



    Toolbar toolbar;

    ImageButton timeline;
    ImageButton mygroup;
    ImageButton makegroup;
    ImageButton invitefriend;
    ImageButton myset;

    private ArrayList<financialDTO> mArrayList;
    private FinancialAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private int count = 0;

    private String mJsonString;

    private String master_key;

    private String master_id;

    private TextView result_tv;

    private FloatingActionButton financial_add_btn;

    private String account_result="";
    private int account_result_int=0;

    private static final int PROFILE_PICTURE = 3503;

    private String upLoadServerUri=null;
    private int serverResponseCode=0;

    private String filePath;
    private String file_name;
    private String filename=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = findViewById(R.id.toolbar);

        SimpleDateFormat format2 = new SimpleDateFormat("yyyy년 MM월 dd일");
        Date time = new Date();
        String time2 = format2.format(time);
        toolbar.setSubtitle(time2);

        timeline = findViewById(R.id.timeline_btn);
        mygroup = findViewById(R.id.new_my);
        makegroup = findViewById(R.id.new_make);
        invitefriend = findViewById(R.id.invite_btn);
        myset = findViewById(R.id.setting_btn);

        String keyword = save_my_id;

        Intent intent_group_content = getIntent();
        master_id=intent_group_content.getStringExtra("master_id");
        master_key=intent_group_content.getStringExtra("master_key");


        boolean isPermissionAllowed = isNotiPermissionAllowed();

        if(!isPermissionAllowed) {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
        }
        mArrayList=new ArrayList<>();

        result_tv = findViewById(R.id.result_tv);


        mAdapter = new FinancialAdapter(FinancialActivity.this,mArrayList,this);
        mRecyclerView = findViewById(R.id.financial_recycler);
        mRecyclerView.setAdapter(mAdapter);



        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.notifyDataSetChanged();

        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(),1));


        GetData task = new GetData();
        task.execute("http://192.168.43.34/group_content/financial/financial_show.php",master_key);



        financial_add_btn=findViewById(R.id.financial_add_btn);

        financial_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder_sub = new AlertDialog.Builder(FinancialActivity.this);


                View view = LayoutInflater.from(FinancialActivity.this).inflate(R.layout.financial_dialog,null,false);

                builder_sub.setView(view);

                final AlertDialog alertdialog_sub = builder_sub.create();

                final RadioButton radio_insert=view.findViewById(R.id.radio_insert);
                final RadioButton radio_out=view.findViewById(R.id.radio_out);
                final EditText financial_dialog_money=view.findViewById(R.id.financial_dialog_money);
                final EditText financial_dialog_detail=view.findViewById(R.id.financial_dialog_detail);
                final Button financial_dialog_btn=view.findViewById(R.id.financial_dialog_btn);

                final DatePicker datepicker=view.findViewById(R.id.datepicker_financial);
                final TimePicker timepicker=view.findViewById(R.id.timepicker_financial);

                financial_dialog_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String radio=null;
                        if(radio_insert.isChecked()){
                            Log.e("20192019","ddd");
                            radio="입금";

                        }
                        else if(radio_out.isChecked()){
                            Log.e("20192019","aaa");
                            radio="출금";
                        }
                        else{
                            Log.e("20192019","bbb");
                            Toast.makeText(getApplicationContext(),"수입, 지출을 선택해주세요",Toast.LENGTH_LONG).show();
                            return;
                        }

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



                        int month = datepicker.getMonth()+1;
                        int day=datepicker.getDayOfMonth();
                        int hour=timepicker.getHour();
                        int minute=timepicker.getMinute();
                        Log.e("cimcim",String.valueOf(datepicker.getYear()));
                        Log.e("cimcim",String.valueOf(month));


                        String month_mix;
                        String day_mix;
                        String hour_mix;
                        String minute_mix;

                        if(month<10){
                            month_mix = "0"+month;
                        }else{
                            month_mix=String.valueOf(month);
                        }

                        if(day<10){
                            day_mix="0"+day;
                        }
                        else{
                            day_mix=String.valueOf(day);
                        }

                        if(hour<10){
                            hour_mix="0"+hour;
                        }
                        else{
                            hour_mix=String.valueOf(hour);
                        }

                        if(minute<10){
                            minute_mix="0"+minute;
                        }
                        else{
                            minute_mix=String.valueOf(minute);
                        }


                        String make_time = month_mix+"/"+day_mix+" "+hour_mix+":"+minute_mix;

                        Log.e("cimcim",make_time);


                        //volley 라이브러리 이용해서 실제 서버와 통신
                        financialRequest financialrequest = new financialRequest("youngseoka_test_room","231******03016",radio,financial_dialog_money.getText().toString(),financial_dialog_detail.getText().toString(),make_time,"수기","no","null",responseListener);
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        queue.add(financialrequest);
                        alertdialog_sub.dismiss();

                        financialDTO ddto = new financialDTO("youngseoka_test_room","231******03016",radio,financial_dialog_money.getText().toString(),financial_dialog_detail.getText().toString(),make_time,"수기","no","null");

                        //mArrayList.add(ddto);

                        mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);


                        if(radio.equals("입금")){
                            account_result_int = account_result_int+Integer.valueOf(financial_dialog_money.getText().toString());
                            account_result=String.valueOf(account_result_int);
                           result_tv.setText(account_result+" 원");
                        }
                        else if(radio.equals("출금")){
                            account_result_int = account_result_int-Integer.valueOf(financial_dialog_money.getText().toString());
                            account_result=String.valueOf(account_result_int);
                            result_tv.setText(account_result+" 원");
                        }



                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                mArrayList.clear();
                                GetData task = new GetData();
                                task.execute("http://192.168.43.34/group_content/financial/financial_show.php",master_key);
                            }
                        }, 500);


                        mAdapter.notifyDataSetChanged();

                    }
                });
                alertdialog_sub.show();



            }
        });


        upLoadServerUri = "http://192.168.43.34/group_content/financial/UploadToServer_financial.php";





    }
    @Override
    public void onClick (String value){
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                mArrayList.clear();
                GetData task = new GetData();
                task.execute("http://192.168.43.34/group_content/financial/financial_show.php",master_key);
            }
        }, 500);
    }

    private boolean isNotiPermissionAllowed() {
        Set<String> notiListenerSet = NotificationManagerCompat.getEnabledListenerPackages(this);
        String myPackageName = getPackageName();

        for(String packageName : notiListenerSet) {
            if(packageName == null) {
                continue;
            }
            if(packageName.equals(myPackageName)) {
                return true;
            }
        }

        return false;
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
            Log.e("TTTT",String.valueOf(mArrayList.size()));


            account_result_int=0;

            for(int index=0; index<mArrayList.size();index++){
                if(mArrayList.get(index).getMoney_type().equals("입금")){
                    account_result_int+=Integer.valueOf(mArrayList.get(index).getMoney());
                }
            }

            for(int index=0;index<mArrayList.size();index++){
                if(mArrayList.get(index).getMoney_type().equals("출금")){
                    account_result_int-=Integer.valueOf(mArrayList.get(index).getMoney());
                }
           }

            account_result = String.valueOf(account_result_int);

            result_tv.setText(account_result+" 원");


            Log.e("ddkkddkk",mArrayList.get(mArrayList.size()-1).getResult());

            mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
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
        String TAG_master_key = "master_key";
        String TAG_account = "account";
        String TAG_money_type ="money_type";
        String TAG_money="money";
        String TAG_money_explain="money_explain";
        String TAG_account_time="account_time";
        String TAG_bank_or_hand="bank_or_hand";
        String TAG_content_edit="content_edit";
        String TAG_result="result";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);



                String master_key = item.getString(TAG_master_key);
                String account = item.getString(TAG_account);
                String money_type = item.getString(TAG_money_type);
                String money = item.getString(TAG_money);
                String money_explain=item.getString(TAG_money_explain);
                String account_time=item.getString(TAG_account_time);
                String bank_or_hand=item.getString(TAG_bank_or_hand);
                String content_edit=item.getString(TAG_content_edit);
                String result=item.getString(TAG_result);

                financialDTO financialdto =new financialDTO(master_key,account,money_type,money,money_explain,account_time,bank_or_hand,content_edit,result);

                financialdto.setMaster_key(master_key);
                financialdto.setAccount(account);
                financialdto.setMoney_type(money_type);
                financialdto.setMoney(money);
                financialdto.setMoney_explain(money_explain);
                financialdto.setAccount_time(account_time);
                financialdto.setBank_or_hand(bank_or_hand);
                financialdto.setContent_edit(content_edit);
                financialdto.setResult(result);



                mArrayList.add(financialdto);
                mAdapter.notifyDataSetChanged();

            }
            Log.e("TTTT",String.valueOf(mArrayList.size()));



        } catch (JSONException e) {

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PROFILE_PICTURE:
                if (data != null) {
                    getPic(data.getData());






                    Log.e("donmills","dsfdf");
                } else {
                    Log.e("donmills","TTTTTTT");
                    return;
                }
                break;
            case 7500:
                Log.e("7500","dsfsfe");
                break;
        }
    }
    public void getPic(Uri imgUri) {
        String imgPath = getRealPathFromURI(imgUri);
        // SharedPreferences 관리 Class : 해당 내용은 블로그에 있으니 참조
        MakeGroupActivity.SharedUtil.putString(getApplicationContext(), "imgPath", imgPath);
        mAdapter.change(imgPath);

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

        SimpleDateFormat format3 = new SimpleDateFormat("yyyyMMddHHmmss");
        Date time2 = new Date();
        String time3 = format3.format(time2);

        Log.e("fjqlddb",mAdapter.getMoney_type());
        Log.e("fjqlddb",mAdapter.getMoney());
        Log.e("fjqlddb",mAdapter.getMoney_explain());
        Log.e("fjqlddb",mAdapter.getAccount_time());
        Log.e("fjqlddb",filePath);
        Log.e("fjqlddb",file_name);

        filename=time3+file_name;

        Financial_dialog_picture_insert_request financial_dialog_picture_insert_request = new Financial_dialog_picture_insert_request(master_key,mAdapter.getMoney_type(),mAdapter.getMoney(),mAdapter.getMoney_explain(),mAdapter.getAccount_time(),filename,responseListener);
        RequestQueue queue = Volley.newRequestQueue(FinancialActivity.this);
        queue.add(financial_dialog_picture_insert_request);


        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("yaeyae",filePath+"TTTT"+filename);
                uploadFile(filePath,filename);
            }
        }).start();

    }

    public String getRealPathFromURI(Uri uri) {
        int index = 0;
        int index_1=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        String[] getna = {MediaStore.Files.FileColumns.DISPLAY_NAME};

        // 이미지 경로로 해당 이미지에 대한 정보를 가지고 있는 cursor 호출
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        Cursor cursor_1 = getContentResolver().query(uri, getna, null, null, null);

        // 데이터가 있으면(가장 처음에 위치한 레코드를 가리킴)
        if (cursor.moveToFirst()) {
            // 해당 필드의 인덱스를 반환하고, 존재하지 않을 경우 예외를 발생시킨다.
            index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        if (cursor_1.moveToFirst()) {
            // 해당 필드의 인덱스를 반환하고, 존재하지 않을 경우 예외를 발생시킨다.
            index_1 = cursor_1.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME);
        }

        Log.e("donmills",cursor.getString(index));

        filePath = cursor.getString(index);
        file_name = cursor_1.getString(index_1);


        return cursor.getString(index);
    }


    public int uploadFile(String sourceFileUri,String name) {

        String fileName = name;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 5120 * 5120;
        File sourceFile = new File(sourceFileUri);
        if (!sourceFile.isFile()) {
            return 0;
        }
        else
        {
            try {
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                serverResponseCode = conn.getResponseCode();
                if(serverResponseCode == 200){
                }
                fileInputStream.close();
                dos.flush();
                dos.close();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }





            return serverResponseCode;





        } // End else block




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
        Log.e("DDDD","dd");
    }
    @Override
    protected void onStop(){
        super.onStop();
    }
}
