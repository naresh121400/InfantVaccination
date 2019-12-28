package com.example.admin.infantvaccination;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    TextView tvNoRecords;
    static SQLiteHelper sqLiteHelper;

    private RecyclerView.Adapter mAdapter;
    private ArrayList<AddChild> childArrayList;

    public static String uid="",type="";
    public  GoogleApiClient mGoogleApiClient;
    RecyclerView recList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();


        //ToolBar

        toolbar = findViewById(R.id.mainTB);
        toolbar.setTitle("Infant Vaccination");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);



        //Database
        sqLiteHelper=new SQLiteHelper(this);


        uid=getIntent().getStringExtra("uid").toString();
        type=getIntent().getStringExtra("type").toString();






        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //-------------------------Alert dialog for deleting---------------

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Confirmation");
                alertDialog.setMessage("Do you want to logout?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if(type.equals("normal")){
                            Intent it=new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(it);
                            finish();
                        }
                        else if(type.equals("gmail")){
                            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                    new ResultCallback<Status>() {
                                        @Override
                                        public void onResult(Status status) {
                                            //updateUI(false);
                                        }
                                    });
                            Intent it=new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(it);
                            finish();
                        }
                        else if(type.equals("facebook")){
                            LoginManager.getInstance().logOut();
                            Intent it=new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(it);
                            finish();
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


        //Recycler View

        tvNoRecords=findViewById(R.id.tvRecords);
        recList = findViewById(R.id.cardlist);
        loadChildData();



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        if(type.equals("facebook")){
            menu.findItem(R.id.action_change_password).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.action_add_child:
                Intent it=new Intent(MainActivity.this,AddChildActivity.class);
                startActivity(it);
                finish();
                break;
            case R.id.action_change_password:
                getMail();
                break;
            case R.id.action_logout:
                logout();
                break;

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //-------------------------Alert dialog for deleting---------------

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Confirmation");
        alertDialog.setMessage("Do you want to logout?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if(type.equals("normal")){
                    Intent it=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(it);
                    finish();
                }
                else if(type.equals("gmail")){
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    //updateUI(false);
                                }
                            });
                    Intent it=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(it);
                    finish();
                }
                else if(type.equals("facebook")){
                    LoginManager.getInstance().logOut();
                    Intent it=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(it);
                    finish();
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
    public void logout(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Confirmation");
        alertDialog.setMessage("Do you want to logout?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                if(type.equals("normal")){
                    Intent it=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(it);
                    finish();
                }
                else if(type.equals("gmail")){
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    //updateUI(false);
                                }
                            });
                    Intent it=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(it);
                    finish();
                }
                else if(type.equals("facebook")){
                    LoginManager.getInstance().logOut();
                    Intent it=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(it);
                    finish();
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

    public void getMail(){

        String email=sqLiteHelper.userMail(uid);
        
        if(email.equals("fail")){
            Toast.makeText(this, "try again", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent it=new Intent(MainActivity.this,ChangePassword.class);
            it.putExtra("email",email);
            startActivity(it);
        }

    }
    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }
    public  void loadChildData(){

        childArrayList = sqLiteHelper.fetchChildList(uid);
        if(childArrayList.size()>0){

            tvNoRecords.setVisibility(View.INVISIBLE);
            recList.setHasFixedSize(true);
            recList.setLayoutManager(new LinearLayoutManager(this));
            mAdapter = new CardViewDataAdapter(childArrayList);
            mAdapter.notifyDataSetChanged();
            recList.setAdapter(mAdapter);
        }
        else{

            tvNoRecords.setVisibility(View.VISIBLE);
        }

    }
}
