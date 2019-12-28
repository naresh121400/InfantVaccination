package com.example.admin.infantvaccination;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class ChildVaccineActivity extends AppCompatActivity {

    static String cid;
    private Toolbar toolbar;
    private TextView tvStatus;
    RecyclerView recList;

    ArrayList<ListModelClass> listModelClassArrayList;

    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_vaccine);

        //ToolBar

        toolbar = findViewById(R.id.childVaccineTB);
        toolbar.setTitle("Child Vaccine Details");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);

        tvStatus=findViewById(R.id.childTvStatus);

        cid = getIntent().getStringExtra("cid").toString();

        //Recycler View

        recList = findViewById(R.id.cardlist);
        listModelClassArrayList = MainActivity.sqLiteHelper.gettingChildVaccineData(cid,"1");

        recList.setHasFixedSize(true);
        recList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CardChildDataAdapter(listModelClassArrayList);
        recList.setAdapter(mAdapter);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOptions();
            }
        });


    }

    private void showOptions() {

        CharSequence colors[] = new CharSequence[] {"All list","Completed list", "uncompleted list"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ChildVaccineActivity.this);
        builder.setTitle("Options");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==1){

                    listModelClassArrayList = MainActivity.sqLiteHelper.gettingChildVaccineData(cid,"2");

                    recList.setHasFixedSize(true);
                    recList.setLayoutManager(new LinearLayoutManager(ChildVaccineActivity.this));
                    mAdapter = new CardChildDataAdapter(listModelClassArrayList);
                    recList.setAdapter(mAdapter);
                }
                else if(which==2){
                    listModelClassArrayList = MainActivity.sqLiteHelper.gettingChildVaccineData(cid,"3");

                    recList.setHasFixedSize(true);
                    recList.setLayoutManager(new LinearLayoutManager(ChildVaccineActivity.this));
                    mAdapter = new CardChildDataAdapter(listModelClassArrayList);
                    recList.setAdapter(mAdapter);
                }
                else if(which==0){
                    listModelClassArrayList = MainActivity.sqLiteHelper.gettingChildVaccineData(cid,"1");

                    recList.setHasFixedSize(true);
                    recList.setLayoutManager(new LinearLayoutManager(ChildVaccineActivity.this));
                    mAdapter = new CardChildDataAdapter(listModelClassArrayList);
                    recList.setAdapter(mAdapter);
                }
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.child_vaccine_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.menuAddVaccine:
                Intent intent=new Intent(ChildVaccineActivity.this,AddVaccineActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.menuNearBy:
                Intent it=new Intent(ChildVaccineActivity.this,MapsActivity.class);
                startActivity(it);
                break;
            case R.id.menuAddEvents:
                Intent it1=new Intent(ChildVaccineActivity.this,CreateCalendarEventsActivity.class);
                it1.putExtra("cid",cid);
                it1.putExtra("sclass","childvaccine");
                it1.putExtra("vid","all");
                startActivity(it1);
                break;
            case R.id.menuSettings:
                Intent intent1=new Intent(ChildVaccineActivity.this,SettingsActivity.class);
                intent1.putExtra("cid",cid);
                startActivity(intent1);
                break;

        }

        return true;
    }
}
