package com.example.youngseok.myapplication.GroupContent;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class check_account_Request extends StringRequest {

    final static private String URL = "http://192.168.43.34/group_content/financial/check_account.php";
    private Map<String,String> parameters;

    public check_account_Request(String master_key, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters=new HashMap<>();
        parameters.put("master_key",master_key);
    }

    @Override
    protected Map<String,String> getParams() throws AuthFailureError {
        return parameters;
    }
}