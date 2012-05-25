package com.monllao.david.androidrestclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.monllao.david.androidrestclient.shares.FacebookShare;

public class ShareActivity extends Activity {

	Video video;
	FacebookShare facebookShare;
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharing);
        
        video = (Video) getIntent().getSerializableExtra("video");
        showLink(video);
        
        String message = video.getName() + " " + video.getUrl();
        
        // Test, only facebook
    	facebookShare = new FacebookShare(this);
    	String feedback = facebookShare.share(message);
    }
    
    
    /**
     * Displays the link to the video
     */
    private void showLink(Video video) {
    	
    	TextView linkView = (TextView) findViewById(R.id.link);
    	
    	linkView.setText(video.getName() + ": " + video.getUrl());
    	linkView.setVisibility(View.VISIBLE);
    }
    
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (facebookShare != null) {
        	facebookShare.end(requestCode, resultCode, data);
        }
    }
    
    /**
     * Finish the application
     */
    public void onBackPressed() {
    	setResult(RESULT_CANCELED);
    	finish();
    }
    
}
