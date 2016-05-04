package com.bongi.emobilepastoralism;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bongi.emobilepastoralism.about.AboutActivity;
import com.bongi.emobilepastoralism.app.AppController;
import com.bongi.emobilepastoralism.loginreg.UserFunctions;
import com.google.android.gcm.GCMRegistrar;


public class SettingsActivity extends ActionBarActivity {
    Toolbar toolbar;
    UserFunctions userFunctions;
    Switch  auto,messenger;

    private static String KEY_LINK = "link";
    private static String KEY_HEAD = "head";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        auto = (Switch) findViewById(R.id.autotog);
        messenger = (Switch) findViewById(R.id.messagetog);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);
        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settiings");




        RelativeLayout loggedout = (RelativeLayout) findViewById(R.id.logout);

        //Test if user is logged in
        userFunctions = new UserFunctions();
        if(userFunctions.isUserLoggedIn(getApplicationContext())) {
            if (loggedout != null) {
                loggedout.setVisibility(View.VISIBLE);
            }
        }else{
            if (loggedout != null) {
                loggedout.setVisibility(View.GONE);
            }
        }
        loadSavedPreferences();
    }



    private void loadSavedPreferences() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean MessengerValue = sharedPreferences.getBoolean("messenger_Value", false);
        boolean AutoValue = sharedPreferences.getBoolean("auto_Value", false);

        if (AutoValue) {
            auto.setChecked(true);
        }else{
            auto.setChecked(false);
        }



        if (MessengerValue) {
            messenger.setChecked(true);
        }else{
            messenger.setChecked(false);
        }



    }


    public void viewNoti(View view) {

        savePreferences("auto_Value", auto.isChecked());

    }




    public void viewAlerts(View view) {


        if (messenger.isChecked()) {
            enableMessenger();
        } else {
            savePreferences("messenger_Value", messenger.isChecked());
        }
    }

    private void savePreferences(String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();

    }





    public void logout(View view) {
        ///Create back dialoag box
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("Are your sure you would like to logout of your account ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LogoutConfirm();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog alert = alt_bld.create();
        alert.setTitle("Logout");
        //alert.setIcon(R.drawable.ic_launcher);
        alert.show();


    }

    public void enableMessenger() {
        ///Create back dialoag box
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("This function allows you to receive Random alert about problem / warnings that are posted on the forum.")
                .setCancelable(false)
                .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        savePreferences("messenger_Value", messenger.isChecked());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        messenger.setChecked(false);
                        dialog.cancel();
                    }
                });


        AlertDialog alert = alt_bld.create();
        alert.setTitle("Enable Auto Alerts");
        alert.setIcon(R.drawable.ic_alert_icon);
        alert.show();


    }

    private void LogoutConfirm() {
        UserFunctions userFunction = new UserFunctions();

        userFunction.logoutUser(getApplicationContext());
        /*Toast alert = Toast.makeText(MainActivity.this,
                "LogOut Successful",
                Toast.LENGTH_LONG);

        alert.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        alert.setGravity(Gravity.TOP, 0, 130);
        alert.show();*/

        AppController.getInstance().getRequestQueue().getCache().clear();

        // UnRegistration now from with GCM
        GCMRegistrar.unregister(this);

        Intent i = new Intent(SettingsActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public void viewabout(View view)  {

        Intent in = new Intent(getApplicationContext(),AboutActivity.class);
        startActivity(in);


    }



    public void viewTC(View view)  {

        String head ="Terms and Conditions";
        String link =ConfigAdd.Page_Link+"/doc/T&C.php?app";

        // Starting new intent
        Intent in = new Intent(getApplicationContext(),FullScreenBrowser.class);
        // sending pid to next activity4
        in.putExtra(KEY_HEAD, head);
        in.putExtra(KEY_LINK, link);
        // starting new activity and expecting some response back
        startActivityForResult(in, 100);


    }


    public void viewPrivacy(View view)  {

        String head ="Privacy";
        String link =ConfigAdd.Page_Link+"/doc/Privacy.php?app";

        // Starting new intent
        Intent in = new Intent(getApplicationContext(),FullScreenBrowser.class);
        // sending pid to next activity4
        in.putExtra(KEY_HEAD, head);
        in.putExtra(KEY_LINK, link);
        // starting new activity and expecting some response back
        startActivityForResult(in, 100);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.empty_menu, menu);
        return true;

    }

    /**
     * On selecting action bar icons
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
