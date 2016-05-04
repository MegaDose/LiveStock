package com.bongi.emobilepastoralism;

import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bongi.emobilepastoralism.adapter.NavDrawerListAdapter;
import com.bongi.emobilepastoralism.imageUploads.AvatarCaptureActivity;
import com.bongi.emobilepastoralism.model.NavDrawerItem;
import com.bongi.emobilepastoralism.portfolio.FarmerPortfolio;
import com.bongi.emobilepastoralism.reports.ReportsActivity;
import com.google.android.gms.nearby.messages.Messages;

import java.util.ArrayList;

public class HomePage extends ActionBarActivity  {


    private int viewPosition = 1;


    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    public static Toolbar toolbar;


    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    String KEY_LINK = "link";
    String KEY_HEAD = "head";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_more);

        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.medianav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.medianav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Tracks
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Events
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Blogs
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));

        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_action_more, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        /*if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }*/


    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_alert:
                alert();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /* *
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_alert).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    public void displayView(int position) {


        // update the main content by replacing fragments
        Intent intent = null;
        switch (position) {
            case 0:
                intent = new Intent(HomePage.this, FarmerPortfolio.class);
                break;
            case 1:
                intent = new Intent(HomePage.this, ReportsActivity.class);
                break;
            case 2:
                TellAFriend();
                break;
            case 3:
                intent = new Intent(HomePage.this, SettingsActivity.class);
                break;



            default:
                break;
        }

        try {
            startActivity(intent);
        }catch (Exception ignored){

        }


    }

    public void weather (View v){
        Intent intent = new Intent(HomePage.this, MainActivity.class);
        startActivity(intent);
    }

    public void info (View v) {
        String head = "Weather Report";
        String link = "http://www.weather.vision";

        // Starting new intent
        Intent in = new Intent(getApplicationContext(), FullScreenBrowser.class);
        // sending pid to next activity4
        in.putExtra(KEY_HEAD, head);
        in.putExtra(KEY_LINK, link);
        // starting new activity and expecting some response back
        startActivityForResult(in, 100);
    }

    public void location (View v) {
        Uri gmmIntentUri = Uri.parse("geo:0,0");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    public void alert(){
        Intent i = new Intent(HomePage.this, AvatarCaptureActivity.class);
        startActivity(i);

    }

    public void RefreshHomeInfo(){

    }

    public void TellAFriend() {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, com.bongi.emobilepastoralism.Messages.ShareApp());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

    }
}
