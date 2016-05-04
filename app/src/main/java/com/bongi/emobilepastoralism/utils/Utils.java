package com.bongi.emobilepastoralism.utils;

/**
 * Created by Thembisile on 2015/11/12.
 */


import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.bongi.emobilepastoralism.R;
import com.bongi.emobilepastoralism.helper.AppConstant;

import java.io.File;
import java.io.FileOutputStream;


public class Utils {
    private String TAG = Utils.class.getSimpleName();
    private Context _context;

    // constructor
    public Utils(Context context) {
        this._context = context;
    }

    /*
     * getting screen width
     */
    @SuppressWarnings("deprecation")
    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) _context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (NoSuchMethodError ignore) {
            // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }

    public void saveImageToSDCard(Bitmap bitmap, String filename) {
        File myDir =new File(android.os.Environment.getExternalStorageDirectory() + File.separator + AppConstant.PHOTOSAVED_ALBUM);


        myDir.mkdirs();
        /*Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);*/
        String fname = "SavedSelfography-" + filename + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(_context,"Image Saved to Gallery", Toast.LENGTH_SHORT).show();
            /*Toast.makeText(
                    _context,
                    _context.getString(R.string.toast_saved).replace("#",
                            "\"" + AppConstant.PHOTOSAVED_ALBUM + "\"")+" gallery folder",
                    Toast.LENGTH_SHORT).show();*/
            Log.d(TAG, "Image saved to: " + file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(_context,
                    _context.getString(R.string.toast_saved_failed),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public String saveShareImageToSDCard(Bitmap bitmap, String filename) {
        File myDir =new File(android.os.Environment.getExternalStorageDirectory() + File.separator + AppConstant.PHOTOSAVED_ALBUM);


        myDir.mkdirs();
        /*Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);*/
        String fname = "SavedSelfography-" + filename + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(_context,
                    _context.getString(R.string.toast_saved_failed),
                    Toast.LENGTH_SHORT).show();
        }

        return file.getAbsolutePath();
    }

    public void setAsWallpaper(Bitmap bitmap) {
        try {
            WallpaperManager wm = WallpaperManager.getInstance(_context);

            wm.setBitmap(bitmap);
            Toast.makeText(_context,
                    _context.getString(R.string.toast_wallpaper_set),
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(_context,
                    _context.getString(R.string.toast_wallpaper_set_failed),
                    Toast.LENGTH_SHORT).show();
        }
    }
}