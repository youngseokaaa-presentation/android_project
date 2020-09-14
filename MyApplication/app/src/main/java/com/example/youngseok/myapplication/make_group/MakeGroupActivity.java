package com.example.youngseok.myapplication.make_group;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.youngseok.myapplication.Initial.join.joinRequest;
import com.example.youngseok.myapplication.Initial.signupActivity;
import com.example.youngseok.myapplication.MainActivity;
import com.example.youngseok.myapplication.MygroupActivity;
import com.example.youngseok.myapplication.R;
import com.example.youngseok.myapplication.invite.InviteActivity;
import com.example.youngseok.myapplication.setting.SettingActivity;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static com.example.youngseok.myapplication.Initial.InitialActivity.instance_save_id;
import static com.example.youngseok.myapplication.Initial.InitialActivity.save_my_id;

public class MakeGroupActivity extends AppCompatActivity {
    Toolbar toolbar;

    ImageButton timeline;
    ImageButton mygroup;
    ImageButton makegroup;
    ImageButton invitefriend;
    ImageButton myset;


    // requestCode
    private static final int PROFILE_PICTURE = 0;

    // file path
    private String filePath;

    ImageView default_image;

    private String File_name;






    int serverResponseCode = 0;

    ProgressDialog dialog = null;



    String upLoadServerUri = null;




    String count;
    //데이터베이스에 저장되는 값인데 그 데이터 개수 체크하는거





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makegroup);
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
                Intent go_main = new Intent(MakeGroupActivity.this,MainActivity.class);
                startActivity(go_main);
                overridePendingTransition(0,0);
                finish();
            }
        });

        mygroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_mygroup = new Intent(MakeGroupActivity.this,MygroupActivity.class);
                startActivity(go_mygroup);
                overridePendingTransition(0,0);
                finish();
            }
        });

        myset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_set = new Intent(MakeGroupActivity.this,SettingActivity.class);
                startActivity(go_set);
                overridePendingTransition(0,0);
                finish();
            }
        });
        invitefriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_invite = new Intent(MakeGroupActivity.this,InviteActivity.class);
                startActivity(go_invite);
                overridePendingTransition(0,0);
                finish();
            }
        });


        final EditText insert_recycle_name = findViewById(R.id.insert_recycle_name);
        final EditText insert_recycle_content = findViewById(R.id.insert_recycle_content);
        insert_recycle_content.setHorizontallyScrolling(false);
        final EditText insert_recycle_sumnail=findViewById(R.id.insert_recycle_sumnail);
        Button confirm = findViewById(R.id.recycler_confirm_btn);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strid = save_my_id;
                String strname = insert_recycle_name.getText().toString();
                String strcontent = insert_recycle_content.getText().toString();
                String strsumnail = insert_recycle_sumnail.getText().toString();


                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);


                            boolean success = jsonResponse.getBoolean("success");

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



                if(File_name==null){
                    File_name="default_moim.png";
                }
                int int_count = Integer.parseInt(count);
                int_count++;
                String to =String.valueOf(int_count);
                String master_key;
                master_key = (strid)+"_"+strname;
                insertRequest insertrequest =new insertRequest(strid,strname,strcontent,strsumnail,File_name,to,strid,master_key,responseListener);
                RequestQueue queue = Volley.newRequestQueue(MakeGroupActivity.this);

                queue.add(insertrequest);





                if(filePath==null)
                {

                }
                else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });

                            uploadFile(filePath);

                        }
                    }).start();
                }









                Intent go_main = new Intent(MakeGroupActivity.this,MygroupActivity.class);
                startActivity(go_main);
                overridePendingTransition(0,0);
                finish();

            }
        });

        default_image = findViewById(R.id.profile_image);
        Glide.with(this).asBitmap().load("http://192.168.43.34/upload_profile/uploads/default_moim.png").override(300,300).into(default_image);








        Button select_image_btn = findViewById(R.id.select_image);

        select_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             selectGallery();
            }
        });








        upLoadServerUri = "http://192.168.43.34/UploadToServer.php";//서버컴퓨터의 ip주소

        new Thread(){
            public void run(){
                Connection.Response response = null;
                try {
                    response = Jsoup.connect("http://192.168.43.34/basicrecycle/count.php")
                            .method(Connection.Method.GET)
                            .execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                org.jsoup.nodes.Document document = null;
                try {
                    document = response.parse();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                count = document.text();
                Log.e("testestest",count);
            }
        }.start();



    }



    public int uploadFile(String sourceFileUri) {



        String fileName = sourceFileUri;



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






            runOnUiThread(new Runnable() {

                public void run() {


                }

            });



            return 0;



        }

        else

        {

            try {



                // open a URL connection to the Servlet

                FileInputStream fileInputStream = new FileInputStream(sourceFile);

                URL url = new URL(upLoadServerUri);



                // Open a HTTP  connection to  the URL

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



                // create a buffer of  maximum size

                bytesAvailable = fileInputStream.available();



                bufferSize = Math.min(bytesAvailable, maxBufferSize);

                buffer = new byte[bufferSize];



                // read file and write it into form...

                bytesRead = fileInputStream.read(buffer, 0, bufferSize);



                while (bytesRead > 0) {



                    dos.write(buffer, 0, bufferSize);

                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);

                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);



                }



                // send multipart form data necesssary after file data...

                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);



                // Responses from the server (code and message)

                serverResponseCode = conn.getResponseCode();

                String serverResponseMessage = conn.getResponseMessage();



                Log.i("uploadFile", "HTTP Response is : "

                        + serverResponseMessage + ": " + serverResponseCode);



                if(serverResponseCode == 200){



                    runOnUiThread(new Runnable() {

                        public void run() {






                        }

                    });

                }



                //close the streams //

                fileInputStream.close();

                dos.flush();

                dos.close();



            } catch (MalformedURLException ex) {



                ex.printStackTrace();



                runOnUiThread(new Runnable() {

                    public void run() {


                    }

                });




            } catch (Exception e) {




                e.printStackTrace();



                runOnUiThread(new Runnable() {

                    public void run() {


                    }

                });

            }

            return serverResponseCode;



        } // End else block

    }












    public static class SharedUtil {
        public static void putString(Context context, String key, String value) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

            SharedPreferences.Editor editor = prefs.edit();

            editor.putString(key, value);
            editor.apply();
        }

        public static String getString(Context context, String key) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            return prefs.getString(key, null);
        }
    }

    public void selectGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI); // 외부 저장소에 있는 이미지 파일들에 대한 모든 정보를 얻을 수 있다.
        intent.setType("image/*"); // image/* 형식의 Type 호출 -> 파일을 열 수 있는 앱들이 나열된다.
        startActivityForResult(intent, PROFILE_PICTURE);
    }

    public void getPic(Uri imgUri) {
        String imgPath = getRealPathFromURI(imgUri);
        // SharedPreferences 관리 Class : 해당 내용은 블로그에 있으니 참조
        SharedUtil.putString(getApplicationContext(), "imgPath", imgPath);
        Glide.with(this).asBitmap().load(imgPath).into(default_image);

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


        Log.d("getRealPathFromURI", "getRealPathFromURI: " + cursor.getString(index));
        filePath=cursor.getString(index);
        File_name = cursor_1.getString(index_1);
        Log.d("getnameTlqkf", "name: " + File_name);

        return cursor.getString(index);
    }

    //onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PROFILE_PICTURE:
                if (data != null) {
                    getPic(data.getData());
                } else {
                    return;
                }
                break;
        }
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
