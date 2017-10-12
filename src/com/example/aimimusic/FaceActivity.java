package com.example.aimimusic;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class FaceActivity extends Activity{
	private TextView mTextView;
	private int index;
	private String title;
	private char[] array;
	private StringBuffer sb = new StringBuffer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_face);
		mTextView = (TextView)findViewById(R.id.txt_face_title);
		title = getResources().getString(R.string.face_activity_title);
		array = title.toCharArray();
		index = 0;
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				sb.append(array[index]);
				mTextView.post(new Runnable() {
					
					@Override
					public void run() {
						mTextView.setText(sb.toString());
					}
				});
				index++;
				if(index>=array.length)
				{
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					this.cancel();
					Intent intent = new Intent(FaceActivity.this,MainActivity.class);
					startActivity(intent);
					FaceActivity.this.finish();
				}
			}
		}, 100,300);
	}
	
}
