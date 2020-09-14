package com.example.youngseok.myapplication.GroupContent.Location;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class marker_modified_request extends StringRequest {

    final static private String URL = "http://192.168.43.34/group_content/geo/modified.php";
    private Map<String,String> parameters;

    public marker_modified_request(String master_key,String title,String snip, String location_lat,String locating_lng, Response.Listener<String> listener)
    {

        super(Method.POST,URL,listener,null);
        parameters=new HashMap<>();
        parameters.put("master_key",master_key);
        parameters.put("title",title);
        parameters.put("snip",snip);
        parameters.put("location_lat",location_lat);
        parameters.put("location_lng",locating_lng);

    }
    @Override
    protected Map<String,String>getParams() throws AuthFailureError {
        return parameters;
    }
}
