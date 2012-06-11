package com.monllao.david.androidrestclient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Silly activity to display the sharing feedback
 * 
 * Includes a link to the URI
 */
public class FeedbackActivity extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharing);
        
        Log.v(AndroidRestClientActivity.APP_NAME, "Feedback activity - onCreate");
	}

}
