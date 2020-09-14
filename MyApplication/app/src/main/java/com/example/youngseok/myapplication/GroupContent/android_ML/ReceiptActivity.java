package com.example.youngseok.myapplication.GroupContent.android_ML;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youngseok.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.document.FirebaseVisionCloudDocumentRecognizerOptions;

import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentTextRecognizer;

import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class ReceiptActivity extends AppCompatActivity {

    ImageView imageView;
    EditText textView;
    Bitmap bitmap;
    Button button;
    Button button2;
    private String currentPhotoPath;
    private Uri photouri;

    private ProgressDialog progressDialog;
    private ArrayList mllist;

    private String filename;

    private int check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_vision);

        Intent intent = getIntent();
        check=intent.getIntExtra("pic",0);

        imageView=findViewById(R.id.imageview);
        textView=findViewById(R.id.textView);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mllist=new ArrayList();



        if(check==1){
            pickImage();
        }else if( check==2){
            takeImage();
        }

        progressDialog = new ProgressDialog(ReceiptActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.setMessage("문자 인식중입니다. . .");




    }

    public void detect(){
        if(bitmap==null){
            Toast.makeText(getApplicationContext(),"nullllll",Toast.LENGTH_LONG).show();
        }
        else{
            Log.e("sinsae","4");
            progressDialog.show();
            FirebaseVisionCloudDocumentRecognizerOptions options =
                    new FirebaseVisionCloudDocumentRecognizerOptions.Builder()
                            .setLanguageHints(Arrays.asList("ko", "hi"))
                            .build();
            Log.e("sinsae","5");
            FirebaseVisionImage firebaseVisionImage= FirebaseVisionImage.fromBitmap(bitmap);
            Log.e("sinsae","6");
            FirebaseVisionDocumentTextRecognizer textDetector = FirebaseVision.getInstance()
                    .getCloudDocumentTextRecognizer(options);

            Log.e("sinsae","7");
            textDetector.processImage(firebaseVisionImage)
                    .addOnSuccessListener(new OnSuccessListener<FirebaseVisionDocumentText>() {
                        @Override
                        public void onSuccess(FirebaseVisionDocumentText result) {
                            // Task completed successfully
                            // ...
                            Log.e("sinsae","1");
                            processImage(result);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Task failed with an exception
                            // ...
                        }
                    });
        }
    }

    private void processImage(FirebaseVisionDocumentText firebaseVisionText){
        List<FirebaseVisionDocumentText.Block> blocks = firebaseVisionText.getBlocks();
        if(blocks.size()==0){
            Toast.makeText(getApplicationContext(),"djqtdj",Toast.LENGTH_SHORT).show();

        }
        else {

            String test = "";
            for(FirebaseVisionDocumentText.Block block : firebaseVisionText.getBlocks()){
                String text = block.getText();
                textView.setText(text);

                Log.e("watchout",text);
                Log.e("Tlqkf;;",text);
                test = test+"\n"+text;
                Log.e("sinsae","2");
            }
            Log.e("sinsae","3");
            textView.setText(test);

            String[] bb = test.split("\\n");
            for(int i=0; i<bb.length;i++){
                String[] aa= bb[i].split("\\s");
                for(int j=0; j<aa.length;j++){
                    mllist.add(aa[j]);
                }
            }
            progressDialog.dismiss();
            Intent intent =new Intent(ReceiptActivity.this,Receip_insert_Activity.class);
            intent.putStringArrayListExtra("array",mllist);
            intent.putExtra("file",filename);
            startActivity(intent);
            finish();
        }
    }



    public void pickImage(){


        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photouri = FileProvider.getUriForFile(this,
                        getPackageName(),
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photouri);
                startActivityForResult(takePictureIntent, 2);
            }
        }

    }

    private void takeImage(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,1);
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private void saveImage (Bitmap bitmap){
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        filename=myDir+"/"+fname;
        Log.e("rmfldnj",fname);
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1 && resultCode==RESULT_OK){
            Uri uri=data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                imageView.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }

            saveImage(bitmap);

            detect();
        }
        else if(requestCode==2 && resultCode==RESULT_OK){
            Log.e("memememe","dddd");


            bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            Log.e("closer",currentPhotoPath);
            ExifInterface exif = null;
            try {
                Log.e("jengaaa",currentPhotoPath);
                exif = new ExifInterface(currentPhotoPath);
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

            imageView.setImageBitmap(rotate(bitmap, exifDegree));


            bitmap = rotate(bitmap,exifDegree);


            saveImage(bitmap);

            detect();

        }
    }
}


