package com.example.youngseok.myapplication.GroupContent.android_ML;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.youngseok.myapplication.GroupContent.Financial.Financial_dialog.Financial_dialog_picture_insert_request;
import com.example.youngseok.myapplication.GroupContent.Financial.financialRequest;
import com.example.youngseok.myapplication.R;
import com.example.youngseok.myapplication.make_group.MakeGroupActivity;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Receip_insert_Activity extends AppCompatActivity {

    private ArrayList mllist;

    private ListView itemlist_rec;
    private EditText insert_money;
    private EditText insert_day;
    private EditText insert_time;
    private EditText insert_detail;

    private Button rec_insert_btn;
    private String file_name;

    private String upLoadServerUri=null;
    private int serverResponseCode=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receip_insert);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        insert_money = findViewById(R.id.insert_money);
        insert_day = findViewById(R.id.insert_day);
        insert_time=findViewById(R.id.insert_time);
        insert_detail=findViewById(R.id.insert_detail);
        rec_insert_btn = findViewById(R.id.rec_insert_btn);

        mllist = new ArrayList();

        Intent intent =getIntent();

        file_name=intent.getStringExtra("file");
        mllist = intent.getStringArrayListExtra("array");

        upLoadServerUri = "http://192.168.43.34/group_content/financial/UploadToServer_financial.php";
        for(int index=0; index<mllist.size();index++){
            Log.e("wwwww",mllist.get(index).toString());
        }

        itemlist_rec = findViewById(R.id.itemlist_rec);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mllist);
        itemlist_rec.setAdapter(adapter);

        itemlist_rec.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("wwww",mllist.get(position).toString());

                if(insert_money.isFocused()){
                    insert_money.setText(mllist.get(position).toString());
                }
                if(insert_day.isFocused()){
                    insert_day.setText(mllist.get(position).toString());
                }
                if(insert_time.isFocused()){
                    insert_time.setText(mllist.get(position).toString());
                }
                if(insert_detail.isFocused()){
                    insert_detail.setText(mllist.get(position).toString());
                }
            }
        });


        rec_insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String a = insert_money.getText().toString();
                String[] aa = a.split(",");
                String b = aa[0]+aa[1];

                String c = insert_day.getText().toString();
                String[] cc = c.split("-");
                String ccc = cc[1]+"/"+cc[2];

                String d = insert_time.getText().toString();
                String[] dd = d.split(":");
                String ddd = dd[0]+":"+dd[1];

                String f = ccc+" "+ddd;

                Log.e("asdfqwer",b);
                Log.e("asdfqwer",f);


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
                financialRequest financialrequest = new financialRequest("youngseoka_test_room","231******03016","출금",b,insert_detail.getText().toString(),f,"수기","no","null",responseListener);
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(financialrequest);
                Toast.makeText(Receip_insert_Activity.this,"성공적으로 등록되었습니다",Toast.LENGTH_LONG).show();
 //               finish();





                String imgPath = file_name;
                // SharedPreferences 관리 Class : 해당 내용은 블로그에 있으니 참조



                Response.Listener<String> responseListener1 = new Response.Listener<String>(){

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







                Financial_dialog_picture_insert_request financial_dialog_picture_insert_request = new Financial_dialog_picture_insert_request("youngseoka_test_room","출금",b,insert_detail.getText().toString(),f,"ddssa.jpg",responseListener1);
                RequestQueue queue1 = Volley.newRequestQueue(Receip_insert_Activity.this);
                queue1.add(financial_dialog_picture_insert_request);


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        uploadFile(file_name,"ddssa.jpg");
                    }
                }).start();

                finish();
            }
        });


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
}
