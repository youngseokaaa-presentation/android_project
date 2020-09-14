package com.example.youngseok.myapplication.GroupContent.Financial;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class financialRequest extends StringRequest {

    final static private String URL = "http://192.168.43.34/group_content/financial/financial_insert.php";
    private Map<String,String> parameters;

    public financialRequest(String master_key,String account,String money_type,String money,String
            money_explain,String account_time,String bank_or_hand,String content_edit,String result, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters=new HashMap<>();
        parameters.put("master_key",master_key);
        parameters.put("account",account);
        parameters.put("money_type",money_type);
        parameters.put("money",money);
        parameters.put("money_explain",money_explain);
        parameters.put("account_time",account_time);
        parameters.put("bank_or_hand",bank_or_hand);
        parameters.put("content_edit",content_edit);
        parameters.put("result",result);
    }

    @Override
    protected Map<String,String> getParams() throws AuthFailureError {
        return parameters;
    }
}
