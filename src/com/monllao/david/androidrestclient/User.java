package com.monllao.david.androidrestclient;

import java.io.IOException;
import java.io.Serializable;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;

import com.monllao.david.androidrestclient.service.AddServerUserService;
import com.monllao.david.androidrestclient.service.GetServerUserService;

/**
 * Representation of the mobile user
 * 
 * A POJO with context depedencies, also manages the user creation
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

	private int id;
	private String username;
	private String password;
	private String email;
	private int timecreated;
	private int lastaccess;
	

	/**
	 * Constructor
	 * 
	 * Used for Jackson conversion
	 */
	public User() {
	}

	/**
	 * Constructor
	 * 
	 * Used for Jackson conversion
	 * 
	 * @param id
	 * @param username
	 * @param email
	 * @param timecreated
	 * @param lastaccess
	 */
	public User(int id, String username, String email, int timecreated, int lastaccess) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.timecreated = timecreated;
		this.lastaccess = lastaccess;
	}
	
	
	/**
	 * New activity constructor
	 * @param context
	 * @throws NotFoundException
	 * @throws IOException
	 */
	public User(Context context) throws NotFoundException, IOException {
		init(context);
	}

	
	/**
	 * Initialises the object with the activity context
	 * @param context
	 */
	public void init(Context context) throws NotFoundException, IOException {
		initEmail(context);
	}
	
	
	/**
	 * Retrieves / creates the user from/on the server
	 * 
	 * After setting up the email and the password
	 * @param context
	 */
	protected void getUserFromServer(Context context) {
		
		SharedPreferences prefs = context.getSharedPreferences(AndroidRestClientActivity.APP_NAME, 0);
		
		// If there is no user let's create it
		if (prefs.getInt("userid", 0) == 0 || prefs.getString("password", "") == "") {
			AddServerUser(context);
			
	    // Retrieve the server user
		} else {
			this.setId(prefs.getInt("userid", 0));  // To call the REST GET method
			GetServerUser(context);
		}

	}
	
	
	/**
	 * Gets the system google account from the system accounts
	 * @param context
	 */
	private void initEmail(Context context) {
		
		// TODO Add a selector
		AccountManager aManager = AccountManager.get(context);
		Account[] accounts = aManager.getAccountsByType("com.google");
		for (Account account : accounts) {
			this.setEmail(account.name);
			this.setUsername(account.name);
		}

		initPassword(context);
	}
	
	
    /**
     * Get the phone user pwd (the same for all the accounts)
     * 
     * If the password was not set it will ask for a password
     *  
     * @param context The context where the dialog should be displayed
     * @return The user password
     */
    private void initPassword(final Context context) {
    	
    	final SharedPreferences prefs = context.getSharedPreferences(AndroidRestClientActivity.APP_NAME, 0);
    	
    	password = prefs.getString("password", "");
    	
    	// Request a new pwd if it is the first access
    	if (password == "") {
    		
    		// Alert dialog
    		AlertDialog.Builder alert = new AlertDialog.Builder(context);
    		alert.setTitle(R.string.set_pwd);
    		alert.setMessage(R.string.set_pwd_info);
    		
    		// Input text
    		final EditText input = new EditText(context);
    		input.setTransformationMethod(new PasswordTransformationMethod());
    		alert.setView(input);
    		
    		// Listener
    		alert.setPositiveButton(R.string.set_pwd_button, new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				SharedPreferences.Editor editor = prefs.edit();
    				editor.putString("password", input.getText().toString());
    				editor.commit();
    				
    				// Now we can create the new user
    				getUserFromServer(context);
    			}
    		});
    		    		
    		alert.show();
    		
    	// Get the user from the server
    	} else {
    		getUserFromServer(context);
    	}

    	// If the user doesn't set up a password it can continue
    }


    /**
     * Sends a petition to create the user on the server
     *   
     * @throws IOException 
     * @throws NotFoundException
     * @param context 
     */
	public void AddServerUser(Context context) {

        // addServerUser Intent
        Intent serverUser = new Intent(context, AddServerUserService.class);
        serverUser.putExtra("user", this);
        context.startService(serverUser);
	}

	
	/**
	 * Sends a petition to get the server user
	 * @param context
	 */
	public void GetServerUser(Context context) {

        // getServerUser Intent
        Intent serverUser = new Intent(context, GetServerUserService.class);
        serverUser.putExtra("user", this);
        context.startService(serverUser);
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public int getTimecreated() {
		return timecreated;
	}

	public void setTimecreated(int timecreated) {
		this.timecreated = timecreated;
	}

	public int getLastaccess() {
		return lastaccess;
	}

	public void setLastaccess(int lastaccess) {
		this.lastaccess = lastaccess;
	}
	
	
}
