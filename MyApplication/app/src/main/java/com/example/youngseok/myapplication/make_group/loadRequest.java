package com.example.youngseok.myapplication.make_group;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class loadRequest extends StringRequest {

    final static private String URL = "http://192.168.43.34/basicrecycle/basic_load.php";
    private Map<String,String> parameters;

    public loadRequest(String id,   Response.Listener<String> listener)
    {

        super(Method.POST,URL,listener,null);
        parameters=new HashMap<>();
        parameters.put("id",id);

    }
    @Override
    protected Map<String,String>getParams() throws AuthFailureError {
        return parameters;
    }
}