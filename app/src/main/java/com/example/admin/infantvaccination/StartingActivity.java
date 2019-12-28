package com.example.admin.infantvaccination;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class StartingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        showIcon();

    }
    public void showIcon(){

        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {

                Intent it=new Intent(StartingActivity.this,LoginActivity.class);
                startActivity(it);
                finish();
            }
        }, 3000);

    }
}
