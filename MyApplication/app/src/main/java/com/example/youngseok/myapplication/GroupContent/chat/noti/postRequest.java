package com.example.youngseok.myapplication.GroupContent.chat.noti;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class postRequest extends StringRequest {

    final static private String URL = "https://fcm.googleapis.com/fcm/send";
    private Map<String,String> parameters;

    public postRequest(String id, String name, String content, String sumnail,String profile ,String idx, Response.Listener<String> listener)
    {

        super(Method.POST,URL,listener,null);
        parameters=new HashMap<>();
        parameters.put("id",id);
        parameters.put("name",name);
        parameters.put("content",content);
        parameters.put("sumnail",sumnail);
        parameters.put("profile",profile);
        parameters.put("idx",idx);

    }
    @Override
    protected Map<String,String>getParams() throws AuthFailureError {
        return parameters;
    }
}