package com.example.admin.infantvaccination;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnReg;
    private EditText etName,etEmail,etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //ToolBar

        toolbar = findViewById(R.id.registerActTB);
        toolbar.setTitle("REGISTER");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);


        etName=findViewById(R.id.etRegisterName);
        etEmail=findViewById(R.id.etRegisterEmail);
        etPassword=findViewById(R.id.etRegisterPassword);

        btnReg=findViewById(R.id.btnRegister);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String name=etName.getText().toString();
                String email=etEmail.getText().toString();
                String password=etPassword.getText().toString();

                if(!name.equals("") && !email.equals("") && !TextUtils.isEmpty(password)){

                    if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                        if(password.length()>=8){

                            UserModelClass umc=new UserModelClass();
                            umc.setNAME(name);
                            umc.setEMAIL(email);
                            umc.setPASSWORD(password);
                            String res=LoginActivity.sqLiteHelper.insertUserDet(umc,"normal");

                            if(res.equals("euser")){

                                Toast.makeText(RegisterActivity.this, "You already have account", Toast.LENGTH_SHORT).show();
                            }
                            else if(res.equals("insert")){
                                Toast.makeText(RegisterActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                            else if(res.equals("not insert")){
                                Toast.makeText(RegisterActivity.this, "Error not registered", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(RegisterActivity.this, "Password length should be atleast 8", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "Email correct email address", Toast.LENGTH_SHORT).show();
                    }

                }
                else{

                    Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

}
