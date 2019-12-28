package com.example.admin.infantvaccination;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Admin on 26-Feb-18.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        String cname=intent.getStringExtra("CNAME").toString();
        String vname=intent.getStringExtra("VNAME").toString();
        String email=intent.getStringExtra("EMAIL").toString().trim();
        String sdate=intent.getStringExtra("SDATE").toString().trim();
        String message=""+cname+" needs vaccine "+vname+" on "+sdate;



        //Sending mail
        SendMail sm = new SendMail(context,email, "Vaccine reminder", message);
        sm.execute();



        //Calling notification services
        Intent myIntent = new Intent(context, NotificationService.class);
        myIntent.putExtra("CNAME",cname);
        myIntent.putExtra("VNAME",vname);
        myIntent.putExtra("EMAIL",vname);
        context.startService(myIntent);




    }
}
