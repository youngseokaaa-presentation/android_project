package com.example.youngseok.myapplication.GroupContent.Location;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import com.example.youngseok.myapplication.MainActivity;
import com.example.youngseok.myapplication.MygroupActivity;
import com.example.youngseok.myapplication.R;
import com.example.youngseok.myapplication.invite.InviteActivity;
import com.example.youngseok.myapplication.make_group.MakeGroupActivity;
import com.example.youngseok.myapplication.setting.SettingActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import static com.example.youngseok.myapplication.Initial.InitialActivity.save_my_id;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {



    Toolbar toolbar;

    ImageButton timeline;
    ImageButton mygroup;
    ImageButton makegroup;
    ImageButton invitefriend;
    ImageButton myset;


    private GoogleMap mGoogleMap=null;

    private Marker currentMarker=null;

    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초

    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;

    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};


    Location mCurrentLocatiion;
    LatLng currentPosition;


    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;

    private View mLayout;
    private boolean Tlqkf = false;  //map 로딩기다리는 참거짓
    private boolean once_excute = false;

    private Geocoder find_geo;
    private static final int PLACE_PICKER_REQUEST =1;



    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton floatingActionButton,fab_sharing_my_location, fab_show_list, fab_add_location;
    private ImageView center_pin;
    private Button marker_insert_btn;

    private FloatingActionButton delete_fb,show_more_fb;
    private Boolean isFabOpen_2=false;
    private Animation fab_close_two,fab_open_two;

    private FloatingActionButton fab_sharing_my_location_on;
    private Animation fab_open_on,fab_close_on;
    private Boolean location_on =false;


    private String mJsonString;

    private ArrayList<my_infoDTO> my_info_array;
    private String master_key;

    private ArrayList<sharing_locationDTO> sharing_location_array;
    private ArrayList<markerDTO> marker_array;

    TextView tv_marker;
    View marker_root_view;

    private ClusterManager<markerDTO> mClusterManager;

    private Boolean dododo=false;
    private Boolean dididi=false;


    private ArrayList<ListDTO> fab_list_array;
    private RecyclerView fab_list_recycler;
    private ListAdapter fab_list_Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm","1");
        super.onCreate(savedInstanceState);
        Log.e("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm","2");

        Log.e("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm","3");
        setContentView(R.layout.activity_location);
        Log.e("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm","4");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Log.e("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm","5");
        toolbar = findViewById(R.id.toolbar);
        Log.e("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm","6");
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
                Intent go_main = new Intent(LocationActivity.this,MainActivity.class);
                startActivity(go_main);
                overridePendingTransition(0,0);
                finish();
            }
        });

        mygroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_mygroup = new Intent(LocationActivity.this,MygroupActivity.class);
                startActivity(go_mygroup);
                overridePendingTransition(0,0);
                finish();
            }
        });

        makegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent go_make = new Intent(LocationActivity.this,MakeGroupActivity.class);
                startActivity(go_make);
                overridePendingTransition(0,0);
                finish();



            }
        });
        myset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_set = new Intent(LocationActivity.this,SettingActivity.class);
                startActivity(go_set);
                overridePendingTransition(0,0);
                finish();
            }
        });

        invitefriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_invite = new Intent(LocationActivity.this,InviteActivity.class);
                startActivity(go_invite);
                overridePendingTransition(0,0);
                finish();
            }
        });




        mLayout = findViewById(R.id.map_linear);


        Log.d(TAG, "onCreate");


        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);


        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);

        Log.e("googlemap_skdhkfk","2");


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);



//        while(Thread.currentThread().isInterrupted()){
//            try {
//                Thread.sleep(100);
//
//                Log.e("Tlqkfcheck","4xcve");
//
//            }catch (InterruptedException e){
//
//                Log.e("Tlqkfcheck","4wefsd");
//                Thread.currentThread().interrupt();
//
//            }
//        }



        if (!Places.isInitialized()) {
            Places.initialize(LocationActivity.this, "AIzaSyABUPdpYqxBpjihAju1AZBtkqzl6o9rWh4");
        }
        AutocompleteFragment autocompleteFragment = (AutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId()+" ,"+place.getAddress()+","
                + place.getLatLng().latitude+","+place.getLatLng().longitude);

                LatLng cucu = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(cucu);
                mGoogleMap.moveCamera(cameraUpdate);
                //ㅋㅋㅋㅋ 이동


            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });





        fab_open= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        fab_close=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        fab_sharing_my_location=findViewById(R.id.fab_sharing_my_location);
        fab_show_list=findViewById(R.id.fab_show_list);
        fab_add_location=findViewById(R.id.fab_add_location);

        center_pin=findViewById(R.id.center_pin);
        marker_insert_btn=findViewById(R.id.marker_insert_btn);
        center_pin.bringToFront();

        delete_fb=findViewById(R.id.delete_fb);
        show_more_fb=findViewById(R.id.show_more_fb);
        fab_open_two= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open_two);
        fab_close_two=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close_two);

        fab_sharing_my_location_on=findViewById(R.id.fab_sharing_my_location_on);
        fab_open_on= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open_on);
        fab_close_on=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close_on);





        marker_insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert_marker();
            }
        });



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim();

            }
        });

        fab_add_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim();
                open_add();
            }
        });
        fab_sharing_my_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim();
                sharing_my_location();
            }
        });
        fab_show_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim();
            }
        });
        fab_sharing_my_location_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
                builder.setMessage("내 위치를 공유를 종료합니다");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(),"하위^^&.",Toast.LENGTH_LONG).show();

                                fab_sharing_my_location_on.startAnimation(fab_close_on);
                                fab_sharing_my_location_on.setClickable(false);
                                location_on=false;

                            }
                        });
                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(),"바위^^&.",Toast.LENGTH_LONG).show();
                            }
                        });
                builder.show();
            }
        });




        my_info_array =new ArrayList<>();
        sharing_location_array = new ArrayList<>();

        GetData task = new GetData();
        task.execute("http://192.168.43.34/group_content/geo/take_my_info.php",save_my_id);
        Intent intent = getIntent();
        master_key=intent.getStringExtra("master_key");



        marker_root_view = LayoutInflater.from(this).inflate(R.layout.marker_custom, null);
        tv_marker = (TextView) marker_root_view.findViewById(R.id.tv_marker);









        fab_show_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);


                View view = LayoutInflater.from(LocationActivity.this).inflate(R.layout.fab_list,null,false);

                builder.setView(view);

                final AlertDialog alertdialog = builder.create();



                fab_list_array = new ArrayList<>();

                fab_list_recycler = view.findViewById(R.id.fab_recyclerview);

                fab_list_Adapter = new ListAdapter(LocationActivity.this,fab_list_array);


                fab_list_recycler.setAdapter(fab_list_Adapter);
                fab_list_recycler.setLayoutManager(new LinearLayoutManager(LocationActivity.this));
                fab_list_Adapter.notifyDataSetChanged();

                alertdialog.show();

                GetData_list task_list = new GetData_list();
                task_list.execute("http://192.168.43.34/group_content/geo/show_list.php",master_key);


                fab_list_Adapter.setItemClick(new ListAdapter.ItemClick() {
                    @Override
                    public void onClick(View view, int position) {
                        Log.e("WKWKWK",String.valueOf(fab_list_array.get(position).getTitle()));

                        Double lat = Double.valueOf(fab_list_array.get(position).getLat());
                        Double lng = Double.valueOf(fab_list_array.get(position).getLng());
                        LatLng laln = new LatLng(lat,lng);

                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(laln, 15);
                        mGoogleMap.moveCamera(cameraUpdate);

                        alertdialog.dismiss();

                    }
                });




            }
        });




    }


    public void sharing_my_location(){
        AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);

        if(location_on==false){
            builder.setMessage("내 위치를 공유합니다"+"\n"+"10초마다 위치정보가 업데이트 됩니다.");
            builder.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(),"하위^^&.",Toast.LENGTH_LONG).show();

                            fab_sharing_my_location_on.startAnimation(fab_open_on);
                            fab_sharing_my_location_on.setClickable(true);
                            location_on=true;

                        }
                    });
            builder.setNegativeButton("아니오",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(),"바위^^&.",Toast.LENGTH_LONG).show();
                        }
                    });
            builder.show();
        }
        else if (location_on==true){
            builder.setMessage("내 위치를 공유를 종료합니다");
            builder.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(),"하위^^&.",Toast.LENGTH_LONG).show();

                            fab_sharing_my_location_on.startAnimation(fab_close_on);
                            fab_sharing_my_location_on.setClickable(false);
                            location_on=false;

                        }
                    });
            builder.setNegativeButton("아니오",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(),"바위^^&.",Toast.LENGTH_LONG).show();
                        }
                    });
            builder.show();
        }



    }


    public void anim(){

        if (isFabOpen) {
            fab_show_list.startAnimation(fab_close);
            fab_add_location.startAnimation(fab_close);
            fab_sharing_my_location.startAnimation(fab_close);
            fab_show_list.setClickable(false);
            fab_add_location.setClickable(false);
            fab_sharing_my_location.setClickable(false);
            isFabOpen = false;
        } else {
            fab_show_list.startAnimation(fab_open);
            fab_add_location.startAnimation(fab_open);
            fab_sharing_my_location.startAnimation(fab_open);
            fab_show_list.setClickable(true);
            fab_add_location.setClickable(true);
            fab_sharing_my_location.setClickable(true);
            isFabOpen = true;
        }
    }

    public void open_add(){
        if(center_pin.getVisibility()==View.VISIBLE){
            center_pin.setVisibility(View.GONE);
            marker_insert_btn.setVisibility(View.GONE);
        }
        else if(center_pin.getVisibility()==View.GONE){
            center_pin.setVisibility(View.VISIBLE);
            marker_insert_btn.setVisibility(View.VISIBLE);
        }

    }

    public void insert_marker(){





         AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);


        View view = LayoutInflater.from(LocationActivity.this).inflate(R.layout.insert_marker_dialog,null,false);

        builder.setView(view);

       final AlertDialog alertdialog = builder.create();
        alertdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));




        final EditText insert_title =view.findViewById(R.id.insert_title);
        final EditText insert_content=view.findViewById(R.id.insert_content);
        Button insert_btn=view.findViewById(R.id.insert_btn);

        insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng currentLatLng_sub = mGoogleMap.getCameraPosition().target;

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(currentLatLng_sub);
                markerOptions.title(insert_title.getText().toString());
                markerOptions.snippet(insert_content.getText().toString());

                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.map_marker);
                Bitmap b=bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 75, 120, false);

                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                markerOptions.draggable(true);




                mGoogleMap.addMarker(markerOptions);

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

                marker_insert markerinsert = new marker_insert(master_key,insert_title.getText().toString(),insert_content.getText().toString(),String.valueOf(currentLatLng_sub.latitude),String.valueOf(currentLatLng_sub.longitude),responseListener);
                RequestQueue queue = Volley.newRequestQueue(LocationActivity.this);
                queue.add(markerinsert);

                open_add();

                alertdialog.dismiss();
            }
        });

        alertdialog.show();




    }

    @Override
    public boolean onMarkerClick(final Marker marker){
        Log.e("nonono5","5dskflwe");
        CameraUpdate move_click_position = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15);
        mGoogleMap.animateCamera(move_click_position,1000,null);


        if (isFabOpen_2==false) {
            delete_fb.startAnimation(fab_open_two);
            show_more_fb.startAnimation(fab_open_two);
            delete_fb.setClickable(true);
            show_more_fb.setClickable(true);
            isFabOpen_2 = true;
        } else if (isFabOpen_2==true) {

            delete_fb.startAnimation(fab_close_two);
            show_more_fb.startAnimation(fab_close_two);
            delete_fb.setClickable(false);
            show_more_fb.setClickable(false);
            isFabOpen_2 = false;

            delete_fb.startAnimation(fab_open_two);
            show_more_fb.startAnimation(fab_open_two);
            delete_fb.setClickable(true);
            show_more_fb.setClickable(true);
            isFabOpen_2 = true;
        }


        delete_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                delete_fb.startAnimation(fab_close_two);
                show_more_fb.startAnimation(fab_close_two);
                delete_fb.setClickable(false);
                show_more_fb.setClickable(false);
                isFabOpen_2 = false;


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

                marker_delete_request markerdelete = new marker_delete_request(master_key,marker.getTitle(),marker.getSnippet(),String.valueOf(marker.getPosition().latitude),String.valueOf(marker.getPosition().longitude),responseListener);
                RequestQueue queue = Volley.newRequestQueue(LocationActivity.this);
                queue.add(markerdelete);


                marker.remove();


            }
        });

        show_more_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_fb.startAnimation(fab_close_two);
                show_more_fb.startAnimation(fab_close_two);
                delete_fb.setClickable(false);
                show_more_fb.setClickable(false);
                isFabOpen_2 = false;


                AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);


                View view = LayoutInflater.from(LocationActivity.this).inflate(R.layout.show_detail_dialog,null,false);

                builder.setView(view);

                final AlertDialog alertdialog = builder.create();
                alertdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));




                final TextView insert_title =view.findViewById(R.id.insert_title);
                final TextView insert_content=view.findViewById(R.id.insert_content);
                Button insert_btn=view.findViewById(R.id.insert_btn);
                Button modified_btn=view.findViewById(R.id.modified_btn);

                insert_title.setText(marker.getTitle());
                insert_content.setText(marker.getSnippet());

                insert_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertdialog.dismiss();
                    }
                });


                modified_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertdialog.dismiss();
                        AlertDialog.Builder builder_sub = new AlertDialog.Builder(LocationActivity.this);


                        View view = LayoutInflater.from(LocationActivity.this).inflate(R.layout.insert_marker_dialog,null,false);

                        builder_sub.setView(view);

                        final AlertDialog alertdialog_sub = builder_sub.create();
                        alertdialog_sub.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));




                        final EditText insert_title =view.findViewById(R.id.insert_title);
                        final EditText insert_content=view.findViewById(R.id.insert_content);
                        Button insert_btn=view.findViewById(R.id.insert_btn);

                        insert_title.setText(marker.getTitle());
                        insert_content.setText(marker.getSnippet());
                        insert_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
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

                                marker_modified_request modified = new marker_modified_request(master_key,insert_title.getText().toString(),insert_content.getText().toString(),String.valueOf(marker.getPosition().latitude),String.valueOf(marker.getPosition().longitude),responseListener);
                                RequestQueue queue = Volley.newRequestQueue(LocationActivity.this);
                                queue.add(modified);

                                marker.setTitle(insert_title.getText().toString());
                                marker.setSnippet(insert_content.getText().toString());
                                alertdialog_sub.dismiss();
                            }
                        });
                        alertdialog_sub.show();

                    }
                });


                alertdialog.show();

                //textsize랑 다 바꿔야댕댕댕댕
            }
        });










        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d(TAG, "onMapReady :");


        Tlqkf=true;

        mGoogleMap = googleMap;
        find_geo=new Geocoder(this);

        mGoogleMap.setOnMarkerClickListener(this);


        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
        //지도의 초기위치를 서울로 이동
        setDefaultLocation();



        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(LocationActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(LocationActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);



        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            startLocationUpdates(); // 3. 위치 업데이트 시작


        }else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(LocationActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                        ActivityCompat.requestPermissions( LocationActivity.this, REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions( LocationActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

        mClusterManager = new ClusterManager<markerDTO>(LocationActivity.this, mGoogleMap);
        mGoogleMap.setOnCameraIdleListener(mClusterManager);

        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(isFabOpen_2==true){
                    delete_fb.startAnimation(fab_close_two);
                    show_more_fb.startAnimation(fab_close_two);
                    delete_fb.setClickable(false);
                    show_more_fb.setClickable(false);
                    isFabOpen_2 = false;
                }

            }
        });



        mGoogleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                if(isFabOpen_2==true){
                    delete_fb.startAnimation(fab_close_two);
                    show_more_fb.startAnimation(fab_close_two);
                    delete_fb.setClickable(false);
                    show_more_fb.setClickable(false);
                    isFabOpen_2 = false;
                }
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mGoogleMap.clear();
                                sharing_location_array = new ArrayList<>();
                                GetData_sharing_location task = new GetData_sharing_location();
                                task.execute("http://192.168.43.34/group_content/geo/show_sharing_location.php",master_key);
                                marker_array=new ArrayList<>();
                                GetData_marker task_marker = new GetData_marker();
                                task_marker.execute("http://192.168.43.34/group_content/geo/show_marker.php",master_key);
                                Log.e("wjsuranjajrwl","ddd");




                            }
                        });Thread.sleep(10000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();




        mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Log.e("nonono","1");


                Log.e("thdso",marker.getTitle());


            }

            @Override
            public void onMarkerDrag(Marker marker) {
                Log.e("nonono","2");

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Log.e("nonono","3");

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


                drag_end_request dragendrequest = new drag_end_request(master_key,marker.getTitle(),marker.getSnippet(),String.valueOf(marker.getPosition().latitude),String.valueOf(marker.getPosition().longitude),responseListener);
                RequestQueue queue = Volley.newRequestQueue(LocationActivity.this);
                queue.add(dragendrequest);





            }
        });













        dododo=true;







    }



    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.e("googlemap_skdhkfk","5");
            List<Location> locationList = locationResult.getLocations();

            if (locationList.size() > 0) {
                location = locationList.get(locationList.size() - 1);
                //location = locationList.get(0);

                currentPosition
                        = new LatLng(location.getLatitude(), location.getLongitude());


//                String markerTitle = getCurrentAddress(currentPosition);
//                String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
//                        + " 경도:" + String.valueOf(location.getLongitude());


                String markerTitle = "내 위치";
                String markerSnippet=getCurrentAddress(currentPosition);

                Log.d(TAG, "onLocationResult : " + markerSnippet);


                //현재 위치에 마커 생성하고 이동

                    setCurrentLocation(location, markerTitle, markerSnippet);





                mCurrentLocatiion = location;
                Log.e("googlemap_skdhkfk","6");
                Log.e("bdhsmsthfl",String.valueOf(mCurrentLocatiion.getLatitude()));

                if(dododo==true){
                    if(dididi==true){
                    }
                    else{


                        dididi=true;
                        Log.e("bdhsmsthfl","dddssss");
                    }
                }
                else{

                }




            }


        }

    };



    private void startLocationUpdates() {

        if (!checkLocationServicesStatus()) {

            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        }else {

            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);



            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED   ) {

                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }


            Log.d(TAG, "startLocationUpdates : call mFusedLocationClient.requestLocationUpdates");

            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            if (checkPermission())
                mGoogleMap.setMyLocationEnabled(true);

        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart");

        if (checkPermission()) {

            Log.d(TAG, "onStart : call mFusedLocationClient.requestLocationUpdates");

            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            Log.e("googlemap_skdhkfk","1");
            if (mGoogleMap!=null) {
                mGoogleMap.setMyLocationEnabled(true);

                Log.e("googlemap_skdhkfk","7");
            }



        }


    }


    @Override
    protected void onStop() {

        super.onStop();

        if (mFusedLocationClient != null) {

            Log.d(TAG, "onStop : call stopLocationUpdates");
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }




    public String getCurrentAddress(LatLng latlng) {
        Log.e("googlemap_skdhkfk","8");
        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }


        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }

    }


    public boolean checkLocationServicesStatus() {
        Log.e("googlemap_skdhkfk","9");
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {
        Log.e("googlemap_skdhkfk","10");




        final LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);

        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.map_marker);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 75, 120, false);

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));


        markerOptions.draggable(true);







        if(Tlqkf==true){

            if(location_on==true){
            //    currentMarker = mGoogleMap.addMarker(markerOptions);

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
                SimpleDateFormat format3 = new SimpleDateFormat ( "yyyy년 MM월 dd일 HH시 mm분");
                Date time2 = new Date();
                String time3 = format3.format(time2);
                my_infoRequest myinforequest = new my_infoRequest(master_key,my_info_array.get(0).getName(),time3,String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()),responseListener);
                RequestQueue queue = Volley.newRequestQueue(LocationActivity.this);
                queue.add(myinforequest);
                Log.e("asdf","asdf");



            }
            else if (location_on==false){}

            Log.e("asdfasdf","sdf");

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
            if(once_excute==false){
                mGoogleMap.moveCamera(cameraUpdate);
                once_excute=true;
            }

        }
        else{

        }

    }


    public void setDefaultLocation() {

        Log.e("googlemap_skdhkfk","11");
        //디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(37.530279, 126.7203273);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";


        if (currentMarker != null) currentMarker.remove();

//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(DEFAULT_LOCATION);
//        markerOptions.title(markerTitle);
//        markerOptions.snippet(markerSnippet);
//        markerOptions.draggable(true);
//        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.map_marker);
//        Bitmap b=bitmapdraw.getBitmap();
//        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 75, 120, false);
//
//        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
//        currentMarker = mGoogleMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mGoogleMap.moveCamera(cameraUpdate);

    }


    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    private boolean checkPermission() {
        Log.e("googlemap_skdhkfk","12");
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);



        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {
            return true;
        }

        return false;

    }



    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {
        Log.e("googlemap_skdhkfk","13");
        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {

                // 퍼미션을 허용했다면 위치 업데이트를 시작합니다.
                startLocationUpdates();
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {


                    // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();

                }else {


                    // "다시 묻지 않음"을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();
                }
            }

        }
    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {
        Log.e("googlemap_skdhkfk","14");
        AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


//        if(requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK)
//        {
//            final Place place = PlacePicker.getPlace(this, data);
//            final CharSequence name = place.getName();
//            final CharSequence address = place.getAddress();
//            String attributions = (String) place.getAttributions();
//            if (attributions == null) {
//                attributions = "";
//            }
////            tv.setText("");
////            tv.append("name : " + name);
////            tv.append("address : " + address+"\n");
////            tv.append(Html.fromHtml(attributions));
//
//        }
//        else{
            super.onActivityResult(requestCode, resultCode, data);
            Log.e("googlemap_skdhkfk","15");
//            switch (requestCode) {
//
//                case GPS_ENABLE_REQUEST_CODE:
//
//                    //사용자가 GPS 활성 시켰는지 검사
//                    if (checkLocationServicesStatus()) {
//                        if (checkLocationServicesStatus()) {
//
//                            Log.d(TAG, "onActivityResult : GPS 활성화 되있음");
//
//
//                            needRequest = true;
//
//                            return;
//                        }
//                    }
//
//                    break;
//            }
   //     }






    }


    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(LocationActivity.this,
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
        String TAG_nickname ="nickname";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);


                String name = item.getString(TAG_name);
                String nickname = item.getString(TAG_nickname);


                my_infoDTO myinfodto = new my_infoDTO();


                myinfodto.setName(name);
                myinfodto.setNickname(nickname);

                my_info_array.add(myinfodto);
            }








        } catch (JSONException e) {

        }


    }

    private class GetData_sharing_location extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            progressDialog = ProgressDialog.show(LocationActivity.this,
     //               "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

//            progressDialog.dismiss();

            if(result==null){

            }
            else{
                mJsonString=result;
                showResult_sharing_location();

            }

            for(int index=0; index<sharing_location_array.size();index++){
                LatLng currentLatLng = new LatLng(Double.valueOf(sharing_location_array.get(index).getLocation_lat()),Double.valueOf(sharing_location_array.get(index).getLocation_lng()));
                MarkerOptions markerOptions = new MarkerOptions();

                if (currentMarker != null) currentMarker.remove();


                String formatted = sharing_location_array.get(index).getName()+"\n"+sharing_location_array.get(index).getTime();
                if(TextUtils.isEmpty(formatted)){
                    Log.e("bbbbtlqkf","hi");
                }
                else{
                    tv_marker.setText(formatted);
                }

                markerOptions.position(currentLatLng);
                markerOptions.title(sharing_location_array.get(index).getName());
                markerOptions.snippet(sharing_location_array.get(index).getTime());


                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(LocationActivity.this, marker_root_view)));


                mGoogleMap.addMarker(markerOptions);

                Log.e("howhowhow","dddd");



            //    mGoogleMap.addMarker(markerOptions);

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
    private void showResult_sharing_location(){

        String TAG_JSON="youngseok";
        String TAG_name = "name";
        String TAG_time="time";
        String TAG_location_lat ="location_lat";
        String TAG_location_lng = "location_lng";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);


                String name = item.getString(TAG_name);
                String time = item.getString(TAG_time);
                String location_lat = item.getString(TAG_location_lat);
                String location_lng = item.getString(TAG_location_lng);


                sharing_locationDTO sharinglocationdto = new sharing_locationDTO();


                sharinglocationdto.setName(name);
                sharinglocationdto.setTime(time);
                sharinglocationdto.setLocation_lat(location_lat);
                sharinglocationdto.setLocation_lng(location_lng);

                sharing_location_array.add(sharinglocationdto);







            }








        } catch (JSONException e) {

        }


    }

    private class GetData_marker extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            progressDialog = ProgressDialog.show(LocationActivity.this,
            //               "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

//            progressDialog.dismiss();

            if(result==null){

            }
            else{
                mJsonString=result;
                showResult_marker();

            }


           // mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocatiion.getLatitude(),mCurrentLocatiion.getLongitude()), 10));




            mClusterManager.clearItems();


            for(int index=0; index<marker_array.size();index++){
                LatLng currentLatLng = new LatLng(Double.valueOf(marker_array.get(index).getLocation_lat()),Double.valueOf(marker_array.get(index).getLocation_lng()));
                MarkerOptions markerOptions = new MarkerOptions();

                if (currentMarker != null) currentMarker.remove();
                markerOptions.position(currentLatLng);
                markerOptions.title(marker_array.get(index).getTitle());
                markerOptions.snippet(marker_array.get(index).getSnip());

                markerDTO offsetItem = new markerDTO(currentLatLng.latitude, currentLatLng.longitude,marker_array.get(index).getTitle(),marker_array.get(index).getSnip());
                mClusterManager.addItem(offsetItem);
                mClusterManager.cluster();





//                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.map_marker);
//                Bitmap b=bitmapdraw.getBitmap();
//                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 75, 120, false);
//
//                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                markerOptions.draggable(true);

     //           mGoogleMap.addMarker(markerOptions);


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
    private void showResult_marker(){

        String TAG_JSON="youngseok";
        String TAG_title = "title";
        String TAG_snip="snip";
        String TAG_location_lat ="location_lat";
        String TAG_location_lng = "location_lng";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);


                String title = item.getString(TAG_title);
                String snip = item.getString(TAG_snip);
                String location_lat = item.getString(TAG_location_lat);
                String location_lng = item.getString(TAG_location_lng);



                markerDTO markerdto = new markerDTO();


                markerdto.setTitle(title);
                markerdto.setSnip(snip);
                markerdto.setLocation_lat(location_lat);
                markerdto.setLocation_lng(location_lng);

                marker_array.add(markerdto);










            }








        } catch (JSONException e) {

        }


    }



    private class GetData_list extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            progressDialog = ProgressDialog.show(LocationActivity.this,
            //               "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

//            progressDialog.dismiss();

            if(result==null){

            }
            else{
                mJsonString=result;
                showResult_list();

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
    private void showResult_list(){

        String TAG_JSON="youngseok";
        String TAG_title = "title";
        String TAG_sub_title="sub_title";
        String TAG_location_lat ="location_lat";
        String TAG_location_lng = "location_lng";
        String TAG_type_code="type_code";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);


                String title = item.getString(TAG_title);
                String sub_title = item.getString(TAG_sub_title);
                String location_lat = item.getString(TAG_location_lat);
                String location_lng = item.getString(TAG_location_lng);
                String type_code = item.getString(TAG_type_code);







                ListDTO listdto = new ListDTO();

                listdto.setTitle(title);
                listdto.setSub_title(sub_title);
                listdto.setLat(location_lat);
                listdto.setLng(location_lng);
                listdto.setType_code(type_code);



                fab_list_array.add(listdto);
                fab_list_Adapter.notifyDataSetChanged();





            }








        } catch (JSONException e) {

        }


    }




    private Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }













}






