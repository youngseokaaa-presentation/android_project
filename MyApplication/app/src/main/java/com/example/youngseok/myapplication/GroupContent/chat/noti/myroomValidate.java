package com.example.youngseok.myapplication.GroupContent.chat.noti;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class myroomValidate extends StringRequest {

    final static private String URL = "http://192.168.43.34/basicrecycle/myroomValidate.php";
    private Map<String,String> parameters;

    public myroomValidate(String id,String name, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters=new HashMap<>();
        parameters.put("id",id);
        parameters.put("name",name);
    }

    @Override
    protected Map<String,String> getParams() throws AuthFailureError {
        return parameters;
    }
}