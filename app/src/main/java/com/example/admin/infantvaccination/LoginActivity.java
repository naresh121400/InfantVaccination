package com.example.admin.infantvaccination;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    static SQLiteHelper sqLiteHelper;
    private EditText etEmail,etPassword;
    private Toolbar toolbar;
    private Button btnLogin;
    SignInButton btnLoginGmail;
    TextView tvReg,tvForget;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;

    public static GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    String gName,gEmail;
    //Facebook variables
    public static String fbId="",fbName="";
    CallbackManager callbackManager;
    LoginButton btnLoginFacebook;
    private String facebook_id,f_name, m_name, l_name, gender, profile_image, full_name, email_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);


        //ToolBar

        toolbar = findViewById(R.id.loginActTB);
        toolbar.setTitle("Login");
        setSupportActionBar(toolbar);


        //Database
        sqLiteHelper=new SQLiteHelper(this);

        callbackManager = CallbackManager.Factory.create();
        btnLoginFacebook=findViewById(R.id.btnLoginActFacebook);
        btnLoginFacebook.setReadPermissions(Arrays.asList("public_profile","email"));

        btnLogin=findViewById(R.id.btnLoginAct);
        btnLoginGmail=findViewById(R.id.btnLoginActGmail);
        tvReg=findViewById(R.id.tvLoginActRegister);
        tvForget=findViewById(R.id.tvLoginForget);
        etEmail=findViewById(R.id.etLoginUname);
        etPassword=findViewById(R.id.etLoginPassword);



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        getKeyHash();

        tvReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(it);
            }
        });

        tvForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it=new Intent(LoginActivity.this,ForgetPassword.class);
                startActivity(it);



            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=etEmail.getText().toString();
                String password=etPassword.getText().toString();
                if(!email.equals("") && !password.equals("")){

                    if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        if(password.length()>=8){

                            String res=LoginActivity.sqLiteHelper.userLogin(email,password);
                            if(res.equals("not register")){
                                Toast.makeText(LoginActivity.this, "Not registerd.Please register first", Toast.LENGTH_SHORT).show();
                            }
                            else if(res.equals("wrong credentials")){
                                Toast.makeText(LoginActivity.this, "Password is wrong", Toast.LENGTH_SHORT).show();
                            }
                            else{

                                //Toast.makeText(LoginActivity.this, res, Toast.LENGTH_SHORT).show();
                                Intent it=new Intent(LoginActivity.this,MainActivity.class);
                                it.putExtra("uid",res);
                                it.putExtra("type","normal");
                                startActivity(it);
                                finish();
                                etEmail.setText("");
                                etPassword.setText("");

                            }

                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Password length should be atleast 8", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Enter Correct email", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLoginGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();

            }
        });
        btnLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFbResult(btnLoginFacebook);

            }
        });


    }

    private void checkFbResult(LoginButton btnLoginFacebook) {



        btnLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            private ProfileTracker mProfileTracker;
            @Override
            public void onSuccess(LoginResult loginResult) {

                    //--------------------------------


                    if(Profile.getCurrentProfile() == null) {

                        mProfileTracker = new ProfileTracker() {
                            @Override
                            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {


                                String res=LoginActivity.sqLiteHelper.checkExistWithFbID(currentProfile.getId());
                                if(res.equals("not register")){

                                    UserModelClass umc=new UserModelClass();
                                    umc.setNAME(currentProfile.getName());
                                    umc.setEMAIL(currentProfile.getId()+"@gmail.com");
                                    umc.setPASSWORD("nareshk@infologitech.in");
                                    umc.setFBID(currentProfile.getId());
                                    String res1=LoginActivity.sqLiteHelper.insertUserDet(umc,"facebook");
                                    if(res1.equals("insert")){
                                        hideProgressDialog();
                                        String res2=LoginActivity.sqLiteHelper.checkExistWithFbID(currentProfile.getId());
                                        Intent it=new Intent(LoginActivity.this,MainActivity.class);
                                        it.putExtra("uid",res2);
                                        it.putExtra("type","facebook");
                                        startActivity(it);
                                        finish();

                                    }
                                    else{
                                        Toast.makeText(LoginActivity.this, "Error try again", Toast.LENGTH_SHORT).show();
                                    }


                                }
                                else{
                                    Intent it=new Intent(LoginActivity.this,MainActivity.class);
                                    it.putExtra("uid",res);
                                    it.putExtra("type","facebook");
                                    startActivity(it);
                                    finish();
                                }
                                mProfileTracker.stopTracking();

                            }
                        };
                        // no need to call startTracking() on mProfileTracker
                        // because it is called by its constructor, internally.
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "not null enterd", Toast.LENGTH_SHORT).show();

                        Profile profile = Profile.getCurrentProfile();

                        Log.v("facebook - Name", profile.getName());
                        Log.v("facebook - Id", profile.getId());
                    }


                    //--------------------------------



                  /*  Profile profile = Profile.getCurrentProfile();

                    if (profile != null) {
                        String res=LoginActivity.sqLiteHelper.checkExistWithUid(profile.getId());
                        if(res.equals("exist")){
                            hideProgressDialog();
                            Toast.makeText(LoginActivity.this, "exist", Toast.LENGTH_SHORT).show();
                            Intent it=new Intent(LoginActivity.this,MainActivity.class);
                            it.putExtra("uid",profile.getId());
                            it.putExtra("type","facebook");
                            startActivity(it);
                            finish();
                        }
                        else if(res.equals("not exist")){
                            Toast.makeText(LoginActivity.this, "not exist", Toast.LENGTH_SHORT).show();
                                UserModelClass umc=new UserModelClass();
                                umc.setID(profile.getId());
                                umc.setNAME(profile.getName());
                                umc.setEMAIL("");
                                umc.setPASSWORD("nareshk@infologitech.in");
                                String res1=LoginActivity.sqLiteHelper.insertUserDet(umc);
                                if(res1.equals("insert")){
                                    hideProgressDialog();
                                    Intent it=new Intent(LoginActivity.this,MainActivity.class);
                                    it.putExtra("uid",profile.getId());
                                    it.putExtra("type","facebook");
                                    startActivity(it);
                                    finish();

                                }

                        }
                    }*/


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        showProgressDialog();
    }
    public  static void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        //updateUI(false);
                    }
                });
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            gName = acct.getDisplayName();
            //String personPhotoUrl = acct.getPhotoUrl().toString();
            gEmail = acct.getEmail();

            Log.e(TAG, "Name: " + gName + ", email: " + gEmail
                    + ", Image: " );



            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN && resultCode==RESULT_OK) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }


    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {


            String res=LoginActivity.sqLiteHelper.checkExist(gEmail);
            if(res.equals("not exist")){
                UserModelClass umc=new UserModelClass();
                umc.setNAME(gName);
                umc.setEMAIL(gEmail);
                umc.setPASSWORD("nareshk@infologitech.in");
                String res1=LoginActivity.sqLiteHelper.insertUserDet(umc,"gmail");
                if(res1.equals("insert")){
                    hideProgressDialog();
                    String res2=LoginActivity.sqLiteHelper.userLogin(gEmail,"nareshk@infologitech.in");
                    Intent it=new Intent(LoginActivity.this,MainActivity.class);
                    it.putExtra("uid",res2);
                    it.putExtra("type","gmail");
                    startActivity(it);
                    finish();

                }
                else{
                    Toast.makeText(this, "Error try again", Toast.LENGTH_SHORT).show();
                }
            }
            else if(res.equals("exist")){
                hideProgressDialog();
                String res1=LoginActivity.sqLiteHelper.getUidFromUser(gEmail);
                Intent it=new Intent(LoginActivity.this,MainActivity.class);
                it.putExtra("uid",res1);
                it.putExtra("type","gmail");
                startActivity(it);
                finish();
            }
        } else {
            hideProgressDialog();
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();

        }
    }
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();


    }
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
    private void getKeyHash() {

        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.example.admin.infantvaccination", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }


}
