package com.monllao.david.androidrestclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class VideoDataActivity extends Activity {

	private EditText titleText;
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_data);

        titleText = (EditText) findViewById(R.id.title);
        
        Button confirmButton = (Button) findViewById(R.id.set_video_data);
        
        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            	
            	Bundle bundle = new Bundle();
            	bundle.putString("title", titleText.getText().toString());
            	
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }

        });
    }
    

}
