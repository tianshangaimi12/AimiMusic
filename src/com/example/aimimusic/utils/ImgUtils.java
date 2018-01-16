package com.example.aimimusic.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

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
	
	/**
	 * 添加状态栏占位视图
	 *
	 * @param activity
	 */
	public static void addStatusViewWithColor(Activity activity, int color) {
	    ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);    
	    View statusBarView = new View(activity);
	    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
	            getStatusBarHeight(activity));
	    statusBarView.setBackgroundColor(color);
	    contentView.addView(statusBarView, lp);
	}
	
	/**
	* 利用反射获取状态栏高度
	* @return
	*/
	public static int getStatusBarHeight(Activity activity) {
	  int result = 0;
	  //获取状态栏高度的资源id
	  int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
	  if (resourceId > 0) {
	      result = activity.getResources().getDimensionPixelSize(resourceId);
	  }
	  return result;
	}
}
