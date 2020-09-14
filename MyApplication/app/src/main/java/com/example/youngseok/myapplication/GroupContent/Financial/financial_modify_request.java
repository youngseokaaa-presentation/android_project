package com.example.youngseok.myapplication.GroupContent.Financial;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class financial_modify_request extends StringRequest {

    final static private String URL = "http://192.168.43.34/group_content/financial/financial_modify.php";
    private Map<String,String> parameters;

    public financial_modify_request(String master_key,String money_type,String money,String
            money_explain,String account_time,String money_type_sub,String money_sub,String money_explain_sub,
                                    String account_time_sub,Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters=new HashMap<>();
        parameters.put("master_key",master_key);

        parameters.put("money_type",money_type);
        parameters.put("money",money);
        parameters.put("money_explain",money_explain);
        parameters.put("account_time",account_time);


        parameters.put("money_type_sub",money_type_sub);
        parameters.put("money_sub",money_sub);
        parameters.put("money_explain_sub",money_explain_sub);
        parameters.put("account_time_sub",account_time_sub);

    }

    @Override
    protected Map<String,String> getParams() throws AuthFailureError {
        return parameters;
    }
}
