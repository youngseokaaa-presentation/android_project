package com.example.youngseok.myapplication.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class Add_member_Broad extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent in = new Intent(context, Add_member_Service_restart.class);
            context.startForegroundService(in);
        } else {
            Intent in = new Intent(context, Add_member_Service.class);
            context.startService(in);
        }



    }
}
