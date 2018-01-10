package com.example.aimimusic.utils;

import android.content.Context;

public class ImgUtils {
	public static int dp2pix(Context context, float dpValue)
	{
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (scale*dpValue+0.5f);
	}
	
	public static String getMusicTime(int duration)
	{
		String time = "";
		if(duration == 0)
		{
			time = "00:00";
		}
		else
		{
			int minute = duration/60;
			int second = duration%60;	
			time = "0"+minute+":"+(second<10 ? "0"+second : second+"");
		}
		return time;	
	}

}
