package com.example.youngseok.myapplication.make_group;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class insertRequest extends StringRequest {

    final static private String URL = "http://192.168.43.34/basicrecycle/basiccontent.php";
    private Map<String,String> parameters;

    public insertRequest(String id, String name, String content, String sumnail,String profile ,String idx,String joiner,String master_key, Response.Listener<String> listener)
    {

        super(Method.POST,URL,listener,null);
        parameters=new HashMap<>();
        parameters.put("id",id);
        parameters.put("name",name);
        parameters.put("content",content);
        parameters.put("sumnail",sumnail);
        parameters.put("profile",profile);
        parameters.put("idx",idx);
        parameters.put("joiner",joiner);
        parameters.put("master_key",master_key);

    }
    @Override
    protected Map<String,String>getParams() throws AuthFailureError {
        return parameters;
    }
}