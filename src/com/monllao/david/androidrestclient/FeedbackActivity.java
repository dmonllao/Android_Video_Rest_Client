package com.monllao.david.androidrestclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FeedbackActivity extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharing);
        
        String message = getIntent().getStringExtra("message");

    	TextView linkView = (TextView) findViewById(R.id.link);
    	
    	// Set the link to the video
    	linkView.setText(message);
    	linkView.setVisibility(View.VISIBLE);
    	
    	// Set the finished text
    	TextView sharingText = (TextView) findViewById(R.id.share_text);
    	sharingText.setText(R.string.sharing_result);
	}

}
