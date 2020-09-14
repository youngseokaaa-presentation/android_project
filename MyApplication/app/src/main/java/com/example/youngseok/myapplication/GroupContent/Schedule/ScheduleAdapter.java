package com.example.youngseok.myapplication.GroupContent.Schedule;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.youngseok.myapplication.GroupContent.GroupContentDTO;
import com.example.youngseok.myapplication.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.youngseok.myapplication.Initial.InitialActivity.save_my_id;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleHolder>   {

    private ArrayList<ScheduleDTO> mSchedule;
    private ArrayList<ScheduleDTO> mSchedule_clear;
    private Context mContext;
    private ArrayList<Dialog_master_id_DTO> mdialog_id;

    private String myear;
    private String mmonth;
    private String mday;

    public static boolean schedule_marker_switch=false;

    public class ScheduleHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        protected TextView year_month_day;
        protected TextView time_hour_minute;
        protected TextView content;
        protected TextView content_detail;



        public final View mView;
        public ScheduleHolder(View view){
            super(view);

            mView=view;
            this.year_month_day=view.findViewById(R.id.year_month_day);
            this.time_hour_minute=view.findViewById(R.id.time_hour_minute);
            this.content=view.findViewById(R.id.content);
            this.content_detail=view.findViewById(R.id.content_detail);

            view.setOnCreateContextMenuListener(this);



        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {}
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                return true;
            }
        };
    }
    public ScheduleAdapter(Context context,ArrayList<ScheduleDTO> mSchedule,ArrayList<Dialog_master_id_DTO> mdialog_id,ArrayList<ScheduleDTO> mSchedule_clear){
        this.mContext=context;
        this.mSchedule=mSchedule;
        this.mdialog_id=mdialog_id;
        this.mSchedule_clear=mSchedule_clear;
    }

    @Override
    public ScheduleHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.schedule_list
                ,viewGroup,false);

        ScheduleHolder viewHolder = new ScheduleHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ScheduleHolder viewHolder, final int position){


        if(mSchedule.get(position).getYear()==2050){
            viewHolder.mView.setVisibility(View.GONE);
        }
        else{
            viewHolder.mView.setVisibility(View.VISIBLE);
        }

        ScheduleDTO data = mSchedule.get(position);

        viewHolder.year_month_day.setText(data.getYear()+"."+data.getMonth()+"."+data.getDay());
        viewHolder.time_hour_minute.setText(data.getTime_hour()+"시 "+data.getTime_minute()+"분");
        viewHolder.content.setText(data.getSchedule_content());
        viewHolder.content_detail.setText(data.getSchedule_content_detail());




        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                View view = LayoutInflater.from(mContext)
                        .inflate(R.layout.schedule_dialog, null, false);
                builder.setView(view);

                final AlertDialog alertdialog = builder.create();
                final DatePicker datePicker = view.findViewById(R.id.datePicker);
                final TimePicker timePicker = view.findViewById(R.id.timePicker);
                final EditText edit_schedule_title = view.findViewById(R.id.edit_schedule_title);
                final EditText edit_schedule_content = view.findViewById(R.id.edit_schedule_content);
                final Button schedule_confirm_btn = view.findViewById(R.id.schedult_confirm_btn);
                final Button schedule_delete_btn = view.findViewById(R.id.schedule_delete_btn);

                 final AlertDialog dialog_main = builder.create();
                dialog_main.show();

                if(save_my_id.equals(mdialog_id.get(0).getId())){
                    edit_schedule_title.setText(mSchedule.get(position).getSchedule_content());


                    edit_schedule_content.setText(mSchedule.get(position).getSchedule_content_detail());



                    datePicker.init(mSchedule.get(position).getYear(), mSchedule.get(position).getMonth()-1,
                           mSchedule.get(position).getDay(), new DatePicker.OnDateChangedListener() {
                                @Override
                                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                }
                            });


                    timePicker.setHour(Integer.parseInt(mSchedule.get(position).getTime_hour()));
                    timePicker.setMinute(Integer.parseInt(mSchedule.get(position).getTime_minute()));

                    schedule_confirm_btn.setText("수정");

                    schedule_delete_btn.setVisibility(View.VISIBLE);
                    schedule_delete_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog.Builder click_builder = new AlertDialog.Builder(mContext);

                            click_builder.setMessage("일정을 삭제 하시겠습니까??");
                            click_builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Response.Listener<String> responseListener = new Response.Listener<String>(){

                                        @Override
                                        public void onResponse(String response){
                                            try{


                                                JSONObject jsonResponse = new JSONObject(response);
                                                boolean success =jsonResponse.getBoolean("success");
                                                if(success){

                                                   notifyDataSetChanged();
                                                }
                                                else{
                                                }
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    //volley 라이브러리 이용해서 실제 서버와 통신
                                    ScheduleDeleteRequest scheduledeleteRequest = new ScheduleDeleteRequest(
                                            mSchedule.get(position).getMaster_key(),
                                            mSchedule.get(position).getName(),
                                            String.valueOf(mSchedule.get(position).getYear()),
                                            String.valueOf(mSchedule.get(position).getMonth()),
                                            String.valueOf(mSchedule.get(position).getDay()),
                                            mSchedule.get(position).getSchedule_content(),
                                            mSchedule.get(position).getSchedule_content_detail(),
                                            mSchedule.get(position).getTime_hour(),
                                            mSchedule.get(position).getTime_minute()
                                            ,responseListener);
                                    RequestQueue queue = Volley.newRequestQueue(mContext);
                                    queue.add(scheduledeleteRequest);

                                    mSchedule_clear.add(mSchedule.get(position));
                                    mSchedule.remove(position);
                                    schedule_marker_switch=true;

                                    notifyDataSetChanged();

                                    dialog.dismiss();
                                    dialog_main.dismiss();
                                }
                            });
                            click_builder.setNegativeButton("취소",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    dialog_main.dismiss();

                                }
                            });
                            click_builder.show();
                        }
                    });

                    notifyDataSetChanged();


                    schedule_confirm_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            final AlertDialog.Builder click_builder = new AlertDialog.Builder(mContext);

                            click_builder.setMessage("일정을 변경하시겠습니까?");
                            click_builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Response.Listener<String> responseListener = new Response.Listener<String>(){

                                        @Override
                                        public void onResponse(String response){
                                            try{


                                                JSONObject jsonResponse = new JSONObject(response);
                                                boolean success =jsonResponse.getBoolean("success");
                                                if(success){

                                                    notifyDataSetChanged();
                                                }
                                                else{
                                                }
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    //volley 라이브러리 이용해서 실제 서버와 통신
                                    ScheduleModifyRequest schedulemodifyRequest = new ScheduleModifyRequest(
                                            mSchedule.get(position).getMaster_key(),
                                            mSchedule.get(position).getName(),
                                            String.valueOf(mSchedule.get(position).getYear()),
                                            String.valueOf(mSchedule.get(position).getMonth()),
                                            String.valueOf(mSchedule.get(position).getDay()),
                                            mSchedule.get(position).getSchedule_content(),
                                            mSchedule.get(position).getSchedule_content_detail(),
                                            mSchedule.get(position).getTime_hour(),
                                            mSchedule.get(position).getTime_minute(),

                                            mSchedule.get(position).getMaster_key(),
                                            mSchedule.get(position).getName(),
                                            String.valueOf(datePicker.getYear()),
                                            String.valueOf(datePicker.getMonth()+1),
                                            String.valueOf(datePicker.getDayOfMonth()),
                                            edit_schedule_title.getText().toString(),
                                            edit_schedule_content.getText().toString(),
                                           String.valueOf( timePicker.getHour()),
                                           String.valueOf( timePicker.getMinute())


                                            ,responseListener);
                                    RequestQueue queue = Volley.newRequestQueue(mContext);
                                    queue.add(schedulemodifyRequest);
                                    mSchedule.remove(position);
                                    ScheduleDTO dto = new ScheduleDTO();
                                    dto.setMaster_key(mSchedule.get(position).getMaster_key());
                                    dto.setName(mSchedule.get(position).getName());
                                    dto.setYear(datePicker.getYear());
                                    dto.setMonth(datePicker.getMonth()+1);
                                    dto.setDay(datePicker.getDayOfMonth());
                                    dto.setSchedule_content(edit_schedule_title.getText().toString());
                                    dto.setSchedule_content_detail(edit_schedule_content.getText().toString());
                                    dto.setTime_hour(  String.valueOf( timePicker.getHour()));
                                    dto.setTime_minute(String.valueOf( timePicker.getMinute()));
                                    mSchedule.add(position,dto);





                                    dialog.dismiss();
                                    dialog_main.dismiss();
                                }
                            });
                            click_builder.setNegativeButton("취소",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    dialog_main.dismiss();

                                }
                            });
                            click_builder.show();








                        }
                    });



                }
                else
                {
                    schedule_confirm_btn.setText("확인");
                    edit_schedule_title.setText(mSchedule.get(position).getSchedule_content());
                    edit_schedule_title.setEnabled(false);
                    edit_schedule_title.setTextColor(Color.BLACK);

                    edit_schedule_content.setText(mSchedule.get(position).getSchedule_content_detail());
                    edit_schedule_content.setEnabled(false);
                    edit_schedule_content.setTextColor(Color.BLACK);

                    datePicker.init(mSchedule.get(position).getYear(), mSchedule.get(position).getMonth()-1,
                            mSchedule.get(position).getDay(), new DatePicker.OnDateChangedListener() {
                                @Override
                                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                }
                            });
                    datePicker.setEnabled(false);

                    timePicker.setHour(Integer.parseInt(mSchedule.get(position).getTime_hour()));
                    timePicker.setMinute(Integer.parseInt(mSchedule.get(position).getTime_minute()));
                    timePicker.setEnabled(false);

                    schedule_confirm_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog_main.dismiss();
                        }
                    });
                }
            }
        });

















    }



    public int getMove_real(){
        Calendar dateandtime = Calendar.getInstance();

        int year=dateandtime.get(Calendar.YEAR);
        int month=dateandtime.get(Calendar.MONTH)+1;
        int day=dateandtime.get(Calendar.DAY_OF_MONTH);

        int k=0;

        for(int index=0; index<mSchedule.size();index++){
            if(mSchedule.get(index).getYear()==year){
                if(mSchedule.get(index).getMonth()==month){
                    if(mSchedule.get(index).getDay()==day){
                        k=index+1;
                    }
                    else{

                    }
                }
                else{
                    //
                }
            }
            else{
                //
            }
        }

        if(k==0){
            for(int index=0; index<mSchedule.size();index++){
                if(mSchedule.get(index).getYear()==year){
                    if(mSchedule.get(index).getMonth()==month){
                        k=index+1;
                        break;
                    }
                    else{
                        //
                    }
                }
                else{
                    //
                }
            }

        }






        return (k==0 ? 0 :k);
    }

    @Override
    public int getItemCount(){
        return (null !=mSchedule ? mSchedule.size() :0);
    }
}

