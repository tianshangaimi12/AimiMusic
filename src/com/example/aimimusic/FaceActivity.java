package com.example.aimimusic;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.aimimusic.utils.HttpUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

public class FaceActivity extends Activity{
	private ImageView mImageView;
	private int width;
	private int height;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_face);
		mImageView = (ImageView)findViewById(R.id.img_bing_pic);
		width = getResources().getDisplayMetrics().widthPixels;
		height = getResources().getDisplayMetrics().heightPixels*4/5;
		getBingPic();
	}
	
	public void getBingPic()
	{
		StringRequest request = new StringRequest(HttpUtils.BING_PIC_URL, 
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Picasso.with(FaceActivity.this)
						.load(response)
						.resize(width, height)
						.into(mImageView,new Callback() {
							
							@Override
							public void onSuccess() {
								mImageView.postDelayed(new Runnable() {
									
									@Override
									public void run() {
										Intent intent = new Intent(FaceActivity.this, MainActivity.class);
										startActivity(intent);
										FaceActivity.this.finish();
									}
								}, 2000);
							}
							
							@Override
							public void onError() {
							}
						});
					}
				}, 
				new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});
		MusicApplication.getRequestQueue(this).add(request);
	}
}
