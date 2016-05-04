package com.bongi.emobilepastoralism.gcm;

import android.content.Context;
import android.content.Intent;

import com.bongi.emobilepastoralism.ConfigAdd;


public final class CommonUtilities {
	
	// give your server registration url here
    static final String SERVER_URL = ConfigAdd.Page_Link+"/android_connect/livestock/gcm/register.php";

    // Google project id
    public static final String SENDER_ID = "";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "LiveStock GCM";

    public static final String DISPLAY_MESSAGE_ACTION =
            "com.androidhive.pushnotifications.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
