package com.bongi.emobilepastoralism.reports;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bongi.emobilepastoralism.ConfigAdd;
import com.bongi.emobilepastoralism.DetectConnection;
import com.bongi.emobilepastoralism.R;
import com.bongi.emobilepastoralism.adapter.NotificationsListAdapter;
import com.bongi.emobilepastoralism.app.AppController;
import com.bongi.emobilepastoralism.locationClasses.GPSService;
import com.bongi.emobilepastoralism.loginreg.DatabaseHandler;
import com.bongi.emobilepastoralism.loginreg.LoginActivity;
import com.bongi.emobilepastoralism.loginreg.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.bongi.emobilepastoralism.data.NotificationItem;



public class ReportsActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = ReportsActivity.class.getSimpleName();

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private NotificationsListAdapter listAdapter;
    private List<NotificationItem> NotificationItem;
    Toolbar toolbar;


    private int offSetnew = 0;
    private int offSetold = 0;
    private int type, stat = 0; // Userd to show where feed should add new items of old item
    String mail,location = null;


    private String URL_FEED = ConfigAdd.Page_Link+"/android_connect/livestock/get_all_notifications.php?mail=";


    // JSON Node names
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_DETAILS = "details";
    private static final String TAG_TYPE = "type";
    private static final String TAG_TIME = "time";
    private static final String TAG_MAIL = "mail";
    private static final String TAG_REF ="ref";
    private static final String TAG_AMP = "amp";

    UserFunctions userFunctions;

    // Progress Dialog
    private ProgressDialog pDialog;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_notifications);


        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);
        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Alert Reports");

        listView = (ListView) findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        NotificationItem = new ArrayList<NotificationItem>();
        listAdapter = new NotificationsListAdapter(this,NotificationItem);
        listView.setAdapter(listAdapter);

        swipeRefreshLayout.setColorSchemeResources(R.color.Swipe_blue, R.color.Swipe_red, R.color.Swipe_gray);


        // Check login status in database
        userFunctions = new UserFunctions();
        if (userFunctions.isUserLoggedIn(getApplicationContext())) {
            //Check internet connection
            if (DetectConnection
                    .checkInternetConnection(ReportsActivity.this)) {


                swipeRefreshLayout.setOnRefreshListener(this);


                /**
                 * Showing Swipe Refresh animation on activity create
                 * As animation won't start on onCreate, post runnable is used
                 */
                swipeRefreshLayout.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                swipeRefreshLayout.setRefreshing(true);

                                                //Test if offset is equal to 0 if so call fetch Firstfeed
                                                if(offSetnew==0) {
                                                    fetchfirstFeed(0, offSetnew);
                                                    type = 0;
                                                }else {
                                                    fetchFeed(0, offSetnew);
                                                    type = 0;
                                                }


                                            }
                                        }
                );
            } else {
                Toast alert = Toast.makeText(ReportsActivity.this,
                        R.string.message_Ninternet,
                        Toast.LENGTH_LONG);

                alert.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                alert.setGravity(Gravity.TOP, 0, 130);
                alert.show();

                // AllStatusFragment.this.startActivity(new Intent(
                // Settings.ACTION_WIRELESS_SETTINGS));
            }

            loadSavedPreferences();
            findnearby();
            pDialog.hide();
        } else {
            openlogin();
            finish();

        }

        listView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount) { //check if we've reached the bottom

                    //Check internet connection
                    if (DetectConnection
                            .checkInternetConnection(ReportsActivity.this)) {
                        if (stat == 0) {
                            stat++;

                            if (offSetnew != 0) {
                                fetchFeed(1, offSetold);
                                type = 1;

                            }

                        }
                    } else {
                        Toast alert = Toast.makeText(ReportsActivity.this, R.string.message_Ninternet,Toast.LENGTH_LONG);

                        alert.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                        alert.setGravity(Gravity.TOP, 0,130);
                        alert.show();

                        // AllStatusFragment.this.startActivity(new Intent(
                        // Settings.ACTION_WIRELESS_SETTINGS));
                    }
                }

            }
        });


    }


    /**
     * This method is called when swipe refresh is pulled down
     */

    @Override
    public void onRefresh() {
        //Check internet connection
        if (DetectConnection
                .checkInternetConnection(ReportsActivity.this)) {
            fetchFeed(0, offSetnew);
            type=0;
        } else {
            Toast alert = Toast.makeText(ReportsActivity.this,R.string.message_Ninternet,Toast.LENGTH_LONG);

            alert.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            alert.setGravity(Gravity.TOP, 0, 130);
            alert.show();

            // AllStatusFragment.this.startActivity(new Intent(
            // Settings.ACTION_WIRELESS_SETTINGS));
        }
    }


    private void fetchFeed(final int type, int set) {

        // showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        String username = db.getUserName();

        // We first check for cached request
        Cache cache = AppController.getInstance().getRequestQueue().getCache();

        final String Feed = URL_FEED + username + "&type=" + type + "&set=" + set+"&location="+location;




        Entry entry = cache.get(Feed);




        Log.v("link", Feed);

        /*if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {


                    parseJsonFeed(new JSONObject(data), type, Feed);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }



        } else {*/

        //Check internet connection
        if (DetectConnection
                .checkInternetConnection(ReportsActivity.this)) {
            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
                    Feed, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        parseJsonFeed(response, type,Feed);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(ReportsActivity.this, "Opps Something went wrong, Try again", Toast.LENGTH_LONG).show();


                }
            });

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);

        }


        else {
            Toast alert = Toast.makeText(ReportsActivity.this,
                    R.string.message_Ninternet,
                    Toast.LENGTH_LONG);

            alert.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
            alert.setGravity(Gravity.TOP,0,130);
            alert.show();

            // AllStatusFragment.this.startActivity(new Intent(
            // Settings.ACTION_WIRELESS_SETTINGS));
        }

    }
    //}



    private void fetchfirstFeed(final int type, int set) {

        //Check internet connection
        if (DetectConnection
                .checkInternetConnection(ReportsActivity.this)) {
            // showing refresh animation before making http call
            swipeRefreshLayout.setRefreshing(true);

            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            String username = db.getUserName();


            // We first check for cached request
            Cache cache = AppController.getInstance().getRequestQueue().getCache();

            final String Feed = URL_FEED + username + "&type=" + type + "&set=" + set+"&location="+location;


            Entry entry = cache.get(Feed);


            Log.v("link", Feed);

            if (entry != null) {
                // fetch the data from cache
                try {
                    String data = new String(entry.data, "UTF-8");
                    try {

                        Calendar calendar = Calendar.getInstance();
                        long serverDate = AppController.getInstance().getRequestQueue().getCache().get(Feed).serverDate;


                       /* if (getMinutesDifference(serverDate, calendar.getTimeInMillis()) >= 30) {
                            AppController.getInstance().getRequestQueue().getCache().remove(Feed);
                        }*/

                        parseJsonFeed(new JSONObject(data), Feed);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


            } else {

                // making fresh volley request and getting json
                JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
                        Feed, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(TAG, "Response: " + response.toString());
                        if (response != null) {
                            parseJsonFeed(response, Feed);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(ReportsActivity.this, "Opps Something went wrong, Try again", Toast.LENGTH_LONG).show();


                    }
                });
                // Adding request to volley request queue
                AppController.getInstance().addToRequestQueue(jsonReq);







            }
        } else {
            Toast alert = Toast.makeText(ReportsActivity.this,
                    R.string.message_Ninternet,
                    Toast.LENGTH_LONG);

            alert.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
            alert.setGravity(Gravity.TOP,0,130);
            alert.show();

            // AllStatusFragment.this.startActivity(new Intent(
            // Settings.ACTION_WIRELESS_SETTINGS));
        }
    }




    /**
     * Parsing json reponse and passing the data to feed view list adapter
     */
    private void parseJsonFeed(JSONObject response,int type,String url) {
        try {
            JSONArray feedArray = response.getJSONArray("noti");

            for (int i = 0; i < feedArray.length(); i++) {

                JSONObject feedObj = (JSONObject) feedArray.get(i);

                NotificationItem item = new NotificationItem();

                int rank = feedObj.getInt("id");
                item.setId(feedObj.getInt("id"));
                item.setFile(feedObj.getString("file"));
                item.setType(feedObj.getInt("type"));
                item.setRead(feedObj.getInt("read"));
                item.setCount(feedObj.getInt("count"));
                item.setMail(feedObj.getString("mail"));
                item.setName(feedObj.getString("name"));
                item.setDetails(feedObj.getString("details"));
                item.setTimeStamp(feedObj.getString("time"));
                item.setNotepadPic(ConfigAdd.Page_Link+feedObj.getString("notepadPic"));



                if (type == 1) {

                    NotificationItem.add(item);

                    // updating offset value to highest value
                    if (rank < offSetold)
                        offSetold = rank;


                } else if (type == 0) {



                    NotificationItem.add(0, item);

                    // updating offset value to highest value
                    if (rank > offSetnew)
                        offSetnew = rank;


                }

                if (offSetold == 0 && rank > offSetold)
                    offSetold = rank;

                //}
                stat = 0;
            }

            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();

            // stopping swipe refresh
            swipeRefreshLayout.setRefreshing(false);

            if(feedArray.length()==0){
                AppController.getInstance().getRequestQueue().getCache().remove(url);

            }



        } catch (JSONException e) {
            e.printStackTrace();
            AppController.getInstance().getRequestQueue().getCache().remove(url);
            // stopping swipe refresh
            swipeRefreshLayout.setRefreshing(false);
            stat = 0;

            if(type==0){
                Toast alert = Toast.makeText(ReportsActivity.this,"No new notifications",Toast.LENGTH_LONG);

                alert.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                alert.setGravity(Gravity.TOP, 0, 130);
                alert.show();
            }
        }


        // Get listview
        ListView lv = listView;

        // on seleting single status feed item
        // launching View Track Screen
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // getting values from selected ListItem
                String ref = ((TextView) view.findViewById(R.id.file)).getText().toString();
                String idref = ((TextView) view.findViewById(R.id.id)).getText().toString();
                String title = ((TextView) view.findViewById(R.id.details)).getText().toString();

                // Starting  intent
                /*Intent tagged = new Intent(getApplicationContext(),FullScreenViewNotiActivity.class);
                tagged.putExtra(TAG_REF, idref);
                tagged.putExtra("img", ref);
                tagged.putExtra("title",title);

                // starting new activity and expecting some response back
                startActivity(tagged);*/
            }
        });


        // Register the ListView  for Context menu
        registerForContextMenu(lv);

    }

    /**
     * Fetch the fisrt status uploads and order them
     */
    private void parseJsonFeed(JSONObject response,  String url) {
        try {
            JSONArray feedArray = response.getJSONArray("noti");

            for (int i = 0; i < feedArray.length(); i++) {

                JSONObject feedObj = (JSONObject) feedArray.get(i);

                NotificationItem item = new NotificationItem();

                int rank = feedObj.getInt("id");
                item.setId(feedObj.getInt("id"));
                item.setFile(feedObj.getString("file"));
                item.setType(feedObj.getInt("type"));
                item.setRead(feedObj.getInt("read"));
                item.setCount(feedObj.getInt("count"));
                item.setMail(feedObj.getString("mail"));
                item.setName(feedObj.getString("name"));
                item.setDetails(feedObj.getString("details"));
                item.setTimeStamp(feedObj.getString("time"));
                item.setNotepadPic(ConfigAdd.Page_Link+feedObj.getString("notepadPic"));


                NotificationItem.add(item);

                // updating offset value to highest value
                if (rank > offSetnew)
                    offSetnew = rank;






                //}
                stat = 0;
            }



            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();

            // stopping swipe refresh
            swipeRefreshLayout.setRefreshing(false);

            //Test is there was any content in array
            if (feedArray.length() == 0) {
                AppController.getInstance().getRequestQueue().getCache().remove(url);
            }

            if (feedArray.length() > 0){
                //Get last id of array feild to set ooffset old
                JSONObject feedObj = (JSONObject) feedArray.get(feedArray.length()-1);
                int rank = feedObj.getInt("id");

                offSetold = rank;
            }

        } catch (JSONException e) {
            e.printStackTrace();

            AppController.getInstance().getRequestQueue().getCache().remove(url);
            // stopping swipe refresh
            swipeRefreshLayout.setRefreshing(false);

        }


        // Get listview
        ListView lv = listView;

        // on seleting single status feed item
        // launching View Track Screen
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // getting values from selected ListItem
                String ref = ((TextView) view.findViewById(R.id.file)).getText().toString();
                String idref = ((TextView) view.findViewById(R.id.id)).getText().toString();
                String title = ((TextView) view.findViewById(R.id.details)).getText().toString();




                // Starting  intent
                /*Intent tagged = new Intent(getApplicationContext(),FullScreenViewNotiActivity.class);
                tagged.putExtra(TAG_REF, idref);
                tagged.putExtra("img", ref);
                tagged.putExtra("title",title);

                // starting new activity and expecting some response back
                startActivity(tagged);*/


            }
        });



        // Register the ListView  for Context menu
        registerForContextMenu(lv);
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
                //NavUtils.navigateUpFromSameTask(this);
                ReportsActivity.this.finish();
                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Launching new activity
     */







    public void openlogin() {
        // user is not logged in show login screen
        Intent login = new Intent(getApplicationContext(), LoginActivity.class);
        //login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);
        // Closing dashboard screen
        //finish();

    }



    //Search Add get current locations
    private void findnearby() {

        String address = "";
        GPSService mGPSService = new GPSService(ReportsActivity.this);
        mGPSService.getLocation();

        if (mGPSService.isLocationAvailable == false) {

            // Here you can ask the user to try again, using return; for that
            Toast.makeText(ReportsActivity.this, "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();

            return;

            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
            // address = "Location not available";
        } else {


            //swipeRefreshLayout.setRefreshing(true);

            // Getting location co-ordinates
            //double latitude = mGPSService.getLatitude();
            //double longitude = mGPSService.getLongitude();
            //Toast.makeText(getActivity(), "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();

            address = mGPSService.getLocationAddress();

            //tvLocation.setText("Latitude: " + latitude + " \nLongitude: " + longitude);
            //tvAddress.setText("Address: " + address);

        }

        //Toast.makeText(getActivity(), "Your address is: " + address, Toast.LENGTH_LONG).show();

        // make sure you close the gps after using it. Save user's battery power
        mGPSService.closeGPS();
        //Test if offset is equal to 0 if so call fetch Firstfeed

        SavedPreferences(address);
    }


    //Functions Used to find current location and add it to information being uploaded with the alert
    private void loadSavedPreferences() {

        String address ="No Address";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ReportsActivity.this);
        address = sharedPreferences.getString("Current_Location", "No Address");
        location = address;
    }

    private void SavedPreferences(String address) {

        try{
            if(address.substring(0,12).equals("IO Exception")){
                address= "location not found";
            }
        } catch (Exception ignore){

        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ReportsActivity.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Current_Location", address);
        editor.apply();
        location = address;
    }







}
