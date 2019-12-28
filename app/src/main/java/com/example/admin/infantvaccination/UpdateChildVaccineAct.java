package com.example.admin.infantvaccination;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class UpdateChildVaccineAct extends AppCompatActivity {


    private Toolbar toolbar;
    private TextView tvVname,tvVschedule,tvStatus;
    private Button btnGiven,btnUpdate,btnRemove;
    private EditText etHospital;

    String vname,vschedule,vid,cid,lvid;

    DatePickerDialog datePickerDialog;

    ListModelClass lmc;

    ImageView imgPlacePicker;

    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_child_vaccine);



        //ToolBar

        toolbar = findViewById(R.id.updateChildVaccineTB);
        toolbar.setTitle("Update Vaccine Details");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);


        //Text view
        vid=getIntent().getStringExtra("vid").toString();
        vname=getIntent().getStringExtra("vname").toString();
        vschedule=getIntent().getStringExtra("vschedule").toString();
        cid=ChildVaccineActivity.cid.toString();

        tvVname=findViewById(R.id.tvChildVname);
        tvVschedule=findViewById(R.id.tvChildVschedule);
        tvStatus=findViewById(R.id.tvChildVStatus);
       // tvVname.setText("VACCINE NAME: "+vname);
      //  tvVschedule.setText("VACCINE SCHEDULE DATE: "+vschedule);


        //Edit text


        etHospital=findViewById(R.id.etChildHospital);



        //Button

        btnGiven=findViewById(R.id.btnChildGiven);
        btnUpdate=findViewById(R.id.btnVaccineUpdate);
        btnRemove=findViewById(R.id.btnVaccineRemove);

        //Image
        imgPlacePicker=findViewById(R.id.imgPlacePicker);


        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();



        lmc=MainActivity.sqLiteHelper.gettingChildVaccineDataWithLid(vid);

        settingDetails();




        btnGiven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                String dates[]=vschedule.split("/");
                int k=Integer.parseInt(dates[1]);
                k=k-1;
                final String todayDate=dates[0]+"/"+k+"/"+dates[2];
                // date picker dialog
                datePickerDialog = new DatePickerDialog(UpdateChildVaccineAct.this,
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

                                     if(start.equals(end)){

                                         btnGiven.setText(dayOfMonth + "/"
                                                 + (monthOfYear + 1) + "/" + year);
                                         btnUpdate.setEnabled(true);

                                    }

                                     else if(start.before(end)){


                                         btnGiven.setText(dayOfMonth + "/"
                                                 + (monthOfYear + 1) + "/" + year);
                                         btnUpdate.setEnabled(true);



                                     }

                                     else if(start.after(end)){

                                         btnUpdate.setEnabled(false);

                                         Toast.makeText(UpdateChildVaccineAct.this, "Select correct date", Toast.LENGTH_SHORT).show();




                                     }



                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();



            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String hospital=etHospital.getText().toString();
                String givenDate=btnGiven.getText().toString();

                if(!hospital.equals("") && !givenDate.equals("")){

                   ListModelClass lmc=new ListModelClass();

                    lmc.setLid(vid);
                    lmc.setLgiven(givenDate);
                    lmc.setLstatus("GIVEN");
                    lmc.setLhospital(hospital);

                    int res = MainActivity.sqLiteHelper.updateVaccineData(lmc);

                    if(res>0){
                        Toast.makeText(UpdateChildVaccineAct.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                        Intent it=new Intent(UpdateChildVaccineAct.this,ChildVaccineActivity.class);
                        it.putExtra("cid",cid);
                        startActivity(it);
                        finish();
                    }

                    //Toast.makeText(UpdateChildVaccineAct.this,vid, Toast.LENGTH_SHORT).show();
                }
                else{

                    Toast.makeText(UpdateChildVaccineAct.this, "Please fill all details", Toast.LENGTH_SHORT).show();

                }


            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(UpdateChildVaccineAct.this,ChildVaccineActivity.class);
                it.putExtra("cid",cid);
                startActivity(it);
                finish();
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                 lvid=MainActivity.sqLiteHelper.getLvidForDelete(vid);
                 //Toast.makeText(UpdateChildVaccineAct.this, lvid, Toast.LENGTH_SHORT).show();

                //-------------------------Alert dialog for deleting---------------

               AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateChildVaccineAct.this);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Confirmation");
                alertDialog.setMessage("Are you sure to delete item?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {



                        //delete code



                        int result = MainActivity.sqLiteHelper.deleteSingleVaccine(cid,lvid);
                        lvid="";

                        if(result>0){

                            Toast.makeText(UpdateChildVaccineAct.this, "Vaccine removed from list", Toast.LENGTH_SHORT).show();
                            Intent it=new Intent(UpdateChildVaccineAct.this,ChildVaccineActivity.class);
                            it.putExtra("cid",cid);
                            startActivity(it);
                            finish();

                        }
                        else{
                            Toast.makeText(UpdateChildVaccineAct.this, "Vaccine not removed,Try again", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                    }
                });
                alertDialog.show();

                //-------------------------------




            }
        });

        imgPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(UpdateChildVaccineAct.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void settingDetails() {

        String status=lmc.getLstatus().toString();
        if(status.equals("GIVEN")){

            tvStatus.setText("This vaccine is COMPLETED");
            tvVname.setText("Vaccine Name:"+lmc.getLvname());
            tvVschedule.setText("Vaccine Scheduled:"+lmc.getLschedule());
            btnGiven.setText(lmc.getLgiven());
            etHospital.setText(lmc.getLhospital());
        }
        else{
            tvStatus.setText("This vaccine is PENDING");
            tvVname.setText("Vaccine Name:"+lmc.getLvname());
            tvVschedule.setText("Vaccine Scheduled:"+lmc.getLschedule());
        }


    }

    @Override
    public void onBackPressed() {

        Intent it=new Intent(UpdateChildVaccineAct.this,ChildVaccineActivity.class);
        it.putExtra("cid",cid);
        startActivity(it);
        finish();

    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                String address = String.format("%s", place.getAddress());
                stBuilder.append(placename);
                stBuilder.append(placename);
                stBuilder.append("\n");
                stBuilder.append(address);
                etHospital.setText(stBuilder.toString());
            }
        }
    }
}
