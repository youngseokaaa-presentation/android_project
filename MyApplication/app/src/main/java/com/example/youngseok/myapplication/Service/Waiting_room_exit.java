package com.example.youngseok.myapplication.Service;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Waiting_room_exit extends StringRequest {

    final static private String URL = "http://192.168.43.34/Invite/invite_after_delete.php";
    private Map<String,String> parameters;

    public Waiting_room_exit(String master_key,String master_id,String joiner,String name, Response.Listener<String> listener)
    {

        super(Method.POST,URL,listener,null);
        parameters=new HashMap<>();
        parameters.put("master_key",master_key);
        parameters.put("master_id",master_id);
        parameters.put("joiner",joiner);
        parameters.put("name",name);


    }
    @Override
    protected Map<String,String>getParams() throws AuthFailureError {
        return parameters;
    }
}