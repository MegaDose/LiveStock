package com.bongi.emobilepastoralism.adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bongi.emobilepastoralism.R;
import com.bongi.emobilepastoralism.helper.TouchImageView;

import java.io.File;
import java.util.ArrayList;

public class FullScreenImageAdapter extends PagerAdapter {

    private Activity _activity;
    private ArrayList<String> _imagePaths;
    private LayoutInflater inflater;

    // constructor
    public FullScreenImageAdapter(Activity activity,
                                  ArrayList<String> imagePaths) {
        this._activity = activity;
        this._imagePaths = imagePaths;
    }

    @Override
    public int getCount() {
        return this._imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final TouchImageView imgDisplay;
        TextView btnClose , btnDelete;
        ImageView btnShare, btnRotRight, btnRotLeft, btnUpload;

        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);

        imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);
        btnClose = (TextView) viewLayout.findViewById(R.id.btnClose);
        btnDelete = (TextView) viewLayout.findViewById(R.id.btnDelete);
        btnShare = (ImageView) viewLayout.findViewById(R.id.btnShare);
        btnUpload = (ImageView) viewLayout.findViewById(R.id.btnUpload);
        btnRotLeft = (ImageView) viewLayout.findViewById(R.id.btnRotLeft);
        btnRotRight = (ImageView) viewLayout.findViewById(R.id.btnRotRight);

        /*BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(_imagePaths.get(position), options);
        */

        final File img = new File(_imagePaths.get(position));
        Log.v("Image file name", String.valueOf(img));
        /*imgDisplay.setImageBitmap(bitmap);*/


        final Matrix matrix = new Matrix();
        BitmapFactory.Options options = new BitmapFactory.Options();

        // down sizing image as it throws OutOfMemory Exception for larger
        // images
        options.inSampleSize = 2;

        final Bitmap[] bitmap = {BitmapFactory.decodeFile(_imagePaths.get(position), options)};

        matrix.postRotate(90);
        bitmap[0] = Bitmap.createBitmap(bitmap[0], 0, 0, bitmap[0].getWidth(), bitmap[0].getHeight(), matrix, true);
        imgDisplay.setImageBitmap(bitmap[0]);



        // close button click event
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _activity.finish();
            }
        });

        // Rotate Left button click event
        btnRotLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matrix.postRotate(-90);
                bitmap[0] = Bitmap.createBitmap(bitmap[0], 0, 0, bitmap[0].getWidth(), bitmap[0].getHeight(), matrix, true);
                imgDisplay.setImageBitmap(bitmap[0]);
            }
        });

        // Rotate Right button click event
        btnRotRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matrix.postRotate(90);
                bitmap[0] = Bitmap.createBitmap(bitmap[0], 0, 0, bitmap[0].getWidth(), bitmap[0].getHeight(), matrix, true);
                imgDisplay.setImageBitmap(bitmap[0]);
            }
        });

        // close button click event
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareImg(String.valueOf(img));
            }
        });


        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }








    public void ShareImg( String filename) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT, "Shared Via @MegaDoseRSA #Selfography");
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filename));
        _activity.startActivity(Intent.createChooser(share, "Share Image"));
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }



}
