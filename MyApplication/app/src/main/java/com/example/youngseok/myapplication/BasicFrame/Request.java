package com.example.youngseok.myapplication.BasicFrame;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Request extends StringRequest {

    final static private String URL = "http://192.168.43.34/group_content/insert_waiting.php";
    private Map<String,String> parameters;

    public Request(String master_key,String master_id,String joiner,String name, Response.Listener<String> listener)
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


//    Response.Listener<String> responseListener = new Response.Listener<String>(){
//
//        @Override
//        public void onResponse(String response){
//            try{
//
//
//                JSONObject jsonResponse = new JSONObject(response);
//                boolean success =jsonResponse.getBoolean("success");
//                if(success){
//                }
//                else{
//                }
//            }
//            catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    };
//    //volley 라이브러리 이용해서 실제 서버와 통신
//    Insert_waiting_request insertwaitingrequest = new Insert_waiting_request(master_key,groupcontent_Array.get(0).getGroup_id()
//            ,invite_list.get(index).getId(),groupcontent_Array.get(0).getGroup_name(),responseListener);
//    RequestQueue queue = Volley.newRequestQueue(Member_listActivity.this);
//                queue.add(insertwaitingrequest);