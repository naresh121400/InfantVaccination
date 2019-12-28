package com.example.admin.infantvaccination;


import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class CreateCalendarEventsActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{


    //---------Creating events reqirements----------

    GoogleAccountCredential mCredential;

    ProgressDialog mProgress;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Google Calendar API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { CalendarScopes.CALENDAR };

    /**
     * Create the main activity.
     * @param savedInstanceState previously saved instance data.
     */


    //-----------Creating events reqirements ends----------

    public static String STATE="initial";
    private Toolbar toolbar;
    String sclass="",cid="";
    Button btnAddEvents,btnDeleteEvents;
    TextView mOutputText;
    ArrayList<String> scheduleDays=new ArrayList<String>();
    ArrayList<CalendarModelClass> eventIds=new ArrayList<CalendarModelClass>();
    public String state="";
    private static ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_calendar_events);


        //ToolBar

        toolbar = findViewById(R.id.addEventsTB);
        toolbar.setTitle("Google Calendar Events");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);


        //Getting buttons

        btnAddEvents=findViewById(R.id.btnAddEvents);
        btnDeleteEvents=findViewById(R.id.btnDeleteEvents);

        //TextView
        mOutputText=findViewById(R.id.tvAddEvents);

        //Getting intent Data
        sclass=getIntent().getStringExtra("sclass").toString();
        cid=getIntent().getStringExtra("cid").toString();

        getDates();


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnAddEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String status=MainActivity.sqLiteHelper.getEstatus(cid);
                if(status.equals("no")){
                    state="1";
                    getResultsFromApi();
                }
                else if(status.equals("yes")){
                    Toast.makeText(CreateCalendarEventsActivity.this, "Events already created", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnDeleteEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



              String status=MainActivity.sqLiteHelper.getEstatus(cid);
                if(status.equals("no")){
                    Toast.makeText(CreateCalendarEventsActivity.this, "No events present in calendar", Toast.LENGTH_SHORT).show();

                }
                else if(status.equals("yes")){

                    state="2";
                    getResultsFromApi();

                }


            }
        });



        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());



    }

    private void getDates() {


        ArrayList<String> dates=MainActivity.sqLiteHelper.getScheduleDays(cid);

        for(int i=0;i<dates.size();i++){
            String splitDates[]=dates.get(i).split("/");
            String newDate=splitDates[2]+"-"+splitDates[1]+"-"+splitDates[0]+"T03:00:02.000Z";
            scheduleDays.add(newDate);



        }


    }


    //Checking the events state



    @Override
    public void onBackPressed() {

            finish();

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void getResultsFromApi() {

        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! isDeviceOnline()) {
            mOutputText.setText("No network connection available.");
        } else {

                if(state.equals("1")){
                    new MakeNotificationTask(mCredential,this).execute();
                }
                else if(state.equals("2")){
                    new DeleteCalendarTask(mCredential,this).execute();
                }



        }
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }
    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                CreateCalendarEventsActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }
    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    mOutputText.setText(
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }





    //--------------------------------------------------------------------checking---------------------------------------


    private class MakeNotificationTask extends AsyncTask<Void, Void, Void>{

        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;
        Context ctx;
        public MakeNotificationTask(GoogleAccountCredential credential,Context ctx) {

            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API Android Quickstart")
                    .build();
            this.ctx=ctx;
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        public void MakeNotification(){

            int k=0;
            CalendarModelClass cmc;
            AddChild childData=MainActivity.sqLiteHelper.getSingleChild(cid);
            ListModelClass lmc;
            ArrayList<ListModelClass> vaccineDetList=MainActivity.sqLiteHelper.gettingChildVaccineData(cid,"1");
            for(int i=0;i<scheduleDays.size();i++){
                lmc=vaccineDetList.get(i);
                k++;
                cmc=new CalendarModelClass();

                String newId =UUID.randomUUID().toString().replaceAll("-", "");
                newId.replace("-","");
                Event event = new Event()
                        .setSummary("VACCINE REMINDER")
                        .setId(newId)
                        .setDescription(childData.getName()+ " needs "+"vaccine "+lmc.getLvname()+" on "+lmc.getLschedule());

                cmc.setCid(cid);
                cmc.setId(newId);
                eventIds.add(cmc);


                DateTime startDateTime = new DateTime(scheduleDays.get(i));
                EventDateTime start = new EventDateTime()
                        .setDateTime(startDateTime);
                event.setStart(start);

                DateTime endDateTime = new DateTime(scheduleDays.get(i));
                EventDateTime end = new EventDateTime()
                        .setDateTime(endDateTime);
                event.setEnd(end);

                EventReminder[] reminderOverrides = new EventReminder[] {
                        new EventReminder().setMethod("popup").setMinutes(14 * 24 * 60),
                        new EventReminder().setMethod("popup").setMinutes(28 * 24 * 60),
                };

                Event.Reminders reminders = new Event.Reminders()
                        .setUseDefault(false)
                        .setOverrides(Arrays.asList(reminderOverrides));
                event.setReminders(reminders);

                String calendarId = "primary";



                try {

                    mService.events().insert(calendarId, event).execute();

                } catch (IOException e) {


                    e.printStackTrace();
                }



            }
                MainActivity.sqLiteHelper.setEstatus(cid,"yes");

                int res=MainActivity.sqLiteHelper.storeCalendarData(eventIds);
                if(res>0){

                    eventIds.clear();
                }
                else{

                }



        }



        @Override
        protected Void doInBackground(Void... params) {
            try {

                MakeNotification();


            } catch (Exception e) {
                mLastError = e;


            }
            return null;
        }

        @Override
        protected void onCancelled() {

            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            CreateCalendarEventsActivity.REQUEST_AUTHORIZATION);
                } else {
                    mOutputText.setText("The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                mOutputText.setText("Request cancelled.");
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            hideProgressDialog();
            Toast.makeText(ctx, "Events created successfully", Toast.LENGTH_SHORT).show();
        }
    }

    //Delete calendar events


    private class DeleteCalendarTask extends AsyncTask<Void, Void, Void>{

        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;
        Context ctx;

        public DeleteCalendarTask(GoogleAccountCredential credential,Context ctx) {

            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API Android Quickstart")
                    .build();
            this.ctx=ctx;
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        public void deleteEvents(){






            ArrayList<CalendarModelClass> cmcList=MainActivity.sqLiteHelper.getCalendarData(cid);
            CalendarModelClass cmc;

            for(int i=0;i<scheduleDays.size();i++){
                cmc=cmcList.get(i);
                //String eid=cmc.getId().toString();
                try {

                    mService.events().delete("primary",cmc.getId().toString()).execute();

                } catch (IOException e) {


                    e.printStackTrace();
                }

            }



                int res=MainActivity.sqLiteHelper.deleteCalendarEvents(cid);
                if(res>0){
                    MainActivity.sqLiteHelper.setEstatus(cid,"no");

                }



        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                deleteEvents();


            } catch (Exception e) {
                mLastError = e;


            }
            return null;
        }

        @Override
        protected void onCancelled() {

            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            CreateCalendarEventsActivity.REQUEST_AUTHORIZATION);
                } else {
                    mOutputText.setText("The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                mOutputText.setText("Request cancelled.");
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            hideProgressDialog();
            Toast.makeText(ctx, "Events deleted successfully", Toast.LENGTH_SHORT).show();
        }
    }
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Processing...");
            mProgressDialog.setTitle("Calendar Events");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }


}
