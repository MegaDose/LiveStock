package com.bongi.emobilepastoralism.loginreg;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bongi.emobilepastoralism.ConfigAdd;
import com.bongi.emobilepastoralism.CustomRequest;
import com.bongi.emobilepastoralism.DetectConnection;
import com.bongi.emobilepastoralism.FullScreenBrowser;
import com.bongi.emobilepastoralism.R;
import com.bongi.emobilepastoralism.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends ActionBarActivity {

    // JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_NAME = "name";
    private static String KEY_KNOWNAS = "amp";
    private static String KEY_LOCATION = "location";
    private static String KEY_ABOUT = "about";
    private static String KEY_INSP = "insp";
    private static String KEY_USERNAME = "username";
    private static String KEY_PASSWORD = "password";
    private static String KEY_GENRE = "genre";
    private static String KEY_SEX = "sex";
    private static String KEY_FOLLOW = "follow";
    private static final String KEY_EMAIL = "email";




    private static String KEY_LINK = "link";
    private static String KEY_HEAD = "head";

    /**
     * The default email to populate the email field with.
     */
    public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";



    // Values for email and password at the time of the login attempt.
    private String mEmail;
    private String mPassword;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private View mLoginStatusView;
    private TextView mLoginStatusMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);



        // Set up the login form.
        mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
        mEmailView = (EditText) findViewById(R.id.email);
        mEmailView.setText(mEmail);






        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int id,
                                                  KeyEvent keyEvent) {
                        if (id == R.id.login || id == EditorInfo.IME_NULL) {
                            attemptLogin();
                            return true;
                        }
                        return false;
                    }
                });

        mLoginFormView = findViewById(R.id.login_form);
        mLoginStatusView = findViewById(R.id.login_status);
        mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

        findViewById(R.id.register_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String head ="Become a member";
                        String link = ConfigAdd.Page_Link+"/Discover/register.php";

                        // Starting new intent
                        Intent in = new Intent(getApplicationContext(),FullScreenBrowser.class);
                        // sending pid to next activity4
                        in.putExtra(KEY_HEAD, head);
                        in.putExtra(KEY_LINK, link);
                        // starting new activity and expecting some response back
                        startActivityForResult(in, 100);
                        finish();
                    }});


        findViewById(R.id.sign_in_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Check internet connection
                        if (DetectConnection
                                .checkInternetConnection(LoginActivity.this)) {

                            // Loading products in Background Thread
                            attemptLogin();

                        } else {
                            Toast alert = Toast.makeText(LoginActivity.this,
                                    R.string.message_Ninternet,
                                    Toast.LENGTH_LONG);

                            alert.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                            alert.setGravity(Gravity.TOP,0,100);
                            alert.show();


                            // AllStatusFragment.this.startActivity(new Intent(
                            // Settings.ACTION_WIRELESS_SETTINGS));
                        }

                    }
                });
    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        mEmail = mEmailView.getText().toString();
        mPassword = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (mPassword.length() < 2) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mEmail)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!mEmail.contains("@")) {
            mEmailView.setError("invalid Password");
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mLoginStatusMessageView.setText("Login");
            showProgress(true);
            login();
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            mLoginStatusView.setVisibility(View.VISIBLE);
            mLoginStatusView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginStatusView.setVisibility(show ? View.VISIBLE
                                    : View.GONE);
                        }
                    });

            mLoginFormView.setVisibility(View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginFormView.setVisibility(show ? View.GONE
                                    : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */


    public void login(){


        String loginURL = ConfigAdd.Page_Link+"/android_connect/livestock/login.php";

        Map<String, String> params = new HashMap<String, String>();
        params.put("mail", mEmail.trim());
        params.put("password", mPassword.trim());

        Log.v("email", mEmail);
        Log.v("password",mPassword);

        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, loginURL, params, new Response.Listener<JSONObject>() {

            @SuppressWarnings("unused")
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response: ", response.toString());
                // check for success tag
                try {
                    if (response != null) {
                        int success = response.getInt(KEY_SUCCESS);

                        if (success == 1) {



                            parseJsonFeed(response);



                            //Regsiter Device to GCM on google servers and main Website
                            DatabaseHandler db = new DatabaseHandler(getApplicationContext());

                            String username = db.getUserName();


                            finish();
                            showProgress(false);
                            finish();

                        } else if (success == 0 || success ==2){

                            showProgress(false);
                            /*Intent dashboard = new Intent(getApplicationContext(), ForgotPassword.class);

                            dashboard.putExtra(KEY_USERNAME, mEmail);
                            dashboard.putExtra("error", "deactive");

                            // Close all views before launching Dashboard
                            dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(dashboard);*/

                            String head ="Password Reset";
                            String link = ConfigAdd.Page_Link+"/Discover/password_recovery/Step_one.php";

                            // Starting new intent
                            Intent in = new Intent(getApplicationContext(),FullScreenBrowser.class);
                            // sending pid to next activity4
                            in.putExtra(KEY_HEAD, head);
                            in.putExtra(KEY_LINK, link);
                            // starting new activity and expecting some response back
                            startActivityForResult(in, 100);





                        }else{

                            showProgress(false);
                            mPasswordView
                                    .setError("Invalid Password");
                            mPasswordView.requestFocus();



                            // failed to create product
                        }
                    }else{
                        //show error message for login
                        ErrorMess();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());

                showProgress(false);

                //show success toast
                Toast alert = Toast.makeText(LoginActivity.this,
                        "Opps!! something went wrong please try again later",
                        Toast.LENGTH_LONG);

                alert.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                alert.setGravity(Gravity.TOP,0,130);
                alert.show();


            }
        });
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("users");
            UserFunctions userFunction = new UserFunctions();
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());

            //for (int i = 0; i < feedArray.length(); i++) {
            for (int i = 0; i < 1; i++) {
                JSONObject json_user = (JSONObject) feedArray.get(i);

                // Clear all previous data in database
                userFunction.logoutUser(getApplicationContext());
                db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_KNOWNAS), json_user.getString(KEY_LOCATION), json_user.getString(KEY_ABOUT), json_user.getString(KEY_INSP),  json_user.getString(KEY_USERNAME),  json_user.getString(KEY_GENRE),  json_user.getString(KEY_SEX),  json_user.getString(KEY_FOLLOW),  json_user.getString(KEY_EMAIL));

            }




        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void ErrorMess(){

        Toast alert = Toast.makeText(LoginActivity.this,"Opps!!! Seems like something went wrong please try Again.",
                Toast.LENGTH_LONG);

        alert.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
        alert.setGravity(Gravity.TOP,0,130);
        alert.show();

    }

    public void viewreset(View view)  {



        // Starting new intent
        Intent in = new Intent(getApplicationContext(),ForgotPassword.class);

        // starting new activity and expecting some response back
        startActivity(in);

        /*/ Starting new intent
        Intent in = new Intent(getApplicationContext(),ForgotPassword.class);
        // sending pid to next activity4


        // starting new activity and expecting some response back
        startActivityForResult(in, 100);*/
    }

    public void viewabout(View view)  {

        String head ="About MegaDose";
        String link =ConfigAdd.Page_Link+"/doc/?about";

        // Starting new intent
        Intent in = new Intent(getApplicationContext(),FullScreenBrowser.class);
        // sending pid to next activity4
        in.putExtra(KEY_HEAD, head);
        in.putExtra(KEY_LINK, link);
        // starting new activity and expecting some response back
        startActivityForResult(in, 100);


    }

    public void viewhelp(View view)  {

        String head ="Help Desk";
        String link =ConfigAdd.Page_Link+"/doc/Help?app&selfo";

        // Starting new intent
        Intent in = new Intent(getApplicationContext(),FullScreenBrowser.class);
        // sending pid to next activity4
        in.putExtra(KEY_HEAD, head);
        in.putExtra(KEY_LINK, link);
        // starting new activity and expecting some response back
        startActivityForResult(in, 100);


    }

    public void viewTC(View view)  {

        String head ="Terms & Conditions";
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

        String head ="Privacy Policy";
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
    public void onBackPressed(){

        finish();

    }



}
