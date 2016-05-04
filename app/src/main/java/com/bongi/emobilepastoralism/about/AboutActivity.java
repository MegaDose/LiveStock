package com.bongi.emobilepastoralism.about;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bongi.emobilepastoralism.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        if (lblVersion != null) {
            lblVersion.setText(version);
        }


    }

}
