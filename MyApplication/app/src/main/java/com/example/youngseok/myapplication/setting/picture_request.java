package com.example.youngseok.myapplication.setting;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class picture_request extends StringRequest {

    final static private String URL = "http://192.168.43.34/Setting/insert_picture.php";
    private Map<String,String> parameters;

    public picture_request(String id,String picture, Response.Listener<String> listener)
    {

        super(Method.POST,URL,listener,null);
        parameters=new HashMap<>();
        parameters.put("id",id);
        parameters.put("picture",picture);

    }
    @Override
    protected Map<String,String>getParams() throws AuthFailureError {
        return parameters;
    }
}