package com.example.admin.infantvaccination;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePassword extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText etChangePass;
    private Button btnChangePass;
    String email="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //ToolBar

        toolbar = findViewById(R.id.changePassTB);
        toolbar.setTitle("Add new child");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);



        etChangePass=findViewById(R.id.etChangePass);
        btnChangePass=findViewById(R.id.btnChangePass);

        email=getIntent().getStringExtra("email").toString().trim();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass=etChangePass.getText().toString();
                int res=LoginActivity.sqLiteHelper.changePassword(email,pass);

                if(res>0){
                    Toast.makeText(ChangePassword.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(ChangePassword.this, "Try again,Error in changing password", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
