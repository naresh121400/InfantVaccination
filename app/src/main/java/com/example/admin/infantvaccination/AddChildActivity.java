package com.example.admin.infantvaccination;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
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

public class AddChildActivity extends AppCompatActivity {


    private Toolbar toolbar;

    private String type;

    private EditText etName,etEmail,etPhone;

    private Button btnSubmit,etDob;

    DatePickerDialog datePickerDialog;

    int ChildId=0;
    String childName="",childEmail="";

    ArrayList<String> vaccineDays;
    ArrayList<String> scheduleDays;
    ArrayList<GettingVaccineData> vaccineData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        //ToolBar

        toolbar = findViewById(R.id.addChildTB);
        toolbar.setTitle("Add new child");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);



        //Edit text

        etName=findViewById(R.id.etChildName);
        etDob=findViewById(R.id.etChildDob);
        etEmail=findViewById(R.id.etChildEmail);
        etPhone=findViewById(R.id.etChildPhone);


        //Button

        btnSubmit=findViewById(R.id.btnSubmit);


        //Vaccine table details

        getVaccineData();


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddChildActivity.this, MainActivity.class);
                intent.putExtra("uid",MainActivity.uid.toString());
                intent.putExtra("type",MainActivity.type.toString());
                startActivity(intent);
                finish();
            }
        });
        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Setting date in edittext


                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                final String todayDate=mDay+"/"+mMonth+"/"+mYear;
                // date picker dialog
                datePickerDialog = new DatePickerDialog(AddChildActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text


                                String selectDate=dayOfMonth+"/"+monthOfYear+"/"+year;

                                try {
                                    Date start = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                                            .parse(todayDate);
                                    Date end = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                                            .parse(selectDate);

                                    //Comapring dates

                                    if (end.after(start)) {


                                        btnSubmit.setText("Select Date");
                                        btnSubmit.setEnabled(false);
                                        Toast.makeText(AddChildActivity.this, "Select correct date", Toast.LENGTH_SHORT).show();


                                    }
                                    else if(end.before(start)){

                                        etDob.setText(dayOfMonth + "/"
                                                + (monthOfYear + 1) + "/" + year);
                                        btnSubmit.setEnabled(true);

                                        createDateSchedule();

                                    }
                                    else if(start.equals(end)){

                                        etDob.setText(dayOfMonth + "/"
                                                + (monthOfYear + 1) + "/" + year);
                                        btnSubmit.setEnabled(true);

                                        createDateSchedule();

                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();





           //--------------
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = etName.getText().toString();
                String dob = etDob.getText().toString();
                String email = etEmail.getText().toString().trim();
                String phone = etPhone.getText().toString();

                String reminder = "yes";
                if (!name.equals("") && !dob.equals("") && !email.equals("") && !phone.equals("")) {


                    if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                        if(Patterns.PHONE.matcher(phone).matches() && phone.length()==10){


                            AddChild addChild = new AddChild();
                            addChild.setName(name);
                            addChild.setDob(dob);
                            addChild.setEmail(email);
                            addChild.setPhone(phone);
                            addChild.setReminder(reminder);
                            addChild.setUid(MainActivity.uid);
                            addChild.setEstatus("no");
                            int res = MainActivity.sqLiteHelper.addNewChild(addChild);

                            ListModelClass listModelClass = new ListModelClass();
                            GettingVaccineData gettingVaccineData;
                            ArrayList<ListModelClass> arrayList = new ArrayList<ListModelClass>();
                            if (res > 0) {

                                ChildId=res;
                                childName = name;
                                childEmail=email;
                                for (int i = 0; i < vaccineDays.size(); i++) {

                                    listModelClass = new ListModelClass();
                                    gettingVaccineData = vaccineData.get(i);
                                    listModelClass.setLcid(res + "");
                                    listModelClass.setLvid(gettingVaccineData.getID());
                                    listModelClass.setLvname(gettingVaccineData.getNAME());
                                    listModelClass.setLschedule(scheduleDays.get(i));
                                    listModelClass.setLgiven("NULL");
                                    listModelClass.setLstatus("NOT GIVEN");
                                    listModelClass.setLhospital("NULL");
                                    arrayList.add(listModelClass);

                                }
                                int result = MainActivity.sqLiteHelper.insertList(arrayList);

                                if (result > 0) {

                                    Toast.makeText(AddChildActivity.this, "Created Successfully", Toast.LENGTH_SHORT).show();




                                            Intent intent = new Intent(AddChildActivity.this, MainActivity.class);
                                            intent.putExtra("uid",MainActivity.uid.toString());
                                            intent.putExtra("type",MainActivity.type.toString());
                                            startActivity(intent);
                                            finish();

                                            vaccineDays.clear();
                                            vaccineData.clear();

                                            creatingNotifications();






                                } else {
                                    Toast.makeText(AddChildActivity.this, "not inserted", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(AddChildActivity.this, "not inserted", Toast.LENGTH_SHORT).show();
                            }



                        }
                        else{

                            Toast.makeText(AddChildActivity.this, "Number should be ten digits", Toast.LENGTH_SHORT).show();
                        }



                    }
                    else {
                        Toast.makeText(AddChildActivity.this, "Email format is not valid", Toast.LENGTH_SHORT).show();
                    }



                }
                else {
                    Toast.makeText(AddChildActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }

            }

        });

    }


    private void getVaccineData() {

        vaccineDays=new ArrayList<String>();
        vaccineData = MainActivity.sqLiteHelper.getVaccineDetails();
        GettingVaccineData gd;
        String dates[]=new String[vaccineData.size()];
        if(vaccineData.size()>0){

            for(int i=0;i<vaccineData.size();i++){
                gd=vaccineData.get(i);
                vaccineDays.add(gd.getDAYS().toString());
            }
        }


    }

    public void createDateSchedule(){

        scheduleDays=new ArrayList<String>();
        String oldDate=etDob.getText().toString();



        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Calendar c = Calendar.getInstance();


        for(int i=0;i<vaccineDays.size();i++){
        try{
            c.setTime(sdf.parse(oldDate));
        }catch(ParseException e){
            e.printStackTrace();
        }



            c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(vaccineDays.get(i).toString()));

            String newDate = sdf.format(c.getTime());
            scheduleDays.add(newDate);


        }



    }


    public void creatingNotifications(){

        ArrayList<NotificationsModelClass> notificationsList=new ArrayList<NotificationsModelClass>();
        NotificationsModelClass nmc;
       // notificationIds=new ArrayList<String>();
        ArrayList<ListModelClass> getList;

        ListModelClass lmc;
        long notificationDate;
        getList=MainActivity.sqLiteHelper.gettingChildVaccineData(ChildId+"","1");

        for(int i=0;i<scheduleDays.size();i++){

            nmc=new NotificationsModelClass();
            lmc=getList.get(i);
            String vName=lmc.getLvname().toString();


            String date=scheduleDays.get(i);
            int cday=Integer.parseInt(date.substring(0,2));
            int cmonth=Integer.parseInt(date.substring(3,5));
            cmonth=cmonth-1;
            int cyear=Integer.parseInt(date.substring(6,date.length()));

            Random random=new Random();
            int rn=random.nextInt(100000);
            //notificationIds.add(rn+"");
            nmc.setCid(ChildId+"");
            nmc.setNotificationId(rn+"");


            Calendar Calendar_Object = Calendar.getInstance();
            Calendar_Object.set(Calendar.MONTH,cmonth);
            Calendar_Object.set(Calendar.YEAR, cyear);
            Calendar_Object.set(Calendar.DAY_OF_MONTH, cday);
            Calendar_Object.set(Calendar.HOUR_OF_DAY, 0);
            Calendar_Object.set(Calendar.MINUTE, 0);
            Calendar_Object.set(Calendar.SECOND, 0);
            Intent myIntent = new Intent(AddChildActivity.this, AlarmReceiver.class);
            myIntent.putExtra("CNAME",childName);
            myIntent.putExtra("VNAME",vName);
            myIntent.putExtra("EMAIL",childEmail);
            myIntent.putExtra("SDATE",scheduleDays.get(i).toString());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(AddChildActivity.this, rn, myIntent, 0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            notificationDate=Calendar_Object.getTimeInMillis();
            nmc.setNotificationDate(notificationDate+"");

            alarmManager.set(AlarmManager.RTC_WAKEUP, notificationDate,
                    pendingIntent);
            //Toast.makeText(AddChildActivity.this, "Alarm set for "+cday+"-"+cmonth+"-"+cyear, Toast.LENGTH_SHORT).show();


            notificationsList.add(nmc);

        }


        int k=MainActivity.sqLiteHelper.storeNotificationData(notificationsList);
        if(k>0){
                notificationsList.clear();
        }
        else{
           // MainActivity.sqLiteHelper.storeNotificationData(notificationsList);
        }
        scheduleDays.clear();


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddChildActivity.this, MainActivity.class);
        intent.putExtra("uid",MainActivity.uid.toString());
        intent.putExtra("type",MainActivity.type.toString());
        startActivity(intent);
        finish();
    }
}
