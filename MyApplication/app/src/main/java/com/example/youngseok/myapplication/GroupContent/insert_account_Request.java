package com.example.youngseok.myapplication.GroupContent;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class insert_account_Request extends StringRequest {

    final static private String URL = "http://192.168.43.34/group_content/financial/insert_account.php";
    private Map<String,String> parameters;

    public insert_account_Request(String master_key,String account,String bank_or_hand, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters=new HashMap<>();
        parameters.put("master_key",master_key);
        parameters.put("account",account);
        parameters.put("bank_or_hand",bank_or_hand);
    }

    @Override
    protected Map<String,String> getParams() throws AuthFailureError {
        return parameters;
    }
}