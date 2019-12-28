package com.example.admin.infantvaccination;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class AddVaccineActivity extends AppCompatActivity {

    private Toolbar toolbar;

    EditText etVaccineName;

    Button btnVaccineDate,btnVaccineAdd;

    DatePickerDialog datePickerDialog;

    String cid,vid,vaccinName,vaccineDob;

    int vday,vmonth,vyear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vaccine);

        //ToolBar

        toolbar = findViewById(R.id.addVaccineTB);
        toolbar.setTitle("Add new vaccine");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);


        //View Data

        etVaccineName=findViewById(R.id.etVaccineName);
        btnVaccineDate=findViewById(R.id.btnVaccineGivenDate);
        btnVaccineAdd=findViewById(R.id.btnAddVaccine);


        cid=ChildVaccineActivity.cid.toString();



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(AddVaccineActivity.this,ChildVaccineActivity.class);
                it.putExtra("cid",cid);
                startActivity(it);
                finish();
            }
        });
        btnVaccineDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                //Setting date in edittext


                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                final String todayDate=mDay+"/"+mMonth+"/"+mYear;
                // date picker dialog
                datePickerDialog = new DatePickerDialog(AddVaccineActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text


                                String selectDate=dayOfMonth+"/"+monthOfYear+"/"+year;

                                try {
                                    Date start = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
                                            .parse(todayDate);
                                    Date end = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
                                            .parse(selectDate);

                                    //Comapring dates


                                    String date=dayOfMonth + "/"
                                            + (monthOfYear + 1) + "/" + year;
                                    if (!date.equals("")) {

                                        btnVaccineDate.setText(dayOfMonth + "/"
                                                + (monthOfYear + 1) + "/" + year);
                                        btnVaccineAdd.setEnabled(true);
                                        vday=dayOfMonth;
                                        vmonth=monthOfYear;
                                        vyear=year;


                                    }
                                    else{
                                        btnVaccineAdd.setEnabled(false);

                                        Toast.makeText(AddVaccineActivity.this, "Please select date", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();



                //--------------------------

            }
        });

        btnVaccineAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {



                ArrayList<ListModelClass> arrayList=new ArrayList<ListModelClass>();
                ListModelClass listModelClass=new ListModelClass();
                String vname=etVaccineName.getText().toString();
                String vdate=btnVaccineDate.getText().toString();

                if(!vname.equals("") && !vdate.equals("")){

                    getVaccineId();

                    listModelClass.setLcid(cid);
                    listModelClass.setLvid(vid);
                    listModelClass.setLvname(vname);
                    listModelClass.setLschedule(vdate);
                    listModelClass.setLgiven("NULL");
                    listModelClass.setLstatus("NOT GIVEN");
                    listModelClass.setLhospital("NULL");
                    arrayList.add(listModelClass);

                    int res=MainActivity.sqLiteHelper.insertList(arrayList);
                    if(res>0){
                        Toast.makeText(AddVaccineActivity.this, "New Vaccine is added", Toast.LENGTH_SHORT).show();
                        Intent it=new Intent(AddVaccineActivity.this,ChildVaccineActivity.class);
                        it.putExtra("cid",cid);
                        startActivity(it);
                        finish();
                        vaccinName=vname;
                        vaccineDob=vdate;
                        vid="";
                        creatingNotifications();

                    }
                    else{

                        Toast.makeText(AddVaccineActivity.this, "Vaccine not added", Toast.LENGTH_SHORT).show();
                    }


                }
                else{
                    Toast.makeText(AddVaccineActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void getVaccineId() {

        ArrayList<ListModelClass> countList;
        countList=MainActivity.sqLiteHelper.gettingChildVaccineData(cid,"1");
        int count=0;
        count=countList.size();
        count=count+1;
        vid=count+"";
       // Toast.makeText(this, "id: "+cid+" vno: "+vid+"", Toast.LENGTH_SHORT).show();

    }
    public void creatingNotifications(){


       AddChild addChild=MainActivity.sqLiteHelper.getSingleChild(cid);


            Random random=new Random();
            int rn=random.nextInt(1000);



            Calendar Calendar_Object = Calendar.getInstance();
            Calendar_Object.set(Calendar.MONTH,vmonth);
            Calendar_Object.set(Calendar.YEAR, vyear);
            Calendar_Object.set(Calendar.DAY_OF_MONTH, vday);
            Calendar_Object.set(Calendar.HOUR_OF_DAY, 0);
            Calendar_Object.set(Calendar.MINUTE, 0);
            Calendar_Object.set(Calendar.SECOND, 0);
            Intent myIntent = new Intent(AddVaccineActivity.this, AlarmReceiver.class);
            myIntent.putExtra("CNAME",addChild.getName());
            myIntent.putExtra("VNAME",vaccinName);
            myIntent.putExtra("EMAIL",addChild.getName().trim());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(AddVaccineActivity.this, rn, myIntent, 0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


            alarmManager.set(AlarmManager.RTC_WAKEUP, Calendar_Object.getTimeInMillis(),
                    pendingIntent);
            //Toast.makeText(AddChildActivity.this, "Alarm set for "+cday+"-"+cmonth+"-"+cyear, Toast.LENGTH_SHORT).show();



    }

    @Override
    public void onBackPressed() {
        Intent it=new Intent(AddVaccineActivity.this,ChildVaccineActivity.class);
        it.putExtra("cid",cid);
        startActivity(it);
        finish();
    }
}
