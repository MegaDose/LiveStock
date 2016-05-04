package com.bongi.emobilepastoralism.imageviewer;

/**
 * Created by Thembisile on 2015/09/03.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bongi.emobilepastoralism.ConfigAdd;
import com.bongi.emobilepastoralism.R;
import com.bongi.emobilepastoralism.app.AppController;
import com.bongi.emobilepastoralism.loginreg.DatabaseHandler;

import org.json.JSONObject;



public class FullScreenViewNotiActivity extends Activity {

    private static final String TAG = FullScreenViewNotiActivity.class.getSimpleName();

    ImageView imgDisplay;
    TextView moreOptions,titleTextView;
    String title,fileName;

    private com.bongi.emobilepastoralism.utils.Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_fullscreen_notification_image);

        final Matrix matrix = new Matrix();
        BitmapFactory.Options options = new BitmapFactory.Options();

        Intent i = getIntent();
        final String img = ConfigAdd.Page_Link + "/LiveStockImages/" + i.getStringExtra("img");
        final String imgOrg = ConfigAdd.Page_Link + "/LiveStockImages/Original/" + i.getStringExtra("img");

        String s = i.getStringExtra("img");
        int position = s.indexOf(".");

        fileName = s.substring(0, position - 1);

        title = getIntent().getStringExtra("title");

        Log.v("sjhf", img);
        moreOptions = (TextView) findViewById(R.id.more);
        imgDisplay = (ImageView) findViewById(R.id.imgDisplay);
        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(title);


        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageLoader.get(img, ImageLoader.getImageListener(imgDisplay, R.drawable.ic_action_loading, R.drawable.ic_action_notfoud));



        utils = new com.bongi.emobilepastoralism.utils.Utils(this);

        // close button click event


        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        String username = db.getUserName();

        String URL_FEED = ConfigAdd.Page_Link+"/android_connect/livestock/update_notifications.php?ref="+i.getStringExtra("ref")+"&mail="+username;

        Log.v("dsfd", URL_FEED);
        // making fresh volley request and getting json
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                URL_FEED, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d(TAG, "Response: " + response.toString());
                if (response != null) {
                    VolleyLog.d(TAG, "Successful notification read");

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





        moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenViewNotiActivity.this.openOptionsMenu();

                PopupMenu popup = new PopupMenu(FullScreenViewNotiActivity.this, moreOptions);

                MenuInflater inflater = popup.getMenuInflater();

                DatabaseHandler db = new DatabaseHandler(FullScreenViewNotiActivity.this);


                String username = db.getUserName();


                popup.getMenu().add(0, v.getId(), 0, "Rotate Left");
                popup.getMenu().add(0, v.getId(), 0, "Rotate Right");
                popup.getMenu().add(0, v.getId(), 0, "Contact");
                popup.getMenu().add(0, v.getId(), 0, "Share");

                //menu.setHeaderTitle("Track Options");
                /*if (db.getRowCount() > 0) {
                    if (mail.trim().equals(username.trim())) {
                        popup.getMenu().add(0, v.getId(), 0, "Remove Image");
                    }
                }*/
                popup.getMenu().add(0, v.getId(), 0, "Close");

                popup.show();


                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getTitle() == "Rotate Left") {

                            //editTrack();

                        } else if (item.getTitle() == "Rotate Right") {

                            // openArtwor();

                        } else if (item.getTitle() == "Contact") {

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

                        } else if (item.getTitle() == "Share") {
                            Share();
                        } else if (item.getTitle() == "Close") {
                            finish();
                        } else {
                            return false;
                        }
                        return true;
                    }


                });

            }
        });
    }


    public void Share() {

        Intent i = getIntent();
        String s = i.getStringExtra("img");
        int position = s.indexOf(".");

        final String fileName = s.substring(0, position - 1);


        Bitmap bitmap = ((BitmapDrawable) imgDisplay.getDrawable())
                .getBitmap();

        String filename = utils.saveShareImageToSDCard(bitmap, fileName);


        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT, "LiveStock Alert Shared Via @MegaDoseRSA #LiveStock");
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filename));
        startActivity(Intent.createChooser(share, "Live Stock Image"));
    }

    public void DownloadImage(){
        Bitmap bitmap = ((BitmapDrawable) imgDisplay.getDrawable())
                .getBitmap();

        utils.saveImageToSDCard(bitmap, fileName);
    }
}

