package com.example.youngseok.myapplication.GroupContent.Financial;


import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.youngseok.myapplication.GroupContent.Financial.Financial_dialog.Financial_dialog_Adapter;
import com.example.youngseok.myapplication.GroupContent.Financial.Financial_dialog.Financial_dialog_DTO;
import com.example.youngseok.myapplication.GroupContent.Financial.Financial_dialog.OnItemClick;
import com.example.youngseok.myapplication.R;
import com.example.youngseok.myapplication.recycler_drag_drop.ItemTouchHelperListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;



public class FinancialAdapter extends RecyclerView.Adapter<FinancialAdapter.FinancialHolder>

implements ItemTouchHelperListener {


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if(fromPosition < 0 || fromPosition >= mRecycler.size() || toPosition < 0 || toPosition >= mRecycler.size()){
            Log.e("Tlqkf","TlqkfTlqkfTlqkf1111");
            return false;
        }

        Log.e("Tlqkf","TlqkfTlqkfTlqkf222");
        financialDTO fromItem = mRecycler.get(fromPosition);

        Log.e("Tlqkf","TlqkfTlqkfTlqkf3333");
        mRecycler.remove(fromPosition);
        Log.e("Tlqkf","TlqkfTlqkfTlqkf44");
        mRecycler.add(toPosition, fromItem);
        Log.e("Tlqkf","TlqkfTlqkfTlqkf5555");

        notifyItemMoved(fromPosition, toPosition);

        return true;
    }

    @Override
    public void onItemRemove(int position) {
        mRecycler.remove(position);
        notifyItemRemoved(position);
    }


    private ArrayList<financialDTO> mRecycler;
    private Context mContext;
    private Financial_dialog_Adapter mAdapter_dialog;
    private ArrayList<Financial_dialog_DTO> mArrayList_dialog;

    private String money_type;
    private String money;
    private String money_explain;
    private String account_time;
    private String mJsonString;
    private OnItemClick mCallback;


    public class FinancialHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{


        protected ImageView financial_pl_mi;
        protected ImageView financial_mark;
        protected TextView money_tv;
        protected TextView money_explain_tv;
        protected TextView account_time_tv;

        public final View mView;
        public FinancialHolder(View view){
            super(view);

            mView=view;

            this.financial_mark=view.findViewById(R.id.financial_mark);
            this.financial_pl_mi=view.findViewById(R.id.financial_pl_mi);
            this.money_tv=view.findViewById(R.id.money_tv);
            this.money_explain_tv=view.findViewById(R.id.money_explain_tv);
            this.account_time_tv=view.findViewById(R.id.account_time_tv);
            view.setOnCreateContextMenuListener(this);
        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {  // 3. 메뉴 추가U

            MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "수정");
            MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);

        }


        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {



            @Override
            public boolean onMenuItemClick(MenuItem item) {


                switch (item.getItemId()) {


                    case 1001:

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);


                        View view = LayoutInflater.from(mContext).inflate(R.layout.financial_dialog,null,false);

                        builder.setView(view);

                        final AlertDialog alertdialog = builder.create();

                        final RadioButton radio_insert=view.findViewById(R.id.radio_insert);
                        final RadioButton radio_out=view.findViewById(R.id.radio_out);
                        final EditText financial_dialog_money=view.findViewById(R.id.financial_dialog_money);
                        final EditText financial_dialog_detail=view.findViewById(R.id.financial_dialog_detail);
                        final Button financial_dialog_btn=view.findViewById(R.id.financial_dialog_btn);

                        final DatePicker datepicker=view.findViewById(R.id.datepicker_financial);
                        final TimePicker timepicker=view.findViewById(R.id.timepicker_financial);



                        final RecyclerView mRecyclerView_dialog=view.findViewById(R.id.financial_dialog_recycler);
                        LinearLayoutManager mLayoutManager;



                        mArrayList_dialog = new ArrayList<>();


                        mAdapter_dialog = new Financial_dialog_Adapter(mContext,mArrayList_dialog);

                        mRecyclerView_dialog.setAdapter(mAdapter_dialog);



                        mLayoutManager = new LinearLayoutManager(mContext);
                        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        mRecyclerView_dialog.setLayoutManager(mLayoutManager);

                        Financial_dialog_DTO financial_dialog_dto_basic = new Financial_dialog_DTO();
                        financial_dialog_dto_basic.setFinancial_dialog_picture("basic_click");
                        mArrayList_dialog.add(financial_dialog_dto_basic);


                        GetData task = new GetData();
                        task.execute("http://192.168.43.34/group_content/financial/financial_dialog/financial_show_imag.php",mRecycler.get(getAdapterPosition()).getMaster_key(),mRecycler.get(getAdapterPosition()).getMoney_type(),mRecycler.get(getAdapterPosition()).getMoney(),mRecycler.get(getAdapterPosition()).getMoney_explain(),mRecycler.get(getAdapterPosition()).getAccount_time());






                        mAdapter_dialog.notifyDataSetChanged();

                        mRecyclerView_dialog.addItemDecoration(new DividerItemDecoration(mRecyclerView_dialog.getContext(),1));







                        if(mRecycler.get(getAdapterPosition()).getMoney_type().equals("입금")){
                            radio_insert.setChecked(true);
                            radio_out.setChecked(false);
                        }
                        else if(mRecycler.get(getAdapterPosition()).getMoney_type().equals("출금")){
                            radio_insert.setChecked(false);
                            radio_out.setChecked(true);
                        }

                        financial_dialog_money.setText(mRecycler.get(getAdapterPosition()).getMoney());

                        financial_dialog_detail.setText(mRecycler.get(getAdapterPosition()).getMoney_explain());

                        money_type=mRecycler.get(getAdapterPosition()).getMoney_type();
                        money=mRecycler.get(getAdapterPosition()).getMoney();
                        money_explain=mRecycler.get(getAdapterPosition()).getMoney_explain();
                        account_time=mRecycler.get(getAdapterPosition()).getAccount_time();


                        Log.e("fanfan",mRecycler.get(getAdapterPosition()).getAccount_time());


                        String a = mRecycler.get(getAdapterPosition()).getAccount_time();

                        String[] aa = a.split("\\s");
                        String[] bb = aa[0].split("\\/");
                        String[] cc = aa[1].split("\\:");

                        Log.e("fanfan",bb[0]+"ddd"+bb[1]);
                        Log.e("fanfan",cc[0]+"ddd"+cc[1]);

                        int month_get = Integer.valueOf(bb[0]);
                        int day_get=Integer.valueOf(bb[1]);
                        int hour_get=Integer.valueOf(cc[0]);
                        int minute_get=Integer.valueOf(cc[1]);

                        datepicker.init(2019, month_get-1, day_get, new DatePicker.OnDateChangedListener() {
                            @Override
                            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            }
                        });
                        timepicker.setHour(hour_get);
                        timepicker.setMinute(minute_get);

                        financial_dialog_btn.setText("수정 완료");

                        financial_dialog_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {



                                String radio_check = null;
                                String money_modify=null;
                                String money_detail_modify=null;
                                String day_time=null;

                                if(radio_insert.isChecked()){
                                    radio_check="입금";
                                }
                                else if(radio_out.isChecked()){
                                    radio_check="출금";
                                }

                                money_modify=financial_dialog_money.getText().toString();
                                money_detail_modify=financial_dialog_detail.getText().toString();


                                int month = datepicker.getMonth()+1;
                                int day=datepicker.getDayOfMonth();
                                int hour=timepicker.getHour();
                                int minute=timepicker.getMinute();



                                String month_mix;
                                String day_mix;
                                String hour_mix;
                                String minute_mix;

                                if(month<10){
                                    month_mix = "0"+month;
                                }else{
                                    month_mix=String.valueOf(month);
                                }

                                if(day<10){
                                    day_mix="0"+day;
                                }
                                else{
                                    day_mix=String.valueOf(day);
                                }

                                if(hour<10){
                                    hour_mix="0"+hour;
                                }
                                else{
                                    hour_mix=String.valueOf(hour);
                                }

                                if(minute<10){
                                    minute_mix="0"+minute;
                                }
                                else{
                                    minute_mix=String.valueOf(minute);
                                }


                                day_time = month_mix+"/"+day_mix+" "+hour_mix+":"+minute_mix;


                                Response.Listener<String> responseListener = new Response.Listener<String>(){

                                    @Override
                                    public void onResponse(String response){
                                        try{


                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success =jsonResponse.getBoolean("success");
                                            if(success){
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
                                financial_modify_request financial_modify_request = new financial_modify_request(mRecycler.get(getAdapterPosition()).getMaster_key()
                                        ,mRecycler.get(getAdapterPosition()).getMoney_type(),mRecycler.get(getAdapterPosition()).getMoney(),
                                        mRecycler.get(getAdapterPosition()).getMoney_explain(),mRecycler.get(getAdapterPosition()).getAccount_time(),
                                        radio_check,money_modify,money_detail_modify,day_time,responseListener);
                                RequestQueue queue = Volley.newRequestQueue(mContext);
                                queue.add(financial_modify_request);

                                financialDTO financialdto = new financialDTO(mRecycler.get(getAdapterPosition()).getMaster_key(),mRecycler.get(getAdapterPosition()).getAccount(),
                                radio_check,money_modify,money_detail_modify,day_time,mRecycler.get(getAdapterPosition()).getBank_or_hand(),
                                        mRecycler.get(getAdapterPosition()).getContent_edit(),mRecycler.get(getAdapterPosition()).getResult());
                                mRecycler.set(getAdapterPosition(),financialdto);
                                notifyItemChanged(getAdapterPosition());

                                mCallback.onClick("1111111111");


                                alertdialog.dismiss();


                            }
                        });





                        alertdialog.show();

                        break;

                    case 1002:

                        Response.Listener<String> responseListener = new Response.Listener<String>(){

                            @Override
                            public void onResponse(String response){
                                try{


                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success =jsonResponse.getBoolean("success");
                                    if(success){
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
                        financial_delete_Request financial_delete_request = new financial_delete_Request(mRecycler.get(getAdapterPosition()).getMaster_key(),mRecycler.get(getAdapterPosition()).getAccount()
                                ,mRecycler.get(getAdapterPosition()).getMoney_type(),mRecycler.get(getAdapterPosition()).getMoney(),
                                mRecycler.get(getAdapterPosition()).getMoney_explain(),mRecycler.get(getAdapterPosition()).getAccount_time(),responseListener);
                        RequestQueue queue = Volley.newRequestQueue(mContext);
                        queue.add(financial_delete_request);

                        mRecycler.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), mRecycler.size());
                        notifyDataSetChanged();







                        mCallback.onClick("1111111111");






                        break;

                }
                return true;
            }
        };


    }
    public FinancialAdapter(Context context, ArrayList<financialDTO> mRecycler, OnItemClick listener){
        this.mContext=context;
        this.mRecycler=mRecycler;
        this.mCallback = listener;
    }

    @Override
    public FinancialHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.financial_list
                ,viewGroup,false);

        FinancialHolder viewHolder = new FinancialHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FinancialHolder viewHolder, final int position){



        final financialDTO data=mRecycler.get(position);

        if(data.getMoney_type().equals("입금")){
            viewHolder.financial_pl_mi.setImageResource(R.drawable.plus);
        }
        else if (data.getMoney_type().equals("출금")){
            viewHolder.financial_pl_mi.setImageResource(R.drawable.minus);
        }else{}

        viewHolder.money_tv.setText(data.getMoney()+" 원");

        viewHolder.money_explain_tv.setText(data.getMoney_explain());

        viewHolder.account_time_tv.setText(data.getAccount_time());

        if(data.getBank_or_hand().equals("기업은행")){
            viewHolder.financial_mark.setImageResource(R.drawable.ibkone);
        }
        else{
            viewHolder.financial_mark.setImageResource(android.R.drawable.ic_menu_edit);
        }



        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);


                View view = LayoutInflater.from(mContext).inflate(R.layout.financial_dialog,null,false);

                builder.setView(view);

                final AlertDialog alertdialog = builder.create();

                final RadioButton radio_insert=view.findViewById(R.id.radio_insert);
                final RadioButton radio_out=view.findViewById(R.id.radio_out);
                final EditText financial_dialog_money=view.findViewById(R.id.financial_dialog_money);
                final EditText financial_dialog_detail=view.findViewById(R.id.financial_dialog_detail);
                final Button financial_dialog_btn=view.findViewById(R.id.financial_dialog_btn);

                final DatePicker datepicker=view.findViewById(R.id.datepicker_financial);
                final TimePicker timepicker=view.findViewById(R.id.timepicker_financial);



                final RecyclerView mRecyclerView_dialog=view.findViewById(R.id.financial_dialog_recycler);
                LinearLayoutManager mLayoutManager;



                mArrayList_dialog = new ArrayList<>();


                mAdapter_dialog = new Financial_dialog_Adapter(mContext,mArrayList_dialog);

                mRecyclerView_dialog.setAdapter(mAdapter_dialog);



                mLayoutManager = new LinearLayoutManager(mContext);
                mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                mRecyclerView_dialog.setLayoutManager(mLayoutManager);

                Financial_dialog_DTO financial_dialog_dto_basic = new Financial_dialog_DTO();
                financial_dialog_dto_basic.setFinancial_dialog_picture("basic_click");
                mArrayList_dialog.add(financial_dialog_dto_basic);


                GetData task = new GetData();
                task.execute("http://192.168.43.34/group_content/financial/financial_dialog/financial_show_imag.php",data.getMaster_key(),data.getMoney_type(),data.getMoney(),data.getMoney_explain(),data.getAccount_time());






                mAdapter_dialog.notifyDataSetChanged();

                mRecyclerView_dialog.addItemDecoration(new DividerItemDecoration(mRecyclerView_dialog.getContext(),1));




                radio_insert.setEnabled(false);
                radio_out.setEnabled(false);
                financial_dialog_money.setEnabled(false);
                financial_dialog_detail.setEnabled(false);
                datepicker.setEnabled(false);
                timepicker.setEnabled(false);


                if(data.getMoney_type().equals("입금")){
                    radio_insert.setChecked(true);
                    radio_out.setChecked(false);
                }
                else if(data.getMoney_type().equals("출금")){
                    radio_insert.setChecked(false);
                    radio_out.setChecked(true);
                }

                financial_dialog_money.setText(data.getMoney());

                financial_dialog_detail.setText(data.getMoney_explain());

                money_type=data.getMoney_type();
                money=data.getMoney();
                money_explain=data.getMoney_explain();
                account_time=data.getAccount_time();


                Log.e("fanfan",data.getAccount_time());


                String a = data.getAccount_time();

                String[] aa = a.split("\\s");
                String[] bb = aa[0].split("\\/");
                String[] cc = aa[1].split("\\:");

                Log.e("fanfan",bb[0]+"ddd"+bb[1]);
                Log.e("fanfan",cc[0]+"ddd"+cc[1]);

                int month_get = Integer.valueOf(bb[0]);
                int day_get=Integer.valueOf(bb[1]);
                int hour_get=Integer.valueOf(cc[0]);
                int minute_get=Integer.valueOf(cc[1]);

                datepicker.init(2019, month_get-1, day_get, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    }
                });
                timepicker.setHour(hour_get);
                timepicker.setMinute(minute_get);


                financial_dialog_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertdialog.dismiss();
                    }
                });





                alertdialog.show();
            }
        });







    }




    public void change(String abc){

        Financial_dialog_DTO financial_dialog_dto = new Financial_dialog_DTO();
        financial_dialog_dto.setFinancial_dialog_picture(abc);
        mArrayList_dialog.add(1,financial_dialog_dto);
        mAdapter_dialog.notifyDataSetChanged();
}


    @Override
    public int getItemCount(){
        return (null !=mRecycler ? mRecycler.size() :0);
    }


    public String getMoney_type(){
        return money_type;
    }
    public String getMoney(){
        return money;
    }
    public String getMoney_explain(){
        return money_explain;
    }
    public String getAccount_time(){
        return account_time;
    }

    private class GetData extends AsyncTask<String, Void, String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);



            if(result==null){

            }
            else{
                mJsonString=result;
                showResult();
            }


        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String master_key = params[1];
            String money_type = params[2];
            String money = params[3];
            String money_explain = params[4];
            String account_time=params[5];


            String postParameters =
                    "master_key=" + master_key +
                            "&money_type=" + money_type +
                            "&money="+money +
                            "&money_explain=" +money_explain +
                            "&account_time=" +account_time;




            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));

                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {



                return null;
            }

        }

    }
    private void showResult(){

        String TAG_JSON="youngseok";
        String TAG_master_key = "master_key";
        String TAG_money_type = "money_type";
        String TAG_money ="money";
        String TAG_money_explain="money_explain";
        String TAG_account_time="account_time";
        String TAG_financial_dialog_picture="financial_dialog_picture";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String master_key = item.getString(TAG_master_key);
                String money_type = item.getString(TAG_money_type);
                String money = item.getString(TAG_money);
                String money_explain = item.getString(TAG_money_explain);
                String account_time = item.getString(TAG_account_time);
                String financial_dialog_picture = item.getString(TAG_financial_dialog_picture);


                Financial_dialog_DTO financial_dialog_dto = new Financial_dialog_DTO();
                String abcc = "http://192.168.43.34/group_content/financial/financial_image/";


                financial_dialog_dto.setMaster_key(master_key);
                financial_dialog_dto.setMoney_type(money_type);
                financial_dialog_dto.setMoney(money);
                financial_dialog_dto.setMoney_explain(money_explain);
                financial_dialog_dto.setAccount_time(account_time);
                financial_dialog_dto.setFinancial_dialog_picture(abcc+financial_dialog_picture);

                mArrayList_dialog.add(1,financial_dialog_dto);
                mAdapter_dialog.notifyDataSetChanged();
            }



        } catch (JSONException e) {

        }

    }
}

