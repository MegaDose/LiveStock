package com.bongi.emobilepastoralism;

/**
 * Created by Thembisile on 2015-05-06.
 */

public class ConfigAdd {
    // File upload url (replace the ip with your server address)
    public static final String Page_Link = "http://10.2.34.47/megadose";
    //public static final String Page_Link = "http://www.megadose.co.za";
    //public static final String Page_Link = "http://192.168.43.152/megadose";
    //public static final String Page_Link ="http://10.50.196.104/megadose";
    //public static final String Page_Link = "http://192.168.0.102/megadose";

    public static final String FILE_UPLOAD_URL = Page_Link+"/android_connect/selfography/AndroidFileUpload/MediaUpload.php";

    public static long getMinutesDifference(long timeStart,long timeStop){
        long diff = timeStop - timeStart;
        long diffMinutes = diff / (60 * 1000);

        return  diffMinutes;
    }

}