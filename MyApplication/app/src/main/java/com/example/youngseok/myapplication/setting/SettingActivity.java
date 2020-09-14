package com.example.youngseok.myapplication.setting;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.youngseok.myapplication.GroupContent.Shot.Camera_Edit_Activity;
import com.example.youngseok.myapplication.GroupContent.Shot.ShotActivity;
import com.example.youngseok.myapplication.GroupContent.ar.FaceActivity;
import com.example.youngseok.myapplication.Initial.InitialActivity;
import com.example.youngseok.myapplication.MainActivity;
import com.example.youngseok.myapplication.MygroupActivity;
import com.example.youngseok.myapplication.R;
import com.example.youngseok.myapplication.invite.InviteActivity;
import com.example.youngseok.myapplication.make_group.CustomAdapter;
import com.example.youngseok.myapplication.make_group.MakeGroupActivity;
import com.example.youngseok.myapplication.make_group.basicGroup;
import com.karumi.dexter.Dexter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.youngseok.myapplication.GroupContent.Shot.ShotActivity.img_add;
import static com.example.youngseok.myapplication.Initial.InitialActivity.save_my_id;

public class SettingActivity extends AppCompatActivity {
    Toolbar toolbar;

    ImageButton timeline;
    ImageButton mygroup;
    ImageButton makegroup;
    ImageButton invitefriend;
    ImageButton myset;

    private ImageView profile_img;
    private TextView nickname;
    private TextView phone;

    private String mJsonString;

    private ArrayList<SettingDTO> Setting_Array;

    private String getImage;
    private Bitmap getbitimage;
    private int serverResponseCode=0;

    private String upLoadServerUri=null;
    private String filename;

    public static boolean ddokddak = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
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
        upLoadServerUri = "http://192.168.43.34/Setting/insert_image.php";


        timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_main = new Intent(SettingActivity.this,MainActivity.class);
                startActivity(go_main);
                overridePendingTransition(0,0);
                finish();
            }
        });

        mygroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_mygroup = new Intent(SettingActivity.this,MygroupActivity.class);
                startActivity(go_mygroup);
                overridePendingTransition(0,0);
                finish();
            }
        });

        makegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_make = new Intent(SettingActivity.this,MakeGroupActivity.class);
                startActivity(go_make);
                overridePendingTransition(0,0);
                finish();
            }
        });

        myset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_set = new Intent(SettingActivity.this,SettingActivity.class);
                startActivity(go_set);
                ddokddak=false;
                img_add="";
                overridePendingTransition(0,0);
                finish();
            }
        });

        invitefriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_invite = new Intent(SettingActivity.this,InviteActivity.class);
                startActivity(go_invite);
                overridePendingTransition(0,0);
                finish();
            }
        });


        Button logout = findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,InitialActivity.class);

                startActivity(intent);
                SharedPreferences saveLoginData = getSharedPreferences("appData", MODE_PRIVATE);
                SharedPreferences.Editor editor = saveLoginData.edit();
                editor.clear();
                editor.commit();
                finish();
            }
        });



        profile_img = findViewById(R.id.profile_picture);
        nickname=findViewById(R.id.nickname);
        phone=findViewById(R.id.phone);




        Intent intent = getIntent();
        getImage=intent.getStringExtra("uri");



        CircleTransform transForm = new CircleTransform();


        Picasso.with(this)
                .load(R.drawable.kakao_default_profile_image)
                .transform(transForm)
                .into(profile_img);

        if(TextUtils.isEmpty(getImage)){
            Log.e("despacito","Tlqkf");
        }

        else{
            Uri uri = Uri.parse(getImage);
            Bitmap bitmap = BitmapFactory.decodeFile(getPathFromUri(uri));

            ExifInterface exif = null;
            try {

                exif = new ExifInterface(getPathFromUri(uri));
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;
            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }
            Glide.with(SettingActivity.this)
                    .load(rotate(bitmap, exifDegree))
                    .apply(new RequestOptions().circleCrop())
                    .into(profile_img);




            Log.e("yaeyaeyae",getPathFromUri(uri));


            new Thread(new Runnable() {
                @Override
                public void run() {
                    File file = new File("http://192.168.43.34/Setting/profile_image/youngseoka.jpg");
                    if(file.exists()){
                        Log.e("eeeeeee","eee");
                        file.delete();
                    }
                    else{
                        Log.e("eeeeeee","aaa");
                        file.delete();
                    }
                    uploadFile(getPathFromUri(uri),save_my_id+".jpg");
                }
            }).start();







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
            picture_request picture_request = new picture_request(keyword,save_my_id+".jpg",responseListener);
            RequestQueue queue = Volley.newRequestQueue(SettingActivity.this);
            queue.add(picture_request);






        }






        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {






                AlertDialog.Builder builder_sub = new AlertDialog.Builder(SettingActivity.this);


                View view = LayoutInflater.from(SettingActivity.this).inflate(R.layout.edit_picture_dialog,null,false);

                builder_sub.setView(view);

                final AlertDialog alertdialog_sub = builder_sub.create();

                final Button filter_btn=view.findViewById(R.id.filter_btn);
                final Button detective_btn=view.findViewById(R.id.detective_btn);
                final Button gallery_btn=view.findViewById(R.id.gallery_btn);

                filter_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent go_shot = new Intent(SettingActivity.this,ShotActivity.class);
                        startActivity(go_shot);
                        alertdialog_sub.dismiss();
                        finish();
                    }
                });

                detective_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent go_detec = new Intent(SettingActivity.this, FaceActivity.class);
                        startActivity(go_detec);
                        alertdialog_sub.dismiss();
                        finish();
                    }
                });

                gallery_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, 1234);
                        alertdialog_sub.dismiss();

                    }
                });

                alertdialog_sub.show();



            }
        });

        Setting_Array = new ArrayList<>();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("Tlqqqqqq","qqqq");
                GetData task = new GetData();
                task.execute("http://192.168.43.34/Setting/show_info",keyword);
            }
        },10);



    }



    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SettingActivity.this,
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

            if(TextUtils.isEmpty(Setting_Array.get(0).getPicture())){

            }else{
                if(ddokddak==false){
                    CircleTransform transForm = new CircleTransform();


                    Picasso
                            .with(SettingActivity.this)
                            .load("http://192.168.43.34/Setting/profile_image/youngseoka.jpg")
                            .transform(transForm)
                            .rotate(90f)
                            .into(profile_img);
                    ddokddak=true;
                    Log.e("Tlqqq","sdfsdfsdfsdfsdf");

                }
                else{}
            }

            nickname.setText(Setting_Array.get(0).getNickname());
            phone.setText(Setting_Array.get(0).getPhone());


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
        String TAG_id = "id";
        String TAG_nickname = "nickname";
        String TAG_phone ="phone";
        String TAG_picture="picture";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String id = item.getString(TAG_id);
                String nickname = item.getString(TAG_nickname);
                String phone = item.getString(TAG_phone);
                String picture = item.getString(TAG_picture);

                SettingDTO settingDTO = new SettingDTO();

                settingDTO.setId(id);
                settingDTO.setNickname(nickname);
                settingDTO.setPhone(phone);
                settingDTO.setPicture(picture);

                Setting_Array.add(settingDTO);
            }



        } catch (JSONException e) {

        }

    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 270;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 0;
        }
        return 90;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    public String getPathFromUri(Uri uri){

        Cursor cursor = getContentResolver().query(uri, null, null, null, null );

        cursor.moveToNext();

        String path = cursor.getString( cursor.getColumnIndex( "_data" ) );

        cursor.close();



        return path;

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == 1234) {
            Uri uri=data.getData();

            Intent intent = new Intent(SettingActivity.this,Camera_Edit_Activity.class);
            intent.putExtra("file_path",getPathFromUri(uri));
            img_add=getPathFromUri(uri);
            startActivityForResult(intent,101);

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