package com.example.aimimusic;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.app.Application;
import android.content.Context;

public class MusicApplication extends Application{
	private static RequestQueue queue;
	
	@Override
	public void onCreate() {
		super.onCreate();
		queue = Volley.newRequestQueue(getApplicationContext());
	}
	
	public static RequestQueue getRequestQueue(Context context)
	{
		if(queue == null)
			queue = Volley.newRequestQueue(context);
		return queue;
	}
}
