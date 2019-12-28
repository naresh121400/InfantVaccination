package com.example.admin.infantvaccination;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Random;

/**
 * Created by Admin on 26-Feb-18.
 */

public class NotificationService  extends Service {

    private NotificationManager mManager;
    static  int NotificationID;
    String cname="",vname="",email="";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

         cname=intent.getStringExtra("CNAME").toString();
         vname=intent.getStringExtra("VNAME").toString();
         email=intent.getStringExtra("EMAIL").toString();
        Random random=new Random();
        mManager = (NotificationManager) this.getApplicationContext()
                .getSystemService(
                        this.getApplicationContext().NOTIFICATION_SERVICE);


        Intent intent1 = new Intent(this.getApplicationContext(), MapsActivity.class);

        Notification notification = new Notification(R.drawable.wright,
               "Vaccine Reminder", System.currentTimeMillis());

        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        NotificationID = random.nextInt(1000);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(
                this.getApplicationContext(), NotificationID, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        //------------------------------


        Notification.Builder builder = new Notification.Builder(this.getApplicationContext());
        builder.setTicker("this is ticker text");
        builder.setContentTitle("Infant Vaccination notification");
        builder.setContentText("Notification for "+cname);
        builder.setSmallIcon(R.drawable.wright);
        builder.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.mipmap.launcher));
        builder.setContentIntent(pendingNotificationIntent);
        builder.setOngoing(false);
        builder.setSubText(cname+" needs vaccine: "+vname);
        builder.setAutoCancel(true);
        builder.setVisibility(Notification.VISIBILITY_PRIVATE);
        builder.build();
        notification = builder.getNotification();

        mManager.notify(NotificationID, notification);


    }



    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
