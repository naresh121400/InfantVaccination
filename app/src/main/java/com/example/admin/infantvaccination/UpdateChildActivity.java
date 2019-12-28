package com.example.admin.infantvaccination;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class UpdateChildActivity extends AppCompatActivity {

    private Toolbar toolbar;
    Button btnDelete,btnEdit;
    String cid="",mode="";
    String result="";
    EditText etChildName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_child);

        //ToolBar

        toolbar = findViewById(R.id.updateChildTB);
        toolbar.setTitle("Update Child Details");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);

        btnDelete=findViewById(R.id.btnChildDelete);
        btnEdit=findViewById(R.id.btnChildEdit);
        etChildName=findViewById(R.id.etUpdateChildName);

        cid=getIntent().getStringExtra("cid").toString();
        mode=getIntent().getStringExtra("mode").toString();
        if(mode.equals("1")){
            btnDelete.setEnabled(false);
            btnEdit.setEnabled(true);
            etChildName.setEnabled(true);
        }
        else if(mode.equals("2")){
            btnDelete.setEnabled(true);
            btnEdit.setEnabled(false);
            etChildName.setEnabled(false);
        }


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateChildActivity.this, MainActivity.class);
                intent.putExtra("uid",MainActivity.uid.toString());
                intent.putExtra("type",MainActivity.type.toString());
                startActivity(intent);
                finish();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //-------------------------Alert dialog for deleting---------------

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateChildActivity.this);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Confirmation");
                alertDialog.setMessage("Are you sure to delete child?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        ArrayList<ListModelClass> getVaccineList;
                        getVaccineList=MainActivity.sqLiteHelper.gettingChildVaccineData(cid+"","1");
                        AddChild addChild;
                        addChild=MainActivity.sqLiteHelper.getSingleChild(cid);
                        ArrayList<NotificationsModelClass> notificationsList=MainActivity.sqLiteHelper.getNotificationData(cid);
                        ListModelClass lmc;
                        NotificationsModelClass nmc;
                        for(int i=0;i<notificationsList.size();i++){

                            lmc=getVaccineList.get(i);
                            nmc=notificationsList.get(i);


                            Intent myIntent = new Intent(UpdateChildActivity.this, AlarmReceiver.class);
                            myIntent.putExtra("CNAME",addChild.getName());
                            myIntent.putExtra("VNAME",lmc.getLvname());
                            myIntent.putExtra("EMAIL",addChild.getEmail());
                            myIntent.putExtra("SDATE",lmc.getLschedule());
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(UpdateChildActivity.this, Integer.parseInt(nmc.getNotificationId()), myIntent, 0);

                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            alarmManager.cancel(pendingIntent);

                        }


                        //delete code

                        result=MainActivity.sqLiteHelper.deleteChild(cid);
                        dialog.cancel();
                        if(result.equals("success")){

                            Toast.makeText(UpdateChildActivity.this, "Child deleted successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UpdateChildActivity.this, MainActivity.class);
                            intent.putExtra("uid",MainActivity.uid.toString());
                            intent.putExtra("type",MainActivity.type.toString());
                            startActivity(intent);
                            finish();
                        }
                        else if(result.equals("failure")){
                            Toast.makeText(UpdateChildActivity.this, "Error in deleting ,Try again", Toast.LENGTH_SHORT).show();
                        }



                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
                alertDialog.show();



            }
        });


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=etChildName.getText().toString();
                if(!TextUtils.isEmpty(name)){
                    int res=MainActivity.sqLiteHelper.updateChildName(cid,name);
                    if(res>0){
                        Toast.makeText(UpdateChildActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateChildActivity.this, MainActivity.class);
                        intent.putExtra("uid",MainActivity.uid.toString());
                        intent.putExtra("type",MainActivity.type.toString());
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(UpdateChildActivity.this, "Not updated try again", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    etChildName.setError("Please fill name");
                }
            }
        });
    }
}
