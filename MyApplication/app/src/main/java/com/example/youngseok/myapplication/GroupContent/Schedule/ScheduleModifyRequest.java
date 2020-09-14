package com.example.youngseok.myapplication.GroupContent.Schedule;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ScheduleModifyRequest extends StringRequest {

    final static private String URL = "http://192.168.43.34/Schedule/modify_schedule.php";
    private Map<String,String> parameters;

    public ScheduleModifyRequest(String master_key,String name, String year, String month,String day, String schedule_content, String schedule_content_detail,
                           String time_hour,String time_minute,
                                 String master_key_ch,
            String name_ch,
            String year_ch,
            String month_ch,
            String day_ch,
            String schedule_content_ch,
            String schedule_content_detail_ch,
            String time_hour_ch,
            String time_minute_ch,Response.Listener<String> listener)
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

        parameters.put("master_key_ch",master_key_ch);
        parameters.put("name_ch",name_ch);
        parameters.put("year_ch",year_ch);
        parameters.put("month_ch",month_ch);
        parameters.put("day_ch",day_ch);
        parameters.put("schedule_content_ch",schedule_content_ch);
        parameters.put("schedule_content_detail_ch",schedule_content_detail_ch);
        parameters.put("time_hour_ch",time_hour_ch);
        parameters.put("time_minute_ch",time_minute_ch);

    }
    @Override
    protected Map<String,String>getParams() throws AuthFailureError {
        return parameters;
    }
}