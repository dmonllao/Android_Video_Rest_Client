package com.monllao.david.androidrestclient.service;

import android.app.IntentService;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * Middle IntentService to group remote petition
 * 
 * @abstract
 */
abstract public class RestService extends IntentService {
	
	Handler handler;
	
	public RestService(String name) {
		super(name);
	}

	/**
	 * Initialises the handler to display toasts
	 */
	public void onCreate() {
		super.onCreate();
		handler = new Handler();
	}

	/**
	 * Show toast message
	 * 
	 * Toast does not run on the main application thread so
	 * @param message
	 */
	protected void showToast(final Context context, final String message) {

		handler.post(new Runnable() {
	        public void run() {
	            Toast.makeText(context, message, Toast.LENGTH_LONG).show();                
	        }
	    });
	}
}
