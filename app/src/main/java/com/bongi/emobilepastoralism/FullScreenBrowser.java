package com.bongi.emobilepastoralism;



import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;


public class FullScreenBrowser extends ActionBarActivity {

    private ProgressBar progress;
    Toolbar toolbar;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_screen_browser);
		
		
	   	//Check internet connection
			 if (DetectConnection
				      .checkInternetConnection(FullScreenBrowser.this)) {
				
					   	
				    } else {
				    	Toast alert = Toast.makeText(FullScreenBrowser.this,
							       R.string.message_Ninternet,
							       Toast.LENGTH_LONG);
							     
							     alert.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
							     alert.setGravity(Gravity.TOP,0,130);
							     alert.show();
				 
				    // AllStatusFragment.this.startActivity(new Intent(
				      // Settings.ACTION_WIRELESS_SETTINGS));
				    }
		
		
		   // getting intent data
        Intent in = getIntent();

        String KEY_LINK = "link";
        String link = in.getStringExtra(KEY_LINK);
        String KEY_HEAD = "head";
        String head = in.getStringExtra(KEY_HEAD);


        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);
        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(head);


        WebView myweb = (WebView) findViewById(R.id.webView1);

        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.GONE);

        myweb.setWebViewClient(new MyWebViewClient());
		myweb.loadUrl(link);



        myweb.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
		
		WebSettings webSettings = myweb.getSettings();
		webSettings.setJavaScriptEnabled(true);
        
        
        
		
	}

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progress.setVisibility(View.GONE);
            FullScreenBrowser.this.progress.setProgress(100);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progress.setVisibility(View.VISIBLE);
            FullScreenBrowser.this.progress.setProgress(0);
            super.onPageStarted(view, url, favicon);
        }
    }


	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		WebView myweb = (WebView) findViewById(R.id.webView1);
	    // Check if the key event was the Back button and if there's history
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && myweb.canGoBack()) {
	        myweb.goBack();
	        return true;
	    }
	    // If it wasn't the Back key or there's no web page history, bubble up to the default
	    // system behavior (probably exit the activity)
	    return super.onKeyDown(keyCode, event);
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
                WebView myweb = (WebView) findViewById(R.id.webView1);

                if (myweb != null) {
                    myweb.stopLoading();
                }
                FullScreenBrowser.this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }



}
