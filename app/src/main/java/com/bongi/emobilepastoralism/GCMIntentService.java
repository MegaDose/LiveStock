package com.bongi.emobilepastoralism;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import com.bongi.emobilepastoralism.gcm.ServerUtilities;
import com.bongi.emobilepastoralism.imageviewer.FullScreenViewNotiActivity;
import com.bongi.emobilepastoralism.loginreg.DatabaseHandler;

import static com.bongi.emobilepastoralism.gcm.CommonUtilities.SENDER_ID;
import static com.bongi.emobilepastoralism.gcm.CommonUtilities.displayMessage;


public class GCMIntentService extends GCMBaseIntentService {

private static final String TAG = "GCMIntentService";

private static final String TAG_ID = "id";
private static final String TAG_NAME = "name";
private static final String TAG_DETAILS = "details";
private static final String TAG_TYPE = "type";
private static final String TAG_TIME = "time";
private static final String TAG_MAIL = "mail";
private static final String TAG_REF ="ref";
private static final String TAG_AMP = "amp";



public GCMIntentService() {
    super(SENDER_ID);
}

/**
 * Method called on device registered
 **/
@Override
protected void onRegistered(Context context, String registrationId) {
    Log.i(TAG, "Device registered: regId = " + registrationId);
    //displayMessage(context, "Your device registred with GCM");

    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
    String email = db.getEmail();

    ServerUtilities.register(context, email, registrationId);
}

/**
 * Method called on device un registred
 * */
@Override
protected void onUnregistered(Context context, String registrationId) {
    Log.i(TAG, "Device unregistered");
    //displayMessage(context, getString(R.string.gcm_unregistered));
    ServerUtilities.unregister(context, registrationId);
}

/**
 * Method called on Receiving a new message
 * */
@Override
protected void onMessage(Context context, Intent intent) {
    Log.i(TAG, "Received message");
    String message = "null";
    String file = "null";
    String ref ="0";

    message = intent.getExtras().getString("message");
    file = intent.getExtras().getString("file");
    ref = intent.getExtras().getString("ref");

    //displayMessage(context, message);
    // notifies user
    generateNotification(context, message ,file, ref);
}

/**
 * Method called on receiving a deleted message
 * */
@Override
protected void onDeletedMessages(Context context, int total) {
    Log.i(TAG, "Received deleted messages notification");
    String message = getString(R.string.gcm_deleted, total);
    displayMessage(context, message);
    // notifies user
//        generateNotification(context, message );
}

/**
 * Method called on Error
 * */
@Override
public void onError(Context context, String errorId) {
    Log.i(TAG, "Received error: " + errorId);
    displayMessage(context, getString(R.string.gcm_error, errorId));
}

@Override
protected boolean onRecoverableError(Context context, String errorId) {
    // log message
    Log.i(TAG, "Received recoverable error: " + errorId);
    displayMessage(context, getString(R.string.gcm_recoverable_error,
            errorId));
    return super.onRecoverableError(context, errorId);
}

/**
 * Issues a notification to inform the user that server has sent a message.
 */
private static void generateNotification(Context context, String message ,String file ,String ref) {

    //Set defualt parameters
    int icon = R.drawable.logo;
    long when = System.currentTimeMillis();
    String title = "New Alert";




        Intent notificationIntent = new Intent(context, FullScreenViewNotiActivity.class);
        notificationIntent.putExtra(TAG_REF, ref);
        notificationIntent.putExtra("img", file);


    PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    NotificationCompat.Builder b = new NotificationCompat.Builder(context);

    b.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(icon)
            .setTicker("LiveStock")
            .setContentTitle(title)
            .setContentText(message)
            .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
            .setContentIntent(contentIntent)
            .setContentInfo("Live Stock Alert");


    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.notify(Integer.parseInt(ref), b.build());






}

}
