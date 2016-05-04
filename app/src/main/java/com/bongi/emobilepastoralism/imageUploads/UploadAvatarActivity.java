package com.bongi.emobilepastoralism.imageUploads;
import android.app.ActionBar;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bongi.emobilepastoralism.R;
import com.bongi.emobilepastoralism.locationClasses.GPSService;
import com.bongi.emobilepastoralism.loginreg.DatabaseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Thembisile on 2015-05-06.
 */


public class UploadAvatarActivity extends ActionBarActivity {
    // LogCat tag
    private static final String TAG = AvatarCaptureActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private String filePath = null;
    private TextView txtPercentage;
    private ImageView imgPreview;
    private VideoView vidPreview;
    private Button btnUpload;
    long totalSize = 0;
    Toolbar toolbar;

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    int id = 10000003;
    TextView nearby;
    EditText Editcaption;
    String location,caption= " ";
    Spinner inputType;

    // Progress Dialog
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        vidPreview = (VideoView) findViewById(R.id.videoPreview);
        nearby = (TextView) findViewById(R.id.currentLocation);
        Editcaption = (EditText) findViewById(R.id.editcaption);
        inputType = (Spinner) findViewById(R.id.inputOptions);
        // get action bar
        ActionBar actionBar = getActionBar();

        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);
        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Uploading New Alert");

        // Receiving the data from previous activity
        Intent i = getIntent();

        // image or video path that is captured in previous activity
        filePath = i.getStringExtra("filePath");

        // boolean flag to identify the media type, image or video
        boolean isImage = i.getBooleanExtra("isImage", true);

        if (filePath != null) {
            // Displaying the image or video on the screen
            previewMedia(isImage);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }

        loadSavedPreferences();
        findnearby();
        addItemsOnOptions();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_main, menu);
        return true;

    }

    //add values into spinners
    public void addItemsOnOptions(){


        List<String> list= new ArrayList<String>();
        list.add("Water Availability");
        list.add("Grassland");
        list.add("Weather Information");
        list.add("Travel Routes");
        list.add("Market Point");
        list.add("Other");



        ArrayAdapter<String> dataAdaper = new ArrayAdapter<String>(this ,
                android.R.layout.simple_spinner_item, list);
        dataAdaper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputType.setAdapter(dataAdaper);
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
                 UploadAvatarActivity.this.finish();
                return true;
            case R.id.action_upload:


                mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mBuilder = new NotificationCompat.Builder(UploadAvatarActivity.this);
                mBuilder.setContentTitle("Alert upload")
                        .setContentText("Upload in progress")
                        .setSmallIcon(R.drawable.ic_action_upload);

                new UploadFileToServer().execute();
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /**
     * Displaying captured image/video on the screen
     * */
    private void previewMedia(boolean isImage) {
        // Checking whether captured media is image or video
        if (isImage) {
            imgPreview.setVisibility(View.VISIBLE);
            vidPreview.setVisibility(View.GONE);
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // down sizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 2;

            final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

            imgPreview.setImageBitmap(bitmap);
        } else {
            imgPreview.setVisibility(View.GONE);
            vidPreview.setVisibility(View.VISIBLE);
            vidPreview.setVideoPath(filePath);
            // start playing
            vidPreview.start();
        }
    }

    /**
     * Uploading the file to server
     * */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();

            // failed to create image upload
            Toast alert = Toast.makeText(UploadAvatarActivity.this,
                    "Alert uploading review notification to view upload progress.",
                    Toast.LENGTH_LONG);

            alert.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
            alert.setGravity(Gravity.TOP, 0, 130);
            alert.show();

            // Displays the progress bar for the first time.
            mBuilder.setProgress(100, 0, false);
            mNotifyManager.notify(id, mBuilder.build());
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
                /*/ Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");*/

            // Update progress
            mBuilder.setProgress(100, progress[0], false);
            mNotifyManager.notify(id, mBuilder.build());
        }

        @Override
        protected String doInBackground(Void... params) {

            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(ConfigAvatar.FILE_UPLOAD_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filePath);

                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                String username = db.getUserName();

                caption = Editcaption.getText().toString();

                // Adding file data to http body
                entity.addPart("uploadedfile", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("mail",new StringBody(username));
                entity.addPart("id",new StringBody(String.valueOf(id)));
                entity.addPart("location",new StringBody(location));
                entity.addPart("caption",new StringBody(caption));


                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);

            mBuilder.setContentText("NotePad image upload successful");
            // Removes the progress bar
            mBuilder.setProgress(0, 0, false);
            mNotifyManager.notify(id, mBuilder.build());

            int success = Integer.parseInt(result.trim());

            if (success == 1) {



                //show success toast
                Toast alert = Toast.makeText(UploadAvatarActivity.this,
                        "Image Upload Successfully Posted",
                        Toast.LENGTH_LONG);

                alert.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                alert.setGravity(Gravity.TOP,0,130);
                alert.show();

                // successfully created product


                // closing this screen
                finish();
            } else {

                imgPreview.setVisibility(View.VISIBLE);
                //vidPreview.setVisibility(View.VISIBLE);


                // failed to create image upload
                Toast alert = Toast.makeText(UploadAvatarActivity.this,
                        "Opps!! something went wrong, Please try again.",
                        Toast.LENGTH_LONG);

                alert.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                alert.setGravity(Gravity.TOP,0,130);
                alert.show();
            }


            super.onPostExecute(result);
        }

    }



    public void closeView(View view){
        UploadAvatarActivity.this.finish();
    }

    private void findnearby() {

        pDialog = new ProgressDialog(UploadAvatarActivity.this);
        pDialog.setMessage("Updating Current Location...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        String address = "";
        GPSService mGPSService = new GPSService(UploadAvatarActivity.this);
        mGPSService.getLocation();

        if (mGPSService.isLocationAvailable == false) {

            // Here you can ask the user to try again, using return; for that
            Toast.makeText(UploadAvatarActivity.this, "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();

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
        pDialog.hide();
    }


    //Functions Used to find current location and add it to information being uploaded with the alert
    private void loadSavedPreferences() {

        String address ="No Address";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(UploadAvatarActivity.this);
        address = sharedPreferences.getString("Current_Location", "No Address");
        nearby.setText(address);
        location = address;
    }

    private void SavedPreferences(String address) {

        try{
            if(address.substring(0,12).equals("IO Exception")){
                address= "location not found";
            }
        } catch (Exception ignore){

        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(UploadAvatarActivity.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Current_Location", address);
        editor.apply();
        nearby.setText(address);
        location = address;
    }

    public void Search(View v){
        findnearby();
        pDialog.hide();
    }


}

