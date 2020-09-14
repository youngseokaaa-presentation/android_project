package com.example.youngseok.myapplication.recycler_drag_drop;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class sortRequest extends StringRequest {

    final static private String URL = "http://192.168.43.34/basicrecycle/sort.php";
    private Map<String,String> parameters;

    public sortRequest(String id,String name,String idx, Response.Listener<String> listener)
    {

        super(Method.POST,URL,listener,null);
        parameters=new HashMap<>();
        parameters.put("id",id);
        parameters.put("name",name);
        parameters.put("idx",idx);

    }
    @Override
    protected Map<String,String>getParams() throws AuthFailureError {
        return parameters;
    }
}
