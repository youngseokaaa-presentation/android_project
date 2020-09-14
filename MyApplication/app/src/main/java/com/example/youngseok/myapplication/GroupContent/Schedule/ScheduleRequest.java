package com.example.youngseok.myapplication.GroupContent.Schedule;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ScheduleRequest extends StringRequest {

    final static private String URL = "http://192.168.43.34/Schedule/insert_schedule.php";
    private Map<String,String> parameters;

    public ScheduleRequest(String master_key,String name, String year, String month,String day, String schedule_content, String schedule_content_detail,
                           String time_hour,String time_minute,Response.Listener<String> listener)
    {

        super(Method.POST,URL,listener,null);
        parameters=new HashMap<>();
        parameters.put("master_key",master_key);
        parameters.put("name",name);
        parameters.put("year",year);
        parameters.put("month",month);
        parameters.put("day",day);
        parameters.put("schedule_content",schedule_content);
        parameters.put("schedule_content_detail",schedule_content_detail);
        parameters.put("time_hour",time_hour);
        parameters.put("time_minute",time_minute);

    }
    @Override
    protected Map<String,String>getParams() throws AuthFailureError {
        return parameters;
    }
}
