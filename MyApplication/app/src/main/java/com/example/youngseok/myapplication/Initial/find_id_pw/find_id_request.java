package com.example.youngseok.myapplication.Initial.find_id_pw;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class find_id_request extends StringRequest {

    final static private String URL = "http://192.168.43.34/find/findid.php";

    private Map<String, String> parameters;



    public find_id_request(String email, String name,  Response.Listener<String> listener){

        super(Method.POST, URL, listener, null);//해당 URL에 POST방식으로 파마미터들을 전송함

        parameters = new HashMap<>();

        parameters.put("email", email);

        parameters.put("name", name);

    }



    @Override

    protected Map<String, String> getParams() throws AuthFailureError {

        return parameters;

    }
}
