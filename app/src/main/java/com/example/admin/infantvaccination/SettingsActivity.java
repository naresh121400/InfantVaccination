package com.example.admin.infantvaccination;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    String cid="";
    Switch switchNotification;
    ArrayList<ListModelClass> getVaccineList;
    AddChild addChild;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //ToolBar

        toolbar = findViewById(R.id.settingsTB);
        toolbar.setTitle("Settings");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);

        switchNotification=findViewById(R.id.switchNotification);

        cid=getIntent().getStringExtra("cid").toString();

        String res=MainActivity.sqLiteHelper.getNotificationStatus(cid);
        if(res.trim().equals("yes")){
            switchNotification.setChecked(true);
        }
        else if(res.trim().equals("no")){
            switchNotification.setChecked(false);
        }

        getrequireDetails();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });
        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b==true){
                    restartNotification();

                }
                else if(b==false){
                    stopNotifications();

                }

            }
        });


    }


    @Override
    public void onBackPressed() {
        finish();
    }
    public void stopNotifications(){

        ArrayList<NotificationsModelClass> notificationsList=MainActivity.sqLiteHelper.getNotificationData(cid);
        ListModelClass lmc;
        NotificationsModelClass nmc;
        for(int i=0;i<notificationsList.size();i++){

            lmc=getVaccineList.get(i);
            nmc=notificationsList.get(i);


            Intent myIntent = new Intent(SettingsActivity.this, AlarmReceiver.class);
            myIntent.putExtra("CNAME",addChild.getName());
            myIntent.putExtra("VNAME",lmc.getLvname());
            myIntent.putExtra("EMAIL",addChild.getEmail());
            myIntent.putExtra("SDATE",lmc.getLschedule());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, Integer.parseInt(nmc.getNotificationId()), myIntent, 0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);

        }
        int k=MainActivity.sqLiteHelper.setNotificationStatus(cid,"no");
        if(k>0){
            Toast.makeText(this, "Notifications Off", Toast.LENGTH_SHORT).show();
        }

    }
    public void restartNotification(){

        ArrayList<NotificationsModelClass> notificationsList=MainActivity.sqLiteHelper.getNotificationData(cid);
        ListModelClass lmc;
        NotificationsModelClass nmc;
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        final String todayDate=mDay+"/"+mMonth+"/"+mYear;
        String scheduleDate="";
        for(int i=0;i<notificationsList.size();i++){

            lmc=getVaccineList.get(i);
            nmc=notificationsList.get(i);
            String dates[]=lmc.getLschedule().split("/");
            int month=Integer.parseInt(dates[1]);
            month=month-1;
            scheduleDate=dates[0]+"/"+month+"/"+dates[2];

            try {
                Date start = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                        .parse(todayDate);
                Date end = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                        .parse(scheduleDate);

                if(end.after(start)){

                    Intent myIntent = new Intent(SettingsActivity.this, AlarmReceiver.class);
                    myIntent.putExtra("CNAME",addChild.getName());
                    myIntent.putExtra("VNAME",lmc.getLvname());
                    myIntent.putExtra("EMAIL",addChild.getEmail());
                    myIntent.putExtra("SDATE",lmc.getLschedule());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, Integer.parseInt(nmc.getNotificationId()), myIntent, 0);

                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    long date=Long.parseLong(nmc.getNotificationDate().trim());


                    alarmManager.set(AlarmManager.RTC_WAKEUP, date, pendingIntent);

                }
            }catch (ParseException e){
                e.printStackTrace();
            }




        }
        int k=MainActivity.sqLiteHelper.setNotificationStatus(cid,"yes");
        if(k>0){
            Toast.makeText(this, "Notifications On", Toast.LENGTH_SHORT).show();
        }



    }


    //Getting child and vaccine details
    public void getrequireDetails(){

        getVaccineList=MainActivity.sqLiteHelper.gettingChildVaccineData(cid+"","1");
        addChild=new AddChild();
        addChild=MainActivity.sqLiteHelper.getSingleChild(cid);

    }
}
