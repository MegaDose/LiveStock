package com.bongi.emobilepastoralism;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        String version =" ";

        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
            version=  /*"PackageName = " + info.packageName + "\nVersionCode = "
                    + info.versionCode + "\nVersionName = "*/"V "
                    + info.versionName ;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        TextView lblVersion = (TextView) findViewById(R.id.versionText);

        lblVersion.setText(version);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        new Handler().postDelayed(new Runnable() {
 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                // Check login status in database




                        Intent in = new Intent(getApplicationContext(), HomePage.class);
                        // starting new activity and expecting some response back
                        startActivity(in);
                        finish();





            }
        }, SPLASH_TIME_OUT);
    }

 
}