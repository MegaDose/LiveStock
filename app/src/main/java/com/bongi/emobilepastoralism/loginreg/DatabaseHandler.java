/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.bongi.emobilepastoralism.loginreg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "selfography_api";

	// Login table name
	private static final String TABLE_LOGIN = "login";

	// Login Table Columns names
	private static final String KEY_NAME = "name";
	private static final String KEY_KNOWNAS = "knownas";
	private static final String KEY_LOCATION = "location";
	private static final String KEY_ABOUT = "about";
	private static final String KEY_INSP = "insp";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_GENRE = "genre";
	private static final String KEY_SEX = "sex";
	private static final String KEY_FOLLOW = "follow";
    private static final String KEY_EMAIL = "email";

	private static final String KEY_MESSAGE ="message";


	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
				+ KEY_NAME + " TEXT,"
				+ KEY_KNOWNAS + " TEXT,"
				+ KEY_LOCATION + " TEXT,"
				+ KEY_ABOUT + " TEXT,"
				+ KEY_INSP + " TEXT ,"
				+ KEY_USERNAME+ " TEXT PRIMARY KEY," 
				+ KEY_GENRE + " TEXT,"
				+ KEY_SEX + " TEXT,"
				+ KEY_FOLLOW + " TEXT,"
                + KEY_EMAIL + " TEXT"+")";
		
		
		db.execSQL(CREATE_LOGIN_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

		// Create tables again
		onCreate(db);
	}


    /**
     *Fetching User name of logged in user
     * */

    public String getUserName(){

        String[] columns ={KEY_USERNAME};
        String name =null;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LOGIN,columns,null,null,null,null,null,"1");
        while(cursor.moveToNext()){
            name = cursor.getString(0);
        }

        db.close();
        cursor.close();

        // return user
        return name;

    }

    public String getName(){

        String[] columns ={KEY_NAME};
        String name =null;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LOGIN,columns,null,null,null,null,null,"1");
        while(cursor.moveToNext()){
            name = cursor.getString(0);
        }

        db.close();
        cursor.close();

        // return user
        return name;

    }

    public String getEmail(){

        String[] columns ={KEY_EMAIL};
        String name =null;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LOGIN,columns,null,null,null,null,null,"1");
        while(cursor.moveToNext()){
            name = cursor.getString(0);
        }

        db.close();
        cursor.close();

        // return user
        return name;

    }

    public String getAmp(){

        String[] columns ={KEY_KNOWNAS};
        String name =null;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LOGIN,columns,null,null,null,null,null,"1");
        while(cursor.moveToNext()){
            name = cursor.getString(0);
        }

        db.close();
        cursor.close();

        // return user
        return name;

    }



	/**
	 * Storing user details in database
	 * */
	public void addUser(String name,String knownas,String location,String about,String insp,String username,String genre,String sex,String follow, String email) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name); // Name
		values.put(KEY_KNOWNAS, knownas);
		values.put(KEY_LOCATION, location);
		values.put(KEY_ABOUT, about);
		values.put(KEY_INSP, insp);
		values.put(KEY_USERNAME, username);
		values.put(KEY_GENRE, genre);
		values.put(KEY_SEX, sex);
		values.put(KEY_FOLLOW, follow);
        values.put(KEY_EMAIL, email);
		

		// Inserting Row
		db.insert(TABLE_LOGIN, null, values);
		db.close(); // Closing database connection
	}
	
	/**
	 * Getting user data from database
	 * */
	public HashMap<String, String> getUserDetails(){
		HashMap<String,String> user = new HashMap<String,String>();
		String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;
		 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
        	user.put("name", cursor.getString(1));
        	user.put("knownas", cursor.getString(2));
        	user.put("location", cursor.getString(3));
        	user.put("about", cursor.getString(4));
        	user.put("insp", cursor.getString(5));
      		user.put("username", cursor.getString(6));
      		user.put("genre", cursor.getString(7));
        	user.put("sex", cursor.getString(8));
      		user.put("follow", cursor.getString(9));
            user.put("email", cursor.getString(10));
        	
        }
        cursor.close();
        db.close(); 
		// return user
		return user;
	}
	
    // Getting single contact
    public Member getMember(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_LOGIN,
        		new String[] { null }, KEY_USERNAME + "=?",
                new String[] { username }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        Member member = new Member(cursor.getString(0),cursor.getString(1), cursor.getString(2),cursor.getString(3), cursor.getString(4),cursor.getString(5), cursor.getString(6),cursor.getString(7), cursor.getString(8));
        // return contact
        return member;

    }
	

	/**
	 * Getting user login status
	 * return true if rows are there in table
	 * */
	public int getRowCount() {
		String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int rowCount = cursor.getCount();
		db.close();
		cursor.close();
		
		// return row count
		return rowCount;
	}
	
	/**
	 * Re crate database
	 * Delete all tables and create them again
	 * */
	public void resetTables(){
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_LOGIN, null, null);
		db.close();
	}

}
