package com.example.youngseok.myapplication.Initial.join;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class joinRequest extends StringRequest {
    final static private String URL = "http://192.168.43.34/Login_Join/UserJoin.php";
    private Map<String,String> parameters;

    public joinRequest(String userNAME, String id, String password, String nickname, String email, String phone, Response.Listener<String> listener)
    {

        super(Method.POST,URL,listener,null);
        parameters=new HashMap<>();
        parameters.put("name",userNAME);
        parameters.put("id",id);
        parameters.put("password",password);
        parameters.put("nickname",nickname);
        parameters.put("email",email);
        parameters.put("phone",phone);

    }
    @Override
    protected Map<String,String>getParams() throws AuthFailureError{
        return parameters;
    }
}
