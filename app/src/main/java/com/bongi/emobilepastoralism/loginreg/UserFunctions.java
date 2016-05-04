/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.bongi.emobilepastoralism.loginreg;


import android.content.Context;

public class UserFunctions {
	
	/**
	 * Function get Login status
	 * */
	public boolean isUserLoggedIn(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		int count = db.getRowCount();
		if(count > 0){
			// user logged in
			return true;
		}
		return false;
	}
	
	/**
	 * Function to logout user
	 * Reset Database
	 * */
	public boolean logoutUser(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		db.resetTables();
		return true;
	}



}
