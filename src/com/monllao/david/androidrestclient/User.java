package com.monllao.david.androidrestclient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.EditText;

/**
 * Representation of the mobile user
 */
public class User {

	Context context;
	
	/**
	 * User password
	 */
	String pwd;
	

	/**
	 * Application scope preferences
	 */
	SharedPreferences prefs;
	
	public User(Context context) {
		
		this.context = context;
		this.prefs = context.getSharedPreferences(AndroidRestClientActivity.APP_NAME, 0);
		setPwd();
	}

	
	/**
	 * Password getter
	 * @return The user password
	 */
	public String getPwd() {
		return pwd;
	}

	
	
    /**
     * Get the phone user pwd (the same for all the accounts)
     * 
     * @param context The context where the dialog should be displayed
     * @return The user password
     */
    private void setPwd() {
    	
    	pwd = prefs.getString("pwd", "");
    	
    	// Request a new pwd if it is the first access
    	if (pwd == "") {
    		
    		// Alert dialog
    		AlertDialog.Builder alert = new AlertDialog.Builder(context);
    		alert.setTitle(R.string.set_pwd);
    		alert.setMessage(R.string.set_pwd_info);
    		
    		// Input text
    		final EditText input = new EditText(context);
    		alert.setView(input);
    		
    		// Listener
    		alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				SharedPreferences.Editor editor = prefs.edit();
    				editor.putString("pwd", input.getText().toString());
    				editor.commit();
    			}
    		});
    		    		
    		alert.show();
    	}

    	// If no password is provided set to ""
		pwd = prefs.getString("pwd", "");
    }

}
