package com.bongi.emobilepastoralism.portfolio;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.bongi.emobilepastoralism.ConfigAdd;
import com.bongi.emobilepastoralism.DetectConnection;
import com.bongi.emobilepastoralism.FullScreenBrowser;
import com.bongi.emobilepastoralism.FullScreenViewActivity;
import com.bongi.emobilepastoralism.R;
import com.bongi.emobilepastoralism.SettingsActivity;
import com.bongi.emobilepastoralism.app.AppController;
import com.bongi.emobilepastoralism.loginreg.DatabaseHandler;
import com.bongi.emobilepastoralism.loginreg.LoginActivity;
import com.bongi.emobilepastoralism.loginreg.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;



public class FarmerPortfolio extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = FarmerPortfolio.class.getSimpleName();

    Toolbar toolbar;


    private static final String TAG_SEX = "sex";
    private static final String TAG_MEMBER = "member";
    private static final String TAG_FOLLOW = "follow";
    private static final String TAG_WEBSITE = "website";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_GENRE = "genre";
    private static final String TAG_ABOUT = "about";
    private static final String TAG_INSP = "insp";

    private static String KEY_LINK = "link";
    private static String KEY_HEAD = "head";

    //For Blogs

    private static final String TAG_HEAD = "head";
    private static final String TAG_BYLINE = "byline";
    private static final String TAG_EDITION = "edition";
    private static final String TAG_VIEWS = "views";
    private static final String TAG_INTRO = "intro";
    private static final String TAG_BODY = "body";
    private static final String TAG_CON = "con";

    private static final String TAG_SOURCE1 = "source1";
    private static final String TAG_SOURCE2 = "source2";
    private static final String TAG_SOURCE3 = "source3";
    private static final String TAG_SOURCE1T = "sourcetest1";
    private static final String TAG_SOURCE2T = "sourcetest2";
    private static final String TAG_SOURCE3T = "sourcetest3";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //For Events

    private static final String TAG_HOST = "host";
    private static final String TAG_DAY = "day";
    private static final String TAG_MONTH = "month";
    private static final String TAG_YEAR = "year";
    private static final String TAG_TAG = "tag";
    private static final String TAG_TIME = "time";
    private static final String TAG_VENUE = "venue";
    private static final String TAG_IMAGE = "image";
    private static final String TAG_TYPE = "type";
    private static final String TAG_LINE = "line";
    private static final String TAG_DISC = "disc";
    private static final String TAG_PRE = "pre";
    private static final String TAG_DOOR = "door";
    private static final String TAG_VIP = "vip";
    private static final String TAG_VVIP = "vvip";
    private static final String TAG_VALDATE = "samedate";
    private static final String TAG_Golden = "golden";
    private static final String TAG_VALPURCHASE = "valpur";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //For Music

    private static final String TAG_Title = "title";
    private static final String TAG_Artist = "artist";
    private static final String TAG_Album = "album";
    private static final String KEY_AMP = "amp";

    private static final String TAG_Day = "day";
    private static final String TAG_Month = "month";
    private static final String TAG_Year = "year";

    private static final String TAG_Genre = "genre";
    private static final String TAG_Views = "views";
    private static final String TAG_Producer = "producer";
    private static final String TAG_Bio = "bio";
    private static final String TAG_Share = "tag_share";
    private static final String TAG_BarPer = "barper";
    private static final String TAG_Rate = "total_rate";
    private static final String TAG_Link1 = "link1";
    private static final String TAG_Link2 = "link2";
    private static final String TAG_Link3 = "link3";
    private static final String TAG_Link4 = "link4";
    private static final String TAG_Download = "download";

    private static final String TAG_Link1v = "link1v";
    private static final String TAG_Link2v = "link2v";
    private static final String TAG_Link3v = "link3v";
    private static final String TAG_Link4v = "link4v";
    private static final String TAG_Downloadv = "downloadv";


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_AMP = "amp";
    private static final String TAG_STATUS = "status";
    private static final String TAG_STATUS2 = "status2";
    private static final String TAG_COUNT = "ccount";
    private static final String TAG_MAIL = "mail";
    private static final String TAG_DATE = "date";
    private static final String TAG_REF = "ref";
    private static final String TAG_NOTE = "cnote";
    private static final String TAG_NotePadPic = "notepadpic";
    private static final String TAG_CAT = "cat";
    String cat ="";

    private static final String TAG_TESTM = "testm";





    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    UserFunctions userFunctions;
    LinearLayout aboutcolapse;



    private String URL_FEED = ConfigAdd.Page_Link+"/android_connect/livestock/get_notepad_details.php?mail=";
    private String URL_Image = ConfigAdd.Page_Link+"/android_connect/livestock/get_notepad_images.php?notepad=view&&mail=";
    private String URL_SHADOW = ConfigAdd.Page_Link+"/android_connect/livestock/shadbar.php?shadow=";

    private String ampref, mail;
    private SwipeRefreshLayout swipeRefreshLayout;
    boolean login = false;


    ScrollView sv;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad_details);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        sv =(ScrollView) findViewById(R.id.scrollView1);

        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);
        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(" ");


        swipeRefreshLayout.setColorSchemeResources(R.color.Swipe_blue, R.color.Swipe_red, R.color.Swipe_gray);

        swipeRefreshLayout.setOnRefreshListener(this);
        ampref =" ";


        //hide field that will be showen after
        aboutcolapse = (LinearLayout) findViewById(R.id.aboutcolapse);
        // hide until its title is clicked
        aboutcolapse.setVisibility(View.GONE);

        // Check login status in database
        userFunctions = new UserFunctions();
        if (userFunctions.isUserLoggedIn(getApplicationContext())) {
            //Check internet connection
            if (DetectConnection
                    .checkInternetConnection(FarmerPortfolio.this)) {
            }

        } else {
            openlogin();
            finish();

        }




            }



    //Clickable sections so show and close content
    public void toggle_about(View v){
        aboutcolapse.setVisibility(aboutcolapse.isShown()
                ? View.GONE
                : View.VISIBLE);
    }


    @Override
    public void onRefresh() {

        // showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);

        Intent i = getIntent();
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        String username = db.getUserName();
        //String username = "admin";
        // getting intent data

        // Get XML values from previous intent
        final String mail = i.getStringExtra(TAG_MAIL);
        String mymail = "&mymail="+username;


        //Clear old url
        AppController.getInstance().getRequestQueue().getCache().remove(URL_FEED + mail + mymail);
        AppController.getInstance().getRequestQueue().getCache().remove(URL_Image + mail);


        // making fresh volley request and getting json
        JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
                URL_FEED+mail+mymail, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d(TAG, "Response: " + response.toString());
                if (response != null) {
                    parseJsonFeed(response);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

            /*/ making fresh volley request and getting json
            JsonObjectRequest jsonReqImage = new JsonObjectRequest(Method.GET,
                    URL_Image+mail, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        parseJsonImageFeed(response, URL_Image+mail);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            })*/;

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(jsonReq);
        //AppController.getInstance().addToRequestQueue(jsonReqImage);



    }




    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {
        swipeRefreshLayout.setRefreshing(true);


        try {
            JSONArray feedArray = response.getJSONArray("member");


            for (int i = 0; i < feedArray.length(); i++) {



                if (imageLoader == null)
                    imageLoader = AppController.getInstance().getImageLoader();

                TextView name = (TextView) findViewById(R.id.nname);
                TextView amp = (TextView) findViewById(R.id.namp);
                TextView mail = (TextView) findViewById(R.id.nmail);
                TextView testm = (TextView) findViewById(R.id.ntestm);


                TextView follow = (TextView) findViewById(R.id.nfollow);
                TextView about = (TextView) findViewById(R.id.nabout);
                TextView location = (TextView) findViewById(R.id.nlocation);
                TextView genre = (TextView) findViewById(R.id.ngenre);
                TextView sex = (TextView) findViewById(R.id.nsex);
                TextView insp = (TextView) findViewById(R.id.ninsp);

                TextView music = (TextView) findViewById(R.id.ntrack);
                TextView event = (TextView) findViewById(R.id.nevent);
                TextView blog = (TextView) findViewById(R.id.nblog);
                TextView shadow = (TextView) findViewById(R.id.nshadow);
                TextView shadowing = (TextView) findViewById(R.id.nshadowing);
                TextView member = (TextView) findViewById(R.id.nmember);
                TextView website = (TextView) findViewById(R.id.nwebsite);
                TextView image = (TextView) findViewById(R.id.nimage);
                TextView imageorg = (TextView) findViewById(R.id.nimageorg);

                RelativeLayout block = (RelativeLayout) findViewById(R.id.block);
                ImageView mess = (ImageView) findViewById(R.id.btnNMessage);
                ImageView mention = (ImageView) findViewById(R.id.btnNMention);
                ImageView shadbut = (ImageView) findViewById(R.id.btnNShadow);
                RelativeLayout edit =(RelativeLayout) findViewById(R.id.btnedt);

















        /*/ Checking for empty_menu feed url
		if (item.getUrl() != empty_menu) {
			url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">"
					+ item.getUrl() + "</a> "));

			// Making url clickable
			url.setMovementMethod(LinkMovementMethod.getInstance());
			url.setVisibility(View.VISIBLE);
		} else {
			// url is empty_menu, remove from the view
			url.setVisibility(View.GONE);
		}*/


                JSONObject feedObj = (JSONObject) feedArray.get(i);

                // Creating The Toolbar and setting it as the Toolbar for the activity
                toolbar = (Toolbar) findViewById(R.id.tool_bar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);
                // enabling action bar app icon and behaving it as toggle button
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Portfolio");



                name.setText(feedObj.getString("name"));
                amp.setText("&"+feedObj.getString("amp"));
                ampref = "&"+feedObj.getString("amp");
                int val =feedObj.getInt("testm");
                testm.setText(String.valueOf(feedObj.getInt("testm")));


                if(val==3){
                    block.setVisibility(View.GONE);
                    mess.setVisibility(View.GONE);
                    mention.setVisibility(View.GONE);
                    shadbut.setVisibility(View.GONE);
                    edit.setVisibility(View.VISIBLE);
                }else{

                    if(val==1){

                        shadbut.setImageResource(R.drawable.ic_unshadow);

                    }
                }

                mail.setText(feedObj.getString("mail"));

                //might be empty_menu sometimes
                String Follow = feedObj.isNull("follow") ? null : feedObj
                        .getString("follow");

                follow.setText(Follow);


                //might be empty_menu sometimes
                String Member = feedObj.isNull("member") ? null : feedObj
                        .getString("member");
                member.setText(Member);

                //might be empty_menu sometimes
                String Website = feedObj.isNull("website") ? null : feedObj
                        .getString("website");
                website.setText(Website);

                // Making url clickable
                //website.setMovementMethod(LinkMovementMethod.getInstance());

                //might be empty_menu sometimes
                String About = feedObj.isNull("about") ? null : feedObj
                        .getString("about");

                about.setText(About);

                //might be empty_menu sometimes
                String Location = feedObj.isNull("location") ? null : feedObj
                        .getString("location");
                location.setText(Location);
                //might be empty_menu sometimes
                String Genre = feedObj.isNull("genre") ? null : feedObj
                        .getString("genre");
                genre.setText(Genre);

                //might be empty_menu sometimes
                String Insp = feedObj.isNull("insp") ? null : feedObj
                        .getString("insp");
                insp.setText(Insp);

                //might be empty_menu sometimes
                String Sex = feedObj.isNull("sex") ? null : feedObj
                        .getString("sex");
                sex.setText(Sex);




                music.setText(String.valueOf(feedObj.getInt("track"))+" Tracks");
                event.setText(String.valueOf(feedObj.getInt("event"))+" Events");
                blog.setText(String.valueOf(feedObj.getInt("blog"))+" Blogs");
                shadow.setText(String.valueOf(feedObj.getInt("shadow"))+" Shadow");
                shadowing.setText(String.valueOf(feedObj.getInt("shadowing"))+" Shadowing");


                // user profile pic

                String imageurl =ConfigAdd.Page_Link+feedObj.getString("notepic");

                //FeedImageView feedImageView = (za.co.megadose.FeedImageView) findViewById(R.id.nnotePic);
                NetworkImageView feedImageView =(NetworkImageView) findViewById(R.id.nnotePic);
                feedImageView.setImageUrl(imageurl, imageLoader);

                image.setText(ConfigAdd.Page_Link+feedObj.getString("notepic"));
                imageorg.setText(ConfigAdd.Page_Link+feedObj.getString("notepicOriginal"));


            }

            if(feedArray.length()==0){
                Toast alert = Toast.makeText(FarmerPortfolio.this,"No Users Found/ Network connection error",Toast.LENGTH_LONG);

                alert.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                alert.setGravity(Gravity.TOP,0,130);
                alert.show();
            }

        } catch (JSONException e) {
            e.printStackTrace();

            swipeRefreshLayout.setRefreshing(false);
        }


        swipeRefreshLayout.setRefreshing(false);
    }









    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_main, menu);
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
                FarmerPortfolio.this.finish();
                return true;
            case R.id.action_search:
                // Search
                //Search();
                return true;
            case R.id.action_share:
                // Upload New Track
                ShareNotePad();
                return true;
            case R.id.action_settings:
                // Upload New Track
                Settings();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Launching new activity
     * */

    public void ShareNotePad() {

        Intent i = getIntent();


        // Get XML values from previous intent
        String mail = i.getStringExtra(TAG_MAIL);

        TextView name = (TextView) findViewById(R.id.nname);
        TextView amp = (TextView) findViewById(R.id.namp);

        String Name =name.getText().toString()+" "+ amp.getText().toString();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Check out "+Name+" MegaDose Entertainment NotePad. - " +
                        "http://www.megadose.co.za/?SearchRes="+mail+" \n\n"+
                        "Also get the android app https://play.google.com/store/apps/details?id=za.co.megadose ");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

    }
    private void Settings() {

        Intent i = new Intent(getApplicationContext() , SettingsActivity.class);
        startActivity(i);

    }

    public void viewEditNotePad(View view)  {

        MoreAccess();
        /*/ getting intent data
        Intent i = getIntent();
        // Get XML values from previous intent
        String name = ((TextView)findViewById(R.id.nname)).getText().toString();
        String gender = ((TextView)findViewById(R.id.nsex)).getText().toString();
        String member = ((TextView)findViewById(R.id.nmember)).getText().toString();
        String website = ((TextView)findViewById(R.id.nwebsite)).getText().toString();
        String follow = ((TextView)findViewById(R.id.nfollow)).getText().toString();
        String location = ((TextView)findViewById(R.id.nlocation)).getText().toString();
        String genre = ((TextView)findViewById(R.id.ngenre)).getText().toString();
        String bio = ((TextView)findViewById(R.id.nabout)).getText().toString();
        String insp = ((TextView)findViewById(R.id.ninsp)).getText().toString();
        String image = ((TextView)findViewById(R.id.nimage)).getText().toString();
        // Starting new intent
        Intent in = new Intent(getApplicationContext(),EditNotepadActivity.class);
        // sending pid to next activity4
        in.putExtra(TAG_NAME, name);
        in.putExtra(TAG_SEX, gender);
        in.putExtra(TAG_MEMBER, member);
        in.putExtra(TAG_WEBSITE, website);
        in.putExtra(TAG_FOLLOW, follow);
        in.putExtra(TAG_LOCATION, location);
        in.putExtra(TAG_GENRE, genre);
        in.putExtra(TAG_ABOUT, bio);
        in.putExtra(TAG_INSP, insp);
        in.putExtra(TAG_IMAGE, image);

        // starting new activity and expecting some response back
        startActivityForResult(in, 100);*/

    }

    /*public void viewShadow(View view)  {

        // getting intent data
        Intent i = getIntent();
        // Get XML values from previous intent
        String mail = i.getStringExtra(TAG_MAIL);
        String name = i.getStringExtra(TAG_NAME);
        // Starting new intent
        Intent in = new Intent(getApplicationContext(),AllShadowActivity.class);
        // sending pid to next activity4
        in.putExtra(TAG_MAIL, mail);
        in.putExtra(TAG_NAME, name);

        // starting new activity and expecting some response back
        startActivityForResult(in, 100);

    }

    public void viewShadowing(View view)  {

        // getting intent data
        Intent i = getIntent();
        // Get XML values from previous intent
        String mail = i.getStringExtra(TAG_MAIL);
        String name = i.getStringExtra(TAG_NAME);
        // Starting new intent
        Intent in = new Intent(getApplicationContext(),AllShadowingActivity.class);
        // sending pid to next activity4
        in.putExtra(TAG_MAIL, mail);
        in.putExtra(TAG_NAME, name);

        // starting new activity and expecting some response back
        startActivityForResult(in, 100);

    }*/

    public void viewTrack(View view)  {

        MoreAccess();

    }

    public void viewWebsite(View view)  {

        TextView headi =(TextView) findViewById(R.id.namp);
        TextView linki =(TextView) findViewById(R.id.nwebsite);

        String head = headi.getText().toString()+" Website";
        String link ="http://"+linki.getText().toString().trim();
        // Starting new intent
        Intent in = new Intent(getApplicationContext(),FullScreenBrowser.class);
        // sending pid to next activity4
        in.putExtra(KEY_LINK, link);
        in.putExtra(KEY_HEAD, head);

        // starting new activity and expecting some response back
        startActivityForResult(in, 100);

    }

    public void viewTwitter(View view)  {

        TextView headi =(TextView) findViewById(R.id.namp);
        TextView linki =(TextView) findViewById(R.id.nfollow);

        String head = headi.getText().toString()+" Twitter Page";
        String link ="https:/www.twitter.com/"+linki.getText().toString().trim();
        // Starting new intent
        Intent in = new Intent(getApplicationContext(),FullScreenBrowser.class);
        // sending pid to next activity4
        in.putExtra(KEY_LINK, link);
        in.putExtra(KEY_HEAD, head);


        // starting new activity and expecting some response back
        startActivityForResult(in, 100);

    }

    public void viewEvent(View view)  {

        MoreAccess();

    }

    public void viewBlog(View view)  {

        MoreAccess();

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Main Buttons

    public void viewnewmessage(View view)  {

        MoreAccess();

    }

    public void viewmention(View view)  {

        MoreAccess();

    }

    public void viewshadow(View view)  {

        String testi = ((TextView) findViewById(R.id.ntestm)).getText().toString();

        if(Integer.parseInt(testi)==1 ){
            Unshadowuser();
        }else{

            ShadowUser("Shadow");
        }


    }

    public void viewStatuses(View view)  {

        MoreAccess();

    }

    private void Unshadowuser() {

        String name = ((TextView) findViewById(R.id.nname)).getText().toString();

        ///Create back dialoag box
        AlertDialog.Builder alt_bld =new AlertDialog.Builder(this);
        alt_bld.setMessage("Are you sure you want to unshadow this user?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id){
                        // Loading products in Background Thread
                        ShadowUser("UnShadow");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog alert = alt_bld.create();
        alert.setTitle("Unshadow " + name + " ?");
        //alert.setIcon(R.drawable.ic_launcher);
        alert.show();
    }


    private void ShadowUser(final String shadval) {
        // getting intent data
        Intent i = getIntent();
        // Get XML values from previous intent
        String maili = "&shadowing="+i.getStringExtra(TAG_MAIL);

        final ImageView shadbut = (ImageView) findViewById(R.id.btnNShadow);
        shadbut.setClickable(false);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        String username = db.getUserName();

        Log.v("address", URL_SHADOW + username + maili + "&shadowval=" + shadval);
        //Check internet connection
        if (DetectConnection
                .checkInternetConnection(FarmerPortfolio.this)) {

            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
                    URL_SHADOW+username+maili+"&shadowval="+shadval, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    VolleyLog.d(TAG, "Response: " + response.toString());


                    // check for success tag
                    try {
                        int success = response.getInt("success");

                        if (success == 1) {


                            TextView testi = (TextView) findViewById(R.id.ntestm);

                            String type = null;

                            if(shadval =="UnShadow") {

                                shadbut.setImageResource(R.drawable.ic_shadow);
                                testi.setText("0");
                                type = "No more Shadowing";
                                shadbut.setClickable(true);
                            }
                            if(shadval =="Shadow"){

                                shadbut.setImageResource(R.drawable.ic_unshadow);
                                testi.setText("1");
                                type="Now Shadowing";
                                shadbut.setClickable(true);
                            }
                            //show success toast
                            Toast alert = Toast.makeText(FarmerPortfolio.this,
                                    type,
                                    Toast.LENGTH_LONG);

                            alert.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                            alert.setGravity(Gravity.TOP, 0, 130);
                            alert.show();


                            Intent i = getIntent();
                            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                            String username = db.getUserName();
                            // getting intent data

                            // Get XML values from previous intent
                            String mail = i.getStringExtra(TAG_MAIL);
                            String mymail = "&mymail="+username;


                            //Clear old url
                            AppController.getInstance().getRequestQueue().getCache().remove(URL_FEED + mail + mymail);

                        } else {
                            //show success toast
                            Toast alert = Toast.makeText(FarmerPortfolio.this,
                                    "Opps!! Action unsuccessful",
                                    Toast.LENGTH_LONG);

                            alert.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                            alert.setGravity(Gravity.TOP, 0, 130);
                            alert.show();

                            shadbut.setClickable(true);
                            // failed to create product
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        shadbut.setClickable(true);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }


        else {
            Toast alert = Toast.makeText(FarmerPortfolio.this,
                    R.string.message_Ninternet,
                    Toast.LENGTH_LONG);

            alert.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
            alert.setGravity(Gravity.TOP,0,130);
            alert.show();

            // AllStatusFragment.this.startActivity(new Intent(
            // Settings.ACTION_WIRELESS_SETTINGS));
        }
    }

    public void viewImage(View view)  {

        String imageref = ((TextView)findViewById(R.id.nimageorg)).getText().toString();
        // Starting  intent
        Intent image = new Intent(getApplicationContext(),FullScreenViewActivity.class);
        // sending pid to next activity
        image.putExtra("img", imageref);

        // starting new activity and expecting some response back
        startActivity(image);

    }





    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonSuccess(JSONObject response) {
        TextView testiv = ((TextView) findViewById(R.id.ntestm));
        String testi = ((TextView) findViewById(R.id.ntestm)).getText().toString();
        ImageView btntest = (ImageView) findViewById(R.id.btnNShadow);

        try {

            // Checking for SUCCESS TAG
            int success = response.getInt("success");
            String success1 = response.getString("success");


            Log.v("success", success1);
            if (success == 1) {

                if(Integer.parseInt(testi)==1 ){

                    testiv.setText("0");
                    btntest.setImageResource(R.drawable.ic_shadow);

                }else{

                    testiv.setText("1");
                    btntest.setImageResource(R.drawable.ic_unshadow);

                }

		/*Toast alert = Toast.makeText(NotePadActivity.this,
			       R.string.message_Shadow,
			       Toast.LENGTH_LONG);

			     alert.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
			     alert.setGravity(Gravity.TOP,0,130);
			     alert.show();	*/
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void openlogin() {
        // user is not logged in show login screen
        Intent login = new Intent(getApplicationContext(), LoginActivity.class);
        //login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);
        // Closing dashboard screen
        //finish();

    }

    public void Searchi() {
        // user is not logged in show login screen
        Intent in = new Intent(getApplicationContext(), FarmerPortfolio.class);
        //login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
        // Closing dashboard screen
        //finish();

    }


    public void MoreAccess(){
        Intent intent = getApplicationContext().getPackageManager().getLaunchIntentForPackage("za.co.megadose");
        if (intent != null) {
            // We found the activity now start the activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
        } else {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + "za.co.megadose"));
            getApplicationContext().startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        userFunctions = new UserFunctions();
        if (userFunctions.isUserLoggedIn(getApplicationContext())) {

            if (login)
                onRefresh();
        }else {
            finish();
        }
    }
}
