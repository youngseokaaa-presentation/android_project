package com.example.youngseok.myapplication.Initial.join;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class emailValidate extends StringRequest {

    final static private String URL = "http://192.168.43.34/Validate/emailValidate.php";
    private Map<String,String> parameters;

    public emailValidate(String email, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters=new HashMap<>();
        parameters.put("email",email);
    }

    @Override
    protected Map<String,String> getParams() throws AuthFailureError {
        return parameters;
    }
}