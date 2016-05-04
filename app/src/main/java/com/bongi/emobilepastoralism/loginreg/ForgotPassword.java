package com.bongi.emobilepastoralism.loginreg;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class ForgotPassword extends ActionBarActivity {

	private static String KEY_LINK = "link";
	private static String KEY_HEAD = "head";
	
	// Progress Dialog
	private ProgressDialog pDialog;
	Toolbar toolbar;

	

	// url to create new product
	private static String url_resetpassword = ConfigAdd.Page_Link+"/android_connect/app/resetpassword.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";

	
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);

		
		findViewById(R.id.reset_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View view) {
						
						//Check internet connection
						 if (DetectConnection
							      .checkInternetConnection(ForgotPassword.this)) {
							   		     
							  // Loading products in Background Thread
								   	ResetPassword();
								   	
							    } else {
							     Toast alert = Toast.makeText(ForgotPassword.this,
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

	
	

	private void ResetPassword() {

		EditText edtemail = (EditText) findViewById(R.id.email);

			String email = edtemail.getText().toString();

		pDialog = new ProgressDialog(ForgotPassword.this);
		pDialog.setMessage("Submitting request..");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();




		// Building Parameters
		Map<String, String> params = new HashMap<String, String>();
		params.put("email", email);


		CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, url_resetpassword, params, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				Log.d("Response: ", response.toString());
				boolean val =false;

				// check for success tag
				try {
					int success = response.getInt(TAG_SUCCESS);
					final String mail =response.getString("mail");

					if (success == 1) {


					} else {
						val =true;
						///Create back dialoag box
						AlertDialog.Builder alt_bld =new AlertDialog.Builder(ForgotPassword.this);
						alt_bld.setMessage("Opps! Seems like we can't find you email "+mail+" on our system, would you like to become a member now.")
								.setCancelable(false)
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										openregister(mail);

									}
								})
								.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
										finish();
									}
								});


						AlertDialog alert = alt_bld.create();
						alert.setTitle("Email Error");
						//alert.setIcon(R.drawable.ic_launcher);
						alert.show();


						pDialog.hide();
						// failed to create product
					}
				} catch (JSONException e) {
					e.printStackTrace();

					//show success toast
					Toast alert = Toast.makeText(ForgotPassword.this,
							"Opps!! Seems like something went wrong, Please try again",
							Toast.LENGTH_LONG);

					alert.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
					alert.setGravity(Gravity.TOP,0,130);
					alert.show();

					pDialog.hide();
				}

				if(val==false){
					///Create back dialoag box
					AlertDialog.Builder alt_bld =new AlertDialog.Builder(ForgotPassword.this);
					alt_bld.setMessage("Perfect! We have sent you an email. Please follow the instruction to complete your password resetting.")
							.setCancelable(false)
							.setPositiveButton("Close", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
									finish();
								}
							});


					AlertDialog alert = alt_bld.create();
					//alert.setTitle("Email Error");
					//alert.setIcon(R.drawable.ic_launcher);
					alert.show();


					pDialog.hide();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError response) {
				Log.d("Response: ", response.toString());

				pDialog.hide();
			}
		});
		AppController.getInstance().addToRequestQueue(jsObjRequest);



	}

	public void openregister(String mail){
		String head ="Become a member";
		String link = ConfigAdd.Page_Link+"/MSS/register.php?email="+mail;

		// Starting new intent
		Intent in = new Intent(getApplicationContext(),FullScreenBrowser.class);
		// sending pid to next activity4
		in.putExtra(KEY_HEAD, head);
		in.putExtra(KEY_LINK, link);
		// starting new activity and expecting some response back
		startActivity(in);
		finish();
	}
}
