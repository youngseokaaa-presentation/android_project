package com.example.youngseok.myapplication.GroupContent.ar;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.youngseok.myapplication.GroupContent.Shot.Camera_Edit_Activity;
import com.example.youngseok.myapplication.R;
import com.example.youngseok.myapplication.setting.SettingActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.example.youngseok.myapplication.GroupContent.ar.CameraSourcePreview;
import com.example.youngseok.myapplication.GroupContent.ar.GraphicOverlay;
import com.example.youngseok.myapplication.GroupContent.ar.CameraSourcePreview;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Random;

import static com.example.youngseok.myapplication.GroupContent.Shot.ShotActivity.img_add;

public final class FaceActivity extends AppCompatActivity {

    private static final String TAG = "FaceActivity";

    private static final int RC_HANDLE_GMS = 9001;
    // permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 255;

    private CameraSource mCameraSource = null;
    private CameraSourcePreview mPreview;
    private GraphicOverlay mGraphicOverlay;
    private boolean mIsFrontFacing = true;

    private ImageButton capture_btn;

    private Camera mCamera;

    private CameraSource.PictureCallback pictureCallback;

    private Bitmap result;
    private ImageView imageview_result;


    // Activity event handlers
    // =======================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called.");

        setContentView(R.layout.activity_face);

        imageview_result=findViewById(R.id.imageview_result);
        mPreview = (CameraSourcePreview) findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay) findViewById(R.id.faceOverlay);
        final ImageButton button = (ImageButton) findViewById(R.id.flipButton);
        button.setOnClickListener(mSwitchCameraButtonListener);

        if (savedInstanceState != null) {
            mIsFrontFacing = savedInstanceState.getBoolean("IsFrontFacing");
        }

        // Start using the camera if permission has been granted to this app,
        // otherwise ask for permission to use it.
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource();
        } else {
            requestCameraPermission();
        }
        capture_btn=findViewById(R.id.capture_btn);
        capture_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent =new Intent(FaceActivity.this, SettingActivity.class);
//                startActivity(intent);
//                finish();



                mCameraSource.takePicture(new CameraSource.ShutterCallback() {
                    @Override
                    public void onShutter() {

                    }
                }, new CameraSource.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes) {


                        // byte 배열을 비트맵으로 변환
                        result = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                        // 사진의 회전값을 맞추기 위해 Matrix 변수 선언
                        Matrix rotateMatrix = new Matrix();
                        rotateMatrix.postRotate(-90);

                        // 사진의 좌우반전을 맞추기 위해 Matrix 변수 선언
                        Matrix sideInversionMatrix = new Matrix();
                        sideInversionMatrix.setScale(-1,1);

                        // 사진의 회전값과 좌우반전을 적용하여 새로운 비트맵 생성
                        result = Bitmap.createBitmap(result, 0,0, result.getWidth(), result.getHeight(), rotateMatrix, false);
                        result = Bitmap.createBitmap(result, 0,0, result.getWidth(), result.getHeight(), sideInversionMatrix, false);

                        // 사진을 미리볼 수 있는 imageView 에 결과 비트맵을 삽입
                        imageview_result.setImageBitmap(result);

                        // CameraSourcePreView 의 캡쳐를 통해 현재 화면에 나타난 마스크를 비트맵으로 변환
                        Bitmap mask = Bitmap.createScaledBitmap(mPreview.capture(), result.getWidth(), result.getHeight(), false);

                        // CameraSource 에서 가져온 사진의 bitmap 위에 마스크를 그려낸다.
                        Canvas canvas = new Canvas(result);
                        Rect rect = new Rect(-900,0,mask.getWidth(), mask.getHeight());
                        canvas.drawBitmap(mask,null,rect, null);

                        // 카메라 화면을 숨기고, 결과 이미지뷰의 Visibility 를 Visible 로 처리한다.
//                        mPreview.setVisibility(View.GONE);
//                        imageview_result.setVisibility(View.VISIBLE);

                        SaveImage(result);


                        String root = Environment.getExternalStorageDirectory().toString();
                        File myDir = new File(root + "/saved_images");
                        myDir.mkdirs();
                        Random generator = new Random();
                        int n = 10000;
                        n = generator.nextInt(n);
                        String fname = "Image-"+ n +".jpg";
                        Log.e("rmfldnj",fname);
                        File file = new File (myDir, fname);
                        if (file.exists ()) file.delete ();
                        try {
                            FileOutputStream out = new FileOutputStream(file);
                            result.compress(Bitmap.CompressFormat.JPEG, 90, out);
                            out.flush();
                            out.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Log.e("rmfldnj",fname);
                        Intent intent = new Intent(FaceActivity.this,Camera_Edit_Activity.class);
                        intent.putExtra("file_path",myDir+"/"+fname);
                        img_add=myDir+"/"+fname;

                        startActivityForResult(intent,101);



                    }
                });









            }
        });
    }

    private View.OnClickListener mSwitchCameraButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            mIsFrontFacing = !mIsFrontFacing;

            if (mCameraSource != null) {
                mCameraSource.release();
                mCameraSource = null;
            }

            createCameraSource();
            startCameraSource();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called.");

        startCameraSource();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mPreview.stop();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("IsFrontFacing", mIsFrontFacing);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mCameraSource != null) {
            mCameraSource.release();
        }
    }

    // Handle camera permission requests
    // =================================

    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission not acquired. Requesting permission.");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions, RC_HANDLE_CAMERA_PERM);
            }
        };
        Snackbar.make(mGraphicOverlay, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // We have permission to access the camera, so create the camera source.
            Log.d(TAG, "Camera permission granted - initializing camera source.");
            createCameraSource();
            return;
        }

        // If we've reached this part of the method, it means that the user hasn't granted the app
        // access to the camera. Notify the user and exit.
        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name)
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.disappointed_ok, listener)
                .show();
    }

    // Camera source
    // =============

    private void createCameraSource() {
        Log.d(TAG, "createCameraSource called.");

        // 1
        Context context = getApplicationContext();
        FaceDetector detector = createFaceDetector(context);

        // 2
        int facing = CameraSource.CAMERA_FACING_FRONT;
        if (!mIsFrontFacing) {
            facing = CameraSource.CAMERA_FACING_BACK;
        }

        // 3
        mCameraSource = new CameraSource.Builder(context, detector)
                .setFacing(facing)
                .setRequestedPreviewSize(320, 240)
                .setRequestedFps(60.0f)
                .setAutoFocusEnabled(true)
                .build();
    }

    private void startCameraSource() {
        Log.d(TAG, "startCameraSource called.");

        // Make sure that the device has Google Play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg = GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    // Face detector
    // =============

    /**
     *  Create the face detector, and check if it's ready for use.
     */
    @NonNull
    private FaceDetector createFaceDetector(final Context context) {
        Log.d(TAG, "createFaceDetector called.");

        FaceDetector detector = new FaceDetector.Builder(context)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .setTrackingEnabled(true)
                .setMode(FaceDetector.FAST_MODE)
                .setProminentFaceOnly(mIsFrontFacing)
                .setMinFaceSize(mIsFrontFacing ? 0.35f : 0.15f)
                .build();

        MultiProcessor.Factory<Face> factory = new MultiProcessor.Factory<Face>() {
            @Override
            public Tracker<Face> create(Face face) {
                return new FaceTracker(mGraphicOverlay, context, mIsFrontFacing);
            }
        };

        Detector.Processor<Face> processor = new MultiProcessor.Builder<>(factory).build();
        detector.setProcessor(processor);

        if (!detector.isOperational()) {
            Log.w(TAG, "Face detector dependencies are not yet available.");

            // Check the device's storage.  If there's little available storage, the native
            // face detection library will not be downloaded, and the app won't work,
            // so notify the user.
            IntentFilter lowStorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowStorageFilter) != null;

            if (hasLowStorage) {
                Log.w(TAG, getString(R.string.low_storage_error));
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.app_name)
                        .setMessage(R.string.low_storage_error)
                        .setPositiveButton(R.string.disappointed_ok, listener)
                        .show();
            }
        }
        return detector;
    }



    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //    camera.setDisplayOrientation(90);




            File pictureFile = getOutputMediaFile();

            if(pictureFile==null){
                return;
            }
            MediaScannerConnection.scanFile(FaceActivity.this,
                    new String[]{pictureFile.toString()},null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override

                        public void onScanCompleted(String path, Uri uri){



                            mCamera.startPreview();
                            Intent intent = new Intent(FaceActivity.this, Camera_Edit_Activity.class);
                            intent.putExtra("file_path",path);
                            img_add=path;
                            startActivityForResult(intent,101);


                        }
                    });

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }




        }
    };

    private File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"My images");

        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdirs()){
                return null;
            }
        }
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(100000);
        File save = new File(mediaStorageDir.getAbsolutePath()+File.separator+"IMG"+num+".jpg");

        return save;
    }

    private void SaveImage(Bitmap finalBitmap) {


    }




}