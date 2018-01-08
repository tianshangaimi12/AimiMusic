package com.example.aimimusic.utils;

import android.content.Context;

public class ImgUtils {
	public static int dp2pix(Context context, float dpValue)
	{
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (scale*dpValue+0.5f);
	}

}
