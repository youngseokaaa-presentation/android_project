package com.example.youngseok.myapplication.GroupContent.Financial.Financial_dialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;



public class financial_dialog_image_delete_request extends StringRequest {

    final static private String URL = "http://192.168.43.34/group_content/financial/financial_dialog/financial_delete.php";
    private Map<String,String> parameters;

    public financial_dialog_image_delete_request(String master_key,String money_type,String money,String
            money_explain,String account_time,String financial_dialog_picture , Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters=new HashMap<>();
        parameters.put("master_key",master_key);

        parameters.put("money_type",money_type);
        parameters.put("money",money);
        parameters.put("money_explain",money_explain);
        parameters.put("account_time",account_time);

        parameters.put("financial_dialog_picture",financial_dialog_picture);
    }

    @Override
    protected Map<String,String> getParams() throws AuthFailureError {
        return parameters;
    }
}