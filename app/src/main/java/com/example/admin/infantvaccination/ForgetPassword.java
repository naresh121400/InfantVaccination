package com.example.admin.infantvaccination;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ForgetPassword extends AppCompatActivity {

    private Toolbar toolbar;

    private EditText etEmail;
    private Button btnSendOtp;

    String sentOtp="";
    String message="";
    String email="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        //ToolBar

        toolbar = findViewById(R.id.forgetActTB);
        toolbar.setTitle("Forget Password");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);


        etEmail=findViewById(R.id.etForgetEmail);
        btnSendOtp=findViewById(R.id.btnSendOtp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 email=etEmail.getText().toString().trim();

                if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                    String res=LoginActivity.sqLiteHelper.checkExist(email);

                    if(res.equals("exist")){
                        Random r = new Random();
                        int numbers = 100000 + (int)(r.nextFloat() * 899900);
                        message="OTP:"+numbers+""+"\n"+"NOTE:It works only within a minute.";
                        sentOtp=""+numbers;


                        SendMail sm = new SendMail(ForgetPassword.this,email, "OTP for change password", message);
                        //Executing sendmail to send email
                        sm.execute();
                        Toast.makeText(ForgetPassword.this, "OTP sent to mail", Toast.LENGTH_SHORT).show();

                        //-----------------------------------


                        LayoutInflater inflater = getLayoutInflater();
                        final View alertLayout = inflater.inflate(R.layout.otp_verification, null);

                        final AlertDialog.Builder alert = new AlertDialog.Builder(ForgetPassword.this);
                        // this is set the view from XML inside AlertDialog
                        alert.setView(alertLayout);
                        // disallow cancel of AlertDialog on click of back button and outside touch
                        alert.setCancelable(false);
                        Button btn=alertLayout.findViewById(R.id.btOtp);
                       final  EditText et=alertLayout.findViewById(R.id.etOtp);
                        final AlertDialog dialog = alert.create();
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String otp=et.getText().toString().trim();
                                if(otp.equals(sentOtp)){
                                    sentOtp="";
                                    Intent intent=new Intent(ForgetPassword.this,ChangePassword.class);
                                    intent.putExtra("email",email);
                                    startActivity(intent);
                                    finish();

                                }
                                else{
                                    Toast.makeText(ForgetPassword.this, "Entered incorrect otp", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });

                        dialog.show();


                        final Timer t = new Timer();
                        t.schedule(new TimerTask() {
                            public void run() {
                                sentOtp="";
                                dialog.dismiss(); // when the task active then close the dialog
                                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                            }
                        }, 90000);


                        //-----------------------------------
                    }
                    else{
                        Toast.makeText(ForgetPassword.this, "You are not registered user", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(ForgetPassword.this, "Enter correct email pattern", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
