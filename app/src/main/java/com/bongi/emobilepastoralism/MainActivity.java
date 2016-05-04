package com.bongi.emobilepastoralism;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void warn(View v) {
        Intent intent = new Intent(MainActivity.this, Warnings.class);
        startActivity(intent);
    }

    public void notify (View v){
        Intent intent = new Intent(MainActivity.this, Notifications.class);
        startActivity(intent);
    }

    /* public void currentLocation (View v) {
         Intent intent = new Intent(MainActivity.this, CurrentLocation.class);
      /* intent.setData(Uri.parse("geo:45.00, -56.00213"));
         if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }*/
    }

