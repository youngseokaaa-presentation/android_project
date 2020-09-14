package com.example.youngseok.myapplication.GroupContent.member_list;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Insert_waiting_request extends StringRequest {

    final static private String URL = "http://192.168.43.34/group_content/insert_waiting.php";
    private Map<String,String> parameters;

    public Insert_waiting_request(String master_key,String master_id,String joiner,String name, Response.Listener<String> listener)
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