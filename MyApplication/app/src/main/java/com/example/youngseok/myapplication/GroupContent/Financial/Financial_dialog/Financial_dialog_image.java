package com.example.youngseok.myapplication.GroupContent.Financial.Financial_dialog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.youngseok.myapplication.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Financial_dialog_image extends AppCompatActivity implements GestureDetector.OnGestureListener {


    private ImageView show_image;
    private String image_resource;
    private ArrayList<Financial_dialog_DTO> arraylist;
    private int image_resource_position;
    private ImageButton download_btn;




    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private GestureDetector gestureScanner;


    private File path;  //  디렉토리 경로
    private File outputFile; //파일명까지 경로

    private ProgressDialog progressBar;









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_dialog_image);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        show_image=findViewById(R.id.show_image);
        gestureScanner = new GestureDetector(this);




        Intent intent = getIntent();
        image_resource=intent.getStringExtra("image_resource");
        image_resource_position=intent.getIntExtra("image_resource_position",0);


        arraylist = new ArrayList<>();
        arraylist = (ArrayList<Financial_dialog_DTO>) intent.getSerializableExtra("image_resource_arr");




        Glide.with(this).load(image_resource).into(show_image);

        for(int index=0;index<arraylist.size();index++){

            Log.e("diEnd",arraylist.get(index).getFinancial_dialog_picture());
            Log.e("diEnd",image_resource);
        }

        progressBar=new ProgressDialog(Financial_dialog_image.this);
        progressBar.setMessage("다운로드중");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(true);

        download_btn= findViewById(R.id.download);
        download_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String fileURL = arraylist.get(image_resource_position).getFinancial_dialog_picture();

                path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                outputFile= new File(path, "download.jpg"); //파일명까지 포함함 경로의 File 객체 생성

                if (outputFile.exists()) { //이미 다운로드 되어 있는 경우

                    AlertDialog.Builder builder = new AlertDialog.Builder(Financial_dialog_image.this);
                    builder.setTitle("파일 다운로드");
                    builder.setMessage("이미 SD 카드에 존재합니다. 다시 다운로드 받을까요?");
                    builder.setNegativeButton("아니오",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                }
                            });
                    builder.setPositiveButton("예",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    outputFile.delete(); //파일 삭제

                                    final DownloadFilesTask downloadTask = new DownloadFilesTask(Financial_dialog_image.this);
                                    downloadTask.execute(fileURL);


                                }
                            });
                    builder.show();

                } else { //새로 다운로드 받는 경우
                    final DownloadFilesTask downloadTask = new DownloadFilesTask(Financial_dialog_image.this);
                    downloadTask.execute(fileURL);


                }
            }
        });



    }

    private class DownloadFilesTask extends AsyncTask<String, String, Long> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadFilesTask(Context context) {
            this.context = context;
        }


        //파일 다운로드를 시작하기 전에 프로그레스바를 화면에 보여줍니다.
        @Override
        protected void onPreExecute() { //2
            super.onPreExecute();

            //사용자가 다운로드 중 파워 버튼을 누르더라도 CPU가 잠들지 않도록 해서
            //다시 파워버튼 누르면 그동안 다운로드가 진행되고 있게 됩니다.
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
            mWakeLock.acquire();

            progressBar.show();


        }


        //파일 다운로드를 진행합니다.
        @Override
        protected Long doInBackground(String... string_url) { //3
            int count;
            long FileSize = -1;
            InputStream input = null;
            OutputStream output = null;
            URLConnection connection = null;

            try {
                URL url = new URL(string_url[0]);
                connection = url.openConnection();
                connection.connect();


                //파일 크기를 가져옴
                FileSize = connection.getContentLength();

                //URL 주소로부터 파일다운로드하기 위한 input stream
                input = new BufferedInputStream(url.openStream(), 8192);

                path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                outputFile = new File(path, "download.jpg"); //파일명까지 포함함 경로의 File 객체 생성

                // SD카드에 저장하기 위한 Output stream
                output = new FileOutputStream(outputFile);


                byte data[] = new byte[1024];
                long downloadedSize = 0;
                while ((count = input.read(data)) != -1) {
                    //사용자가 BACK 버튼 누르면 취소가능
                    if (isCancelled()) {
                        input.close();
                        return Long.valueOf(-1);
                    }

                    downloadedSize += count;

                    if (FileSize > 0) {
                        float per = ((float) downloadedSize / FileSize) * 100;
                        String str = "Downloaded " + downloadedSize + "KB / " + FileSize + "KB (" + (int) per + "%)";
                        publishProgress("" + (int) ((downloadedSize * 100) / FileSize), str);

                    }

                    //파일에 데이터를 기록합니다.
                    output.write(data, 0, count);
                }
                // Flush output
                output.flush();

                // Close streams
                output.close();
                input.close();


            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                mWakeLock.release();

            }
            return FileSize;
        }
        @Override
        protected void onProgressUpdate(String... progress) { //4
            super.onProgressUpdate(progress);

            // if we get here, length is known, now set indeterminate to false
            progressBar.setIndeterminate(false);
            progressBar.setMax(100);
            progressBar.setProgress(Integer.parseInt(progress[0]));
            progressBar.setMessage(progress[1]);
        }

        //파일 다운로드 완료 후
        @Override
        protected void onPostExecute(Long size) { //5
            super.onPostExecute(size);

            progressBar.dismiss();

            if ( size > 0) {
                Toast.makeText(getApplicationContext(), "다운로드 완료되었습니다. 파일 크기=" + size.toString(), Toast.LENGTH_LONG).show();

                Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(Uri.fromFile(outputFile));
                sendBroadcast(mediaScanIntent);



            }
            else
                Toast.makeText(getApplicationContext(), "다운로드 에러", Toast.LENGTH_LONG).show();
        }

    }



    public boolean onDown(MotionEvent e) {

        return true;
    }



    @Override
    public boolean onTouchEvent(MotionEvent me) {



        return gestureScanner.onTouchEvent(me);
    }
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;

            // right to left swipe
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                //Toast.makeText(getApplicationContext(), "Left Swipe", Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {



                                if(image_resource_position>=arraylist.size()){
                                    Toast.makeText(getApplicationContext(), "마지막 사진 입니다.", Toast.LENGTH_SHORT).show();
                                    image_resource_position=arraylist.size()-1;
                                    Glide.with(Financial_dialog_image.this).load(arraylist.get(image_resource_position-1).getFinancial_dialog_picture()).into(show_image);

                                }
                                else{
                                    image_resource_position=image_resource_position+1;
                                    if(image_resource_position==arraylist.size()){
                                        Toast.makeText(getApplicationContext(), "마지막 사진 입니다.", Toast.LENGTH_SHORT).show();
                                        image_resource_position=image_resource_position-1;
                                    }

                                    Glide.with(Financial_dialog_image.this).load(arraylist.get(image_resource_position).getFinancial_dialog_picture()).into(show_image);
                                }

                            }
                        });
                    }
                }).start();
            }
            // left to right swipe
            else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
               // Toast.makeText(getApplicationContext(), "Right Swipe", Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                if(arraylist.get(0).getFinancial_dialog_picture().equals("basic_click")){
                                    if(image_resource_position<=1){
                                        Toast.makeText(getApplicationContext(), "첫번째 사진 입니다.", Toast.LENGTH_SHORT).show();
                                        image_resource_position=1;

                                        Glide.with(Financial_dialog_image.this).load(arraylist.get(image_resource_position).getFinancial_dialog_picture()).into(show_image);

                                    }
                                    else{
                                        image_resource_position=image_resource_position-1;
                                        Glide.with(Financial_dialog_image.this).load(arraylist.get(image_resource_position).getFinancial_dialog_picture()).into(show_image);
                                    }
                                }
                                else{
                                    if(image_resource_position<=0){
                                        Toast.makeText(getApplicationContext(), "첫번째 사진 입니다.", Toast.LENGTH_SHORT).show();
                                        image_resource_position=0;

                                        Glide.with(Financial_dialog_image.this).load(arraylist.get(image_resource_position).getFinancial_dialog_picture()).into(show_image);

                                    }
                                    else{
                                        image_resource_position=image_resource_position-1;
                                        Glide.with(Financial_dialog_image.this).load(arraylist.get(image_resource_position).getFinancial_dialog_picture()).into(show_image);
                                    }
                                }

                            }
                        });
                    }
                }).start();
            }
            // down to up swipe
            else if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
               // Toast.makeText(getApplicationContext(), "Swipe up", Toast.LENGTH_SHORT).show();
            }
            // up to down swipe
            else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
               // Toast.makeText(getApplicationContext(), "Swipe down", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

        }
        return true;
    }
    public void onLongPress(MotionEvent e) {

    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        return true;
    }

    public void onShowPress(MotionEvent e) {

    }

    public boolean onSingleTapUp(MotionEvent e) {

        return true;
    }












}
